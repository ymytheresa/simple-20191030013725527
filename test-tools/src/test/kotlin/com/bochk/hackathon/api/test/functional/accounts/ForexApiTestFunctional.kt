package com.bochk.hackathon.api.test.functional.accounts

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.functional.model.AccountTransaction
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.functional.model.ForexRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class ForexApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java)
    private val util = com.bochk.hackathon.api.test.functional.AccountUtil()

    private fun assertNoAccountChange(ibmAppUser: String, srcAccountNo: String, dstAccountNo: String, srcCurrency: Currency, dstCurrency: Currency, doTransaction: () -> Unit) {
        // check src balance before
        val srcBalanceBefore = util.getAccountBalance(ibmAppUser, srcAccountNo, srcCurrency)

        // check dst balance before
        val dstBalanceBefore = util.getAccountBalance(ibmAppUser, dstAccountNo, dstCurrency)

        // check src transaction history before
        val transactionCountBefore = util.getTransactionCount(ibmAppUser, srcAccountNo)

        doTransaction()

        // check src balance after
        val srcBalanceAfter = util.getAccountBalance(ibmAppUser, srcAccountNo, srcCurrency)

        // check dst balance after
        val dstBalanceAfter = util.getAccountBalance(ibmAppUser, dstAccountNo, dstCurrency)

        // check src transaction history after
        val transactionCountAfter = util.getTransactionCount(ibmAppUser, srcAccountNo)

        assertEquals(srcBalanceBefore, srcBalanceAfter)
        assertEquals(dstBalanceBefore, dstBalanceAfter)
        assertEquals(transactionCountBefore, transactionCountAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should transfer money to an intra-bank account (same customer) with currency converted`(remark: String) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val rate = 788.08

        // check src balance before
        val srcBalanceBefore = util.getAccountBalance(ibmAppUser, srcAccountNo, srcCurrency)
        assertTrue(srcBalanceBefore > dstAmount * rate / 100)

        // check dst balance before
        val dstBalanceBefore = util.getAccountBalance(ibmAppUser, dstAccountNo, dstCurrency)

        val body = ForexRequest()
                .srcCurrency(srcCurrency)
                .dstAccountNo(dstAccountNo)
                .dstCurrency(dstCurrency)
                .dstAmount(dstAmount)
                .rate(rate)
                .remark(remark)
        val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute().body()!!

        // check src balance after
        val srcBalanceAfter = util.getAccountBalance(ibmAppUser, srcAccountNo, srcCurrency)
        assertBigDecimalEqualsAfterRounding((srcBalanceBefore.toBigDecimal() - (dstAmount.toBigDecimal() * rate.toBigDecimal() / 100.toBigDecimal())).setScale(2, BigDecimal.ROUND_HALF_UP), srcBalanceAfter.toBigDecimal())

        // check dst balance after
        val dstBalanceAfter = util.getAccountBalance(ibmAppUser, dstAccountNo, dstCurrency)
        assertBigDecimalEqualsAfterRounding(dstBalanceBefore.toBigDecimal() + dstAmount.toBigDecimal(), dstBalanceAfter.toBigDecimal())

        // check response
        assertAll("response",
                Executable { assertEquals(srcBalanceAfter, response.srcBalanceAfter) },
                Executable { assertEquals(dstBalanceAfter, response.dstBalanceAfter) },
                Executable { assertEquals(rate, response.rate) }
        )

        // check src transaction history
        val srcLastTransaction = util.getLastTransaction(ibmAppUser, srcAccountNo)!!
        assertAll("srcLastTransaction",
                { assertTrue(srcLastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(ChronoUnit.SECONDS.between(srcLastTransaction.datetime, OffsetDateTime.now()) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.FOREX, srcLastTransaction.txnType) },
                { assertEquals(srcAccountNo, srcLastTransaction.accountNo) },
                { assertEquals(dstAccountNo, srcLastTransaction.accountNo2) },
                { assertNull(srcLastTransaction.fpsPhoneNo) },
                { assertNull(srcLastTransaction.fpsEmail) },
                { assertNull(srcLastTransaction.fpsId) },
                { assertEquals(srcCurrency, srcLastTransaction.currency) },
                { assertEquals(dstCurrency, srcLastTransaction.currency2) },
                { assertBigDecimalEqualsAfterRounding(-(dstAmount.toBigDecimal() * rate.toBigDecimal() / 100.toBigDecimal()).setScale(2, BigDecimal.ROUND_HALF_UP), srcLastTransaction.amount.toBigDecimal()) },
                { assertEquals(dstAmount, srcLastTransaction.amount2) },
                { assertEquals(response.srcBalanceAfter, srcLastTransaction.afterBalance) },
                { assertEquals(response.dstBalanceAfter, srcLastTransaction.afterBalance2) },
                { assertNull(srcLastTransaction.stockCode) },
                { assertEquals(remark, srcLastTransaction.remark) }
        )

        // check dst transaction history
        val dstLastTransaction = util.getLastTransaction(ibmAppUser, dstAccountNo)!!
        assertEquals(srcLastTransaction, dstLastTransaction)
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(401, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
        })
    }

    @Test
    fun `should reject if src account does not belong to the customer`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287511111111"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CUSTOMER_MUST_BE_THE_SAME, errorResponse.code)
        })
    }

    @Test
    fun `should reject if dst account does not belong to the customer`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1287511111111"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CUSTOMER_MUST_BE_THE_SAME, errorResponse.code)
        })
    }

    @Test
    fun `should reject if src account is invalid`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "x"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_ACCOUNT_NO, errorResponse.code)
        })
    }

    @Test
    fun `should reject if dst account is invalid`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333331"
        val dstAccountNo = "x"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_ACCOUNT_NO, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, -10.0])
    fun `should reject if amount is zero or negative`(dstAmount: Double) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_AMOUNT, errorResponse.code)
        })
    }

    @Test
    fun `should reject if insufficient balance`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 999999999999999.toDouble()
        val rate = 788.08
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INSUFFICIENT_ACCOUNT_BALANCE, errorResponse.code)
        })
    }

    @Test
    fun `should reject if src and dst currency are the same`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.HKD
        val dstAmount = 1.0
        val rate = 100.0
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CURRENCY_MUST_BE_DIFFERENT, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [788.08 * 0.79, 788.08 * 1.21])
    fun `should reject if exchange rate is out of 80% to 120% range`(rate: Double) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, srcCurrency, dstCurrency, {
            val body = ForexRequest()
                    .srcCurrency(srcCurrency)
                    .dstAccountNo(dstAccountNo)
                    .dstCurrency(dstCurrency)
                    .dstAmount(dstAmount)
                    .rate(rate)
                    .remark(remark)
            val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_RATE, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [788.08 * 0.8, 788.08 * 1.2])
    fun `should accept if exchange rate is within 80% to 120% range`(rate: Double) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstAccountNo = "1268813333332"
        val srcCurrency = Currency.HKD
        val dstCurrency = Currency.USD
        val dstAmount = 10.5
        val remark = "hello 123"

        val body = ForexRequest()
                .srcCurrency(srcCurrency)
                .dstAccountNo(dstAccountNo)
                .dstCurrency(dstCurrency)
                .dstAmount(dstAmount)
                .rate(rate)
                .remark(remark)
        val response = api.accountsAccountNoForexPost(srcAccountNo, ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

}
