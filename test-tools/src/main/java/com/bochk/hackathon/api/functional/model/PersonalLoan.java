/*
 * BOCHK FinTech Hackathon 2018 API (Backend)
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.14 (20180124)
 * Contact: it_innovation_lab@bochk.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.bochk.hackathon.api.functional.model;

import java.util.Objects;
import com.bochk.hackathon.api.functional.model.Currency;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.time.LocalDate;

/**
 * PersonalLoan
 */
public class PersonalLoan {
  @SerializedName("open_date")
  private LocalDate openDate = null;

  @SerializedName("original_amount")
  private Long originalAmount = null;

  @SerializedName("currency")
  private Currency currency = null;

  @SerializedName("amount")
  private Long amount = null;

  @SerializedName("no_of_terms")
  private Long noOfTerms = null;

  @SerializedName("rate")
  private Double rate = null;

  @SerializedName("installment_amount")
  private Long installmentAmount = null;

  @SerializedName("installment_day")
  private Long installmentDay = null;

  @SerializedName("remark")
  private String remark = null;

  public PersonalLoan openDate(LocalDate openDate) {
    this.openDate = openDate;
    return this;
  }

   /**
   * Creation date of loan application
   * @return openDate
  **/
  @ApiModelProperty(required = true, value = "Creation date of loan application")
  public LocalDate getOpenDate() {
    return openDate;
  }

  public void setOpenDate(LocalDate openDate) {
    this.openDate = openDate;
  }

  public PersonalLoan originalAmount(Long originalAmount) {
    this.originalAmount = originalAmount;
    return this;
  }

   /**
   * Amount loaned (in dollar)
   * @return originalAmount
  **/
  @ApiModelProperty(required = true, value = "Amount loaned (in dollar)")
  public Long getOriginalAmount() {
    return originalAmount;
  }

  public void setOriginalAmount(Long originalAmount) {
    this.originalAmount = originalAmount;
  }

  public PersonalLoan currency(Currency currency) {
    this.currency = currency;
    return this;
  }

   /**
   * Get currency
   * @return currency
  **/
  @ApiModelProperty(required = true, value = "")
  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public PersonalLoan amount(Long amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Amount outstanding (in dollar)
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "Amount outstanding (in dollar)")
  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  public PersonalLoan noOfTerms(Long noOfTerms) {
    this.noOfTerms = noOfTerms;
    return this;
  }

   /**
   * Number of installments (monthly)
   * @return noOfTerms
  **/
  @ApiModelProperty(required = true, value = "Number of installments (monthly)")
  public Long getNoOfTerms() {
    return noOfTerms;
  }

  public void setNoOfTerms(Long noOfTerms) {
    this.noOfTerms = noOfTerms;
  }

  public PersonalLoan rate(Double rate) {
    this.rate = rate;
    return this;
  }

   /**
   * Annual interest rate (%)
   * @return rate
  **/
  @ApiModelProperty(required = true, value = "Annual interest rate (%)")
  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public PersonalLoan installmentAmount(Long installmentAmount) {
    this.installmentAmount = installmentAmount;
    return this;
  }

   /**
   * Amount to repay in each installment
   * @return installmentAmount
  **/
  @ApiModelProperty(required = true, value = "Amount to repay in each installment")
  public Long getInstallmentAmount() {
    return installmentAmount;
  }

  public void setInstallmentAmount(Long installmentAmount) {
    this.installmentAmount = installmentAmount;
  }

  public PersonalLoan installmentDay(Long installmentDay) {
    this.installmentDay = installmentDay;
    return this;
  }

   /**
   * Day of month to repay
   * minimum: 1
   * maximum: 31
   * @return installmentDay
  **/
  @ApiModelProperty(required = true, value = "Day of month to repay")
  public Long getInstallmentDay() {
    return installmentDay;
  }

  public void setInstallmentDay(Long installmentDay) {
    this.installmentDay = installmentDay;
  }

  public PersonalLoan remark(String remark) {
    this.remark = remark;
    return this;
  }

   /**
   * Free text remark
   * @return remark
  **/
  @ApiModelProperty(value = "Free text remark")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonalLoan personalLoan = (PersonalLoan) o;
    return Objects.equals(this.openDate, personalLoan.openDate) &&
        Objects.equals(this.originalAmount, personalLoan.originalAmount) &&
        Objects.equals(this.currency, personalLoan.currency) &&
        Objects.equals(this.amount, personalLoan.amount) &&
        Objects.equals(this.noOfTerms, personalLoan.noOfTerms) &&
        Objects.equals(this.rate, personalLoan.rate) &&
        Objects.equals(this.installmentAmount, personalLoan.installmentAmount) &&
        Objects.equals(this.installmentDay, personalLoan.installmentDay) &&
        Objects.equals(this.remark, personalLoan.remark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openDate, originalAmount, currency, amount, noOfTerms, rate, installmentAmount, installmentDay, remark);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonalLoan {\n");
    
    sb.append("    openDate: ").append(toIndentedString(openDate)).append("\n");
    sb.append("    originalAmount: ").append(toIndentedString(originalAmount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    noOfTerms: ").append(toIndentedString(noOfTerms)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
    sb.append("    installmentAmount: ").append(toIndentedString(installmentAmount)).append("\n");
    sb.append("    installmentDay: ").append(toIndentedString(installmentDay)).append("\n");
    sb.append("    remark: ").append(toIndentedString(remark)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

