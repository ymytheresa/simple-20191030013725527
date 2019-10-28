package com.boc.cloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Branch extends BaseEntity {

    @ApiModelProperty(value = "District code", required = true, example = "sha_tin_district")
    private String district_code;

    @ApiModelProperty(value = "District name", required = true, example = "Shatin District")
    private String district_name;

    @ApiModelProperty(value = "Branch code", required = true, example = "688")
    private String branch_code;

    @ApiModelProperty(value = "Name of the branch", required = true, example = "Fo Tan Branch")
    private String name;

    @ApiModelProperty(value = "Address of the branch", required = true, example = "No 2, 1/F Shatin Galleria, 18-24 Shan Mei Street, Fo Tan, New Territories")
    private String address;

    @ApiModelProperty(value = "Address of the branch in coordinates", required = true, example = "central_western_district")
    private AddressCoordinates address_coordinates;

    @ApiModelProperty(value = "Phone number", required = true, example = "+852 2691 7193")
    private String phone_no;

    @ApiModelProperty(value = "Opening hours", required = true)
    private OpeningHours opening_hours;

    @ApiModelProperty(value = "Services offered", required = true, example = "ATM_CENTRE")
    private String[] services;

    @ApiModelProperty(value = "How congested the branch is", required = false, example = "VERY_CONGESTED")
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

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
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

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
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
        @ApiModelProperty(value = "Latitude", required = true, example = "22.397241")
        private Double latitude;

        @ApiModelProperty(value = "Longitude", required = true, example = "114.193418")
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

    public static class OpeningHours {
        @ApiModelProperty(value = "Monday to Friday", required = false, example = "9:00 – 17:00")
        private String mon2fri;

        @ApiModelProperty(value = "Saturday", required = false, example = "9:00 – 13:00")
        private String sat;

        @ApiModelProperty(value = "Sunday and holidays", required = false, example = "Closed")
        private String sunholiday;

        public String getMon2fri() {
            return mon2fri;
        }

        public void setMon2fri(String mon2fri) {
            this.mon2fri = mon2fri;
        }

        public String getSat() {
            return sat;
        }

        public void setSat(String sat) {
            this.sat = sat;
        }

        public String getSunholiday() {
            return sunholiday;
        }

        public void setSunholiday(String sunholiday) {
            this.sunholiday = sunholiday;
        }
    }

}
