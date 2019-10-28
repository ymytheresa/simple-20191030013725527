package com.boc.cloud.api.rest.personalloans;


import com.boc.cloud.api.MsgKeys;
import com.boc.cloud.api.exception.ErrorResponse;
import com.boc.cloud.api.exception.Http400Exception;
import com.boc.cloud.api.rest.BaseRestApi;
import com.boc.cloud.api.service.CheckService;
import com.boc.cloud.api.utils.Common;
import com.boc.cloud.entity.*;
import com.boc.cloud.model.request.PersonalLoanApplicationRequest;
import com.boc.cloud.model.request.PersonalLoanCheckRateRequest;
import com.boc.cloud.model.response.PersonalLoan;
import com.boc.cloud.model.response.PersonalLoanApplicationResponse;
import com.boc.cloud.model.response.PersonalLoanCheckRateResponse;
import com.cloudant.client.api.Database;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * List personal loans
 * Check personalized loan rate
 * Apply for personal loan
 *
 * @author jinjuan
 */
@RestController
public class PersonalLoansApi extends BaseRestApi {
    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "Get personal loan list", notes = "Get personal loan list", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "accountNotFound", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/personal-loans", method = RequestMethod.GET)
    public List<PersonalLoan> getList(HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customer_id = this.getCustomerId(request);
        checkService.checkCustomerId(customer_id, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        Account account = new Account();
        account.setCustomer_id(customer_id);

        List<Account> accountList = db.findByIndex(getSelector(account), Account.class);
        if (accountList == null || accountList.size() == 0) {
            throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_NOT_FOUND, MsgKeys.NOT_EXIST, "Account");
        }
        String accountIds = "";
        for (Account accountInfo : accountList) {
            accountIds = accountIds + "\"" + accountInfo.getAccount_id() + "\"" + ",";
        }
        String selector = "{\"selector\": {\"type\": {\"$eq\": \"Personal_Loan\"},\"account_id\":{\"$in\":[" + accountIds.substring(0, accountIds.length() - 1)
            + "]}},\"sort\":[\"amount_id\",\"asc\"]}";

        List<PersonalLoan> loans = db.findByIndex(selector, PersonalLoan.class);
        loans.sort(Comparator.comparing(PersonalLoan::getOpen_date));
        return loans;
    }

