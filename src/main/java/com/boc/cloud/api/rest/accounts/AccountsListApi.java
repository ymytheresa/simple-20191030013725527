package com.boc.cloud.api.rest.accounts;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.api.utils.Common;
import com.boc.cloud.entity.Account;
import com.boc.cloud.entity.Account_Balance;
import com.boc.cloud.model.response.AccountBalance;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * List accounts (with balances) Retrieve the customer name of a 3rd party bank
 * or FPS account List account transactions Transfer money (intra-bank)
 *
 * @author wuxi
 */
@RestController
public class AccountsListApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Get account list", notes = "Get a list of accounts owned by the customer", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful")})
    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    public List<AccountSummary> getList(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        Account account = new Account();
        String CustomerId = getCustomerId(request);
        checkService.checkCustomerId(CustomerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        account.setCustomer_id(CustomerId);

        List<Account> info = db.findByIndex(getSelector(account), Account.class);
        List<AccountSummary> balanceResult = new ArrayList<>();
        for (Account item : info) {
            String account_id = item.getAccount_id();
            Account_Balance balance = new Account_Balance();
            balance.setAccount_id(account_id);
            Common.sleep();
            List<Account_Balance> balanceList = db.findByIndex(getSelector(balance), Account_Balance.class);

            AccountSummary accountSummary = new AccountSummary();
            accountSummary.setAccount_no(item.getAccount_no());
            List<AccountBalance> accountBalanceArray = new ArrayList<>();
            for (Account_Balance account_Balance : balanceList) {
                AccountBalance bean = new AccountBalance();
                bean.setBalance(Double.parseDouble(account_Balance.getBalance()));
                bean.setCurrency(account_Balance.getCurrency());
                accountBalanceArray.add(bean);
            }
            accountSummary.setBalances(accountBalanceArray);
            balanceResult.add(accountSummary);
        }
        return balanceResult;
    }

    @ApiModel
    private class AccountSummary {

        @ApiModelProperty(value = "Account number", required = true, example = "1288212222223")
        private String account_no;

        @ApiModelProperty(value = "Available balances", required = true)
        private List<AccountBalance> balances;

        public String getAccount_no() {
            return account_no;
        }

        public void setAccount_no(String account_no) {
            this.account_no = account_no;
        }

        public List<AccountBalance> getBalances() {
            return balances;
        }

        public void setBalances(List<AccountBalance> balances) {
            this.balances = balances;
        }
    }

}
