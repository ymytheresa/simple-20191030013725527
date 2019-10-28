package com.boc.cloud.api.rest.accounts;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.api.utils.Common;
import com.boc.cloud.entity.Account;
import com.boc.cloud.entity.Account_Txn;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuxi List account transactions
 */
@RestController
public class TransactionsApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Get account transactions list", notes = "Get a list of transactions for the specified account", produces = "application/json")
    @ApiImplicitParam(name = "account_no", value = "account_no", defaultValue = "1287511111111", paramType = "path", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "invalidAccountNo / inputRequired",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/accounts/{account_no}/transactions", method = RequestMethod.GET)
    public List<Account_Txn> listTransactions(@PathVariable String account_no, HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customerId = getCustomerId(request);
        checkService.checkCustomerId(customerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        String selector = "{\"selector\": {\"type\": \"Account_Txn\",\"$or\": [{\"account_no\": \""
            + account_no + "\"},{\"account_no2\": \"" + account_no + "\"}]}}";

        checkService.isRequired(account_no, "account_no");

        checkService.checkAcno(account_no, MsgKeys.ERROR_INVALID_ACCOUNT_NO);

        Account account = new Account();
        account.setCustomer_id(customerId);
        Common.sleep();
        List<Account> accountList = db.findByIndex(getSelector(account), Account.class);
        List<String> accountNoList = accountList.stream().map(Account::getAccount_no).collect(Collectors.toList());

        Common.sleep();
        List<Account_Txn> resultList = db.findByIndex(selector, Account_Txn.class);
        resultList.sort(Comparator.comparing(Account_Txn::getDatetime).reversed());
        for (Account_Txn temp : resultList) {
            // mask balances if account does not belong to the current customer
            if (!accountNoList.contains(temp.getAccount_no())) {
                temp.setAfter_balance(null);
            }
            if (!accountNoList.contains(temp.getAccount_no2())) {
                temp.setAfter_balance2(null);
            }

            String amount = temp.getAmount();
            if (StringUtils.isNotBlank(amount)) {
                BigDecimal b = new BigDecimal(amount);
                temp.setAmount(b.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

            String amount2 = temp.getAmount2();
            if (StringUtils.isNotBlank(amount2)) {
                BigDecimal b = new BigDecimal(amount2);
                temp.setAmount2(b.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

            String after_balance = temp.getAfter_balance();
            if (StringUtils.isNotBlank(after_balance)) {
                BigDecimal b = new BigDecimal(after_balance);
                temp.setAfter_balance(b.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }

            String after_balance2 = temp.getAfter_balance2();
            if (StringUtils.isNotBlank(after_balance2)) {
                BigDecimal b = new BigDecimal(after_balance2);
                temp.setAfter_balance2(b.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }

        return resultList;
    }

}
