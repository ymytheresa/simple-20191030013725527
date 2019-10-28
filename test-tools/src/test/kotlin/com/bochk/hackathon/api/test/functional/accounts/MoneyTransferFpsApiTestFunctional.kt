package com.bochk.hackathon.api.test.functional.accounts

import com.bochk.hackathon.api.functional.api.AccountOperationsApi
import com.bochk.hackathon.api.functional.model.AccountTransaction
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.functional.model.MoneyTransferFpsRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class MoneyTransferFpsApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(AccountOperationsApi::class.java)
    private val util = com.bochk.hackathon.api.test.functional.AccountUtil()

    private fun assertNoAccountChange(ibmAppUser: String, srcAccountNo: String, currency: Currency, doTransaction: () -> Unit) {
        // check src balance before
        val srcBalanceBefore = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)

        // check src transaction history before
        val transactionCountBefore = util.getTransactionCount(ibmAppUser, srcAccountNo)

        doTransaction()

        // check src balance after
        val srcBalanceAfter = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)

        // check src transaction history after
        val transactionCountAfter = util.getTransactionCount(ibmAppUser, srcAccountNo)

        assertEquals(srcBalanceBefore, srcBalanceAfter)
        assertEquals(transactionCountBefore, transactionCountAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should transfer money to an inter-bank account using phone no`(remark: String) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1268813333332"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.PHONE_NO
        val dstInfo = "91111111"
        val currency = Currency.CNY
        val amount = 3.45

        // check balance before
        val srcBalanceBefore = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertTrue(srcBalanceBefore > amount)

        val body = MoneyTransferFpsRequest()
                .currency(currency)
                .amount(amount)
                .dstInfoType(dstInfoType)
                .dstInfo(dstInfo)
                .remark(remark)
        val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute().body()!!

        // check src balance after
        val srcBalanceAfter = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(srcBalanceBefore.toBigDecimal() - amount.toBigDecimal(), srcBalanceAfter.toBigDecimal())

        // check remaining balance
        assertEquals(srcBalanceAfter, response.srcBalanceAfter)

        // check account name
        assertEquals(util.getThirdPartyName(ibmAppUser, ThirdPartyNameApiTestFunctional.AccountInfoType.valueOf(dstInfoType.value), dstInfo), response.dstAccountName)

        // check transaction history
        val lastTransaction = util.getLastTransaction(ibmAppUser, srcAccountNo)!!
        assertAll("lastTransaction",
                { assertTrue(lastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(lastTransaction.datetime.until(OffsetDateTime.now(), ChronoUnit.SECONDS) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.TRANSFER, lastTransaction.txnType) },
                { assertEquals(srcAccountNo, lastTransaction.accountNo) },
                { assertNull(lastTransaction.accountNo2) },
                { assertEquals(dstInfo, lastTransaction.fpsPhoneNo) },
                { assertNull(lastTransaction.fpsEmail) },
                { assertNull(lastTransaction.fpsId) },
                { assertEquals(currency, lastTransaction.currency) },
                { assertEquals(currency, lastTransaction.currency2) },
                { assertEquals(-amount, lastTransaction.amount) },
                { assertEquals(amount, lastTransaction.amount2) },
                { assertEquals(response.srcBalanceAfter, lastTransaction.afterBalance) },
                { assertNull(lastTransaction.afterBalance2) },
                { assertNull(lastTransaction.stockCode) },
                { assertEquals(remark, lastTransaction.remark) })
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should transfer money to an inter-bank account using email`(remark: String) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.EMAIL
        val dstInfo = "chan_tai_man@bochk.com"
        val currency = Currency.HKD
        val amount = 1.11

        // check balance before
        val srcBalanceBefore = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertTrue(srcBalanceBefore > amount)

        val body = MoneyTransferFpsRequest()
                .currency(currency)
                .amount(amount)
                .dstInfoType(dstInfoType)
                .dstInfo(dstInfo)
                .remark(remark)
        val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute().body()!!

        // check src balance after
        val srcBalanceAfter = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(srcBalanceBefore.toBigDecimal() - amount.toBigDecimal(), srcBalanceAfter.toBigDecimal())

        // check remaining balance
        assertEquals(srcBalanceAfter, response.srcBalanceAfter)

        // check account name
        assertEquals(util.getThirdPartyName(ibmAppUser, ThirdPartyNameApiTestFunctional.AccountInfoType.valueOf(dstInfoType.value), dstInfo), response.dstAccountName)

        // check transaction history
        val lastTransaction = util.getLastTransaction(ibmAppUser, srcAccountNo)!!
        assertAll("lastTransaction",
                { assertTrue(lastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(lastTransaction.datetime.until(OffsetDateTime.now(), ChronoUnit.SECONDS) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.TRANSFER, lastTransaction.txnType) },
                { assertEquals(srcAccountNo, lastTransaction.accountNo) },
                { assertNull(lastTransaction.accountNo2) },
                { assertNull(lastTransaction.fpsPhoneNo) },
                { assertEquals(dstInfo, lastTransaction.fpsEmail) },
                { assertNull(lastTransaction.fpsId) },
                { assertEquals(currency, lastTransaction.currency) },
                { assertEquals(currency, lastTransaction.currency2) },
                { assertEquals(-amount, lastTransaction.amount) },
                { assertEquals(amount, lastTransaction.amount2) },
                { assertEquals(response.srcBalanceAfter, lastTransaction.afterBalance) },
                { assertNull(lastTransaction.afterBalance2) },
                { assertNull(lastTransaction.stockCode) },
                { assertEquals(remark, lastTransaction.remark) }
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should transfer money to an inter-bank account using FPS ID`(remark: String) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.ID
        val dstInfo = "FPS-P00003"
        val currency = Currency.HKD
        val amount = 1.2

        // check balance before
        val srcBalanceBefore = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertTrue(srcBalanceBefore > amount)

        val body = MoneyTransferFpsRequest()
                .currency(currency)
                .amount(amount)
                .dstInfoType(dstInfoType)
                .dstInfo(dstInfo)
                .remark(remark)
        val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute().body()!!

        // check src balance after
        val srcBalanceAfter = util.getAccountBalance(ibmAppUser, srcAccountNo, currency)
        assertBigDecimalEqualsAfterRounding(srcBalanceBefore.toBigDecimal() - amount.toBigDecimal(), srcBalanceAfter.toBigDecimal())

        // check remaining balance
        assertEquals(srcBalanceAfter, response.srcBalanceAfter)

        // check account name
        assertEquals(util.getThirdPartyName(ibmAppUser, ThirdPartyNameApiTestFunctional.AccountInfoType.valueOf(dstInfoType.value), dstInfo), response.dstAccountName)

        // check transaction history
        val lastTransaction = util.getLastTransaction(ibmAppUser, srcAccountNo)!!
        assertAll("lastTransaction",
                { assertTrue(lastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(lastTransaction.datetime.until(OffsetDateTime.now(), ChronoUnit.SECONDS) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.TRANSFER, lastTransaction.txnType) },
                { assertEquals(srcAccountNo, lastTransaction.accountNo) },
                { assertNull(lastTransaction.accountNo2) },
                { assertNull(lastTransaction.fpsPhoneNo) },
                { assertNull(lastTransaction.fpsEmail) },
                { assertEquals(dstInfo, lastTransaction.fpsId) },
                { assertEquals(currency, lastTransaction.currency) },
                { assertEquals(currency, lastTransaction.currency2) },
                { assertEquals(-amount, lastTransaction.amount) },
                { assertEquals(amount, lastTransaction.amount2) },
                { assertEquals(response.srcBalanceAfter, lastTransaction.afterBalance) },
                { assertNull(lastTransaction.afterBalance2) },
                { assertNull(lastTransaction.stockCode) },
                { assertEquals(remark, lastTransaction.remark) }
        )
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.PHONE_NO
        val dstInfo = "32222222"
        val currency = Currency.CNY
        val amount = 3.45
        val remark = "abcde 12345"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(401, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
        })
    }

    @Test
    fun `should reject if src account is wrong`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "x"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.ID
        val dstInfo = "FPS-P00003"
        val currency = Currency.HKD
        val amount = 0.6
        val remark = "aaa"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_ACCOUNT_NOT_FOUND, errorResponse.code)
        })
    }

    @Test
    fun `should reject if dst ID is wrong`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.ID
        val dstInfo = "x"
        val currency = Currency.HKD
        val amount = 2.3
        val remark = "aaa"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_DST_ACCOUNT_NOT_FOUND, errorResponse.code)
        })
    }

    @Test
    fun `should reject if dst phone no is wrong`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.PHONE_NO
        val dstInfo = "x"
        val currency = Currency.HKD
        val amount = 2.3
        val remark = "aaa"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_DST_ACCOUNT_NOT_FOUND, errorResponse.code)
        })
    }

    @Test
    fun `should reject if dst email is wrong`() {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.EMAIL
        val dstInfo = "x"
        val currency = Currency.HKD
        val amount = 2.3
        val remark = "aaa"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_DST_ACCOUNT_NOT_FOUND, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, -10.0])
    fun `should reject if amount is zero or negative`(amount: Double) {
        val ibmAppUser = "cust3103"
        val srcAccountNo = "1287513333331"
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.ID
        val dstInfo = "FPS-P00003"
        val currency = Currency.HKD
        val remark = "aaa"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

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
        val dstInfoType = MoneyTransferFpsRequest.DstInfoTypeEnum.ID
        val dstInfo = "FPS-P00003"
        val currency = Currency.HKD
        val amount = 99999999999999999.toDouble()
        val remark = "aaa"

        assertNoAccountChange(ibmAppUser, srcAccountNo, currency, {
            val body = MoneyTransferFpsRequest()
                    .currency(currency)
                    .amount(amount)
                    .dstInfoType(dstInfoType)
                    .dstInfo(dstInfo)
                    .remark(remark)
            val response = api.accountsAccountNoMoneyTransferFpsPost(srcAccountNo, ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INSUFFICIENT_ACCOUNT_BALANCE, errorResponse.code)
        })
    }

}
