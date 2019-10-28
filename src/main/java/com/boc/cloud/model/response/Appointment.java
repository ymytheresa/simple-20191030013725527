package com.boc.cloud.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Appointment {

    @ApiModelProperty(value = "Unique ID of the appointment", required = true, example = "3")
    private String appointment_id;

    @ApiModelProperty(value = "Phone number", required = true, example = "61234567")
    private String phone_no;

    @ApiModelProperty(value = "Code of the branch reserved", required = true, example = "916")
    private String branch_code;

    @ApiModelProperty(value = "Time reserved", required = true, example = "")
    private String datetime;

    @ApiModelProperty(value = "Service required", required = true, allowableValues = "WEALTH_MANAGEMENT,PRIVATE_BANKING,CORP_BANKING,RMB_SERVICES,CROSS_BORDER_SERVICES,INVESTMENT,MORTGAGE,LOAN,OTHERS", example = "LOAN")
    private String requested_service_category;

    @ApiModelProperty(value = "Detail of service required", required = false, example = "Discuss about mortgage")
    private String requested_service_detail;

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRequested_service_category() {
        return requested_service_category;
    }

    public void setRequested_service_category(String requested_service_category) {
        this.requested_service_category = requested_service_category;
    }

    public String getRequested_service_detail() {
        return requested_service_detail;
    }

    public void setRequested_service_detail(String requested_service_detail) {
        this.requested_service_detail = requested_service_detail;
    }

}
