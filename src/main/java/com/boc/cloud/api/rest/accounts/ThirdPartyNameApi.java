package com.boc.cloud.api.rest.accounts;

import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.entity.Account;
import com.boc.cloud.entity.Customer;
import com.boc.cloud.entity.Third_Party_Account;
import com.cloudant.client.api.Database;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wuxi
 * Retrieve the customer name of a 3rd party bank or FPS account
 */
@RestController
public class ThirdPartyNameApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Query the name of the owner of the specified non-BOCHK account", notes = "Query the customer name of the third party account using a Faster Payment System information or account number", produces = "application/json")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "account_info", value = "account_info", paramType = "query", defaultValue = "61111111", required = true),
        @ApiImplicitParam(name = "account_info_type", value = "account_info_type", paramType = "query", defaultValue = "FPS_PHONE_NO", required = true)})
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "inputRequired / accountNotFound / customerMustBeDifferent",
            response = ErrorResponse.class)})
    @RequestMapping(value = "/api/accounts/third-party-name", method = RequestMethod.GET)
    public ThirdPartyResult getThirdParty(@RequestParam String account_info,
                                          @RequestParam AccountInfoType account_info_type, HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customer_id = getCustomerId(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        checkService.isRequired(account_info, "account_info");

        String accountInfoType = account_info_type.value();
        if (accountInfoType.equals("FPS_PHONE_NO") || accountInfoType.equals("FPS_EMAIL") || accountInfoType.equals("FPS_ID")) {
            Third_Party_Account tpa = new Third_Party_Account();
            switch (account_info_type) {
                case FPS_PHONE_NO:
                    tpa.setFps_phone_no(account_info);
                    break;
                case FPS_EMAIL:
                    tpa.setFps_email(account_info);
                    break;
                case FPS_ID:
                    tpa.setFps_id(account_info);
                    break;
                case ACCOUNT_NO:
                    break;
                default:
                    break;
            }
            List<Third_Party_Account> list = db.findByIndex(getSelector(tpa), Third_Party_Account.class);
            if (list == null || list.size() == 0) {
                throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_NOT_FOUND, MsgKeys.IS_INVALID, "Third party account info");
            }
            Third_Party_Account third_Party_Account = list.get(0);
            String account_name = third_Party_Account.getAccount_name();
            System.out.println(account_name);

            ThirdPartyResult result = new ThirdPartyResult();
            result.setName(account_name);
            return result;
        } else if (accountInfoType.equals("ACCOUNT_NO")) {
            Account condition = new Account();
            condition.setAccount_no(account_info);
            List<Account> list = db.findByIndex(getSelector(condition), Account.class);
            if (list == null || list.size() == 0) {
                // No have such account_no in
                throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_NOT_FOUND, MsgKeys.IS_INVALID, "Third party account info");
            }
            Account account = list.get(0);
            String account_no = account.getAccount_no();
            System.out.println(account_no);
            checkService.checkAcno(account_no, MsgKeys.ERROR_INVALID_ACCOUNT_NO);

            Customer searchCustomer = new Customer();
            searchCustomer.setCustomer_id(account.getCustomer_id());
            Customer customer = db.findByIndex(getSelector(searchCustomer), Customer.class).get(0);
            System.out.println(customer.getFull_name());

            String CustomerId = getCustomerId(request);
            if (customer.getCustomer_id().equals(CustomerId)) {
                // the account_no is same as the customer_id from the authenticated token
                throw new Http400Exception(MsgKeys.ERROR_CUSTOMER_MUST_BE_DIFFERENT, MsgKeys.IS_INVALID, "Account");
            }
            System.out.println(account_no);

            ThirdPartyResult result = new ThirdPartyResult();
            result.setName(customer.getFull_name());
            return result;
        }
        return null;
    }

    public class ThirdPartyResult {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public enum AccountInfoType {
        FPS_PHONE_NO, FPS_EMAIL, FPS_ID, ACCOUNT_NO;

        public String value() {
            return this.toString();
        }
    }
}
