package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PersonalLoanCheckRateResponse {

    @ApiModelProperty(value = "Currency to loan (must be HKD)", required = true, allowableValues = "HKD", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount to loan", required = true, example = "1000000")
    private Long amount;

    @ApiModelProperty(value = "Number of repayment terms", required = true, example = "24")
    private Long no_of_terms;

    @ApiModelProperty(value = "Annual interest rate", required = true, example = "2.7")
    private Double rate;

    @ApiModelProperty(value = "Repayment amount in each installment", required = true, example = "226739")
    private Long installment_amount;

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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(Long installment_amount) {
        this.installment_amount = installment_amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
