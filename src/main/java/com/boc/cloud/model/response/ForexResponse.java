package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ForexResponse {

    @ApiModelProperty(value = "Source account balance after the transaction", required = true, example = "40500.0")
    private Double src_balance_after;

    @ApiModelProperty(value = "Recipient account balance after the transaction", required = true, example = "12500.0")
    private Double dst_balance_after;

    @ApiModelProperty(value = "Exchange rate", required = true, example = "788.08")
    private String rate;

    public Double getSrc_balance_after() {
        return src_balance_after;
    }

    public void setSrc_balance_after(Double src_balance_after) {
        this.src_balance_after = src_balance_after;
    }

    public Double getDst_balance_after() {
        return dst_balance_after;
    }

    public void setDst_balance_after(Double dst_balance_after) {
        this.dst_balance_after = dst_balance_after;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

}
