package com.shuvo.rft.superrent.signup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.renter.RenterModify;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    TextInputLayout fullNameLay, addressLay, mobNumblay, emailLay, passwordLay, confirmPasswordLay;
    TextInputEditText fullName, address, mobNUmb, email, password, confirmPassword;

    Button signUp;

    TextView goToLogin;

    CircularProgressIndicator circularProgressIndicator;
    LinearLayout fullLayout;

    String name = "";
    String addrr = "";
    String numb= "";
    String mail = "";
    String pass = "";
    String conPass = "";

    private Boolean conn = false;
    private Boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullNameLay = findViewById(R.id.user_name_sign_up);
        addressLay = findViewById(R.id.house_address_sign_up);
        mobNumblay = findViewById(R.id.mobile_number_sign_up);
        emailLay = findViewById(R.id.email_address_sign_up);
        passwordLay = findViewById(R.id.password_sign_up);
        confirmPasswordLay = findViewById(R.id.confirm_password_sign_up);

        fullName = findViewById(R.id.user_name_given_sign_up);
        address = findViewById(R.id.house_address_given_sign_up);
        mobNUmb = findViewById(R.id.mobile_number_given_sign_up);
        email = findViewById(R.id.email_address_given_sign_up);
        password = findViewById(R.id.password_given_sign_up);
        confirmPassword = findViewById(R.id.confirm_password_given_sign_up);

        signUp = findViewById(R.id.sign_up_button);

        fullLayout = findViewById(R.id.sign_up_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_sign_up);
        circularProgressIndicator.setVisibility(View.GONE);

        goToLogin = findViewById(R.id.login_text);

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //for Email validation check
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if (!email.getText().toString().isEmpty()) {
                            if(!email.getText().toString().contains("@")) {
                                emailLay.setHelperText("Invalid Email Address");
                            } else {
                                emailLay.setHelperText("");
                            }
                        }
                        else {
                            emailLay.setHelperText("Email address must be given");
                            emailLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!email.getText().toString().isEmpty()) {
                        if(!email.getText().toString().contains("@")) {
                            emailLay.setHelperText("Invalid Email Address");
                        } else {
                            emailLay.setHelperText("");
                        }
                    }
                    else {
                        emailLay.setHelperText("Email address must be given");
                        emailLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking user name is given or not
        fullName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!fullName.getText().toString().isEmpty()) {
                            fullNameLay.setHelperText("");
                        } else {
                            fullNameLay.setHelperText("Name must be given");
                            fullNameLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        fullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!fullName.getText().toString().isEmpty()) {
                        fullNameLay.setHelperText("");
                    } else {
                        fullNameLay.setHelperText("Name must be given");
                        fullNameLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking phone is given or not
        mobNUmb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!mobNUmb.getText().toString().isEmpty()) {
                            if(mobNUmb.getText().toString().length() < 11) {
                                mobNumblay.setHelperText("Phone Number is nor correct");
                            } else {
                                mobNumblay.setHelperText("");
                            }
                        } else {
                            mobNumblay.setHelperText("Phone number must be given");
                            mobNumblay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        mobNUmb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!mobNUmb.getText().toString().isEmpty()) {
                        if(mobNUmb.getText().toString().length() < 11) {
                            mobNumblay.setHelperText("Phone Number is nor correct");
                        } else {
                            mobNumblay.setHelperText("");
                        }
                    } else {
                        mobNumblay.setHelperText("Phone number must be given");
                        mobNumblay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking address is given or not
        address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!address.getText().toString().isEmpty()) {
                            addressLay.setHelperText("");
                        } else {
                            addressLay.setHelperText("Address must be given");
                            addressLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!address.getText().toString().isEmpty()) {
                        addressLay.setHelperText("");
                    } else {
                        addressLay.setHelperText("Address must be given");
                        addressLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking password is given or not
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!password.getText().toString().isEmpty()) {
                            passwordLay.setHelperText("");
                        } else {
                            passwordLay.setHelperText("Password must be given");
                            passwordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

//                        password.clearFocus();
//                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!password.getText().toString().isEmpty()) {
                        passwordLay.setHelperText("");
                    } else {
                        passwordLay.setHelperText("Password must be given");
                        passwordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking confirm password is given or not
        confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!confirmPassword.getText().toString().isEmpty()) {
                            confirmPasswordLay.setHelperText("");
                        } else {
                            confirmPasswordLay.setHelperText("Password must be given");
                            confirmPasswordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        confirmPassword.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!confirmPassword.getText().toString().isEmpty()) {
                        confirmPasswordLay.setHelperText("");
                        if (!password.getText().toString().isEmpty()) {
                            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                                confirmPasswordLay.setHelperText("Password did not match");
                                confirmPasswordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                            }
                        }
                    } else {
                        confirmPasswordLay.setHelperText("Password must be given");
                        confirmPasswordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
                name = fullName.getText().toString();
                mail = email.getText().toString();
                pass = confirmPassword.getText().toString();
                numb = mobNUmb.getText().toString();
                addrr = address.getText().toString();
                String firstPass = password.getText().toString();

                if (firstPass.equals(pass)) {
                    if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || numb.isEmpty() || addrr.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Fill Up The Required Field",Toast.LENGTH_SHORT).show();
                    }
                    else if (numb.length() < 11) {
                        Toast.makeText(getApplicationContext(), "Phone Number is not correct",Toast.LENGTH_SHORT).show();
                    }
                    else if (!mail.contains("@")) {
                        Toast.makeText(getApplicationContext(), "Please Give Proper Email Address",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        System.out.println(pass);
                        addUser();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password did not match",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            finish();
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void addUser() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        conn = false;
        connected = false;

        String addUserUrl = "http://119.18.148.32:8080/super/superrent_app/auth/signup";

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);

        StringRequest addUserReq = new StringRequest(Request.Method.POST, addUserUrl, response -> {
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
                headers.put("P_MOBILE_NO",numb);
                headers.put("P_PASSWORD",pass);
                headers.put("P_STATUS","1");
                headers.put("P_USER_TYPE","1");
                headers.put("P_ANALYTICS_ACCESS","1");
                headers.put("P_USER_FULL_NAME",name);
                headers.put("P_USER_PROPERTY_ADDRESS",addrr);
                headers.put("p_email_address", mail);
                return headers;
            }
        };

        requestQueue.add(addUserReq);

    }

    private void updateLay() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);

        if(conn) {
            if (connected) {
                AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                        .setMessage("Sign Up Successful. Please Login to continue.")
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
                AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                        .setMessage("Failed to Sign Up. Please try again.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    addUser();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                addUser();
                dialog.dismiss();
            });
        }
    }
}