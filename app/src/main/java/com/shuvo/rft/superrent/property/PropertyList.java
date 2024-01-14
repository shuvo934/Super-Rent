package com.shuvo.rft.superrent.property;

import com.shuvo.rft.superrent.property.property_details.PropertyDetailsList;

import java.util.ArrayList;

public class PropertyList {
    private String rpm_id;
    private String rpm_prop_name;
    private String rpm_prop_address;
    private String rpm_prop_owner;
    private String rpm_prop_owner_contact;
    private String rpm_prop_lat;
    private String rpm_prop_long;
    private String rpm_prop_active_flag;
    private String rpm_notes;
    private ArrayList<PropertyDetailsList> propertyDetailsLists;
    private boolean updatedListValue;

    public PropertyList(String rpm_id, String rpm_prop_name, String rpm_prop_address, String rpm_prop_owner, String rpm_prop_owner_contact, String rpm_prop_lat, String rpm_prop_long, String rpm_prop_active_flag, String rpm_notes, ArrayList<PropertyDetailsList> propertyDetailsLists, boolean updatedListValue) {
        this.rpm_id = rpm_id;
        this.rpm_prop_name = rpm_prop_name;
        this.rpm_prop_address = rpm_prop_address;
        this.rpm_prop_owner = rpm_prop_owner;
        this.rpm_prop_owner_contact = rpm_prop_owner_contact;
        this.rpm_prop_lat = rpm_prop_lat;
        this.rpm_prop_long = rpm_prop_long;
        this.rpm_prop_active_flag = rpm_prop_active_flag;
        this.rpm_notes = rpm_notes;
        this.propertyDetailsLists = propertyDetailsLists;
        this.updatedListValue = updatedListValue;
    }

    public String getRpm_id() {
        return rpm_id;
    }

    public void setRpm_id(String rpm_id) {
        this.rpm_id = rpm_id;
    }

    public String getRpm_prop_name() {
        return rpm_prop_name;
    }

    public void setRpm_prop_name(String rpm_prop_name) {
        this.rpm_prop_name = rpm_prop_name;
    }

    public String getRpm_prop_address() {
        return rpm_prop_address;
    }

    public void setRpm_prop_address(String rpm_prop_address) {
        this.rpm_prop_address = rpm_prop_address;
    }

    public String getRpm_prop_owner() {
        return rpm_prop_owner;
    }

    public void setRpm_prop_owner(String rpm_prop_owner) {
        this.rpm_prop_owner = rpm_prop_owner;
    }

    public String getRpm_prop_owner_contact() {
        return rpm_prop_owner_contact;
    }

    public void setRpm_prop_owner_contact(String rpm_prop_owner_contact) {
        this.rpm_prop_owner_contact = rpm_prop_owner_contact;
    }

    public String getRpm_prop_lat() {
        return rpm_prop_lat;
    }

    public void setRpm_prop_lat(String rpm_prop_lat) {
        this.rpm_prop_lat = rpm_prop_lat;
    }

    public String getRpm_prop_long() {
        return rpm_prop_long;
    }

    public void setRpm_prop_long(String rpm_prop_long) {
        this.rpm_prop_long = rpm_prop_long;
    }

    public String getRpm_prop_active_flag() {
        return rpm_prop_active_flag;
    }

    public void setRpm_prop_active_flag(String rpm_prop_active_flag) {
        this.rpm_prop_active_flag = rpm_prop_active_flag;
    }

    public String getRpm_notes() {
        return rpm_notes;
    }

    public void setRpm_notes(String rpm_notes) {
        this.rpm_notes = rpm_notes;
    }

    public ArrayList<PropertyDetailsList> getPropertyDetailsLists() {
        return propertyDetailsLists;
    }

    public void setPropertyDetailsLists(ArrayList<PropertyDetailsList> propertyDetailsLists) {
        this.propertyDetailsLists = propertyDetailsLists;
    }

    public boolean isUpdatedListValue() {
        return updatedListValue;
    }

    public void setUpdatedListValue(boolean updatedListValue) {
        this.updatedListValue = updatedListValue;
    }
}
