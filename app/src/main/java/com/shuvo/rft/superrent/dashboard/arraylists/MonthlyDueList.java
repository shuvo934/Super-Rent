package com.shuvo.rft.superrent.dashboard.arraylists;

public class MonthlyDueList {
    private String renter_name;
    private String flat_no;
    private String due_amount;

    public MonthlyDueList(String renter_name, String flat_no, String due_amount) {
        this.renter_name = renter_name;
        this.flat_no = flat_no;
        this.due_amount = due_amount;
    }

    public String getRenter_name() {
        return renter_name;
    }

    public void setRenter_name(String renter_name) {
        this.renter_name = renter_name;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(String due_amount) {
        this.due_amount = due_amount;
    }
}
