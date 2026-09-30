package com.slashgil.marvel.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.slashgil.marvel.R
import com.slashgil.marvel.data.local.contract.MarvelLocalDataSource
import com.slashgil.marvel.data.mapper.toDomain
import com.slashgil.marvel.data.remote.contract.MarvelRemoteDataSource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AlphabetSyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SyncWorkerEntryPoint {
        fun remoteDataSource(): MarvelRemoteDataSource
        fun localDataSource(): MarvelLocalDataSource
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            SyncWorkerEntryPoint::class.java
        )
        val remoteDataSource = entryPoint.remoteDataSource()
        val localDataSource = entryPoint.localDataSource()

        createNotificationChannels()

        val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var currentIndex = prefs.getInt(KEY_LETTER_INDEX, 0)

        val alphabet = ('a'..'z').toList()
        Log.d(TAG, "Starting AlphabetSyncWorker at index $currentIndex ('${alphabet.getOrNull(currentIndex)}')")

        try {
            runCatching {
                val initialLetter = alphabet.getOrNull(currentIndex)?.uppercase() ?: "A"
                setForeground(createForegroundInfo(initialLetter, currentIndex + 1, alphabet.size))
            }

            while (currentIndex < alphabet.size) {
                val letterChar = alphabet[currentIndex]
                val letter = letterChar.toString()
                val upperLetter = letterChar.uppercase()

                Log.d(TAG, "Syncing characters for query letter: '$letter'")

                runCatching {
                    setForeground(createForegroundInfo(upperLetter, currentIndex + 1, alphabet.size))

                    val response = remoteDataSource.searchCharacters(letter)
                    val domainList = response.results?.map { it.toDomain() } ?: emptyList()
                    if (domainList.isNotEmpty()) {
                        localDataSource.saveCharacters(domainList)
                        Log.d(TAG, "Saved ${domainList.size} characters for letter '$letter'")
                    }
                }.onFailure { e ->
                    Log.w(TAG, "Failed sync for letter '$letter': ${e.message}")
                }

                currentIndex++
                prefs.edit().putInt(KEY_LETTER_INDEX, currentIndex).apply()

                delay(400)
            }

            prefs.edit().putInt(KEY_LETTER_INDEX, 0).apply()
            Log.d(TAG, "Completed full A-Z character sync!")

            showCompletionNotification()

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "AlphabetSyncWorker error: ${e.message}", e)
            Result.retry()
        }
    }

    private fun createForegroundInfo(currentLetter: String, currentProgress: Int, total: Int): ForegroundInfo {
        val title = "Marvel Database Syncing..."
        val content = "Fetching '$currentLetter' characters ($currentProgress/$total)"

        val notification = NotificationCompat.Builder(appContext, PROGRESS_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setProgress(total, currentProgress, false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID_PROGRESS,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID_PROGRESS, notification)
        }
    }

    private fun showCompletionNotification() {
        val notification = NotificationCompat.Builder(appContext, COMPLETE_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Marvel Database Ready ⚡")
            .setContentText("All A-Z superhero character data has been downloaded for offline access!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_ID_COMPLETE, notification)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val progressChannel = NotificationChannel(
                PROGRESS_CHANNEL_ID,
                "Marvel Background Sync Progress",
                NotificationManager.IMPORTANCE_LOW
            )
            val completeChannel = NotificationChannel(
                COMPLETE_CHANNEL_ID,
                "Marvel Background Sync Completion",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(progressChannel)
            notificationManager.createNotificationChannel(completeChannel)
        }
    }

    companion object {
        private const val TAG = "AlphabetSyncWorker"
        private const val PREFS_NAME = "alphabet_sync_prefs"
        private const val KEY_LETTER_INDEX = "current_letter_index"
        const val WORK_NAME = "AlphabetSyncWork"

        private const val PROGRESS_CHANNEL_ID = "marvel_sync_progress_channel"
        private const val COMPLETE_CHANNEL_ID = "marvel_sync_complete_channel"

        private const val NOTIFICATION_ID_PROGRESS = 1001
        private const val NOTIFICATION_ID_COMPLETE = 1002

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<AlphabetSyncWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
