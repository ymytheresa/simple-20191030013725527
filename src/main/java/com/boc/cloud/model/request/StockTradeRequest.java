package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StockTradeRequest {

    @ApiModelProperty(value = "Action to take", required = true, allowableValues = "BUY,SELL", example = "BUY")
    private String action;

    @ApiModelProperty(value = "Numeric stock code", required = true, example = "2388")
    private String stock_code;

    @ApiModelProperty(value = "Unit price of the stock", required = true, example = "38.8")
    private Double unit_price;

    @ApiModelProperty(value = "Quantity to buy or sell", required = true, example = "500")
    private Long quantity;

    @ApiModelProperty(value = "Number of the settlement account", required = true, example = "1287511111111")
    private String settlement_account_no;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getSettlement_account_no() {
        return settlement_account_no;
    }

    public void setSettlement_account_no(String settlement_account_no) {
        this.settlement_account_no = settlement_account_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
