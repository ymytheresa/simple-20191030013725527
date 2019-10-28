package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StockTradeResponse {

    @ApiModelProperty(value = "Balance of the settlement account after the stock transaction", required = true, example = "5003.2")
    private Double settlement_account_balance_after;

    public Double getSettlement_account_balance_after() {
        return settlement_account_balance_after;
    }

    public void setSettlement_account_balance_after(Double settlement_account_balance_after) {
        this.settlement_account_balance_after = settlement_account_balance_after;
    }
}
