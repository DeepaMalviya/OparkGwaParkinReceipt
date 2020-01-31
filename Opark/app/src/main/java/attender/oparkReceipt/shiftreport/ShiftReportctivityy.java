/*
package attender.opark.shiftreport;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;

import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Locale;
import java.util.Map;

import attender.opark.MainActivity;
import attender.opark.R;
import attender.opark.base.AppConstants;
import attender.opark.base.AppController;
import attender.opark.base.ClickListener;
import attender.opark.login.Login;
import attender.opark.parkinglist.model.ParkingListMoel;
import attender.opark.subscription.activity.Subscription;
import attender.opark.subscription.activity.Subscriptionctivity;
import attender.opark.subscription.model.CustomRequest;
import attender.opark.base.DividerItemDecoration;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;


public class ShiftReportctivityy extends AppCompatActivity implements RangeTimePickerDialog.ISelectedTime, View.OnClickListener {
    private Toolbar toolBar;
    // private TextView textToolHeader, totalTwoPass,loginTimeshow, timeDifference, tvcash, FourwheelerTotalOutCash, totalCollectionbothPaytmCash, collectionCash, timeDifferenceshow, totalTwowheeler, FourwheelerOutPaytm, FourwheelerTotalOutPaytm, totalCollectionbothPaytm, totalTwowheelerPaytm, totalFourwheeler, totalCollection, totalCollectionPaytm, totalFourwheelerboth, FourwheelerTotalOut, totalCollectionboth, agentName, reportTime, parkingID;
    private CardView card_view;
    SharedPreferences sharedpref;
    String[] pType = new String[3];
    RelativeLayout relativeLayoutReview;
    private ServiceManager mServiceManager = null;
    Printer printer;
    int count = 0;
    RecyclerView recyclerViewParkingList;
    View view, viewTotal;
    ShiftAdapter parkingAdapter;
    ArrayList<ParkingListMoel> parkinglList = new ArrayList<>();
    ArrayList<String> parkinglList1 = new ArrayList<>();
    String vendorId, userRole, userContactNo, vendorName, park, getparkingType;
    String parkingTypelist;
    String vNumber, fromdate, todate, date, time, time1, pass_Add, pass_AddAmount, pass_Renew, pass_RenewAmount,userType;
    SharedPreferences.Editor ed;
    Button getReport;
    TextView from_date, to_date, fromTime, to_time;
    DatePickerDialog datePickerDialog;
    Calendar myCalendar;
    SimpleDateFormat sdf;
    TimePickerDialog mEventTimePickerDialog;
    Date mSelectDate;
    LinearLayout date1;
    TextView printlist, passAdd, passAddAmount, passRenew, passRenewAmount;
    String agentId, parkingId, parkingType, parkingType12, userName, pType2, pType4, parkingType1234, checkInRormal, checkOutRormal, checkoutUsingPaytm, cashCollection, paytmCollection,
            totalcollection, checkInPass, checkOutPass, checkInVIP, checkOutVIP, parking_Type, agentName, reportTime, loginTime, timeDifference, parkingName;
    TextView textToolHeader, etagentName, etreportTime, ettimeDifferenceshow, etparkingTypeReport, etcheckInNormal, etcheckOutNormal, etCheck_Outusing_Paytm, etCheckInPass, etCheckOutPass,
            etCheckInVIP, etCheckOutVIP, etCollectionCash, etpaytmCollection, etTotalCollection, etparkingName, etloginTimeshow;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD); // Set of font family alrady present with itextPdf library.
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.BLACK);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public static final String DEST = "/opark.pdf";

    static final int TIME_DIALOG_ID = 1111;
    private int hr;
    private int min;
    RadioGroup radioGroup;
    RadioButton twowheeler,fourwheeler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_reportctivity);
        printer = WeiposImpl.as().openPrinter();

        init();
        initSdk();
        to_date.setOnClickListener(this);
        from_date.setOnClickListener(this);

        getReport.setOnClickListener(this);
//        if (AppConstants.isInternetAvailable(ShiftReportctivity.this)) {
//            getDetails(parkingId,parkingType);
//        } else {
//            Toast.makeText(ShiftReportctivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
//        }


    }

    public void init() {

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Report");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        fourwheeler = findViewById(R.id.fourwheeler);
        twowheeler = findViewById(R.id.twowheeler);

        radioGroup = findViewById(R.id.radioGroup);


        to_date = (TextView) findViewById(R.id.to_date);
        from_date = (TextView) findViewById(R.id.from_date);
        to_time = (TextView) findViewById(R.id.to_time);
        fromTime = (TextView) findViewById(R.id.fromTime);
        date1 = (LinearLayout) findViewById(R.id.date1);
        getReport = (Button) findViewById(R.id.getReport);
        etagentName = (TextView) findViewById(R.id.etagentName);
        etparkingName = (TextView) findViewById(R.id.etparkingName);
        etreportTime = (TextView) findViewById(R.id.etreportTime);
        etloginTimeshow = (TextView) findViewById(R.id.etloginTimeshow);
        ettimeDifferenceshow = (TextView) findViewById(R.id.ettimeDifferenceshow);
        etparkingTypeReport = (TextView) findViewById(R.id.etparkingTypeReport);
        etcheckInNormal = (TextView) findViewById(R.id.etcheckInNormal);
        etcheckOutNormal = (TextView) findViewById(R.id.etcheckOutNormal);
        etCheck_Outusing_Paytm = (TextView) findViewById(R.id.etCheck_Outusing_Paytm);
        etCheckInPass = (TextView) findViewById(R.id.etCheckInPass);
        etCheckOutPass = (TextView) findViewById(R.id.etCheckOutPass);
        etCheckInVIP = (TextView) findViewById(R.id.etCheckInVIP);
        passAdd = (TextView) findViewById(R.id.passAdd);
        passAddAmount = (TextView) findViewById(R.id.passAddAmount);
        passRenewAmount = (TextView) findViewById(R.id.passRenewAmount);
        passRenew = (TextView) findViewById(R.id.passRenew);

        etCheckOutVIP = (TextView) findViewById(R.id.etCheckOutVIP);
        etCollectionCash = (TextView) findViewById(R.id.etCollectionCash);

        view = (View) findViewById(R.id.view);
        viewTotal = (View) findViewById(R.id.viewTotal);
        etpaytmCollection = (TextView) findViewById(R.id.etpaytmCollection);
        etTotalCollection = (TextView) findViewById(R.id.etTotalCollection);

        relativeLayoutReview = (RelativeLayout) findViewById(R.id.relativeLayoutReview);


        card_view = (CardView) findViewById(R.id.card_view);
        printlist = (TextView) findViewById(R.id.printlist);

        recyclerViewParkingList = (RecyclerView) findViewById(R.id.recyclerViewParkingList);
        recyclerViewParkingList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewParkingList.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));


        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedpref.getString("agentId", "");
        parkingId = sharedpref.getString("parkingId", "");
        parkingType = sharedpref.getString("getparkingType", "");
        parkingType12 = sharedpref.getString("VehicleType1", "");
        userName = sharedpref.getString("userName", "");

        pType = parkingType.toString().split(",");

        if (pType.length == 1) {

            parkingType1234 = sharedpref.getString("VehicleType1", "");
        }


        if (pType.length == 2) {
            //2Wheeler,4Wheeler
            pType2 = pType[0];
            pType4 = pType[1];
        }

        if (AppConstants.isInternetAvailable(ShiftReportctivity.this)) {

            if (pType.length == 2) {
                if (parkingType12.equals("2Wheeler") && pType2.equals("2Wheeler")) {
                    // shiftReport(parkingId, agentId, parkingType12);
                    getDetails(parkingId, pType2);
                } else {
                    //  shiftReport(parkingId, agentId, pType4);
                    getDetails(parkingId, pType4);
                }
                // shiftReport(parkingId, parkingType12, agentId);

            } else {

                // shiftReport(parkingId, agentId, parkingType1234);
                getDetails(parkingId, parkingType1234);

            }
        } else {
            Toast.makeText(ShiftReportctivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }


        if (parkingType12.equals("2Wheeler,")) {

        }
        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker1();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twowheeler) {
                    twowheeler.setChecked(true);
                    userType = "2Wheeler";
                }

                if (checkedId == R.id.fourwheeler) {
                    fourwheeler.setChecked(true);
                    userType = "4Wheeler";

                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            //Do something

            AlertDialog.Builder builder = new AlertDialog.Builder(ShiftReportctivity.this);
            builder.setMessage("Are you sure you want to Logout?").setIcon(R.drawable.oparklogonew).setTitle("Opark")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            logOut(agentId, parkingId);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        if (id == R.id.print) {

            printReceipt();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut(String AgentId, String parkingId) {
        final ProgressDialog pDialog = new ProgressDialog(ShiftReportctivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);


        */
