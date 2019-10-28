package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ForexRequest {

    @ApiModelProperty(value = "Original currency", required = true, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "HKD")
    private String src_currency;

    @ApiModelProperty(value = "Destination account number", required = true, example = "1287512222221")
    private String dst_account_no;

    @ApiModelProperty(value = "Currency to convert to", required = true, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "CNY")
    private String dst_currency;

    @ApiModelProperty(value = "Amount of destination currency to get", required = true, example = "200")
    private Double dst_amount;

    @ApiModelProperty(value = "Exchange rate against HKD100", required = true, example = "115.64")
    private Double rate;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getDst_account_no() {
        return dst_account_no;
    }

    public void setDst_account_no(String dst_account_no) {
        this.dst_account_no = dst_account_no;
    }

    public Double getDst_amount() {
        return dst_amount;
    }

    public void setDst_amount(Double dst_amount) {
        this.dst_amount = dst_amount;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getSrc_currency() {
        return src_currency;
    }

    public void setSrc_currency(String src_currency) {
        this.src_currency = src_currency;
    }

    public String getDst_currency() {
        return dst_currency;
    }

    public void setDst_currency(String dst_currency) {
        this.dst_currency = dst_currency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
