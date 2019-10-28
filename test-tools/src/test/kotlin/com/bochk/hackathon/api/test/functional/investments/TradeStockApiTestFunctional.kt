package com.bochk.hackathon.api.test.functional.investments

import com.bochk.hackathon.api.functional.api.InvestmentsApi
import com.bochk.hackathon.api.functional.model.AccountTransaction
import com.bochk.hackathon.api.functional.model.Currency
import com.bochk.hackathon.api.functional.model.StockTradeRequest
import com.bochk.hackathon.api.test.*
import com.bochk.hackathon.api.test.functional.FunctionalBaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class TradeStockApiTestFunctional : FunctionalBaseTest() {

    private val api = getApiService(InvestmentsApi::class.java)
    private val accountUtil = com.bochk.hackathon.api.test.functional.AccountUtil()
    private val investmentUtil = com.bochk.hackathon.api.test.functional.InvestmentUtil()

    private fun assertNoStockInventoryChange(ibmAppUser: String, settlementAccountNo: String, stockCode: String, doTransaction: () -> Unit) {
        // check balance before
        val balanceBefore = accountUtil.getAccountBalance(ibmAppUser, settlementAccountNo, Currency.HKD)

        // check stock inventory before
        val stockInventoryBefore = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        // check transaction history before
        val transactionCountBefore = accountUtil.getTransactionCount(ibmAppUser, settlementAccountNo)

        doTransaction()

        // check balance after
        val balanceAfter = accountUtil.getAccountBalance(ibmAppUser, settlementAccountNo, Currency.HKD)

        // check stock inventory after
        val stockInventoryAfter = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        // check transaction history after
        val transactionCountAfter = accountUtil.getTransactionCount(ibmAppUser, settlementAccountNo)

        assertEquals(balanceBefore, balanceAfter)
        assertEquals(stockInventoryBefore, stockInventoryAfter)
        assertEquals(transactionCountBefore, transactionCountAfter)
    }

    @ParameterizedTest
    @ValueSource(strings = ["5", "0005"])
    fun `should buy stock 5 and 0005`(stockCode: String) {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val unitPrice = 78.85
        val quantity = 10L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        // check balance before
        val balanceBefore = accountUtil.getAccountBalance(ibmAppUser, settlementAccountNo, Currency.HKD)

        // check stock inventory before
        val stockInventoryBefore = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        val body = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .settlementAccountNo(settlementAccountNo)
                .remark(remark)
        val response = api.investmentsStockPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!

        // check balance after
        val balanceAfter = accountUtil.getAccountBalance(ibmAppUser, settlementAccountNo, Currency.HKD)

        // check stock inventory after
        val stockInventoryAfter = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        // check response
        assertBigDecimalEqualsAfterRounding(balanceBefore.toBigDecimal() - (unitPrice.toBigDecimal() * quantity.toBigDecimal()), balanceAfter.toBigDecimal())
        assertBigDecimalEqualsAfterRounding(balanceAfter.toBigDecimal(), responseBody.settlementAccountBalanceAfter.toBigDecimal())
        assertEquals(stockInventoryBefore + quantity, stockInventoryAfter)

        // check transaction history
        val lastTransaction = accountUtil.getLastTransaction(ibmAppUser, settlementAccountNo)!!
        assertAll("lastTransaction",
                { assertTrue(lastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(ChronoUnit.SECONDS.between(lastTransaction.datetime, OffsetDateTime.now()) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.STOCK, lastTransaction.txnType) },
                { assertEquals(settlementAccountNo, lastTransaction.accountNo) },
                { assertNull(lastTransaction.accountNo2) },
                { assertNull(lastTransaction.fpsPhoneNo) },
                { assertNull(lastTransaction.fpsEmail) },
                { assertNull(lastTransaction.fpsId) },
                { assertEquals(Currency.HKD, lastTransaction.currency) },
                { assertNull(lastTransaction.currency2) },
                { assertEquals(-(unitPrice * quantity), lastTransaction.amount) },
                { assertNull(lastTransaction.amount2) },
                { assertEquals(balanceAfter, lastTransaction.afterBalance) },
                { assertNull(lastTransaction.afterBalance2) },
                { assertEquals(Integer.parseInt(stockCode).toString(), lastTransaction.stockCode) },
                { assertEquals(remark, lastTransaction.remark) }
        )
    }

    @Test
    fun `should buy stock using different settlement accounts and still get valid stock inventory`() {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "5"
        val unitPrice = 78.85
        val remark = "this is a remark"

        val quantity1 = 10L
        val settlementAccountNo1 = "1287513333331"
        val quantity2 = 12L
        val settlementAccountNo2 = "1268813333332"

        // check stock inventory before
        val stockInventoryBefore = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        // settle using account 1
        val body1 = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity1)
                .settlementAccountNo(settlementAccountNo1)
                .remark(remark)
        val response1 = api.investmentsStockPost(ibmAppUser, body1).execute()
        assert(response1.isSuccessful)

        // settle using account 2
        val body2 = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity2)
                .settlementAccountNo(settlementAccountNo2)
                .remark(remark)
        val response2 = api.investmentsStockPost(ibmAppUser, body2).execute()
        assert(response2.isSuccessful)

        // check stock inventory after
        val stockInventoryAfter = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        // compare inventory
        assertEquals(stockInventoryAfter, stockInventoryBefore + quantity1 + quantity2)
    }

    @ParameterizedTest
    @ValueSource(strings = ["5", "0005"])
    fun `should sell stock 5 and 0005`(stockCode: String) {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.SELL
        val unitPrice = 78.85
        val quantity = 8L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        // check balance before
        val balanceBefore = accountUtil.getAccountBalance(ibmAppUser, settlementAccountNo, Currency.HKD)

        // check stock inventory before
        val stockInventoryBefore = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        val body = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .settlementAccountNo(settlementAccountNo)
                .remark(remark)
        val response = api.investmentsStockPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
        val responseBody = response.body()!!

        // check balance after
        val balanceAfter = accountUtil.getAccountBalance(ibmAppUser, settlementAccountNo, Currency.HKD)

        // check stock inventory after
        val stockInventoryAfter = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        // check response
        assertBigDecimalEqualsAfterRounding(balanceBefore.toBigDecimal() + (unitPrice.toBigDecimal() * quantity.toBigDecimal()), balanceAfter.toBigDecimal())
        assertEquals(balanceAfter, responseBody.settlementAccountBalanceAfter)
        assertBigDecimalEqualsAfterRounding(stockInventoryBefore.toBigDecimal() - quantity.toBigDecimal(), stockInventoryAfter.toBigDecimal())

        // check transaction history
        val lastTransaction = accountUtil.getLastTransaction(ibmAppUser, settlementAccountNo)!!
        assertAll("lastTransaction",
                { assertTrue(lastTransaction.datetime < OffsetDateTime.now()) },
                { assertTrue(ChronoUnit.SECONDS.between(lastTransaction.datetime, OffsetDateTime.now()) < 60) },
                { assertEquals(AccountTransaction.TxnTypeEnum.STOCK, lastTransaction.txnType) },
                { assertEquals(settlementAccountNo, lastTransaction.accountNo) },
                { assertNull(lastTransaction.accountNo2) },
                { assertNull(lastTransaction.fpsPhoneNo) },
                { assertNull(lastTransaction.fpsEmail) },
                { assertNull(lastTransaction.fpsId) },
                { assertEquals(Currency.HKD, lastTransaction.currency) },
                { assertNull(lastTransaction.currency2) },
                { assertEquals(unitPrice * quantity, lastTransaction.amount) },
                { assertNull(lastTransaction.amount2) },
                { assertEquals(balanceAfter, lastTransaction.afterBalance) },
                { assertNull(lastTransaction.afterBalance2) },
                { assertEquals(Integer.parseInt(stockCode).toString(), lastTransaction.stockCode) },
                { assertEquals(remark, lastTransaction.remark) }
        )
    }

    @Test
    fun `should reject stock purchase if customer is invalid`() {
        val ibmAppUser = "x"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "5"
        val unitPrice = 78.85
        val quantity = 5L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        val body = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .settlementAccountNo(settlementAccountNo)
                .remark(remark)
        val response = api.investmentsStockPost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject stock selling if customer is invalid`() {
        val ibmAppUser = "x"
        val action = StockTradeRequest.ActionEnum.SELL
        val stockCode = "5"
        val unitPrice = 78.85
        val quantity = 5L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        val body = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .settlementAccountNo(settlementAccountNo)
                .remark(remark)
        val response = api.investmentsStockPost(ibmAppUser, body).execute()

        assert(!response.isSuccessful)
        Assertions.assertEquals(401, response.code())
        val errorResponse = response.errorBody()!!.toErrorResponse()
        assertEquals(ERROR_AUTH_CODE_GRANT_REQUIRED, errorResponse.code)
    }

    @Test
    fun `should reject stock purchase if stock code is not in our list`() {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "9999"
        val unitPrice = 78.85
        val quantity = 5L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_STOCK_CODE, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [788.08 * 0.79, 788.08 * 1.21])
    fun `should reject stock purchase if stock price is out of 80% to 120% range`(unitPrice: Double) {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "5"
        val quantity = 10L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_STOCK_UNIT_PRICE, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [78.85 * 0.8, 78.85 * 1.2])
    fun `should accept stock purchase if stock price is within 80% to 120% range`(unitPrice: Double) {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "5"
        val quantity = 10L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        val body = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .settlementAccountNo(settlementAccountNo)
                .remark(remark)
        val response = api.investmentsStockPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

    @ParameterizedTest
    @ValueSource(doubles = [788.08 * 0.79, 788.08 * 1.21])
    fun `should reject stock selling if stock price is out of 80% to 120% range`(unitPrice: Double) {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.SELL
        val stockCode = "5"
        val quantity = 2L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INVALID_STOCK_UNIT_PRICE, errorResponse.code)
        })
    }

    @ParameterizedTest
    @ValueSource(doubles = [78.85 * 0.8, 78.85 * 1.2])
    fun `should accept stock selling if stock price is within 80% to 120% range`(unitPrice: Double) {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.SELL
        val stockCode = "5"
        val quantity = 2L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        val body = StockTradeRequest()
                .action(action)
                .stockCode(stockCode)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .settlementAccountNo(settlementAccountNo)
                .remark(remark)
        val response = api.investmentsStockPost(ibmAppUser, body).execute()

        assert(response.isSuccessful)
    }

    @Test
    fun `should reject if selling more stock than available`() {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.SELL
        val stockCode = "5"
        val unitPrice = 78.85
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        // check stock inventory before
        val stockInventoryBefore = investmentUtil.getStockQuantity(ibmAppUser, stockCode)

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(stockInventoryBefore + 1)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INSUFFICIENT_STOCK, errorResponse.code)
        })
    }

    @Test
    fun `should reject stock purchase if insufficient balance`() {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "1"
        val unitPrice = 96.55
        val quantity = 10000000L
        val settlementAccountNo = "1288213333333"
        val remark = "this is a remark"

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_INSUFFICIENT_ACCOUNT_BALANCE, errorResponse.code)
        })
    }

    @Test
    fun `should reject stock purchase if the settlement account does not belong to the customer`() {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.BUY
        val stockCode = "1"
        val unitPrice = 96.55
        val quantity = 10L
        val settlementAccountNo = "1287511111111"
        val remark = "this is a remark"

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CUSTOMER_MUST_BE_THE_SAME, errorResponse.code)
        })
    }

    @Test
    fun `should reject stock selling if the settlement account does not belong to the customer`() {
        val ibmAppUser = "cust3103"
        val action = StockTradeRequest.ActionEnum.SELL
        val stockCode = "1"
        val unitPrice = 96.55
        val quantity = 10L
        val settlementAccountNo = "1287511111111"
        val remark = "this is a remark"

        assertNoStockInventoryChange(ibmAppUser, settlementAccountNo, stockCode, {
            val body = StockTradeRequest()
                    .action(action)
                    .stockCode(stockCode)
                    .unitPrice(unitPrice)
                    .quantity(quantity)
                    .settlementAccountNo(settlementAccountNo)
                    .remark(remark)
            val response = api.investmentsStockPost(ibmAppUser, body).execute()

            assert(!response.isSuccessful)
            Assertions.assertEquals(400, response.code())
            val errorResponse = response.errorBody()!!.toErrorResponse()
            assertEquals(ERROR_CUSTOMER_MUST_BE_THE_SAME, errorResponse.code)
        })
    }

}