/*http://api.parkoye.com/index.php/parking/transaction_detail?userdata&parkingId=4&agentId=111&transactionId=389&vehicleNo=DL11CZ7000&mode=Normal*//*


        String urlData = AppConstants.BASEURL + "user/logout?userId=" + AgentId + "&parkingId=" + parkingId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlData, null, new Response.Listener<JSONObject>() {


            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(JSONObject response) {
                //  Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");


                    if (status == 0) {

                        JSONObject loginresponce = response.getJSONObject("data");

                        String userId = loginresponce.getString("userId");
                        String isLogOut = loginresponce.getString("isLogOut");

                        getSharedPreferences("opark", Context.MODE_PRIVATE).edit().remove("userContactNo").remove("userpassword").
                                remove("agentId").remove("userName").remove("userRole").remove("parkingVehicle").
                                remove("parkingId").remove("parkingType").remove("parkingName").remove("vendorName").remove("VehicleType1").remove("tagID").commit();

                        Intent logout_intent1 = new Intent(ShiftReportctivity.this, Login.class);
                        logout_intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout_intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                    pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ShiftReportctivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    sendError(e.toString(), "user/logout?userId=");
                    pDialog.dismiss();
                    // Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
//                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(error.toString(), "user/logout?userId=");
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(ShiftReportctivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ShiftReportctivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                // hide the progress dialog
                //      pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


//    public void shiftReportSevice(final String parkingId, final String agentId, String parkingType) {
//
//        final ProgressDialog pDialog = new ProgressDialog(ShiftReportctivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.setIndeterminate(true);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pDialog.show();
//        pDialog.setContentView(R.layout.custom_progress_bar);
//
//      */
/*http://staggingapi.opark.in/index.php/v1/report/shift?parkingId=1&parkingType=2Wheeler&agentId=3*//*

//      */
/*http://staggingapi.opark.in/index.php/v1/report/shift?parkingId=4&agentId=3*//*

//
//        String urlData = AppConstants.BASEURL + "report/shift?parkingId=" + parkingId + "&agentId=" + agentId + "&parkingType=" + parkingType;
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                //Log.d(TAG, response.toString());
//                try {
//                    System.out.println("JSON RETURN " + response.toString());
//
//                    String data = String.valueOf(response.get("data"));
//                    String message = String.valueOf(response.get("message"));
//                    int status = response.getInt("status");
//                    if (status == 0) {
//
//                        JSONObject availableresponce = response.getJSONObject("data");
//
//
//                        parkingname = availableresponce.getString("parkingId");
//                        agentname = availableresponce.getString("agentName");
//                        reporttime = availableresponce.getString("reportTime");
//                        login_Time = availableresponce.getString("loginTime");
//                        tim_Difference = availableresponce.getString("timeDifference");//reportList
//                        if (pType.length == 2) {
//                            SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = storeAllValues.edit();
//                            editor.putString("parkingPrint", "2Wheeler4Wheeler");
//                            editor.apply();
//                            editor.commit();
//*/
/*
//
//                            parkingname = availableresponce.getString("parkingId");
//                            agentname = availableresponce.getString("agentName");
//                            reporttime = availableresponce.getString("reportTime");
//                            login_Time = availableresponce.getString("loginTime");
//                            tim_Difference = availableresponce.getString("timeDifference");//reportList
/*/
/*//*

//
//
//                            card_view1.setVisibility(View.VISIBLE);
//                            card_view.setVisibility(View.VISIBLE);
//                            fix.setVisibility(View.VISIBLE);
//                            fix1.setVisibility(View.VISIBLE);
//                            loginTime123.setVisibility(View.VISIBLE);
//                            timeDifference.setVisibility(View.VISIBLE);
//
//                            TotalCheckInTwoWheeler = availableresponce.getString("TotalCheckIn- 2Wheeler");//TotalCheckIn- 4Wheeler
//                            TotalCheckOutTwoWheeler = availableresponce.getString("TotalCheckOut- 2Wheeler");
//                            TotalCheckOutPaytm = availableresponce.getString("TotalCheckOutPaytm- 2Wheeler");
//                            TotalCollectionPaytm = availableresponce.getString("TotalCollectionPaytm- 2Wheeler");
//                            TotalCollectionTwoWheeler = availableresponce.getString("TotalCollection- 2Wheeler");
//                            cash = availableresponce.getString("TotalCheckOutCash- 2Wheeler");
//                            totalcollectionCash = availableresponce.getString("TotalCollectionCash- 2Wheeler");
//
//                            TotalCheckIn = availableresponce.getString("TotalCheckIn- 4Wheeler");
//                            TotalCheckOut = availableresponce.getString("TotalCheckOut- 4Wheeler");
//                            TotalCheckOutPaytmFour = availableresponce.getString("TotalCheckOutPaytm- 4Wheeler");
//                            TotalCollection = availableresponce.getString("TotalCollection- 4Wheeler");
//                            TotalCollectionPaytmFour = availableresponce.getString("TotalCollectionPaytm- 4Wheeler");
//                            collectionCashFour = availableresponce.getString("TotalCheckOutCash- 4Wheeler");
//                            totalcollectionCashFour = availableresponce.getString("TotalCollectionCash- 4Wheeler");
//
//
//                            */
/*agentName.setText("Attendant Name                : " + agentname);
//                            parkingID.setText("Parking Name                  : " + parkingname);
//                            reportTime.setText(": " + reporttime);*//*

//
//                            totalTwowheeler.setText("2W Total Check In          : " + TotalCheckInTwoWheeler);
//                            totalTwowheelerPaytm.setText("2W PayTm Check Out    : " + TotalCheckOutPaytm);
//                            tvcash.setText("2W Cash Check Out       : " + cash);
//                            totalFourwheeler.setText("2W Total Check Out       : " + TotalCheckOutTwoWheeler);
//
//                            totalCollectionPaytm.setText("PayTm  Collection 2W  : " + TotalCollectionPaytm);
//                            collectionCash.setText("Cash Collection 2W      : " + totalcollectionCash);
//                            totalCollection.setText("Total  Collection 2W     : " + TotalCollectionTwoWheeler);
//
//                            agentName.setText("Attendant Name         : " + agentname);
//                            parkingID.setText(": " + parkingname);
//                            reportTime.setText(": " + reporttime);
//                            loginTimeshow.setText(": " + login_Time);
//                            timeDifferenceshow.setText(": " + tim_Difference);
//
//
//                            totalFourwheelerboth.setText("4W Total Check In          : " + TotalCheckIn);
//                            FourwheelerTotalOut.setText("4W Total Check Out       : " + TotalCheckOut);
//                            FourwheelerTotalOutPaytm.setText("4W Paytm Check Out     : " + TotalCheckOutPaytmFour);
//                            FourwheelerTotalOutCash.setText("4W Cash Check Out       : " + collectionCashFour);
//
//                            totalCollectionbothPaytm.setText("PayTm  Collection 4W   : " + TotalCollectionPaytmFour);
//                            totalCollectionbothPaytmCash.setText("Cash Collection 4W       : " + totalcollectionCashFour);
//                            totalCollectionboth.setText("Total Collection 4W       : " + TotalCollection);
//
//
//                        } else {
//                            if (pType.length == 1) {
//                                if (parkingType12.equals("2Wheeler")) {
//
//                                   */
/* parkingname = availableresponce.getString("parkingId");
//                                    agentname = availableresponce.getString("agentName");
//                                    reporttime = availableresponce.getString("reportTime");
//                                    login_Time = availableresponce.getString("loginTime");
//                                    tim_Difference = availableresponce.getString("timeDifference");//reportList*//*

//
//                                    card_view1.setVisibility(View.GONE);
//                                    fix.setVisibility(View.VISIBLE);
//                                    fix1.setVisibility(View.VISIBLE);
//                                    loginTime123.setVisibility(View.VISIBLE);
//                                    timeDifference.setVisibility(View.VISIBLE);
//                                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = storeAllValues.edit();
//
//                                    editor.putString("parkingPrint", "2Wheeler");
//                                    editor.apply();
//                                    editor.commit();
//
//                                    TotalCheckInTwoWheeler = availableresponce.getString("TotalCheckIn- 2Wheeler");//TotalCheckIn- 4Wheeler
//                                    TotalCheckOutTwoWheeler = availableresponce.getString("TotalCheckOut- 2Wheeler");
//                                    TotalCollectionTwoWheeler = availableresponce.getString("TotalCollection- 2Wheeler");
//                                    TotalCheckOutPaytm = availableresponce.getString("TotalCheckOutPaytm- 2Wheeler");
//                                    TotalCollectionPaytm = availableresponce.getString("TotalCollectionPaytm- 2Wheeler");
//                                    cash = availableresponce.getString("TotalCheckOutCash- 2Wheeler");
//                                    totalcollectionCash = availableresponce.getString("TotalCollectionCash- 2Wheeler");
//
//                                    agentName.setText("Attendant Name         : " + agentname);
//                                    parkingID.setText(": " + parkingname);
//                                    reportTime.setText(": " + reporttime);
//                                    loginTimeshow.setText(login_Time);
//                                    timeDifferenceshow.setText(tim_Difference);
//                                    totalTwowheeler.setText("2W Total Check In              : " + TotalCheckInTwoWheeler);
//                                    totalFourwheeler.setText("2W Total Check Out            : " + TotalCheckOutTwoWheeler);
//                                    totalTwowheelerPaytm.setText("2W PayTm Check Out        : " + TotalCheckOutPaytm);
//                                    tvcash.setText("2W Cash Check Out           : " + cash);
//
//                                    totalCollectionPaytm.setText("PayTm  Collection 2W  : " + TotalCollectionPaytm);
//                                    collectionCash.setText("Cash Collection 2W      : " + totalcollectionCash);
//                                    totalCollection.setText("Total Collection 2W      : " + TotalCollectionTwoWheeler);
//
//
//                                } else {
//
//                                    if (parkingType12.equals("4Wheeler")) {
//
//                                        card_view1.setVisibility(View.GONE);
//                                        fix.setVisibility(View.VISIBLE);
//                                        fix1.setVisibility(View.VISIBLE);
//                                        loginTime123.setVisibility(View.VISIBLE);
//                                        timeDifference.setVisibility(View.VISIBLE);
//                                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor = storeAllValues.edit();
//
//                                        editor.putString("parkingPrint", "4Wheeler");
//                                        editor.apply();
//                                        editor.commit();
//
//                                        TotalCheckIn = availableresponce.getString("TotalCheckIn- 4Wheeler");
//                                        TotalCheckOut = availableresponce.getString("TotalCheckOut- 4Wheeler");
//                                        TotalCheckOutPaytmFour = availableresponce.getString("TotalCheckOutPaytm- 4Wheeler");
//                                        TotalCollection = availableresponce.getString("TotalCollection- 4Wheeler");
//                                        TotalCollectionPaytmFour = availableresponce.getString("TotalCollectionPaytm- 4Wheeler");
//                                        collectionCashFour = availableresponce.getString("TotalCheckOutCash- 4Wheeler");
//                                        totalcollectionCashFour = availableresponce.getString("TotalCollectionCash- 4Wheeler");
//
//                                        agentName.setText("Attendant Name         : " + agentname);
//                                        parkingID.setText(": " + parkingname);
//                                        reportTime.setText(": " + reporttime);
//                                        loginTimeshow.setText(login_Time);
//                                        timeDifferenceshow.setText(tim_Difference);
//
//                                        totalTwowheeler.setText("4W Total Check In          : " + TotalCheckIn);
//                                        totalFourwheeler.setText("4W Total Check Out       : " + TotalCheckOut);
//                                        totalTwowheelerPaytm.setText("4W Paytm Check Out     : " + TotalCheckOutPaytmFour);
//                                        tvcash.setText("4W Cash Check Out       : " + collectionCashFour);
//
//                                        totalCollectionPaytm.setText("PayTm  Collection 4W   : " + TotalCollectionPaytmFour);
//                                        // totalCollection.setText("Cash Collection 4W       : " + totalcollectionCashFour);
//                                        totalCollection.setText("Total Collection 4W       : " + TotalCollection);
//
//                                        // collectionCash.setText("Total Collection 4W       : " + TotalCollection);
//                                        collectionCash.setText("Cash Collection 4W       : " + totalcollectionCashFour);
//
//
//                                    }
//                                }
//                            }
//                        }
//
//                        pDialog.dismiss();
//
//
//                    } else {
//                        Toast.makeText(ShiftReportctivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                    pDialog.dismiss();
//
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                    sendError(e.toString(), "report/shift?parkingId=");
//                    Toast.makeText(ShiftReportctivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
//                    pDialog.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    sendError(e.toString(), "report/shift?parkingId=");
//                    Toast.makeText(ShiftReportctivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
//
//                    pDialog.dismiss();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                sendError(error.toString(), "report/shift?parkingId=");
//                // VolleyLog.d(TAG, "Error: " + error.getMessage());
//
//                Toast.makeText(ShiftReportctivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                pDialog.dismiss();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//
//    }

    private void showTimePicker() {

        if (myCalendar == null) {
            myCalendar = Calendar.getInstance();
        }

        mEventTimePickerDialog = new TimePickerDialog(ShiftReportctivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH));
                datetime.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH));
                datetime.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR));
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.SECOND, 00);
                datetime.set(Calendar.MINUTE, minute);
                mSelectDate = new Date(datetime.getTimeInMillis());
                Date mCurrentDate = new Date(c.getTimeInMillis());

                int hour = hourOfDay % 12;
                fromTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                time = String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM");

//                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
//                    //it's after current
//                    int hour = hourOfDay % 12;
//                   // etTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
//                    time = String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM");
//                } else {
//                    //it's before current'
//                    Toast.makeText(ShiftReportctivity.this, "Invalid Time", Toast.LENGTH_LONG).show();
//                }

            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);

        mEventTimePickerDialog.show();
    }

    private void showTimePicker1() {

        if (myCalendar == null) {
            myCalendar = Calendar.getInstance();
        }

        mEventTimePickerDialog = new TimePickerDialog(ShiftReportctivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH));
                datetime.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH));
                datetime.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR));
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.SECOND, 00);
                datetime.set(Calendar.MINUTE, minute);
                mSelectDate = new Date(datetime.getTimeInMillis());
                Date mCurrentDate = new Date(c.getTimeInMillis());

                int hour = hourOfDay % 12;
                to_time.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                time1 = String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM");

//                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
//                    //it's after current
//                    int hour = hourOfDay % 12;
//                   // etTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
//                    time = String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM");
//                } else {
//                    //it's before current'
//                    Toast.makeText(ShiftReportctivity.this, "Invalid Time", Toast.LENGTH_LONG).show();
//                }

            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);

        mEventTimePickerDialog.show();
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here  hh:mm a
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        to_date.setText(sdf.format(myCalendar.getTime()));
        todate = to_date.getText().toString();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        //  showTimePicker();

        from_date.setText(sdf.format(myCalendar.getTime()));
        fromdate = from_date.getText().toString();

        if (to_date.getText().toString().equals("")) {
            myCalendar = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateLabel1();
                }

            };
            datePickerDialog = new DatePickerDialog(ShiftReportctivity.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

            Date newDate = myCalendar.getTime();
            datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.getReport:
                if (AppConstants.isInternetAvailable(ShiftReportctivity.this)) {


                    String url = AppConstants.BASEURL + "report/shift";
                    Map<String, String> parameterData = new HashMap<>();
                    parameterData.put(("parkingId"), parkingId);
                    parameterData.put(("agentId"), agentId);
                    parameterData.put(("startDateTime"), fromdate + " " + time);
                    parameterData.put(("endDateTime"), todate + " " + time1);
                    parameterData.put(("parkingType"), userType);
                    if (from_date.getText().toString().equals("") && to_date.getText().toString().equals("")) {
                        // getreportShift(url, parameterData);
                        Toast.makeText(this, "Select Date ", Toast.LENGTH_LONG).show();
                    } else if (fromTime.getText().toString().equals("") && to_time.getText().toString().equals("")) {
                        Toast.makeText(this, "Select Time ", Toast.LENGTH_LONG).show();
                    } else {
                        getreportShift(url, parameterData);
                    }

//                    if (pType.length == 2) {
//                        if (parkingType12.equals("2Wheeler") && pType2.equals("2Wheeler")) {
//                            parameterData.put(("parkingType"), parkingType12);
//                            if (from_date.getText().toString().equals("") && to_date.getText().toString().equals("")) {
//                                // getreportShift(url, parameterData);
//                                Toast.makeText(this, "Select Date ", Toast.LENGTH_LONG).show();
//                            } else if (fromTime.getText().toString().equals("") && to_time.getText().toString().equals("")) {
//                                Toast.makeText(this, "Select Time ", Toast.LENGTH_LONG).show();
//                            } else {
//                                getreportShift(url, parameterData);
//                            }
//
//
//                        } else {
//                            parameterData.put(("parkingType"), parkingType12);
//                            if (from_date.getText().toString().equals("") && to_date.getText().toString().equals("")) {
//                                // getreportShift(url, parameterData);
//                                Toast.makeText(this, "Select Date ", Toast.LENGTH_LONG).show();
//                            } else if (fromTime.getText().toString().equals("") && to_time.getText().toString().equals("")) {
//                                Toast.makeText(this, "Select Time ", Toast.LENGTH_LONG).show();
//                            } else {
//                                getreportShift(url, parameterData);
//                            }
//                            // getreportShift(url, parameterData);
//                        }
//                    } else {
//
//                        parameterData.put(("parkingType"), parkingType12);
//                        if (from_date.getText().toString().equals("") && to_date.getText().toString().equals("")) {
//                            // getreportShift(url, parameterData);
//                            Toast.makeText(this, "Select Date ", Toast.LENGTH_LONG).show();
//                        } else if (fromTime.getText().toString().equals("") && to_time.getText().toString().equals("")) {
//                            Toast.makeText(this, "Select Time ", Toast.LENGTH_LONG).show();
//                        } else {
//                            getreportShift(url, parameterData);
//                        }
//                        //getreportShift(url, parameterData);
//                    }
                } else {
                    Toast.makeText(ShiftReportctivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.from_date:
                myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateLabel();
                    }

                };
                datePickerDialog = new DatePickerDialog(ShiftReportctivity.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

                Date newDate = myCalendar.getTime();
                datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());
                break;
            case R.id.to_date:
                myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date12 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateLabel1();
                    }

                };
                datePickerDialog = new DatePickerDialog(ShiftReportctivity.this, date12, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

                Date newDate1 = myCalendar.getTime();
                datePickerDialog.getDatePicker().setMaxDate(newDate1.getTime());
                break;

        }
    }

    private void getreportShift(String url, Map<String, String> parameterData) {
        final ProgressDialog pDialog = new ProgressDialog(ShiftReportctivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectgetReport(jsonObject);
                    pDialog.dismiss();


                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(ShiftReportctivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, parameterData, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            sendError(e.toString(), "report/shift?parkingId=");
            pDialog.dismiss();
        }
    }

    private void processJsonObjectgetReport(JSONObject response) {
        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                if (status == 0) {

                    date1.setVisibility(View.GONE);
                    JSONObject availableresponce = response.getJSONObject("data");

                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("parkingPrint", "2Wheeler4Wheeler");
                    editor.apply();
                    editor.commit();

                    card_view.setVisibility(View.VISIBLE);
                    viewTotal.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);


                    parkingName = availableresponce.getString("parkingId");
                    parking_Type = availableresponce.getString("parkingType");
                    agentName = availableresponce.getString("agentName");
                    reportTime = availableresponce.getString("reportTime");
                    loginTime = availableresponce.getString("loginTime");
                    timeDifference = availableresponce.getString("timeDifference");

                    checkInRormal = availableresponce.getString("CheckIn- Normal");
                    checkOutRormal = availableresponce.getString("CheckOut- Normal");
                    checkoutUsingPaytm = availableresponce.getString("Checkout using Paytm");

                    checkInPass = availableresponce.getString("CheckIn-Pass");
                    checkOutPass = availableresponce.getString("CheckOut-Pass");
                    checkInVIP = availableresponce.getString("CheckIn-VIP");
                    checkOutVIP = availableresponce.getString("CheckOut-VIP");

                    cashCollection = availableresponce.getString("CashCollection");
                    paytmCollection = availableresponce.getString("PaytmCollection");
                    totalcollection = availableresponce.getString("TotalCollection");
                    //pass_Add,pass_AddAmount,pass_Renew,pass_RenewAmount

                    pass_Add = availableresponce.getString("pass-add");
                    pass_AddAmount = availableresponce.getString("passAddAmount");
                    pass_Renew = availableresponce.getString("pass-renew");
                    pass_RenewAmount = availableresponce.getString("passRenewAmount");

                    passAdd.setText("Pass Add                           : " + pass_Add);
                    passAddAmount.setText("Pass Add Amount           : " + pass_AddAmount);
                    passRenew.setText("Pass Renew                      : " + pass_Renew);
                    passRenewAmount.setText("Pass Renew Amount       : " + pass_RenewAmount);

                    etagentName.setText("Attendant Name     : " + agentName);
                    etparkingName.setText("Parking Name : " + parkingName);
                    etparkingTypeReport.setText("Parking Type             : " + parking_Type);
                    etreportTime.setText("Report Time               : " + reportTime);
                    etloginTimeshow.setText("Login Time                 : " + loginTime);
                    ettimeDifferenceshow.setText("Time Difference        : " + timeDifference);

                    etcheckInNormal.setText("Total Check In                    : " + checkInRormal);
                    etcheckOutNormal.setText("Total Check Out                 : " + checkOutRormal);
                    etCheck_Outusing_Paytm.setText("Total Check Out Paytm    : " + checkoutUsingPaytm);

                    etCheckInPass.setText("Total check In Pass          : " + checkInPass);
                    etCheckOutPass.setText("Total check Out Pass       : " + checkOutPass);


                    etCheckInVIP.setText("Total Check In VIP            : " + checkInVIP);
                    etCheckOutVIP.setText("Total check Out VIP          : " + checkOutVIP);
                    // totalTwowheelerPaytm.setText("Total PayTm Check Out    : " + TotalCheckOutPaytm);

                    etCollectionCash.setText("Cash Collection       : " + cashCollection);
                    etpaytmCollection.setText("PayTm  Collection   : " + paytmCollection);

                    etTotalCollection.setText("Total Collection       : " + totalcollection);

                } else {
                    Toast.makeText(ShiftReportctivity.this, message, Toast.LENGTH_SHORT).show();
                    card_view.setVisibility(View.GONE);
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                sendError(e.toString(), "report/shift?parkingId=");
                // pDialog.dismiss();
            } catch (JSONException e) {
                sendError(e.toString(), "report/shift?parkingId=");
                Toast.makeText(this, "Technical Error!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void shiftReport(final String parkingId, final String agentId, String parkingType) {

        final ProgressDialog pDialog = new ProgressDialog(ShiftReportctivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

      */
