package attender.oparkReceipt.vehiclelist.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.permissions.AppPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.base.VolleyMultipartRequest;
import attender.oparkReceipt.base.VolleySingleton;

import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.PopulateSpinnerPojo;

import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;

import static attender.oparkReceipt.base.AppConstants.getDataColumn;
import static attender.oparkReceipt.base.AppConstants.isDownloadsDocument;
import static attender.oparkReceipt.base.AppConstants.isExternalStorageDocument;
import static attender.oparkReceipt.base.AppConstants.isMediaDocument;


public class LossReceipt extends AppCompatActivity {
    private static final String TAG = "LossReceipt";
    private Toolbar toolBar;
    private TextView textToolHeader;
    EditText etStateCode, etCityCode, etVehicleCode, etVehicleNumber, vipVehicleNumber, otpVehicleNumber, rcnumber, name, mobilenumber, idNumber;
    private RadioGroup radioGroup, radioGroupType;
    private RadioButton normalUser, vipUser, onlineUser, fourwheeler, threeWheelerR, othrheeler, twowheeler;
    LinearLayout normalUserVehicle, vipUserVehicle;
    String subId = "", userTypeData, idName = "Select Identity Card", checkInDateTime;
    String agentId = "", GateId, userRole = "", userName = "", userContactNo = "", vendorId = "", vendorName = "", parkingName = "", parkingType = "", vehicleType1 = "", parkingId = "", userType = "", parkingType11, parkingType12 = "", subVehicleNo = "";
    Spinner idspinner;
    TextView chechinTime;
    Button submit, getTime;
    SharedPreferences.Editor ed;
    SharedPreferences sharedpref;
    ArrayList<String> plannames = new ArrayList<>();
    ArrayList<PopulateSpinnerPojo> planeList = new ArrayList<>();
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Uri imageUri;
    Uri imageUri1 = Uri.parse("");
    private String selectedPath = "";
    ImageView idPhoto, camera;
    String[] getParkingType = new String[3];
    private AppPermissions mRuntimePermission;
    private static final String[] ALL_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String receiptHeading = "", parkingAddress = "", vehicleNo = "", checkInDate = "", checkOutDate = "", onlineUserText = "", extraChargeText = "", printReceipt = "", duration = "", durationUnit = "", grandTotal = "", currencySymbol = "", poweredBy = "", companyWebsite = "", receiptNo = "";
    private static final int ALL_REQUEST_CODE = 0;
    private ServiceManager mServiceManager = null;
    Printer printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_receipt);
        printer = WeiposImpl.as().openPrinter();
        init();
        initSdk();
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        GateId = sharedpref.getString("GateId", "");
        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("getparkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        //getParkingID = sharedpref.getString("getParkingID", "");

        //parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType12 = sharedpref.getString("VehicleType1", "");

        /*if (parkingType12.equals("2Wheeler")) {
            vehicleType1 = "2Wheeler";
            twowheeler.setVisibility(View.VISIBLE);
            twowheeler.setChecked(true);
        } else if (parkingType12.equals("3Wheeler")) {
            vehicleType1 = "3Wheeler";
            threeWheelerR.setVisibility(View.VISIBLE);
            threeWheelerR.setChecked(true);
        } else {
            vehicleType1 = "4Wheeler";
            fourwheeler.setVisibility(View.VISIBLE);
            fourwheeler.setChecked(true);
        }*/



        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler,other")) {
            //twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.VISIBLE);
            if (parkingType12.equals("2Wheeler")) {
                fourwheeler.setVisibility(View.VISIBLE);
                threeWheelerR.setVisibility(View.VISIBLE);
                othrheeler.setVisibility(View.VISIBLE);
                twowheeler.setChecked(true);
            } else if (parkingType12.equals("4Wheeler")) {
                threeWheelerR.setVisibility(View.VISIBLE);
                twowheeler.setVisibility(View.VISIBLE);
                othrheeler.setVisibility(View.VISIBLE);
                fourwheeler.setChecked(true);
            } else if (parkingType12.equals("other")) {
                threeWheelerR.setVisibility(View.VISIBLE);
                twowheeler.setVisibility(View.VISIBLE);
                fourwheeler.setVisibility(View.VISIBLE);
                // fourwheeler.setChecked(true);
                othrheeler.setChecked(true);
            } else {
                twowheeler.setVisibility(View.VISIBLE);
                fourwheeler.setVisibility(View.VISIBLE);
                othrheeler.setVisibility(View.VISIBLE);
                threeWheelerR.setChecked(true);
            }
        } else if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler")) {
            //twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
            if (parkingType12.equals("2Wheeler")) {
                fourwheeler.setVisibility(View.VISIBLE);
                threeWheelerR.setVisibility(View.VISIBLE);
                twowheeler.setChecked(true);
            } else if (parkingType12.equals("4Wheeler")) {
                threeWheelerR.setVisibility(View.VISIBLE);
                twowheeler.setVisibility(View.VISIBLE);
                fourwheeler.setChecked(true);
            } else {
                twowheeler.setVisibility(View.VISIBLE);
                fourwheeler.setVisibility(View.VISIBLE);
                threeWheelerR.setChecked(true);
            }
        } else if (parkingType.equals("2Wheeler,4Wheeler")) {
            //  twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
            if (parkingType12.equals("2Wheeler")) {
                twowheeler.setChecked(true);
                twowheeler.setVisibility(View.VISIBLE);
                fourwheeler.setVisibility(View.VISIBLE);
                // threeWheelerR.setVisibility(View.GONE);
            } else {
                fourwheeler.setChecked(true);
                twowheeler.setVisibility(View.VISIBLE);
                fourwheeler.setVisibility(View.VISIBLE);
                //threeWheelerR.setVisibility(View.GONE);
            }
        } else if (parkingType.equals("2Wheeler")) {
            twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
        } else if (parkingType.equals("4Wheeler")) {
            fourwheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
        } else if (parkingType.equals("3Wheeler")) {
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setChecked(true);
            fourwheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
        } else {
            //  Log.e(TAG, "init: parkingType" + parkingType);
        }
        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler,other")) {
            othrheeler.setChecked(true);
            twowheeler.setChecked(true);
            fourwheeler.setChecked(true);
            threeWheelerR.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.VISIBLE);
        }else
        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler")) {
            twowheeler.setChecked(true);
            fourwheeler.setChecked(true);
            threeWheelerR.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.VISIBLE);
            fourwheeler.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.GONE);
        }
        else
        if (parkingType.equals("2Wheeler,4Wheeler")) {
            twowheeler.setChecked(true);
            fourwheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.GONE);
        }else
        if (parkingType.equals("4Wheeler")) {
            fourwheeler.setChecked(true);
            twowheeler.setVisibility(View.GONE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.GONE);
        }else
        if (parkingType.equals("2Wheeler")) {
            twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.GONE);
            othrheeler.setVisibility(View.GONE);

        }
        else
        if (parkingType.equals("4Wheeler")) {
            fourwheeler.setChecked(true);
            twowheeler.setVisibility(View.GONE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.GONE);

        }else
        if (parkingType.equals("other")) {
            othrheeler.setChecked(true);
            twowheeler.setVisibility(View.GONE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.GONE);
            othrheeler.setVisibility(View.VISIBLE);

        }else
        if (parkingType.equals("2Wheeler")) {
            twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.GONE);
            othrheeler.setVisibility(View.GONE);


        }else
        if (parkingType.equals("2Wheeler")) {
            twowheeler.setChecked(true);
            twowheeler.setVisibility(View.VISIBLE);
            threeWheelerR.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.GONE);
            othrheeler.setVisibility(View.GONE);
            twowheeler.setVisibility(View.GONE);
        }else
        if (parkingType.equals("3Wheeler")) {
            threeWheelerR.setChecked(true);
            twowheeler.setVisibility(View.GONE);
            fourwheeler.setVisibility(View.GONE);
            threeWheelerR.setVisibility(View.VISIBLE);
            othrheeler.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //twowheeler.setChecked(true);
    }

    public void init() {

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setTitle("");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Lost Receipt");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        submit = (Button) findViewById(R.id.submit);
        getTime = (Button) findViewById(R.id.getTime);

        normalUser = (RadioButton) findViewById(R.id.normalUserr);
        vipUser = (RadioButton) findViewById(R.id.vipUser);
        onlineUser = (RadioButton) findViewById(R.id.onlineUser);
        twowheeler = (RadioButton) findViewById(R.id.twowheeler);
        othrheeler = (RadioButton) findViewById(R.id.othrheeler);
        threeWheelerR = (RadioButton) findViewById(R.id.threeWheelerR);
        fourwheeler = (RadioButton) findViewById(R.id.fourwheeler);
        etStateCode = (EditText) findViewById(R.id.etStateCode);
        etCityCode = (EditText) findViewById(R.id.etCityCode);
        etVehicleCode = (EditText) findViewById(R.id.etVehicleCode);
        etVehicleNumber = (EditText) findViewById(R.id.etVehicleNumber);
        vipVehicleNumber = (EditText) findViewById(R.id.vipVehicleNumber);
        otpVehicleNumber = (EditText) findViewById(R.id.otpVehicleNumber);
        mobilenumber = (EditText) findViewById(R.id.mobilenumber);
        idNumber = (EditText) findViewById(R.id.idNumber);
        name = (EditText) findViewById(R.id.name);
        chechinTime = (TextView) findViewById(R.id.chechinTime);
        rcnumber = (EditText) findViewById(R.id.rcnumber);
        idspinner = (Spinner) findViewById(R.id.storelocation);
        idPhoto = (ImageView) findViewById(R.id.idPhoto);
        camera = (ImageView) findViewById(R.id.camera);

        normalUserVehicle = (LinearLayout) findViewById(R.id.normalUserVehicle);
        vipUserVehicle = (LinearLayout) findViewById(R.id.vipUserVehicle);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroupType);


        mobilenumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (mobilenumber.getText().toString().length() == 10)     //size as per your requirement
                {
                    rcnumber.requestFocus();
                    mobilenumber.setSelection(mobilenumber.getText().length());
                    AppConstants.hideKeyboard(LossReceipt.this, mobilenumber);
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
                    AppConstants.hideKeyboard(LossReceipt.this, etVehicleNumber);

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


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
                //selectImage();
            }
        });

        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String v = etStateCode.getText().toString() + etCityCode.getText().toString() + etVehicleCode.getText().toString() + etVehicleNumber.getText().toString();
                if (parkingType12.equals("")) {
                    Toast.makeText(LossReceipt.this, "Select Parking Type", Toast.LENGTH_SHORT).show();
                } else if (v.equals("")) {
                    Toast.makeText(LossReceipt.this, "Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                } else {
                    chechInTimereceiptService(parkingId, parkingType12, v);
                }
            }
        });
        etVehicleNumber.requestFocus();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etVehicleNumber.getText().toString().equals("")) {
                    Toast.makeText(LossReceipt.this, "Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                    etVehicleNumber.requestFocus();
                } else if (name.getText().toString().equals("")) {
                    name.requestFocus();
                    Toast.makeText(LossReceipt.this, "Enter Vehicle Holder Name", Toast.LENGTH_SHORT).show();
                } else if (mobilenumber.getText().toString().equals("")) {
                    mobilenumber.requestFocus();
                    Toast.makeText(LossReceipt.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (rcnumber.getText().toString().equals("")) {
                    rcnumber.requestFocus();
                    Toast.makeText(LossReceipt.this, "Enter RC Number", Toast.LENGTH_SHORT).show();
                } else {


                    if (!idName.equals("")) {
                        if (idNumber.getText().toString().equals("")) {
                            idNumber.requestFocus();
                            Toast.makeText(LossReceipt.this, "Enter Selected IdCard Number", Toast.LENGTH_SHORT).show();
                        } else if (imageUri == null) {
                            Toast.makeText(LossReceipt.this, "Take Id Card Photo", Toast.LENGTH_SHORT).show();
                        } else {
                            lossservice();
                        }
                    } else {
                        lossservice();
                    }

//                    if (!setSpinnerError(idspinner, "Select Identity Card")) {
//                        if (idNumber.getText().toString().equals("")) {
//                            idNumber.requestFocus();
//                            Toast.makeText(LossReceipt.this, "Enter Selected IdCard Number", Toast.LENGTH_SHORT).show();
//                        } else if (imageUri != null) {
//                            lossservice();
//                        } else {
//                            Toast.makeText(LossReceipt.this, "Take IdCard Photo", Toast.LENGTH_SHORT).show();
//
//                        }
//                    } else {
//
//
//                    }
//                    if (setSpinnerError(idspinner, "Select Identity Card")) {
//                        if (idNumber.getText().toString().equals("")) {
//                            idNumber.requestFocus();
//                            Toast.makeText(LossReceipt.this, "Enter Selected IdCard Number", Toast.LENGTH_SHORT).show();
//                        } else if (imageUri != null) {
//                            lossservice();
//                        } else {
//                            Toast.makeText(LossReceipt.this, "Take IdCard Photo", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }else {
//                        lossservice();
//                    }
                }
                // lossservice();
            }
        });
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        userTypeData = sharedpref.getString("userType", "");
        Log.e(TAG, "id: userTypeData" + userTypeData);
        if (userTypeData.equals("Normal")) {
            normalUser.setChecked(true);
            vipUser.setChecked(false);
            userType = "Normal";
        } else {
            vipUser.setChecked(true);
            normalUser.setChecked(false);
            userType = "VIP";
        }
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

                }

                if (checkedId == R.id.onlineUser) {
                    onlineUser.setChecked(true);
                    userType = "Online";
                }
            }
        });
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twowheeler) {
                    twowheeler.setChecked(true);
                    vehicleType1 = "2Wheeler";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", vehicleType1);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                } else if (checkedId == R.id.fourwheeler) {
                    twowheeler.setChecked(false);
                    vehicleType1 = "4Wheeler";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", vehicleType1);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                } else if (checkedId == R.id.threeWheelerR) {
                    threeWheelerR.setChecked(false);
                    vehicleType1 = "3Wheeler";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", vehicleType1);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                } else if (checkedId == R.id.othrheeler) {
                    othrheeler.setChecked(false);
                    vehicleType1 = "other";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", vehicleType1);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                    Log.e(TAG, "onCheckedChanged:parkingType12 ==="+parkingType12 );
                }

            }
        });

        if (AppConstants.isInternetAvailable(LossReceipt.this)) {
            lossreceiptService();
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);

        }

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LossReceipt.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = AppConstants.checkPermission(LossReceipt.this);

                cameraIntent();

            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (data != null) {
                    onCaptureImageResult(data);
                } else {
                    Toast.makeText(LossReceipt.this, "Take ID Image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
        //selectedPath = getRealPathFromURI(selectedImageUri);
        idPhoto.setVisibility(View.VISIBLE);
        imageUri = Uri.fromFile(destination);
        imageUri1 = Uri.fromFile(destination);
        selectedPath = destination.getAbsolutePath();

        // Toast.makeText(getActivity(), destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        idPhoto.setImageBitmap(thumbnail);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }


            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public void lossservice() {

        String urlData = AppConstants.BASEURL + "parking/checkout_lost";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setContentView(R.layout.custom_progress_bar);


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, urlData,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {

                            JSONObject result = new JSONObject(resultResponse);
                            //int res = result.getInt("RESPONSECODE");

                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(result.get("message"));
                            int status = result.getInt("status");
                            String response1 = String.valueOf(result.get("data"));


                            if (status == 0) {

                                idPhoto.setVisibility(View.VISIBLE);
                                JSONObject loginresponce = result.getJSONObject("data");
                                receiptHeading = loginresponce.getString("receiptHeading");
                                parkingAddress = loginresponce.getString("parkingAddress");
                                vehicleNo = loginresponce.getString("vehicleNo");
                                checkInDate = loginresponce.getString("checkInDate");
                                checkOutDate = loginresponce.getString("checkOutDate");
                                duration = loginresponce.getString("duration");
                                durationUnit = loginresponce.getString("durationUnit");
                                currencySymbol = loginresponce.getString("currencySymbol");
                                grandTotal = loginresponce.getString("grandTotal");
                                poweredBy = loginresponce.getString("poweredBy");
                                companyWebsite = loginresponce.getString("companyWebsite");
                                receiptNo = loginresponce.getString("receiptNo");
                                extraChargeText = loginresponce.getString("extraChargeText");
                                printReceipt = loginresponce.getString("printReceipt");
                                cleareText();
                                printReceipt();
                                // cleareText();
                                // Picasso.with(LossReceipt.this).load(R.mipmap.police_car512).into(idPhoto);
                                pDialog.dismiss();
                                // String respo = result.getString("RESPONSE");
                                Toast.makeText(LossReceipt.this, message, Toast.LENGTH_SHORT).show();


                            } else {
                                pDialog.dismiss();
                                cleareText();
                                Toast.makeText(LossReceipt.this, message, Toast.LENGTH_SHORT).show();
                            }

                            //  Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            sendError(e.toString(), "parking/checkout_lost");

                            pDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        sendError(errorMessage, "parking/checkout_lost");

                        pDialog.dismiss();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        sendError(errorMessage, "parking/checkout_lost");

                        pDialog.dismiss();
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                            sendError(errorMessage, "parking/checkout_lost");
                            pDialog.dismiss();
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                            sendError(errorMessage, "parking/checkout_lost");
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                            sendError(errorMessage, "parking/checkout_lost");
                            pDialog.dismiss();
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                            sendError(errorMessage, "parking/checkout_lost");
                            pDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        pDialog.dismiss();
                        sendError(e.toString(), "parking/checkout_lost");
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                pDialog.dismiss();
                error.printStackTrace();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                parkingType12 = sharedpref.getString("VehicleType1", "");

                //  String user_id = ConstantData.getString(getApplicationContext(),  USER_ID, "");
                String vehicleNumber = etStateCode.getText().toString() + etCityCode.getText().toString() + etVehicleCode.getText().toString() + etVehicleNumber.getText().toString();

                java.util.Map<String, String> params = new HashMap<>();
                params.put("mode", userType);
                params.put("idType", subId);
                params.put("vehicleNo", vehicleNumber);
                params.put("agentId", agentId);
                params.put("rcNo", rcnumber.getText().toString());
                params.put("holderName", name.getText().toString());
                params.put("mobileNo", mobilenumber.getText().toString());
                params.put("parkingId", parkingId);
                params.put("parkingType", parkingType12);
                params.put("idNo", idNumber.getText().toString());
                params.put("gateId", GateId);

                Log.e("===============", "getParams:params " + params);
//                params.put("operatorId", agentId);
//                params.put("vendorId", vendorId);


                //.addFileToUpload(path, "file") //Adding file
                return params;
            }

            @Override
            protected java.util.Map<String, DataPart> getByteData() {

                java.util.Map<String, DataPart> params = new HashMap<>();
                String uploadId = UUID.randomUUID().toString();
                try {
                    InputStream iStream = getApplicationContext().getContentResolver().openInputStream(imageUri1);
                    byte[] inputData = getBytes(iStream);
                    params.put("idCardImage", new DataPart(uploadId + ".jpg", inputData, "image/*"));

//                    if (!idName.equals("")) {
//                        if (imageUri != null) {
//                            InputStream iStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
//                            byte[] inputData = getBytes(iStream);
//                            params.put("idCardImage", new DataPart(uploadId + ".jpg", inputData, "image/*"));
//                        } else {
//                            Toast.makeText(LossReceipt.this, "Take IdCard Image", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//
//                    }

//                    if (idName.equals("Select Identity Card")) {
//                        if (imageUri != null) {
//                            InputStream iStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
//                            byte[] inputData = getBytes(iStream);
//                            params.put("idCardImage", new DataPart(uploadId + ".jpg", inputData, "image/*"));
//                        } else {
//                            Toast.makeText(LossReceipt.this, "Take IdCard Image", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        InputStream iStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
//                        byte[] inputData = getBytes(iStream);
//                        params.put("idCardImage", new DataPart(uploadId + ".jpg", inputData, "image/*"));
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkout_lost");
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);

    }

    private void cleareText() {
        etStateCode.setText("");
        etCityCode.setText("");
        etVehicleCode.setText("");
        etVehicleNumber.setText("");

        mobilenumber.setText("");
        idNumber.setText("");
        name.setText("");
        rcnumber.setText("");
        idPhoto.setVisibility(View.GONE);
        // Picasso.with(LossReceipt.this).load(R.mipmap.police_car512).into(idPhoto);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void lossreceiptService() {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/parking/id_list
        String urlData = AppConstants.BASEURL + "parking/id_list";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.d("Response", response + "");

                        planeList.clear();
                        // plannames.clear();
                        plannames.add("Select Identity Card");
                        PopulateSpinnerPojo populateSpinnerPojo1 = new PopulateSpinnerPojo();
                        populateSpinnerPojo1.setStoreId("0");
                        populateSpinnerPojo1.setStoreName("Identity Card");
                        planeList.add(populateSpinnerPojo1);

                        try {

                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(response.get("message"));
                            int status = response.getInt("status");
                            String response1 = String.valueOf(response.get("data"));
                            if (status == 0) {
                                final JSONArray arrayObj = new JSONArray(response1);
                                for (int i = 0; i < arrayObj.length(); i++) {

                                    JSONObject jsonObject = arrayObj.getJSONObject(i);

                                    String planId1 = jsonObject.getString("cardTypeId");
                                    String planName = jsonObject.getString("cardTypeName");

                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
                                    populateSpinnerPojo.setPlanId(planId1);
                                    populateSpinnerPojo.setPlanName(planName);
                                    plannames.add(planName);
                                    planeList.add(populateSpinnerPojo);
//                                    JSONObject jsonObject = arrayObj.getJSONObject(i);
//
//                                    String VoterId = jsonObject.getString("cardTypeId");
//                                    String cardTypeName = jsonObject.getString("cardTypeName");
//
//                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
//                                    populateSpinnerPojo.setStoreId(VoterId);
//                                    populateSpinnerPojo.setStoreName(cardTypeName);
//                                    plannames.add(cardTypeName);
//                                    // plannames.add(VoterId);
//                                    planeList.add(populateSpinnerPojo);
                                }
                                idspinner.setAdapter(new ArrayAdapter(LossReceipt.this, R.layout.my_spinner_style, plannames));

                                idspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subId = planeList.get(pos).getPlanId();
                                        idName = planeList.get(pos).getPlanName();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                            } else {
                                planeList.clear();
                                plannames.add(response1);
                                idspinner.setAdapter(new ArrayAdapter(LossReceipt.this, R.layout.my_spinner_style, plannames));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            sendError(e.toString(), "parking/id_list");


                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        sendError(error.toString(), "parking/id_list");


                    }
                });
        {


            request.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().getRequestQueue().add(request);

        }
    }

    public void chechInTimereceiptService(String parkingId, String parkingType, String vehicleNo) {
        final ProgressDialog pDialog = new ProgressDialog(LossReceipt.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/parking/lost_vehicle_list?parkingId=1&parkingType=2Wheeler&vehicleNo=AA0000
        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/parking/lost_vehicle_list?parkingId=1&parkingType=2Wheeler,4Wheeler&vehicleNo=3155
        String urlData = AppConstants.BASEURL + "parking/lost_vehicle_list?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.d("Response", response + "");


                        try {

                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(response.get("message"));
                            int status = response.getInt("status");
                            String response1 = String.valueOf(response.get("data"));
                            if (status == 0) {

//                                JSONObject loginresponce = response.getJSONObject("data");
//                                String checkInDateTime = loginresponce.getString("checkInDateTime");
//                                chechinTime.setVisibility(View.VISIBLE);
//                                chechinTime.setText(checkInDateTime);
                                final JSONArray arrayObj = new JSONArray(response1);
                                for (int i = 0; i < arrayObj.length(); i++) {

                                    JSONObject jsonObject = arrayObj.getJSONObject(i);

                                    checkInDateTime = jsonObject.getString("checkInDateTime");
                                    chechinTime.setVisibility(View.VISIBLE);
                                    chechinTime.setText("CheckInDateTime : " + checkInDateTime);
                                    // String aadharCard = jsonObject.getString("cardTypeName");

//                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
//                                    populateSpinnerPojo.setStoreId(VoterId);
//                                    populateSpinnerPojo.setStoreName(aadharCard);
//                                    plannames.add(aadharCard);
//                                    // plannames.add(VoterId);
//                                    planeList.add(populateSpinnerPojo);
                                }
                                pDialog.dismiss();
                            } else {
                                pDialog.dismiss();
                                Toast.makeText(LossReceipt.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            sendError(e.toString(), "parking/lost_vehicle_list?parkingId=");


                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        sendError(error.toString(), "parking/lost_vehicle_list?parkingId=");


                    }
                });
        {


            request.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().getRequestQueue().add(request);

        }
    }

    private boolean setSpinnerError(Spinner spinner, String error) {

        boolean flag = true;

        View selectedView = spinner.getSelectedView();

        if (!spinner.getSelectedItem().toString().equals(error)) {

            flag = true;

        } else {
            if (selectedView != null && selectedView instanceof TextView) {
                spinner.requestFocus();
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError("error"); // any name of the error will do
                selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
                selectedTextView.setText("Select Identity Card"); // actual error message
                spinner.performClick(); // to open the spinner list if error is found.
                flag = false;
            }
        }
        return flag;
    }

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(LossReceipt.this, new Weipos.OnInitListener() {

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
            Toast.makeText(LossReceipt.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LossReceipt.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(LossReceipt.this, printer);

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

        /*sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        String parkingPrint = sharedpref.getString("parkingPrint", "");*/
        /*message": "Checkout Successfull",
    "data": {
        "receiptHeading": "Smart Parking Gwalior",
        "parkingAddress": "City Center MLCP Block - A\r\n",
        "vehicleNo": "MP04DD1111/2Wheeler",
        "checkInDate": "07/02/2019 04:16 PM",
        "checkOutDate": "07/02/2019 04:16 PM",
        "onlineUserText": "",
        "checkInTime": "04:16 PM",
        "checkOutTime": "04:16 PM",
        "duration": 0,
        "durationUnit": " mins 33 sec ",
        "grandTotal": "",
        "currencySymbol": " (No Charges for VIP User)",
        "userContactNo": "9485623258",
        "agentId": 16,
        "availableSlots": 211,
        "parkingId": 1,
        "parkingRate": "5",
        "total": 5,
        "barcode": "",
        "mode": "VIP",
        "responseType": "checkout",
        "minimumAmount": "5",
        "parkingName": "City Center MLCP Block - A",
        "poweredBy": "Daffodil Technologies(I) Pvt Ltd",
        "companyWebsite": "www.daffodilglobal.com",
        "parkingType": "2Wheeler",
        "receiptNo": "201902078",
        "cardNo": "",
        "printReceipt": "",
        "extraChargeText": ""
    }*/

        printer.printText(receiptHeading, Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                Printer.Gravity.CENTER);

        printer.printText(parkingAddress,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

        printer.printText("--------------", Printer.FontFamily.SONG,
                Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                Printer.Gravity.CENTER);

        printer.printText(onlineUserText, Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                Printer.Gravity.LEFT);
//
        printer.printText("VNO.-" + vehicleNo,
                Printer.FontFamily.SONG, Printer.FontSize.LARGE,
                Printer.FontStyle.BOLD, Printer.Gravity.RIGHT);

        printer.printText("", Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                Printer.Gravity.CENTER);

        printer.printText("In-  " + checkInDate,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
        printer.printText("Out- " + checkOutDate,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

        printer.printText("", Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                Printer.Gravity.CENTER);

        printer.printText(duration + " " + durationUnit + " " + currencySymbol + " " + String.valueOf(grandTotal),
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

        printer.printText(printReceipt,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

        printer.printText(extraChargeText,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);


        printer.printText("\n", Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                Printer.Gravity.CENTER);
        printer.printText("Powered By:- " + poweredBy,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

        printer.printText(companyWebsite, Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                Printer.Gravity.CENTER);

        printer.printText("Receipt No. -  " + receiptNo,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

        printer.printText("\n", Printer.FontFamily.SONG,
                Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                Printer.Gravity.CENTER);

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(LossReceipt.this)) {
            send(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);
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
                Toast.makeText(LossReceipt.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LossReceipt.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
    }

}
