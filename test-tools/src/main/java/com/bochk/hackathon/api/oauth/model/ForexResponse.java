/*
 * BOCHK FinTech Hackathon 2018 API (Frontend)
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.14 (20180124)
 * Contact: it_innovation_lab@bochk.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.bochk.hackathon.api.oauth.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * ForexResponse
 */
public class ForexResponse {
  @SerializedName("src_balance_after")
  private Double srcBalanceAfter = null;

  @SerializedName("dst_balance_after")
  private Double dstBalanceAfter = null;

  @SerializedName("rate")
  private Double rate = null;

  public ForexResponse srcBalanceAfter(Double srcBalanceAfter) {
    this.srcBalanceAfter = srcBalanceAfter;
    return this;
  }

   /**
   * Get srcBalanceAfter
   * @return srcBalanceAfter
  **/
  @ApiModelProperty(required = true, value = "")
  public Double getSrcBalanceAfter() {
    return srcBalanceAfter;
  }

  public void setSrcBalanceAfter(Double srcBalanceAfter) {
    this.srcBalanceAfter = srcBalanceAfter;
  }

  public ForexResponse dstBalanceAfter(Double dstBalanceAfter) {
    this.dstBalanceAfter = dstBalanceAfter;
    return this;
  }

   /**
   * Get dstBalanceAfter
   * @return dstBalanceAfter
  **/
  @ApiModelProperty(required = true, value = "")
  public Double getDstBalanceAfter() {
    return dstBalanceAfter;
  }

  public void setDstBalanceAfter(Double dstBalanceAfter) {
    this.dstBalanceAfter = dstBalanceAfter;
  }

  public ForexResponse rate(Double rate) {
    this.rate = rate;
    return this;
  }

   /**
   * Get rate
   * @return rate
  **/
  @ApiModelProperty(required = true, value = "")
  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ForexResponse forexResponse = (ForexResponse) o;
    return Objects.equals(this.srcBalanceAfter, forexResponse.srcBalanceAfter) &&
        Objects.equals(this.dstBalanceAfter, forexResponse.dstBalanceAfter) &&
        Objects.equals(this.rate, forexResponse.rate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(srcBalanceAfter, dstBalanceAfter, rate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForexResponse {\n");
    
    sb.append("    srcBalanceAfter: ").append(toIndentedString(srcBalanceAfter)).append("\n");
    sb.append("    dstBalanceAfter: ").append(toIndentedString(dstBalanceAfter)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
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
