package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CustomerProfile {

    @ApiModelProperty(value = "Full name", required = true, example = "CHAN Fin Tech")
    private String full_name;

    @ApiModelProperty(value = "Address line 1", required = true, example = "Flat 88, Floor 23, Hacking House, FinTech Garden")
    private String address1;

    @ApiModelProperty(value = "Address line 2", required = true, example = "Shau Kei Wan, HK")
    private String address2;

    @ApiModelProperty(value = "Address line 3", required = true, example = "Hong Kong")
    private String address3;

    @ApiModelProperty(value = "Contact phone number", required = true, example = "92222222")
    private String phone_no;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

}
