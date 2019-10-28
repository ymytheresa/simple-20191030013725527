package com.bochk.hackathon.api.test.functional.selfservice

import com.bochk.hackathon.api.functional.api.HackathonSelfServiceApi
import com.bochk.hackathon.api.functional.model.Body
import com.bochk.hackathon.api.test.ERROR_CONFIRMATION_REQUIRED
import com.bochk.hackathon.api.test.functional.*
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ResetDbApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(HackathonSelfServiceApi::class.java)
    private val accountUtil = AccountUtil()
    private val appointmentUtil = AppointmentUtil()
    private val creditCardUtil = CreditCardUtil()
    private val investmentUtil = InvestmentUtil()

    @Disabled
    @Test
    fun `should reset DB if confirmed`() {
        val ibmAppUserAuthCode = "cust3103"
        val ibmAppUserClientCred = "00000000-0000-0000-0000-000000000000"

        val accountNo1 = "1287513333331"
        val accountNo2 = "1268813333332"
        val accountNo3 = "1288213333333"

        val creditCard1 = "4163180000002852"
        val creditCard2 = "5228650000008331"
        val creditCard3 = "6250400000006771"

        val body = Body().areYouSure(Body.AreYouSureEnum.YES)
        val response = api.internalResetDbPost(ibmAppUserAuthCode, body).execute()

        assert(response.isSuccessful)

        assertEquals(0, accountUtil.getTransactionCount(ibmAppUserAuthCode, accountNo1))
        assertEquals(0, accountUtil.getTransactionCount(ibmAppUserAuthCode, accountNo2))
        assertEquals(0, accountUtil.getTransactionCount(ibmAppUserAuthCode, accountNo3))

        assertNull(appointmentUtil.getAppointment(ibmAppUserClientCred, "68686868", "0"))

        assertEquals(Pair(500000.0, 0.0), creditCardUtil.getCardCredit(ibmAppUserAuthCode, creditCard1))
        assertEquals(Pair(500000.0, 0.0), creditCardUtil.getCardCredit(ibmAppUserAuthCode, creditCard2))
        assertEquals(Pair(500000.0, 0.0), creditCardUtil.getCardCredit(ibmAppUserAuthCode, creditCard3))

        assertEquals(0, investmentUtil.getStockQuantity(ibmAppUserAuthCode, "5"))
    }

    @Test
    fun `should not reset DB if rejected`() {
        val ibmAppUser = "cust3103"

        val body = Body().areYouSure(Body.AreYouSureEnum.NO)
        val response = api.internalResetDbPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }


    @Test
    fun `should not reset DB if not confirmed`() {
        val ibmAppUser = "cust3103"

        val body = Body()
        val response = api.internalResetDbPost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_CONFIRMATION_REQUIRED, errorResponse.code)
    }

}
