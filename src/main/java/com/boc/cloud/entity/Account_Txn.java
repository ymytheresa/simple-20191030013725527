package com.boc.cloud.entity;

import com.boc.cloud.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Account_Txn extends BaseEntity {

    @ApiModelProperty(value = "Timestamp of the transaction", required = true, example = "")
    private String datetime;

    @ApiModelProperty(value = "Transaction type", required = true, allowableValues = "FOREX,TRANSFER,LOAN,STOCK", example = "TRANSFER")
    private String txn_type;

    @ApiModelProperty(value = "Account number", required = true, example = "1287511111111")
    private String account_no;

    @ApiModelProperty(value = "Another account involved in the transaction", required = false, example = "1268811111112")
    private String account_no2;

    @ApiModelProperty(value = "Third party phone number (for money transfer to third party)", required = false, example = "")
    private String fps_phone_no;

    @ApiModelProperty(value = "Third party email address (for money transfer to third party)", required = false, example = "")
    private String fps_email;

    @ApiModelProperty(value = "Third party Faster Payment System ID (for money transfer to third party)", required = false, example = "")
    private String fps_id;

    @ApiModelProperty(value = "Currency of account", required = true, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Currency of the other account involved in the transaction", required = false, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "HKD")
    private String currency2;

    @ApiModelProperty(value = "Amount, from the first account's point of view", required = true, example = "-500")
    private String amount;

    @ApiModelProperty(value = "Amount, from the other account's point of view", required = false, example = "500")
    private String amount2;

    @ApiModelProperty(value = "Balance of the first account after the transaction", required = true, example = "4500.0")
    private String after_balance;

    @ApiModelProperty(value = "Balance of the other account after the transaction", required = false, example = "10500.0")
    private String after_balance2;

    @ApiModelProperty(value = "Stock code (for stock purchase)", required = false, example = "")
    private String stock_code;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getAccount_no() {
        return account_no;
    }

    public String getAccount_no2() {
        return account_no2;
    }

    public String getAfter_balance() {
        return after_balance;
    }

    public String getAfter_balance2() {
        return after_balance2;
    }

    public String getAmount() {
        return amount;
    }

    public String getAmount2() {
        return amount2;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCurrency2() {
        return currency2;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getFps_email() {
        return fps_email;
    }

    public String getFps_id() {
        return fps_id;
    }

    public String getFps_phone_no() {
        return fps_phone_no;
    }

    public String getRemark() {
        return remark;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public void setAccount_no2(String account_no2) {
        this.account_no2 = account_no2;
    }

    public void setAfter_balance(String after_balance) {
        this.after_balance = after_balance;
    }

    public void setAfter_balance2(String after_balance2) {
        this.after_balance2 = after_balance2;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAmount2(String amount2) {
        this.amount2 = amount2;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCurrency2(String currency2) {
        this.currency2 = currency2;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setFps_email(String fps_email) {
        this.fps_email = fps_email;
    }

    public void setFps_id(String fps_id) {
        this.fps_id = fps_id;
    }

    public void setFps_phone_no(String fps_phone_no) {
        this.fps_phone_no = fps_phone_no;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

}
