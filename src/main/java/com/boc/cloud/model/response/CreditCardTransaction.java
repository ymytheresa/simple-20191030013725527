package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CreditCardTransaction {

    @ApiModelProperty(value = "Timestamp of the transaction", required = true, example = "2018-02-05T11:29:58Z")
    private String datetime;

    @ApiModelProperty(value = "Credit card number", required = true, example = "4163180000008206")
    private String card_no;

    @ApiModelProperty(value = "Currency", required = true, allowableValues = "HKD,USD,CNY", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount", required = true, example = "400.0")
    private Double amount;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

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
