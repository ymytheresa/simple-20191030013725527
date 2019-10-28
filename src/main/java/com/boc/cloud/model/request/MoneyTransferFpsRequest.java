package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MoneyTransferFpsRequest {

    @ApiModelProperty(value = "Currency of amount to transfer", required = true, allowableValues = "HKD,USD,CNY,AUD,CAD,CHF,EUR,GBP,JPY,NZD,SGD,THB,BND,ZAR,DKK", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount to transfer", required = true, example = "150")
    private Double amount;

    @ApiModelProperty(value = "Type of information entered in dst_info, either FPS_PHONE_NO, FPS_EMAIL, or FPS_ID", required = true, example = "FPS_PHONE_NO")
    private String dst_info_type;

    @ApiModelProperty(value = "Information of the recipient, depending on dst_info_type", required = true, example = "61111111")
    private String dst_info;

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

    public String getDst_info_type() {
        return dst_info_type;
    }

    public void setDst_info_type(String dst_info_type) {
        this.dst_info_type = dst_info_type;
    }

    public String getDst_info() {
        return dst_info;
    }

    public void setDst_info(String dst_info) {
        this.dst_info = dst_info;
    }
}
