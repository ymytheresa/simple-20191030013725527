package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MoneyTransferResponse {

    @ApiModelProperty(value = "Account balance after the transaction", required = true, example = "40500.0")
    private Double src_balance_after;

    @ApiModelProperty(value = "Name of the destination account", required = true, example = "CHAN Fin Tech")
    private String dst_account_name;

    public Double getSrc_balance_after() {
        return src_balance_after;
    }

    public void setSrc_balance_after(Double src_balance_after) {
        this.src_balance_after = src_balance_after;
    }

    public String getDst_account_name() {
        return dst_account_name;
    }

    public void setDst_account_name(String dst_account_name) {
        this.dst_account_name = dst_account_name;
    }
}
