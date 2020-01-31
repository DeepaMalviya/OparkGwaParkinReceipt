/*
package attender.opark.parkinglist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import attender.opark.R;
import attender.opark.TransationActivity;
import attender.opark.base.AppConstants;
import attender.opark.base.AppController;
import attender.opark.booking.AvailableSlotModel;
import attender.opark.booking.CheckOutModel;
import attender.opark.booking.FourWheeler;
import attender.opark.booking.QRScanPrint;
import attender.opark.booking.ReceiptLoss;
import attender.opark.booking.TwoWheeler;
import attender.opark.login.Login;
import attender.opark.booking.CheckInModel;
import attender.opark.apkservices.MyService;
import attender.opark.shiftreport.ShiftReportctivity;
import attender.opark.subscription.activity.RenewCard;
import attender.opark.subscription.activity.Subscription;
import attender.opark.subscription.model.CustomRequest;
import attender.opark.subscription.model.SubscriptionModel;
import attender.opark.subscription.activity.Subscriptionctivity;
import attender.opark.vehiclelist.activity.FourWheelerList;
import attender.opark.vehiclelist.activity.LossReceipt;
import attender.opark.vehiclelist.activity.TwoWheelerList;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String android_id;
    Toolbar toolBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sharedpref;
    String agentId = "", userRole = "", userName = "", userContactNo = "", vendorId = "", vendorName = "", parkingName = "", parkingType = "", parkingId = "", userType = "", parkingType11, parkingType12 = "", subVehicleNo = "";
    String poweredBy = "", companyWebsite = "", parkingAddress = "", receiptHeading = "", printError = "", message1 = "";
    private TextView textToolHeader;
    CircleImageView toolBarUserAvatar;
    private TextView tvAvailableSpots, nameHeader, headerEmail;
    private TextView tvTotalSpots, attenderName, parking_Name;
    private String mobileNumber = "";
    private String vehicleNumber = "";
    private String StateCode = "";
    private String CityCode = "";
    private String VehicleCode = "";
    private String vipvehicleNumber = "";
    LinearLayout normalUserVehicle, vipUserVehicle;
    private Button btnCheckIn;
    private Button btnCheckout;
    private Button btnScanQRCodeCheckIn1;
    private Button btnScanQRCode, btnScanQRCodeCheckIn;
    ProgressDialog dialog;
    String tagID = "", vipInfo = "";
    private int REQUEST_CODE_BLUETOOTH = 100;
    private int REQUEST_CODE_LOCATION = 101;
    private int REQUEST_CODE_STORAGE = 102;
    private RadioGroup radioGroup;
    private RadioButton normalUser, vipUser, onlineUser;
    EditText etMobileNo, etStateCode, etCityCode, etVehicleCode, etVehicleNumber, vipVehicleNumber, otpVehicleNumber, vipinfo;
    String getReceiptHeading, getParkingAddress, getCheckInDate, getVehicleNo, getParkingRate, getAdditionalParkingRate, barCode, getQrCode, ReceiptStaticText,
            getAgentName, getReceiptMobile, getPoweredBy, lastLine, getCompanyWebsite, getReceiptNo, getprintReceipt, onlineUserText;
    String getReceiptHeadingOut, getParkingAddressOut, getCheckInDateOut, getCheckOutDateOut, getVehicleNoOut, getPoweredByOut, getCompanyWebsiteOut, getReceiptNoOut,
            getduration, getDurationUnit, getCurrencySymbol, getGrandTotal, parkingType2, getParkingID, getParkingName;

    SharedPreferences.Editor ed;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private NfcAdapter mNfcAdapter;
    String[] pType = new String[3];
    private ServiceManager mServiceManager = null;
    Printer printer;
    MyService myService;
    InputFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printer = WeiposImpl.as().openPrinter();

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("getparkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        getParkingID = sharedpref.getString("getParkingID", "");
        getParkingName = sharedpref.getString("getParkingName", "");


        //parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType12 = sharedpref.getString("VehicleType1", "");

        //  parkingType2 = sharedpref.getString("VehicleType2", "");


        findViews();
        initSdk();
        initNFC();


        if (parkingType.equals("2Wheeler,4Wheeler")) {
            navigationView.getMenu().findItem(R.id.nav_twowheeler).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_LossReceipt).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_carInActivityTwo).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_fourwheeler).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_carOutActivityFour).setVisible(true);
            navigationView.getMenu().findItem(R.id.towvhicle).setVisible(false);

        } else {
            if (parkingType12.equals("4Wheeler")) {
                navigationView.getMenu().findItem(R.id.nav_twowheeler).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_LossReceipt).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_fourwheeler).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_carInActivityTwo).setVisible(false);
                navigationView.getMenu().findItem(R.id.towvhicle).setVisible(false);
                // navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            }
            if (parkingType12.equals("2Wheeler")) {
                navigationView.getMenu().findItem(R.id.nav_fourwheeler).setVisible(false);
                navigationView.getMenu().findItem(R.id.towvhicle).setVisible(false);
                // navigationView.getMenu().findItem(R.id.nav_sub).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_twowheeler).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_LossReceipt).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_carOutActivityFour).setVisible(false);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        initSdk();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

        if (AppConstants.isInternetAvailable(MainActivity.this)) {
            availableSlotSevice(parkingId, parkingType12, agentId);
        } else {
            Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
        normalUser.setChecked(true);
        if (userType.equals("Normal")) {
            btnScanQRCode.setVisibility(View.VISIBLE);
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
        }


    }

    public void url() {

        */
/*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*//*

        */
/*http://staggingapi.opark.in/index.php/v1/user/inventory?parkingId=1&parkingType=2W*//*

        */
