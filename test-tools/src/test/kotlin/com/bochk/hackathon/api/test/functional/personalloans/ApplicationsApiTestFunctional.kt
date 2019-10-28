package com.bochk.hackathon.api.test.functional.personalloans

import com.bochk.hackathon.api.functional.api.PersonalLoansApi
import com.bochk.hackathon.api.functional.model.AccountTransaction
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.functional.model.PersonalLoanApplicationRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.AccountUtil
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class ApplicationsApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(PersonalLoansApi::class.java)
    private val accountUtil = AccountUtil()
    private val personalLoanUtil = PersonalLoanUtil()

    private fun assertNoPersonalLoanApplicationChange(ibmAppUser: String, accountNo: String, currency: Currency, doTransaction: () -> Unit) {
        // check balance before
        val balanceBefore = accountUtil.getAccountBalance(ibmAppUser, accountNo, currency)

        // check transaction history before
        val transactionCountBefore = accountUtil.getTransactionCount(ibmAppUser, accountNo)

        doTransaction()

        // check balance after
        val balanceAfter = accountUtil.getAccountBalance(ibmAppUser, accountNo, currency)

        // check transaction history after
        val transactionCountAfter = accountUtil.getTransactionCount(ibmAppUser, accountNo)

        assertEquals(balanceBefore, balanceAfter)
        assertEquals(transactionCountBefore, transactionCountAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "this is a remark"])
    fun `should apply personal loan`(remark: String) {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        // check balance before
        val balanceBefore = accountUtil.getAccountBalance(ibmAppUser, accountNo, currency)

        val body = PersonalLoanApplicationRequest()
                .accountNo(accountNo)
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .rate(yearlyRate)
                .installmentAmount(installmentAmount)
                .installmentDay(installmentDay)
                .remark(remark)
        val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!

        // check balance after
        val balanceAfter = accountUtil.getAccountBalance(ibmAppUser, accountNo, currency)
        assertBigDecimalEqualsAfterRounding(balanceBefore.toBigDecimal() + amount.toBigDecimal(), balanceAfter.toBigDecimal())

        // check response
        assertAll("response",
                { assertEquals(accountNo, responseBody.accountNo) },
                { assertEquals(currency, responseBody.currency) },
                { assertEquals(yearlyRate, responseBody.rate) },
                { assertEquals(amount, responseBody.amount) },
                { assertEquals(noOfTerms, responseBody.noOfTerms) },
                { assertEquals(installmentAmount, responseBody.installmentAmount) },
                { assertEquals(installmentDay, responseBody.installmentDay) },
                { assertEquals(remark, responseBody.remark) },
                { assertEquals(balanceAfter, responseBody.balanceAfter) }
        )

        // check transaction history
        val lastTransaction = accountUtil.getLastTransaction(ibmAppUser, accountNo)!!
        assertAll("lastTransaction",
                { assertTrue(lastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(ChronoUnit.SECONDS.between(lastTransaction.datetime, OffsetDateTime.now()) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.LOAN, lastTransaction.txnType) },
                { assertEquals(accountNo, lastTransaction.accountNo) },
                { assertNull(lastTransaction.accountNo2) },
                { assertNull(lastTransaction.fpsPhoneNo) },
                { assertNull(lastTransaction.fpsEmail) },
                { assertNull(lastTransaction.fpsId) },
                { assertEquals(currency, lastTransaction.currency) },
                { assertNull(lastTransaction.currency2) },
                { assertEquals(amount.toDouble(), lastTransaction.amount) },
                { assertNull(lastTransaction.amount2) },
                { assertEquals(balanceAfter, lastTransaction.afterBalance) },
                { assertNull(lastTransaction.afterBalance2) },
                { assertNull(lastTransaction.stockCode) },
                { assertEquals(remark, lastTransaction.remark) }
        )
    }

    @Test
    fun `should reject if loan rate is invalid`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L
        val yearlyRate = 1.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_LOAN_RATE, errorResponse.code)
        })
    }

    @Test
    fun `should reject if installment amount is invalid`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = 100L

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_INSTALLMENT_AMOUNT, errorResponse.code)
        })
    }

    @Test
    fun `should reject if customer is invalid`() {
        val ibmAppUser = "x"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(401, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
        })
    }

    @Test
    fun `should reject if the inputted account does not belong to the customer`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1287511111111"
        val currency = Currency.HKD
        val amount = 150000L
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CUSTOMER_MUST_BE_THE_SAME, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(longs = [9999, 1000001])
    fun `should reject if amount is out of the range of 10,000 and 1,000,000`(amount: Long) {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "x"

        val installmentAmount = 123L

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_AMOUNT, errorResponse.code)
        })
    }

    @Test
    fun `should accept if amount is GTE 10,000`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 10000L
        val noOfTerms = 24L
        val yearlyRate = 3.2
        val installmentDay = 14L
        val remark = "x"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        val body = PersonalLoanApplicationRequest()
                .accountNo(accountNo)
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .rate(yearlyRate)
                .installmentAmount(installmentAmount)
                .installmentDay(installmentDay)
                .remark(remark)
        val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

    @Test
    fun `should accept if amount is LTE 1000000`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 1000000L
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "x"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        val body = PersonalLoanApplicationRequest()
                .accountNo(accountNo)
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .rate(yearlyRate)
                .installmentAmount(installmentAmount)
                .installmentDay(installmentDay)
                .remark(remark)
        val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

    @Test
    fun `should reject if currency is not HKD`() {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.CNY
        val amount = 150000L
        val noOfTerms = 24L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_CURRENCY, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(longs = [0, 361])
    fun `should reject if number of terms is out of the range of 1 and 360`(noOfTerms: Long) {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 150000L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        assertNoPersonalLoanApplicationChange(ibmAppUser, accountNo, currency, {
            val body = PersonalLoanApplicationRequest()
                    .accountNo(accountNo)
                    .currency(currency)
                    .amount(amount)
                    .noOfTerms(noOfTerms)
                    .rate(yearlyRate)
                    .installmentAmount(installmentAmount)
                    .installmentDay(installmentDay)
                    .remark(remark)
            val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_NO_OF_TERMS, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(longs = [1, 360])
    fun `should accept if number of terms is within the range of 1 and 360`(noOfTerms: Long) {
        val ibmAppUser = "cust3103"
        val accountNo = "1287513333331"
        val currency = Currency.HKD
        val amount = 150000L
        val yearlyRate = 2.7
        val installmentDay = 14L
        val remark = "this is a remark"

        val installmentAmount = personalLoanUtil.calculateInstallmentAmount(amount, yearlyRate, noOfTerms)

        val body = PersonalLoanApplicationRequest()
                .accountNo(accountNo)
                .currency(currency)
                .amount(amount)
                .noOfTerms(noOfTerms)
                .rate(yearlyRate)
                .installmentAmount(installmentAmount)
                .installmentDay(installmentDay)
                .remark(remark)
        val response = api.personalLoansApplicationsPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

}
