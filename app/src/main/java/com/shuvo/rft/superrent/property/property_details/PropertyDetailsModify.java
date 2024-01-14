package com.shuvo.rft.superrent.property.property_details;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.property.property_details.rentAmnt.RentAmount;
import com.shuvo.rft.superrent.renter.ChoiceDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PropertyDetailsModify extends AppCompatActivity {

    TextView actionBarText;
    ImageView backLogo;

    TextView propertyNameText;

    TextInputEditText floorNo;
    TextInputLayout floorNoLay;

    TextInputEditText blockNo;
    TextInputLayout blockNoLay;

    TextInputEditText flatNo;
    TextInputLayout flatNoLay;

    TextInputEditText shopMeasurementNo;

    AmazingSpinner currentRenter;

    TextInputEditText renterStartDate;

    AmazingSpinner renterRentRenewType;

    TextInputEditText renterRentRenewAfter;

    AmazingSpinner renterType;

    MaterialButton editRenterAmount;

    Button add;
    Button update;
    Button delete;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    LinearLayout updateButtonLay;

    ArrayList<ChoiceDataList> renterRentRenewTypeLists;
    ArrayList<ChoiceDataList> renterLists;
    ArrayList<ChoiceDataList> renterTypeLists;
    private Boolean conn = false;
    private Boolean connected = false;

    String rpd_id = "";
    String rpm_id = "";
    String p_floor_no = "";
    String p_block_no = "";
    String p_flat_no = "";
    String p_sh_measure = "";

    String p_curren_renter_id = "";
    String p_renter_name = "";

    String p_renter_start_date = "";

    String p_renter_rent_renewType_id = "";
    String p_renter_rent_renew_type_name = "";

    String p_renter_rent_renew_after = "";

    String p_renter_type_id = "";
    String p_renter_type_name = "";
    private int mYear, mMonth, mDay;
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details_modify);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        actionBarText = findViewById(R.id.modify_property_details_action_bar_text);
        backLogo = findViewById(R.id.back_logo_of_modify_property_details);

        propertyNameText = findViewById(R.id.property_name_in_property_details_modify);

        floorNo = findViewById(R.id.floor_no_for_update_add);
        floorNoLay = findViewById(R.id.floor_no_layout_for_update_add);
        blockNo = findViewById(R.id.block_no_for_update_add);
        blockNoLay = findViewById(R.id.block_no_layout_for_update_add);
        flatNo = findViewById(R.id.flat_no_for_update_add);
        flatNoLay =  findViewById(R.id.flat_no_layout_for_update_add);
        shopMeasurementNo = findViewById(R.id.shop_measurement_for_update_add);
        currentRenter = findViewById(R.id.current_renter_name_spinner);
        renterStartDate = findViewById(R.id.renter_start_date_for_update_add);
        renterRentRenewType = findViewById(R.id.renter_rent_renew_type_spinner);
        renterRentRenewAfter = findViewById(R.id.renter_rent_renew_after_for_update_add);
        renterType = findViewById(R.id.renter_type_spinner);
        editRenterAmount = findViewById(R.id.edit_renter_amount);

        add = findViewById(R.id.add_property_details_button);
        update = findViewById(R.id.update_property_details_button);
        delete = findViewById(R.id.delete_property_details_button);
        updateButtonLay = findViewById(R.id.update_delete_property_details_layout);

        fullLayout = findViewById(R.id.add_edit_property_details_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_add_edit_property_details);
        circularProgressIndicator.setVisibility(View.GONE);

        user_code = userInfoLists.get(0).getUser_name();

        renterRentRenewTypeLists = new ArrayList<>();
        renterRentRenewTypeLists.add(new ChoiceDataList("1","Yearly"));
        renterRentRenewTypeLists.add(new ChoiceDataList("2","Monthly"));

        renterTypeLists = new ArrayList<>();
        renterTypeLists.add(new ChoiceDataList("3","House Renter"));

        Intent intent = getIntent();
        boolean editFlag = intent.getBooleanExtra("RPD_EDIT_FLAG",false);
        rpm_id = intent.getStringExtra("RPM_ID");
        String houseName = intent.getStringExtra("RPM_PROP_NAME");
        propertyNameText.setText(houseName);
        if (editFlag) {
            rpd_id = intent.getStringExtra("RPD_ID");
            p_floor_no = intent.getStringExtra("RPD_FLOOR_NO");
            p_block_no = intent.getStringExtra("RPD_BLOCK_NO");
            p_flat_no = intent.getStringExtra("RPD_FLAT_NO");
            p_sh_measure = intent.getStringExtra("RPD_MEASURE");
            p_curren_renter_id = intent.getStringExtra("RPD_RENTER_ID");
            p_renter_name = intent.getStringExtra("RPD_RENTER_NAME");
            p_renter_start_date = intent.getStringExtra("RPD_START_DATE");
            p_renter_rent_renew_type_name = intent.getStringExtra("rpd_renter_rent_renew_type");
            p_renter_rent_renew_after = intent.getStringExtra("rpd_renter_rent_renew_after");
            p_renter_type_name = intent.getStringExtra("rpd_renter_type");

            floorNo.setText(p_floor_no);
            blockNo.setText(p_block_no);
            flatNo.setText(p_flat_no);
            shopMeasurementNo.setText(p_sh_measure);
            currentRenter.setText(p_renter_name);
            renterStartDate.setText(p_renter_start_date);
            renterRentRenewType.setText(p_renter_rent_renew_type_name);
            renterRentRenewAfter.setText(p_renter_rent_renew_after);
            renterType.setText(p_renter_type_name);

            for (int i =0; i < renterRentRenewTypeLists.size(); i++) {
                if (p_renter_rent_renew_type_name.equals(renterRentRenewTypeLists.get(i).getName())) {
                    p_renter_rent_renewType_id = renterRentRenewTypeLists.get(i).getId();
                }
            }

            for (int i =0; i < renterTypeLists.size(); i++) {
                if (p_renter_type_name.equals(renterTypeLists.get(i).getName())) {
                    p_renter_type_id = renterTypeLists.get(i).getId();
                }
            }
            actionBarText.setText("UPDATE FLAT DETAILS");
            updateButtonLay.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
            editRenterAmount.setEnabled(true);
            editRenterAmount.setBackgroundTintList(ContextCompat.getColorStateList(PropertyDetailsModify.this,R.color.dark_yellow));

        }
        else {
            actionBarText.setText("NEW FLAT DETAILS");
            updateButtonLay.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
            editRenterAmount.setEnabled(false);
            editRenterAmount.setBackgroundTintList(ContextCompat.getColorStateList(PropertyDetailsModify.this,R.color.disabled));
        }

        ArrayList<String> type1 = new ArrayList<>();
        for(int i = 0; i < renterRentRenewTypeLists.size(); i++) {
            type1.add(renterRentRenewTypeLists.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

        renterRentRenewType.setAdapter(arrayAdapter1);

        ArrayList<String> type2 = new ArrayList<>();
        for(int i = 0; i < renterTypeLists.size(); i++) {
            type2.add(renterTypeLists.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

        renterType.setAdapter(arrayAdapter2);

        renterRentRenewType.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < renterRentRenewTypeLists.size(); j++) {
                if (name.equals(renterRentRenewTypeLists.get(j).getName())) {
                    p_renter_rent_renewType_id = (renterRentRenewTypeLists.get(j).getId());
                }
            }
            System.out.println(p_renter_rent_renewType_id);
        });

        currentRenter.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < renterLists.size(); j++) {
                if (name.equals(renterLists.get(j).getName())) {
                    p_curren_renter_id = (renterLists.get(j).getId());
                }
            }
            System.out.println(p_curren_renter_id);
        });

        renterType.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < renterTypeLists.size(); j++) {
                if (name.equals(renterTypeLists.get(j).getName())) {
                    p_renter_type_id = (renterTypeLists.get(j).getId());
                }
            }
            System.out.println(p_renter_type_id);
        });

        backLogo.setOnClickListener(view -> finish());

        floorNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                floorNoLay.setHelperText("");
            }
        });

        blockNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                blockNoLay.setHelperText("");
            }
        });

        flatNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                flatNoLay.setHelperText("");
            }
        });

        renterStartDate.setOnClickListener(v -> {
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(PropertyDetailsModify.this, new DatePickerDialog.OnDateSetListener() {
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
                            renterStartDate.setText(date_text);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        editRenterAmount.setOnClickListener(view -> {
            Intent intent1 = new Intent(PropertyDetailsModify.this, RentAmount.class);
            intent1.putExtra("RPD_ID",rpd_id);
            startActivity(intent1);
        });

        add.setOnClickListener(view -> {
            p_floor_no = Objects.requireNonNull(floorNo.getText()).toString();
            p_block_no = Objects.requireNonNull(blockNo.getText()).toString();
            p_flat_no = Objects.requireNonNull(flatNo.getText()).toString();
            p_sh_measure = Objects.requireNonNull(shopMeasurementNo.getText()).toString();
            p_renter_start_date = Objects.requireNonNull(renterStartDate.getText()).toString();
            p_renter_rent_renew_after = Objects.requireNonNull(renterRentRenewAfter.getText()).toString();

            if (!p_floor_no.isEmpty()) {
                floorNoLay.setHelperText("");
                if (!p_block_no.isEmpty()) {
                    blockNoLay.setHelperText("");
                    if (!p_flat_no.isEmpty()) {
                        flatNoLay.setHelperText("");
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PropertyDetailsModify.this);
                        builder.setTitle("Add New Flat!")
                                .setMessage("Do you want to add New Flat information?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addFlat();
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
                        flatNoLay.setHelperText("Please Provide Flat No");
                    }
                }
                else {
                    blockNoLay.setHelperText("Please Provide Block No");
                }
            }
            else {
                floorNoLay.setHelperText("Please Provide Floor No");
            }
        });

        update.setOnClickListener(view -> {
            p_floor_no = Objects.requireNonNull(floorNo.getText()).toString();
            p_block_no = Objects.requireNonNull(blockNo.getText()).toString();
            p_flat_no = Objects.requireNonNull(flatNo.getText()).toString();
            p_sh_measure = Objects.requireNonNull(shopMeasurementNo.getText()).toString();
            p_renter_start_date = Objects.requireNonNull(renterStartDate.getText()).toString();
            p_renter_rent_renew_after = Objects.requireNonNull(renterRentRenewAfter.getText()).toString();

            if (!p_floor_no.isEmpty()) {
                floorNoLay.setHelperText("");
                if (!p_block_no.isEmpty()) {
                    blockNoLay.setHelperText("");
                    if (!p_flat_no.isEmpty()) {
                        flatNoLay.setHelperText("");
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PropertyDetailsModify.this);
                        builder.setTitle("Update Flat Details!")
                                .setMessage("Do you want to update this Flat information?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updateFlat();
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
                        flatNoLay.setHelperText("Please Provide Flat No");
                    }
                }
                else {
                    blockNoLay.setHelperText("Please Provide Block No");
                }
            }
            else {
                floorNoLay.setHelperText("Please Provide Floor No");
            }
        });

        getData();
    }

    public void getData() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        conn = false;
        connected = false;
        renterLists = new ArrayList<>();

        String renterUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/renter/"+user_code;

        RequestQueue requestQueue = Volley.newRequestQueue(PropertyDetailsModify.this);

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

                        renterLists.add(new ChoiceDataList(rri_id,rri_name));
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
        fullLayout.setVisibility(View.VISIBLE);
        if (conn) {
            if (connected) {
                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < renterLists.size(); i++) {
                    type1.add(renterLists.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                currentRenter.setAdapter(arrayAdapter1);

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
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
            AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
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

    public void addFlat() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        conn = false;
        connected = false;

        String addFlatUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/updatepropertydtl/";

        RequestQueue requestQueue = Volley.newRequestQueue(PropertyDetailsModify.this);

        StringRequest addFlatReq = new StringRequest(Request.Method.POST, addFlatUrl, response -> {
            conn = true;
            System.out.println(response);
            if (response.equals("200\n")) {
                connected = true;
                addUpdateLay();
            }
            else if (response.equals("100\n")) {
                connected = false;
                addUpdateLay();
            }
            else {
                connected = false;
                addUpdateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            addUpdateLay();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("P_RPD_RPM_ID",rpm_id);
                params.put("P_RPD_FLOOR_NO",p_floor_no);
                params.put("P_RPD_BLOCK_NO",p_block_no);
                params.put("P_RPD_SHOP_NO",p_flat_no);
                params.put("P_RPD_MEASUREMENT",p_sh_measure);
                params.put("P_RPD_CURRENT_RENTER_ID",p_curren_renter_id);
                params.put("P_RPD_CURR_RENTER_STARTDATE",p_renter_start_date);
                params.put("P_RPD_RENTER_RENT_RENEW_TYPE",p_renter_rent_renewType_id);
                params.put("P_RPD_RENTER_RENT_RENEW_AFTER",p_renter_rent_renew_after);
                params.put("P_RPD_RENTER_TYPE",p_renter_type_id);
                params.put("P_OPERATION_TYPE","1");
                params.put("P_USER_NAME",user_code);
                return params;
            }
        };

        requestQueue.add(addFlatReq);
    }

    private void addUpdateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
                        .setMessage("New Flat Added")
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
                AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
                        .setMessage("Failed to add new Flat Details. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    addFlat();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                addFlat();
                dialog.dismiss();
            });
        }
    }

    public void updateFlat() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        conn = false;
        connected = false;

        String addFlatUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/updatepropertydtl/";

        RequestQueue requestQueue = Volley.newRequestQueue(PropertyDetailsModify.this);

        StringRequest addFlatReq = new StringRequest(Request.Method.POST, addFlatUrl, response -> {
            conn = true;
            System.out.println(response);
            if (response.equals("200\n")) {
                connected = true;
                upUpdateLay();
            }
            else if (response.equals("100\n")) {
                connected = false;
                upUpdateLay();
            }
            else {
                connected = false;
                upUpdateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            upUpdateLay();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("P_RPD_ID",rpd_id);
                params.put("P_RPD_RPM_ID",rpm_id);
                params.put("P_RPD_FLOOR_NO",p_floor_no);
                params.put("P_RPD_BLOCK_NO",p_block_no);
                params.put("P_RPD_SHOP_NO",p_flat_no);
                params.put("P_RPD_MEASUREMENT",p_sh_measure);
                params.put("P_RPD_CURRENT_RENTER_ID",p_curren_renter_id);
                params.put("P_RPD_CURR_RENTER_STARTDATE",p_renter_start_date);
                params.put("P_RPD_RENTER_RENT_RENEW_TYPE",p_renter_rent_renewType_id);
                params.put("P_RPD_RENTER_RENT_RENEW_AFTER",p_renter_rent_renew_after);
                params.put("P_RPD_RENTER_TYPE",p_renter_type_id);
                params.put("P_OPERATION_TYPE","2");
                params.put("P_USER_NAME",user_code);
                return params;
            }
        };

        requestQueue.add(addFlatReq);
    }

    private void upUpdateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
                        .setMessage("Flat Details Updated")
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
                AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
                        .setMessage("Failed to update Flat Details. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    updateFlat();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(PropertyDetailsModify.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                updateFlat();
                dialog.dismiss();
            });
        }
    }
}