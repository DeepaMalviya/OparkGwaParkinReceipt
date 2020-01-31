package attender.oparkReceipt.subscription.activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.InputValidation;
import attender.oparkReceipt.subscription.model.PopulateSpinnerPojo;
import attender.oparkReceipt.subscription.model.SubscriptionModel;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class RenewCard extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolBar;
    private TextView textToolHeader;
    String tagID;
    //    private TextInputLayout textInputLayoutRenewCard;
//    private TextInputEditText textInputReNewCard;
//    private AppCompatButton appCompatButtonRenewCard;
    Spinner spinnerDurationn, spinnerplan;
    ArrayList<String> planSelection = new ArrayList<>();
    ArrayList<String> planlId = new ArrayList<>();
    ArrayList<String> plannames = new ArrayList<>();
    ArrayList<String> duratioList = new ArrayList<>();
    ArrayList<PopulateSpinnerPojo> planeList = new ArrayList<>();
    SharedPreferences sharedpref;

    RadioGroup radioGroup;
    RadioButton twowheelerRadio, threewheelerRadio, fourwheelerRadio;
    //    ImageView bike, car;
    String cardNo, cardNo1;
    //    private String VehicleType = "";
//    private String subCardNo = "";
    private ServiceManager mServiceManager = null;
    Printer printer;
    private NfcAdapter mNfcAdapter;
    private InputValidation inputValidation;

    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutMobileNumber;
    private TextInputLayout textInputLayout1Name;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutnfcnumber;
    private TextInputLayout textInputLayoutnumber;

    private EditText textInputEditTextStateCode;
    private EditText textInputEditTextVehicleCode;
    private EditText textInputEditTextCityCode;
    private EditText textInputEditTextvehicleNumber;

    private TextInputEditText textInputEditTextMobileNumber;
    private TextInputEditText textInputEditName;
    private TextInputEditText textInputEditAddress;
    private TextInputEditText textInputEditnfcnumber;
    private TextInputEditText textInputEditnumber;
    private AppCompatButton appCompatButtonRegister;

    String agentId, userRole, userName, userContactNo, vendorId, vendorName, parkingName, parkingType, parkingId, vehicleNo, AgentName, ParkingName, holderName, passHeading, plan, total, renewableDate,
            pType2, pType4, parkingVehicle, sidefour, userType, parkingType1234, name, mobileNumber, subDurationId, subPlanId, planename, varient1;

    private String vehicleNumber = "";
    private String StateCode = "";
    private String CityCode = "";
    private String VehicleCode = "";
    private String VehicleType = "";
    private String subCardNo = "";
    private String subAgentNo = "", Price;
    private String subVehicleNo = "";
    private String address = "";
    ImageView bike, threecar, car;
    EditText TvPricee;
    ArrayList<String> durationNumber = new ArrayList<>();
LinearLayout linearPric;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_card);
        printer = WeiposImpl.as().openPrinter();


        initViews();
        //  init();
        initSdk();
        initNFC();
        initSdk();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        linearPric =  findViewById(R.id.linearPric);
        linearPric.setVisibility(View.GONE);
        TvPricee = (EditText) findViewById(R.id.TvPricee);
        Price = TvPricee.getText().toString().trim();