/*http://staggingapi.opark.in/index.php/v1/report/shift?parkingId=1&parkingType=2Wheeler&agentId=3*//*

      */
/*http://staggingapi.opark.in/index.php/v1/report/shift?parkingId=4&agentId=3*//*


        String urlData = AppConstants.BASEURL + "report/shift?parkingId=" + parkingId + "&agentId=" + agentId + "&parkingType=" + parkingType;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                try {
                    System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");
                    if (status == 0) {

                        date1.setVisibility(View.GONE);
                        JSONObject availableresponce = response.getJSONObject("data");

                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("parkingPrint", "2Wheeler4Wheeler");
                        editor.apply();
                        editor.commit();

                        card_view.setVisibility(View.VISIBLE);
                        viewTotal.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);

                        parkingName = availableresponce.getString("parkingId");
                        parking_Type = availableresponce.getString("parkingType");
                        agentName = availableresponce.getString("agentName");
                        reportTime = availableresponce.getString("reportTime");
                        loginTime = availableresponce.getString("loginTime");
                        timeDifference = availableresponce.getString("timeDifference");

                        checkInRormal = availableresponce.getString("CheckIn- Normal");
                        checkOutRormal = availableresponce.getString("CheckOut- Normal");
                        checkoutUsingPaytm = availableresponce.getString("Checkout using Paytm- ");

                        checkInPass = availableresponce.getString("CheckIn-Pass");
                        checkOutPass = availableresponce.getString("CheckOut-Pass");
                        checkInVIP = availableresponce.getString("CheckIn-VIP");
                        checkOutVIP = availableresponce.getString("CheckOut-VIP");

                        cashCollection = availableresponce.getString("CashCollection");
                        paytmCollection = availableresponce.getString("PaytmCollection");
                        totalcollection = availableresponce.getString("TotalCollection- ");


                        etagentName.setText("Attendant Name     : " + agentName);
                        etparkingName.setText("Parking Name : " + parkingName);
                        etparkingTypeReport.setText("Parking Type             : " + parking_Type);
                        etreportTime.setText("Report Time               : " + reportTime);
                        etloginTimeshow.setText("Login Time                 : " + loginTime);
                        ettimeDifferenceshow.setText("Time Difference        : " + timeDifference);

                        etcheckInNormal.setText("Total Check In                    : " + checkInRormal);
                        etcheckOutNormal.setText("Total Check Out                 : " + checkOutRormal);
                        etCheck_Outusing_Paytm.setText("Total Check Out Paytm    : " + checkoutUsingPaytm);

                        etCheckInPass.setText("Total check In Pass          : " + checkInPass);
                        etCheckOutPass.setText("Total check Out Pass       : " + checkOutPass);


                        etCheckInVIP.setText("Total Check In VIP            : " + checkInVIP);
                        etCheckOutVIP.setText("Total check Out VIP          : " + checkOutVIP);
                        // totalTwowheelerPaytm.setText("Total PayTm Check Out    : " + TotalCheckOutPaytm);

                        etCollectionCash.setText("Cash Collection       : " + cashCollection);
                        etpaytmCollection.setText("PayTm  Collection   : " + paytmCollection);

                        etTotalCollection.setText("Total Collection       : " + totalcollection);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ShiftReportctivity.this, message, Toast.LENGTH_SHORT).show();
                        card_view.setVisibility(View.GONE);
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "report/shift?parkingId=");
                    Toast.makeText(ShiftReportctivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "report/shift?parkingId=");
                    Toast.makeText(ShiftReportctivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();

                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(error.toString(), "report/shift?parkingId=");
                // VolleyLog.d(TAG, "Error: " + error.getMessage());

                Toast.makeText(ShiftReportctivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void printNormal(Context newEntryActivity, Printer printer) {

        */