/*http://staggingapi.opark.in/index.php/v1/parking/checkin?parkingId=1&parkingType=4Wheeler&vehicleNo=6678&agentId=3&mode=Normal&userdata=9993289838*//*

    }

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(MainActivity.this, new Weipos.OnInitListener() {

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

    private void findViews() {

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        textToolHeader = (TextView) findViewById(R.id.toolbar_title);
        toolBarUserAvatar = (CircleImageView) toolBar.findViewById(R.id.toolBarUserAvatar);


        if (parkingType12.equals("2Wheeler")) {
            textToolHeader.setText("Two Wheeler");
            toolBarUserAvatar.setImageResource(R.mipmap.twobike);
        } else {
            textToolHeader.setText("Four Wheeler");
            toolBarUserAvatar.setImageResource(R.mipmap.police_car512);
        }

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        btnScanQRCodeCheckIn1 = (Button) findViewById(R.id.btnScanQRCodeCheckIn1);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        btnScanQRCode = (Button) findViewById(R.id.btnScanQRCode);
        btnScanQRCodeCheckIn = (Button) findViewById(R.id.btnScanQRCodeCheckIn);
        attenderName = (TextView) findViewById(R.id.attenderName);
        parking_Name = (TextView) findViewById(R.id.parking_Name);

        normalUserVehicle = (LinearLayout) findViewById(R.id.normalUserVehicle);
        vipUserVehicle = (LinearLayout) findViewById(R.id.vipUserVehicle);

        tvTotalSpots = (TextView) findViewById(R.id.tvTotalSpots);
        tvAvailableSpots = (TextView) findViewById(R.id.tvAvailableSpots);
        otpVehicleNumber = (EditText) findViewById(R.id.otpVehicleNumber);
        vipinfo = (EditText) findViewById(R.id.vipinfo);


        normalUser = (RadioButton) findViewById(R.id.normalUser);
        vipUser = (RadioButton) findViewById(R.id.vipUser);
        onlineUser = (RadioButton) findViewById(R.id.onlineUser);
        etStateCode = (EditText) findViewById(R.id.etStateCode);
        etCityCode = (EditText) findViewById(R.id.etCityCode);
        etVehicleCode = (EditText) findViewById(R.id.etVehicleCode);
        etVehicleNumber = (EditText) findViewById(R.id.etVehicleNumber);
        vipVehicleNumber = (EditText) findViewById(R.id.vipVehicleNumber);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        attenderName.setText(userName);
        parking_Name.setText(getParkingName);

        View hraderView = navigationView.getHeaderView(0);
        nameHeader = (TextView) hraderView.findViewById(R.id.nameHeader);
        headerEmail = (TextView) hraderView.findViewById(R.id.headerEmail);
        nameHeader.setText(userName);
        //  headerEmail.setText(userName + ".@gmail.com");

        etVehicleNumber.requestFocus();

        setListener();
    }

    private void setListener() {

        btnCheckIn.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        btnScanQRCode.setOnClickListener(this);
        btnScanQRCodeCheckIn.setOnClickListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {


                    case R.id.nav_twowheeler:
                        Intent intentTwo = new Intent(MainActivity.this, TwoWheeler.class);
                        intentTwo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentTwo);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;

                    case R.id.nav_fourwheeler:
                        Intent intentFour = new Intent(MainActivity.this, FourWheeler.class);
                        intentFour.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentFour);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        return true;
//                    case R.id.nav_receiptLoss:
//                        Intent intentLossReceipt = new Intent(MainActivity.this, ReceiptLoss.class);
//                        intentLossReceipt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|IntentCompat.FLAG_ACTIVITY_CLEAR_TASK)
                    case R.id.nav_LossReceipt:
                        Intent intentloaa = new Intent(MainActivity.this, LossReceipt.class);
                        // intentloaa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentloaa);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_shiftreport:

                        Intent intentShift = new Intent(MainActivity.this, ShiftReportctivity.class);
                        startActivity(intentShift);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_carInActivityTwo:
                        Intent intentIn = new Intent(MainActivity.this, TwoWheelerList.class);
                        startActivity(intentIn);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_carOutActivityFour:
                        Intent intentFourlist = new Intent(MainActivity.this, FourWheelerList.class);
                        startActivity(intentFourlist);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;

                    case R.id.nav_sub:
                        Intent intentnav_sub = new Intent(MainActivity.this, Subscription.class);
                        startActivity(intentnav_sub);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;

                    case R.id.nav_renew:
                        Intent intentnav_renew = new Intent(MainActivity.this, RenewCard.class);
                        startActivity(intentnav_renew);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;

                    case R.id.nav_detail:
                        Intent intentnav_detail = new Intent(MainActivity.this, Subscriptionctivity.class);
                        startActivity(intentnav_detail);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_download:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Are you sure you want to Update?").setIcon(R.drawable.oparklogonew).setTitle("Opark")
                                .setCancelable(false)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // startService(new Intent(MainActivity.this, MyService.class));
                                        new DownloadNewVersion().execute();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert1 = builder1.create();
                        alert1.show();


                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                }
                return true;
            }

        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,
                R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();


        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        etMobileNo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etMobileNo.getText().toString().length() == 10)     //size as per your requirement
                {
                    etMobileNo.requestFocus();
                    etMobileNo.setSelection(etMobileNo.getText().length());
                    AppConstants.hideKeyboard(MainActivity.this, etMobileNo);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        etStateCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etStateCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    etCityCode.requestFocus();
                    etStateCode.setSelection(etStateCode.getText().length());

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        etCityCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etCityCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    etVehicleCode.requestFocus();
                    etCityCode.setSelection(etCityCode.getText().length());

                }
                if (etCityCode.getText().toString().length() == 0)     //size as per your requirement
                {
                    etCityCode.setSelection(etCityCode.getText().length());
                    etStateCode.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        etVehicleCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etVehicleCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    etVehicleCode.setSelection(etVehicleCode.getText().length());
                    etVehicleNumber.requestFocus();

                }
                if (etVehicleCode.getText().toString().length() == 0)     //size as per your requirement
                {
                    etVehicleCode.setSelection(etVehicleCode.getText().length());
                    etCityCode.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        etVehicleNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etVehicleNumber.getText().toString().length() == 4)     //size as per your requirement
                {
                    etVehicleNumber.setSelection(etVehicleNumber.getText().length());
                    AppConstants.hideKeyboard(MainActivity.this, etVehicleNumber);

                }
                if (etVehicleNumber.getText().toString().length() == 0)     //size as per your requirement
                {
                    etVehicleNumber.setSelection(etVehicleNumber.getText().length());
                    etVehicleCode.requestFocus();
                    //AppConstants.hideKeyboard(MainActivity.this, etVehicleNumber);

                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        otpVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpVehicleNumber.getText().toString().length() == 4) {
                    otpVehicleNumber.setSelection(etVehicleNumber.getText().length());
                    AppConstants.hideKeyboard(MainActivity.this, otpVehicleNumber);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        vipVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (vipVehicleNumber.getText().toString().length() == 10)     //size as per your requirement
                {
                    vipVehicleNumber.setSelection(vipVehicleNumber.getText().length());
                    vipVehicleNumber.requestFocus();
                    AppConstants.hideKeyboard(MainActivity.this, etVehicleNumber);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        normalUser.setChecked(true);
        btnScanQRCode.setVisibility(View.VISIBLE);
        userType = "Normal";
        if (userType.equals("Normal")) {
            btnScanQRCode.setVisibility(View.VISIBLE);
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.normalUser) {
                    normalUser.setChecked(true);
                    userType = "Normal";
                    vipVehicleNumber.setVisibility(View.GONE);
                    otpVehicleNumber.setVisibility(View.GONE);
                    vipinfo.setVisibility(View.GONE);
                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
                    normalUserVehicle.setVisibility(View.VISIBLE);
                    etMobileNo.setVisibility(View.VISIBLE);
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckout.setVisibility(View.VISIBLE);
                    btnScanQRCode.setVisibility(View.VISIBLE);

                }

                if (checkedId == R.id.vipUser) {
                    vipUser.setChecked(true);
                    userType = "VIP";
                    vipVehicleNumber.setVisibility(View.GONE);
                    otpVehicleNumber.setVisibility(View.GONE);
                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
                    normalUserVehicle.setVisibility(View.VISIBLE);
                    vipinfo.setVisibility(View.VISIBLE);
                    etMobileNo.setVisibility(View.VISIBLE);
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckout.setVisibility(View.VISIBLE);
                    btnScanQRCode.setVisibility(View.VISIBLE);

//                    vipVehicleNumber.setVisibility(View.VISIBLE);
//                    normalUserVehicle.setVisibility(View.GONE);
//                    otpVehicleNumber.setVisibility(View.GONE);
//                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
//                    vipVehicleNumber.setHint("Enter Vehicle no.");
//                    vipVehicleNumber.setText("");
//                    etMobileNo.setVisibility(View.GONE);
//                    btnCheckIn.setVisibility(View.VISIBLE);
//                    btnCheckout.setVisibility(View.VISIBLE);
//                    btnScanQRCode.setVisibility(View.VISIBLE);


                }

                if (checkedId == R.id.onlineUser) {
                    onlineUser.setChecked(true);
                    normalUserVehicle.setVisibility(View.GONE);
                    etMobileNo.setVisibility(View.GONE);
                    vipVehicleNumber.setVisibility(View.GONE);
                    vipinfo.setVisibility(View.GONE);
                    otpVehicleNumber.setVisibility(View.VISIBLE);
                    vipVehicleNumber.setText("");
                    otpVehicleNumber.setHint("Enter OTP");
                    userType = "Online";
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckout.setVisibility(View.VISIBLE);
                    btnScanQRCodeCheckIn.setVisibility(View.VISIBLE);
                    btnScanQRCode.setVisibility(View.GONE);
                }
            }
        });


    }

    public void numericType() {
        filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile("[1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                        return "";
                    }
                }

                return null;
            }
        };


    }

    public void type() {
        filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                        return "";
                    }
                }

                return null;
            }
        };
        vipVehicleNumber.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        AppConstants.hideKeyboard(MainActivity.this, v);

        if (v == btnCheckIn) {
            validate();
        } else if (v == btnCheckout) {
            validate1();
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        } else if (v == btnScanQRCode) {
            moveToQrScan();
        } else if (v == btnScanQRCodeCheckIn) {
            moveToQrScan();
        }*/