//        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
//        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
//        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
//        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputLayoutMobileNumber = (TextInputLayout) findViewById(R.id.textInputLayoutMobileNumber);
        textInputLayout1Name = (TextInputLayout) findViewById(R.id.textInputLayout1Name);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.textInputLayoutAddress);
        textInputLayoutnfcnumber = (TextInputLayout) findViewById(R.id.textInputLayoutnfcnumber);
        textInputLayoutnumber = (TextInputLayout) findViewById(R.id.textInputLayoutnumber);

        textInputEditTextStateCode = (EditText) findViewById(R.id.textInputEditStateCode);

        textInputEditTextCityCode = (EditText) findViewById(R.id.textInputTextCityCode);
        textInputEditTextVehicleCode = (EditText) findViewById(R.id.textInputEditVehicleCode);

        textInputEditTextvehicleNumber = (EditText) findViewById(R.id.textInputEditTextvehicleNumber);
        textInputEditTextMobileNumber = (TextInputEditText) findViewById(R.id.textInputEditTextMobileNumber);
        textInputEditName = (TextInputEditText) findViewById(R.id.textInputEditName);
        textInputEditAddress = (TextInputEditText) findViewById(R.id.textInputEditAddress);
        textInputEditnfcnumber = (TextInputEditText) findViewById(R.id.textInputEditnfcnumber);
        textInputEditnumber = (TextInputEditText) findViewById(R.id.textInputEditnumber);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        twowheelerRadio = (RadioButton) findViewById(R.id.twowheelerRadio);
        threewheelerRadio = (RadioButton) findViewById(R.id.threewheelerRadio);
        fourwheelerRadio = (RadioButton) findViewById(R.id.fourwheelerRadio);
        bike = (ImageView) findViewById(R.id.bike);
        car = (ImageView) findViewById(R.id.car);
        threecar = (ImageView) findViewById(R.id.threecar);
        spinnerplan = (Spinner) findViewById(R.id.spinnerplan);
        spinnerDurationn = (Spinner) findViewById(R.id.spinnerDurationn);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Renew Card");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        //cardNo = AppConstants.getString(RenewCard.this, AppConstants.TAGID, "");

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
        parkingType1234 = sharedpref.getString("VehicleType1", "");

        String pType[] = parkingType.toString().split(",");


