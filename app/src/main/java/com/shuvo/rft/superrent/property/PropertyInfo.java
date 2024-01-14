package com.shuvo.rft.superrent.property;

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
import com.shuvo.rft.superrent.property.property_details.PropertyDetailsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class PropertyInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    PropertyListAdapter propertyListAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static RelativeLayout layout;
    public static int PrNewButtonHandler = 0;
    public static CircularProgressIndicator p_info_circularProgressIndicator;

    private Boolean conn = false;
    private Boolean connected = false;
    ArrayList<PropertyList> propertyLists;

    ExtendedFloatingActionButton floatingActionButton;
    String user_code ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info);

        layout = findViewById(R.id.property_layout);
        p_info_circularProgressIndicator = findViewById(R.id.progress_indicator_property_info);
        p_info_circularProgressIndicator.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.property_list_view);

        user_code = userInfoLists.get(0).getUser_name();

        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        floatingActionButton = findViewById(R.id.new_property_add_button);

        floatingActionButton.setOnClickListener(view -> {
            if (PrNewButtonHandler == 0) {
                Intent intent = new Intent(PropertyInfo.this, ProperrtyModify.class);
                startActivity(intent);
            }
            else if (PrNewButtonHandler == 1) {
                Toast.makeText(getApplicationContext(), "Please wait while loading", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getProperty();
    }

    @Override
    public void onBackPressed() {
        if (PrNewButtonHandler == 1) {
            Toast.makeText(getApplicationContext(), "Please wait while loading", Toast.LENGTH_SHORT).show();
        }
        else {
            finish();
        }
    }

    public void getProperty() {
        p_info_circularProgressIndicator.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        PrNewButtonHandler = 1;
        conn = false;
        connected = false;
        propertyLists = new ArrayList<>();

        String renterUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/property/"+user_code;

        RequestQueue requestQueue = Volley.newRequestQueue(PropertyInfo.this);

        StringRequest renterReq = new StringRequest(Request.Method.GET, renterUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject propertyInfo = array.getJSONObject(i);
                        String rpm_id = propertyInfo.getString("rpm_id")
                                .equals("null") ? "" : propertyInfo.getString("rpm_id");

                        String rpm_prop_name = propertyInfo.getString("rpm_prop_name")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_name");

                        String rpm_prop_address = propertyInfo.getString("rpm_prop_address")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_address");
                        String rpm_prop_owner = propertyInfo.getString("rpm_prop_owner")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_owner");
                        String rpm_prop_owner_contact = propertyInfo.getString("rpm_prop_owner_contact")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_owner_contact");
                        String rpm_prop_lat = propertyInfo.getString("rpm_prop_lat")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_lat");
                        String rpm_prop_long = propertyInfo.getString("rpm_prop_long")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_long");
                        String rpm_prop_active_flag = propertyInfo.getString("rpm_prop_active_flag")
                                .equals("null") ? "" : propertyInfo.getString("rpm_prop_active_flag");
                        String rpm_notes = propertyInfo.getString("rpm_notes")
                                .equals("null") ? "" : propertyInfo.getString("rpm_notes");

                        propertyLists.add(new PropertyList(rpm_id,rpm_prop_name,rpm_prop_address,
                                rpm_prop_owner,rpm_prop_owner_contact,rpm_prop_lat,rpm_prop_long,
                                rpm_prop_active_flag,rpm_notes,new ArrayList<>(),false));

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
        if (propertyLists.size() != 0) {
            for (int i = 0; i < propertyLists.size(); i++) {
                finalVal = propertyLists.get(i).isUpdatedListValue();
                boolean val = propertyLists.get(i).isUpdatedListValue();
                if (!val) {
                    String id = propertyLists.get(i).getRpm_id();
                    getPropertyDetails(id);
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

    public void getPropertyDetails(String id) {
        String url = "http://119.18.148.32:8080/super/superrent_app/masterdata/propertydtl/"+id+"";
        RequestQueue requestQueue = Volley.newRequestQueue(PropertyInfo.this);
        ArrayList<PropertyDetailsList> propertyDetailsLists = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detailsInfo = array.getJSONObject(i);
                        String rpd_id = detailsInfo.getString("rpd_id");
                        String rpd_rpm_id = detailsInfo.getString("rpd_rpm_id");

                        String rpd_floor_no = detailsInfo.getString("rpd_floor_no")
                                .equals("null") ? "" : detailsInfo.getString("rpd_floor_no");
                        String rpd_block_no = detailsInfo.getString("rpd_block_no")
                                .equals("null") ? "" : detailsInfo.getString("rpd_block_no");
                        String rpd_shop_no = detailsInfo.getString("rpd_shop_no")
                                .equals("null") ? "" : detailsInfo.getString("rpd_shop_no");
                        String rpd_measurement = detailsInfo.getString("rpd_measurement")
                                .equals("null") ? "" : detailsInfo.getString("rpd_measurement");
                        String rpd_current_renter_id = detailsInfo.getString("rpd_current_renter_id")
                                .equals("null") ? "" : detailsInfo.getString("rpd_current_renter_id");
                        String rri_name = detailsInfo.getString("rri_name")
                                .equals("null") ? "" : detailsInfo.getString("rri_name");
                        String rpd_current_renter_start_date = detailsInfo.getString("rpd_current_renter_start_date")
                                .equals("null") ? "" : detailsInfo.getString("rpd_current_renter_start_date");
                        String rpd_renter_rent_renew_type = detailsInfo.getString("rpd_renter_rent_renew_type")
                                .equals("null") ? "" :  detailsInfo.getString("rpd_renter_rent_renew_type");
                        String rpd_renter_rent_renew_after = detailsInfo.getString("rpd_renter_rent_renew_after")
                                .equals("null") ? "" : detailsInfo.getString("rpd_renter_rent_renew_after");
                        String rpd_renter_type = detailsInfo.getString("rpd_renter_type")
                                .equals("null") ? "" : detailsInfo.getString("rpd_renter_type");

                        propertyDetailsLists.add(new PropertyDetailsList(rpd_id,rpd_rpm_id,rpd_floor_no,rpd_block_no,
                                rpd_shop_no,rpd_measurement,rpd_current_renter_id,rri_name,rpd_current_renter_start_date,
                                rpd_renter_rent_renew_type,rpd_renter_rent_renew_after,rpd_renter_type));
                    }
                }
                for (int i = 0; i < propertyLists.size(); i++) {
                    if (propertyLists.get(i).getRpm_id().equals(id)) {
                        propertyLists.get(i).setUpdatedListValue(true);
                        propertyLists.get(i).setPropertyDetailsLists(propertyDetailsLists);
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

        requestQueue.add(stringRequest);
    }

    public void updateLayout() {
        p_info_circularProgressIndicator.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        PrNewButtonHandler = 0;
        if (conn) {
            if (connected) {
                propertyListAdapter = new PropertyListAdapter(propertyLists, PropertyInfo.this);
                ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(propertyListAdapter);
                animationAdapter.setDuration(500);
                animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
                animationAdapter.setFirstOnly(false);
                recyclerView.setAdapter(animationAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PropertyInfo.this)
                        .setMessage("There is a network issue in the server. Please try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getProperty();
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
            AlertDialog dialog = new AlertDialog.Builder(PropertyInfo.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Exit", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getProperty();
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