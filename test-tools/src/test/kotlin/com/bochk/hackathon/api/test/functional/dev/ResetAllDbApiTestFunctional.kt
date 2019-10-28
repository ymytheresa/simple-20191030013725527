package com.bochk.hackathon.api.test.functional.dev

import com.bochk.hackathon.api.functional.api.DevelopersOnlyApi
import com.bochk.hackathon.api.functional.model.Body1
import com.bochk.hackathon.api.test.ERROR_CONFIRMATION_REQUIRED
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ResetAllDbApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(DevelopersOnlyApi::class.java)

    @Disabled
    @Test
    fun `should reset all DB if confirmed`() {
        val body = Body1().areYouSure(Body1.AreYouSureEnum.YES)
        val response = api.internalResetAllDbPost(body).execute()

        assert(response.isSuccessful)
    }

    @Test
    fun `should not reset DB if rejected`() {
        val body = Body1().areYouSure(Body1.AreYouSureEnum.NO)
        val response = api.internalResetAllDbPost(body).execute()

        assert(response.isSuccessful)
    }


    @Test
    fun `should not reset DB if not confirmed`() {
        val body = Body1()
        val response = api.internalResetAllDbPost(body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_CONFIRMATION_REQUIRED, errorResponse.code)
    }

}
