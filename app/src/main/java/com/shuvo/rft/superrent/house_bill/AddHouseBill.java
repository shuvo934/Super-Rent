package com.shuvo.rft.superrent.house_bill;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.house_bill.house_bill_details.HouseBillCollection;
import com.shuvo.rft.superrent.property.ProperrtyModify;
import com.shuvo.rft.superrent.property.PropertyInfo;
import com.shuvo.rft.superrent.property.PropertyList;
import com.shuvo.rft.superrent.property.PropertyListAdapter;
import com.shuvo.rft.superrent.renter.ChoiceDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class AddHouseBill extends AppCompatActivity {

    TextView actionBarText;
    ImageView backLogo;
    AmazingSpinner propertyName;
    TextView propertyNameLay;
    AmazingSpinner monthYear;
    TextView monthYearLay;
    Button create;
    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    ArrayList<ChoiceDataList> propertyLists;
    ArrayList<ChoiceDataList> monthLists;

    String p_name = "";
    String month_name = "";

    String rpm_id = "";
    String ryms_id = "";
    private Boolean conn = false;
    private Boolean connected = false;
    int total_count = 0;
    String updated_by = "";
    String updated_date = "";
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house_bill);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));
        actionBarText = findViewById(R.id.new_month_bill_action_bar_text);
        backLogo = findViewById(R.id.back_logo_of_new_month_bill);

        fullLayout = findViewById(R.id.new_month_bill_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_new_month_bill);
        circularProgressIndicator.setVisibility(View.GONE);

        propertyName = findViewById(R.id.property_new_month_bill);
        propertyNameLay = findViewById(R.id.property_new_month_bill_layout);

        monthYear = findViewById(R.id.month_new_month_bill);
        monthYearLay = findViewById(R.id.month_new_month_bill_layout);

        create = findViewById(R.id.create_new_month_bill_button);

        user_code = userInfoLists.get(0).getUser_name();

        propertyLists = new ArrayList<>();
        monthLists = new ArrayList<>();

        Date t_date1 = Calendar.getInstance().getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        updated_date = df1.format(t_date1);
        updated_by = userInfoLists.get(0).getUser_name();

        propertyName.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < propertyLists.size(); j++) {
                if (name.equals(propertyLists.get(j).getName())) {
                    rpm_id = propertyLists.get(j).getId();
                }
            }
            System.out.println(rpm_id);
            propertyNameLay.setVisibility(View.GONE);
        });

        monthYear.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < monthLists.size(); j++) {
                if (name.equals(monthLists.get(j).getName())) {
                    ryms_id = monthLists.get(j).getId();
                }
            }
            System.out.println(ryms_id);
            monthYearLay.setVisibility(View.GONE);
        });

        backLogo.setOnClickListener(view -> finish());

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rpm_id.isEmpty()) {
                    if (!ryms_id.isEmpty()) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddHouseBill.this);
                        builder.setTitle("Add New House Bill!")
                                .setMessage("Do you want to add new House Bill?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkHouseBill();
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        monthYearLay.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    propertyNameLay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        conn = false;
        connected = false;
        propertyLists = new ArrayList<>();
        monthLists = new ArrayList<>();

        String propertyUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/property/"+user_code;
        String monthUrl = "http://119.18.148.32:8080/super/superrent_app/bill/billmstmonth/"+user_code;
        RequestQueue requestQueue = Volley.newRequestQueue(AddHouseBill.this);

        StringRequest monthReq = new StringRequest(Request.Method.GET, monthUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String year_new = info.getString("year")
                                .equals("null") ? "" : info.getString("year");
                        String ryms_id_new = info.getString("ryms_id")
                                .equals("null") ? "" : info.getString("ryms_id");

                        monthLists.add(new ChoiceDataList(ryms_id_new, year_new));
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

        StringRequest propReq = new StringRequest(Request.Method.GET, propertyUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject propertyInfo = array.getJSONObject(i);
                        String rpm_id_new = propertyInfo.getString("rpm_id")
                                .equals("null") ? "" : propertyInfo.getString("rpm_id");

                        String rpm_prop_name = propertyInfo.getString("rpm_prop_name")
                                .equals("null") ? "Not Found" : propertyInfo.getString("rpm_prop_name");

                        propertyLists.add(new ChoiceDataList(rpm_id_new,rpm_prop_name));

                    }
                }
                requestQueue.add(monthReq);
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

        requestQueue.add(propReq);
    }

    private void updateLayout() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        if (conn) {
            if (connected) {
                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < propertyLists.size(); i++) {
                    type1.add(propertyLists.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                propertyName.setAdapter(arrayAdapter1);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < monthLists.size(); i++) {
                    type.add(monthLists.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                monthYear.setAdapter(arrayAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AddHouseBill.this)
                        .setMessage("There is a network issue in the server. Please try later")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getData();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AddHouseBill.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getData();
                dialog.dismiss();
            });
        }
    }

    public void checkHouseBill() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        conn = false;
        connected = false;
        total_count = 0;
        String url = "http://119.18.148.32:8080/super/superrent_app/bill/billmstvalidation/"+rpm_id+"/"+ryms_id+"/"+user_code;
        String addMasterBillUrl = "http://119.18.148.32:8080/super/superrent_app/bill/billmstCRUD/";
