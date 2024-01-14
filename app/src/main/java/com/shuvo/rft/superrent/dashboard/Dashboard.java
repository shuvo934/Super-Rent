package com.shuvo.rft.superrent.dashboard;

import static com.shuvo.rft.superrent.MainActivity.userInfoLists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.rft.superrent.MainActivity;
import com.shuvo.rft.superrent.R;
import com.shuvo.rft.superrent.dashboard.adapters.FiveCollectionAdapters;
import com.shuvo.rft.superrent.dashboard.arraylists.ChartValue;
import com.shuvo.rft.superrent.dashboard.arraylists.FiveCollectLists;
import com.shuvo.rft.superrent.house_bill.HouseBill;
import com.shuvo.rft.superrent.property.PropertyInfo;
import com.shuvo.rft.superrent.renter.RenterInfo;
import com.shuvo.rft.superrent.renter.RenterList;
import com.shuvo.rft.superrent.renter.RenterListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class Dashboard extends AppCompatActivity {

//    CardView renter;
//    CardView property;
//    CardView houseBill;

    TextView userName;
    String user_name = "";
    ImageView logOut;
    BottomNavigationView bottomNavigationView;

    CircularProgressIndicator circularProgressIndicator;
    RelativeLayout fullLayout;
    private Boolean conn = false;
    private Boolean connected = false;
    String user_code = "";

    String total_property = "";
    TextView totalProperty;
    String total_flat = "";
    TextView totalFlat;
    String total_renter = "";
    TextView totalRenter;
    String total_month_amount = "";
    TextView totalAmountMonth;
    String total_collection = "";
    TextView totalCollection;
    String total_due = "";
    TextView totalDue;
    LineChart lineChart;
    ArrayList<ChartValue> chartValues;
    ArrayList<Entry> dataValue;
    ArrayList<String> monthName;

    RecyclerView fiveCollectionView;
    RecyclerView.LayoutManager layoutManager;
    FiveCollectionAdapters fiveCollectionAdapters;
    TextView fiveCollDataNotFound;
    ArrayList<FiveCollectLists> fiveCollectLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        logOut = findViewById(R.id.log_out_icon_main_menu);
        userName = findViewById(R.id.user_full_name);

        fullLayout = findViewById(R.id.dashboard_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_dashboard);
        circularProgressIndicator.setVisibility(View.GONE);

        totalProperty = findViewById(R.id.property_total_dashboard);
        totalFlat = findViewById(R.id.flat_total_dashboard);
        totalRenter = findViewById(R.id.renter_total_dashboard);
        totalAmountMonth = findViewById(R.id.current_month_total_amnt_dashboard);
        totalCollection = findViewById(R.id.collection_total_dashboard);
        totalDue = findViewById(R.id.due_total_dashboard);
        lineChart = findViewById(R.id.collection_growth);
        fiveCollectionView = findViewById(R.id.last_five_collection_list_view);
        fiveCollDataNotFound = findViewById(R.id.five_collection_data_not_found);
        fiveCollDataNotFound.setVisibility(View.GONE);

        fiveCollectionView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        fiveCollectionView.setLayoutManager(layoutManager);

        user_name = userInfoLists.get(0).getUser_name().toUpperCase();
        user_code = userInfoLists.get(0).getUser_name();

        userName.setText(user_name);

        bottomNavigationView = findViewById(R.id.bottom_navigation_layout);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.renter) {
                    Intent intent = new Intent(Dashboard.this, RenterInfo.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.property) {
                    Intent intent = new Intent(Dashboard.this, PropertyInfo.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.house_bill) {
                    Intent intent = new Intent(Dashboard.this, HouseBill.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
                builder.setTitle("LOG OUT!")
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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

        LineChartInit();
    }

    public void LineChartInit() {

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        //xAxis.setAxisMaximum(10f);
        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setExtraLeftOffset(30);
        lineChart.setExtraRightOffset(30);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getLegend().setEnabled(false);
        //lineChart.getAxisLeft().mAxisMinimum = 5000f;
        lineChart.getAxisLeft().setTextColor(Color.GRAY);
        lineChart.getAxisLeft().setXOffset(10f);
        lineChart.getAxisLeft().setTextSize(8);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.setTouchEnabled(false);

    }

    public static class MyAxisValueFormatter extends ValueFormatter {
        private ArrayList<String> mvalues = new ArrayList<>();
        public MyAxisValueFormatter(ArrayList<String> mvalues) {
            this.mvalues = mvalues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {

            if (mvalues != null) {
                if (value < 0 || value >= mvalues.size()) {
                    return "";
                } else {
//                    System.out.println(value);
//                    System.out.println(axis);
//                    System.out.println(mvalues.get((int) value));
                    return (mvalues.get((int) value));
                }
            } else {
                return "";
            }
//            return (mvalues.get((int) value));
        }
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                .setTitle("EXIT!")
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDashboardData();
    }

    public void getDashboardData() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        conn = false;
        connected = false;
        chartValues = new ArrayList<>();
        fiveCollectLists = new ArrayList<>();
//        http://119.18.148.32:8080/super/superrent_app/analytics/duerenterlist/
        String totPropertyUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/totalproperty/"+user_code+"";
        String totFlatUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/totalflat/"+user_code+"";
        String totRenterUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/activerenter/"+user_code+"";
        String totalCMAmountUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/currentmonthtotal/"+user_code+"";
        String totCollAmntUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/currentmonthcollec/"+user_code+"";
        String totColldueAmntUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/currentmonthdue/"+user_code+"";
        String collectionGrowthUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/collectionGrowth?USER_NAME="+user_code+"&RHMBM_RPM_ID=";
        String fiveCollUrl = "http://119.18.148.32:8080/super/superrent_app/analytics/lastfewcollection/"+user_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        StringRequest fiveCollReq = new StringRequest(Request.Method.GET, fiveCollUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String due_renter_name = info.getString("due_renter_name")
                                .equals("null") ? "" : info.getString("due_renter_name");
                        String rhmbd_bill_collect_date = info.getString("rhmbd_bill_collect_date")
                                .equals("null") ? "" : info.getString("rhmbd_bill_collect_date");
                        String rhmbd_collected_amt = info.getString("rhmbd_collected_amt")
                                .equals("null") ? "0" : info.getString("rhmbd_collected_amt");
                        String sl = info.getString("sl")
                                .equals("null") ? "0" : info.getString("sl");

                        fiveCollectLists.add(new FiveCollectLists(due_renter_name,rhmbd_bill_collect_date,rhmbd_collected_amt,sl));
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

        StringRequest collectGrReq = new StringRequest(Request.Method.GET, collectionGrowthUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String rhmbm_ryms_id = info.getString("rhmbm_ryms_id")
                                .equals("null") ? "" : info.getString("rhmbm_ryms_id");
                        String mon = info.getString("mon")
                                .equals("null") ? "" : info.getString("mon");
                        String total_collection = info.getString("total_collection")
                                .equals("null") ? "0" : info.getString("total_collection");

                        chartValues.add(new ChartValue(rhmbm_ryms_id,mon,total_collection));
                    }
                }
                requestQueue.add(fiveCollReq);
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

        StringRequest totColldueAmntReq = new StringRequest(Request.Method.GET, totColldueAmntUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        total_due = info.getString("total_collection")
                                .equals("null") ? "0" : info.getString("total_collection");
                    }
                }
                requestQueue.add(collectGrReq);
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

        StringRequest totCollAmntReq = new StringRequest(Request.Method.GET, totCollAmntUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        total_collection = info.getString("total_collection")
                                .equals("null") ? "0" : info.getString("total_collection");
                    }
                }
                requestQueue.add(totColldueAmntReq);
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

        StringRequest totCMAmntReq = new StringRequest(Request.Method.GET, totalCMAmountUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        total_month_amount = info.getString("total_camount")
                                .equals("null") ? "0" : info.getString("total_camount");
                    }
                }
                requestQueue.add(totCollAmntReq);
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

        StringRequest totRenterReq = new StringRequest(Request.Method.GET, totRenterUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        total_renter = info.getString("total_renter")
                                .equals("null") ? "0" : info.getString("total_renter");
                    }
                }
                requestQueue.add(totCMAmntReq);
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

        StringRequest totFlatReq = new StringRequest(Request.Method.GET, totFlatUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        total_flat = info.getString("total_flat")
                                .equals("null") ? "0" : info.getString("total_flat");
                    }
                }
                requestQueue.add(totRenterReq);
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

        StringRequest totPrpReq = new StringRequest(Request.Method.GET, totPropertyUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject prpTotInfo = array.getJSONObject(i);
                         total_property = prpTotInfo.getString("total_property")
                                .equals("null") ? "0" : prpTotInfo.getString("total_property");
                    }
                }
                requestQueue.add(totFlatReq);
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

        requestQueue.add(totPrpReq);
    }

    public void updateLayout() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        if (conn) {
            if (connected) {
                totalProperty.setText(total_property);
                totalFlat.setText(total_flat);
                totalRenter.setText(total_renter);
                totalAmountMonth.setText(total_month_amount);
                totalCollection.setText(total_collection);
                totalDue.setText(total_due);

                dataValue = new ArrayList<>();

                if (chartValues.size() == 1) {
                    dataValue.add(new Entry(0,0,0));
                    for (int i = 0; i < chartValues.size(); i++) {
                        dataValue.add(new Entry(i+1,Float.parseFloat(chartValues.get(i).getTotal_collection()), chartValues.get(i).getRhmbm_ryms_id()));
                    }
                } else {
                    for (int i = 0; i < chartValues.size(); i++) {
                        dataValue.add(new Entry(i,Float.parseFloat(chartValues.get(i).getTotal_collection()), chartValues.get(i).getRhmbm_ryms_id()));
                    }
                }

                monthName = new ArrayList<>();

                if (chartValues.size() == 1) {
                    monthName.add(chartValues.get(0).getMon());
                    for (int i = 0; i < chartValues.size(); i++) {
                        monthName.add(chartValues.get(i).getMon());
                    }
                } else {
                    for (int i = 0; i < chartValues.size(); i++) {
                        monthName.add(chartValues.get(i).getMon());
                    }
                }

                lineChart.animateXY(1000,1000);
                LineDataSet lineDataSet = new LineDataSet(dataValue,"set1");
//                lineDataSet.setValueFormatter(new MyValueFormatter());
//                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//                dataSets.add(lineDataSet);

                LineData data1 = new LineData(lineDataSet);
                lineDataSet.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSet.setCircleRadius(2f);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setColor(Color.parseColor("#ffc048"));
                lineDataSet.setDrawFilled(true);
                lineDataSet.setValueTextSize(9f);
                Drawable drawable = ContextCompat.getDrawable(Dashboard.this, R.drawable.sales_chart_fill);
                lineDataSet.setFillDrawable(drawable);

                lineDataSet.setDrawCircleHole(false);
                lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                lineChart.getXAxis().setTextSize(4f);
                lineChart.setData(data1);
                lineChart.invalidate();


                if (fiveCollectLists.size() == 0) {
                    fiveCollDataNotFound.setVisibility(View.VISIBLE);
                }
                else {
                    fiveCollDataNotFound.setVisibility(View.GONE);
                }
                fiveCollectionAdapters = new FiveCollectionAdapters(fiveCollectLists,Dashboard.this);
                fiveCollectionView.setAdapter(fiveCollectionAdapters);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getDashboardData();
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
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Exit", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getDashboardData();
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