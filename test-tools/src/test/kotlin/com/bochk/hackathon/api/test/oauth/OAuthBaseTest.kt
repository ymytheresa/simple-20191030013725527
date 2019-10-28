package com.bochk.hackathon.api.test.oauth

import com.bochk.hackathon.api.oauth.ApiClient
import com.bochk.hackathon.api.oauth.auth.OAuth
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.oltu.oauth2.client.request.OAuthClientRequest
import org.apache.oltu.oauth2.common.message.types.GrantType
import org.junit.jupiter.api.BeforeAll
import org.slf4j.bridge.SLF4JBridgeHandler
import java.lang.UnsupportedOperationException
import java.util.concurrent.TimeUnit

abstract class OAuthBaseTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            // Optionally remove existing handlers attached to j.u.l root logger
            SLF4JBridgeHandler.removeHandlersForRootLogger()  // (since SLF4J 1.6.5)

            // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
            // the initialization phase of your application
            SLF4JBridgeHandler.install()
        }

        // test
        //private const val baseUrl = "https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/api/"
        // prod
        private const val baseUrl = "https://api.au.apiconnect.ibmcloud.com/bochkhackathon-2018/sandbox/api/"

        private const val connectTimeout = 5L
        private const val readTimeout = 300L
        private const val writeTimeout = 5L

        // -----------------------------------------------------------------------------------------------------------------
        // OAuth (API Connect)

        // test
        //private const val oauthBaseUrl = "https://api.au.apiconnect.ibmcloud.com/hkboc-hackathon/dev/oauth2/"
        // prod
        private const val oauthBaseUrl = "https://api.au.apiconnect.ibmcloud.com/bochkhackathon-2018/sandbox/oauth2/"
        private const val authorizationUrl = "${oauthBaseUrl}authorize"
        private const val tokenUrl = "${oauthBaseUrl}token"

        private const val scope = "all"

        // test
        //const val clientId = "3e5a53ba-1123-4df0-98ab-70c09dcf81f5"
        //const val clientSecret = "mX3fV7sO4dS4cD4oY2lX8qD6vH6wX7sG7oI2dV2mC6oW2dV3pD"
        // prod
        private const val clientId = "cf4de22c-6849-4f28-a2cd-390368d8aa39"
        private const val clientSecret = "Y5lW5iL1dN6dS8wH3bQ7iB2gU2fU3hQ3fH7aR2gY2hJ0kT3gD0"

        private const val username = "cust3103"
        private const val password = "bochk"

        private const val redirectUri = "https://www.example.com"     // must be the same as the "OAuth Redirection URL" configured in API Connect portal

        // -----------------------------------------------------------------------------------------------------------------

        fun <T> getApiService(serviceClass: Class<T>, grantType: GrantType? = null): T {
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val apiClient = if (grantType != null) {
                val authorization = when (grantType) {
                    GrantType.CLIENT_CREDENTIALS -> {
                        val oauthOkHttpClient = OkHttpClient.Builder()
                                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                                .readTimeout(readTimeout, TimeUnit.SECONDS)
                                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                                .addInterceptor(logging)
                                .authenticator { route, response ->
                                    val credential = Credentials.basic(clientId, clientSecret)
                                    response.request().newBuilder().header("Authorization", credential).build()
                                }
                                .build()
                        val tokenRequestBuilder = OAuthClientRequest.TokenRequestBuilder(tokenUrl)
                                .setGrantType(GrantType.CLIENT_CREDENTIALS)
                                .setScope(scope)
                        OAuth(oauthOkHttpClient, tokenRequestBuilder)
                    }
                    GrantType.AUTHORIZATION_CODE -> {
                        val oauthOkHttpClient = OkHttpClient.Builder()
                                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                                .readTimeout(readTimeout, TimeUnit.SECONDS)
                                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                                .addInterceptor(logging)
                                .authenticator { route, response ->
                                    val credential = Credentials.basic(clientId, clientSecret)
                                    response.request().newBuilder().header("Authorization", credential).build()
                                }
                                .build()
                        val tokenRequestBuilder = OAuthClientRequest.TokenRequestBuilder(tokenUrl)
                                .setGrantType(GrantType.AUTHORIZATION_CODE)
                                .setScope(scope)
                                .setCode(requestAuthCode())
                        OAuth(oauthOkHttpClient, tokenRequestBuilder)
                    }
                    else -> throw UnsupportedOperationException("Unsupported grant type")
                }
                ApiClient().addAuthorization("My OAuth", authorization)
            } else {
                ApiClient()
            }

            val apiOkHttpClient = apiClient.okBuilder
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build()

            return apiClient.adapterBuilder
                    .baseUrl(baseUrl)
                    .client(apiOkHttpClient)
                    .build()
                    .create(serviceClass)
        }

        private fun requestAuthCode(): String {
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build()

            val url = OAuthClientRequest.AuthenticationRequestBuilder(authorizationUrl)
                    .setClientId(clientId)
                    .setRedirectURI(redirectUri)
                    .setResponseType("code")
                    .setScope(scope)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .setParameter("login", "true")
                    .buildQueryMessage()
                    .locationUri

            val request = Request.Builder()
                    .url(url)
                    .build()

            val call = okHttpClient.newCall(request)

            val response = call.execute()
            val redirectUrl = response.header("Location")
            return redirectUrl?.let { """.*[^\w]code=([^&]+).*""".toRegex().matchEntire(it)?.groups?.get(1)?.value }!!
        }

    }

}