/*sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        String parkingPrint = sharedpref.getString("parkingPrint", "");*//*


        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);


        if (storeAllValues.getString("parkingPrint", "").equals("2Wheeler4Wheeler")) {
            */
/* String parkingname,agentname,reporttime,TotalCheckInTwoWheeler,TotalCheckOutTwoWheeler,TotalCollectionTwoWheeler,TotalCheckIn,TotalCheckOut,TotalCollection;*//*


            printer.printText("Attender Name : " + agentName, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.LEFT);

            printer.printText(parkingName,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("Parking Type  : " + parking_Type,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("login Time : " + loginTime,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("Reporting  : " + reportTime,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("Difference : " + timeDifference,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);


            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("Total Check In        : " + checkInRormal,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Check Out       : " + checkOutRormal,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Check Out Paytm : " + checkoutUsingPaytm,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Check In Pass   : " + checkInPass,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Check Out Pass  : " + checkOutPass,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Add Pass        : " + pass_Add,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Pass Add Amount       : " + pass_AddAmount,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Pass Renew      : " + pass_Renew,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Pass Renew Amount       : " + pass_RenewAmount,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Check In VIP    : " + checkInVIP,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Check Out VIP   : " + checkOutVIP,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

//            printer.printText("Total Check Out Cash  : " + cashCollection,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);


            printer.printText("---------------", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("Cash Collection  : " + cashCollection,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Paytm Collection : " + paytmCollection,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Total Collection : " + totalcollection,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText("---------------", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);


        }


        if (storeAllValues.getString("parkingPrint", "").equals("parkinglList")) {


            if (count == 1) {
                printer.printText(parkingName,
                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                        Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

                Calendar c = Calendar.getInstance();
                Calendar dateTime = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

                String formattedDate = df.format(c.getTime());
                printer.printText("Date : " + formattedDate,
                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                        Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

                printer.printText("Parking Type  : " + parking_Type,
                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);


                printer.printText("----------------", Printer.FontFamily.SONG,
                        Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                        Printer.Gravity.CENTER);
            }

            printer.printText(String.valueOf(count + ". ") + String.valueOf(vNumber),
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("----------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);
            printer.printText(" ", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);


//            if (count == 1) {
//                printer.printText(parkingName,
//                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.BOLD, Printer.Gravity.LEFT);
//
//                Calendar c = Calendar.getInstance();
//                Calendar dateTime = Calendar.getInstance();
//
//
//                System.out.println("Current time =&gt; " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//
//                String formattedDate = df.format(c.getTime());
//                // Toast.makeText(ShiftReportctivity.this, formattedDate, Toast.LENGTH_LONG).show();
//// Now formattedDate have current date/time
//                // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
//
//                printer.printText("Date : " + formattedDate,
//                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.BOLD, Printer.Gravity.LEFT);
//
//                printer.printText("Parking Type  : " + parking_Type,
//                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//
//                printer.printText("----------------", Printer.FontFamily.SONG,
//                        Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                        Printer.Gravity.CENTER);
//            }
//
//            printer.printText(String.valueOf(count + ". ") + String.valueOf(vNumber),
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("----------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//            printer.printText(" ", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);


        }


    }

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(ShiftReportctivity.this, new Weipos.OnInitListener() {

            @Override
            public void onInitOk() {
                // TODO Auto-generated method stub

                mServiceManager = WeiposImpl.as().openServiceManager();

                try {
                    printer = WeiposImpl.as().openPrinter();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            @Override
            public void onError(String message) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDestroy() {
                // TODO Auto-generated method stub

            }
        });

    }

    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(ShiftReportctivity.this, "No Printer found", Toast.LENGTH_SHORT).show();
            return;
        }

        printer.setOnEventListener(new IPrint.OnEventListener() {

            @Override
            public void onEvent(final int what, String in) {
                // TODO Auto-generated method stub
                final String info = in;

                runOnUiThread(new Runnable() {
                    public void run() {
                        final String message = getPrintErrorInfo(what, info);
                        if (message == null || message.length() < 1) {
                            return;
                        }
                        Toast.makeText(ShiftReportctivity.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(ShiftReportctivity.this, printer);

        } catch (Exception e) {

        }

    }

    private String getPrintErrorInfo(int what, String info) {
        String message = "";
        switch (what) {
            case IPrint.EVENT_CONNECT_FAILD:
                message = "Printer Connetion Fail";
                break;
            case IPrint.EVENT_CONNECTED:

                break;
            case IPrint.EVENT_PAPER_JAM:
                message = "Paper Jam";
                break;
            case IPrint.EVENT_UNKNOW:
                message = "Unknow Error";
                break;
            case IPrint.EVENT_STATE_OK:

                break;
            case IPrint.EVENT_OK://

                break;
            case IPrint.EVENT_NO_PAPER:
                message = "No Paper Found";
                break;
            case IPrint.EVENT_HIGH_TEMP:
                message = "High Temp ";
                break;
            case IPrint.EVENT_PRINT_FAILD:
                message = "Print Fail";
                break;
        }

        return message;
    }

//    public void printNormal(Context newEntryActivity, Printer printer) {
//
//        */
/*sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
//        String parkingPrint = sharedpref.getString("parkingPrint", "");*//*

//
//        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
//
//
//        if (storeAllValues.getString("parkingPrint", "").equals("2Wheeler4Wheeler")) {
//            */
/* String parkingname,agentname,reporttime,TotalCheckInTwoWheeler,TotalCheckOutTwoWheeler,TotalCollectionTwoWheeler,TotalCheckIn,TotalCheckOut,TotalCollection;*//*

//
//            printer.printText("Attender Name : " + agentname, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
//                    Printer.Gravity.LEFT);
//
//            printer.printText("Parking Name  : " + parkingname,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//           */
/* printer.printText("---------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);*//*

//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("login Time : " + login_Time,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Reporting : " + reporttime,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Difference : " + tim_Difference,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//
//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("2W Total Check In   : " + TotalCheckInTwoWheeler,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("2W Paytm CheckOut   : " + TotalCheckOutPaytm,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("2W Cash CheckOut    : " + cash,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("2W Total Check Out  : " + TotalCheckOutTwoWheeler,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("---------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("Paytm Collection 2W : " + TotalCollectionPaytm,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("Cash Collection 2W  : " + totalcollectionCash,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("Total Collection 2W : " + TotalCollectionTwoWheeler,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);
//
//            printer.printText("---------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//
//            // printer.printQrCode(barCode, 200, Printer.Gravity.CENTER);
//
//            // printer.printBarCode(barCode,1,100,1);
//
//
//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("4W Total CheckIn    : " + TotalCheckIn, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("4W Paytm CheckOut   : " + TotalCheckOutPaytmFour, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("4W Cash CheckOut    : " + collectionCashFour, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//           */
/* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
/*/
/*//*

//            printer.printText("4W Total CheckOut   : " + TotalCheckOut, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("---------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("Paytm Collection 4W : " + TotalCollectionPaytmFour, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("Cash Collection  4W : " + totalcollectionCashFour, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("Total Collection 4W : " + TotalCollection, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);
//
//            printer.printText("\n", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//
//        }
//        if (storeAllValues.getString("parkingPrint", "").equals("2Wheeler")) {
//            // Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
//
//            printer.printText("Attender Name : " + agentname, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
//                    Printer.Gravity.LEFT);
//
//            printer.printText("Parking Name  : " + parkingname,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//           */
/* printer.printText("---------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);*//*

//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("login Time : " + login_Time,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Reporting : " + reporttime,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Difference : " + tim_Difference,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//
//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("2W Total CheckIn    :  " + TotalCheckInTwoWheeler, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//            printer.printText("2W Paytm CheckOut  :  " + TotalCheckOutPaytm,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText("2W Cash CheckOut    : " + cash,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//
//            printer.printText("2W Total Check Out  :  " + TotalCheckOutTwoWheeler,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.BOLD, Printer.Gravity.CENTER);
//
//            printer.printText("--------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
////
//            printer.printText("Paytm Collection 2W : " + TotalCollectionPaytm,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//            printer.printText("Cash Collection 2W   : " + totalcollectionCash,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("--------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("Total Collection 2W : " + TotalCollectionTwoWheeler,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.BOLD, Printer.Gravity.RIGHT);
//
//
//            printer.printText("\n", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//
//        }
//        if (storeAllValues.getString("parkingPrint", "").equals("4Wheeler")) {
//            // Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
//
//            printer.printText("Attender Name : " + agentname, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
//                    Printer.Gravity.LEFT);
//
//            printer.printText("Parking Name  : " + parkingname,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//           */
/* printer.printText("---------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);*//*

//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("login Time : " + login_Time,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Reporting : " + reporttime,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Difference : " + tim_Difference,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//
//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//            printer.printText("4W Total CheckIn  : " + TotalCheckIn, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//            printer.printText("4W Paytm CheckOut : " + TotalCheckIn, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//            printer.printText("4W Cash CheckOut  : " + TotalCheckIn, Printer.FontFamily.SONG,
//                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("4W Total CheckOut : " + TotalCheckOut,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.BOLD, Printer.Gravity.CENTER);
//
//            printer.printText("--------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//            printer.printText("Paytm Collection 4W : " + TotalCollectionPaytmFour,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("Cash Collection 4W  : " + totalcollectionCashFour,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("--------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
////
//            printer.printText("Total Collection 4W : " + TotalCollection,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.BOLD, Printer.Gravity.RIGHT);
//
//            printer.printText("\n", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//
//        }
//
//
//        if (storeAllValues.getString("parkingPrint", "").equals("parkinglList")) {
//
//            if (count == 1) {
//                printer.printText(parkingname,
//                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.BOLD, Printer.Gravity.LEFT);
//
//                Calendar c = Calendar.getInstance();
//                Calendar dateTime = Calendar.getInstance();
//
//
//                System.out.println("Current time =&gt; " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//
//                String formattedDate = df.format(c.getTime());
//                // Toast.makeText(ShiftReportctivity.this, formattedDate, Toast.LENGTH_LONG).show();
//// Now formattedDate have current date/time
//                // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
//
//                printer.printText("Date : " + formattedDate,
//                        Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.BOLD, Printer.Gravity.LEFT);
//
//
//                printer.printText("----------------", Printer.FontFamily.SONG,
//                        Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                        Printer.Gravity.CENTER);
//            }
//
//            printer.printText(String.valueOf(count + ". ") + String.valueOf(vNumber),
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
//
//            printer.printText("----------------", Printer.FontFamily.SONG,
//                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//            printer.printText(" ", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.BOLD,
//                    Printer.Gravity.CENTER);
//
//
//        }
//
//
//    }

    public void getDetails(String parkingId, String parkingType) {
        final ProgressDialog pDialog = new ProgressDialog(ShiftReportctivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
       */
