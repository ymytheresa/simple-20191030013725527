package com.boc.cloud.api.service;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.entity.Account;
import com.boc.cloud.entity.Currency;
import com.boc.cloud.entity.Customer;
import com.boc.cloud.entity.Branch;
import com.boc.cloud.entity.Stock;
import com.cloudant.client.api.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Common validation service for checking api input parameters.
 */

@Service
public class CheckService {
    @Autowired
    private Database db;

    private boolean checkExists(Class<?> clazz, String key, String value, String name, String errorCode) {
        if (value == null || value.trim().length() == 0) {
            throw new Http400Exception(errorCode, MsgKeys.NOT_EXIST, name);
        }

        String selector = String.format("{selector: {type: \"%s\", %s: \"%s\"}}", clazz.getSimpleName(), key, value);
        System.out.println(selector);
        System.out.println(clazz.getSimpleName());
        List<?> result = db.findByIndex(selector, clazz);
        if (result.size() == 0) {
            throw new Http400Exception(errorCode, MsgKeys.NOT_EXIST, name);
        }

        return true;
    }

    public void checkAcno(String value, String errorCode) {
        checkExists(Account.class, "account_no", value, "account_no", errorCode);
    }

    /**
     * check currency is HKD
     */
    public void checkHKCurrency(String currency) {
        if (!"HKD".equals(currency)) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.PERSONAL_LOAN_CURRENCY_HKD);
        }
    }

    /**
     * 检查输入金额是否大于0
     */
    public void checkAmount(Double value) {
        if (value == null) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_GT, "amount", "0");
        }

        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal amtBig = new BigDecimal(value);
        if (amtBig.compareTo(zero) != 1) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_GT, "amount", "0");
        }
    }

    /**
     * 检查交易碼是否存在
     */
    public void checkStockCode(String value, String errorCode) {
        checkExists(Stock.class, "stock_code", value, "stock_code", errorCode);
    }

    /**
     * 输入必填
     */
    public void isRequired(String value, String name) {
        if (String.valueOf(value).equals("") || value == null) {
            throw new Http400Exception(MsgKeys.ERROR_INPUT_REQUIRED, MsgKeys.INPUT_REQUIRED, name);
        }
    }

    /**
     * 檢查輸入幣種是否存在
     */
    public void checkCurrency(String value, String errorCode) {
        checkExists(Currency.class, "currency_code", value, "currency", errorCode);
    }

    /**
     * 檢查branch-code是否存在
     */
    public void checkBranch(String value, String errorCode) {
        checkExists(Branch.class, "branch_code", value, "Branch code", errorCode);
    }

    /**
     * 檢查customer-id是否存在
     */
    public boolean checkCustomerId(String value, String errorCode) {
        return checkExists(Customer.class, "customer_id", value, "customer_id", errorCode);
    }

}