/*else {
            moveToQrScan();
        }*//*

    }

    private void moveToQrScan() {
        try {
            Intent qrintent = new Intent(MainActivity.this, QRScanPrint.class);
            qrintent.putExtra("userType", userType);
            qrintent.putExtra("Acticityclass", "MainActicity");
            qrintent.putExtra("Wheeler", parkingType12);


            startActivity(qrintent);
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    private void validate() {

        mobileNumber = etMobileNo.getText().toString();
        StateCode = etStateCode.getText().toString();
        CityCode = etCityCode.getText().toString();
        VehicleCode = etVehicleCode.getText().toString();
        vehicleNumber = etVehicleNumber.getText().toString();
        vipvehicleNumber = vipVehicleNumber.getText().toString();
        vipInfo = vipinfo.getText().toString();
        //  otp_VehicleNumber = otpVehicleNumber.getText().toString();

        if (userType.equals("VIP")) {
            if (AppConstants.isBlank(vipvehicleNumber)) {
                chechIn();
//                vipVehicleNumber.requestFocus();
//                Toast.makeText(MainActivity.this, "Enter Vehicle number required", Toast.LENGTH_SHORT).show();
            } else {
                apiCall();
            }
        } else {
            if (userType.equals("Online")) {
                //apiCall();
                if (AppConstants.isBlank(otpVehicleNumber.getText().toString())) {
                    otpVehicleNumber.requestFocus();
                    Toast.makeText(MainActivity.this, "Enter OTP required", Toast.LENGTH_SHORT).show();
                } else {
                    apiCall();
                }
            } else {
                chechIn();
            }
        }
    }

    public void chechIn() {
        if (AppConstants.isBlank(vehicleNumber)) {
            showAlert("Valid vehicle number required");
            etVehicleNumber.requestFocus();
        } else if (etVehicleNumber.getText().toString().length() < 4) {
            AppConstants.showToast(MainActivity.this, "Vehicle number should be 4 digit");
            etVehicleNumber.requestFocus();
        } else {
            if (!AppConstants.isBlank(mobileNumber) || !AppConstants.isBlank(StateCode) || !AppConstants.isBlank(CityCode) || !AppConstants.isBlank(VehicleCode)) {
                if (etMobileNo.getText().toString().length() > 0) {
                    if (etMobileNo.getText().toString().length() < 10) {
                        showAlert("Mobile number should be 10 digits");
                        etMobileNo.requestFocus();
                    } else {
                        apiCall();
                    }
                } else {
                    if (etStateCode.getText().toString().length() > 0) {
                        if (etStateCode.getText().toString().length() < 2) {
                            showAlert("State Code should be 2 letters");
                            etStateCode.requestFocus();
                        } else {
                            apiCall();
                        }
                    } else {
                        if (etCityCode.getText().toString().length() > 0) {
                            if (etCityCode.getText().toString().length() < 2) {
                                showAlert("CityCode Code should be 2 digits");
                                etCityCode.requestFocus();
                            } else {
                                apiCall();
                            }
                        } else {
                            if (etVehicleCode.getText().toString().length() > 0) {
                                if (etVehicleCode.getText().toString().length() < 2) {
                                    showAlert("Vehicle Code should be 2 digits");
                                    etVehicleCode.requestFocus();
                                } else {
                                    apiCall();
                                }
                            } else {
                                apiCall();
                            }
                        }
                    }
                }
            } else {
                apiCall();
            }
        }
    }

    private void validate1() {

        mobileNumber = etMobileNo.getText().toString();
        StateCode = etStateCode.getText().toString();
        CityCode = etCityCode.getText().toString();
        VehicleCode = etVehicleCode.getText().toString();
        vehicleNumber = etVehicleNumber.getText().toString();
        vipvehicleNumber = vipVehicleNumber.getText().toString();
        vipInfo = vipinfo.getText().toString();
        // otp_VehicleNumber = otpVehicleNumber.getText().toString();

        if (userType.equals("VIP")) {
            if (AppConstants.isBlank(vipvehicleNumber)) {
                chechOut();
//                vipVehicleNumber.requestFocus();
//                Toast.makeText(MainActivity.this, "Enter Vehicle number required", Toast.LENGTH_SHORT).show();
            } else {
                apiCallCheckOut();
            }
        } else {
            if (userType.equals("Online")) {
                //apiCallCheckOut();
                if (AppConstants.isBlank(otpVehicleNumber.getText().toString())) {
                    otpVehicleNumber.requestFocus();
                    Toast.makeText(MainActivity.this, "Enter OTP required", Toast.LENGTH_SHORT).show();
                } else {
                    apiCallCheckOut();
                }
            } else {
                chechOut();
            }
        }
    }

    public void chechOut() {
        if (AppConstants.isBlank(vehicleNumber)) {
            showAlert("Valid vehicle number required");
            etVehicleNumber.requestFocus();
        } else if (etVehicleNumber.getText().toString().length() < 4) {
            AppConstants.showToast(MainActivity.this, "Vehicle number should be 4 digit");
            etVehicleNumber.requestFocus();
        } else {
            if (!AppConstants.isBlank(mobileNumber) || !AppConstants.isBlank(StateCode) || !AppConstants.isBlank(CityCode) || !AppConstants.isBlank(VehicleCode)) {
                if (etMobileNo.getText().toString().length() > 0) {
                    if (etMobileNo.getText().toString().length() < 10) {
                        showAlert("Mobile number should be 10 digits");
                        etMobileNo.requestFocus();
                    } else {
                        apiCallCheckOut();
                    }
                } else {
                    if (etStateCode.getText().toString().length() > 0) {
                        if (etStateCode.getText().toString().length() < 2) {
                            showAlert("State Code should be 2 letters");
                            etStateCode.requestFocus();
                        } else {
                            apiCallCheckOut();
                        }
                    } else {
                        if (etCityCode.getText().toString().length() > 0) {
                            if (etCityCode.getText().toString().length() < 2) {
                                showAlert("CityCode Code should be 2 digits");
                                etCityCode.requestFocus();
                            } else {
                                apiCallCheckOut();
                            }
                        } else {
                            if (etVehicleCode.getText().toString().length() > 0) {
                                if (etVehicleCode.getText().toString().length() < 2) {
                                    showAlert("Vehicle Code should be 2 digits");
                                    etVehicleCode.requestFocus();
                                } else {
                                    apiCallCheckOut();
                                }
                            } else {
                                apiCallCheckOut();
                            }
                        }
                    }
                }
            } else {
                apiCallCheckOut();
            }
        }
    }

    private void apiCall() {
        String vehicleNo;
        if (userType.equals("VIP")) {
            vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            // vehicleNo = vipvehicleNumber;
        } else {
            if (userType.equals("Online")) {
                // vehicleNo = vipvehicleNumber;
                vehicleNo = otpVehicleNumber.getText().toString();
                // vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            } else {
                vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            }
            // vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        }

        if (AppConstants.isInternetAvailable(MainActivity.this)) {
            if (userType.equals("VIP")) {
                if (!vipinfo.getText().toString().equals("")) {
                    //vehicleCheckIn(parkingId, parkingType12, vehicleNo, agentId, userType, mobileNumber, tagID, vipInfo);
                    */