//        pType2 = pType[0];
//        pType4 = pType[1];

        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler")) {
            twowheelerRadio.setChecked(true);
            twowheelerRadio.setVisibility(View.VISIBLE);
            fourwheelerRadio.setVisibility(View.VISIBLE);
            threewheelerRadio.setVisibility(View.VISIBLE);
            if (parkingType1234.equals("2Wheeler")) {
                fourwheelerRadio.setVisibility(View.VISIBLE);
                threewheelerRadio.setVisibility(View.VISIBLE);
                car.setVisibility(View.GONE);
                threecar.setVisibility(View.GONE);
                VehicleType = parkingType1234;
                twowheelerRadio.setChecked(true);
            } else if (parkingType1234.equals("4Wheeler")) {
                threewheelerRadio.setVisibility(View.VISIBLE);
                threecar.setVisibility(View.GONE);
                twowheelerRadio.setVisibility(View.VISIBLE);
                bike.setVisibility(View.GONE);
                threecar.setVisibility(View.GONE);
                car.setVisibility(View.VISIBLE);
                VehicleType = parkingType1234;
                fourwheelerRadio.setChecked(true);
            } else if (parkingType1234.equals("3Wheeler")) {
                twowheelerRadio.setVisibility(View.VISIBLE);
                fourwheelerRadio.setVisibility(View.VISIBLE);
                bike.setVisibility(View.GONE);
                car.setVisibility(View.GONE);
                threecar.setVisibility(View.VISIBLE);
                VehicleType = parkingType1234;
                threewheelerRadio.setChecked(true);
            }
        } else if (parkingType.equals("2Wheeler,4Wheeler")) {
            //  twowheelerRadio.setChecked(true);
            twowheelerRadio.setVisibility(View.VISIBLE);
            fourwheelerRadio.setVisibility(View.VISIBLE);
            threewheelerRadio.setVisibility(View.GONE);
            if (parkingType1234.equals("2Wheeler")) {
                twowheelerRadio.setChecked(true);
                threecar.setVisibility(View.GONE);
                bike.setVisibility(View.VISIBLE);
                car.setVisibility(View.VISIBLE);
                threecar.setVisibility(View.GONE);
                twowheelerRadio.setVisibility(View.VISIBLE);
                fourwheelerRadio.setVisibility(View.VISIBLE);
                // threewheelerRadio.setVisibility(View.GONE);
            } else {
                fourwheelerRadio.setChecked(true);
                threecar.setVisibility(View.GONE);
                threecar.setVisibility(View.GONE);
                bike.setVisibility(View.VISIBLE);
                car.setVisibility(View.VISIBLE);
                threecar.setVisibility(View.GONE);
                twowheelerRadio.setVisibility(View.VISIBLE);
                fourwheelerRadio.setVisibility(View.VISIBLE);
                //threewheelerRadio.setVisibility(View.GONE);
            }
        } else if (parkingType.equals("2Wheeler")) {
            twowheelerRadio.setChecked(true);
            threecar.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            twowheelerRadio.setVisibility(View.VISIBLE);
            fourwheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.GONE);
        } else if (parkingType.equals("4Wheeler")) {
            fourwheelerRadio.setChecked(true);
            threecar.setVisibility(View.GONE);
            twowheelerRadio.setVisibility(View.GONE);
            fourwheelerRadio.setVisibility(View.VISIBLE);
            threewheelerRadio.setVisibility(View.GONE);
            bike.setVisibility(View.GONE);
            car.setVisibility(View.VISIBLE);
            threecar.setVisibility(View.GONE);
        } else if (parkingType.equals("3Wheeler")) {
            twowheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setChecked(true);
            fourwheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.VISIBLE);
            bike.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            threecar.setVisibility(View.VISIBLE);
        } else {
            // Log.e(TAG, "init: parkingType" + parkingType);
        }
       /* if (parkingType1234.equals("2Wheeler")) {
            fourwheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            threecar.setVisibility(View.GONE);
            VehicleType = parkingType1234;
            twowheelerRadio.setChecked(true);
        }
        if (parkingType1234.equals("4Wheeler")) {
            twowheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.GONE);
            bike.setVisibility(View.GONE);
            threecar.setVisibility(View.GONE);
            VehicleType = parkingType1234;
            fourwheelerRadio.setChecked(true);
        }
        if (parkingType1234.equals("3Wheeler")) {
            twowheelerRadio.setVisibility(View.GONE);
            fourwheelerRadio.setVisibility(View.GONE);
            bike.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            VehicleType = parkingType1234;
            threewheelerRadio.setChecked(true);
        }*/
        if (AppConstants.isInternetAvailable(RenewCard.this)) {
            // durationNumber.add("12 Months");
            for (int j = 1; j <= 12; j++) {
                System.out.println(j);
                durationNumber.add(j + " Months");
            }
            spinnerDurationn.setAdapter(new ArrayAdapter(RenewCard.this,
                    R.layout.my_spinner_style,
                    durationNumber));

            planService(parkingType1234);
        } else {
            Toast.makeText(RenewCard.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }


//        twowheelerRadio.setChecked(true);
//        VehicleType = parkingType1234;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twowheelerRadio) {
                    twowheelerRadio.setChecked(true);
                    VehicleType = "2Wheeler";
                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("VehicleType", VehicleType);
                    editor.apply();
                    editor.commit();

                    planService(VehicleType);
                }
                if (checkedId == R.id.fourwheelerRadio) {
                    fourwheelerRadio.setChecked(true);
                    VehicleType = "4Wheeler";
                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("VehicleType", VehicleType);
                    editor.apply();
                    editor.commit();

                    planService(VehicleType);
                }
                if (checkedId == R.id.threewheelerRadio) {
                    threewheelerRadio.setChecked(true);
                    VehicleType = "3Wheeler";
                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("VehicleType", VehicleType);
                    editor.apply();
                    editor.commit();

                    planService(VehicleType);
                }
            }
        });


        textInputEditnfcnumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
              /*  if (textInputEditnfcnumber.getText().toString().length())     //size as per your requirement
                {
                    textInputEditnfcnumber.requestFocus();

                }*/
                textInputEditnfcnumber.setSelection(textInputEditnfcnumber.getText().length());
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        textInputEditName.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (textInputEditName.getText().toString().length() == 0)     //size as per your requirement
                {
                    textInputEditnfcnumber.requestFocus();

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
        textInputEditTextStateCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (textInputEditTextStateCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    textInputEditTextCityCode.requestFocus();

                }
//                if (textInputEditTextStateCode.getText().toString().length() == 0)     //size as per your requirement
//                {
//                    textInputEditTextMobileNumber.requestFocus();
//
//                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        textInputEditTextCityCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (textInputEditTextCityCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    textInputEditTextVehicleCode.requestFocus();
                }
                if (textInputEditTextCityCode.getText().toString().length() == 0)     //size as per your requirement
                {
                    textInputEditTextStateCode.requestFocus();
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
        textInputEditTextVehicleCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (textInputEditTextVehicleCode.getText().toString().length() == 2)     //size as per your requirement
                {
                    textInputEditTextvehicleNumber.requestFocus();

                }
                if (textInputEditTextVehicleCode.getText().toString().length() == 0)     //size as per your requirement
                {
                    textInputEditTextCityCode.requestFocus();

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
        textInputEditTextvehicleNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (textInputEditTextvehicleNumber.getText().toString().length() == 4)     //size as per your requirement
                {
                    textInputEditTextMobileNumber.requestFocus();
                }
                if (textInputEditTextvehicleNumber.getText().toString().length() == 0)     //size as per your requirement
                {
                    textInputEditTextVehicleCode.requestFocus();
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
        textInputEditTextMobileNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (textInputEditTextMobileNumber.getText().toString().length() == 10)     //size as per your requirement
                {
                    textInputEditAddress.requestFocus();

                }
//                if (textInputEditTextMobileNumber.getText().toString().length() == 0)     //size as per your requirement
//                {
//                    textInputEditName.requestFocus();
//
//                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        textInputEditAddress.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
//                if (textInputEditAddress.getText().toString().length() == 10)     //size as per your requirement
//                {
//                    spinnerplan.requestFocus();
//                    AppConstants.hideKeyboard(Subscription.this, textInputEditTextvehicleNumber);
//                }
//                if (textInputEditAddress.getText().toString().length() == 0)     //size as per your requirement
//            {
//                // textInputEditTextvehicleNumber.requestFocus();
//                // AppConstants.hideKeyboard(Subscription.this, textInputEditTextvehicleNumber);
//
//            }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

    }


    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("ReNew Card");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//        textInputLayoutRenewCard = (TextInputLayout) findViewById(R.id.textInputLayoutRenewCard);
//        textInputReNewCard = (TextInputEditText) findViewById(R.id.textInputReNewCard);
//        appCompatButtonRenewCard = (AppCompatButton) findViewById(R.id.appCompatButtonRenewCard);
        spinnerplan = (Spinner) findViewById(R.id.spinnerplan);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        twowheelerRadio = (RadioButton) findViewById(R.id.twowheelerRadio);
        fourwheelerRadio = (RadioButton) findViewById(R.id.fourwheelerRadio);
        bike = (ImageView) findViewById(R.id.bike);
        car = (ImageView) findViewById(R.id.car);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twowheelerRadio) {
                    twowheelerRadio.setChecked(true);
                    VehicleType = "2Wheeler";
                }
                if (checkedId == R.id.fourwheelerRadio) {
                    fourwheelerRadio.setChecked(true);
                    VehicleType = "4Wheeler";
                }
            }
        });
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("parkingType", "");
        parkingId = sharedpref.getString("parkingId", "");

        parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType1234 = sharedpref.getString("VehicleType1", "");


        String pType[] = parkingType.toString().split(",");


