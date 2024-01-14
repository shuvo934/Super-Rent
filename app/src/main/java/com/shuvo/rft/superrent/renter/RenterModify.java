package com.shuvo.rft.superrent.renter;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.property.ProperrtyModify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RenterModify extends AppCompatActivity {

    TextView actionBarText;
    ImageView backLogo;

    TextInputEditText renterName;
    TextInputLayout renterNameLay;

    TextInputEditText renterId;
    TextInputLayout renterIdLay;

    TextInputEditText renterContact;
    TextInputLayout renterContactLay;

    TextInputEditText renterEmail;
    TextInputLayout renterEmailLay;

    TextInputEditText renterComName;
    TextInputLayout renterComLay;

    AmazingSpinner activeFlag;
    TextView activeFlagLay;

    Button add;
    Button edit;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    ArrayList<ChoiceDataList> flagsList;
    String name = "";
    String n_id = "";
    String r_contact = "";
    String r_email = "";
    String r_f_name = "";
    String flag_id = "";

    String rri_id = "";

    private Boolean conn = false;
    private Boolean connected = false;
    String user_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter_modify);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        actionBarText = findViewById(R.id.modify_renter_action_bar_text);
        backLogo = findViewById(R.id.back_logo_of_modify_renter);

        fullLayout = findViewById(R.id.add_edit_renter_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_add_edit_renter);
        circularProgressIndicator.setVisibility(View.GONE);

        renterName = findViewById(R.id.renter_name_for_update_add);
        renterNameLay = findViewById(R.id.renter_name_layout_for_update_add);

        renterId = findViewById(R.id.renter_national_id);
        renterIdLay = findViewById(R.id.renter_national_id_layout);

        renterContact = findViewById(R.id.renter_contact_no);
        renterContactLay = findViewById(R.id.renter_contact_no_layout);

        renterEmail = findViewById(R.id.renter_email_no);
        renterEmailLay = findViewById(R.id.renter_email_no_layout);

        renterComName = findViewById(R.id.renter_flat_company_name);
        renterComLay = findViewById(R.id.renter_flat_company_name_layout);

        activeFlag = findViewById(R.id.renter_active_flag);
        activeFlagLay = findViewById(R.id.renter_active_flag_layout);

        add = findViewById(R.id.add_renter_button);
        edit = findViewById(R.id.update_renter_button);

        user_code = userInfoLists.get(0).getUser_name();

        flagsList = new ArrayList<>();
        flagsList.add(new ChoiceDataList("0","IN-ACTIVE"));
        flagsList.add(new ChoiceDataList("1","ACTIVE"));

        Intent intent = getIntent();
        boolean editFlag = intent.getBooleanExtra("RENTER_EDIT_FLAG",false);
        if (editFlag) {
            actionBarText.setText("EDIT RENTER");
            add.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);

            name = intent.getStringExtra("RENTER_NAME");
            n_id = intent.getStringExtra("RENTER_N_ID");
            r_contact = intent.getStringExtra("RENTER_CONTACT");
            r_email = intent.getStringExtra("RENTER_EMAIL");
            r_f_name = intent.getStringExtra("RENTER_FLAT_NAME");
            flag_id = intent.getStringExtra("RENTER_ACTIVE_FLAG");
            rri_id = intent.getStringExtra("RENTER_ID");

            renterName.setText(name);
            renterId.setText(n_id);
            renterContact.setText(r_contact);
            renterEmail.setText(r_email);
            renterComName.setText(r_f_name);
            for (int i = 0; i < flagsList.size(); i++) {
                if (flag_id.equals(flagsList.get(i).getId())) {
                    activeFlag.setText(flagsList.get(i).getName());
                }
            }
        }
        else {
            actionBarText.setText("NEW RENTER");
            add.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        }

        ArrayList<String> type1 = new ArrayList<>();
        for(int i = 0; i < flagsList.size(); i++) {
            type1.add(flagsList.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

        activeFlag.setAdapter(arrayAdapter1);

        activeFlag.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < flagsList.size(); j++) {
                if (name.equals(flagsList.get(j).getName())) {
                    flag_id = (flagsList.get(j).getId());
                }
            }
            System.out.println(flag_id);
            activeFlagLay.setVisibility(View.GONE);
        });

        backLogo.setOnClickListener(view -> finish());

        renterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                renterNameLay.setHelperText("");
            }
        });

        renterId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                renterIdLay.setHelperText("");
            }
        });

        renterContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().contains("+")) {
                    renterContactLay.setHelperText("Invalid Number");
                }
                else {
                    renterContactLay.setHelperText("");
                }
            }
        });

        renterEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                renterEmailLay.setHelperText("");
            }
        });

        renterComName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                renterComLay.setHelperText("");
            }
        });

        add.setOnClickListener(view -> {
            name = Objects.requireNonNull(renterName.getText()).toString();
            n_id = Objects.requireNonNull(renterId.getText()).toString();
            r_contact = Objects.requireNonNull(renterContact.getText()).toString();
            r_email = Objects.requireNonNull(renterEmail.getText()).toString();
            r_f_name = Objects.requireNonNull(renterComName.getText()).toString();

            if (!name.isEmpty()) {
                renterNameLay.setHelperText("");
                if(!n_id.isEmpty()) {
                    renterIdLay.setHelperText("");
                    if (!r_contact.isEmpty()) {
                        if (r_contact.contains("+")) {
                            renterContactLay.setHelperText("Invalid Number");
                        }
                        else {
                            renterContactLay.setHelperText("");
                            if(!r_email.isEmpty()) {
                                renterEmailLay.setHelperText("");
                                if (!r_f_name.isEmpty()) {
                                    renterComLay.setHelperText("");
                                    if (!flag_id.isEmpty()) {
                                        activeFlagLay.setVisibility(View.GONE);
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RenterModify.this);
                                        builder.setTitle("Add New Renter!")
                                                .setMessage("Do you want to add New Renter information?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        addRenter();
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
                                        activeFlagLay.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    renterComLay.setHelperText("Please give Flat No/Company Name");
                                }
                            }
                            else {
                                renterEmailLay.setHelperText("Please give Email");
                            }
                        }
                    }
                    else {
                        renterContactLay.setHelperText("Please give Contact");
                    }
                }
                else {
                    renterIdLay.setHelperText("Please give National ID");
                }
            }
            else {
                renterNameLay.setHelperText("Please give Name");
            }
        });

        edit.setOnClickListener(view -> {
            name = Objects.requireNonNull(renterName.getText()).toString();
            n_id = Objects.requireNonNull(renterId.getText()).toString();
            r_contact = Objects.requireNonNull(renterContact.getText()).toString();
            r_email = Objects.requireNonNull(renterEmail.getText()).toString();
            r_f_name = Objects.requireNonNull(renterComName.getText()).toString();

            if (!name.isEmpty()) {
                renterNameLay.setHelperText("");
                if(!n_id.isEmpty()) {
                    renterIdLay.setHelperText("");
                    if (!r_contact.isEmpty()) {
                        if (r_contact.contains("+")) {
                            renterContactLay.setHelperText("Invalid Number");
                        }
                        else {
                            renterContactLay.setHelperText("");
                            if(!r_email.isEmpty()) {
                                renterEmailLay.setHelperText("");
                                if (!r_f_name.isEmpty()) {
                                    renterComLay.setHelperText("");
                                    if (!flag_id.isEmpty()) {
                                        activeFlagLay.setVisibility(View.GONE);
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RenterModify.this);
                                        builder.setTitle("Update Renter!")
                                                .setMessage("Do you want to update this Renter information?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        editRenter();
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
                                        activeFlagLay.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    renterComLay.setHelperText("Please give Flat No/Company Name");
                                }
                            }
                            else {
                                renterEmailLay.setHelperText("Please give Email");
                            }
                        }
                    }
                    else {
                        renterContactLay.setHelperText("Please give Contact");
                    }
                }
                else {
                    renterIdLay.setHelperText("Please give National ID");
                }
            }
            else {
                renterNameLay.setHelperText("Please give Name");
            }
        });

    }

    public void addRenter() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;
        System.out.println(user_code);

        String addRenterUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/createrenter/";

        RequestQueue requestQueue = Volley.newRequestQueue(RenterModify.this);

        StringRequest addRenterReq = new StringRequest(Request.Method.POST, addRenterUrl, response -> {
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
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_RRI_NAME",name);
                headers.put("P_RRI_NATIONAL_ID",n_id);
                headers.put("P_RRI_CONTACT",r_contact);
                headers.put("P_RRI_EMAIL",r_email);
                headers.put("P_RRI_BUSINESS_COMPANY_NAME",r_f_name);
                headers.put("P_RRI_ACTIVE_FLAG",flag_id);
                headers.put("P_USER_NAME",user_code);
                return headers;
            }
        };

        requestQueue.add(addRenterReq);

    }

    private void updateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(RenterModify.this)
                        .setMessage("New Renter Added")
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
                AlertDialog dialog = new AlertDialog.Builder(RenterModify.this)
                        .setMessage("Failed to add new Renter. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    addRenter();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(RenterModify.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                addRenter();
                dialog.dismiss();
            });
        }
    }

    public void editRenter() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;

        String editRenterUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/updaterenter/?P_RRI_ID="+rri_id+"&P_RRI_NAME="+name+"&P_RRI_NATIONAL_ID="+n_id+"&P_RRI_CONTACT="+r_contact+"&P_RRI_EMAIL="+r_email+"&P_RRI_BUSINESS_COMPANY_NAME="+r_f_name+"&P_RRI_ACTIVE_FLAG="+flag_id+"&P_USER_NAME="+user_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(RenterModify.this);

        StringRequest editRenterReq = new StringRequest(Request.Method.PUT, editRenterUrl, response -> {
            System.out.println(response);
            conn = true;
            if (response.equals("200\n")) {
                connected = true;
                editUpdateLay();
            }
            else if (response.equals("100\n")) {
                connected = false;
                editUpdateLay();
            }
            else {
                connected = false;
                editUpdateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            editUpdateLay();
        });

        requestQueue.add(editRenterReq);
    }

    public void editUpdateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(RenterModify.this)
                        .setMessage("Renter Updated")
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
                AlertDialog dialog = new AlertDialog.Builder(RenterModify.this)
                        .setMessage("Failed to Update Renter. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    editRenter();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(RenterModify.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                editRenter();
                dialog.dismiss();
            });
        }
    }

}