package com.boc.cloud.entity;

import com.boc.cloud.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Atm extends BaseEntity {

    @ApiModelProperty(value = "District code", required = true, example = "central_western_district")
    private String district_code;

    @ApiModelProperty(value = "District name", required = true, example = "Central & Western District")
    private String district_name;

    @ApiModelProperty(value = "Name of the ATM", required = true, example = "3/F Bank of China Tower")
    private String name;

    @ApiModelProperty(value = "Address of where the ATM is located", required = true, example = "3/F Bank of China Tower, 1 Garden Road, HK")
    private String address;

    @ApiModelProperty(value = "Address of the ATM in coordinates", required = true)
    private AddressCoordinates address_coordinates;

    @ApiModelProperty(value = "Services offered", required = true, example = "ATM_CENTRE")
    private String[] services;

    @ApiModelProperty(value = "How congested the ATM is", required = false, example = "CONGESTED")
    private String business_status;

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressCoordinates getAddress_coordinates() {
        return address_coordinates;
    }

    public void setAddress_coordinates(AddressCoordinates address_coordinates) {
        this.address_coordinates = address_coordinates;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public static class AddressCoordinates {

        @ApiModelProperty(value = "Latitude", required = true, example = "22.2796675")
        private Double latitude;

        @ApiModelProperty(value = "Longitude", required = true, example = "114.1612875")
        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

    }

}