//        pType2 = pType[0];
//        pType4 = pType[1];
        if (parkingType1234.equals("2Wheeler")) {
            fourwheelerRadio.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            VehicleType = parkingType1234;
            twowheelerRadio.setChecked(true);
        }
        if (parkingType1234.equals("4Wheeler")) {
            twowheelerRadio.setVisibility(View.GONE);
            bike.setVisibility(View.GONE);
            VehicleType = parkingType1234;
            fourwheelerRadio.setChecked(true);
        }
        if (AppConstants.isInternetAvailable(RenewCard.this)) {
            planService(parkingType1234);
        } else {
            Toast.makeText(RenewCard.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }


    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
//        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    public void planService(final String parkingType1234) {

        final ProgressDialog pDialog = new ProgressDialog(RenewCard.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        String urlData = AppConstants.BASEURL + "plan/list?parkingType=" + parkingType1234;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.d("Response", response + "");
                        plannames.clear();
                        plannames.add("Select Plan");
                        PopulateSpinnerPojo populateSpinnerPojo1 = new PopulateSpinnerPojo();
                        populateSpinnerPojo1.setPlanId("0");
                        populateSpinnerPojo1.setPlanName("Plane");
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

                                    String planId1 = jsonObject.getString("planId");
                                    String planName = jsonObject.getString("planName");
                                    // String price = jsonObject.getString("price");
                                    // String duration = jsonObject.getString("duration");

                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
                                    populateSpinnerPojo.setPlanId(planId1);
                                    populateSpinnerPojo.setPlanName(planName);
                                    //  populateSpinnerPojo.setPrice(price);
                                    //  populateSpinnerPojo.setDuration(duration);
                                    plannames.add(planName);
                                    //  duratioList.add(duration + " Months");
                                    if (!durationNumber.contains("1 Months")) {
                                        /*durationLits.clear();
                                        durationLits.add(" 1 Months");
                                        //durationLits.add(duration+" Months");
                                        spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this,
                                                R.layout.my_spinner_style,
                                                durationLits));*/
                                        //  durationNumber.add(duration + " Months");

                                    } else {

                                        spinnerDurationn.setAdapter(new ArrayAdapter(RenewCard.this,
                                                R.layout.my_spinner_style,
                                                durationNumber));
                                        if (durationNumber.equals("1 Months")) {
                                            TvPricee.setEnabled(false);
                                            TvPricee.setFocusable(false);
                                            TvPricee.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                            TvPricee.setClickable(false); // user navigates with wheel and selects widget

                                        } else {
                                            TvPricee.setEnabled(true);
                                            TvPricee.setFocusable(true);
                                            TvPricee.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                                            TvPricee.setClickable(true); // user navigates with wheel and selects widget

                                        }

                                    }
                                    planeList.add(populateSpinnerPojo);
                                }
                                spinnerplan.setAdapter(new ArrayAdapter(RenewCard.this,
                                        R.layout.my_spinner_style,
                                        plannames));
                               /* spinnerDurationn.setAdapter(new ArrayAdapter(RenewCard.this,
                                        R.layout.my_spinner_style,
                                        duratioList));*/
                                spinnerplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subPlanId = planeList.get(pos).getPlanId();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                                spinnerDurationn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subDurationId = durationNumber.get(pos);
                                        if (subDurationId.equals("1 Months")) {
                                            subDurationId="1 Months";
                                            linearPric.setVisibility(View.GONE);
                                            TvPricee.setEnabled(false);
                                            TvPricee.setFocusable(false);
                                            TvPricee.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                            TvPricee.setClickable(false); // user navigates with wheel and selects widget

                                        } else {
                                            linearPric.setVisibility(View.VISIBLE);

                                            TvPricee.setEnabled(true);
                                            TvPricee.setFocusable(true);
                                            TvPricee.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                                            TvPricee.setClickable(true); // user navigates with wheel and selects widget

                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                                pDialog.dismiss();
                            } else {

                                plannames.add(response1);
                                spinnerplan.setAdapter(new ArrayAdapter(RenewCard.this, R.layout.my_spinner_style, plannames));
                                spinnerDurationn.setAdapter(new ArrayAdapter(RenewCard.this, R.layout.my_spinner_style, durationNumber));
                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RenewCard.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        Toast.makeText(RenewCard.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
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

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(RenewCard.this, new Weipos.OnInitListener() {

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

    public void id(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("android.nfc.extra.ID");
        Appendable stringBuilder = new StringBuilder(byteArrayExtra.length * 2);
        Formatter formatter = new Formatter(stringBuilder);
        for (int length = byteArrayExtra.length - 1; length > -1; length--) {
            formatter.format("%02X", new Object[]{Byte.valueOf(byteArrayExtra[length])});
        }

        tagID = stringBuilder.toString();
        AppConstants.setString(RenewCard.this, AppConstants.TAGID, tagID);
        // Toast.makeText(this, tagID, Toast.LENGTH_LONG).show();
        nfcDetailForINOut(tagID);
        textInputEditnfcnumber.setText(tagID);

        formatter.close();

    }

    private void initNFC() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

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


        initSdk();
    }


    public void nfcDetailForINOut(String tagid) {
        // Toast.makeText(this, "tagid    " + tagid, Toast.LENGTH_LONG).show();

        final ProgressDialog pDialog = new ProgressDialog(RenewCard.this);
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
                        subscriptionModel.setAddress(checkINresponce.getString("address"));
                        subscriptionModel.setCardHolderName(checkINresponce.getString("cardHolderName"));
                        subscriptionModel.setMobileNo(checkINresponce.getString("mobileNo"));
                        subscriptionModel.setPassNumber(checkINresponce.getString("passNo"));
                        String pass = checkINresponce.getString("passNo");
                        subVehicleNo = subscriptionModel.getVehicleNo();

                        String[] totalsubVehicleNo = subVehicleNo.toString().split(",");

                        String StateCode = totalsubVehicleNo[0];
                        String CityCode = totalsubVehicleNo[1];
                        String VehicleCode = totalsubVehicleNo[2];
                        String vehicleNumber = totalsubVehicleNo[3];

                        textInputEditTextStateCode.setText(StateCode);
                        textInputEditTextCityCode.setText(CityCode);
                        textInputEditTextVehicleCode.setText(VehicleCode);
                        textInputEditTextvehicleNumber.setText(vehicleNumber);
                        textInputEditnumber.setText(pass);

                        textInputEditTextMobileNumber.setText(checkINresponce.getString("mobileNo"));
                        textInputEditName.setText(checkINresponce.getString("cardHolderName"));
                        textInputEditAddress.setText(checkINresponce.getString("address"));


                        textInputEditTextMobileNumber.setEnabled(false);
                        textInputEditnumber.setEnabled(false);
                        textInputEditName.setEnabled(false);
                        textInputEditAddress.setEnabled(false);

                        textInputEditTextStateCode.setEnabled(false);
                        textInputEditTextCityCode.setEnabled(false);
                        textInputEditTextvehicleNumber.setEnabled(false);
                        textInputEditTextVehicleCode.setEnabled(false);
                        textInputEditnfcnumber.setEnabled(false);
                        textInputEditnfcnumber.setSelection(textInputEditnfcnumber.getText().length());

                        textInputEditnfcnumber.setText(tagID);

                        Toast.makeText(RenewCard.this, message, Toast.LENGTH_SHORT).show();

                    } else {

                        JSONObject checkINresponce = response.getJSONObject("data");
                        String message1 = String.valueOf(response.get("message"));
                        if (message1.equals("Card Not Exist !")) {
                            textInputEditTextStateCode.setEnabled(false);
                            textInputEditTextCityCode.setEnabled(false);
                            textInputEditTextvehicleNumber.setEnabled(false);
                            textInputEditTextVehicleCode.setEnabled(false);
                            textInputEditName.setEnabled(false);
                            textInputEditAddress.setEnabled(false);
                            textInputEditTextMobileNumber.setEnabled(false);
                            textInputEditnumber.setEnabled(false);
                            textInputEditnfcnumber.setText("");
                            textInputEditnumber.setText("");
                            Toast.makeText(RenewCard.this, message1, Toast.LENGTH_SHORT).show();
                        } else {
                            SubscriptionModel subscriptionModel = new SubscriptionModel();

                        /*subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subscriptionModel.setAgentId(checkINresponce.getString("agentId"));*/
                            subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                            subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                            subscriptionModel.setAddress(checkINresponce.getString("address"));
                            subscriptionModel.setCardHolderName(checkINresponce.getString("cardHolderName"));
                            subscriptionModel.setMobileNo(checkINresponce.getString("mobileNo"));
                            subVehicleNo = subscriptionModel.getVehicleNo();
                            String pass = checkINresponce.getString("passNo");

                            String[] totalsubVehicleNo = subVehicleNo.toString().split(",");

                            String StateCode = totalsubVehicleNo[0];
                            String CityCode = totalsubVehicleNo[1];
                            String VehicleCode = totalsubVehicleNo[2];
                            String vehicleNumber = totalsubVehicleNo[3];

                            textInputEditTextStateCode.setText(StateCode);
                            textInputEditTextCityCode.setText(CityCode);
                            textInputEditTextVehicleCode.setText(VehicleCode);
                            textInputEditTextvehicleNumber.setText(vehicleNumber);
                            textInputEditTextMobileNumber.setText(checkINresponce.getString("mobileNo"));
                            textInputEditName.setText(checkINresponce.getString("cardHolderName"));
                            textInputEditAddress.setText(checkINresponce.getString("address"));

                            textInputEditTextMobileNumber.setEnabled(false);
                            textInputEditnumber.setEnabled(false);
                            textInputEditName.setEnabled(false);
                            textInputEditAddress.setEnabled(false);

                            textInputEditTextStateCode.setEnabled(false);
                            textInputEditTextCityCode.setEnabled(false);
                            textInputEditTextvehicleNumber.setEnabled(false);
                            textInputEditTextVehicleCode.setEnabled(false);
                            textInputEditnfcnumber.setEnabled(false);
                            textInputEditnfcnumber.setSelection(textInputEditnfcnumber.getText().length());
                            textInputEditnumber.setText(pass);
                            textInputEditnumber.setSelection(textInputEditnfcnumber.getText().length());

                        }

                        // Toast.makeText(RenewCard.this, message, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(RenewCard.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RenewCard.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(RenewCard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(RenewCard.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString() + "\n parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) {
                    sendError(error.toString() + "\n parking/pass_detail?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString() + "\n parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString() + "\n parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString() + "\n parking/pass_detail?cardNo=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString() + "\n parking/pass_detail?cardNo=");

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
            Toast.makeText(RenewCard.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RenewCard.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(RenewCard.this, printer);

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

        printer.printText(passHeading, Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                Printer.Gravity.LEFT);
        printer.printText(ParkingName, Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                Printer.Gravity.LEFT);
        printer.printText(AgentName, Printer.FontFamily.SONG,
                Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                Printer.Gravity.LEFT);
        printer.printText("---------------", Printer.FontFamily.SONG,
                Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                Printer.Gravity.CENTER);

        printer.printText(holderName,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);


        printer.printText(subVehicleNo,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

        printer.printText(subCardNo,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

        printer.printText(renewableDate,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

        printer.printText(plan,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

        printer.printText("---------------", Printer.FontFamily.SONG,
                Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                Printer.Gravity.CENTER);

        printer.printText(total,
                Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
        printer.printText("\n", Printer.FontFamily.SONG,
                Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                Printer.Gravity.CENTER);


        textInputEditnfcnumber.setText("");
        textInputEditName.setText("");
        textInputEditTextStateCode.setText("");
        textInputEditTextCityCode.setText("");
        textInputEditTextVehicleCode.setText("");
        textInputEditTextvehicleNumber.setText("");
        textInputEditTextMobileNumber.setText("");
        textInputEditAddress.setText("");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                verifyFromSQLite();
                break;
        }
    }

    private void verifyFromSQLite() {

        cardNo = textInputEditnfcnumber.getText().toString().trim();
        cardNo1 = textInputEditnumber.getText().toString().trim();

       /* if (Price.equals("")) {
            TvPricee.setError(getString(R.string.error_message_price));
            TvPricee.requestFocus();
            return;
        }*/
        /*if (subDurationId.equals("1 Months")) {

        } else {
            if (Price.equals("")) {
                TvPricee.setError(getString(R.string.error_message_price));
                TvPricee.requestFocus();
                return;
            }
        }*/
        if (cardNo.equals("")) {
            textInputEditnfcnumber.setError("Enter NFS Card Number");
            textInputEditnfcnumber.requestFocus();
            return;

        } else if (cardNo1.equals("")) {
            textInputEditnumber.setError("Enter Card Number");
            textInputEditnumber.requestFocus();
        } else {
            if (setSpinnerError(spinnerplan, "Select Plan")) {
                Price = TvPricee.getText().toString().trim();
                sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                String VehiType = sharedpref.getString("VehicleType", "");


                        String url = AppConstants.BASEURL + "pass/renew";
                        Map<String, String> parameterData = new HashMap<>();
                        parameterData.put(("parkingId"), parkingId);
                        parameterData.put(("agentId"), agentId);
                        parameterData.put(("cardNo"), cardNo);
                        parameterData.put(("passNo"), cardNo1);
                        parameterData.put(("planId"), subPlanId);
                        parameterData.put(("duration"), subDurationId);
                        parameterData.put(("price"), Price);
                        //jsonObjectRequestRegister(url, parameterData);
                        if (AppConstants.isInternetAvailable(RenewCard.this)) {
                            registerTagId(url, parameterData);
                            Log.e("1111", "verifyFromSQLite:parameterData " + parameterData);
                            Log.e("11111", "verifyFromSQLite:url " + url);
                        } else {
                            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                        }
                   /* } else {
                        String url = AppConstants.BASEURL + "pass/renew";
                        Map<String, String> parameterData = new HashMap<>();
                        parameterData.put(("parkingId"), parkingId);
                        parameterData.put(("agentId"), agentId);
                        parameterData.put(("cardNo"), cardNo);
                        parameterData.put(("passNo"), cardNo1);
                        parameterData.put(("planId"), subPlanId);
                        parameterData.put(("duration"), subDurationId);
                        parameterData.put(("price"), Price);
                        //jsonObjectRequestRegister(url, parameterData);
                        if (AppConstants.isInternetAvailable(RenewCard.this)) {
                            registerTagId(url, parameterData);
                            Log.e("1111", "verifyFromSQLite:parameterData " + parameterData);
                            Log.e("11111", "verifyFromSQLite:url " + url);
                        } else {
                            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                        }
                    }*/

            }
        }


    }


    public void registerTagId(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(RenewCard.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectRegister(jsonObject);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    sendError(volleyError.toString());
                    //Toast.makeText(getActivity(), "error==>  " + volleyError.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(RenewCard.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());

            sendError(e.toString());
            Toast.makeText(RenewCard.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }
    }

    private void processJsonObjectRegister(JSONObject response) {

        if (response != null) {
            Log.e("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");
                Log.e("111111111111111111111", "processJsonObjectRegister:status " + status);
                if (status == 0) {

                    JSONObject checkINresponce = response.getJSONObject("data");
                    SubscriptionModel subscriptionModel = new SubscriptionModel();

                    subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                    subscriptionModel.setAgentId(checkINresponce.getString("agentId"));
                    subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                    subscriptionModel.setHolderName(checkINresponce.getString("holderName"));
                    subscriptionModel.setPassHeading(checkINresponce.getString("passHeading"));
                    subscriptionModel.setPlan(checkINresponce.getString("plan"));
                    subscriptionModel.setTotal(checkINresponce.getString("total"));
                    subscriptionModel.setRenewableDate(checkINresponce.getString("renewableDate"));
                    subscriptionModel.setParkingName(checkINresponce.getString("parkingName"));
                    subscriptionModel.setAgentName(checkINresponce.getString("agentName"));

                    subCardNo = subscriptionModel.getCardNo();
                    subAgentNo = subscriptionModel.getAgentId();
                    subVehicleNo = subscriptionModel.getVehicleNo();
                    renewableDate = subscriptionModel.getRenewableDate();
                    total = subscriptionModel.getTotal();
                    plan = subscriptionModel.getPlan();
                    passHeading = subscriptionModel.getPassHeading();
                    holderName = subscriptionModel.getHolderName();
                    AgentName = subscriptionModel.getAgentName();
                    ParkingName = subscriptionModel.getParkingName();

                    //cleareText();
                    printReceipt();
                    Toast.makeText(RenewCard.this, message, Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(RenewCard.this, message, Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();

                }


            } catch (NullPointerException e) {
                Toast.makeText(RenewCard.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RenewCard.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
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
                selectedTextView.setText("Select Plan"); // actual error message
                spinner.performClick(); // to open the spinner list if error is found.
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reprintreceipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.Paytm:
                Intent reprintIntent = new Intent(RenewCard.this, ReprintReceipt.class);
                startActivity(reprintIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            default:
        }
        return super.onOptionsItemSelected(item);

    }

    private void sendError(String e) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());

        if (AppConstants.isInternetAvailable(RenewCard.this)) {
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

                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                // pDialog.dismiss();
            }
        }
    }
}
