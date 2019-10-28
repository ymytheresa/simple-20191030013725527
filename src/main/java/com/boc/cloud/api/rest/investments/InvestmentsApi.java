package com.boc.cloud.api.rest.investments;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.api.utils.Common;
import com.boc.cloud.entity.*;
import com.boc.cloud.model.request.StockTradeRequest;
import com.boc.cloud.model.response.StockTradeResponse;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuxi List investments & Trade stock
 */
@RestController
public class InvestmentsApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Get customer account stock list", notes = "Get customer account stock list", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful")})
    @RequestMapping(value = "/api/investments", method = RequestMethod.GET)
    public List<com.boc.cloud.model.response.Stock> getList(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customerId = getCustomerId(request);
        checkService.checkCustomerId(customerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        Stock_Balance selector = new Stock_Balance();
        selector.setCustomer_id(customerId);
        Common.sleep();
        return db.findByIndex(getSelector(selector), com.boc.cloud.model.response.Stock.class);
    }

    @ApiOperation(value = "stock trading", notes = "stock trading", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "actionRequired / unitPriceRequired / quantityRequired / " +
            "invalidAccountNo / customerMustBeTheSame / invalidStockCode / " +
            "invalidStockUnitPrice / insufficientStock", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/investments/stock", method = RequestMethod.POST)
    public StockTradeResponse getStock(@RequestBody @ApiParam(value = "stock trading input values(JSON)") StockTradeRequest data,
                                       HttpServletRequest request)
        throws Exception {
        Database db = getDb(request);
        String customerId = getCustomerId(request);
        checkService.checkCustomerId(customerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        //check 参数必填
        checkService.isRequired(data.getStock_code(), "Stock code");

        String[] tradeAction = new String[]{"BUY", "SELL"};
        if (!StringUtils.isNotBlank(data.getAction())) {
            throw new Http400Exception(MsgKeys.ERROR_ACTION_REQUIRED, MsgKeys.INPUT_REQUIRED, "action");
        }
        if (!Arrays.asList(tradeAction).contains(data.getAction())) {
            throw new Http400Exception(MsgKeys.ERROR_ACTION_REQUIRED, MsgKeys.INPUT_REQUIRED, "action");
        }
        if (data.getUnit_price() == null) {
            throw new Http400Exception(MsgKeys.ERROR_UNIT_PRICE_REQUIRED, MsgKeys.INPUT_REQUIRED, "unit_price");
        }
        if (data.getQuantity() == null) {
            throw new Http400Exception(MsgKeys.ERROR_QUANTITY_REQUIRED, MsgKeys.INPUT_REQUIRED, "quantity");
        }
        checkService.isRequired(data.getSettlement_account_no(), "account_no");
        checkService.checkAmount(data.getUnit_price());
        checkService.checkAmount(data.getQuantity().doubleValue());

        //check 交易帳戶是否存在
        checkService.checkAcno(data.getSettlement_account_no(), MsgKeys.ERROR_INVALID_ACCOUNT_NO);

        // check if the account belongs to the customer
        Account account = new Account();
        account.setAccount_no(data.getSettlement_account_no());
        Common.sleep();
        List<Account> accountList = db.findByIndex(getSelector(account), Account.class);
        String accountCustomerId = accountList.get(0).getCustomer_id();
        if (!accountCustomerId.equals(customerId)) {
            throw new Http400Exception(MsgKeys.ERROR_CUSTOMER_MUST_BE_THE_SAME, MsgKeys.SETTLEMENT_ACCOUNT_NOT_BELONG_TO_CUSTOMER);
        }

        // check stock_code 是否存在
        String stockCode;
        try {
            stockCode = String.valueOf(Integer.parseInt(data.getStock_code()));
        } catch (NumberFormatException ex) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_STOCK_CODE, MsgKeys.MUST_BE_NUMERIC, "Stock price");
        }
        checkService.checkStockCode(stockCode, MsgKeys.ERROR_INVALID_STOCK_CODE);
        // check unit_price
        Stock stockSelector = new Stock();
        stockSelector.setStock_code(stockCode);
        Common.sleep();
        Stock stockResult = db.findByIndex(getSelector(stockSelector), Stock.class).get(0);
        if (data.getUnit_price() < Double.parseDouble(stockResult.getPrice()) * 0.8 || data.getUnit_price() > Double.parseDouble(stockResult.getPrice()) * 1.2) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_STOCK_UNIT_PRICE, MsgKeys.STOCK_UNIT_PRICE_NOT_MATCH);
        }

        // check 账户余额是否够
        BigDecimal b1 = new BigDecimal(Double.toString(data.getUnit_price()));// 单位股
        BigDecimal b2 = new BigDecimal(Long.toString(data.getQuantity()));// 数量
        Double price = b1.multiply(b2).doubleValue();
        BigDecimal priceResult = new BigDecimal(Double.toString(price));
        Account_Balance balance = findBalance(data, db);
        BigDecimal amtBig = new BigDecimal(balance.getBalance());
        Double balanceAfter = null;
        String action = data.getAction();

        if (action.equals("BUY")) {
            // 如果是buy 检查账户余额是否够
            checkPrice(data, priceResult, db);
            // 更新account_balance 表
            Double d1 = amtBig.subtract(priceResult).doubleValue();
            balanceAfter = d1;
            balance.setBalance(Double.toString(d1));
            Common.sleep();
            db.update(balance);
            // insert to Invest_Txn
            saveInvestTxn(data, price, balance, "BUY", db);
            // save to Account_Txn
            saveAccountTxn(data, price, d1, true, db);
            // update Stock_Balance
            this.updateStockBalance(customerId, stockCode, data.getQuantity(), "BUY", db);
        } else if (action.equals("SELL")) {
            Stock_Balance selector = new Stock_Balance();
            selector.setCustomer_id(customerId);
            selector.setCode(stockCode);
            List<Stock_Balance> stickList = db.findByIndex(getSelector(selector), Stock_Balance.class);
            if (stickList.size() <= 0) {
                throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_STOCK, MsgKeys.STOCK_IS_NOT_ENOUGH);
            }

            // 更新account_balance 表
            Double d2 = amtBig.add(priceResult).doubleValue();
            balanceAfter = d2;
            balance.setBalance(Double.toString(d2));
            // update Stock_Balance
            this.updateStockBalance(customerId, stockCode, data.getQuantity(), "SELL", db);
            Common.sleep();
            db.update(balance);
            // insert to Invest_Txn
            saveInvestTxn(data, price, balance, "SELL", db);
            // save to Account_Txn
            saveAccountTxn(data, price, d2, false, db);
        }
        StockTradeResponse output = new StockTradeResponse();
        output.setSettlement_account_balance_after(balanceAfter);
        return output;
    }

    private void saveAccountTxn(StockTradeRequest data, Double price, Double afterBalance, boolean isSell, Database db) {
        String stockCode = String.valueOf(Integer.parseInt(data.getStock_code()));
        Account_Txn accountTxn = new Account_Txn();
        String dataTime = now();
        accountTxn.setDatetime(dataTime);
        accountTxn.setAccount_no(data.getSettlement_account_no());
        accountTxn.setTxn_type("STOCK");
        accountTxn.setCurrency("HKD");
        if (isSell) {
            accountTxn.setAmount(Double.toString(-price));
        } else {
            accountTxn.setAmount(Double.toString(price));
        }
        accountTxn.setAfter_balance(Double.toString(afterBalance));
        accountTxn.setRemark(data.getRemark());
        accountTxn.setType("Account_Txn");
        accountTxn.setStock_code(stockCode);
        Common.sleep();
        db.save(accountTxn);
    }

    private void saveInvestTxn(StockTradeRequest data, Double price, Account_Balance balance, String action, Database db) {
        String stockCode = String.valueOf(Integer.parseInt(data.getStock_code()));
        Stock stock = new Stock();
        stock.setStock_code(stockCode);
        Common.sleep();
        Stock stockObj = db.findByIndex(getSelector(stock), Stock.class).get(0);
        String peRatio = stockObj.getPe_ratio();
        Invest_Txn txn = new Invest_Txn();
        String dataTime = now();
        txn.setDate_time(dataTime);
        txn.setAccount_id(balance.getAccount_id());
        txn.setAction(action);
        txn.setCurrency("HKD");
        txn.setCode(stockCode);
        txn.setPrice(Double.toString(data.getUnit_price()));
        txn.setQuantity(Double.toString(data.getQuantity()));
        txn.setAmount(Double.toString(price));
        txn.setPe_ratio(peRatio);
        txn.setRemark(data.getRemark());
        txn.setType("Invest_Txn");
        Common.sleep();
        db.save(txn);
    }

    private void checkPrice(StockTradeRequest data, BigDecimal priceResult, Database db) {
        Account_Balance balance = findBalance(data, db);
        BigDecimal amtBig = new BigDecimal(balance.getBalance());

        if (priceResult.compareTo(amtBig) > 0) {
            throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_ACCOUNT_BALANCE, MsgKeys.TX_INSUF_BAL);
        }
    }

    private Account_Balance findBalance(StockTradeRequest data, Database db) {
        Account account = new Account();
        account.setAccount_no(data.getSettlement_account_no());
        Common.sleep();
        Account obj = db.findByIndex(getSelector(account), Account.class).get(0);

        Account_Balance balanceObj = new Account_Balance();
        balanceObj.setAccount_id(obj.getAccount_id());
        balanceObj.setCurrency("HKD");
        Common.sleep();
        return db.findByIndex(getSelector(balanceObj), Account_Balance.class).get(0);
    }

    private void updateStockBalance(String customerId, String stock_code, Long quantity, String action, Database db) {
        Stock_Balance stockSelector = new Stock_Balance();
        stockSelector.setCustomer_id(customerId);
        stockSelector.setCode(stock_code);

        Common.sleep();
        List<Stock_Balance> balanceList = db.findByIndex(getSelector(stockSelector), Stock_Balance.class);
        Stock_Balance stockObj;
        if (CollectionUtils.isEmpty(balanceList)) {
            stockObj = new Stock_Balance();
            stockObj.setType("Stock_Balance");
            stockObj.setCustomer_id(customerId);
            stockObj.setCode(stock_code);
            if ("BUY".equals(action)) {
                stockObj.setQuantity(Long.toString(quantity));
            }

            Common.sleep();
            db.save(stockObj);
        } else {
            stockObj = balanceList.get(0);
            BigDecimal originalQuantity = new BigDecimal(stockObj.getQuantity());
            BigDecimal inputQuantity = new BigDecimal(quantity);
            if ("BUY".equals(action)) {
                stockObj.setQuantity((originalQuantity.add(inputQuantity)).toString());
            } else if ("SELL".equals(action)) {
                if (inputQuantity.compareTo(originalQuantity) > 0) {
                    throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_STOCK, MsgKeys.STOCK_IS_NOT_ENOUGH);
                }
                stockObj.setQuantity((originalQuantity.subtract(inputQuantity)).toString());
            }
            Common.sleep();
            db.update(stockObj);
        }
    }
}