/* String urlData = AppConstants.BASEURL + "parking/checkin?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo + "&vipInfo=" + vipInfo;
*//*

                    String url = AppConstants.BASEURL + "parking/checkin";
                    Map<String, String> parameterData = new HashMap<>();
                    parameterData.put(("parkingId"), parkingId);
                    parameterData.put(("parkingType"), parkingType12);
                    parameterData.put(("vehicleNo"), vehicleNo);
                    parameterData.put(("agentId"), agentId);
                    parameterData.put(("mode"), userType);
                    parameterData.put(("userdata"), mobileNumber);
                    parameterData.put(("cardNo"), tagID);
                    parameterData.put(("vipInfo"), vipInfo);

                    vehicleCheckInServices(url, parameterData);

                } else {
                    Toast.makeText(MainActivity.this, "Enter VIP Info", Toast.LENGTH_LONG).show();
                    vipinfo.requestFocus();
                }
            } else {

                String url = AppConstants.BASEURL + "parking/checkin";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), parkingType12);
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("mode"), userType);
                parameterData.put(("userdata"), mobileNumber);
                parameterData.put(("cardNo"), tagID);
                parameterData.put(("vipInfo"), vipInfo);

                vehicleCheckInServices(url, parameterData);

                //   vehicleCheckIn(parkingId, parkingType12, vehicleNo, agentId, userType, mobileNumber, tagID, vipInfo);
            }
        } else {
            Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    private void apiCallCheckOut() {

        String vehicleNo;
        if (userType.equals("VIP")) {
            vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            // vehicleNo = vipvehicleNumber;
        } else {
            if (userType.equals("Online")) {
                vehicleNo = otpVehicleNumber.getText().toString();
            } else {
                vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            }
            //vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        }

        if (AppConstants.isInternetAvailable(MainActivity.this)) {

            // String vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            vehicleCheckOut(parkingId, parkingType12, vehicleNo, agentId, userType, mobileNumber, tagID, vipInfo);
        } else {
            Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    private void showAlert(String message) {
        AppConstants.showToast(MainActivity.this, message);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
    }

    public void availableSlotSevice(final String parkingId, final String parkingType, final String agentId) {

        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        */
/*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*//*

        */
