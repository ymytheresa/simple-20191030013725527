package com.bochk.hackathon.api.test.functional.accounts

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.functional.model.AccountTransaction
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.functional.model.MoneyTransferRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.AccountUtil
import com.bochk.hackathon.api.test.functional.CustomerUtil
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class MoneyTransferApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java)
    private val accountUtil = AccountUtil()
    private val customerUtil = CustomerUtil()

    private fun assertNoAccountChange(ibmAppUser: String, srcAccountNo: String, dstAccountNo: String, currency: Currency, doTransaction: () -> Unit) {
        // check src balance before
        val srcBalanceBefore = accountUtil.getAccountBalance(ibmAppUser, srcAccountNo, currency)

        // check dst balance before
        val dstBalanceBefore = accountUtil.getAccountBalance(ibmAppUser, dstAccountNo, currency)

        // check src transaction history before
        val transactionCountBefore = accountUtil.getTransactionCount(ibmAppUser, srcAccountNo)

        doTransaction()

        // check src balance after
        val srcBalanceAfter = accountUtil.getAccountBalance(ibmAppUser, srcAccountNo, currency)

        // check dst balance after
        val dstBalanceAfter = accountUtil.getAccountBalance(ibmAppUser, dstAccountNo, currency)

        // check src transaction history after
        val transactionCountAfter = accountUtil.getTransactionCount(ibmAppUser, srcAccountNo)

        assertEquals(srcBalanceBefore, srcBalanceAfter)
        assertEquals(dstBalanceBefore, dstBalanceAfter)
        assertEquals(transactionCountBefore, transactionCountAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should transfer money to an intra-bank account (same customer)`(remark: String) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 10.5
        val customerName = "HEI Ke Song"

        // check src balance before
        val srcBalanceBefore = accountUtil.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertTrue(srcBalanceBefore > amount)

        // check dst balance before
        val dstBalanceBefore = accountUtil.getAccountBalance(ibmAppUser, dstAccountNo, currency)

        val body = MoneyTransferRequest()
                .currency(currency)
                .amount(amount)
                .dstAccountNo(dstAccountNo)
                .remark(remark)
        val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute().body()!!

        // check src balance after
        val srcBalanceAfter = accountUtil.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(srcBalanceBefore.toBigDecimal() - amount.toBigDecimal(), srcBalanceAfter.toBigDecimal())

        // check dst balance after
        val dstBalanceAfter = accountUtil.getAccountBalance(ibmAppUser, dstAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(dstBalanceBefore.toBigDecimal() + amount.toBigDecimal(), dstBalanceAfter.toBigDecimal())

        // check response
        assertEquals(srcBalanceAfter, response.srcBalanceAfter)
        assertEquals(customerUtil.getCustomerInfo(ibmAppUser).fullName, response.dstAccountName) // src and dst account should have the same name, but the API can't get the name of the current customer

        // check src transaction history
        val srcLastTransaction = accountUtil.getLastTransaction(ibmAppUser, srcAccountNo)!!
        assertAll("srcLastTransaction",
                { assertTrue(srcLastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(ChronoUnit.SECONDS.between(srcLastTransaction.datetime, OffsetDateTime.now()) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.TRANSFER, srcLastTransaction.txnType) },
                { assertEquals(srcAccountNo, srcLastTransaction.accountNo) },
                { assertEquals(dstAccountNo, srcLastTransaction.accountNo2) },
                { assertNull(srcLastTransaction.fpsPhoneNo) },
                { assertNull(srcLastTransaction.fpsEmail) },
                { assertNull(srcLastTransaction.fpsId) },
                { assertEquals(currency, srcLastTransaction.currency) },
                { assertEquals(currency, srcLastTransaction.currency2) },
                { assertEquals(-amount, srcLastTransaction.amount) },
                { assertEquals(amount, srcLastTransaction.amount2) },
                { assertEquals(srcBalanceAfter, srcLastTransaction.afterBalance) },
                { assertEquals(dstBalanceAfter, srcLastTransaction.afterBalance2) },
                { assertNull(srcLastTransaction.stockCode) },
                { assertEquals(remark, srcLastTransaction.remark) }
        )

        // check dst transaction history
        val dstLastTransaction = accountUtil.getLastTransaction(ibmAppUser, dstAccountNo)!!
        assertEquals(dstLastTransaction, srcLastTransaction)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should transfer money to an intra-bank account (different customer)`(remark: String) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "1287512222221"
        val currency = Currency.HKD
        val amount = 10.5
        val dstIbmAppUser = "cust3102"

        // check src balance before
        val srcBalanceBefore = accountUtil.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertTrue(srcBalanceBefore > amount)

        // check dst balance before
        val dstBalanceBefore = accountUtil.getAccountBalance(dstIbmAppUser, dstAccountNo, currency)

        val body = MoneyTransferRequest()
                .currency(currency)
                .amount(amount)
                .dstAccountNo(dstAccountNo)
                .remark(remark)
        val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute().body()!!

        // check src balance after
        val srcBalanceAfter = accountUtil.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(srcBalanceBefore.toBigDecimal() - amount.toBigDecimal(), srcBalanceAfter.toBigDecimal())

        // check dst balance after
        val dstBalanceAfter = accountUtil.getAccountBalance(dstIbmAppUser, dstAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(dstBalanceBefore.toBigDecimal() + amount.toBigDecimal(), dstBalanceAfter.toBigDecimal())

        // check response
        assertEquals(srcBalanceAfter, response.srcBalanceAfter)
        assertEquals(accountUtil.getThirdPartyName(ibmAppUser, ThirdPartyNameApiTestFunctional.AccountInfoType.ACCOUNT_NO, dstAccountNo), response.dstAccountName)

        // check transaction history from src's perspective
        val srcLastTransaction = accountUtil.getLastTransaction(ibmAppUser, srcAccountNo)!!
        assertAll("srcLastTransaction",
                { assertTrue(srcLastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(ChronoUnit.SECONDS.between(srcLastTransaction.datetime, OffsetDateTime.now()) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.TRANSFER, srcLastTransaction.txnType) },
                { assertEquals(srcAccountNo, srcLastTransaction.accountNo) },
                { assertEquals(dstAccountNo, srcLastTransaction.accountNo2) },
                { assertNull(srcLastTransaction.fpsPhoneNo) },
                { assertNull(srcLastTransaction.fpsEmail) },
                { assertNull(srcLastTransaction.fpsId) },
                { assertEquals(currency, srcLastTransaction.currency) },
                { assertEquals(currency, srcLastTransaction.currency2) },
                { assertEquals(-amount, srcLastTransaction.amount) },
                { assertEquals(amount, srcLastTransaction.amount2) },
                { assertEquals(srcBalanceAfter, srcLastTransaction.afterBalance) },
                { assertNull(srcLastTransaction.afterBalance2) },
                { assertNull(srcLastTransaction.stockCode) },
                { assertEquals(remark, srcLastTransaction.remark) }
        )

        // check transaction history from dst's perspective
        val dstLastTransaction = accountUtil.getLastTransaction(dstIbmAppUser, dstAccountNo)!!
        assertAll("dstLastTransaction",
                { assertEquals(srcLastTransaction.datetime, dstLastTransaction.datetime) },
                { assertEquals(srcLastTransaction.txnType, dstLastTransaction.txnType) },
                { assertEquals(srcLastTransaction.accountNo, dstLastTransaction.accountNo) },
                { assertEquals(srcLastTransaction.accountNo2, dstLastTransaction.accountNo2) },
                { assertNull(srcLastTransaction.fpsPhoneNo) },
                { assertNull(srcLastTransaction.fpsEmail) },
                { assertNull(srcLastTransaction.fpsId) },
                { assertEquals(srcLastTransaction.currency, dstLastTransaction.currency) },
                { assertEquals(srcLastTransaction.currency2, dstLastTransaction.currency2) },
                { assertEquals(srcLastTransaction.amount, dstLastTransaction.amount) },
                { assertEquals(srcLastTransaction.amount2, dstLastTransaction.amount2) },
                { assertNull(dstLastTransaction.afterBalance) },
                { assertEquals(dstBalanceAfter, dstLastTransaction.afterBalance2) },
                { assertNull(dstLastTransaction.stockCode) },
                { assertEquals(srcLastTransaction.remark, dstLastTransaction.remark) }
        )
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "1268813333331"
        val currency = Currency.HKD
        val amount = 10.5
        val remark = "hello 123"

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, currency, {
            val body = MoneyTransferRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstAccountNo(dstAccountNo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(401, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
        })
    }

    @Test
    fun `should reject if src account is invalid`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "x"
        val dstAccountNo = "1287512222221"
        val currency = Currency.HKD
        val amount = 3.toDouble()
        val remark = ""

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, currency, {
            val body = MoneyTransferRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstAccountNo(dstAccountNo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_ACCOUNT_NO, errorResponse.code)
        })
    }

    @Test
    fun `should reject if dst account is invalid`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "x"
        val currency = Currency.HKD
        val amount = 5.toDouble()
        val remark = ""

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, currency, {
            val body = MoneyTransferRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstAccountNo(dstAccountNo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_DST_ACCOUNT_NOT_FOUND, errorResponse.code)
        })
    }

    @Test
    fun `should reject if src and dst account are the same`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "1268813333332"
        val currency = Currency.HKD
        val amount = 5.toDouble()
        val remark = ""

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, currency, {
            val body = MoneyTransferRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstAccountNo(dstAccountNo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_ACCOUNT_MUST_BE_DIFFERENT, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, -10.0])
    fun `should reject if amount is zero or negative`(amount: Double) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "1287512222221"
        val currency = Currency.HKD
        val remark = ""

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, currency, {
            val body = MoneyTransferRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstAccountNo(dstAccountNo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_AMOUNT, errorResponse.code)
        })
    }

    @Test
    fun `should reject if insufficient balance`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstAccountNo = "1287512222221"
        val currency = Currency.HKD
        val amount = 999999999.99
        val remark = ""

        assertNoAccountChange(ibmAppUser, srcAccountNo, dstAccountNo, currency, {
            val body = MoneyTransferRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstAccountNo(dstAccountNo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INSUFFICIENT_ACCOUNT_BALANCE, errorResponse.code)
        })
    }

}