    @ApiOperation(value = "Check personal loan interest rate", notes = "Check personal loan interest rate", consumes = "application/json", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "invalidNoOfTerms / invalidAmount", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/personal-loans/personalized-rate", method = RequestMethod.POST)
    public PersonalLoanCheckRateResponse checkPersonalizedRate(@RequestBody @ApiParam(value = "Check personal loan interest rate input values(JSON)") PersonalLoanCheckRateRequest input,
                                                               HttpServletRequest request) throws Exception {
        //1. The request object personal_loan_check_rate_request include currency, amount, nbr_of_terms, remark
        //2. The logic is only to check the personal_loan_rate table and retrive the annual_interest_rate.
        //3. However from the peronsal_loan_rate_response object will contain currency, amount, no_of_terms, rate, installment_amount, remark
        //FIXME 待確認，目前是按照先查詢Personal_Loan，如果不存在再查詢Personal_Loan_Rate。
        checkService.isRequired(input.getCurrency(), "currency");
        checkService.isRequired(input.getAmount() + "", "amount");
        checkService.isRequired(input.getNo_of_terms() + "", "no_of_terms");
        checkService.checkHKCurrency(input.getCurrency());
        if (input.getNo_of_terms() <= 0) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_NO_OF_TERMS, MsgKeys.NUMBER_GT, "Number of terms", "0");
        }
        if (input.getNo_of_terms() > 360) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_NO_OF_TERMS, MsgKeys.NUMBER_LTE, "Number of terms", "360");
        }

        Database db = getDb(request);
        String customerId = getCustomerId(request);
        checkService.checkCustomerId(customerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);

        PersonalLoanCheckRateResponse output = new PersonalLoanCheckRateResponse();
        output.setCurrency(input.getCurrency());
        output.setAmount(input.getAmount());
        output.setNo_of_terms((long) input.getNo_of_terms());
        output.setRemark(input.getRemark());

        long inputAmount = input.getAmount();
        if (inputAmount < 10000) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_GTE, "Amount", "10000");
        }
        if (inputAmount > 1000000) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_LTE, "Amount", "1000000");
        }

        String selector = "{\"selector\": {\"type\": {\"$eq\": \"Personal_Loan_Rate\"},\"currency\":{\"$eq\": \""
            + input.getCurrency() + "\"}},\"sort\":[\"amount_cap\",\"asc\"]}";
        List<Personal_Loan_Rate> loanRateList = db.findByIndex(selector, Personal_Loan_Rate.class);
        if (loanRateList == null || loanRateList.size() == 0) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST, "Personal_Loan_Rate");
        }
        for (Personal_Loan_Rate loanRate : loanRateList) {
            double amountCap = Double.parseDouble(loanRate.getAmount_cap());
            if (inputAmount <= amountCap) {
                output.setRate(Double.parseDouble(loanRate.getAnnual_interest_rate()));
                Long temp = this.calculateInstallmentAmount(inputAmount, output.getRate(), (long) (input.getNo_of_terms()));
                output.setInstallment_amount(temp);
                break;
            }
        }

        return output;
    }

    @ApiOperation(value = "Apply personal loan", notes = "Apply personal loan", consumes = "application/json", produces = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "successful"),
        @ApiResponse(code = 400, message = "accountNotFound / invalidNoOfTerms / invalidAmount / " +
            "invalidLoanRate / invalidInstallmentAmount / customerMustBeTheSame / " +
            "invalidCurrency", response = ErrorResponse.class)})
    @RequestMapping(value = "/api/personal-loans/applications", method = RequestMethod.POST)
    public PersonalLoanApplicationResponse createPersonalLoan(@RequestBody @ApiParam(value = "Apply personal loan input values(JSON)") PersonalLoanApplicationRequest input,
                                                              HttpServletRequest request) throws Exception {
        Database db = getDb(request);
        String customerId = this.getCustomerId(request);
        checkService.checkCustomerId(customerId, MsgKeys.ERROR_INVALID_CUSTOMER_ID);
        Account account = new Account();
        account.setCustomer_id(customerId);

        List<Account> list = db.findByIndex(getSelector(account), Account.class);
        if (list == null || list.size() == 0) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST);
        }
        boolean flag = false;
        for (Account item : list) {
            if (item.getAccount_no().equals(input.getAccount_no())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new Http400Exception(MsgKeys.ERROR_CUSTOMER_MUST_BE_THE_SAME, MsgKeys.NOT_MATCH, "Account");
        }

        String currency = input.getCurrency();
        checkService.isRequired(input.getAccount_no(), "account_no");
        checkService.isRequired(currency, "currency");
        checkService.isRequired(input.getAmount() + "", "amount");
        checkService.isRequired(input.getNo_of_terms() + "", "no_of_terms");
        checkService.isRequired(input.getRate().toString(), "rate");
        checkService.isRequired(input.getInstallment_amount() + "", "installment_amount");
        checkService.isRequired(input.getInstallment_day() + "", "installment_day");
        // check currency is HKD
        checkService.checkHKCurrency(currency);
        if (input.getNo_of_terms() <= 0) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_NO_OF_TERMS, MsgKeys.NUMBER_GT, "Number of terms", "0");
        }
        if (input.getNo_of_terms() > 360) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_NO_OF_TERMS, MsgKeys.NUMBER_LTE, "Number of terms", "360");
        }

        // check the amount and rate is matched personal_loan_rate table
        double amount;
        try {
            amount = input.getAmount();
        } catch (Exception e) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.IS_INVALID, "Amount");
        }
        if (amount < 10000) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_GTE, "Amount", "10000");
        }
        if (amount > 1000000) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_AMOUNT, MsgKeys.NUMBER_LTE, "Amount", "1000000");
        }
        BigDecimal rate = new BigDecimal(input.getRate().toString());

        String selector = "{\"selector\": {\"type\": {\"$eq\": \"Personal_Loan_Rate\"}},\"sort\":[\"amount_cap\",\"asc\"]}";
        List<Personal_Loan_Rate> loanRateList = db.findByIndex(selector, Personal_Loan_Rate.class);
        if (loanRateList == null || loanRateList.size() == 0) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST, "Personal_Loan_Rate");
        }
        for (Personal_Loan_Rate loanRate : loanRateList) {
            double amountCap = Double.parseDouble(loanRate.getAmount_cap());
            if (amount <= amountCap) {
                if (rate.multiply(new BigDecimal(100))
                    .compareTo((new BigDecimal(loanRate.getAnnual_interest_rate())).multiply(new BigDecimal(100))) != 0) {
                    throw new Http400Exception(MsgKeys.ERROR_INVALID_LOAN_RATE, MsgKeys.PERSONAL_LOAN_AMOUNT_RATE_NO_VALID);
                }
                break;
            }
        }
        // check installment is equal input intallment_amout
        Long temp = this.calculateInstallmentAmount(input.getAmount(), input.getRate(), input.getNo_of_terms());

        BigDecimal calculatedAmount = new BigDecimal(temp);
        if (calculatedAmount.compareTo(new BigDecimal(input.getInstallment_amount())) != 0) {
            throw new Http400Exception(MsgKeys.ERROR_INVALID_INSTALLMENT_AMOUNT, MsgKeys.PERSONAL_LOAN_INSTALLMENT_AMOUNT_NO_VALID);
        }

        Account accountObj = new Account();
        accountObj.setAccount_no(input.getAccount_no());
        List<Account> accountList = db.findByIndex(getSelector(accountObj), Account.class);
        if (accountList == null || accountList.size() == 0) {
            throw new Http400Exception(MsgKeys.ERROR_ACCOUNT_NOT_FOUND, MsgKeys.NOT_EXIST, "Account");
        }
        Account accountInfo = accountList.get(0);

        Personal_Loan personalLoan = new Personal_Loan();
        personalLoan.setType("Personal_Loan");
        personalLoan.setAccount_id(accountInfo.getAccount_id());
        personalLoan.setOpen_date(nowDate());
        personalLoan.setAmount(input.getAmount() + "");
        personalLoan.setOriginal_amount(input.getAmount() + "");
        personalLoan.setCurrency(input.getCurrency());
        personalLoan.setInstallment_amount(input.getInstallment_amount() + "");
        personalLoan.setInstallment_day(input.getInstallment_day() + "");
        personalLoan.setNo_of_terms(input.getNo_of_terms() + "");
        personalLoan.setRate(input.getRate().toString());
        personalLoan.setRemark(input.getRemark());

        // insert account_txn table and set the txn_type to "LOAN".
        Account_Txn accountTxn = new Account_Txn();
        accountTxn.setType("Account_Txn");
        accountTxn.setDatetime(now());
        accountTxn.setTxn_type("LOAN");
        accountTxn.setAccount_no(input.getAccount_no());
        accountTxn.setCurrency(input.getCurrency());
        accountTxn.setAmount(input.getAmount() + "");

        // update balance field in the account_balance table
        Account_Balance accountBalanceObj = new Account_Balance();
        accountBalanceObj.setAccount_id(accountInfo.getAccount_id());
        accountBalanceObj.setCurrency(input.getCurrency());

        Account_Balance accountBalance;
        try {
            accountBalance = db.findByIndex(getSelector(accountBalanceObj), Account_Balance.class).get(0);
        } catch (Exception e) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST, "Account_Balance");
        }
        if (accountBalance == null) {
            throw new Http400Exception(MsgKeys.ERROR_INTERNAL, MsgKeys.NOT_EXIST, "Account_Balance");
        }
        double balance = Double.parseDouble(accountBalance.getBalance());
        accountBalance.setBalance(Common.add(balance, amount) + "");
        accountTxn.setAfter_balance(accountBalance.getBalance());
        accountTxn.setRemark(input.getRemark());

        db.save(personalLoan);
        db.save(accountTxn);
        db.update(accountBalance);

        PersonalLoanApplicationResponse output = new PersonalLoanApplicationResponse();
        output.setAmount(input.getAmount());
        output.setAccount_no(input.getAccount_no());
        output.setCurrency(input.getCurrency());
        output.setInstallment_amount(input.getInstallment_amount());
        output.setInstallment_day(input.getInstallment_day());
        output.setNo_of_terms(input.getNo_of_terms());
        output.setRate(input.getRate());
        output.setRemark(input.getRemark());
        output.setBalance_after(Common.add(balance, amount));
        return output;
    }

    private Long calculateInstallmentAmount(Long amount, Double yearlyRate, Long noOfTerms) {
        Double monthlyRate = yearlyRate / 12;
        return Math.round(amount * (monthlyRate + (monthlyRate / (Math.pow(1 + monthlyRate, noOfTerms.doubleValue()) - 1))));
    }

}