/*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=2&parkingType=2Wheeler&agentId=9*//*


        String urlData = AppConstants.BASEURL + "parking/inventory?parkingId=" + parkingId + "&parkingType=" + parkingType + "&agentId=" + agentId;

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

                        JSONObject availableresponce = response.getJSONObject("data");

                        // JSONObject loginresponce = response.getJSONObject("data");

                        AvailableSlotModel availableSlotModel = new AvailableSlotModel();
                        availableSlotModel.setParkingId(availableresponce.getString("parkingId"));
                        availableSlotModel.setParkingType(availableresponce.getString("parkingType"));
                        availableSlotModel.setBookedSlots(availableresponce.getString("bookedSlots"));
                        availableSlotModel.setAvailableSlots(availableresponce.getString("availableSlots"));

                       */
/* if (parkingType.equals("2W")){
                            tvAvailableSpots.setText(availableresponce.getString("availableSlots"));
                            tvTotalSpots.setText(availableresponce.getString("bookedSlots"));
                        }*//*

                        tvAvailableSpots.setText(availableresponce.getString("availableSlots"));
                        tvTotalSpots.setText(availableresponce.getString("bookedSlots"));


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/inventory?parkingId=");
                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/inventory?parkingId=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(error.toString(), "parking/inventory?parkingId=");
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void logOut(String AgentId, String parkingId) {
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
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

                        getSharedPreferences("opark", Context.MODE_PRIVATE).edit().remove("userContactNo").
                                remove("agentId").remove("userRole").remove("userName").remove("vendorId").remove("parkingName").remove("userId").remove("getParkingID").
                                remove("vendorName").remove("getparkingType").remove("parkingId").remove("vendorName").remove("VehicleType1").remove("tagID").commit();

                        Intent logout_intent1 = new Intent(MainActivity.this, Login.class);
                        logout_intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout_intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                    pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/logout?userId=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    // Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
//                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                sendError(error.toString(), "user/logout?userId=");
                Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                // hide the progress dialog
                //      pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public void vehicleCheckInServices(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectCheckIn(jsonObject);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    sendError(volleyError.toString(), "parking/pass_add");
                    Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(), "error==>  " + volleyError.toString(), Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            sendError(e.toString(), "parking/pass_add");
            pDialog.dismiss();
        }
    }

    private void processJsonObjectCheckIn(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {
                    JSONObject checkINresponce = response.getJSONObject("data");
                    CheckInModel checkInModel = new CheckInModel();

                    checkInModel.setReceiptHeading(checkINresponce.getString("receiptHeading"));
                    checkInModel.setParkingAddress(checkINresponce.getString("parkingAddress"));
                    checkInModel.setUserContactNo(checkINresponce.getString("userContactNo"));
                    checkInModel.setCheckInDate(checkINresponce.getString("checkInDate"));
                    checkInModel.setAgentId(checkINresponce.getString("agentId"));
                    checkInModel.setAvailableSlots(checkINresponce.getString("availableSlots"));
                    checkInModel.setParkingId(checkINresponce.getString("parkingId"));
                    checkInModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                    checkInModel.setParkingRate(checkINresponce.getString("parkingRate"));
                    checkInModel.setAdditionalParkingRate(checkINresponce.getString("additionalParkingRate"));
                    checkInModel.setMode(checkINresponce.getString("mode"));
                    checkInModel.setReceiptStaticText(checkINresponce.getString("receiptStaticText"));
                    checkInModel.setReceiptEmail(checkINresponce.getString("receiptEmail"));
                    checkInModel.setReceiptMobile(checkINresponce.getString("receiptMobile"));
                    checkInModel.setReceiptWebsite(checkINresponce.getString("receiptWebsite"));
                    checkInModel.setOnlineUserText(checkINresponce.getString("onlineUserText"));
                    //checkInModel.setBarcode(checkINresponce.getString("barcode"));
                    checkInModel.setResponseType(checkINresponce.getString("responseType"));
                    checkInModel.setParkingType(checkINresponce.getString("parkingType"));
                    checkInModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                    checkInModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                    checkInModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                    checkInModel.setQrCode(checkINresponce.getString("barCode"));
                    checkInModel.setAgentName(checkINresponce.getString("agentName"));
                    checkInModel.setCardNo(checkINresponce.getString("cardNo"));
                    checkInModel.setLastLine(checkINresponce.getString("lastLine"));

                    //  Toast.makeText(MainActivity.this, "Parking Done!" + message, Toast.LENGTH_LONG).show();


                    getReceiptHeading = checkInModel.getReceiptHeading();
                    getParkingAddress = checkInModel.getParkingAddress();
                    getCheckInDate = checkInModel.getCheckInDate();
                    getVehicleNo = checkInModel.getVehicleNo();
                    getParkingRate = checkInModel.getParkingRate();
                    getAdditionalParkingRate = checkInModel.getAdditionalParkingRate();
                    getQrCode = checkInModel.getQrCode();
                    getAgentName = checkInModel.getAgentName();
                    getReceiptMobile = checkInModel.getReceiptMobile();
                    getPoweredBy = checkInModel.getPoweredBy();
                    getCompanyWebsite = checkInModel.getCompanyWebsite();
                    getReceiptNo = checkInModel.getReceiptNo();
                    ReceiptStaticText = checkInModel.getReceiptStaticText();
                    barCode = checkInModel.getQrCode();
                    onlineUserText = checkInModel.getOnlineUserText();
                    lastLine = checkInModel.getLastLine();
                    String cardNo = checkInModel.getCardNo();
                    String mobile = checkInModel.getUserContactNo();

                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("parkingPrint", "checkInModel");
                    editor.apply();
                    editor.commit();


                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("mobile", mobile);


                    ed.apply();
                    ed.commit();

                    if (cardNo.equals("")) {
                        printReceipt();
                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        otpVehicleNumber.setText("");
                        etMobileNo.setText("");
                        vipinfo.setText("");
                        tagID = "";
                        availableSlotSevice(parkingId, parkingType12, agentId);

                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    // pDialog.dismiss();

                } else {
                    if (message.equals("Card already Used for Checkin. Checkout first and then use again !")) {
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        otpVehicleNumber.setText("");
                        vipinfo.setText("");
                        tagID = "";
                        availableSlotSevice(parkingId, parkingType12, agentId);
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        //Vehicle already parked !
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        otpVehicleNumber.setText("");
                        vipinfo.setText("");
                        tagID = "";
                        etVehicleNumber.requestFocus();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (NullPointerException e) {
                sendError(e.toString(), "parking/checkin?parkingId=");
                Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                sendError(e.toString(), "parking/checkin?parkingId=");
                Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
    }

    public void vehicleCheckIn(final String parkingId, String parkingType, String vehicleNo, final String agentId, String mode, String userdata, String cardNo, String vipInfo) {
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        */
/*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*//*


        */