/*http://staggingapi.opark.in/index.php/v1/report/list?parkingId=1*//*

        String urlData = AppConstants.BASEURL + "report/list?parkingId=" + parkingId + "&parkingType=" + parkingType;
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                parkinglList.clear();

                try {
                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");
                    // int detail = json.getInt("detail");

                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);

                        relativeLayoutReview.setVisibility(View.VISIBLE);
                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
                        int countlist = 0;
                        for (int i = 0; i < arrayObj.length(); i++) {

                            countlist++;
                            JSONObject jsonObject = arrayObj.getJSONObject(i);

                            ParkingListMoel parkinglListModel = new ParkingListMoel();

                            parkinglListModel.setVehicleNo(jsonObject.getString("vehicleNo"));
                            parkinglListModel.setCheckInDateTime(jsonObject.getString("checkInDateTime"));


                            String n = jsonObject.getString("vehicleNo");
                            String d = jsonObject.getString("checkInDateTime");

                            parkinglList.add(parkinglListModel);
                            parkinglList1.add("VehicleNo." + n + "\n" + "CheckInDate: " + d);
                        }


                        parkingAdapter = new ShiftAdapter(ShiftReportctivity.this, ShiftReportctivity.this, parkinglList);
                        recyclerViewParkingList.setAdapter(parkingAdapter);
                        parkingAdapter.notifyDataSetChanged();


                        printlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ShiftReportctivity.this);
                                builder.setMessage("Do you want to print ?").setIcon(R.drawable.logo).setTitle("Opark")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = storeAllValues.edit();
                                                editor.putString("parkingPrint", "parkinglList");

                                                editor.apply();
                                                editor.commit();
                                                count = 0;

                                                for (int i = 0; i < parkinglList1.size(); i++) {
                                                    vNumber = parkinglList1.get(i);
                                                    count++;
                                                    printReceipt();
                                                }

//                                                int listSize = parkinglList1.size();
//                                                final int itemPrint = 5;
//                                                final int pageRequired = listSize / itemPrint;
//                                                int i;
//                                                for (i = 0; i <= pageRequired; i++) {
//
//                                                    for (int j = itemPrint * i; j < itemPrint * (i + 1); j++) {
//                                                        if (j<listSize){
//                                                            vNumber = parkinglList1.get(j);
//                                                            count++;
//                                                            printReceipt();
//                                                        }
//                                                    }
//                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });


                        pDialog.dismiss();
                        //  printlist.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(ShiftReportctivity.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(ShiftReportctivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ShiftReportctivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        Toast.makeText(ShiftReportctivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;

        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(ShiftReportctivity.this)) {
            send(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    private void send(String url, Map<String, String> parameterData) {
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObject(jsonObject);

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, parameterData, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
        }
    }

    private void processJsonObject(JSONObject response) {
        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");
            } catch (NullPointerException e) {
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPdf(String sometext) {
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/oparkpdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "bm.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "PDF Save", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    int minteger = 0;

    public void createPdfdoc(View view) {

        // Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
        minteger = minteger + 1;
        String root = Environment.getExternalStorageDirectory().toString() + "/MyPDFDoc";
        final File dir = new File(root);
        if (!dir.exists())
            dir.mkdirs();
        try {
            PrintDocument(dir.getPath() + DEST + minteger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PrintDocument(String dest) throws IOException, IOException {
        try {

//            Document document = new Document();
            Document document = new Document(PageSize.PENGUIN_SMALL_PAPERBACK, 10f, 10f, 20f, 0f);//PENGUIN_SMALL_PAPERBACK used to set the paper size
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            addTitlePage(document);
            document.close();

            File file = new File(dest);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTitlePage(Document document)
            throws DocumentException {

        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("Vehicle List", subFont));
        addEmptyLine(preface, 1);
        // Lets write a big header
        int count = 0;
        for (int i = 0; i < parkinglList1.size(); i++) {

            vNumber = parkinglList1.get(i);
            count++;
            preface.add(new Paragraph(count + ". " + vNumber, redFont));
            addEmptyLine(preface, 1);
        }


        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void showCustomDialogTimePicker() {
        // Create an instance of the dialog fragment and show it
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.newInstance();
        dialog.setIs24HourView(true);
        dialog.setRadiusDialog(20);
        dialog.setTextTabStart("Start");
        dialog.setTextTabEnd("End");
        dialog.setTextBtnPositive("Accept");
        dialog.setTextBtnNegative("Close");
        dialog.setValidateRange(false);
        dialog.setColorBackgroundHeader(R.color.colorPrimary);
        dialog.setColorBackgroundTimePickerHeader(R.color.colorPrimary);
        dialog.setColorTextButton(R.color.colorPrimaryDark);
        dialog.enableMinutes(true);
//        dialog.setStartTabIcon(R.drawable.ic_access_time_black_24dp);
//        dialog.setEndTabIcon(R.drawable.ic_timelapse_black_24dp);
//      dialog.setInitialOpenedTab(RangeTimePickerDialog.InitialOpenedTab.START_CLOCK_TAB);
//      dialog.setInitialStartClock(3,45);
//      dialog.setInitialEndClock(16,33);
        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "");
    }

    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        fromTime.setText(hourStart + ":" + minuteStart);
        to_time.setText(hourEnd + ":" + minuteEnd);
        //   Toast.makeText(this, "Start: " + hourStart + ":" + minuteStart + "\nEnd: " + hourEnd + ":" + minuteEnd, Toast.LENGTH_SHORT).show();
    }


//    public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {
//
//        public Activity c;
//        public Dialog d;
//        public Button yes, no;
//
//        public CustomDialogClass(Activity a) {
//            super(a);
//            // TODO Auto-generated constructor stub
//            this.c = a;
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.custom_dialog);
//            yes = (Button) findViewById(R.id.btn_yes);
//            no = (Button) findViewById(R.id.btn_no);
//            yes.setOnClickListener(this);
//            no.setOnClickListener(this);
//
//        }
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//
//                case R.id.btn_yes:
//                    createPdf1( );
//                    break;
//                case R.id.btn_no:
//
//                    break;
//                default:
//                    break;
//            }
//            dismiss();
//        }
//    }

}
*/
