package com.shuvo.rft.superrent.dashboard.arraylists;

public class ChartValue {
    private String rhmbm_ryms_id;
    private String mon;
    private String total_collection;

    public ChartValue(String rhmbm_ryms_id, String mon, String total_collection) {
        this.rhmbm_ryms_id = rhmbm_ryms_id;
        this.mon = mon;
        this.total_collection = total_collection;
    }

    public String getRhmbm_ryms_id() {
        return rhmbm_ryms_id;
    }

    public void setRhmbm_ryms_id(String rhmbm_ryms_id) {
        this.rhmbm_ryms_id = rhmbm_ryms_id;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTotal_collection() {
        return total_collection;
    }

    public void setTotal_collection(String total_collection) {
        this.total_collection = total_collection;
    }
}