/*http://staggingapi.opark.in/index.php/v1/parking/checkin?parkingId=9&parkingType=4Wheeler&vehicleNo=1258&agentId=&mode=Normal&userdata=&cardNo=*//*


        String urlData = AppConstants.BASEURL + "parking/checkin?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo + "&vipInfo=" + vipInfo;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                try {
                    System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {

                        JSONObject checkINresponce = response.getJSONObject("data");
                        CheckInModel checkInModel = new CheckInModel();

                        checkInModel.setReceiptHeading(checkINresponce.getString("receiptHeading"));
                        checkInModel.setParkingAddress(checkINresponce.getString("parkingAddress"));
                        checkInModel.setUserContactNo(checkINresponce.getString("userContactNo"));
                        checkInModel.setCheckInDate(checkINresponce.getString("checkInDate"));
                        checkInModel.setAgentId(checkINresponce.getString("agentId"));
                        checkInModel.setAvailableSlots(checkINresponce.getString("availableSlots"));
                        checkInModel.setParkingId(checkINresponce.getString("parkingId"));
                        checkInModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        checkInModel.setParkingRate(checkINresponce.getString("parkingRate"));
                        checkInModel.setAdditionalParkingRate(checkINresponce.getString("additionalParkingRate"));
                        checkInModel.setMode(checkINresponce.getString("mode"));
                        checkInModel.setReceiptStaticText(checkINresponce.getString("receiptStaticText"));
                        checkInModel.setReceiptEmail(checkINresponce.getString("receiptEmail"));
                        checkInModel.setReceiptMobile(checkINresponce.getString("receiptMobile"));
                        checkInModel.setReceiptWebsite(checkINresponce.getString("receiptWebsite"));
                        checkInModel.setOnlineUserText(checkINresponce.getString("onlineUserText"));
                        //checkInModel.setBarcode(checkINresponce.getString("barcode"));
                        checkInModel.setResponseType(checkINresponce.getString("responseType"));
                        checkInModel.setParkingType(checkINresponce.getString("parkingType"));
                        checkInModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                        checkInModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                        checkInModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                        checkInModel.setQrCode(checkINresponce.getString("barCode"));
                        checkInModel.setAgentName(checkINresponce.getString("agentName"));
                        checkInModel.setCardNo(checkINresponce.getString("cardNo"));
                        checkInModel.setLastLine(checkINresponce.getString("lastLine"));

                        //  Toast.makeText(MainActivity.this, "Parking Done!" + message, Toast.LENGTH_LONG).show();


                        getReceiptHeading = checkInModel.getReceiptHeading();
                        getParkingAddress = checkInModel.getParkingAddress();
                        getCheckInDate = checkInModel.getCheckInDate();
                        getVehicleNo = checkInModel.getVehicleNo();
                        getParkingRate = checkInModel.getParkingRate();
                        getAdditionalParkingRate = checkInModel.getAdditionalParkingRate();
                        getQrCode = checkInModel.getQrCode();
                        getAgentName = checkInModel.getAgentName();
                        getReceiptMobile = checkInModel.getReceiptMobile();
                        getPoweredBy = checkInModel.getPoweredBy();
                        getCompanyWebsite = checkInModel.getCompanyWebsite();
                        getReceiptNo = checkInModel.getReceiptNo();
                        ReceiptStaticText = checkInModel.getReceiptStaticText();
                        barCode = checkInModel.getQrCode();
                        onlineUserText = checkInModel.getOnlineUserText();
                        lastLine = checkInModel.getLastLine();
                        String cardNo = checkInModel.getCardNo();
                        String mobile = checkInModel.getUserContactNo();

                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("parkingPrint", "checkInModel");
                        editor.apply();
                        editor.commit();


                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("mobile", mobile);


                        ed.apply();
                        ed.commit();

                        if (cardNo.equals("")) {
                            printReceipt();
                        } else {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            otpVehicleNumber.setText("");
                            etMobileNo.setText("");
                            vipinfo.setText("");
                            tagID = "";
                            availableSlotSevice(parkingId, parkingType12, agentId);

                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();


                    } else {
                        if (message.equals("Card already Used for Checkin. Checkout first and then use again !")) {
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            etMobileNo.setText("");
                            otpVehicleNumber.setText("");
                            vipinfo.setText("");
                            tagID = "";
                            availableSlotSevice(parkingId, parkingType12, agentId);
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            //Vehicle already parked !
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            etMobileNo.setText("");
                            otpVehicleNumber.setText("");
                            vipinfo.setText("");
                            tagID = "";
                            etVehicleNumber.requestFocus();
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.d("JSONException", e + "");

                    //Send Error in Server
                    sendError(e.toString(), "parking/checkin?parkingId=");

                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();

                    //Send Error in Server
                    sendError(e.toString(), "parking/checkin?parkingId=");

                    Log.d("NullPointerException", e + "");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());

                Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();

                //Send Error in Server
                sendError(error.toString(), "parking/checkin?parkingId=");
                // hide the progress dialog
                Log.d("VolleyLog", error + "");
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void vehicleCheckOut(final String parkingId, String parkingType, String vehicleNo, final String agentId, String mode, String userdata, String cardNo, String vipInfo) {
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
         */
