package com.shuvo.rft.superrent.renter;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class RenterInfo extends AppCompatActivity {

    RecyclerView recyclerView;
    RenterListAdapter renterListAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static RelativeLayout layout;
    public static int floatingButtonHandler = 0;
    public static CircularProgressIndicator circularProgressIndicator;

    private Boolean conn = false;
    private Boolean connected = false;
    ArrayList<RenterList> renterLists;

    ExtendedFloatingActionButton floatingActionButton;
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_info);

        layout = findViewById(R.id.renter_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_renter_info);
        circularProgressIndicator.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.renter_list_view);

        user_code = userInfoLists.get(0).getUser_name();

        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        floatingActionButton = findViewById(R.id.new_renter_add_button);

        floatingActionButton.setOnClickListener(view -> {
            if (floatingButtonHandler == 0) {
                Intent intent = new Intent(RenterInfo.this, RenterModify.class);
                startActivity(intent);
            }
            else if (floatingButtonHandler == 1) {
                Toast.makeText(getApplicationContext(), "Please wait while loading", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getRenter();
    }

    @Override
    public void onBackPressed() {
        if (floatingButtonHandler == 1) {
            Toast.makeText(getApplicationContext(), "Please wait while loading", Toast.LENGTH_SHORT).show();
        }
        else {
            finish();
        }
    }

    public void getRenter() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        floatingButtonHandler = 1;
        conn = false;
        connected = false;
        renterLists = new ArrayList<>();

        String renterUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/renter/"+user_code;

        RequestQueue requestQueue = Volley.newRequestQueue(RenterInfo.this);

        StringRequest renterReq = new StringRequest(Request.Method.GET, renterUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject renterInfo = array.getJSONObject(i);
                        String rri_id = renterInfo.getString("rri_id").equals("null") ? "" : renterInfo.getString("rri_id");
                        String rri_name = renterInfo.getString("rri_name").equals("null") ? "Not Found" : renterInfo.getString("rri_name");
                        String rri_national_id = renterInfo.getString("rri_national_id").equals("null") ? "Not Found" : renterInfo.getString("rri_national_id");
                        String rri_contact = renterInfo.getString("rri_contact").equals("null") ? "Not Found" : renterInfo.getString("rri_contact");
                        String rri_email = renterInfo.getString("rri_email").equals("null") ? "Not Found" : renterInfo.getString("rri_email");
                        String rri_business_company_name = renterInfo.getString("rri_business_company_name").equals("null") ? "Not Found" : renterInfo.getString("rri_business_company_name");
                        String rri_active_flag = renterInfo.getString("rri_active_flag").equals("null") ? "0" : renterInfo.getString("rri_active_flag");

                        renterLists.add(new RenterList(rri_id,rri_name,rri_national_id,rri_contact,rri_email,rri_business_company_name,rri_active_flag));
                    }
                }
                connected = true;
                updateLayout();
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

    public void updateLayout() {
        circularProgressIndicator.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        floatingButtonHandler = 0;
        if (conn) {
            if (connected) {
                renterListAdapter = new RenterListAdapter(renterLists, RenterInfo.this);
                ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(renterListAdapter);
                animationAdapter.setDuration(500);
                animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
                animationAdapter.setFirstOnly(false);
                recyclerView.setAdapter(animationAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(RenterInfo.this)
                        .setMessage("There is a network issue in the server. Please try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getRenter();
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
            AlertDialog dialog = new AlertDialog.Builder(RenterInfo.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Exit", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getRenter();
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