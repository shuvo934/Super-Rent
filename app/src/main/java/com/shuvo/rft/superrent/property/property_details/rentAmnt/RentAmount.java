package com.shuvo.rft.superrent.property.property_details.rentAmnt;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.property.property_details.PropertyDetailsModify;
import com.shuvo.rft.superrent.renter.ChoiceDataList;
import com.shuvo.rft.superrent.renter.RenterModify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RentAmount extends AppCompatActivity {

    ImageView backLogo;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextInputLayout rentAmntLay;
    TextInputEditText rentAmnt;

    TextInputLayout appDateLay;
    TextInputEditText appDate;

    AmazingSpinner status;
    TextView statusLay;

    Button save;

    TextView currRentAmnt;
    TextView currAppDate;
    TextView currRentStatus;

    TextView noCurrDataMsg;
    TextView noPreDataMsg;
    LinearLayout currDataFound;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    PreviousRentAmntListAdapter previousRentAmntListAdapter;

    private Boolean conn = false;
    private Boolean connected = false;
    ArrayList<RentAmountList> rentAmountLists;

    String rpd_id = "";
    ArrayList<ChoiceDataList> flagsList;
    String flag_id = "";
    String p_rent_amount = "";
    String p_apply_date = "";

    private int mYear, mMonth, mDay;
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_amount);
        backLogo = findViewById(R.id.back_logo_of_add_rent_amount);

        fullLayout = findViewById(R.id.add_rent_amount_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_add_renter_amount);
        circularProgressIndicator.setVisibility(View.GONE);

        rentAmntLay = findViewById(R.id.rent_amount_layout_for_rent_add);
        rentAmnt = findViewById(R.id.rent_amount_for_rent_add);

        appDateLay = findViewById(R.id.rent_apply_date_layout_for_rent_add);
        appDate = findViewById(R.id.rent_apply_date_for_rent_add);

        status = findViewById(R.id.add_rent_active_flag);
        statusLay = findViewById(R.id.add_rent_active_flag_layout);

        save = findViewById(R.id.add_new_rent_button);

        currRentAmnt = findViewById(R.id.current_rent_amount_in_add_rent);
        currAppDate = findViewById(R.id.apply_date_in_add_rent);
        currRentStatus = findViewById(R.id.status_in_add_rent);

        noCurrDataMsg = findViewById(R.id.no_current_rent_amnt_data_msg);
        noPreDataMsg = findViewById(R.id.no_previois_rent_amnt_data_msg);
        currDataFound = findViewById(R.id.current_data_found_layout);
        currDataFound.setVisibility(View.GONE);

        noCurrDataMsg.setVisibility(View.GONE);
        noPreDataMsg.setVisibility(View.GONE);
        user_code = userInfoLists.get(0).getUser_name();

        recyclerView = findViewById(R.id.rent_amount_list_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        flagsList = new ArrayList<>();
        flagsList.add(new ChoiceDataList("0","IN-ACTIVE"));
        flagsList.add(new ChoiceDataList("1","ACTIVE"));

        Intent intent = getIntent();
        rpd_id = intent.getStringExtra("RPD_ID");
        System.out.println(rpd_id);

        ArrayList<String> type1 = new ArrayList<>();
        for(int i = 0; i < flagsList.size(); i++) {
            type1.add(flagsList.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

        status.setAdapter(arrayAdapter1);

        status.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < flagsList.size(); j++) {
                if (name.equals(flagsList.get(j).getName())) {
                    flag_id = (flagsList.get(j).getId());
                }
            }
            System.out.println(flag_id);
            statusLay.setVisibility(View.GONE);
        });

        backLogo.setOnClickListener(view -> finish());

        rentAmnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                rentAmntLay.setHelperText("");
            }
        });

        appDate.setOnClickListener(v -> {
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(RentAmount.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String monthName = "";
                            String dayOfMonthName = "";
                            String numFormatMonth = "";
                            String yearName = "";
                            month = month + 1;
//                                if (month == 1) {
//                                    monthName = "JAN";
//                                } else if (month == 2) {
//                                    monthName = "FEB";
//                                } else if (month == 3) {
//                                    monthName = "MAR";
//                                } else if (month == 4) {
//                                    monthName = "APR";
//                                } else if (month == 5) {
//                                    monthName = "MAY";
//                                } else if (month == 6) {
//                                    monthName = "JUN";
//                                } else if (month == 7) {
//                                    monthName = "JUL";
//                                } else if (month == 8) {
//                                    monthName = "AUG";
//                                } else if (month == 9) {
//                                    monthName = "SEP";
//                                } else if (month == 10) {
//                                    monthName = "OCT";
//                                } else if (month == 11) {
//                                    monthName = "NOV";
//                                } else if (month == 12) {
//                                    monthName = "DEC";
//                                }

                            if (dayOfMonth <= 9) {
                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                            } else {
                                dayOfMonthName = String.valueOf(dayOfMonth);
                            }
                            if (month <= 9) {
                                numFormatMonth = "0" + String.valueOf(month);
                            } else {
                                numFormatMonth = String.valueOf(month);
                            }
                            yearName  = String.valueOf(year);
//                                yearName = yearName.substring(yearName.length()-2);
                            System.out.println(yearName);
                            System.out.println(dayOfMonthName);
                            String date_text = dayOfMonthName + "-" + numFormatMonth + "-" + yearName;
                            appDate.setText(date_text);
                            appDateLay.setHelperText("");
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        save.setOnClickListener(view -> {
            p_rent_amount = Objects.requireNonNull(rentAmnt.getText()).toString();
            p_apply_date = Objects.requireNonNull(appDate.getText()).toString();

            if (!p_rent_amount.isEmpty()) {
                rentAmntLay.setHelperText("");
                if (!p_apply_date.isEmpty()) {
                    appDateLay.setHelperText("");
                    if (!flag_id.isEmpty()) {
                        statusLay.setVisibility(View.GONE);
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RentAmount.this);
                        builder.setTitle("Add New Rent Amount!")
                                .setMessage("Do you want to add New Rent Amount?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addRentAmount();
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
                        statusLay.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    appDateLay.setHelperText("Please provide Apply Date");
                }
            }
            else {
                rentAmntLay.setHelperText("Please provide Rent Amount");
            }
        });

        getRentAmnt();

    }

    public void getRentAmnt() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        conn = false;
        connected = false;
        rentAmountLists = new ArrayList<>();

        String url = "http://119.18.148.32:8080/super/superrent_app/masterdata/rentamountlist/"+rpd_id+"/"+user_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

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

                        String prh_id = info.getString("prh_id")
                                .equals("null") ? "" : info.getString("prh_id");
                        String prh_rpd_id = info.getString("prh_rpd_id")
                                .equals("null") ? "" : info.getString("prh_rpd_id");
                        String floor_info = info.getString("floor_info")
                                .equals("null") ? "" : info.getString("floor_info");
                        String prh_current_amt = info.getString("prh_current_amt")
                                .equals("null") ? "" : info.getString("prh_current_amt");
                        String prh_previous_amt = info.getString("prh_previous_amt")
                                .equals("null") ? "" : info.getString("prh_previous_amt");
                        String prh_ryms_id = info.getString("prh_ryms_id")
                                .equals("null") ? "" : info.getString("prh_ryms_id");
                        String prh_apply_date = info.getString("prh_apply_date")
                                .equals("null") ? "" : info.getString("prh_apply_date");
                        String status = info.getString("status")
                                .equals("null") ? "" : info.getString("status");

                        rentAmountLists.add(new RentAmountList(prh_id,prh_rpd_id,floor_info,prh_current_amt,prh_previous_amt,prh_ryms_id,
                                prh_apply_date,status));
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
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
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        if (conn) {
            if (connected) {
                if (rentAmountLists.size() == 0) {
                    noPreDataMsg.setVisibility(View.VISIBLE);
                    noCurrDataMsg.setVisibility(View.VISIBLE);
                    currDataFound.setVisibility(View.GONE);
                }
                else if (rentAmountLists.size() == 1) {
                    noPreDataMsg.setVisibility(View.VISIBLE);
                    noCurrDataMsg.setVisibility(View.GONE);
                    currDataFound.setVisibility(View.VISIBLE);

                    String curRA = rentAmountLists.get(0).getPrh_current_amt();
                    String cuAD = rentAmountLists.get(0).getPrh_apply_date();
                    String stat = rentAmountLists.get(0).getStatus();

                    currRentAmnt.setText(curRA);
                    currAppDate.setText(cuAD);
                    currRentStatus.setText(stat);
                }
                else {
                    noPreDataMsg.setVisibility(View.GONE);
                    noCurrDataMsg.setVisibility(View.GONE);
                    currDataFound.setVisibility(View.VISIBLE);

                    String curRA = rentAmountLists.get(rentAmountLists.size()-1).getPrh_current_amt();
                    String cuAD = rentAmountLists.get(rentAmountLists.size()-1).getPrh_apply_date();
                    String stat = rentAmountLists.get(rentAmountLists.size()-1).getStatus();

                    currRentAmnt.setText(curRA);
                    currAppDate.setText(cuAD);
                    currRentStatus.setText(stat);

                    rentAmountLists.remove(rentAmountLists.size()-1);

                    previousRentAmntListAdapter = new PreviousRentAmntListAdapter(rentAmountLists, RentAmount.this);
                    recyclerView.setAdapter(previousRentAmntListAdapter);
                }
                connected = false;
                conn = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(RentAmount.this)
                        .setMessage("There is a network issue in the server. Please try later")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getRentAmnt();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(RentAmount.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getRentAmnt();
                dialog.dismiss();
            });
        }
    }

    public void addRentAmount() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;

        String addRentUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/rentamountCRUD/";

        RequestQueue requestQueue = Volley.newRequestQueue(RentAmount.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addRentUrl, response -> {
            conn = true;
            System.out.println(response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("R_RESPONSE");
                System.out.println(string_out);
                if (string_out.equals("200")) {
                    connected = true;
                    updateLay();
                }
                else if (string_out.equals("100")) {
                    connected = false;
                    updateLay();
                }
                else {
                    connected = false;
                    updateLay();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
//            if (response.equals("200\n")) {
//                connected = true;
//                updateLay();
//            }
//            else if (response.equals("100\n")) {
//                connected = false;
//                updateLay();
//            }
//            else {
//                connected = false;
//                updateLay();
//            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("P_PRH_RPD_ID",rpd_id);
                params.put("P_PRH_CURRENT_AMT",p_rent_amount);
                params.put("P_PRH_APPLY_DATE",p_apply_date);
                params.put("P_PRH_ACTIVE_STAUS",flag_id);
                params.put("P_OPERATION_TYPE","1");
                params.put("P_USER_NAME",user_code);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(RentAmount.this)
                        .setMessage("New Rent Amount Added")
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
            else {
                AlertDialog dialog = new AlertDialog.Builder(RentAmount.this)
                        .setMessage("Failed to add new Rent Amount. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    addRentAmount();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(RentAmount.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                addRentAmount();
                dialog.dismiss();
            });
        }
    }
}