/*http://staggingapi.opark.in/index.php/v1/parking/checkout?parkingId=1&parkingType=4Wheeler&vehicleNo=6565&agentId=3&mode=Normal&userdata=9993289838*//*


        String urlData = AppConstants.BASEURL + "parking/checkout?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo + "&vipInfo=" + vipInfo;

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

                        //  availableSlotSevice(parkingId, parkingType1234);

                        JSONObject checkINresponce = response.getJSONObject("data");

                        CheckOutModel checkOutModel = new CheckOutModel();

                        String rh = checkINresponce.getString("receiptHeading");

                        checkOutModel.setReceiptHeading(checkINresponce.getString("receiptHeading"));
                        checkOutModel.setParkingAddress(checkINresponce.getString("parkingAddress"));
                        checkOutModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        checkOutModel.setCheckInDate(checkINresponce.getString("checkInDate"));
                        checkOutModel.setCheckOutDate(checkINresponce.getString("checkOutDate"));
                        checkOutModel.setCheckInTime(checkINresponce.getString("checkInTime"));
                        checkOutModel.setCheckOutTime(checkINresponce.getString("checkOutTime"));
                        checkOutModel.setDuration(checkINresponce.getString("duration"));
                        checkOutModel.setDurationUnit(checkINresponce.getString("durationUnit"));
                        checkOutModel.setGrandTotal(checkINresponce.getString("grandTotal"));
                        checkOutModel.setCurrencySymbol(checkINresponce.getString("currencySymbol"));
                        checkOutModel.setUserContactNo(checkINresponce.getString("userContactNo"));
                        checkOutModel.setAgentId(checkINresponce.getString("agentId"));
                        checkOutModel.setAvailableSlots(checkINresponce.getString("availableSlots"));
                        checkOutModel.setParkingId(checkINresponce.getString("parkingId"));
                        checkOutModel.setParkingRate(checkINresponce.getString("parkingRate"));
                        checkOutModel.setTotal(checkINresponce.getString("total"));
                        checkOutModel.setMode(checkINresponce.getString("mode"));
                        checkOutModel.setOnlineUserText(checkINresponce.getString("onlineUserText"));
                        //  checkOutModel.setBarcode(checkINresponce.getString("barCode"));
                        checkOutModel.setResponseType(checkINresponce.getString("responseType"));
                        checkOutModel.setMinimumAmount(checkINresponce.getString("minimumAmount"));
                        checkOutModel.setParkingType(checkINresponce.getString("parkingType"));
                        checkOutModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                        checkOutModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                        checkOutModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                        checkOutModel.setCardNo(checkINresponce.getString("cardNo"));
                        checkOutModel.setPrintReceipt(checkINresponce.getString("printReceipt"));


                        String heading = checkOutModel.getReceiptHeading();
                        String cardNo = checkINresponce.getString("cardNo");

                        getReceiptHeadingOut = checkOutModel.getReceiptHeading();
                        getParkingAddressOut = checkOutModel.getParkingAddress();
                        getVehicleNoOut = checkOutModel.getVehicleNo();
                        getCheckInDateOut = checkOutModel.getCheckInDate();
                        getCheckOutDateOut = checkOutModel.getCheckOutDate();
                        getduration = checkOutModel.getDuration();
                        getDurationUnit = checkOutModel.getDurationUnit();
                        getCurrencySymbol = checkOutModel.getCurrencySymbol();
                        getGrandTotal = checkOutModel.getGrandTotal();
                        getPoweredByOut = checkOutModel.getPoweredBy();
                        getReceiptNoOut = checkOutModel.getReceiptNo();
                        getCompanyWebsiteOut = checkOutModel.getCompanyWebsite();
                        getprintReceipt = checkOutModel.getPrintReceipt();
                        onlineUserText = checkOutModel.getOnlineUserText();

                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();

                        editor.putString("parkingPrint", "checkOUTModel");
                        editor.apply();
                        editor.commit();


                        if (cardNo.equals("")) {
                            printReceipt();
                        } else {

                            if (getprintReceipt.equals("yes")) {
                                printReceipt();
                            } else {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                etStateCode.setText("");
                                etCityCode.setText("");
                                etVehicleCode.setText("");
                                etVehicleNumber.setText("");
                                vipVehicleNumber.setText("");
                                etMobileNo.setText("");
                                tagID = "";
                                availableSlotSevice(parkingId, parkingType12, agentId);
                            }

                            //cleareText();
                        }

                        //printReceipt();
                        pDialog.dismiss();


                    } else {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        tagID = "";
                        //cleareText();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkout?parkingId=");
                    Log.d("NullPointerException", e + "");
                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    cleareText();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    cleareText();
                    sendError(e.toString(), "parking/checkout?parkingId=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    Log.d("JSONException", e + "");
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                Log.d("Response error", error + "");
                sendError(error.toString(), "parking/checkout?parkingId=");
                // hide the progress dialog
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void cleareText() {
        // etStateCode.setText("");
        // etCityCode.setText("");
        etVehicleCode.setText("");
        etVehicleNumber.setText("");
        vipVehicleNumber.setText("");
        etMobileNo.setText("");
        otpVehicleNumber.setText("");
        vipinfo.setText("");
    }

    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(MainActivity.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(MainActivity.this, printer);

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

    public void printNormal(Context newEntryActivity, Printer printer) {

        */
/*sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        String parkingPrint = sharedpref.getString("parkingPrint", "");*//*


        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);

        if (storeAllValues.getString("parkingPrint", "").equals("checkInModel")) {

            CheckInModel checkInModel = new CheckInModel();
            //checkInModel.getReceiptHeading();

            printer.printText("      " + getReceiptHeading, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(getParkingAddress,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.ITALIC, Printer.Gravity.LEFT);

            printer.printText("---------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("     " + getCheckInDate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.RIGHT);

            printer.printText("VNO. -  " + getVehicleNo,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText(getParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText(getAdditionalParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText(onlineUserText, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.LEFT);

            printer.printQrCode(barCode, 300, Printer.Gravity.CENTER);

            // printer.printBarCode(barCode,1,100,1);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("ATTENDANT - " + getAgentName + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

           */
/* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
*//*

            printer.printText("Helpline No:" + getReceiptMobile, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText("          Powered By:-" + "\n" + getPoweredBy, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText(getCompanyWebsite, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText("Receipt No. -  " + getReceiptNo, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.CENTER);

            printer.printText(lastLine, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);


            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            availableSlotSevice(parkingId, parkingType12, agentId);
            cleareText();
        }
        if (storeAllValues.getString("parkingPrint", "").equals("checkOUTModel")) {
            // Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();

            printer.printText(getReceiptHeadingOut, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(getParkingAddressOut,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText("--------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(onlineUserText, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.LEFT);
//
            printer.printText("VNO.-" + getVehicleNoOut,
                    Printer.FontFamily.SONG, Printer.FontSize.LARGE,
                    Printer.FontStyle.BOLD, Printer.Gravity.RIGHT);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("In-  " + getCheckInDateOut,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
            printer.printText("Out- " + getCheckOutDateOut,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText(getduration + " " + getDurationUnit + " " + getCurrencySymbol + " " + String.valueOf(getGrandTotal),
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);
            printer.printText("Powered By:- " + getPoweredByOut,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getCompanyWebsiteOut, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("Receipt No. -  " + getReceiptNoOut,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            availableSlotSevice(parkingId, parkingType12, agentId);
            cleareText();

        }
        if (storeAllValues.getString("parkingPrint", "").equals(printError)) {
//        }
//        if (printError.equals("yes")) {
            printer.printText(receiptHeading, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);
            printer.printText(parkingAddress,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText("--------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(message1, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText(poweredBy,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            cleareText();
        }


    }

    private void initNFC() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    public void id(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("android.nfc.extra.ID");
        Appendable stringBuilder = new StringBuilder(byteArrayExtra.length * 2);
        Formatter formatter = new Formatter(stringBuilder);
        for (int length = byteArrayExtra.length - 1; length > -1; length--) {
            formatter.format("%02X", new Object[]{Byte.valueOf(byteArrayExtra[length])});
        }

        tagID = stringBuilder.toString();

        AppConstants.setString(MainActivity.this, AppConstants.TAGID, tagID);

        //  Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        if (tagID.equals("")) {
            Toast.makeText(this, "Card Not register", Toast.LENGTH_SHORT).show();
        } else {
            nfcDetailForINOut(tagID);
        }
        formatter.close();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        if (tag != null) {
            // Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_LONG).show();
            Ndef ndef = Ndef.get(tag);


        }
        if (intent.getByteArrayExtra("android.nfc.extra.ID") != null) {
            Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);

            id(intent);
        }
    }

    public void nfcDetailForINOut(String tagid) {
        // Toast.makeText(this, "tagid    " + tagid, Toast.LENGTH_LONG).show();

        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        */
/*http://staggingapi.opark.in/index.php/v1/parking/pass_detail?cardNo=4BHH5678*//*


        String urlData = AppConstants.BASEURL + "parking/pass_detail?cardNo=" + tagid;

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

                        JSONObject checkINresponce = response.getJSONObject("data");

                        SubscriptionModel subscriptionModel = new SubscriptionModel();

                        */
/*subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subscriptionModel.setAgentId(checkINresponce.getString("agentId"));*//*

                        subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subVehicleNo = subscriptionModel.getVehicleNo();

                        String[] totalsubVehicleNo = subVehicleNo.toString().split(",");

                        String StateCode = totalsubVehicleNo[0];
                        String CityCode = totalsubVehicleNo[1];
                        String VehicleCode = totalsubVehicleNo[2];
                        String vehicleNumber = totalsubVehicleNo[3];

                        etStateCode.setText(StateCode);
                        etCityCode.setText(CityCode);
                        etVehicleCode.setText(VehicleCode);
                        etVehicleNumber.setText(vehicleNumber);


                        String vehicleNo1 = StateCode + CityCode + VehicleCode + vehicleNumber;

                        // vehicleCheckIn(parkingId, parkingType1234, vehicleNo1, agentId, userType, mobileNumber, subscriptionModel.getCardNo());

                        //    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                        pDialog.dismiss();


                    } else {

                        printError = String.valueOf(response.get("printError"));
                        message1 = String.valueOf(response.get("message"));
                        JSONObject checkINresponce = response.getJSONObject("data");

                        if (message1.equals("Card Not Exist !")) {

                            Toast.makeText(MainActivity.this, message1, Toast.LENGTH_SHORT).show();

                        } else {
                            SubscriptionModel subscriptionModel = new SubscriptionModel();

                            SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = storeAllValues.edit();
                            editor.putString("parkingPrint", printError);
                            editor.apply();
                            editor.commit();

                            subscriptionModel.setReceiptHeading(checkINresponce.getString("receiptHeading"));
                            subscriptionModel.setParkingAddress(checkINresponce.getString("parkingAddress"));
                            subscriptionModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                            subscriptionModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                            subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                            tagID = "";

                            receiptHeading = subscriptionModel.getReceiptHeading();
                            parkingAddress = subscriptionModel.getParkingAddress();
                            companyWebsite = subscriptionModel.getCompanyWebsite();
                            poweredBy = subscriptionModel.getPoweredBy();
                            printReceipt();
                        }


                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                sendError(error.toString(), "parking/pass_detail?cardNo=");
                // hide the progress dialog
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    class DownloadNewVersion extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setCancelable(false);

            dialog.setMessage("Downloading...");

            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgress(progress[0]);
            String msg = "";
            if (progress[0] > 99) {

                msg = "Finishing... ";

            } else {

                msg = "Downloading... " + progress[0] + "%";
            }
            dialog.setMessage(msg);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            dialog.dismiss();

            if (result) {

                Toast.makeText(getApplicationContext(), "Update Done",
                        Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getApplicationContext(), "No updates availabel",
                        Toast.LENGTH_SHORT).show();

            }

        }


        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;

            try {
                //https://opark.in/O_par_aPi/stg/apk/apkname.apk
                // https://opark.in/O_par_aPi/stg/apk/app-debug.apk

                //URL url = new URL("http://api.opark.in/apk/app-debug.apk"); //stg
                URL url = new URL("https://opark.in/O_par_aPi/live/apk/app-debug.apk"); //Live

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory() + "/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file, "app-debug.apk");

                if (outputFile.exists()) {
                    outputFile.delete();
                }


                InputStream is = c.getInputStream();

                int total_size = 1431692;//size of apk

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded = 0;

                FileOutputStream fos = new FileOutputStream(outputFile);

                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded += len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                // Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
            }
            return flag;


        }

    }

    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + "app-debug.apk")), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


//
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_BLUETOOTH) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.BLUETOOTH_ADMIN) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    break;
                }
            }
        } else if (requestCode == REQUEST_CODE_STORAGE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    break;
                }
            }
        } else if (requestCode == REQUEST_CODE_LOCATION) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                    break;
                }
            }
        }
        switch (requestCode) {
            case 100:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        moveToQrScan();
                        break;
                    }
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_CODE_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
        } else {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patmid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.Paytm:
                //  Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, TransationActivity.class);
                if (parkingType12.equals("4Wheeler")) {

                    intent.putExtra("parkingType", "4Wheeler");
                } else {
                    intent.putExtra("parkingType", "2Wheeler");

                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            default:
        }
        return super.onOptionsItemSelected(item);

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(MainActivity.this)) {
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
                Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
    }


}

*/
