package com.bochk.hackathon.api.test.functional.auth

import com.bochk.hackathon.api.functional.api.SecurityApi
import com.bochk.hackathon.api.functional.auth.HttpBasicAuth
import com.bochk.hackathon.api.test.ERROR_INVALID_INPUT
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AuthenticationApiTestFunctional : FunctionalBaseTest() {

    @Test
    fun `should return positive if credentials are correct`() {
        val httpBasicAuth = HttpBasicAuth()
        httpBasicAuth.username = "cust3103"
        httpBasicAuth.password = "bochk"

        val api = getBasicAuthApiService(SecurityApi::class.java, httpBasicAuth)
        val response = api.authGet().execute()

        assert(response.isSuccessful)
    }

    @ParameterizedTest
    @ValueSource(strings = ["x", "cust0000", "cust0001", "cust2200", "cust2201"])
    fun `should return negative if username is incorrect`(username: String) {
        val httpBasicAuth = HttpBasicAuth()
        httpBasicAuth.username = username
        httpBasicAuth.password = "bochk"

        val api = getBasicAuthApiService(SecurityApi::class.java, httpBasicAuth)
        val response = api.authGet().execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

    @Test
    fun `should return negative if password is incorrect`() {
        val httpBasicAuth = HttpBasicAuth()
        httpBasicAuth.username = "cust3103"
        httpBasicAuth.password = "x"

        val api = getBasicAuthApiService(SecurityApi::class.java, httpBasicAuth)
        val response = api.authGet().execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
    }

    @Test
    fun `should return negative if credentials are not provided`() {
        val api = getApiService(SecurityApi::class.java)
        val response = api.authGet().execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_INVALID_INPUT, errorResponse.code)
    }

}
