package attender.oparkReceipt.booking;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import attender.oparkReceipt.MainActivity;
import attender.oparkCard.R;
import attender.oparkReceipt.TransationActivity;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.SubscriptionModel;
import attender.oparkReceipt.subscription.model.SubscriptionModelNew;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class
ThreeWheeler extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ThreeWheeler";
    private String android_id, vipInfo = "";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    String agentId, userRole, userName, getParkingName, userContactNo, vendorId,userTypeData, vendorName, parkingName, parkingType, parkingId, pType2, pType4, parkingVehicle, subVehicleNo, userType, barCode, printReceipt = "";
    private TextView tvAvailableSpots, textToolHeader;
    private TextView tvTotalSpots, attenderName, parking_Name;
    private String mobileNumber = "";
    private String vipinfo = "";
    private String vehicleNumber = "";
    private String vipvehicleNumber = "";
    private String StateCode = "";
    private String CityCode = "";
    private String VehicleCode = "";
    LinearLayout normalUserVehicle, vipUserVehicle;
    private Button btnCheckIn;
    private Button btnCheckout;
    private Button btnScanQRCode, btnScanQRCodeCheckIn;
    private RadioGroup radioGroup;
    private RadioButton normalUser, vipUser, onlineUser;
    private Toolbar toolBar;
    EditText etMobileNo, etStateCode, etCityCode, etVehicleCode, etVehicleNumber, vipVehicleNumber, otp_VehicleNumber, etvipInfo;
    String getReceiptHeading, getParkingAddress, ReceiptStaticText, lastLine, getCheckInDate, getVehicleNo, getParkingRate, getAdditionalParkingRate, getQrCode, getAgentName, getReceiptMobile, getPoweredBy, getCompanyWebsite, getReceiptNo;
    private ServiceManager mServiceManager = null;
    Printer printer;
    private NfcAdapter mNfcAdapter;
    String tagID = "";
    private int REQUEST_CODE_BLUETOOTH = 100;
    private int REQUEST_CODE_LOCATION = 101;
    private int REQUEST_CODE_STORAGE = 102;

    String getReceiptHeadingOut, getParkingAddressOut, getCheckInDateOut, getCheckOutDateOut, getVehicleNoOut, parkingType2,
            getPoweredByOut, getCompanyWebsiteOut, getReceiptNoOut, getduration, getDurationUnit, getCurrencySymbol, getGrandTotal, parkingType1234, parkingTypeTwo, parkingTypeFour;
    String[] pType = new String[3];
    String poweredBy = "", companyWebsite = "", parkingAddress = "", receiptHeading = "", printError = "", message1 = "";
    InputFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_wheeler);
        printer = WeiposImpl.as().openPrinter();

        findViews();
        initNFC();


    }

    public void url() {

        /*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*/
        /*http://staggingapi.opark.in/index.php/v1/user/inventory?parkingId=1&parkingType=2W*/
    }


    private void findViews() {

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
        parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingTypeTwo = sharedpref.getString("towWheeler", "");
        parkingTypeFour = sharedpref.getString("fourWheeler", "");
        getParkingName = sharedpref.getString("getParkingName", "");

        // parkingType1234 = sharedpref.getString("VehicleType1", "");


        pType = parkingType.toString().split(",");

        if (pType.length == 1) {
            parkingType1234 = sharedpref.getString("VehicleType1", "");
        }


        if (pType.length == 2) {
            pType2 = pType[0];
            pType4 = pType[1];
        }


        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        textToolHeader = (TextView) findViewById(R.id.toolbar_title);
        textToolHeader.setText("Three Wheeler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ThreeWheeler.this, MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        etMobileNo = (EditText) findViewById(R.id.etMobileNo1);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn1);
        btnCheckout = (Button) findViewById(R.id.btnCheckout1);
        btnScanQRCode = (Button) findViewById(R.id.btnScanQRCode1);
        btnScanQRCodeCheckIn = (Button) findViewById(R.id.btnScanQRCodeCheckIn1);
        attenderName = (TextView) findViewById(R.id.attenderName1);
        parking_Name = (TextView) findViewById(R.id.parking_Name1);

        normalUserVehicle = (LinearLayout) findViewById(R.id.normalUserVehicle1);
        vipUserVehicle = (LinearLayout) findViewById(R.id.vipUserVehicle1);

        tvTotalSpots = (TextView) findViewById(R.id.tvTotalSpots1);
        otp_VehicleNumber = (EditText) findViewById(R.id.otp_VehicleNumber1);
        etvipInfo = (EditText) findViewById(R.id.etvipInfo1);
        tvAvailableSpots = (TextView) findViewById(R.id.tvAvailableSpots1);

        normalUser = (RadioButton) findViewById(R.id.normalUserr1);
        vipUser = (RadioButton) findViewById(R.id.vipUser1);
        onlineUser = (RadioButton) findViewById(R.id.onlineUser1);
        etStateCode = (EditText) findViewById(R.id.etStateCode1);
        etCityCode = (EditText) findViewById(R.id.etCityCode1);
        etVehicleCode = (EditText) findViewById(R.id.etVehicleCode1);
        etVehicleNumber = (EditText) findViewById(R.id.etVehicleNumber1);
        vipVehicleNumber = (EditText) findViewById(R.id.vipVehicleNumber1);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        attenderName.setText(userName);
        parking_Name.setText(getParkingName);
        etVehicleNumber.requestFocus();
        etStateCode.setEnabled(false);
        etCityCode.setEnabled(false);
        etVehicleCode.setEnabled(false);
        etVehicleNumber.setEnabled(false);
        vipVehicleNumber.setEnabled(false);
        etMobileNo.setEnabled(false);

        setListener();
    }


    private void setListener() {

        btnCheckIn.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        btnScanQRCode.setOnClickListener(this);
        btnScanQRCodeCheckIn.setOnClickListener(this);


        etMobileNo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etMobileNo.getText().toString().length() == 10)     //size as per your requirement
                {
                    etMobileNo.requestFocus();
                    etMobileNo.setSelection(etMobileNo.getText().length());
                    AppConstants.hideKeyboard(ThreeWheeler.this, etMobileNo);
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
                    etStateCode.setSelection(etStateCode.getText().length());
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
        etCityCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etCityCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    etCityCode.setSelection(etCityCode.getText().length());
                    etVehicleCode.requestFocus();
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
                    AppConstants.hideKeyboard(ThreeWheeler.this, etVehicleNumber);
                }
                if (etVehicleNumber.getText().toString().length() == 0)     //size as per your requirement
                {
                    etVehicleCode.requestFocus();
                    // AppConstants.hideKeyboard(FourWheeler.this, etVehicleNumber);
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
        otp_VehicleNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_VehicleNumber.getText().toString().length() == 4)     //size as per your requirement
                {
                    otp_VehicleNumber.setSelection(otp_VehicleNumber.getText().length());
                    AppConstants.hideKeyboard(ThreeWheeler.this, otp_VehicleNumber);
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
        vipVehicleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vipVehicleNumber.getText().toString().length() == 10)     //size as per your requirement
                {
                    vipVehicleNumber.setSelection(vipVehicleNumber.getText().length());
                    AppConstants.hideKeyboard(ThreeWheeler.this, vipVehicleNumber);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        try {
            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
            userTypeData = sharedpref.getString("userType", "");

            if (userTypeData.equals("Normal")) {
                normalUser.setChecked(true);
                vipUser.setChecked(false);
                userType = "Normal";
                etvipInfo.setVisibility(View.GONE);
            } else {
                vipUser.setChecked(true);
                normalUser.setChecked(false);
                userType = "VIP";
                etvipInfo.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // userType = "Normal";
        userTypeData="Normal";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.normalUser) {
                    normalUser.setChecked(true);
                    userType = "Normal";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("userType", userType);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();
                    userTypeData = sharedpref.getString("userType", "");
                    Log.e(TAG, "id: userTypeData" + userTypeData);


                    vipVehicleNumber.setVisibility(View.GONE);
                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckout.setVisibility(View.VISIBLE);
                    btnScanQRCode.setVisibility(View.GONE);
                    normalUserVehicle.setVisibility(View.VISIBLE);
                    etMobileNo.setVisibility(View.VISIBLE);
                    otp_VehicleNumber.setVisibility(View.GONE);
                    etvipInfo.setVisibility(View.GONE);
                }

                if (checkedId == R.id.vipUser) {
                    vipUser.setChecked(true);
                    userType = "VIP";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("userType", userType);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();
                    userTypeData = sharedpref.getString("userType", "");
                    Log.e(TAG, "id: userTypeData" + userTypeData);


                    vipVehicleNumber.setVisibility(View.GONE);
                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckout.setVisibility(View.VISIBLE);
                    btnScanQRCode.setVisibility(View.GONE);
                    normalUserVehicle.setVisibility(View.VISIBLE);
                    etMobileNo.setVisibility(View.VISIBLE);
                    etvipInfo.setVisibility(View.VISIBLE);
                    otp_VehicleNumber.setVisibility(View.GONE);
//                    otp_VehicleNumber.setText("");
//                    vipVehicleNumber.setVisibility(View.VISIBLE);
//                    normalUserVehicle.setVisibility(View.GONE);
//                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
//                    btnCheckIn.setVisibility(View.VISIBLE);
//                    btnCheckout.setVisibility(View.VISIBLE);
//                    btnScanQRCode.setVisibility(View.VISIBLE);
//                    otp_VehicleNumber.setVisibility(View.GONE);
//                    etMobileNo.setVisibility(View.GONE);


                }

                if (checkedId == R.id.onlineUser) {
                    onlineUser.setChecked(true);
                    normalUserVehicle.setVisibility(View.GONE);
                    etMobileNo.setVisibility(View.GONE);
                    vipVehicleNumber.setVisibility(View.GONE);
                    otp_VehicleNumber.setVisibility(View.VISIBLE);
                    btnScanQRCodeCheckIn.setVisibility(View.GONE);
                    btnCheckIn.setVisibility(View.VISIBLE);
                    btnCheckout.setVisibility(View.VISIBLE);
                    btnScanQRCode.setVisibility(View.GONE);
                    otp_VehicleNumber.setHint("Enter OTP");
                    vipVehicleNumber.setText("");
                    userType = "Online";

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

        AppConstants.hideKeyboard(ThreeWheeler.this, v);

        if (v == btnCheckIn) {
            validate();
        } else if (v == btnCheckout) {
            validate1();
        } else if (ContextCompat.checkSelfPermission(ThreeWheeler.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        } else if (v == btnScanQRCode) {
            moveToQrScan();
        } else if (v == btnScanQRCodeCheckIn) {
            moveToQrScan();
        }
    }

    private void moveToQrScan() {
        try {
            Intent qrintent = new Intent(ThreeWheeler.this, QRScanPrint.class);
            qrintent.putExtra("userType", userType);
            qrintent.putExtra("Acticityclass", "TwoWheelerActivity");

            if (pType.length == 2) {
                qrintent.putExtra("Wheeler", "3Wheeler");
            } else {
                qrintent.putExtra("Wheeler", "3Wheeler");
            }
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


        if (userType.equals("VIP")) {
            if (AppConstants.isBlank(vipvehicleNumber)) {
                if (!etvipInfo.getText().toString().equals("")) {
                    vipInfo = etvipInfo.getText().toString();
                    chechIn();
                } else {
                    Toast.makeText(ThreeWheeler.this, "Enter VIP Info", Toast.LENGTH_SHORT).show();
                }
                //  chechIn();
//                vipVehicleNumber.requestFocus();
//                Toast.makeText(TwoWheeler.this, "Enter Vehicle number required", Toast.LENGTH_SHORT).show();
            } else {
                apiCall();
            }
        } else {
            if (userType.equals("Online")) {
                if (AppConstants.isBlank(otp_VehicleNumber.getText().toString())) {
                    otp_VehicleNumber.requestFocus();
                    Toast.makeText(ThreeWheeler.this, "Enter OTP required", Toast.LENGTH_SHORT).show();
                } else {
                    apiCall();
                }
                // apiCall();
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
            AppConstants.showToast(ThreeWheeler.this, "Vehicle number should be 4 digit");
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

        if (userType.equals("VIP")) {
            if (AppConstants.isBlank(vipvehicleNumber)) {
                checkOut();
//                vipVehicleNumber.requestFocus();
//                Toast.makeText(TwoWheeler.this, "Enter Vehicle number required", Toast.LENGTH_SHORT).show();
            } else {
                apiCallCheckOut();
            }
        } else {
            if (userType.equals("Online")) {
                if (AppConstants.isBlank(otp_VehicleNumber.getText().toString())) {
                    vipVehicleNumber.requestFocus();
                    Toast.makeText(ThreeWheeler.this, "Enter OTP required", Toast.LENGTH_SHORT).show();
                } else {
                    apiCallCheckOut();
                }
                // apiCallCheckOut();
            } else {
                checkOut();
            }
        }
    }

    public void checkOut() {
        if (AppConstants.isBlank(vehicleNumber)) {
            showAlert("Valid vehicle number required");
            etVehicleNumber.requestFocus();
        } else if (etVehicleNumber.getText().toString().length() < 4) {
            AppConstants.showToast(ThreeWheeler.this, "Vehicle number should be 4 digit");
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
                vehicleNo = otp_VehicleNumber.getText().toString();
                otp_VehicleNumber.requestFocus();
                //  vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            } else {
                vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            }
            //vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        }
        if (AppConstants.isInternetAvailable(ThreeWheeler.this)) {
            // String vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            //  vehicleCheckIn(parkingId, parkingType1234, vehicleNo, agentId, userType, mobileNumber, tagID);

            if (pType.length == 2) {
                String url = AppConstants.BASEURL + "parking/checkin";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), "3Wheeler");
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("mode"), userType);
                parameterData.put(("userdata"), mobileNumber);
                parameterData.put(("cardNo"), tagID);
                parameterData.put(("vipInfo"), vipInfo);

                vehicleCheckInServices(url, parameterData);
                //  vehicleCheckIn(parkingId, pType2, vehicleNo, agentId, userType, mobileNumber, tagID, vipinfo);
            } else {
                String url = AppConstants.BASEURL + "parking/checkin";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), "3Wheeler");
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("mode"), userType);
                parameterData.put(("userdata"), mobileNumber);
                parameterData.put(("cardNo"), tagID);
                parameterData.put(("vipInfo"), vipInfo);
                vehicleCheckInServices(url, parameterData);
                // vehicleCheckIn(parkingId, parkingType1234, vehicleNo, agentId, userType, mobileNumber, tagID, vipinfo);
            }
        } else {
            Toast.makeText(ThreeWheeler.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }


    public void vehicleCheckInServices(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(ThreeWheeler.this);
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
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "parking/checkin");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/checkin");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Server error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/checkin");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/checkin");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/checkin");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "parking/checkin");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_LONG).show();
                    }   pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            sendError(e.toString(), "parking/checkin");
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
                    cleareText();
                    JSONObject checkINresponce = response.getJSONObject("data");
                    etStateCode.setEnabled(false);
                    etCityCode.setEnabled(false);
                    etVehicleCode.setEnabled(false);
                    etVehicleNumber.setEnabled(false);
                    vipVehicleNumber.setEnabled(false);
                    etMobileNo.setEnabled(false);

                    etStateCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    etCityCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    etVehicleCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    etVehicleNumber.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    vipVehicleNumber.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    etMobileNo.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnCheckIn.setBackground(getResources().getDrawable(R.drawable.buttongradient_gray));
                    btnCheckout.setBackground(getResources().getDrawable(R.drawable.buttongradient_gray));
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
                    //  checkInModel.setBarcode(checkINresponce.getString("barcode"));
                    checkInModel.setResponseType(checkINresponce.getString("responseType"));
                    checkInModel.setParkingType(checkINresponce.getString("parkingType"));
                    checkInModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                    checkInModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                    checkInModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                    checkInModel.setQrCode(checkINresponce.getString("barCode"));
                    checkInModel.setAgentName(checkINresponce.getString("agentName"));
                    checkInModel.setCardNo(checkINresponce.getString("cardNo"));
                    checkInModel.setLastLine(checkINresponce.getString("lastLine"));

                    // Toast.makeText(FourWheeler.this, "Parking Done!" + message, Toast.LENGTH_LONG).show();

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
                    barCode = checkInModel.getQrCode();
                    lastLine = checkInModel.getLastLine();

                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("parkingPrint", "checkInModel");
                    editor.apply();
                    editor.commit();


                    String cardNo = checkInModel.getCardNo();
                    if (cardNo.equals("")) {
                        printReceipt();
                        // Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        tagID = "";
                        if (pType.length == 2) {
                            availableSlotSevice(parkingId, "3Wheeler", agentId);
                        } else {
                            availableSlotSevice(parkingId, "3Wheeler", agentId);
                        }
                        // availableSlotSevice(parkingId, parkingType1234);
                        // printReceipt();
                    }

                    // printReceipt();
                    // Toast.makeText(FourWheeler.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    if (message.equals("Card already Used for Checkin. Checkout first and then use again !")) {
                        Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        tagID = "";
                        if (pType.length == 2) {
                            availableSlotSevice(parkingId, "3Wheeler", agentId);
                        } else {
                            availableSlotSevice(parkingId, "3Wheeler", agentId);
                        }
                    } else {
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        tagID = "";
                        Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NullPointerException e) {
                sendError(e.toString(), "parking/checkin");
                Toast.makeText(ThreeWheeler.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                sendError(e.toString(), "parking/checkin");
                Toast.makeText(ThreeWheeler.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
    }


    private void apiCallCheckOut() {
        String vehicleNo;
        if (userType.equals("VIP")) {
            //vehicleNo = vipvehicleNumber;
            vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        } else {
            if (userType.equals("Online")) {
                // vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
                vehicleNo = otp_VehicleNumber.getText().toString();
            } else {
                vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            }
            // vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        }
        if (AppConstants.isInternetAvailable(ThreeWheeler.this)) {
            //    String vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
            if (pType.length == 2) {
                vehicleCheckOut(parkingId, "3Wheeler", vehicleNo, agentId, userType, mobileNumber, tagID, vipinfo);
            } else {

                vehicleCheckOut(parkingId, "3Wheeler", vehicleNo, agentId, userType, mobileNumber, tagID, vipinfo);
            }
            //  vehicleCheckOut(parkingId, parkingType1234, vehicleNo, agentId, userType, mobileNumber, tagID);
        } else {
            Toast.makeText(ThreeWheeler.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    private void showAlert(String message) {
        AppConstants.showToast(ThreeWheeler.this, message);
    }

    public void cleareText() {
        // etStateCode.setText("");
        //etCityCode.setText("");
        etVehicleCode.setText("");
        etVehicleNumber.setText("");
        vipVehicleNumber.setText("");
        etMobileNo.setText("");
        etvipInfo.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(ThreeWheeler.this, MainActivity.class);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/


    public void availableSlotSevice(final String parkingId, final String parkingType, final String agentId) {

        final ProgressDialog pDialog = new ProgressDialog(ThreeWheeler.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        /*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*/

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

                       /* if (parkingType.equals("2W")){
                            tvAvailableSpots.setText(availableresponce.getString("availableSlots"));
                            tvTotalSpots.setText(availableresponce.getString("bookedSlots"));
                        }*/
                        tvAvailableSpots.setText(availableresponce.getString("availableSlots"));
                        tvTotalSpots.setText(availableresponce.getString("bookedSlots"));


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/inventory?parkingId=");
                    Toast.makeText(ThreeWheeler.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/inventory?parkingId=");
                    Toast.makeText(ThreeWheeler.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(TwoWheeler.this, error.getMessage(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(ThreeWheeler.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {   sendError(error.toString(), "parking/inventory?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                } else if (error instanceof ServerError) {   sendError(error.toString(), "parking/inventory?parkingId=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {   sendError(error.toString(), "parking/inventory?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                }  else if (error instanceof NoConnectionError) {   sendError(error.toString(), "parking/inventory?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                } else if (error instanceof TimeoutError) {   sendError(error.toString(), "parking/inventory?parkingId=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }else if (error instanceof ParseError) {   sendError(error.toString(), "parking/inventory?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void vehicleCheckIn(final String parkingId, String parkingType, String vehicleNo, final String agentId, String mode, String userdata, String cardNo, String vipinfo) {
        final ProgressDialog pDialog = new ProgressDialog(ThreeWheeler.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        /*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*/

        String urlData = AppConstants.BASEURL + "parking/checkin?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo + "&vipInfo=" + vipinfo;

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

                        //cleareText();
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
                        //checkInModel.setPrintReceipt(checkINresponce.getString("printReceipt "));

                        //  Toast.makeText(TwoWheeler.this, "Parking Done!" + message, Toast.LENGTH_LONG).show();
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
                        barCode = checkInModel.getQrCode();
                        lastLine = checkInModel.getLastLine();
                        // printReceipt  = checkInModel.getPrintReceipt();

                        //  Toast.makeText(TwoWheeler.this, getCheckInDate, Toast.LENGTH_SHORT).show();
//
                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("parkingPrint", "checkInModel");
                        editor.apply();
                        editor.commit();

                        String cardNo = checkInModel.getCardNo();
                        if (cardNo.equals("")) {
                            printReceipt();
                            etVehicleNumber.requestFocus();
                        } else {

                            Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_LONG).show();
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            etMobileNo.setText("");
                            tagID = "";
                            etVehicleNumber.requestFocus();
                            if (pType.length == 2) {
                                availableSlotSevice(parkingId, "3Wheeler", agentId);
                            } else {
                                availableSlotSevice(parkingId, "3Wheeler", agentId);
                            }
//                            availableSlotSevice(parkingId, parkingType1234);

                        }
                        //printReceipt();
                        //  Toast.makeText(TwoWheeler.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();


                    } else {
                        if (message.equals("Card already Used for Checkin. Checkout first and then use again !")) {
                            Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_LONG).show();
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            etMobileNo.setText("");
                            tagID = "";
                            if (pType.length == 2) {
                                availableSlotSevice(parkingId, "3Wheeler", agentId);
                            } else {
                                availableSlotSevice(parkingId, "3Wheeler", agentId);
                            }
                        } else {
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            etMobileNo.setText("");
                            tagID = "";
                            Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkin?parkingId=");
                    Toast.makeText(ThreeWheeler.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkin?parkingId=");
                    Toast.makeText(ThreeWheeler.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(TwoWheeler.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ThreeWheeler.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {   sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) {   sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {   sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }  else if (error instanceof NoConnectionError) {   sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof TimeoutError) {   sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }else if (error instanceof ParseError) {   sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void vehicleCheckOut(final String parkingId, String parkingType, String vehicleNo, final String agentId, String mode, String userdata, String cardNo, String vipinfo) {
        final ProgressDialog pDialog = new ProgressDialog(ThreeWheeler.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
         /*http://staggingapi.opark.in/index.php/v1/parking/checkout?parkingId=1&parkingType=4Wheeler&vehicleNo=6565&agentId=3&mode=Normal&userdata=9993289838*/

        String urlData = AppConstants.BASEURL + "parking/checkout?parkingId=" + parkingId + "&parkingType=" + "3Wheeler" + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo + "&vipInfo=" + vipinfo;

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
                        etStateCode.setEnabled(false);
                        etCityCode.setEnabled(false);
                        etVehicleCode.setEnabled(false);
                        etVehicleNumber.setEnabled(false);
                        vipVehicleNumber.setEnabled(false);
                        etMobileNo.setEnabled(false);

                        etStateCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        etCityCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        etVehicleCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        etVehicleNumber.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        vipVehicleNumber.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        etMobileNo.setBackgroundColor(getResources().getColor(R.color.light_grey));
                        btnCheckIn.setBackground(getResources().getDrawable(R.drawable.buttongradient_gray));
                        btnCheckout.setBackground(getResources().getDrawable(R.drawable.buttongradient_gray));

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
                        checkOutModel.setBarcode(checkINresponce.getString("barcode"));
                        checkOutModel.setResponseType(checkINresponce.getString("responseType"));
                        checkOutModel.setMinimumAmount(checkINresponce.getString("minimumAmount"));
                        checkOutModel.setParkingType(checkINresponce.getString("parkingType"));
                        checkOutModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                        checkOutModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                        checkOutModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                        checkOutModel.setCardNo(checkINresponce.getString("cardNo"));
                        checkOutModel.setPrintReceipt(checkINresponce.getString("printReceipt"));

                        String cardNo = checkINresponce.getString("cardNo");

                        String heading = checkOutModel.getReceiptHeading();
                        String add = checkOutModel.getParkingAddress();

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
                        printReceipt = checkOutModel.getPrintReceipt();

                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();

                        editor.putString("parkingPrint", "checkOUTModel");

                        editor.apply();
                        editor.commit();


                        if (printReceipt.equals("no")) {
                            //  printReceipt();
                           // Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            etStateCode.setText("");
                            etCityCode.setText("");
                            etVehicleCode.setText("");
                            etVehicleNumber.setText("");
                            vipVehicleNumber.setText("");
                            etMobileNo.setText("");
                            tagID = "";
                            availableSlotSevice(parkingId, "3Wheeler", agentId);
                        } else if (cardNo.equals("")) {

                            printReceipt();

                        } else {

                            if (printReceipt.equals("yes")) {
                                printReceipt();
                            } else {
                                etStateCode.setText("");
                                etCityCode.setText("");
                                etVehicleCode.setText("");
                                etVehicleNumber.setText("");
                                vipVehicleNumber.setText("");
                                etMobileNo.setText("");
                                etVehicleNumber.requestFocus();
                                if (pType.length == 2) {
                                    availableSlotSevice(parkingId, "3Wheeler", agentId);
                                } else {
                                    availableSlotSevice(parkingId, "3Wheeler", agentId);
                                }

                            }
                            //Toast.makeText(TwoWheeler.this, message, Toast.LENGTH_SHORT).show();


                            // availableSlotSevice(parkingId, parkingType1234);
                        }
                        //printReceipt();
                        pDialog.dismiss();


                    } else {
                        etStateCode.setText("");
                        etCityCode.setText("");
                        etVehicleCode.setText("");
                        etVehicleNumber.setText("");
                        vipVehicleNumber.setText("");
                        etMobileNo.setText("");
                        etVehicleNumber.requestFocus();
                        Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
                        //cleareText();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkout?parkingId=");
                    Toast.makeText(ThreeWheeler.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    cleareText();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkout?parkingId=");
                    Toast.makeText(ThreeWheeler.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    cleareText();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   Toast.makeText(TwoWheeler.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ThreeWheeler.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) { sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) { sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) { sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }  else if (error instanceof NoConnectionError) { sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof TimeoutError) { sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }else if (error instanceof ParseError) { sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(ThreeWheeler.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ThreeWheeler.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {

            printNormal(ThreeWheeler.this, printer);

        } catch (Exception e) {

        }

    }

    public void printNormal(Context newEntryActivity, Printer printer) {

        /*sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        String parkingPrint = sharedpref.getString("parkingPrint", "");*/

        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);

        if (storeAllValues.getString("parkingPrint", "").equals("checkInModel")) {

            //  Toast.makeText(TwoWheeler.this, "With Print :   " + getCheckInDate, Toast.LENGTH_SHORT).show();

            printer.printText(getReceiptHeading, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.LEFT);

            printer.printText(getParkingAddress,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("---------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("     " + getCheckInDate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("VEHICLENO.-" + getVehicleNo,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getAdditionalParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printQrCode(barCode, 200, Printer.Gravity.CENTER);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("ATTENDANT - " + getAgentName + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

           /* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
*/
            printer.printText("Helpline No:" + getReceiptMobile, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Powered By:-" + getPoweredBy, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getCompanyWebsite, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Receipt No. -  " + getReceiptNo, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText(lastLine, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            if (pType.length == 2) {
                availableSlotSevice(parkingId, "3Wheeler", agentId);
            } else {
                availableSlotSevice(parkingId, "3Wheeler", agentId);
            }
            // availableSlotSevice(parkingId, parkingType1234);
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

            if (pType.length == 2) {
                availableSlotSevice(parkingId, "3Wheeler", agentId);
            } else {
                availableSlotSevice(parkingId, "3Wheeler", agentId);
            }
            // availableSlotSevice(parkingId, parkingType1234);
            cleareText();

        }

//        if (printError.equals("yes")) {
        if (storeAllValues.getString("parkingPrint", "").equals(printError)) {
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

        AppConstants.setString(ThreeWheeler.this, AppConstants.TAGID, tagID);

        //Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        if (tagID.equals("")) {
            Toast.makeText(this, "Card Not register", Toast.LENGTH_SHORT).show();
        } else {
            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

            userTypeData = sharedpref.getString("userType", "");
            Log.e(TAG, "id: userTypeData" + userTypeData);
            if (userTypeData.equals("Normal")) {
                normalUser.setChecked(true);
                vipUser.setChecked(false);
                userType = "Normal";
                etvipInfo.setVisibility(View.GONE);
            } else {
                vipUser.setChecked(true);
                normalUser.setChecked(false);
                userType = "VIP";
                etvipInfo.setVisibility(View.VISIBLE);
            }
            etStateCode.setEnabled(true);
            etCityCode.setEnabled(true);
            etVehicleCode.setEnabled(true);
            etVehicleNumber.setEnabled(true);
            vipVehicleNumber.setEnabled(true);
            etMobileNo.setEnabled(true);
            etStateCode.setEnabled(true);

            etStateCode.setBackgroundColor(getResources().getColor(R.color.white));
            etCityCode.setBackgroundColor(getResources().getColor(R.color.white));
            etVehicleCode.setBackgroundColor(getResources().getColor(R.color.white));
            etVehicleNumber.setBackgroundColor(getResources().getColor(R.color.white));
            vipVehicleNumber.setBackgroundColor(getResources().getColor(R.color.white));
            etMobileNo.setBackgroundColor(getResources().getColor(R.color.white));
            btnCheckIn.setBackground(getResources().getDrawable(R.drawable.buttongradient));
            btnCheckout.setBackground(getResources().getDrawable(R.drawable.gradientback));
            //nfcDetailForINOut(tagID);
            passDetailsCheckoutApi(tagID);
        }
        formatter.close();

    }
    public void passDetailsCheckoutApi(String tagID) {
        Log.e(TAG, "passDetailsCheckoutApi: " );
        final ProgressDialog pDialog = new ProgressDialog(ThreeWheeler.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/parking/pass_detail?cardNo=4BHH5678*/
//cardNo=CARD578&parkingId=30&parkingType=2Wheeler
        String urlData = AppConstants.BASEURL + "parking/pass_detail_checkout?cardNo=" + tagID+"&parkingId="+parkingId+"&parkingType=3Wheeler";
        Log.e(TAG, "passDetailsCheckoutApi:urlData "+urlData );
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");
                    Toast.makeText(ThreeWheeler.this, ""+message, Toast.LENGTH_SHORT).show();

                    if (status == 0) {
                        Toast.makeText(ThreeWheeler.this, ""+message, Toast.LENGTH_SHORT).show();
                        JSONObject checkINresponce = response.getJSONObject("data");

                        SubscriptionModelNew subscriptionModel = new SubscriptionModelNew();

                        /*subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subscriptionModel.setAgentId(checkINresponce.getString("agentId"));*/
                        subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subVehicleNo = subscriptionModel.getVehicleNo();

                        String[] totalsubVehicleNo = subVehicleNo.toString().split(",");

                       /* String StateCode = totalsubVehicleNo[0];
                        String CityCode = totalsubVehicleNo[1];
                        String VehicleCode = totalsubVehicleNo[2];
                        String vehicleNumber = totalsubVehicleNo[3];

                        etStateCode.setText(StateCode);
                        etCityCode.setText(CityCode);
                        etVehicleCode.setText(VehicleCode);
                        etVehicleNumber.setText(vehicleNumber);
*/try {
                            if (totalsubVehicleNo[0].equals(" ")) {
                                etStateCode.setText("");
                            } else {
                                String StateCode = totalsubVehicleNo[0];
                                etStateCode.setText(StateCode);
                            }


                            String vehicleNo1 = StateCode + CityCode + VehicleCode + vehicleNumber;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (totalsubVehicleNo[1].equals(" ")) {
                                etCityCode.setText("");
                            } else {
                                String StateCode = totalsubVehicleNo[1];
                                etCityCode.setText(StateCode);
                            }
                          /*  String CityCode = totalsubVehicleNo[1];

                            etCityCode.setText(CityCode);
*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (totalsubVehicleNo[2].equals(" ")) {
                                etVehicleCode.setText("");
                            } else {
                                String VehicleCode = totalsubVehicleNo[2];
                                etVehicleCode.setText(VehicleCode);
                            }
                         /*   String VehicleCode = totalsubVehicleNo[2];

                            etVehicleCode.setText(VehicleCode);
*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            String vehicleNumber = totalsubVehicleNo[3];

                            etVehicleNumber.setText(vehicleNumber);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String vehicleNo1 = StateCode + CityCode + VehicleCode + vehicleNumber;

                        // vehicleCheckIn(parkingId, parkingType1234, vehicleNo1, agentId, userType, mobileNumber, subscriptionModel.getCardNo());

                        //    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ThreeWheeler.this, ""+message, Toast.LENGTH_SHORT).show();

                        if (message1.equals("Card Not Exist !")) {

                            Toast.makeText(ThreeWheeler.this, message1, Toast.LENGTH_SHORT).show();

                        } else {

                        }


                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                   /* e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();*/
                } catch (JSONException e) {
                 /*   e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();*/
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                   /* sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
*/
                } else  {
                   /* sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
*/
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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

        final ProgressDialog pDialog = new ProgressDialog(ThreeWheeler.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/parking/pass_detail?cardNo=4BHH5678*/

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

                        /*subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subscriptionModel.setAgentId(checkINresponce.getString("agentId"));*/
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

                            Toast.makeText(ThreeWheeler.this, message1, Toast.LENGTH_SHORT).show();

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

                            receiptHeading = subscriptionModel.getReceiptHeading();
                            parkingAddress = subscriptionModel.getParkingAddress();
                            companyWebsite = subscriptionModel.getCompanyWebsite();
                            poweredBy = subscriptionModel.getPoweredBy();
                            tagID = "";
                            printReceipt();
                            Toast.makeText(ThreeWheeler.this, message1, Toast.LENGTH_SHORT).show();
                        }


                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(ThreeWheeler.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(ThreeWheeler.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(ThreeWheeler.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {  sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) {  sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {  sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }  else if (error instanceof NoConnectionError) {  sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof TimeoutError) {  sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }else if (error instanceof ParseError) {  sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
        if (AppConstants.isInternetAvailable(ThreeWheeler.this)) {

            if (pType.length == 2) {
                availableSlotSevice(parkingId, "3Wheeler", agentId);

            } else {
                availableSlotSevice(parkingId, "3Wheeler", agentId);
            }
        } else {
            Toast.makeText(ThreeWheeler.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }

        try {
            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
            userTypeData = sharedpref.getString("userType", "");

            if (userTypeData.equals("Normal")) {
                normalUser.setChecked(true);
                vipUser.setChecked(false);
                userType = "Normal";
                etvipInfo.setVisibility(View.GONE);
            } else {
                vipUser.setChecked(true);
                normalUser.setChecked(false);
                userType = "VIP";
                etvipInfo.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userType.equals("Normal")) {
            btnScanQRCode.setVisibility(View.GONE);
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
        }
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
            startActivity(new Intent(ThreeWheeler.this, MainActivity.class));
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
                Intent intent = new Intent(ThreeWheeler.this, TransationActivity.class);
                if (pType.length == 2) {

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

        if (AppConstants.isInternetAvailable(ThreeWheeler.this)) {
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
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Server error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ThreeWheeler.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_LONG).show();
                    }
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


}
