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
import java.time.OffsetDateTime;

/**
 * Appointment
 */
public class Appointment {
  @SerializedName("appointment_id")
  private String appointmentId = null;

  @SerializedName("phone_no")
  private String phoneNo = null;

  @SerializedName("branch_code")
  private String branchCode = null;

  @SerializedName("datetime")
  private OffsetDateTime datetime = null;

  /**
   * Gets or Sets requestedServiceCategory
   */
  @JsonAdapter(RequestedServiceCategoryEnum.Adapter.class)
  public enum RequestedServiceCategoryEnum {
    WEALTH_MANAGEMENT("WEALTH_MANAGEMENT"),
    
    PRIVATE_BANKING("PRIVATE_BANKING"),
    
    CORP_BANKING("CORP_BANKING"),
    
    RMB_SERVICES("RMB_SERVICES"),
    
    CROSS_BORDER_SERVICES("CROSS_BORDER_SERVICES"),
    
    INVESTMENT("INVESTMENT"),
    
    MORTGAGE("MORTGAGE"),
    
    LOAN("LOAN"),
    
    OTHERS("OTHERS");

    private String value;

    RequestedServiceCategoryEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static RequestedServiceCategoryEnum fromValue(String text) {
      for (RequestedServiceCategoryEnum b : RequestedServiceCategoryEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<RequestedServiceCategoryEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RequestedServiceCategoryEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public RequestedServiceCategoryEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return RequestedServiceCategoryEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("requested_service_category")
  private RequestedServiceCategoryEnum requestedServiceCategory = null;

  @SerializedName("requested_service_detail")
  private String requestedServiceDetail = null;

  public Appointment appointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
    return this;
  }

   /**
   * Get appointmentId
   * @return appointmentId
  **/
  @ApiModelProperty(required = true, value = "")
  public String getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }

  public Appointment phoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
    return this;
  }

   /**
   * Get phoneNo
   * @return phoneNo
  **/
  @ApiModelProperty(required = true, value = "")
  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public Appointment branchCode(String branchCode) {
    this.branchCode = branchCode;
    return this;
  }

   /**
   * Get branchCode
   * @return branchCode
  **/
  @ApiModelProperty(required = true, value = "")
  public String getBranchCode() {
    return branchCode;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  public Appointment datetime(OffsetDateTime datetime) {
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

  public Appointment requestedServiceCategory(RequestedServiceCategoryEnum requestedServiceCategory) {
    this.requestedServiceCategory = requestedServiceCategory;
    return this;
  }

   /**
   * Get requestedServiceCategory
   * @return requestedServiceCategory
  **/
  @ApiModelProperty(required = true, value = "")
  public RequestedServiceCategoryEnum getRequestedServiceCategory() {
    return requestedServiceCategory;
  }

  public void setRequestedServiceCategory(RequestedServiceCategoryEnum requestedServiceCategory) {
    this.requestedServiceCategory = requestedServiceCategory;
  }

  public Appointment requestedServiceDetail(String requestedServiceDetail) {
    this.requestedServiceDetail = requestedServiceDetail;
    return this;
  }

   /**
   * Get requestedServiceDetail
   * @return requestedServiceDetail
  **/
  @ApiModelProperty(value = "")
  public String getRequestedServiceDetail() {
    return requestedServiceDetail;
  }

  public void setRequestedServiceDetail(String requestedServiceDetail) {
    this.requestedServiceDetail = requestedServiceDetail;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Appointment appointment = (Appointment) o;
    return Objects.equals(this.appointmentId, appointment.appointmentId) &&
        Objects.equals(this.phoneNo, appointment.phoneNo) &&
        Objects.equals(this.branchCode, appointment.branchCode) &&
        Objects.equals(this.datetime, appointment.datetime) &&
        Objects.equals(this.requestedServiceCategory, appointment.requestedServiceCategory) &&
        Objects.equals(this.requestedServiceDetail, appointment.requestedServiceDetail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appointmentId, phoneNo, branchCode, datetime, requestedServiceCategory, requestedServiceDetail);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Appointment {\n");
    
    sb.append("    appointmentId: ").append(toIndentedString(appointmentId)).append("\n");
    sb.append("    phoneNo: ").append(toIndentedString(phoneNo)).append("\n");
    sb.append("    branchCode: ").append(toIndentedString(branchCode)).append("\n");
    sb.append("    datetime: ").append(toIndentedString(datetime)).append("\n");
    sb.append("    requestedServiceCategory: ").append(toIndentedString(requestedServiceCategory)).append("\n");
    sb.append("    requestedServiceDetail: ").append(toIndentedString(requestedServiceDetail)).append("\n");
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

