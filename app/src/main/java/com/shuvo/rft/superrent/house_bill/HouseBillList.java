package com.shuvo.rft.superrent.house_bill;

import com.shuvo.rft.superrent.house_bill.house_bill_details.HouseBillDetailsList;

import java.util.ArrayList;

public class HouseBillList {
    private String rpm_prop_name;
    private String month_name;
    private String rhmbm_id;
    private String rpm_id;
    private String ryms_id;
    private ArrayList<HouseBillDetailsList> houseBillDetailsLists;
    private boolean updated;

    public HouseBillList(String rpm_prop_name, String month_name, String rhmbm_id, String rpm_id, String ryms_id, ArrayList<HouseBillDetailsList> houseBillDetailsLists, boolean updated) {
        this.rpm_prop_name = rpm_prop_name;
        this.month_name = month_name;
        this.rhmbm_id = rhmbm_id;
        this.rpm_id = rpm_id;
        this.ryms_id = ryms_id;
        this.houseBillDetailsLists = houseBillDetailsLists;
        this.updated = updated;
    }

    public String getRpm_prop_name() {
        return rpm_prop_name;
    }

    public void setRpm_prop_name(String rpm_prop_name) {
        this.rpm_prop_name = rpm_prop_name;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getRhmbm_id() {
        return rhmbm_id;
    }

    public void setRhmbm_id(String rhmbm_id) {
        this.rhmbm_id = rhmbm_id;
    }

    public String getRpm_id() {
        return rpm_id;
    }

    public void setRpm_id(String rpm_id) {
        this.rpm_id = rpm_id;
    }

    public String getRyms_id() {
        return ryms_id;
    }

    public void setRyms_id(String ryms_id) {
        this.ryms_id = ryms_id;
    }

    public ArrayList<HouseBillDetailsList> getHouseBillDetailsLists() {
        return houseBillDetailsLists;
    }

    public void setHouseBillDetailsLists(ArrayList<HouseBillDetailsList> houseBillDetailsLists) {
        this.houseBillDetailsLists = houseBillDetailsLists;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