//        String addBillDtlUrl = "http://103.56.208.123:8001/apex/superrent_app/bill/billdtlCRUD/";
        RequestQueue requestQueue = Volley.newRequestQueue(AddHouseBill.this);

//        StringRequest addBillDtlReq = new StringRequest(Request.Method.POST, addBillDtlUrl, response -> {
//            conn = true;
//            System.out.println(response);
//            if (response.equals("200\n")) {
//                connected = true;
//                updateInterface();
//            }
//            else if (response.equals("100\n")) {
//                connected = false;
//                updateInterface();
//            }
//            else {
//                connected = false;
//                updateInterface();
//            }
//        }, error -> {
//            conn = false;
//            connected = false;
//            error.printStackTrace();
//            updateInterface();
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("P_RHMBD_ID",rhmbd_id);
//                params.put("P_RHMBD_RHMBM_ID",rhmbd_rhmbm_id);
//                params.put("P_RHMBD_RRI_ID",rhmbd_rri_id);
//                params.put("P_RHMBM_RPM_ID",rpm_id);
//                params.put("P_RHMBD_RYMS_ID",ryms_id);
//
//                params.put("P_RHMBD_MONTHLY_BILL_AMT",monthly_bill_amnt);
//                params.put("P_RHMBD_DUE_AMT",prv_due_amnt);
//                params.put("P_RHMBD_MONTHLY_EXTRA_CHARGE",monthly_extra_bill);
//                params.put("P_RHMBD_TOTAL_BILL_AMT",total_bill);
//                params.put("P_RHMBD_BILL_DATE",bill_date);
//                params.put("P_RHMBD_BILL_EXPIRE_DATE","");
//                params.put("P_RHMBD_BILL_PREPARED_BY",last_updated_by);
//                params.put("P_RHMBD_BILL_PREPARED_DATE",bill_date);
//
//                params.put("P_RHMBD_COLLECTED_AMT",collected_amnt);
//                params.put("P_RHMBD_BILL_COLLECT_DATE",collected_date);
//                params.put("P_RHMBD_NOTES",notes_by_user);
//                params.put("P_RHMBD_BILL_LAST_UPDATED_DATE",last_updated_date);
//                params.put("P_RHMBD_BILL_LAST_UPDATED_BY",last_updated_by);
//                params.put("P_OPERATION_TYPE","2");
//                return params;
//            }
//        };

        StringRequest addMasterBillReq = new StringRequest(Request.Method.POST, addMasterBillUrl, response -> {
            conn = true;
            System.out.println(response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("R_RESPONSE");
                System.out.println(string_out);
                if (string_out.equals("200")) {
                    connected = true;
                    updateInterface();
                }
                else if (string_out.equals("100")) {
                    connected = false;
                    updateInterface();
                }
                else {
                    connected = false;
                    updateInterface();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("P_RHMBM_RPM_ID",rpm_id);
                params.put("P_RHMBM_RYMS_ID",ryms_id);
                params.put("P_RHMBM_ACTIVE_STATUS","1");
                params.put("P_RHMBM_PREPARED_BY",updated_by);
                params.put("P_RHMBM_PREPARED_DATE",updated_date);
                params.put("P_RHMBM_LAST_UPDATED_BY",updated_by);
                params.put("P_RHMBM_LAST_UPDATED_DATE",updated_date);
                params.put("P_OPERATION_TYPE","1");
                params.put("P_USER_NAME",user_code);
                return params;
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        total_count = info.getInt("total_count");
                    }
                }
                if (total_count > 0) {
                    connected = true;
                    updateInterface();
                }
                else {
                    requestQueue.add(addMasterBillReq);
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        requestQueue.add(stringRequest);
    }

    private void updateInterface() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                if (total_count > 0) {
                    AlertDialog dialog = new AlertDialog.Builder(AddHouseBill.this)
                            .setMessage("Sorry selected month bill already process for this property.")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {
                        dialog.dismiss();
                    });
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(AddHouseBill.this)
                            .setMessage("New House Bill Added.")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {
                        finish();
                        dialog.dismiss();
                    });
                }

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AddHouseBill.this)
                        .setMessage("Failed to add New House Bill. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    checkHouseBill();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AddHouseBill.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                checkHouseBill();
                dialog.dismiss();
            });
        }
    }
}