package com.shuvo.rft.superrent;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.rft.superrent.dashboard.Dashboard;
import com.shuvo.rft.superrent.login.UserInfoList;
import com.shuvo.rft.superrent.property.PropertyInfo;
import com.shuvo.rft.superrent.renter.RenterInfo;
import com.shuvo.rft.superrent.signup.SignUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView goToSignUp;
    TextInputEditText mob;
    TextInputEditText pass;
    TextView forgotPassword;

    TextView login_failed;

    Button login;

    CheckBox checkBox;

    String mobile = "";
    String password = "";
    private Boolean conn = false;
    private Boolean connected = false;

    public static ArrayList<UserInfoList> userInfoLists = new ArrayList<>();

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    public static final String MyPREFERENCES = "UserPassSuperRent" ;
    public static final String user_emp_code = "nameKey";
    public static final String user_password = "passKey";
    public static final String checked = "trueFalse";

    SharedPreferences sharedpreferences;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fullLayout = findViewById(R.id.login_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_log_in);
        circularProgressIndicator.setVisibility(View.GONE);

        mob = findViewById(R.id.mobile_number_given_log_in);
        pass = findViewById(R.id.password_given_log_in);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);
        goToSignUp = findViewById(R.id.sign_up_text);

        login = findViewById(R.id.log_in_button);

        forgotPassword = findViewById(R.id.forgot_password);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            mob.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);

        pass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    pass.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        goToSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {

            closeKeyBoard();

            login_failed.setVisibility(View.GONE);
            mobile = mob.getText().toString();
            password = pass.getText().toString();

            if (!mobile.isEmpty() && !password.isEmpty()) {
                loginCheck();
            } else {
                Toast.makeText(getApplicationContext(), "Please Give User Name and Password", Toast.LENGTH_SHORT).show();
            }

        });

        closeKeyBoard();
        mob.clearFocus();
        pass.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mob.getWindowToken(), 0);
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

    public void loginCheck() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        conn = false;
        connected = false;

        userInfoLists = new ArrayList<>();

        String loginUrl = "http://119.18.148.32:8080/super/superrent_app/auth/users/?USER_NAME="+mobile+"&PASSWORD="+password+"";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest loginReq = new StringRequest(Request.Method.GET, loginUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userInfo = array.getJSONObject(i);
                        String user_id = userInfo.getString("user_id")
                                .equals("null") ? "" : userInfo.getString("user_id");
                        user_id = transformText(user_id);

                        String user_name = userInfo.getString("user_name")
                                .equals("null") ? "" : userInfo.getString("user_name");
                        user_name = transformText(user_name);

                        String password = userInfo.getString("password")
                                .equals("null") ? "" : userInfo.getString("password");
                        password = transformText(password);

                        String description = userInfo.getString("description")
                                .equals("null") ? "" : userInfo.getString("description");
                        description = transformText(description);

                        String user_grp = userInfo.getString("user_grp")
                                .equals("null") ? "" : userInfo.getString("user_grp");
                        user_grp = transformText(user_grp);

                        String status = userInfo.getString("status")
                                .equals("null") ? "" : userInfo.getString("status");
                        status = transformText(status);

                        String employee_id = userInfo.getString("employee_id")
                                .equals("null") ? "" : userInfo.getString("employee_id");
                        employee_id = transformText(employee_id);

                        String create_by = userInfo.getString("create_by")
                                .equals("null") ? "" : userInfo.getString("create_by");
                        create_by = transformText(create_by);

                        String create_date = userInfo.getString("create_date")
                                .equals("null") ? "" : userInfo.getString("create_date");
                        create_date = transformText(create_date);

                        String update_by = userInfo.getString("update_by")
                                .equals("null") ? "" : userInfo.getString("update_by");
                        update_by = transformText(update_by);

                        String update_date = userInfo.getString("update_date")
                                .equals("null") ? "" : userInfo.getString("update_date");
                        update_date = transformText(update_date);

                        String location = userInfo.getString("location")
                                .equals("null") ? "" : userInfo.getString("location");
                        location = transformText(location);

                        String user_type = userInfo.getString("user_type")
                                .equals("null") ? "" : userInfo.getString("user_type");
                        user_type = transformText(user_type);

                        String emp_id = userInfo.getString("emp_id")
                                .equals("null") ? "" : userInfo.getString("emp_id");
                        emp_id = transformText(emp_id);

                        userInfoLists.add(new UserInfoList(user_id,user_name,password,description,user_grp,
                                status,employee_id,create_by,create_date,update_by,update_date,location,user_type,
                                emp_id));
                    }
                    connected = true;
                }
                else {
                    connected = false;
                }
                goToHomepage();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                goToHomepage();
            }
        }, error -> {
           conn = false;
           connected = false;
           error.printStackTrace();
           goToHomepage();
        });

        requestQueue.add(loginReq);
    }

    private void goToHomepage() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        if (conn) {
            if (connected) {
                if (checkBox.isChecked()){
                    System.out.println("Remembered");
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(user_emp_code);
                    editor.remove(user_password);
                    editor.remove(checked);
                    editor.putString(user_emp_code,mobile);
                    editor.putString(user_password,password);
                    editor.putBoolean(checked,true);
                    editor.apply();
                    editor.commit();
                }
                else {
                    System.out.println("Not Remembered");
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(user_emp_code);
                    editor.remove(user_password);
                    editor.remove(checked);

                    editor.apply();
                    editor.commit();
                }
                Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish();

            }
            else {
                login_failed.setVisibility(View.VISIBLE);
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                loginCheck();
                dialog.dismiss();
            });
        }
    }

    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}