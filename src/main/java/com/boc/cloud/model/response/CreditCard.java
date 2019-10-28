package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CreditCard {

    @ApiModelProperty(value = "Credit card number", required = true, example = "4163180000008206")
    private String card_no;

    @ApiModelProperty(value = "Credit card type", required = true, allowableValues = "VISA,MASTERCARD,UNIONPAY", example = "VISA")
    private String card_type;

    @ApiModelProperty(value = "Open date of the credit card", required = true, example = "2018-01-09")
    private String open_date;

    @ApiModelProperty(value = "Currency", required = true, allowableValues = "HKD,USD,CNY", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "", required = true, example = "500000.0")
    private Double credit_limit;

    @ApiModelProperty(value = "", required = true, example = "0.0")
    private Double credit_used;

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(Double credit_limit) {
        this.credit_limit = credit_limit;
    }

    public Double getCredit_used() {
        return credit_used;
    }

    public void setCredit_used(Double credit_used) {
        this.credit_used = credit_used;
    }

}
