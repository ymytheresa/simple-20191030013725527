package com.bochk.hackathon.api.test.functional.personalloans

import com.bochk.hackathon.api.functional.api.PersonalLoansApi
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.functional.model.PersonalLoanCheckRateRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PersonalizedRateApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(PersonalLoansApi::class.java)
    private val util = PersonalLoanUtil()

    /*
    amount_cap	annual_interest_rate
    50000	3.20%
    100000	3%
    1000000	2.70%
     */
    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should return a valid rate when borrowing $150,000`(remark: String) {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L

        val yearlyRate = 2.7

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!
        assertAll("response",
                { assertEquals(currency, responseBody.currency) },
                { assertEquals(amount, responseBody.amount) },
                { assertEquals(noOfTerms, responseBody.noOfTerms) },
                { assertEquals(yearlyRate, responseBody.rate) },
                { assertEquals(util.calculateInstallmentAmount(amount, yearlyRate, noOfTerms), responseBody.installmentAmount) },
                { assertEquals(remark, responseBody.remark) }
        )
    }

    @Test
    fun `should return a valid rate when borrowing $30,000`() {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val amount = 30000L
        val noOfTerms = 24L
        val remark = "xxx"

        val yearlyRate = 3.2

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!
        assertAll("response",
                { assertEquals(currency, responseBody.currency) },
                { assertEquals(amount, responseBody.amount) },
                { assertEquals(noOfTerms, responseBody.noOfTerms) },
                { assertEquals(yearlyRate, responseBody.rate) },
                { assertEquals(util.calculateInstallmentAmount(amount, yearlyRate, noOfTerms), responseBody.installmentAmount) },
                { assertEquals(remark, responseBody.remark) }
        )
    }

    @Test
    fun `should return a valid rate when borrowing $60,000`() {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val amount = 60000L
        val noOfTerms = 24L
        val remark = "xxx"

        val yearlyRate = 3.0

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!
        assertAll("response",
                { assertEquals(currency, responseBody.currency) },
                { assertEquals(amount, responseBody.amount) },
                { assertEquals(noOfTerms, responseBody.noOfTerms) },
                { assertEquals(yearlyRate, responseBody.rate) },
                { assertEquals(util.calculateInstallmentAmount(amount, yearlyRate, noOfTerms), responseBody.installmentAmount) },
                { assertEquals(remark, responseBody.remark) }
        )
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L
        val remark = "this is a remark"

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @ParameterizedTest
    @ValueSource(longs = [9999, 1000001])
    fun `should accept if amount is out of the range of 10,000 and 1,000,000`(amount: Long) {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val noOfTerms = 24L
        val remark = "this is a remark"

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_AMOUNT, errorResponse.code)
    }

    @ParameterizedTest
    @ValueSource(longs = [10000, 1000000])
    fun `should accept if amount is within the range of 10,000 and 1,000,000`(amount: Long) {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val noOfTerms = 24L
        val remark = "this is a remark"

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

    @Test
    fun `should reject if currency is not HKD`() {
        val ibmAppUser = "cust3103"
        val currency = Currency.CNY
        val amount = 150000L
        val noOfTerms = 24L
        val remark = "this is a remark"

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_CURRENCY, errorResponse.code)
    }

    @ParameterizedTest
    @ValueSource(longs = [0, 361])
    fun `should reject if number of terms is out of the range of 1 and 360`(noOfTerms: Long) {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val amount = 150000L
        val remark = "this is a remark"

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(400, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_INVALID_NO_OF_TERMS, errorResponse.code)
    }

    @ParameterizedTest
    @ValueSource(longs = [1, 360])
    fun `should accept if number of terms is within the range of 1 and 360`(noOfTerms: Long) {
        val ibmAppUser = "cust3103"
        val currency = Currency.HKD
        val amount = 150000L
        val remark = "this is a remark"

        val body = PersonalLoanCheckRateRequest()
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .remark(remark)
        val response = api.personalLoansPersonalizedRatePost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

}
