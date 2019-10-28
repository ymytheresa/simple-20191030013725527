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
import com.boc.cloud.entity.Customer;
import com.boc.cloud.model.request.MoneyTransferRequest;
import com.boc.cloud.model.response.MoneyTransferResponse;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author wuxi
 * Transfer money (intra-bank)
 */
@RestController
public class MoneyTransferApi extends BaseRestApi {

    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Money transfer to BOCHK accounts", notes = "Transfer money to other accounts in BOCHK", produces = "application/json")
    @ApiImplicitParam(name = "account_no", value = "account_no", defaultValue = "1287511111111", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "inputRequired / amountRequired / " +
            "accountMustBeDifferent / invalidAmount / invalidAccountNo / " +
            "invalidCurrency / insufficientAccountBalance",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/accounts/{account_no}/money-transfer", method = RequestMethod.POST)
    public MoneyTransferResponse transfer(@PathVariable String account_no,
                                          @RequestBody @ApiParam(value = "cross client transfer input values(JSON)") MoneyTransferRequest data,
                                          HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customer_id = getCustomerId(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        Double src_balance_after1;
        //檢查必填
        checkService.isRequired(account_no, "account_no");
        checkService.isRequired(data.getDst_account_no(), "dst_account_no");
        checkService.isRequired(data.getCurrency(), "currency");
        if (data.getAmount() == null) {
            throw new Http400Exception(MsgKeys.ERROR_AMOUNT_REQUIRED, MsgKeys.INPUT_REQUIRED, "amount");
        }
        //检查如果转出账号鱼被转账号相同则拒纳
        if (account_no.equals(data.getDst_account_no())) {
            throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_MUST_BE_DIFFERENT, MsgKeys.MUST_BE_DIFFERENT, "Source and destination currencies");
        }
        //检查转出账号是否存在
        checkService.checkAcno(account_no, MsgKeys.ERROR_INVALID_ACCOUNT_NO);
        //检查被转出账号是否存在
        checkService.checkAcno(data.getDst_account_no(), MsgKeys.ERROR_DST_ACCOUNT_NOT_FOUND);
        //检查转出额度 需要大于0
        checkService.checkAmount(data.getAmount());
        //检查币种是否存在
        checkService.checkCurrency(data.getCurrency(), MsgKeys.ERROR_INVALID_CURRENCY);

        //转出账户 更新balance表
        Account_Balance obj = getBalance(account_no, data, db);
        if (Double.parseDouble(obj.getBalance()) <= data.getAmount()) {
            throw new Http400Exception(MsgKeys.ERROR_INSUFFICIENT_ACCOUNT_BALANCE, MsgKeys.TX_INSUF_BAL);
        }
        src_balance_after1 = saveBalance(obj, data, false, db);

        //被转出账户更新balance表
        Account_Balance amountObj = getBalance(data.getDst_account_no(), data, db);
        Double src_balance_after2 = saveBalance(amountObj, data, true, db);

        saveAcountTxn(account_no, data.getDst_account_no(), data, src_balance_after1, src_balance_after2, db);

        Account accountObj = new Account();
        accountObj.setAccount_no(data.getDst_account_no());
        Account account = db.findByIndex(getSelector(accountObj), Account.class).get(0);

        Customer customerObj = new Customer();
        customerObj.setCustomer_id(account.getCustomer_id());
        Customer customer = db.findByIndex(getSelector(customerObj), Customer.class).get(0);

        //output
        MoneyTransferResponse output = new MoneyTransferResponse();
        output.setDst_account_name(customer.getFull_name());
        output.setSrc_balance_after(src_balance_after1);
        return output;
    }

    private void saveAcountTxn(String account_no1, String account_no2, MoneyTransferRequest data, Double balanceAfter, Double balanceAfter2, Database db) {
        Account_Txn addObj = new Account_Txn();
        addObj.setDatetime(now());
        addObj.setTxn_type("TRANSFER");
        addObj.setAccount_no(account_no1);
        addObj.setAccount_no2(account_no2);
        addObj.setCurrency(data.getCurrency());
        addObj.setCurrency2(data.getCurrency());
        addObj.setAmount("-" + Double.toString(data.getAmount()));
        addObj.setAmount2(Double.toString(data.getAmount()));
        addObj.setAfter_balance(balanceAfter.toString());
        addObj.setAfter_balance2(balanceAfter2.toString());
        addObj.setRemark(data.getRemark());
        addObj.setType("Account_Txn");
        Common.sleep();
        db.save(addObj);
    }

    private Double saveBalance(Account_Balance obj, MoneyTransferRequest data, boolean isAdd, Database db) {
        BigDecimal b1 = new BigDecimal(obj.getBalance());
        BigDecimal b2 = new BigDecimal(Double.toString(data.getAmount()));
        Double amount;
        if (isAdd) {
            amount = b1.add(b2).doubleValue();
        } else {
            amount = b1.subtract(b2).doubleValue();
        }
        // two decimal places
        BigDecimal b = new BigDecimal(amount);
        amount = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        obj.setBalance(Double.toString(amount));
        Common.sleep();
        db.update(obj);
        return amount;
    }

    private Account_Balance getBalance(String account_no, MoneyTransferRequest data, Database db) {
        Account account = new Account();
        account.setAccount_no(account_no);
        Common.sleep();
        Account accountId = db.findByIndex(getSelector(account), Account.class).get(0);
        Account_Balance balance = new Account_Balance();
        balance.setAccount_id(accountId.getAccount_id());
        balance.setCurrency(data.getCurrency());
        Common.sleep();
        return db.findByIndex(getSelector(balance), Account_Balance.class).get(0);
    }

}
