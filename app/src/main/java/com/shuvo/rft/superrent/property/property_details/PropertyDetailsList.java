package com.shuvo.rft.superrent.property.property_details;

public class PropertyDetailsList {
    private String rpd_id;
    private String rpd_rpm_id;
    private String rpd_floor_no;
    private String rpd_block_no;
    private String rpd_shop_no;
    private String rpd_measurement;
    private String rpd_current_renter_id;
    private String rri_name;
    private String rpd_current_renter_start_date;
    private String rpd_renter_rent_renew_type;
    private String rpd_renter_rent_renew_after;
    private String rpd_renter_type;

    public PropertyDetailsList(String rpd_id, String rpd_rpm_id, String rpd_floor_no, String rpd_block_no, String rpd_shop_no, String rpd_measurement, String rpd_current_renter_id, String rri_name, String rpd_current_renter_start_date, String rpd_renter_rent_renew_type, String rpd_renter_rent_renew_after, String rpd_renter_type) {
        this.rpd_id = rpd_id;
        this.rpd_rpm_id = rpd_rpm_id;
        this.rpd_floor_no = rpd_floor_no;
        this.rpd_block_no = rpd_block_no;
        this.rpd_shop_no = rpd_shop_no;
        this.rpd_measurement = rpd_measurement;
        this.rpd_current_renter_id = rpd_current_renter_id;
        this.rri_name = rri_name;
        this.rpd_current_renter_start_date = rpd_current_renter_start_date;
        this.rpd_renter_rent_renew_type = rpd_renter_rent_renew_type;
        this.rpd_renter_rent_renew_after = rpd_renter_rent_renew_after;
        this.rpd_renter_type = rpd_renter_type;
    }

    public String getRpd_id() {
        return rpd_id;
    }

    public void setRpd_id(String rpd_id) {
        this.rpd_id = rpd_id;
    }

    public String getRpd_rpm_id() {
        return rpd_rpm_id;
    }

    public void setRpd_rpm_id(String rpd_rpm_id) {
        this.rpd_rpm_id = rpd_rpm_id;
    }

    public String getRpd_floor_no() {
        return rpd_floor_no;
    }

    public void setRpd_floor_no(String rpd_floor_no) {
        this.rpd_floor_no = rpd_floor_no;
    }

    public String getRpd_block_no() {
        return rpd_block_no;
    }

    public void setRpd_block_no(String rpd_block_no) {
        this.rpd_block_no = rpd_block_no;
    }

    public String getRpd_shop_no() {
        return rpd_shop_no;
    }

    public void setRpd_shop_no(String rpd_shop_no) {
        this.rpd_shop_no = rpd_shop_no;
    }

    public String getRpd_measurement() {
        return rpd_measurement;
    }

    public void setRpd_measurement(String rpd_measurement) {
        this.rpd_measurement = rpd_measurement;
    }

    public String getRpd_current_renter_id() {
        return rpd_current_renter_id;
    }

    public void setRpd_current_renter_id(String rpd_current_renter_id) {
        this.rpd_current_renter_id = rpd_current_renter_id;
    }

    public String getRri_name() {
        return rri_name;
    }

    public void setRri_name(String rri_name) {
        this.rri_name = rri_name;
    }

    public String getRpd_current_renter_start_date() {
        return rpd_current_renter_start_date;
    }

    public void setRpd_current_renter_start_date(String rpd_current_renter_start_date) {
        this.rpd_current_renter_start_date = rpd_current_renter_start_date;
    }

    public String getRpd_renter_rent_renew_type() {
        return rpd_renter_rent_renew_type;
    }

    public void setRpd_renter_rent_renew_type(String rpd_renter_rent_renew_type) {
        this.rpd_renter_rent_renew_type = rpd_renter_rent_renew_type;
    }

    public String getRpd_renter_rent_renew_after() {
        return rpd_renter_rent_renew_after;
    }

    public void setRpd_renter_rent_renew_after(String rpd_renter_rent_renew_after) {
        this.rpd_renter_rent_renew_after = rpd_renter_rent_renew_after;
    }

    public String getRpd_renter_type() {
        return rpd_renter_type;
    }

    public void setRpd_renter_type(String rpd_renter_type) {
        this.rpd_renter_type = rpd_renter_type;
    }
}
