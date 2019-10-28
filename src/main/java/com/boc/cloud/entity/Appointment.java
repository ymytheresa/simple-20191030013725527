package com.boc.cloud.entity;

public class Appointment extends BaseEntity {
    private String appointment_id;
    private String phone_no;
    private String branch_code;
    private String datetime;
    private String requested_service_category;
    private String requested_service_detail;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public String getRequested_service_category() {
        return requested_service_category;
    }

    public void setRequested_service_category(String requested_service_category) {
        this.requested_service_category = requested_service_category;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getRequested_service_detail() {
        return requested_service_detail;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setRequested_service_detail(String requested_service_detail) {
        this.requested_service_detail = requested_service_detail;
    }

}
