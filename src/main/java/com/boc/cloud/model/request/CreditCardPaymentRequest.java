package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CreditCardPaymentRequest {

    @ApiModelProperty(value = "Currency to pay (must match with credit card)", required = true, allowableValues = "HKD,USD,CNY", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount to pay", required = true, example = "50")
    private Double amount;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
