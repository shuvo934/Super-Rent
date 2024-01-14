package com.shuvo.rft.superrent.dashboard.arraylists;

public class FiveCollectLists {
    private String due_renter_name;
    private String rhmbd_bill_collect_date;
    private String rhmbd_collected_amt;
    private String sl;

    public FiveCollectLists(String due_renter_name, String rhmbd_bill_collect_date, String rhmbd_collected_amt, String sl) {
        this.due_renter_name = due_renter_name;
        this.rhmbd_bill_collect_date = rhmbd_bill_collect_date;
        this.rhmbd_collected_amt = rhmbd_collected_amt;
        this.sl = sl;
    }

    public String getDue_renter_name() {
        return due_renter_name;
    }

    public void setDue_renter_name(String due_renter_name) {
        this.due_renter_name = due_renter_name;
    }

    public String getRhmbd_bill_collect_date() {
        return rhmbd_bill_collect_date;
    }

    public void setRhmbd_bill_collect_date(String rhmbd_bill_collect_date) {
        this.rhmbd_bill_collect_date = rhmbd_bill_collect_date;
    }

    public String getRhmbd_collected_amt() {
        return rhmbd_collected_amt;
    }

    public void setRhmbd_collected_amt(String rhmbd_collected_amt) {
        this.rhmbd_collected_amt = rhmbd_collected_amt;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }
}
