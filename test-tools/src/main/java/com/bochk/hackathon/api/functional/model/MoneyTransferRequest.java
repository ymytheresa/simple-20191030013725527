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

/**
 * MoneyTransferRequest
 */
public class MoneyTransferRequest {
  @SerializedName("currency")
  private Currency currency = null;

  @SerializedName("amount")
  private Double amount = null;

  @SerializedName("dst_account_no")
  private String dstAccountNo = null;

  @SerializedName("remark")
  private String remark = null;

  public MoneyTransferRequest currency(Currency currency) {
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

  public MoneyTransferRequest amount(Double amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Amount to transfer (in dollar)
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "Amount to transfer (in dollar)")
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public MoneyTransferRequest dstAccountNo(String dstAccountNo) {
    this.dstAccountNo = dstAccountNo;
    return this;
  }

   /**
   * Account to receive money
   * @return dstAccountNo
  **/
  @ApiModelProperty(required = true, value = "Account to receive money")
  public String getDstAccountNo() {
    return dstAccountNo;
  }

  public void setDstAccountNo(String dstAccountNo) {
    this.dstAccountNo = dstAccountNo;
  }

  public MoneyTransferRequest remark(String remark) {
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
    MoneyTransferRequest moneyTransferRequest = (MoneyTransferRequest) o;
    return Objects.equals(this.currency, moneyTransferRequest.currency) &&
        Objects.equals(this.amount, moneyTransferRequest.amount) &&
        Objects.equals(this.dstAccountNo, moneyTransferRequest.dstAccountNo) &&
        Objects.equals(this.remark, moneyTransferRequest.remark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currency, amount, dstAccountNo, remark);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MoneyTransferRequest {\n");
    
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    dstAccountNo: ").append(toIndentedString(dstAccountNo)).append("\n");
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

