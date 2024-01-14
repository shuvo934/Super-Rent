package com.shuvo.rft.superrent.property;

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
import com.shuvo.rft.superrent.property.property_details.PropertyDetailsModify;
import com.shuvo.rft.superrent.renter.ChoiceDataList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProperrtyModify extends AppCompatActivity {

    TextView actionBarText;
    ImageView backLogo;

    TextInputEditText propertyName;
    TextInputLayout propertyNameLay;

    TextInputEditText propertyAddress;
    TextInputLayout propertyAddressLay;

    TextInputEditText propertyOwnerContact;
    TextInputLayout propertyOwnerContactLay;

    TextInputEditText propertyOwner;
    TextInputLayout propertyOwnerLay;

    TextInputEditText propertyNotes;
    TextInputLayout propertyNotesLay;

    AmazingSpinner propertyStatus;
    TextView propertyStatusLay;

    Button add;
    Button edit;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    ArrayList<ChoiceDataList> flagsList;
    String p_name = "";
    String p_address = "";
    String p_owner = "";
    String p_owner_contact = "";
    String p_notes = "";
    String flag_id = "";

    String rpm_id = "";

    private Boolean conn = false;
    private Boolean connected = false;
    String user_code = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properrty_modify);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        actionBarText = findViewById(R.id.modify_property_action_bar_text);
        backLogo = findViewById(R.id.back_logo_of_modify_property);

        fullLayout = findViewById(R.id.add_edit_property_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_add_edit_property);
        circularProgressIndicator.setVisibility(View.GONE);

        propertyName = findViewById(R.id.property_name_for_update_add);
        propertyNameLay = findViewById(R.id.property_name_layout_for_update_add);

        propertyAddress = findViewById(R.id.property_address_for_update_add);
        propertyAddressLay = findViewById(R.id.property_address_layout_for_update_add);

        propertyOwner = findViewById(R.id.property_owner_for_update_add);
        propertyOwnerLay = findViewById(R.id.property_owner_layout_for_update_add);

        propertyOwnerContact = findViewById(R.id.property_owner_contact_for_update_add);
        propertyOwnerContactLay = findViewById(R.id.property_owner_contact_layout_for_update_add);

        propertyNotes = findViewById(R.id.property_notes_for_update_add);
        propertyNotesLay = findViewById(R.id.property_notes_layout_for_update_add);

        propertyStatus = findViewById(R.id.property_active_flag);
        propertyStatusLay = findViewById(R.id.property_active_flag_layout);

        add = findViewById(R.id.add_property_button);
        edit = findViewById(R.id.update_property_button);

        user_code = userInfoLists.get(0).getUser_name();
        flagsList = new ArrayList<>();
        flagsList.add(new ChoiceDataList("0","IN-ACTIVE"));
        flagsList.add(new ChoiceDataList("1","ACTIVE"));

        Intent intent = getIntent();
        boolean editFlag = intent.getBooleanExtra("RMP_EDIT_FLAG",false);
        if (editFlag) {
            actionBarText.setText("EDIT PROPERTY");
            add.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);

            p_name = intent.getStringExtra("RPM_PROP_NAME");
            p_address = intent.getStringExtra("RPM_PROP_ADDRESS");
            p_owner = intent.getStringExtra("RPM_PROP_OWNER");
            p_owner_contact = intent.getStringExtra("RPM_PROP_OWNER_CONTACT");
            String flag_name = intent.getStringExtra("RPM_ACTIVE_FLAG");
            p_notes = intent.getStringExtra("RPM_NOTES");
            rpm_id = intent.getStringExtra("RPM_ID");

            propertyName.setText(p_name);
            propertyAddress.setText(p_address);
            propertyOwner.setText(p_owner);
            propertyOwnerContact.setText(p_owner_contact);
            propertyNotes.setText(p_notes);
            propertyStatus.setText(flag_name);

            for (int i =0; i < flagsList.size(); i++) {
                if (flag_name.equals(flagsList.get(i).getName())) {
                    flag_id = flagsList.get(i).getId();
                }
            }
        }
        else {
            actionBarText.setText("NEW PROPERTY");
            add.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        }

        ArrayList<String> type1 = new ArrayList<>();
        for(int i = 0; i < flagsList.size(); i++) {
            type1.add(flagsList.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

        propertyStatus.setAdapter(arrayAdapter1);

        propertyStatus.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < flagsList.size(); j++) {
                if (name.equals(flagsList.get(j).getName())) {
                    flag_id = (flagsList.get(j).getId());
                }
            }
            System.out.println(flag_id);
            propertyStatusLay.setVisibility(View.GONE);
        });

        backLogo.setOnClickListener(view -> finish());

        propertyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                propertyNameLay.setHelperText("");
            }
        });

        propertyAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                propertyAddressLay.setHelperText("");
            }
        });

        propertyOwner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                propertyOwnerLay.setHelperText("");
            }
        });

        propertyOwnerContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().contains("+")) {
                    propertyOwnerContactLay.setHelperText("Invalid Number");
                }
                else {
                    propertyOwnerContactLay.setHelperText("");
                }

            }
        });

        propertyNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                propertyNotesLay.setHelperText("");
            }
        });

        add.setOnClickListener(view -> {
            p_name = Objects.requireNonNull(propertyName.getText()).toString();
            p_address = Objects.requireNonNull(propertyAddress.getText()).toString();
            p_owner = Objects.requireNonNull(propertyOwner.getText()).toString();
            p_owner_contact = Objects.requireNonNull(propertyOwnerContact.getText()).toString();
            p_notes = Objects.requireNonNull(propertyNotes.getText()).toString();

            if (!p_name.isEmpty()) {
                propertyNameLay.setHelperText("");
                if(!p_address.isEmpty()) {
                    propertyAddressLay.setHelperText("");
                    if (!p_owner_contact.isEmpty()) {
                        if (p_owner_contact.contains("+")) {
                            propertyOwnerContactLay.setHelperText("Invalid Number");
                        }
                        else {
                            propertyOwnerContactLay.setHelperText("");
                            if(!p_owner.isEmpty()) {
                                propertyOwnerLay.setHelperText("");
                                if (!p_notes.isEmpty()) {
                                    propertyNotesLay.setHelperText("");
                                    if (!flag_id.isEmpty()) {
                                        propertyStatusLay.setVisibility(View.GONE);
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProperrtyModify.this);
                                        builder.setTitle("Add New Property!")
                                                .setMessage("Do you want to add New Property information?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        addProperty();
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
                                        propertyStatusLay.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    propertyNotesLay.setHelperText("Please provide Property Notes");
                                }
                            }
                            else {
                                propertyOwnerLay.setHelperText("Please provide Owner Name");
                            }
                        }
                    }
                    else {
                        propertyOwnerContactLay.setHelperText("Please provide Owner Contact");
                    }
                }
                else {
                    propertyAddressLay.setHelperText("Please provide Property Address");
                }
            }
            else {
                propertyNameLay.setHelperText("Please provide Property Name");
            }
        });

        edit.setOnClickListener(view -> {
            p_name = Objects.requireNonNull(propertyName.getText()).toString();
            p_address = Objects.requireNonNull(propertyAddress.getText()).toString();
            p_owner = Objects.requireNonNull(propertyOwner.getText()).toString();
            p_owner_contact = Objects.requireNonNull(propertyOwnerContact.getText()).toString();
            p_notes = Objects.requireNonNull(propertyNotes.getText()).toString();

            if (!p_name.isEmpty()) {
                propertyNameLay.setHelperText("");
                if(!p_address.isEmpty()) {
                    propertyAddressLay.setHelperText("");
                    if (!p_owner_contact.isEmpty()) {
                        if (p_owner_contact.contains("+")) {
                            propertyOwnerContactLay.setHelperText("Invalid Number");
                        }
                        else {
                            propertyOwnerContactLay.setHelperText("");
                            if(!p_owner.isEmpty()) {
                                propertyOwnerLay.setHelperText("");
                                if (!p_notes.isEmpty()) {
                                    propertyNotesLay.setHelperText("");
                                    if (!flag_id.isEmpty()) {
                                        propertyStatusLay.setVisibility(View.GONE);
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProperrtyModify.this);
                                        builder.setTitle("Update Property!")
                                                .setMessage("Do you want to update this Property information?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        editProperty();
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
                                        propertyStatusLay.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    propertyNotesLay.setHelperText("Please provide Property Notes");
                                }
                            }
                            else {
                                propertyOwnerLay.setHelperText("Please provide Owner Name");
                            }
                        }
                    }
                    else {
                        propertyOwnerContactLay.setHelperText("Please provide Owner Contact");
                    }
                }
                else {
                    propertyAddressLay.setHelperText("Please provide Property Address");
                }
            }
            else {
                propertyNameLay.setHelperText("Please provide Property Name");
            }
        });
    }

    public void addProperty() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;

        String addPropertyUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/updateproperty/";

        RequestQueue requestQueue = Volley.newRequestQueue(ProperrtyModify.this);

        StringRequest addPropertyReq = new StringRequest(Request.Method.POST, addPropertyUrl, response -> {
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
                headers.put("P_RPM_PROP_NAME",p_name);
                headers.put("P_RPM_PROP_ADDRESS",p_address);
                headers.put("P_RPM_PROP_OWNER",p_owner);
                headers.put("P_RPM_PROP_OWNER_CONTACT",p_owner_contact);
                headers.put("P_RPM_NOTES",p_notes);
                headers.put("P_RPM_PROP_ACTIVE_FLAG",flag_id);
                headers.put("P_OPERATION_TYPE","1");
                headers.put("P_USER_NAME",user_code);
                return headers;
            }
        };

        requestQueue.add(addPropertyReq);
    }

    private void updateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(ProperrtyModify.this)
                        .setMessage("New Property Added")
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
                AlertDialog dialog = new AlertDialog.Builder(ProperrtyModify.this)
                        .setMessage("Failed to add new Property. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    addProperty();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(ProperrtyModify.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                addProperty();
                dialog.dismiss();
            });
        }
    }

    public void editProperty() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;

        String editPropertyUrl = "http://119.18.148.32:8080/super/superrent_app/masterdata/updateproperty/";

        RequestQueue requestQueue = Volley.newRequestQueue(ProperrtyModify.this);

        StringRequest editPropertyReq = new StringRequest(Request.Method.POST, editPropertyUrl, response -> {
            conn = true;
            System.out.println(response);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_RPM_ID",rpm_id);
                headers.put("P_RPM_PROP_NAME",p_name);
                headers.put("P_RPM_PROP_ADDRESS",p_address);
                headers.put("P_RPM_PROP_OWNER",p_owner);
                headers.put("P_RPM_PROP_OWNER_CONTACT",p_owner_contact);
                headers.put("P_RPM_NOTES",p_notes);
                headers.put("P_RPM_PROP_ACTIVE_FLAG",flag_id);
                headers.put("P_OPERATION_TYPE","2");
                headers.put("P_USER_NAME",user_code);
                return headers;
            }
        };

        requestQueue.add(editPropertyReq);

    }

    public void editUpdateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(ProperrtyModify.this)
                        .setMessage("Property Updated")
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
                AlertDialog dialog = new AlertDialog.Builder(ProperrtyModify.this)
                        .setMessage("Failed to Update Property. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    editProperty();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(ProperrtyModify.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                editProperty();
                dialog.dismiss();
            });
        }
    }
}