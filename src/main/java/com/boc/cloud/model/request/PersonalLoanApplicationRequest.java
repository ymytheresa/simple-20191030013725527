package com.boc.cloud.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PersonalLoanApplicationRequest {

    @ApiModelProperty(value = "Account to receive loaned amount", required = true, example = "1268813333332")
    private String account_no;

    @ApiModelProperty(value = "Currency to loan (must be HKD)", required = true, allowableValues = "HKD", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Amount to loan", required = true, example = "150000")
    private Long amount;

    @ApiModelProperty(value = "Number of repayment terms", required = true, example = "24")
    private Long no_of_terms;

    @ApiModelProperty(value = "Annual interest rate", required = true, example = "2.7")
    private Double rate;

    @ApiModelProperty(value = "Repayment amount at each installment", required = true, example = "34011")
    private Long installment_amount;

    @ApiModelProperty(value = "Repayment month day (1-31)", required = true, example = "10")
    private Long installment_day;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Long getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(Long installment_amount) {
        this.installment_amount = installment_amount;
    }

    public Long getInstallment_day() {
        return installment_day;
    }

    public void setInstallment_day(Long installment_day) {
        this.installment_day = installment_day;
    }

}
