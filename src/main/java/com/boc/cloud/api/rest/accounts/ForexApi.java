package com.boc.cloud.api.rest.accounts;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.api.utils.Common;
import com.boc.cloud.entity.Account;
import com.boc.cloud.entity.Account_Balance;
import com.boc.cloud.entity.Account_Txn;
import com.boc.cloud.entity.FX_Rate;
import com.boc.cloud.model.request.ForexRequest;
import com.boc.cloud.model.response.ForexResponse;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
public class ForexApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;
    private String[] CurrencyRange = new String[]{"HKD", "USD", "CNY", "AUD", "CAD", "CHF", "EUR", "GBB", "JPY", "NZD", "SGD", "THB", "BND", "ZAK", "DKK"};

    @ApiOperation(value = "Perform foreign currency exchange", notes = "Convert one currency to another, and deposit the converted amount to another account of the same customer", consumes = "application/json", produces = "application/json")
    @ApiImplicitParam(name = "account_no", value = "account_no ", defaultValue = "1288211111113", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "inputRequired / dstAccountNoRequired / amountRequired / " +
            "dstCurrencyRequired / rateRequired / srcCurrencyRequired / invalidAmount / " +
            "currencyMustBeDifferent / invalidCurrency / invalidAccountNo / invalidRate / " +
            "insufficientAccountBalance / currencyNotFound / customerMustBeTheSame",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/accounts/{account_no}/forex", method = RequestMethod.POST)
    public ForexResponse saveExchange(@PathVariable String account_no,
                                      @RequestBody @ApiParam(value = "exchange currency input values(JSON)") ForexRequest input, HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customer_id = getCustomerId(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        if ("".equals(input.getDst_account_no()) || input.getDst_account_no() == null) {
            throw new Http400Exception(MsgKeys.ERROR_DST_ACCOUNT_NO_REQUIRED, MsgKeys.INPUT_REQUIRED, "dst_account_no");
        }
        if (String.valueOf(input.getDst_amount()).equals("") || input.getDst_amount() == null) {
            throw new Http400Exception(MsgKeys.ERROR_AMOUNT_REQUIRED, MsgKeys.INPUT_REQUIRED, "dst_amount");
        }
        if ("".equals(input.getDst_currency()) || input.getDst_currency() == null) {
            throw new Http400Exception(MsgKeys.ERROR_DST_CURRENCY_REQUIRED, MsgKeys.INPUT_REQUIRED, "dst_currency");
        }
        if (String.valueOf(input.getRate()).equals("") || input.getRate() == null) {
            throw new Http400Exception(MsgKeys.ERROR_RATE_REQUIRED, MsgKeys.INPUT_REQUIRED, "rate");
        }
        if ("".equals(input.getSrc_currency()) || input.getSrc_currency() == null) {
            throw new Http400Exception(MsgKeys.ERROR_SRC_CURRENCY_REQUIRED, MsgKeys.INPUT_REQUIRED, "src_currency");
        }
        // 金額 不能<=0
        if (Double.parseDouble(input.getDst_amount().toString()) <= 0) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_GT, "Amount", "0");
        }
        // 两个currency不能相同
        if (input.getSrc_currency().equals(input.getDst_currency())) {
            throw new Http400Exception(MsgKeys.ERROR_CURRENCY_MUST_BE_DIFFERENT, MsgKeys.MUST_BE_DIFFERENT, "Source and destination currencies");
        }
        // dst_currency和 src_currency 要在CurrencyRange范围内
        if (!Arrays.asList(CurrencyRange).contains(input.getSrc_currency())) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.IS_OUT_OF_RANGE, "Source currency");
        }
        if (!Arrays.asList(CurrencyRange).contains(input.getDst_currency())) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.IS_OUT_OF_RANGE, "Destination currency");
        }
        // check dst_account是否存在
        checkService.checkAcno(account_no, MsgKeys.ERROR_INVALID_ACCOUNT_NO);
        checkService.checkAcno(input.getDst_account_no(), MsgKeys.ERROR_INVALID_ACCOUNT_NO);

        FX_Rate fx_rate = new FX_Rate();
        fx_rate.setCurrency(input.getDst_currency());
        Common.sleep();
        List<FX_Rate> fx_rate_list = db.findByIndex(getSelector(fx_rate), FX_Rate.class);
        double rate;
        if (fx_rate_list.size() <= 0) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST, "rate");
        }
        rate = Double.parseDouble(fx_rate_list.get(0).getRate());
        if (input.getRate() < rate * 0.8 || input.getRate() > rate * 1.2) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_RATE, MsgKeys.IS_OUT_OF_RANGE, "rate");
        }

        Account_Balance dst_account_balance = new Account_Balance();
        Account_Balance src_account_balance = new Account_Balance();

        Account src_account = new Account();
        src_account.setAccount_no(account_no);
        Common.sleep();
        List<Account> src_account_list = db.findByIndex(getSelector(src_account), Account.class);
        String src_customer_id;
        if (src_account_list == null || src_account_list.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST, "account_no");
        }
        String src_account_id = src_account_list.get(0).getAccount_id();
        src_customer_id = src_account_list.get(0).getCustomer_id();
        Account_Balance src_ab = new Account_Balance();
        src_ab.setAccount_id(src_account_id);
        Common.sleep();
        List<Account_Balance> src_ab_list = db.findByIndex(getSelector(src_ab), Account_Balance.class);
        Boolean hasSrcCurrency = false;
        for (Account_Balance ab : src_ab_list) {
            if (ab.getCurrency().equals(input.getSrc_currency())) {
                src_account_balance = ab;
                hasSrcCurrency = true;
                if (input.getDst_amount() > Common.mul(Double.valueOf(ab.getBalance()), input.getRate())) {
                    throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_ACCOUNT_BALANCE, MsgKeys.TX_INSUF_BAL);
                }
            }
        }
        if (!hasSrcCurrency) {
            throw new Http400Exception(MsgKeys.ERROR_CURRENCY_NOT_FOUND, MsgKeys.NOT_EXIST, "src_currency");
        }

        // 查询 src_account_id
        Account dst_account = new Account();
        dst_account.setAccount_no(input.getDst_account_no());
        Common.sleep();
        List<Account> dst_account_list = db.findByIndex(getSelector(dst_account), Account.class);

        // 如果dst_account_id存在，则找到相应的currency
        if (dst_account_list == null || dst_account_list.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_DST_ACCOUNT_NOT_FOUND, MsgKeys.NOT_EXIST, "dst_account_no");
        }

        String dst_account_id = dst_account_list.get(0).getAccount_id();
        String dst_customer_id = dst_account_list.get(0).getCustomer_id();
        if (!dst_customer_id.equals(src_customer_id)) {
            throw new Http400Exception(MsgKeys.ERROR_CUSTOMER_MUST_BE_THE_SAME, MsgKeys.TX_EXCHANGE_ACCOUNT_MUST_SAME_CUSTOMER);
        }

        Account_Balance dst_ab = new Account_Balance();
        dst_ab.setAccount_id(dst_account_id);
        Common.sleep();
        List<Account_Balance> dst_ab_list = db.findByIndex(getSelector(dst_ab), Account_Balance.class);
        Boolean hasDstCurrency = false;
        for (Account_Balance ab : dst_ab_list) {
            if (ab.getCurrency().equals(input.getDst_currency())) {
                dst_account_balance = ab;
                hasDstCurrency = true;
            }
        }

        if (!hasDstCurrency) {
            dst_account_balance.setAccount_id(dst_account_id);
            dst_account_balance.setCurrency(input.getDst_currency());
            dst_account_balance.setBalance(String.valueOf(input.getDst_amount()));
            dst_account_balance.setType("Account_Balance");
            db.save(dst_account_balance);
            Common.sleep();
        } else {
            // two decimal places
            Double resultDstBalance = input.getDst_amount();
            if (dst_account_balance.getBalance() != null) {
                resultDstBalance = Common.add(Double.parseDouble(dst_account_balance.getBalance()), input.getDst_amount());
            }
            BigDecimal b2 = new BigDecimal(resultDstBalance);
            resultDstBalance = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            dst_account_balance.setBalance(String.valueOf(resultDstBalance));
            Common.sleep();
            if (dst_account_balance.get_id() != null) {
                db.update(dst_account_balance);
            }
        }
        // two decimal places
        Double resultSrcBalance = Common.sub(Double.parseDouble(src_account_balance.getBalance()), Common.mul(input.getDst_amount(), Common.div(rate, 100.0, 1)));
        BigDecimal b = new BigDecimal(resultSrcBalance);
        resultSrcBalance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        src_account_balance.setBalance(String.valueOf(resultSrcBalance));
        Common.sleep();
        db.update(src_account_balance);

        Account_Txn account_tnx = new Account_Txn();
        account_tnx.setType("Account_Txn");
        account_tnx.setTxn_type("FOREX");
        account_tnx.setAccount_no(account_no);
        account_tnx.setAccount_no2(input.getDst_account_no());
        account_tnx.setAfter_balance(String.valueOf(Double.parseDouble(src_account_balance.getBalance())));
        account_tnx.setAfter_balance2(String.valueOf(Double.parseDouble(dst_account_balance.getBalance())));
        account_tnx.setAmount(String.valueOf(-Common.mul(input.getDst_amount(), Common.div(rate, 100, 1))));
        account_tnx.setAmount2(String.valueOf(input.getDst_amount()));
        account_tnx.setCurrency(src_account_balance.getCurrency());
        account_tnx.setCurrency2(dst_account_balance.getCurrency());
        account_tnx.setDatetime(now());
        account_tnx.setRemark(input.getRemark());
        Common.sleep();
        db.save(account_tnx);

        ForexResponse result = new ForexResponse();
        result.setSrc_balance_after(Double.parseDouble(src_account_balance.getBalance()));
        result.setDst_balance_after(Double.parseDouble(dst_account_balance.getBalance()));
        result.setRate(String.valueOf(rate));
        return result;
    }
}
