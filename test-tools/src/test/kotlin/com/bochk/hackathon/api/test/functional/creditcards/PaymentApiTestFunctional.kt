package com.bochk.hackathon.api.test.functional.creditcards

import com.bochk.hackathon.api.functional.api.CardOperationsApi
import com.bochk.hackathon.api.functional.model.CreditCardPaymentRequest
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PaymentApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(CardOperationsApi::class.java)
    private val util = com.bochk.hackathon.api.test.functional.CreditCardUtil()

    private fun assertNoCreditLimitChange(ibmAppUser: String, cardNo: String, doTransaction: () -> Unit) {
        // credit used before
        val (creditLimitBefore, creditUsedBefore) = util.getCardCredit(ibmAppUser, cardNo)

        doTransaction()

        // credit used after
        val (creditLimitAfter, creditUsedAfter) = util.getCardCredit(ibmAppUser, cardNo)

        assertEquals(creditUsedBefore, creditUsedAfter)
        assertEquals(creditLimitBefore, creditLimitAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should pay by credit card`(remark: String) {
        val ibmAppUser = "cust3103"
        val cardNo = "4163180000002852"
        val currency = Currency.HKD
        val amount = 5.toDouble()

        // credit used before
        val (creditLimitBefore, creditUsedBefore) = util.getCardCredit(ibmAppUser, cardNo)

        val body = CreditCardPaymentRequest()
                .currency(currency)
                .amount(amount)
                .remark(remark)
        val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!

        // credit used after
        val (creditLimitAfter, creditUsedAfter) = util.getCardCredit(ibmAppUser, cardNo)

        assertBigDecimalEqualsAfterRounding(creditLimitBefore.toBigDecimal() - creditUsedBefore.toBigDecimal() - amount.toBigDecimal(), creditLimitAfter.toBigDecimal() - creditUsedAfter.toBigDecimal())
        assertBigDecimalEqualsAfterRounding(creditLimitAfter.toBigDecimal() - creditUsedAfter.toBigDecimal(), responseBody.creditUnused.toBigDecimal())
    }

    @Test
    fun `should pay by credit card for decimal amount`() {
        val ibmAppUser = "cust3103"
        val cardNo = "4163180000002852"
        val currency = Currency.HKD
        val amount = 5.6789
        val remark = "this is a remark"

        // credit used before
        val (creditLimitBefore, creditUsedBefore) = util.getCardCredit(ibmAppUser, cardNo)

        val body = CreditCardPaymentRequest()
                .currency(currency)
                .amount(amount)
                .remark(remark)
        val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!

        // credit used after
        val (creditLimitAfter, creditUsedAfter) = util.getCardCredit(ibmAppUser, cardNo)

        assertBigDecimalEqualsAfterRounding(creditLimitBefore.toBigDecimal() - creditUsedBefore.toBigDecimal() - amount.toBigDecimal(), creditLimitAfter.toBigDecimal() - creditUsedAfter.toBigDecimal())
        assertBigDecimalEqualsAfterRounding(creditLimitAfter.toBigDecimal() - creditUsedAfter.toBigDecimal(), responseBody.creditUnused.toBigDecimal())
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val cardNo = "4163180000002852" // VISA
        val currency = Currency.HKD
        val amount = 5.toDouble()
        val remark = "this is a remark"

        val body = CreditCardPaymentRequest()
                .currency(currency)
                .amount(amount)
                .remark(remark)
        val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject if card no is invalid`() {
        val ibmAppUser = "cust3103"
        val cardNo = "x"
        val currency = Currency.HKD
        val amount = 5.toDouble()
        val remark = "this is a remark"

        assertNoCreditLimitChange(ibmAppUser, cardNo, {
            val body = CreditCardPaymentRequest()
                    .currency(currency)
                    .amount(amount)
                    .remark(remark)
            val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(404, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CREDIT_CARD_NOT_FOUND, errorResponse.code)
        })
    }

    @Test
    fun `should reject if currency is invalid`() {
        val ibmAppUser = "cust3103"
        val cardNo = "5228650000008331" // MC
        val currency = Currency.HKD
        val amount = 5.toDouble()
        val remark = "this is a remark"

        assertNoCreditLimitChange(ibmAppUser, cardNo, {
            val body = CreditCardPaymentRequest()
                    .currency(currency)
                    .amount(amount)
                    .remark(remark)
            val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(404, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_CURRENCY, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, -10.0])
    fun `should reject if amount is zero or negative`(amount: Double) {
        val ibmAppUser = "cust3103"
        val cardNo = "4163180000002852"
        val currency = Currency.HKD
        val remark = "this is a remark"

        assertNoCreditLimitChange(ibmAppUser, cardNo, {
            val body = CreditCardPaymentRequest()
                    .currency(currency)
                    .amount(amount)
                    .remark(remark)
            val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_AMOUNT, errorResponse.code)
        })
    }

    @Test
    fun `should reject if amount exceeds credit limit`() {
        val ibmAppUser = "cust3103"
        val cardNo = "5228650000008331" // MC
        val currency = Currency.USD
        val amount = 99999999.toDouble()
        val remark = "this is a remark"

        assertNoCreditLimitChange(ibmAppUser, cardNo, {
            val body = CreditCardPaymentRequest()
                    .currency(currency)
                    .amount(amount)
                    .remark(remark)
            val response = api.creditCardsCardNoPaymentPost(cardNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INSUFFICIENT_CREDIT, errorResponse.code)
        })
    }

}
