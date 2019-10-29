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
import com.bochk.hackathon.api.functional.model.AccountBalance;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountSummary
 */
public class AccountSummary {
  @SerializedName("account_no")
  private String accountNo = null;

  @SerializedName("balances")
  private List<AccountBalance> balances = new ArrayList<>();

  public AccountSummary accountNo(String accountNo) {
    this.accountNo = accountNo;
    return this;
  }

   /**
   * Get accountNo
   * @return accountNo
  **/
  @ApiModelProperty(required = true, value = "")
  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public AccountSummary balances(List<AccountBalance> balances) {
    this.balances = balances;
    return this;
  }

  public AccountSummary addBalancesItem(AccountBalance balancesItem) {
    this.balances.add(balancesItem);
    return this;
  }

   /**
   * Get balances
   * @return balances
  **/
  @ApiModelProperty(required = true, value = "")
  public List<AccountBalance> getBalances() {
    return balances;
  }

  public void setBalances(List<AccountBalance> balances) {
    this.balances = balances;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountSummary accountSummary = (AccountSummary) o;
    return Objects.equals(this.accountNo, accountSummary.accountNo) &&
        Objects.equals(this.balances, accountSummary.balances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountNo, balances);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountSummary {\n");
    
    sb.append("    accountNo: ").append(toIndentedString(accountNo)).append("\n");
    sb.append("    balances: ").append(toIndentedString(balances)).append("\n");
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
