package com.shuvo.rft.superrent.house_bill.house_bill_details;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.property.ProperrtyModify;
import com.shuvo.rft.superrent.property.property_details.rentAmnt.RentAmount;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class HouseBillCollection extends AppCompatActivity {
    ImageView backLogo;
    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    TextView houseName;
    TextView billMonth;
    TextView flatWithRenter;
    TextView billDate;
    TextView monthlyBillAmnt;
    TextView prvDueAmnt;
    TextView monthlyExtraBill;
    TextView totalBill, billCollected, billToCollect;
    TextInputLayout collectedAmntLay;
    TextInputEditText collectedAmnt;
    TextInputLayout collectDateLay;
    TextInputEditText collectDate;
    TextInputLayout notesLay;
    TextInputEditText notes;
    Button save, clearData;
    private Boolean conn = false;
    private Boolean connected = false;
    private int mYear, mMonth, mDay;
    String house_name = "";
    String month_bill_name = "";
    String flat_renter = "";
    String bill_date = "";
    String monthly_bill_amnt = "";
    String prv_due_amnt = "";
    String monthly_extra_bill = "";
    String total_bill = "";
    String collected_amnt = "";
    String collected_date = "";
    String notes_by_user = "";
    String bill_to_collect = "";

    String rhmbd_id = "";
    String rhmbd_rhmbm_id = "";
    String rhmbd_rri_id = "";
    String rsmbd_rpd_id = "";
    String rhmbd_ryms_id = "";
    String last_updated_by = "";
    String last_updated_date = "";
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_bill_collection);
        backLogo = findViewById(R.id.back_logo_of_hb_collection);
        fullLayout = findViewById(R.id.hb_collection_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_hb_collection);
        circularProgressIndicator.setVisibility(View.GONE);

        houseName = findViewById(R.id.property_name_hb_collection);
        billMonth = findViewById(R.id.month_name_bill_hb_collection);
        flatWithRenter = findViewById(R.id.flat_with_renter_hb_collection);
        billDate = findViewById(R.id.bill_date_hb_collection);
        monthlyBillAmnt = findViewById(R.id.monthly_bill_amnt_hb_collection);
        prvDueAmnt = findViewById(R.id.previous_due_amnt_hb_collection);
        monthlyExtraBill = findViewById(R.id.monthly_extra_charge_hb_collection);
        totalBill = findViewById(R.id.total_bill_hb_collection);
        billCollected = findViewById(R.id.collected_bill_hb_collection);
        billToCollect = findViewById(R.id.bill_amount_to_collect_hb_collection);

        collectedAmntLay = findViewById(R.id.collected_amount_hb_collection_layout);
        collectedAmnt = findViewById(R.id.collected_amount_hb_collection);

        collectDateLay = findViewById(R.id.collection_date_layout_for_hb_collection);
        collectDate = findViewById(R.id.collection_date_for_hb_collection);

        notesLay = findViewById(R.id.notes_hb_collection_layout);
        notes = findViewById(R.id.notes_hb_collection);

        save = findViewById(R.id.save_collected_amnt_button);
        clearData = findViewById(R.id.clear_collected_amnt_button);

        user_code = userInfoLists.get(0).getUser_name();

        Intent intent = getIntent();
        house_name = intent.getStringExtra("house_name");
        month_bill_name = intent.getStringExtra("month_name_bill");
        rhmbd_id = intent.getStringExtra("rhmbd_id");
        rhmbd_rhmbm_id = intent.getStringExtra("rhmbd_rhmbm_id");
        rhmbd_rri_id = intent.getStringExtra("rhmbd_rri_id");
        rsmbd_rpd_id = intent.getStringExtra("rsmbd_rpd_id");
        flat_renter = intent.getStringExtra("renter_info");
        rhmbd_ryms_id = intent.getStringExtra("rhmbd_ryms_id");
        monthly_bill_amnt = intent.getStringExtra("rhmbd_monthly_bill_amt");
        prv_due_amnt = intent.getStringExtra("rhmbd_due_amt");
        monthly_extra_bill = intent.getStringExtra("rhmbd_monthly_extra_charge");
        total_bill = intent.getStringExtra("rhmbd_total_bill_amt");
        collected_amnt = intent.getStringExtra("rhmbd_collected_amt");
        bill_date = intent.getStringExtra("rhmbd_bill_date");
        collected_date = intent.getStringExtra("rhmbd_bill_collect_date");
        notes_by_user = intent.getStringExtra("rhmbd_notes");

        if (collected_date.isEmpty()) {
            Date t_date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            collected_date = df.format(t_date);
        }
        houseName.setText(house_name);
        billMonth.setText(month_bill_name);
        collectDate.setText(collected_date);
        flatWithRenter.setText(flat_renter);
        billDate.setText(bill_date);
        monthlyBillAmnt.setText(monthly_bill_amnt);
        prvDueAmnt.setText(prv_due_amnt);
        monthlyExtraBill.setText(monthly_extra_bill);
        totalBill.setText(total_bill);
        notes.setText(notes_by_user);
        billCollected.setText(collected_amnt.isEmpty() ? "0" : collected_amnt);

        int tot;
        int colAmnt;
        int billCollect;
        try {
            colAmnt = Integer.parseInt(collected_amnt);
        }
        catch (Exception e) {
            colAmnt = 0;
        }
        try {
            tot = Integer.parseInt(total_bill);
        }
        catch (Exception e) {
            tot = 0;
        }
        billCollect = tot-colAmnt;
        bill_to_collect = String.valueOf(billCollect);
        collectedAmnt.setText(total_bill);
        billToCollect.setText(bill_to_collect);

        Date t_date1 = Calendar.getInstance().getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        last_updated_date = df1.format(t_date1);
        last_updated_by = userInfoLists.get(0).getUser_name();

        backLogo.setOnClickListener(view -> finish());

        collectedAmnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                collectedAmntLay.setHelperText("");
            }
        });

        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                notesLay.setHelperText("");
            }
        });

        collectDate.setOnClickListener(v -> {
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(HouseBillCollection.this, (view, year, month, dayOfMonth) -> {

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
                        collectDate.setText(date_text);
                        collectDateLay.setHelperText("");
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collected_amnt = Objects.requireNonNull(collectedAmnt.getText()).toString();
                collected_date = Objects.requireNonNull(collectDate.getText()).toString();
                notes_by_user = Objects.requireNonNull(notes.getText()).toString();
                if (!collected_amnt.isEmpty()) {
                    collectedAmntLay.setHelperText("");
                    int col_amnt = Integer.parseInt(collected_amnt);
                    int tot_bll = Integer.parseInt(total_bill);
                    if (col_amnt <= tot_bll) {
                        if (!collected_date.isEmpty()) {
                            collectDateLay.setHelperText("");
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HouseBillCollection.this);
                            builder.setTitle("Bill Collection!")
                                    .setMessage("Do you want to save this collection information?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            saveAmount();
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
                            collectDateLay.setHelperText("Please provide Collection Date");
                        }
                    }
                    else {
                        collectedAmntLay.setHelperText("Collected Amount will not be greater than Total Bill Amount");
                        Toast.makeText(getApplicationContext(), "Please provide accurate amount",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    collectedAmntLay.setHelperText("Please provide Collected Amount");
                }
            }
        });
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HouseBillCollection.this);
                builder.setTitle("Clear Bill Data!")
                        .setMessage("Do you want to clear all data for this bill?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearAmount();
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
        });
    }

    public void saveAmount() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;

        String saveCollectionUrl = "http://119.18.148.32:8080/super/superrent_app/bill/billdtlCRUD/";
        RequestQueue requestQueue = Volley.newRequestQueue(HouseBillCollection.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, saveCollectionUrl, response -> {
            conn = true;
            System.out.println(response);
            if (response.equals("200\n")) {
                connected = true;
                updateLay();
            }
            else if (response.equals("100\n")) {
                connected = false;
                updateLay();
            }
            else {
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
                params.put("P_RHMBD_ID",rhmbd_id);
                params.put("P_RHMBD_RHMBM_ID",rhmbd_rhmbm_id);
                params.put("P_RHMBD_RRI_ID",rhmbd_rri_id);
                params.put("P_RHMBD_RPD_ID",rsmbd_rpd_id);
                params.put("P_RHMBD_RYMS_ID",rhmbd_ryms_id);

                params.put("P_RHMBD_MONTHLY_BILL_AMT",monthly_bill_amnt);
                params.put("P_RHMBD_DUE_AMT",prv_due_amnt);
                params.put("P_RHMBD_MONTHLY_EXTRA_CHARGE",monthly_extra_bill);
                params.put("P_RHMBD_TOTAL_BILL_AMT",total_bill);
                params.put("P_RHMBD_BILL_DATE",bill_date);
                params.put("P_RHMBD_BILL_EXPIRE_DATE","");
                params.put("P_RHMBD_BILL_PREPARED_BY",last_updated_by);
                params.put("P_RHMBD_BILL_PREPARED_DATE",bill_date);

                params.put("P_RHMBD_COLLECTED_AMT",collected_amnt);
                params.put("P_RHMBD_BILL_COLLECT_DATE",collected_date);
                params.put("P_RHMBD_NOTES",notes_by_user);
                params.put("P_RHMBD_BILL_LAST_UPDATED_DATE",last_updated_date);
                params.put("P_RHMBD_BILL_LAST_UPDATED_BY",last_updated_by);
                params.put("P_OPERATION_TYPE","2");
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
                AlertDialog dialog = new AlertDialog.Builder(HouseBillCollection.this)
                        .setMessage("Bill Collection Updated")
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
                AlertDialog dialog = new AlertDialog.Builder(HouseBillCollection.this)
                        .setMessage("Failed to Update Bill Collection. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    saveAmount();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(HouseBillCollection.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                saveAmount();
                dialog.dismiss();
            });
        }
    }

    public void clearAmount() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;
        System.out.println("rhmbd_id: "+rhmbd_id);
        String clearCollectionUrl = "http://119.18.148.32:8080/super/superrent_app/bill/billdtlCRUD/";
        RequestQueue requestQueue = Volley.newRequestQueue(HouseBillCollection.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, clearCollectionUrl, response -> {
            conn = true;
            System.out.println(response);
            if (response.equals("200\n")) {
                connected = true;
                updateAfterClear();
            }
            else if (response.equals("100\n")) {
                connected = false;
                updateAfterClear();
            }
            else {
                connected = false;
                updateAfterClear();
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
            updateAfterClear();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("P_RHMBD_ID",rhmbd_id);

                params.put("P_OPERATION_TYPE","3");
                params.put("P_USER_NAME",user_code);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateAfterClear() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(HouseBillCollection.this)
                        .setMessage("Bill Data Cleared")
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
                AlertDialog dialog = new AlertDialog.Builder(HouseBillCollection.this)
                        .setMessage("Failed to Clear Bill Collection. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    clearAmount();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(HouseBillCollection.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                clearAmount();
                dialog.dismiss();
            });
        }
    }
}