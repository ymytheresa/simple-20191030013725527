package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PersonalLoan {

    @ApiModelProperty(value = "Loan open date", required = true, example = "2018-02-05")
    private String open_date;

    @ApiModelProperty(value = "Original loaned amount", required = true, example = "1000000")
    private Long original_amount;

    @ApiModelProperty(value = "Currency of the loan (must be HKD)", required = true, allowableValues = "HKD", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Outstanding amount", required = true, example = "1000000")
    private Long amount;

    @ApiModelProperty(value = "Number of repayment terms", required = true, example = "24")
    private Long no_of_terms;

    @ApiModelProperty(value = "Annual interest rate", required = true, example = "2.7")
    private String rate;

    @ApiModelProperty(value = "Repayment amount in each installment", required = true, example = "226739.0")
    private Long installment_amount;

    @ApiModelProperty(value = "Repayment month day", required = true, example = "14")
    private Long installment_day;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getInstallment_amount() {
        return installment_amount;
    }

    public Long getInstallment_day() {
        return installment_day;
    }

    public Long getNo_of_terms() {
        return no_of_terms;
    }

    public String getOpen_date() {
        return open_date;
    }

    public Long getOriginal_amount() {
        return original_amount;
    }

    public String getRate() {
        return rate;
    }

    public String getRemark() {
        return remark;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void set(String currency) {
        this.currency = currency;
    }

    public void setInstallment_amount(Long installment_amount) {
        this.installment_amount = installment_amount;
    }

    public void setInstallment_day(Long installment_day) {
        this.installment_day = installment_day;
    }

    public void setNo_of_terms(Long no_of_terms) {
        this.no_of_terms = no_of_terms;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public void setOriginal_amount(Long original_amount) {
        this.original_amount = original_amount;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
