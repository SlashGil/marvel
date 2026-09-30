package com.slashgil.marvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slashgil.marvel.presentation.characters.CharactersScreen
import com.slashgil.marvel.presentation.characters.impl.CharactersViewModel
import com.slashgil.marvel.presentation.splash.SplashScreen
import com.slashgil.marvel.ui.theme.MarvelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isFirstLaunch = savedInstanceState == null

        setContent {
            MarvelTheme {
                var showSplash by rememberSaveable { mutableStateOf(isFirstLaunch) }

                AnimatedContent(
                    targetState = showSplash,
                    transitionSpec = {
                        fadeIn().togetherWith(fadeOut())
                    },
                    label = "SplashToMainTransition"
                ) { isSplash ->
                    if (isSplash) {
                        SplashScreen(
                            onSplashFinished = {
                                showSplash = false
                            }
                        )
                    } else {
                        val state by viewModel.uiState.collectAsStateWithLifecycle()
                        CharactersScreen(
                            state = state,
                            onSearchQueryChanged = viewModel::onSearchQueryChanged,
                            onPublisherSelected = viewModel::onPublisherSelected,
                            onCharacterSelected = viewModel::onCharacterSelected,
                            onDismissDetail = viewModel::onDismissDetail,
                            onRetry = viewModel::loadCharacters
                        )
                    }
                }
            }
        }
    }
}
