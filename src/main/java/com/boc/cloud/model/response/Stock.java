package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Stock {

    @ApiModelProperty(value = "Stock code", required = true, example = "2388")
    private String code;

    @ApiModelProperty(value = "Quantity owned", required = true, example = "500")
    private Long quantity;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
