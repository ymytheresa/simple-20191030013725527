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
import java.time.OffsetDateTime;

/**
 * CreditCardTransaction
 */
public class CreditCardTransaction {
  @SerializedName("datetime")
  private OffsetDateTime datetime = null;

  @SerializedName("card_no")
  private String cardNo = null;

  @SerializedName("currency")
  private Currency currency = null;

  @SerializedName("amount")
  private Double amount = null;

  @SerializedName("remark")
  private String remark = null;

  public CreditCardTransaction datetime(OffsetDateTime datetime) {
    this.datetime = datetime;
    return this;
  }

   /**
   * Get datetime
   * @return datetime
  **/
  @ApiModelProperty(required = true, value = "")
  public OffsetDateTime getDatetime() {
    return datetime;
  }

  public void setDatetime(OffsetDateTime datetime) {
    this.datetime = datetime;
  }

  public CreditCardTransaction cardNo(String cardNo) {
    this.cardNo = cardNo;
    return this;
  }

   /**
   * Get cardNo
   * @return cardNo
  **/
  @ApiModelProperty(required = true, value = "")
  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public CreditCardTransaction currency(Currency currency) {
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

  public CreditCardTransaction amount(Double amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Get amount
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "")
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public CreditCardTransaction remark(String remark) {
    this.remark = remark;
    return this;
  }

   /**
   * Get remark
   * @return remark
  **/
  @ApiModelProperty(value = "")
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
    CreditCardTransaction creditCardTransaction = (CreditCardTransaction) o;
    return Objects.equals(this.datetime, creditCardTransaction.datetime) &&
        Objects.equals(this.cardNo, creditCardTransaction.cardNo) &&
        Objects.equals(this.currency, creditCardTransaction.currency) &&
        Objects.equals(this.amount, creditCardTransaction.amount) &&
        Objects.equals(this.remark, creditCardTransaction.remark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(datetime, cardNo, currency, amount, remark);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditCardTransaction {\n");
    
    sb.append("    datetime: ").append(toIndentedString(datetime)).append("\n");
    sb.append("    cardNo: ").append(toIndentedString(cardNo)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
