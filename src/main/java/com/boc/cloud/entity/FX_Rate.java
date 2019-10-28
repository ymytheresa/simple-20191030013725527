package com.boc.cloud.entity;

import io.swagger.annotations.ApiModelProperty;

public class FX_Rate extends BaseEntity {

    @ApiModelProperty(value = "Currency", required = true, allowableValues = "USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "USD")
    private String currency;

    @ApiModelProperty(value = "Exchange rate", required = true, example = "788.08")
    private String rate;

    @ApiModelProperty(value = "Description", required = true, example = "United States Dollar")
    private String description;

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getRate() {
        return rate;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

}
