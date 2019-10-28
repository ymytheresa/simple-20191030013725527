package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CreditCardPaymentResponse {

    @ApiModelProperty(value = "Unused credit", required = true, example = "5000")
    private Double credit_unused;

    public Double getCredit_unused() {
        return credit_unused;
    }

    public void setCredit_unused(Double credit_unused) {
        this.credit_unused = credit_unused;
    }

}
