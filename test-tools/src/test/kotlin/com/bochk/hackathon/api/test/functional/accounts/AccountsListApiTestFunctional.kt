package com.bochk.hackathon.api.test.functional.accounts

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AccountsListApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java)

    @Test
    fun `should return a list of accounts with balances`() {
        val ibmAppUser = "cust3103"

        val response = api.accountsGet(ibmAppUser).execute()

        assert(response.isSuccessful)
        val accounts = response.body()!!
        assertEquals(3, accounts.size)

        val account = accounts.firstOrNull { it.accountNo == "1268813333332" }!!
        val balances = account.balances

        assertEquals(3, balances.size)
        assertTrue(balances.firstOrNull { it.currency == Currency.HKD }!!.balance > 0)
        assertTrue(balances.firstOrNull { it.currency == Currency.USD }!!.balance > 0)
        assertTrue(balances.firstOrNull { it.currency == Currency.CNY }!!.balance > 0)
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"

        val response = api.accountsGet(ibmAppUser).execute()

        assert(!response.isSuccessful)
        assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

}
