package com.bochk.hackathon.api.test.functional.customer

import com.bochk.hackathon.api.functional.api.CustomerOperationsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProfileApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(CustomerOperationsApi::class.java)

    @Test
    fun `should return customer info`() {
        val ibmAppUser = "cust3103"

        val response = api.customerProfileGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val customerInfo = response.body()!!
        assertEquals("HEI Ke Song", customerInfo.fullName)
        assertEquals("G/F, 3988 Hackathon Street", customerInfo.address1)
        assertEquals("Admiralty, HK", customerInfo.address2)
        assertEquals("Hong Kong", customerInfo.address3)
        assertEquals("93333333", customerInfo.phoneNo)
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"

        val response = api.customerProfileGet(ibmAppUser).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

}
