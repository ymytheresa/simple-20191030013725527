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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * OpeningHours
 */
public class OpeningHours {
  @SerializedName("mon2fri")
  private String mon2fri = null;

  @SerializedName("sat")
  private String sat = null;

  @SerializedName("sunholiday")
  private String sunholiday = null;

  public OpeningHours mon2fri(String mon2fri) {
    this.mon2fri = mon2fri;
    return this;
  }

   /**
   * Get mon2fri
   * @return mon2fri
  **/
  @ApiModelProperty(value = "")
  public String getMon2fri() {
    return mon2fri;
  }

  public void setMon2fri(String mon2fri) {
    this.mon2fri = mon2fri;
  }

  public OpeningHours sat(String sat) {
    this.sat = sat;
    return this;
  }

   /**
   * Get sat
   * @return sat
  **/
  @ApiModelProperty(value = "")
  public String getSat() {
    return sat;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }

  public OpeningHours sunholiday(String sunholiday) {
    this.sunholiday = sunholiday;
    return this;
  }

   /**
   * Get sunholiday
   * @return sunholiday
  **/
  @ApiModelProperty(value = "")
  public String getSunholiday() {
    return sunholiday;
  }

  public void setSunholiday(String sunholiday) {
    this.sunholiday = sunholiday;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OpeningHours openingHours = (OpeningHours) o;
    return Objects.equals(this.mon2fri, openingHours.mon2fri) &&
        Objects.equals(this.sat, openingHours.sat) &&
        Objects.equals(this.sunholiday, openingHours.sunholiday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mon2fri, sat, sunholiday);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OpeningHours {\n");
    
    sb.append("    mon2fri: ").append(toIndentedString(mon2fri)).append("\n");
    sb.append("    sat: ").append(toIndentedString(sat)).append("\n");
    sb.append("    sunholiday: ").append(toIndentedString(sunholiday)).append("\n");
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

