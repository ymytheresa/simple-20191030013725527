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
import com.boc.cloud.entity.Third_Party_Account;
import com.boc.cloud.model.request.MoneyTransferFpsRequest;
import com.boc.cloud.model.response.MoneyTransferFpsResponse;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangmengxue
 * FPS Transfer
 */
@RestController
public class MoneyTransferFpsApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;
    private String[] dstInfoTypeRange = new String[]{"FPS_PHONE_NO", "FPS_EMAIL", "FPS_ID"};
    private String[] curRange = new String[]{"CNY", "HKD"};

    @ApiOperation(value = "Transfer money to other non-BOCHK bank accounts", notes = "Transfer money to third party through Faster Payment System", produces = "application/json")
    @ApiImplicitParam(name = "account_no", value = "account_no", defaultValue = "1288211111113", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "amountRequired / invalidAmount / currencyRequired / " +
            "invalidCurrency / invalidDstInfoType / dstInfoRequired / " +
            "dstInfoTypeRequired / dstAccountNotFound",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/accounts/{account_no}/money-transfer-fps", method = RequestMethod.POST)
    public MoneyTransferFpsResponse payment(@PathVariable String account_no,
                                            @RequestBody @ApiParam(value = "FPS transfer input values(JSON)") MoneyTransferFpsRequest input,
                                            HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customer_id = getCustomerId(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        if (String.valueOf(input.getAmount()).equals("") || input.getAmount() == null) {
            throw new Http400Exception(MsgKeys.ERROR_AMOUNT_REQUIRED, MsgKeys.INPUT_REQUIRED, "amount");
        }
        checkService.checkAmount(input.getAmount());
        if ("".equals(input.getCurrency()) || input.getCurrency() == null) {
            throw new Http400Exception(MsgKeys.ERROR_CURRENCY_REQUIRED, MsgKeys.INPUT_REQUIRED, "currency");
        }
        if (!Arrays.asList(curRange).contains(input.getCurrency())) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_CURRENCY, MsgKeys.IS_INVALID, "Currency");
        }
        if (!Arrays.asList(dstInfoTypeRange).contains(input.getDst_info_type())) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_DST_INFO_TYPE, MsgKeys.NOT_EXIST, "dst_info_type");
        }
        if ("".equals(input.getDst_info()) || input.getDst_info() == null) {
            throw new Http400Exception(MsgKeys.ERROR_DST_INFO_REQUIRED, MsgKeys.INPUT_REQUIRED, "dst_info ");
        }
        if ("".equals(input.getDst_info_type()) || input.getDst_info_type() == null) {
            throw new Http400Exception(MsgKeys.ERROR_DST_INFO_TYPE_REQUIRED, MsgKeys.INPUT_REQUIRED, "dst_info_type ");
        }
        String account_name;
        Third_Party_Account third_account = new Third_Party_Account();
        switch (input.getDst_info_type()) {
            case "FPS_PHONE_NO":
                third_account.setFps_phone_no(input.getDst_info());
                break;
            case "FPS_EMAIL":
                third_account.setFps_email(input.getDst_info());
                break;
            case "FPS_ID":
                third_account.setFps_id(input.getDst_info());
                break;
        }
        List<Third_Party_Account> third_account_list = db.findByIndex(getSelector(third_account), Third_Party_Account.class);
        if (third_account_list == null || third_account_list.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_DST_ACCOUNT_NOT_FOUND, MsgKeys.NOT_EXIST, "Third party account");
        }
        account_name = third_account_list.get(0).getAccount_name();

        String account_id;
        String src_bal;
        MoneyTransferFpsResponse result = new MoneyTransferFpsResponse();
        Account_Balance ab = new Account_Balance();
        // use account_no query account_id
        Account account = new Account();
        account.setAccount_no(account_no);
        List<Account> account_list = db.findByIndex(getSelector(account), Account.class);
        if (account_list == null || account_list.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_NOT_FOUND, MsgKeys.NOT_EXIST, "account_no");
        }
        // use account_id and currency query bal
        account_id = account_list.get(0).getAccount_id();
        ab.setAccount_id(account_id);
        ab.setCurrency(input.getCurrency());
        List<Account_Balance> account_balance_list = db.findByIndex(getSelector(ab), Account_Balance.class);
        if (account_balance_list == null || account_balance_list.size() != 1) {
            throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_NOT_FOUND, MsgKeys.NOT_EXIST, "account_no");
        }
        // update bal
        src_bal = account_balance_list.get(0).getBalance();
        if (Double.parseDouble(src_bal) < input.getAmount()) {
            throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_ACCOUNT_BALANCE, MsgKeys.CREDIT_CARD_AMT_INSUFFICIENT);
        }
        // two decimal places
        Double resultBalance = Common.sub(Double.parseDouble(src_bal), input.getAmount());
        BigDecimal b = new BigDecimal(resultBalance);
        resultBalance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        account_balance_list.get(0).setBalance(String.valueOf(resultBalance));
        db.update(account_balance_list.get(0));

        //insert account_txn
        Account_Txn account_tnx = new Account_Txn();
        switch (input.getDst_info_type()) {
            case "FPS_PHONE_NO":
                account_tnx.setFps_phone_no(input.getDst_info());
                break;
            case "FPS_EMAIL":
                account_tnx.setFps_email(input.getDst_info());
                break;
            case "FPS_ID":
                account_tnx.setFps_id(input.getDst_info());
                break;
        }
        account_tnx.setType("Account_Txn");
        account_tnx.setTxn_type("TRANSFER");
        account_tnx.setAccount_no(account_no);
        account_tnx.setAfter_balance(String.valueOf(Double.parseDouble(account_balance_list.get(0).getBalance())));
        account_tnx.setAmount(String.valueOf(-input.getAmount()));
        account_tnx.setAmount2(String.valueOf(input.getAmount()));
        account_tnx.setCurrency(input.getCurrency());
        account_tnx.setCurrency2(input.getCurrency());
        account_tnx.setDatetime(now());
        account_tnx.setRemark(input.getRemark());
        db.save(account_tnx);

        //create result list
        result.setDst_account_name(account_name);
        result.setSrc_balance_after(Double.parseDouble(account_balance_list.get(0).getBalance()));

        return result;
    }
}
