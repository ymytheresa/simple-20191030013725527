package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PersonalLoanCheckRateRequest {

    @ApiModelProperty(value = "Currency to loan (must be HKD)", required = true, allowableValues = "HKD", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount to loan", required = true, example = "100000")
    private Long amount;

    @ApiModelProperty(value = "Number of repayment terms", required = true, example = "12")
    private Long no_of_terms;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getNo_of_terms() {
        return no_of_terms;
    }

    public void setNo_of_terms(Long no_of_terms) {
        this.no_of_terms = no_of_terms;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
