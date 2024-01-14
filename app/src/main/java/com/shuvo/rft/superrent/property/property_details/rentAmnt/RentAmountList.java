package com.shuvo.rft.superrent.property.property_details.rentAmnt;

public class RentAmountList {
    private String prh_id;
    private String prh_rpd_id;
    private String floor_info;
    private String prh_current_amt;
    private String prh_previous_amt;
    private String prh_ryms_id;
    private String prh_apply_date;
    private String status;

    public RentAmountList(String prh_id, String prh_rpd_id, String floor_info, String prh_current_amt, String prh_previous_amt, String prh_ryms_id, String prh_apply_date, String status) {
        this.prh_id = prh_id;
        this.prh_rpd_id = prh_rpd_id;
        this.floor_info = floor_info;
        this.prh_current_amt = prh_current_amt;
        this.prh_previous_amt = prh_previous_amt;
        this.prh_ryms_id = prh_ryms_id;
        this.prh_apply_date = prh_apply_date;
        this.status = status;
    }

    public String getPrh_id() {
        return prh_id;
    }

    public void setPrh_id(String prh_id) {
        this.prh_id = prh_id;
    }

    public String getPrh_rpd_id() {
        return prh_rpd_id;
    }

    public void setPrh_rpd_id(String prh_rpd_id) {
        this.prh_rpd_id = prh_rpd_id;
    }

    public String getFloor_info() {
        return floor_info;
    }

    public void setFloor_info(String floor_info) {
        this.floor_info = floor_info;
    }

    public String getPrh_current_amt() {
        return prh_current_amt;
    }

    public void setPrh_current_amt(String prh_current_amt) {
        this.prh_current_amt = prh_current_amt;
    }

    public String getPrh_previous_amt() {
        return prh_previous_amt;
    }

    public void setPrh_previous_amt(String prh_previous_amt) {
        this.prh_previous_amt = prh_previous_amt;
    }

    public String getPrh_ryms_id() {
        return prh_ryms_id;
    }

    public void setPrh_ryms_id(String prh_ryms_id) {
        this.prh_ryms_id = prh_ryms_id;
    }

    public String getPrh_apply_date() {
        return prh_apply_date;
    }

    public void setPrh_apply_date(String prh_apply_date) {
        this.prh_apply_date = prh_apply_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
