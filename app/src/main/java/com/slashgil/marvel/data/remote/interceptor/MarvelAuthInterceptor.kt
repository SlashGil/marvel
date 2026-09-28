package com.slashgil.marvel.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest

class MarvelAuthInterceptor(
    private val publicKey: String = PUBLIC_KEY,
    private val privateKey: String = PRIVATE_KEY
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val ts = System.currentTimeMillis().toString()
        val hash = calculateHash(ts, privateKey, publicKey)

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("ts", ts)
            .addQueryParameter("apikey", publicKey)
            .addQueryParameter("hash", hash)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    private fun calculateHash(ts: String, privateKey: String, publicKey: String): String {
        val input = "$ts$privateKey$publicKey"
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    companion object {
        // Default public key for Marvel API
        const val PUBLIC_KEY: String = "89073c6833b3762692edb3b0b7da03bd"
        // Default private key placeholder
        const val PRIVATE_KEY: String = "4dfa9ef037ecb96a1df8e9860b7ea4a2b92ef948"
    }
}
