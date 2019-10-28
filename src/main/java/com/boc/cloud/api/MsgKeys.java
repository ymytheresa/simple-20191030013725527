package com.boc.cloud.api;

/**
 * Constant class for i18 message keys.
 */
public interface MsgKeys {
    String INTERNAL_SERVER_ERROR = "common.interval.server.error";
    String INTERNAL_SERVER_ERROR_WITH_STACKTRACE = "common.interval.server.error.with.stacktrace";

    String INPUT_REQUIRED = "common.input.required";
    String NOT_EXIST = "common.not.exist";
    String IS_INVALID = "common.invalid";
    String NOT_MATCH = "common.not.match";
    String MUST_BE_NUMERIC = "must.be.numeric";
    String IS_OUT_OF_RANGE = "is.out.of.range";
    String MUST_BE_DIFFERENT = "must.be.different";

    // number
    String NUMBER_EQ = "common.number.eq";
    String NUMBER_LT = "common.number.lt";
    String NUMBER_LTE = "common.number.lte";
    String NUMBER_GT = "common.number.gt";
    String NUMBER_GTE = "common.number.gte";

    // security
    String FORBIDDEN = "security.forbidden";
    String GRANT_TYPE_AUTH_CODE_REQUIRED = "security.grant.type.auth.code.required";
    String GRANT_TYPE_CLIENT_CRENDENTIALS_REQUIRED = "security.grant.type.client.credentials.required";

    // credit card
    String CREDIT_CARD_AMT_INSUFFICIENT = "creditcard.amt.insufficient";
    String CREDIT_CARD_INVALID_CURRENCY = "creditcard.currency.not.match";

    //Appointment
    String APPOINTMENT_DATETIME_NOT_MATCH = "appointment.datetime.not.match";
    String APPOINTMENT_DATETIME_INVALID = "appointment.datetime.invalid";

    // Transaction
    String TX_INSUF_BAL = "tx.insuf.bal";
    String TX_EXCHANGE_ACCOUNT_MUST_SAME_CUSTOMER = "tx.exchange.account.must.same.customer";
    String TX_EXCHANGE_CUR_MUST_NOT_HKD = "tx.exchange.cur.must.not.hkd";

    // Stock
    String STOCK_UNIT_PRICE_NOT_MATCH = "stock.unit.price.not.match";
    String STOCK_IS_NOT_ENOUGH = "stock.is.not.enough";
    String SETTLEMENT_ACCOUNT_NOT_BELONG_TO_CUSTOMER = "settlement.account.not.belong.to.customer";

    // Personal_loan
    String PERSONAL_LOAN_CURRENCY_HKD = "personal.loan.currency.hkd";
    String PERSONAL_LOAN_AMOUNT_RATE_NO_VALID = "personal.loan.amount.rate.no.valid";
    String PERSONAL_LOAN_INSTALLMENT_AMOUNT_NO_VALID = "personal.loan.installment.amount.no.valid";


    /*
     * Error codes
     */
    String ERROR_FORBIDDEN = "forbidden";
    String ERROR_AUTH_CODE_GRANT_REQUIRED = "authCodeGrantRequired";
    String ERROR_CLIENT_CRED_GRANT_REQUIRED = "clientCredGrantRequired";

    String ERROR_BRANCH_CODE_REQUIRED = "branchCodeRequired";
    String ERROR_DATETIME_REQUIRED = "datetimeRequired";
    String ERROR_REQUESTED_SERVICE_CATEGORY_REQUIRED = "requestedServiceCategoryRequired";
    String ERROR_ACTION_REQUIRED = "actionRequired";
    String ERROR_UNIT_PRICE_REQUIRED = "unitPriceRequired";
    String ERROR_QUANTITY_REQUIRED = "quantityRequired";
    String ERROR_AMOUNT_REQUIRED = "amountRequired";
    String ERROR_CURRENCY_REQUIRED = "currencyRequired";
    String ERROR_INPUT_REQUIRED = "inputRequired";
    String ERROR_SRC_CURRENCY_REQUIRED = "srcCurrencyRequired";
    String ERROR_DST_CURRENCY_REQUIRED = "dstCurrencyRequired";
    String ERROR_DST_ACCOUNT_NO_REQUIRED = "dstAccountNoRequired";
    String ERROR_RATE_REQUIRED = "rateRequired";
    String ERROR_DST_INFO_TYPE_REQUIRED = "dstInfoTypeRequired";
    String ERROR_DST_INFO_REQUIRED = "dstInfoRequired";
    String ERROR_CONFIRMATION_REQUIRED = "confirmationRequired";

    String ERROR_INVALID_PHONE_NO = "invalidPhoneNo";
    String ERROR_INVALID_DATETIME = "invalidDatetime";
    String ERROR_INVALID_DATE = "invalidDate";
    String ERROR_INVALID_REQUESTED_SERVICE_CATEGORY = "invalidRequestedServiceCategory";
    String ERROR_INVALID_BRANCH_CODE = "invalidBranchCode";
    String ERROR_INVALID_LOAN_RATE = "invalidLoanRate";
    String ERROR_INVALID_INSTALLMENT_AMOUNT = "invalidInstallmentAmount";
    String ERROR_INVALID_CURRENCY = "invalidCurrency";
    String ERROR_INVALID_CUSTOMER_ID = "invalidCustomerId";
    String ERROR_INVALID_STOCK_CODE = "invalidStockCode";
    String ERROR_INVALID_ACCOUNT_NO = "invalidAccountNo";
    String ERROR_INVALID_STOCK_UNIT_PRICE = "invalidStockUnitPrice";
    String ERROR_INVALID_AMOUNT = "invalidAmount";
    String ERROR_INVALID_RATE = "invalidRate";
    String ERROR_INVALID_DST_INFO_TYPE = "invalidDstInfoType";
    String ERROR_INVALID_NO_OF_TERMS = "invalidNoOfTerms";

    String ERROR_APPOINTMENT_NOT_FOUND = "appointmentNotFound";
    String ERROR_CREDIT_CARD_NOT_FOUND = "creditCardNotFound";
    String ERROR_ACCOUNT_NOT_FOUND = "accountNotFound";
    String ERROR_DST_ACCOUNT_NOT_FOUND = "dstAccountNotFound";
    String ERROR_STOCK_NOT_FOUND = "stockNotFound";
    String ERROR_CURRENCY_NOT_FOUND = "currencyNotFound";

    String ERROR_INSUFFICIENT_CREDIT = "insufficientCredit";
    String ERROR_INSUFFICIENT_ACCOUNT_BALANCE = "insufficientAccountBalance";
    String ERROR_INSUFFICIENT_STOCK = "insufficientStock";

    String ERROR_ACCOUNT_MUST_BE_DIFFERENT = "accountMustBeDifferent";
    String ERROR_CUSTOMER_MUST_BE_THE_SAME = "customerMustBeTheSame";
    String ERROR_CUSTOMER_MUST_BE_DIFFERENT = "customerMustBeDifferent";
    String ERROR_CURRENCY_MUST_BE_DIFFERENT = "currencyMustBeDifferent";

    String ERROR_INTERNAL = "internalError";
    String ERROR_INVALID_INPUT = "invalidInput";

}
