package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MoneyTransferRequest {

    @ApiModelProperty(value = "Currency of amount to transfer", required = true, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount to transfer", required = true, example = "150")
    private Double amount;

    @ApiModelProperty(value = "Account number of the recipient", required = true, example = "1287512222221")
    private String dst_account_no;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDst_account_no() {
        return dst_account_no;
    }

    public void setDst_account_no(String dst_account_no) {
        this.dst_account_no = dst_account_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
