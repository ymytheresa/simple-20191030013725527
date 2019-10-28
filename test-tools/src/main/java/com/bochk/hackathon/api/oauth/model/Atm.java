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
import com.bochk.hackathon.api.oauth.model.AddressCoordinates;
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
 * Atm
 */
public class Atm {
  @SerializedName("district_code")
  private String districtCode = null;

  @SerializedName("district_name")
  private String districtName = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("address")
  private String address = null;

  @SerializedName("address_coordinates")
  private AddressCoordinates addressCoordinates = null;

  @SerializedName("services")
  private List<String> services = new ArrayList<>();

  /**
   * Gets or Sets businessStatus
   */
  @JsonAdapter(BusinessStatusEnum.Adapter.class)
  public enum BusinessStatusEnum {
    UNCONGESTED("UNCONGESTED"),
    
    CONGESTED("CONGESTED"),
    
    VERY_CONGESTED("VERY_CONGESTED");

    private String value;

    BusinessStatusEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static BusinessStatusEnum fromValue(String text) {
      for (BusinessStatusEnum b : BusinessStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<BusinessStatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final BusinessStatusEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public BusinessStatusEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return BusinessStatusEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("business_status")
  private BusinessStatusEnum businessStatus = null;

  public Atm districtCode(String districtCode) {
    this.districtCode = districtCode;
    return this;
  }

   /**
   * Get districtCode
   * @return districtCode
  **/
  @ApiModelProperty(required = true, value = "")
  public String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(String districtCode) {
    this.districtCode = districtCode;
  }

  public Atm districtName(String districtName) {
    this.districtName = districtName;
    return this;
  }

   /**
   * Get districtName
   * @return districtName
  **/
  @ApiModelProperty(required = true, value = "")
  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public Atm name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(required = true, value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Atm address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(required = true, value = "")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Atm addressCoordinates(AddressCoordinates addressCoordinates) {
    this.addressCoordinates = addressCoordinates;
    return this;
  }

   /**
   * Get addressCoordinates
   * @return addressCoordinates
  **/
  @ApiModelProperty(required = true, value = "")
  public AddressCoordinates getAddressCoordinates() {
    return addressCoordinates;
  }

  public void setAddressCoordinates(AddressCoordinates addressCoordinates) {
    this.addressCoordinates = addressCoordinates;
  }

  public Atm services(List<String> services) {
    this.services = services;
    return this;
  }

  public Atm addServicesItem(String servicesItem) {
    this.services.add(servicesItem);
    return this;
  }

   /**
   * Get services
   * @return services
  **/
  @ApiModelProperty(example = "\"CASH_DEPOSIT\"", required = true, value = "")
  public List<String> getServices() {
    return services;
  }

  public void setServices(List<String> services) {
    this.services = services;
  }

  public Atm businessStatus(BusinessStatusEnum businessStatus) {
    this.businessStatus = businessStatus;
    return this;
  }

   /**
   * Get businessStatus
   * @return businessStatus
  **/
  @ApiModelProperty(value = "")
  public BusinessStatusEnum getBusinessStatus() {
    return businessStatus;
  }

  public void setBusinessStatus(BusinessStatusEnum businessStatus) {
    this.businessStatus = businessStatus;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Atm atm = (Atm) o;
    return Objects.equals(this.districtCode, atm.districtCode) &&
        Objects.equals(this.districtName, atm.districtName) &&
        Objects.equals(this.name, atm.name) &&
        Objects.equals(this.address, atm.address) &&
        Objects.equals(this.addressCoordinates, atm.addressCoordinates) &&
        Objects.equals(this.services, atm.services) &&
        Objects.equals(this.businessStatus, atm.businessStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtCode, districtName, name, address, addressCoordinates, services, businessStatus);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Atm {\n");
    
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    districtName: ").append(toIndentedString(districtName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    addressCoordinates: ").append(toIndentedString(addressCoordinates)).append("\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
    sb.append("    businessStatus: ").append(toIndentedString(businessStatus)).append("\n");
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

