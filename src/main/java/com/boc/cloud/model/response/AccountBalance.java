package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModelProperty;

public class AccountBalance {

    @ApiModelProperty(value = "Currency", required = true, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Available balance", required = true, example = "50000.0")
    private Double balance;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
