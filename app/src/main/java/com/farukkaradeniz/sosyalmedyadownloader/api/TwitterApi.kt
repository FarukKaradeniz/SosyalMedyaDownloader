package com.farukkaradeniz.sosyalmedyadownloader.api

import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

/**
 * Created by Faruk Karadeniz on 25.01.2018.
 * Twitter: twitter.com/Omeerfk
 * Github: github.com/FarukKaradeniz
 * LinkedIn: linkedin.com/in/FarukKaradeniz
 * Website: farukkaradeniz.com
 */
class TwitterApi(private val key: String, private val secret: String) {

    private lateinit var twitter: Twitter
    private var isInitialized = false

    private fun initializeTwitter(): Twitter {
        var builder = ConfigurationBuilder()
                .setOAuthConsumerKey(key)
                .setOAuthConsumerSecret(secret)
                .setApplicationOnlyAuthEnabled(true)

        try {
            val token = TwitterFactory(builder.build()).instance.oAuth2Token
            builder = ConfigurationBuilder()
                    .setOAuthConsumerKey(key)
                    .setOAuthConsumerSecret(secret)
                    .setApplicationOnlyAuthEnabled(true)
                    .setOAuth2AccessToken(token.accessToken)
                    .setOAuth2TokenType(token.tokenType)
        }
        catch (e: TwitterException) {
            e.printStackTrace()
        }

        return TwitterFactory(builder.build()).instance
    }

    fun getTweetStatus(id: Long): Status {
        if (!isInitialized) {
            twitter = initializeTwitter()
            isInitialized = true
        }
        return twitter.showStatus(id)
    }



}
