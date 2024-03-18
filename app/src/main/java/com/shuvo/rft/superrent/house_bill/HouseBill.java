package com.shuvo.rft.superrent.house_bill;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.house_bill.house_bill_details.HouseBillDetailsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class HouseBill extends AppCompatActivity {

    RecyclerView recyclerView;
    HouseBillListAdapter houseBillListAdapter;
    RecyclerView.LayoutManager layoutManager;
    public static RelativeLayout houseBillLayout;
    public static int hBNewButtonHandler = 0;
    public static CircularProgressIndicator hb_info_circularProgressIndicator;

    private Boolean conn = false;
    private Boolean connected = false;
    ArrayList<HouseBillList> houseBillLists;

    ExtendedFloatingActionButton floatingActionButton;
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_bill);
        houseBillLayout = findViewById(R.id.house_bill_layout);
        hb_info_circularProgressIndicator = findViewById(R.id.progress_indicator_house_bill);
        hb_info_circularProgressIndicator.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.house_bill_list_view);

        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        user_code = userInfoLists.get(0).getUser_name();

        floatingActionButton = findViewById(R.id.new_house_bill_add_button);

        floatingActionButton.setOnClickListener(view -> {
            if (hBNewButtonHandler == 0) {
                Intent intent = new Intent(HouseBill.this, AddHouseBill.class);
                startActivity(intent);
            }
            else if (hBNewButtonHandler == 1) {
                Toast.makeText(getApplicationContext(), "Please wait while loading", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHouseBill();
    }

    @Override
    public void onBackPressed() {
        if (hBNewButtonHandler == 1) {
            Toast.makeText(getApplicationContext(), "Please wait while loading", Toast.LENGTH_SHORT).show();
        }
        else {
            finish();
        }
    }

    public void getHouseBill() {
        hb_info_circularProgressIndicator.setVisibility(View.VISIBLE);
        houseBillLayout.setVisibility(View.GONE);
        hBNewButtonHandler = 1;
        conn = false;
        connected = false;
        houseBillLists = new ArrayList<>();
        String billInfo = "http://119.18.148.32:8080/super/superrent_app/bill/monthlybillinfo/"+user_code;

        RequestQueue requestQueue = Volley.newRequestQueue(HouseBill.this);

        StringRequest renterReq = new StringRequest(Request.Method.GET, billInfo, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String rpm_prop_name = info.getString("rpm_prop_name")
                                .equals("null") ? "" : info.getString("rpm_prop_name");

                        String month_name = info.getString("month_name")
                                .equals("null") ? "Not Found" : info.getString("month_name");

                        String rhmbm_id = info.getString("rhmbm_id")
                                .equals("null") ? "Not Found" : info.getString("rhmbm_id");
                        String rpm_id = info.getString("rpm_id")
                                .equals("null") ? "Not Found" : info.getString("rpm_id");
                        String ryms_id = info.getString("ryms_id")
                                .equals("null") ? "Not Found" : info.getString("ryms_id");

                        houseBillLists.add(new HouseBillList(rpm_prop_name,month_name,rhmbm_id,
                                rpm_id,ryms_id,new ArrayList<>(),false));

                    }
                }
                checkDetailsValue();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(renterReq);
    }

    public void checkDetailsValue() {
        boolean finalVal = false;
        if (houseBillLists.size() != 0) {
            for (int i = 0; i < houseBillLists.size(); i++) {
                finalVal = houseBillLists.get(i).isUpdated();
                boolean val = houseBillLists.get(i).isUpdated();
                if (!val) {
                    String rhmbm_id = houseBillLists.get(i).getRhmbm_id();
                    String rpm_id = houseBillLists.get(i).getRpm_id();
                    String ryms_id = houseBillLists.get(i).getRyms_id();
                    getHouseBillDetails(rhmbm_id, rpm_id, ryms_id, i);
                    break;
                }
            }
            if (finalVal) {
                System.out.println("HERE WE GO");
                connected = true;
                updateLayout();
            }
        }
        else {
            connected = true;
            updateLayout();
        }
    }

    public void getHouseBillDetails(String rhmbm_id, String rpm_id, String ryms_id, int index) {
        String url = "http://119.18.148.32:8080/super/superrent_app/bill/billdtlgetinfo/"+rhmbm_id+"/"+rpm_id+"/"+ryms_id+"/"+user_code;
        RequestQueue requestQueue = Volley.newRequestQueue(HouseBill.this);
        ArrayList<HouseBillDetailsList> houseBillDetailsLists = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detailsInfo = array.getJSONObject(i);
                        String rhmbd_id = detailsInfo.getString("rhmbd_id");
                        String rhmbd_rhmbm_id = detailsInfo.getString("rhmbd_rhmbm_id");

                        String rhmbd_rri_id = detailsInfo.getString("rhmbd_rri_id")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_rri_id");
                        String rsmbd_rpd_id = detailsInfo.getString("rsmbd_rpd_id")
                                .equals("null") ? "" : detailsInfo.getString("rsmbd_rpd_id");
                        String renter_info = detailsInfo.getString("renter_info")
                                .equals("null") ? "" : detailsInfo.getString("renter_info");
                        String rhmbd_ryms_id = detailsInfo.getString("rhmbd_ryms_id")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_ryms_id");
                        String rhmbd_monthly_bill_amt = detailsInfo.getString("rhmbd_monthly_bill_amt")
                                .equals("null") ? "0" : detailsInfo.getString("rhmbd_monthly_bill_amt");
                        String rhmbd_due_amt = detailsInfo.getString("rhmbd_due_amt")
                                .equals("null") ? "0" : detailsInfo.getString("rhmbd_due_amt");
                        String rhmbd_monthly_extra_charge = detailsInfo.getString("rhmbd_monthly_extra_charge")
                                .equals("null") ? "0" : detailsInfo.getString("rhmbd_monthly_extra_charge");
                        String rhmbd_total_bill_amt = detailsInfo.getString("rhmbd_total_bill_amt")
                                .equals("null") ? "0" :  detailsInfo.getString("rhmbd_total_bill_amt");
                        String rhmbd_collected_amt = detailsInfo.getString("rhmbd_collected_amt")
                                .equals("null") ? "0" : detailsInfo.getString("rhmbd_collected_amt");
                        String rhmbd_bill_date = detailsInfo.getString("rhmbd_bill_date")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_date");
                        String rhmbd_bill_collect_date = detailsInfo.getString("rhmbd_bill_collect_date")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_collect_date");
                        String rhmbd_bill_expire_date = detailsInfo.getString("rhmbd_bill_expire_date")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_expire_date");
                        String rhmbd_notes = detailsInfo.getString("rhmbd_notes")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_notes");
                        String rhmbd_bill_prepared_by = detailsInfo.getString("rhmbd_bill_prepared_by")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_prepared_by");
                        String rhmbd_bill_prepared_date = detailsInfo.getString("rhmbd_bill_prepared_date")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_prepared_date");
                        String rhmbd_bill_last_updated_by = detailsInfo.getString("rhmbd_bill_last_updated_by")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_last_updated_by");
                        String rhmbd_bill_last_updated_date = detailsInfo.getString("rhmbd_bill_last_updated_date")
                                .equals("null") ? "" : detailsInfo.getString("rhmbd_bill_last_updated_date");

                        houseBillDetailsLists.add(new HouseBillDetailsList(rhmbd_id,rhmbd_rhmbm_id,rhmbd_rri_id,rsmbd_rpd_id,
                                renter_info,rhmbd_ryms_id,rhmbd_monthly_bill_amt,rhmbd_due_amt,rhmbd_monthly_extra_charge,
                                rhmbd_total_bill_amt,rhmbd_collected_amt,rhmbd_bill_date,rhmbd_bill_collect_date,
                                rhmbd_bill_expire_date,rhmbd_notes,rhmbd_bill_prepared_by,rhmbd_bill_prepared_date,
                                rhmbd_bill_last_updated_by,rhmbd_bill_last_updated_date));
                    }
                }

                houseBillLists.get(index).setHouseBillDetailsLists(houseBillDetailsLists);
                houseBillLists.get(index).setUpdated(true);
                checkDetailsValue();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(stringRequest);
    }

    public void updateLayout() {
        hb_info_circularProgressIndicator.setVisibility(View.GONE);
        houseBillLayout.setVisibility(View.VISIBLE);
        hBNewButtonHandler = 0;
        if (conn) {
            if (connected) {
                houseBillListAdapter = new HouseBillListAdapter(houseBillLists, HouseBill.this);
                ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(houseBillListAdapter);
                animationAdapter.setDuration(500);
                animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
                animationAdapter.setFirstOnly(false);
                recyclerView.setAdapter(animationAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(HouseBill.this)
                        .setMessage("There is a network issue in the server. Please try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getHouseBill();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(view -> {
                    dialog.dismiss();
                    finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(HouseBill.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Exit", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getHouseBill();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(view -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}