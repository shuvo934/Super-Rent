package com.shuvo.rft.superrent.renter;

public class RenterList {
    private String rri_id;
    private String rri_name;
    private String rri_national_id;
    private String rri_contact;
    private String rri_email;
    private String rri_business_company_name;
    private String rri_active_flag;

    public RenterList(String rri_id, String rri_name, String rri_national_id, String rri_contact, String rri_email, String rri_business_company_name, String rri_active_flag) {
        this.rri_id = rri_id;
        this.rri_name = rri_name;
        this.rri_national_id = rri_national_id;
        this.rri_contact = rri_contact;
        this.rri_email = rri_email;
        this.rri_business_company_name = rri_business_company_name;
        this.rri_active_flag = rri_active_flag;
    }

    public String getRri_id() {
        return rri_id;
    }

    public void setRri_id(String rri_id) {
        this.rri_id = rri_id;
    }

    public String getRri_name() {
        return rri_name;
    }

    public void setRri_name(String rri_name) {
        this.rri_name = rri_name;
    }

    public String getRri_national_id() {
        return rri_national_id;
    }

    public void setRri_national_id(String rri_national_id) {
        this.rri_national_id = rri_national_id;
    }

    public String getRri_contact() {
        return rri_contact;
    }

    public void setRri_contact(String rri_contact) {
        this.rri_contact = rri_contact;
    }

    public String getRri_email() {
        return rri_email;
    }

    public void setRri_email(String rri_email) {
        this.rri_email = rri_email;
    }

    public String getRri_business_company_name() {
        return rri_business_company_name;
    }

    public void setRri_business_company_name(String rri_business_company_name) {
        this.rri_business_company_name = rri_business_company_name;
    }

    public String getRri_active_flag() {
        return rri_active_flag;
    }

    public void setRri_active_flag(String rri_active_flag) {
        this.rri_active_flag = rri_active_flag;
    }
}
