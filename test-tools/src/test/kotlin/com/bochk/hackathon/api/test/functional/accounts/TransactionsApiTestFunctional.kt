package com.bochk.hackathon.api.test.functional.accounts

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.test.ERROR_AUTH_CODE_GRANT_REQUIRED
import com.bochk.hackathon.api.test.ERROR_INVALID_ACCOUNT_NO
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import com.bochk.hackathon.api.test.toErrorResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransactionsApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java)

    @Test
    fun `should return a list of transactions for the account in descending order of timestamp`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1268813333332"
        val response = api.accountsAccountNoTransactionsGet(accountNo, ibmAppUser).execute()

        assert(response.isSuccessful)
        val transactions = response.body()!!
        assert(transactions.isNotEmpty())   // in reality, an account could have empty history

        assertEquals(transactions.sortedByDescending { it.datetime }, transactions)
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val accountNo = "x"

        val response = api.accountsAccountNoTransactionsGet(accountNo, ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if an invalid account no is specified`() {
        val ibmAppUser = "cust3103"
        val accountNo = "x"

        val response = api.accountsAccountNoTransactionsGet(accountNo, ibmAppUser).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        Assertions.assertEquals(ERROR_INVALID_ACCOUNT_NO, errorResponse.code)
    }

}
