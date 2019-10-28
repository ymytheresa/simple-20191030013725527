package com.bochk.hackathon.api.test.functional

import com.bochk.hackathon.api.functional.ApiClient
import com.bochk.hackathon.api.functional.auth.HttpBasicAuth
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.BeforeAll
import org.slf4j.bridge.SLF4JBridgeHandler
import java.util.concurrent.TimeUnit

abstract class FunctionalBaseTest {

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

        private const val baseUrl = "https://bocapis.mybluemix.net/api/"

        private const val connectTimeout = 5L
        private const val readTimeout = 300L
        private const val writeTimeout = 5L

        fun <T> getApiService(serviceClass: Class<T>): T {
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val apiClient = ApiClient()

            val apiOkHttpClient = apiClient.okBuilder
                    .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor {
                        val request = it.request()
                        val newRequest: Request
                        newRequest = request.newBuilder()
                                .addHeader("x-app-sharedsecret", "oY2EwOTRjb")
                                .build()
                        it.proceed(newRequest)
                    }
                    .build()

            return apiClient.adapterBuilder
                    .baseUrl(baseUrl)
                    .client(apiOkHttpClient)
                    .build()
                    .create(serviceClass)
        }

        fun <T> getBasicAuthApiService(serviceClass: Class<T>, httpBasicAuth: HttpBasicAuth): T {
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val apiClient = ApiClient().addAuthorization("basic", httpBasicAuth)

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

    }

}
