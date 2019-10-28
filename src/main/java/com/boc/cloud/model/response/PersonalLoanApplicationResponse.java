package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PersonalLoanApplicationResponse {

    @ApiModelProperty(value = "Account number to receive the loaned amount", required = true, example = "1268813333332")
    private String account_no;

    @ApiModelProperty(value = "Currency to loan (must be HKD)", required = true, allowableValues = "HKD", example = "HKD")
    private String currency;

    @ApiModelProperty(value = "Annual interest rate", required = true, example = "2.7")
    private Double rate;

    @ApiModelProperty(value = "Amount loaned", required = true, example = "1000000")
    private Long amount;

    @ApiModelProperty(value = "Number of repayment terms", required = true, example = "24")
    private Long no_of_terms;

    @ApiModelProperty(value = "Repayment amount at each installment", required = true, example = "34011")
    private Long installment_amount;

    @ApiModelProperty(value = "Repayment month day (1-31)", required = true, example = "10")
    private Long installment_day;

    @ApiModelProperty(value = "Free-text remark", required = false, example = "")
    private String remark;

    @ApiModelProperty(value = "Balance of the account after receiving the loaned amount", required = true, example = "1500000")
    private Double balance_after;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
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

    public Long getInstallment_day() {
        return installment_day;
    }

    public void setInstallment_day(Long installment_day) {
        this.installment_day = installment_day;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getBalance_after() {
        return balance_after;
    }

    public void setBalance_after(Double balance_after) {
        this.balance_after = balance_after;
    }

}
