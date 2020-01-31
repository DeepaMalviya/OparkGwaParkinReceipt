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
import android.support.v7.widget.AppCompatTextView;
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
import attender.oparkReceipt.booking.CheckInModel;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.InputValidation;
import attender.oparkReceipt.subscription.model.PopulateSpinnerPojo;
import attender.oparkReceipt.subscription.model.SubscriptionModel;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;


public class Subscription extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolBar;
    private TextView textToolHeader;
    String tagID, lostPass = "";
    EditText TvPrice;
    LinearLayout linearPrice;
    public static final String TAG = Subscription.class.getSimpleName();
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private NfcAdapter mNfcAdapter;

    private NestedScrollView nestedScrollView;

    //    private TextInputLayout textInputLayoutName;
//    private TextInputLayout textInputLayoutEmail;
//    private TextInputLayout textInputLayoutPassword;
//    private TextInputLayout textInputLayoutConfirmPassword;
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

    private AppCompatButton appCompatButtonRegister, appCompatButton;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;
    String Price, cardNo;
    private String vehicleNumber = "";
    private String StateCode = "";
    private String CityCode = "";
    private String VehicleCode = "";
    private String VehicleType = "";
    private String subCardNo = "";
    private String subAgentNo = "";
    private String subVehicleNo = "";
    private String address = "";
    ImageView bike, car, threecar;
    SharedPreferences sharedpref;
    String agentId, userRole, userName, userContactNo, vendorId, vendorName, parkingName, parkingType, parkingId, vehicleNo, AgentName, ParkingName, holderName, passHeading, plan, total, renewableDate,
            pType2, pType4, parkingVehicle, sidefour, userType, getParkingID, parkingType1234, name, mobileNumber, subDurationId, subPlanId, planename, varient1, cardNumber;
    RadioGroup radioGroup;
    RadioButton twowheelerRadio, threewheelerRadio, fourwheelerRadio;

    Spinner spinnerDuration, spinnerplan;
    ArrayList<String> planSelection = new ArrayList<>();
    ArrayList<String> planlId = new ArrayList<>();
    ArrayList<String> plannames = new ArrayList<>();
    ArrayList<String> durationLits = new ArrayList<>();
    ArrayList<String> durationNumber = new ArrayList<>();

    ArrayList<PopulateSpinnerPojo> planeList = new ArrayList<>();
    private ServiceManager mServiceManager = null;
    Printer printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        printer = WeiposImpl.as().openPrinter();

        initViews();
        initListeners();
        initObjects();
        initNFC();
        initSdk();

        // textInputEditName.requestFocus();
        textInputEditnfcnumber.requestFocus();

    }

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(Subscription.this, new Weipos.OnInitListener() {

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

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

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
        textInputEditnumber = (TextInputEditText) findViewById(R.id.textInputEditnumber);
        textInputEditAddress = (TextInputEditText) findViewById(R.id.textInputEditAddress);
        textInputEditnfcnumber = (TextInputEditText) findViewById(R.id.textInputEditnfcnumber);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        appCompatButton = (AppCompatButton) findViewById(R.id.appCompatButton);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        twowheelerRadio = (RadioButton) findViewById(R.id.twowheelerRadio);
        threewheelerRadio = (RadioButton) findViewById(R.id.threewheelerRadio);
        fourwheelerRadio = (RadioButton) findViewById(R.id.fourwheelerRadio);
        bike = (ImageView) findViewById(R.id.bike);
        car = (ImageView) findViewById(R.id.car);
        threecar = (ImageView) findViewById(R.id.threecar);
        spinnerplan = (Spinner) findViewById(R.id.spinnerplan);
        spinnerDuration = (Spinner) findViewById(R.id.spinnerDuration);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        TvPrice = (EditText) findViewById(R.id.TvPrice);
        linearPrice = findViewById(R.id.linearPrice);
        textToolHeader.setText("Add Card");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        cardNo = AppConstants.getString(Subscription.this, AppConstants.TAGID, "");

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        // parkingType = sharedpref.getString("parkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        parkingType = sharedpref.getString("getparkingType", "");
        parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType1234 = sharedpref.getString("VehicleType1", "");
        getParkingID = sharedpref.getString("getParkingID", "");
        Log.e(TAG, "initViews: parkingType1234=======" + parkingType1234);
        Log.e(TAG, "initViews: parkingType=======" + parkingType);
        Log.e(TAG, "initViews: parkingId=======" + parkingId);
        String pType[] = parkingType.toString().split(",");


//        pType2 = pType[0];
//        pType4 = pType[1];
        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler")) {
            twowheelerRadio.setChecked(true);
            twowheelerRadio.setVisibility(View.VISIBLE);
            fourwheelerRadio.setVisibility(View.VISIBLE);
            threewheelerRadio.setVisibility(View.VISIBLE);
            if (parkingType1234.equals("2Wheeler")) {
                fourwheelerRadio.setVisibility(View.GONE);
                threewheelerRadio.setVisibility(View.GONE);
                car.setVisibility(View.GONE);
                threecar.setVisibility(View.GONE);
                VehicleType = parkingType1234;
                twowheelerRadio.setChecked(true);
            } else if (parkingType1234.equals("4Wheeler")) {
                threewheelerRadio.setVisibility(View.GONE);
                threecar.setVisibility(View.GONE);
                twowheelerRadio.setVisibility(View.GONE);
                bike.setVisibility(View.GONE);
                VehicleType = parkingType1234;
                fourwheelerRadio.setChecked(true);
            } else {
                twowheelerRadio.setVisibility(View.GONE);
                fourwheelerRadio.setVisibility(View.GONE);
                bike.setVisibility(View.GONE);
                car.setVisibility(View.GONE);
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
                twowheelerRadio.setVisibility(View.VISIBLE);
                fourwheelerRadio.setVisibility(View.VISIBLE);
                // threewheelerRadio.setVisibility(View.GONE);
            } else {
                fourwheelerRadio.setChecked(true);
                threecar.setVisibility(View.GONE);
                twowheelerRadio.setVisibility(View.VISIBLE);
                fourwheelerRadio.setVisibility(View.VISIBLE);
                //threewheelerRadio.setVisibility(View.GONE);
            }
        } else if (parkingType.equals("2Wheeler")) {
            twowheelerRadio.setChecked(true);
            threecar.setVisibility(View.GONE);
            twowheelerRadio.setVisibility(View.VISIBLE);
            fourwheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.GONE);
        } else if (parkingType.equals("4Wheeler")) {
            fourwheelerRadio.setChecked(true);
            threecar.setVisibility(View.GONE);
            twowheelerRadio.setVisibility(View.GONE);
            fourwheelerRadio.setVisibility(View.VISIBLE);
            threewheelerRadio.setVisibility(View.GONE);
        } else if (parkingType.equals("3Wheeler")) {
            twowheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setChecked(true);
            fourwheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.VISIBLE);
        } else {
            Log.e(TAG, "init: parkingType" + parkingType);
        }
      /*  if (parkingType1234.equals("2Wheeler")) {
            fourwheelerRadio.setVisibility(View.GONE);
            threewheelerRadio.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            threecar.setVisibility(View.GONE);
            VehicleType = parkingType1234;
            twowheelerRadio.setChecked(true);
        }
        if (parkingType1234.equals("4Wheeler")) {
            threewheelerRadio.setVisibility(View.GONE);
            threecar.setVisibility(View.GONE);
            twowheelerRadio.setVisibility(View.GONE);
            bike.setVisibility(View.GONE);
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
        if (AppConstants.isInternetAvailable(Subscription.this)) {
            //  durationNumber.add("Select Duration");
            //durationNumber.add("1 Months");
            for (int j = 1; j <= 12; j++) {
                System.out.println(j);

                durationNumber.add(j + " Months");
                Log.e(TAG, "initViews:durationNumber " + j);

            }
            spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this,
                    R.layout.my_spinner_style,
                    durationNumber));
            Log.e(TAG, "initViews:parkingType1234 " + parkingType1234);

            planService(parkingType1234);

        } else {
            Toast.makeText(Subscription.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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
                    editor.putString("VehicleType1", VehicleType);
                    editor.apply();
                    editor.commit();

                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                    String VehicleType1 = sharedpref.getString("VehicleType1", "");
                    String VehiType = sharedpref.getString("VehicleType", "");
                    Log.e(TAG, "verifyFromSQLite:VehiType "+VehiType );
                    Log.e(TAG, "verifyFromSQLite:VehicleType1 "+VehicleType1 );
                    planService(VehicleType);
                }
                if (checkedId == R.id.fourwheelerRadio) {
                    fourwheelerRadio.setChecked(true);
                    VehicleType = "4Wheeler";
                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("VehicleType1", VehicleType);
                    editor.apply();
                    editor.commit();
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                    String VehicleType1 = sharedpref.getString("VehicleType1", "");
                    String VehiType = sharedpref.getString("VehicleType", "");
                    Log.e(TAG, "verifyFromSQLite:VehiType "+VehiType );
                    Log.e(TAG, "verifyFromSQLite:VehicleType1 "+VehicleType1 );
                    planService(VehicleType);
                }
                if (checkedId == R.id.threewheelerRadio) {
                    threewheelerRadio.setChecked(true);
                    VehicleType = "3Wheeler";
                    SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeAllValues.edit();
                    editor.putString("VehicleType1", VehicleType);
                    editor.apply();
                    editor.commit();
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                    String VehicleType1 = sharedpref.getString("VehicleType1", "");
                    String VehiType = sharedpref.getString("VehicleType", "");
                    Log.e(TAG, "verifyFromSQLite:VehiType "+VehiType );
                    Log.e(TAG, "verifyFromSQLite:VehicleType1 "+VehicleType1 );
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

    private void updateSpinnerData(String vehicleType) {

    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatButton.setOnClickListener(this);

//        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    private void initObjects() {
        inputValidation = new InputValidation(Subscription.this);
       /* databaseHelper = new DatabaseHelper(activity);
        user = new User();*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                verifyFromSQLite();
                break;
            case R.id.appCompatButton:
                updateNfcCard();

        }
    }

    private void updateNfcCard() {

        StateCode = textInputEditTextStateCode.getText().toString().trim();
        VehicleCode = textInputEditTextVehicleCode.getText().toString().trim();
        CityCode = textInputEditTextCityCode.getText().toString().trim();
        vehicleNumber = textInputEditTextvehicleNumber.getText().toString().trim();
        cardNumber = textInputEditnumber.getText().toString().trim();
        cardNo = textInputEditnfcnumber.getText().toString().trim();
        vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        lostPass = "NFC";
        Price = TvPrice.getText().toString().trim();

        if (!inputValidation.isInputEditTextFilled(textInputEditnfcnumber, textInputLayoutnfcnumber, "Enter NFS Card Number")) {
//            if (AppConstants.isBlank(cardNo)) {
//                textInputEditnfcnumber.setError();
//                textInputEditnfcnumber.requestFocus();
//            }
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditnumber, textInputLayoutnumber, "Enter Pass Number")) {
            //  textInputEditnumber.setError("Enter Pass Number");
            textInputEditnumber.requestFocus();
            return;
        }

        if (StateCode.equals("")) {
            textInputEditTextStateCode.setError(getString(R.string.error_message_code));
            textInputEditTextStateCode.requestFocus();
            return;
        }
        if (CityCode.equals("")) {
            textInputEditTextCityCode.setError(getString(R.string.error_message_citycode));
            textInputEditTextCityCode.requestFocus();
            return;
        }
        if (VehicleCode.equals("")) {
            textInputEditTextVehicleCode.setError(getString(R.string.error_valid_email_password));
            textInputEditTextVehicleCode.requestFocus();
            return;
        }

        if (vehicleNumber.equals("")) {
            textInputEditTextvehicleNumber.setError(getString(R.string.error_message_email));
            textInputEditTextvehicleNumber.requestFocus();
            return;
        } else if (setSpinnerError(spinnerplan, "Select Plan")) {

            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

            String VehicleType1 = sharedpref.getString("VehicleType1", "");
            String VehiType = sharedpref.getString("VehicleType", "");
            Log.e(TAG, "verifyFromSQLite:VehiType "+VehiType );
            Log.e(TAG, "verifyFromSQLite:VehicleType1 "+VehicleType1 );
            if (VehiType.equals("")) {
//https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/parking/pass_lost
                String url = AppConstants.BASEURL + "pass/lost";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), VehicleType1);
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("cardNo"), cardNo);
                parameterData.put(("passNo"), cardNumber);
                parameterData.put(("planId"), subPlanId);
                parameterData.put(("duration"), subDurationId);
                parameterData.put(("price"), Price);

                // parkingId,parkingType,vehicleNo,agentId,cardNo,planId,passNo

                // jsonObjectRequestRegister(url, parameterData);
                if (AppConstants.isInternetAvailable(Subscription.this)) {
                    registerLostCard(url, parameterData);
                    Log.e(TAG, "updateNfcCard:parameterData " + parameterData);
                    Log.e(TAG, "updateNfcCard:url " + url);
                } else {
                    Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                }
            }else {
                String url = AppConstants.BASEURL + "pass/lost";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), VehiType);
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("cardNo"), cardNo);
                parameterData.put(("passNo"), cardNumber);
                parameterData.put(("planId"), subPlanId);
                parameterData.put(("duration"), subDurationId);
                parameterData.put(("price"), Price);

                // parkingId,parkingType,vehicleNo,agentId,cardNo,planId,passNo

                // jsonObjectRequestRegister(url, parameterData);
                if (AppConstants.isInternetAvailable(Subscription.this)) {
                    registerLostCard(url, parameterData);
                    Log.e(TAG, "updateNfcCard:parameterData " + parameterData);
                    Log.e(TAG, "updateNfcCard:url " + url);
                } else {
                    Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                }

            }




        }
    }

    public void registerLostCard(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(Subscription.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectLostCardRegister(jsonObject);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    pDialog.dismiss();
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "pass/lost");
                        Toast.makeText(Subscription.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "pass/lost");
                        Toast.makeText(Subscription.this,
                                "Oops. Server error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "pass/lost");
                        Toast.makeText(Subscription.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "pass/lost");
                        Toast.makeText(Subscription.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "pass/lost");
                        Toast.makeText(Subscription.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "pass/lost");
                        Toast.makeText(Subscription.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            sendError(e.toString(), "pass/lost");
            pDialog.dismiss();
        }
    }

    private void processJsonObjectLostCardRegister(JSONObject response) {

        if (response != null) {
            Log.e("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {
                    Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();

                    JSONObject checkINresponce = response.getJSONObject("data");
                    SubscriptionModel subscriptionModel = new SubscriptionModel();

                    subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                    subscriptionModel.setAgentId(checkINresponce.getString("agentId"));
                    subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                    subscriptionModel.setHolderName(checkINresponce.getString("holderName"));
                    subscriptionModel.setPassHeading(checkINresponce.getString("passHeading"));
                    subscriptionModel.setPlan(checkINresponce.getString("plan"));
                    subscriptionModel.setTotal(checkINresponce.getString("total"));
                    // subscriptionModel.setRenewableDate(checkINresponce.getString("renewableDate"));
                    subscriptionModel.setParkingName(checkINresponce.getString("parkingName"));
                    subscriptionModel.setAgentName(checkINresponce.getString("agentName"));

                    subCardNo = subscriptionModel.getCardNo();
                    subAgentNo = subscriptionModel.getAgentId();
                    subVehicleNo = subscriptionModel.getVehicleNo();
                    Log.e(TAG, "processJsonObjectLostCardRegister:subVehicleNo "+subVehicleNo );
                    // renewableDate = subscriptionModel.getRenewableDate();
                    total = subscriptionModel.getTotal();
                    plan = subscriptionModel.getPlan();
                    passHeading = subscriptionModel.getPassHeading();
                    holderName = subscriptionModel.getHolderName();
                    AgentName = subscriptionModel.getAgentName();
                    ParkingName = subscriptionModel.getParkingName();

                    cleareText();
                    printReceipt();
                    Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();

                }


            } catch (NullPointerException e) {
                Toast.makeText(Subscription.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                sendError(e.toString(), "pass/lost");
                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                sendError(e.toString(), "pass/lost");
                Toast.makeText(Subscription.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
    }


    public void cleareText() {
        textInputEditTextStateCode.setText("");
        textInputEditTextVehicleCode.setText("");
        textInputEditTextCityCode.setText("");
        textInputEditTextvehicleNumber.setText("");
        textInputEditTextMobileNumber.setText("");
        textInputEditAddress.setText("");
        textInputEditName.setText("");
        textInputEditnfcnumber.setText("");
        textInputEditnumber.setText("");


        //plannames.add("Select Plan");
        //plannames.clear();
       // durationLits.add("Select Duration");
        //durationLits.clear();


    }

    private void showAlert(String message) {
        AppConstants.showToast(Subscription.this, message);
    }

    private void verifyFromSQLite() {
        StateCode = textInputEditTextStateCode.getText().toString().trim();
        VehicleCode = textInputEditTextVehicleCode.getText().toString().trim();
        CityCode = textInputEditTextCityCode.getText().toString().trim();
        vehicleNumber = textInputEditTextvehicleNumber.getText().toString().trim();
        name = textInputEditName.getText().toString().trim();
        mobileNumber = textInputEditTextMobileNumber.getText().toString().trim();
        address = textInputEditAddress.getText().toString().trim();
        cardNumber = textInputEditnumber.getText().toString().trim();
        cardNo = textInputEditnfcnumber.getText().toString().trim();
        Price = TvPrice.getText().toString().trim();
        vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;

        if (!inputValidation.isInputEditTextFilled(textInputEditnfcnumber, textInputLayoutnfcnumber, "Enter NFS Card Number")) {
//            if (AppConstants.isBlank(cardNo)) {
//                textInputEditnfcnumber.setError();
//                textInputEditnfcnumber.requestFocus();
//            }
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditnumber, textInputLayoutnumber, "Enter Pass Number")) {
            //  textInputEditnumber.setError("Enter Pass Number");
            textInputEditnumber.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditName, textInputLayout1Name, "Enter Name")) {
            // textInputEditName.setError(getString(R.string.error_message_name));
            textInputEditName.requestFocus();
            return;
        }

        if (subDurationId.equals("1 Months")) {

        } else {
            if (Price.equals("")) {
                TvPrice.setError(getString(R.string.error_message_price));
                TvPrice.requestFocus();
                return;
            }
        }


        if (StateCode.equals("")) {
            textInputEditTextStateCode.setError(getString(R.string.error_message_code));
            textInputEditTextStateCode.requestFocus();
            return;
        }
        if (CityCode.equals("")) {
            textInputEditTextCityCode.setError(getString(R.string.error_message_citycode));
            textInputEditTextCityCode.requestFocus();
            return;
        }
        if (VehicleCode.equals("")) {
            textInputEditTextVehicleCode.setError(getString(R.string.error_valid_email_password));
            textInputEditTextVehicleCode.requestFocus();
            return;
        }

        if (vehicleNumber.equals("")) {
            textInputEditTextvehicleNumber.setError(getString(R.string.error_message_email));
            textInputEditTextvehicleNumber.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextMobileNumber, textInputLayoutMobileNumber, "Enter Mobile Number")) {
            textInputEditTextMobileNumber.setError(getString(R.string.error_message_mobile));
            textInputEditTextMobileNumber.requestFocus();
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditAddress, textInputLayoutAddress, "Enter Address")) {
            textInputEditAddress.setError("Enter Address");
            textInputEditAddress.requestFocus();
            return;
        } else if (setSpinnerError(spinnerplan, "Select Plan")) {
            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

            parkingType1234 = sharedpref.getString("VehicleType1", "");
            if (parkingType1234.equals("")) {
                String url = AppConstants.BASEURL + "pass/add";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), parkingType1234);
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("cardNo"), cardNo);
                parameterData.put(("passNo"), cardNumber);
                parameterData.put(("name"), name);
                parameterData.put(("mobileNo"), mobileNumber);
                parameterData.put(("address"), address);
                parameterData.put(("planId"), subPlanId);
                parameterData.put(("duration"), subDurationId);
                parameterData.put(("price"), Price);

                Log.e(TAG, "verifyFromSQLite:if " + subDurationId.replace(" ", ""));
                // parkingId,parkingType,vehicleNo,agentId,cardNo,planId,passNo

                // jsonObjectRequestRegister(url, parameterData);
                if (AppConstants.isInternetAvailable(Subscription.this)) {
                    registerTagId(url, parameterData);
                    Log.e(TAG, "registerTagId:parameterData if " + parameterData);
                    Log.e(TAG, "registerTagId:url if " + url);
                } else {
                    Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                }
            } else {
                String url = AppConstants.BASEURL + "pass/add";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("parkingId"), parkingId);
                parameterData.put(("parkingType"), parkingType1234);
                parameterData.put(("vehicleNo"), vehicleNo);
                parameterData.put(("agentId"), agentId);
                parameterData.put(("cardNo"), cardNo);
                parameterData.put(("passNo"), cardNumber);
                parameterData.put(("name"), name);
                parameterData.put(("mobileNo"), mobileNumber);
                parameterData.put(("address"), address);
                parameterData.put(("planId"), subPlanId);
                parameterData.put(("duration"), subDurationId);
                parameterData.put(("price"), Price);
                Log.e(TAG, "verifyFromSQLite: " + subDurationId.replace(" ", ""));
                // parkingId,parkingType,vehicleNo,agentId,cardNo,planId,passNo

                // jsonObjectRequestRegister(url, parameterData);
                if (AppConstants.isInternetAvailable(Subscription.this)) {
                    registerTagId(url, parameterData);
                    Log.e(TAG, "registerTagId:parameterData " + parameterData);
                    Log.e(TAG, "registerTagId:url " + url);
                } else {
                    Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                }
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

    public void registerTagId(String url, final Map<String, String> params) {
        Log.e(TAG, "registerTagId: ");
        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(Subscription.this);
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
                    pDialog.dismiss();
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "pass/add");
                        Toast.makeText(Subscription.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "pass/add");
                        Toast.makeText(Subscription.this,
                                "Oops. Server error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "pass/add");
                        Toast.makeText(Subscription.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "pass/add");
                        Toast.makeText(Subscription.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "pass/add");
                        Toast.makeText(Subscription.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "pass/add");
                        Toast.makeText(Subscription.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            sendError(e.toString(), "pass/add");
            pDialog.dismiss();
        }
    }

    private void processJsonObjectRegister(JSONObject response) {
        Log.e(TAG, "processJsonObjectRegister: ");
        if (response != null) {
            Log.e("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");
                Log.e(TAG, "processJsonObjectRegister: status" + status);
                if (status == 0) {

                    Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();

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
                    try {
                        AgentName = subscriptionModel.getAgentName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ParkingName = subscriptionModel.getParkingName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cleareText();
                    printReceipt();
                    Log.e(TAG, "processJsonObjectRegister:message if" + message);
                } else {
                    Log.e(TAG, "processJsonObjectRegister:message else" + message);
                    Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();

                }


            } catch (NullPointerException e) {
                Toast.makeText(Subscription.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                sendError(e.toString(), "pass/add");
                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "processJsonObjectRegister: "+ e.getMessage() );

                sendError(e.toString(), "pass/add");
                Toast.makeText(Subscription.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
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
        AppConstants.setString(Subscription.this, AppConstants.TAGID, tagID);
        // Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        //nfcDetailForINOut(tagID);
        textInputEditnfcnumber.setText(tagID);
        textInputEditnfcnumber.setEnabled(false);


        Log.e(TAG, "id:tagID-- " + tagID);
        formatter.close();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        if (tag != null) {
            // Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);


        }
        if (intent.getByteArrayExtra("android.nfc.extra.ID") != null) {
            Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);

            id(intent);
        }
    }

    private void initNFC() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    public void planService(final String parkingType1234) {
        final ProgressDialog pDialog = new ProgressDialog(Subscription.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        String urlData = AppConstants.BASEURL + "plan/list?parkingType=" + parkingType1234;
        Log.e(TAG, "planService:urlData=== " + urlData);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.e("Response", response + "");
                        plannames.clear();
                        planlId.clear();
                      //  plannames.add("Select Plan");
                        durationLits.add("Select Duration");

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
                                    //String duration = jsonObject.getString("duration");

                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
                                    populateSpinnerPojo.setPlanId(planId1);
                                    populateSpinnerPojo.setPlanName(planName);
                                    //  populateSpinnerPojo.setDuration(duration);
                                    //   populateSpinnerPojo.setPrice(price);
                                    plannames.add(planName);
                                    planlId.add(planId1);
                                    //  durationNumber.add( " 12 Months");
                                    if (!durationNumber.contains("1 Months")) {
                                        durationNumber.clear();
                                        durationNumber.add(" 1 Months");
                                        //durationLits.add(duration+" Months");
                                        spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this,
                                                R.layout.my_spinner_style,
                                                durationNumber));
                                        //  durationNumber.add(duration + " Months");

                                    } else {

                                        spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this,
                                                R.layout.my_spinner_style,
                                                durationNumber));
                                        if (durationNumber.equals("1 Months")) {
                                            linearPrice.setVisibility(View.GONE);
                                            TvPrice.setEnabled(false);
                                            TvPrice.setFocusable(false);
                                            TvPrice.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(false); // user navigates with wheel and selects widget

                                        } else {
                                            linearPrice.setVisibility(View.VISIBLE);

                                            TvPrice.setEnabled(true);
                                            TvPrice.setFocusable(true);
                                            TvPrice.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(true); // user navigates with wheel and selects widget

                                        }

                                    }
                                    planeList.add(populateSpinnerPojo);
                                }
                                spinnerplan.setAdapter(new ArrayAdapter(Subscription.this,
                                        R.layout.my_spinner_style,
                                        plannames));


                                spinnerplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                       // subPlanId = planeList.get(pos).getPlanId();
                                        try {
                                            subPlanId = planlId.get(pos);
                                            String subPrice = planeList.get(pos).getPrice();
                                            String subDuration = planeList.get(pos).getDuration();
                                            TvPrice.setText(" " + subPrice);
                                            Log.e(TAG, "onItemSelected:subPlanId=== "+subPlanId );
                                            Log.e(TAG, "onItemSelected:subDuration=== "+subDuration );
                                            Log.e(TAG, "onItemSelected:subPrice=== "+subPrice );
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //spinnerDuration.setSelection(subDuration);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                                spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subDurationId = durationNumber.get(pos);
                                        if (subDurationId.equals("1 Months")) {
                                            linearPrice.setVisibility(View.GONE);
                                            TvPrice.setEnabled(false);
                                            TvPrice.setFocusable(false);
                                            TvPrice.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(false); // user navigates with wheel and selects widget

                                        } else {
                                            linearPrice.setVisibility(View.VISIBLE);
                                            TvPrice.setEnabled(true);
                                            TvPrice.setFocusable(true);
                                            TvPrice.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(true); // user navigates with wheel and selects widget

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                                pDialog.dismiss();
                            } else {

                               // plannames.add(response1);
                                //durationLits.add(response1);
                              //  spinnerplan.setAdapter(new ArrayAdapter(Subscription.this, R.layout.my_spinner_style, plannames));
                              //  spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this, R.layout.my_spinner_style, durationNumber));
                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            sendError(e.toString(), "plan/list?parkingType=");
                            e.printStackTrace();
                            Toast.makeText(Subscription.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendError(error.toString(), "plan/list?parkingType=");
                        Log.d("Response error", error + "");
                        Toast.makeText(Subscription.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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
/* public void planService(final String parkingType1234) {
        final ProgressDialog pDialog = new ProgressDialog(Subscription.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        String urlData = AppConstants.BASEURL + "plan/list?parkingType=" + parkingType1234;
        Log.e(TAG, "planService:urlData=== " + urlData);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.d("Response", response + "");

                        plannames.add("Select Plan");
                        durationLits.add("Select Duration");
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
                                    String price = jsonObject.getString("price");
                                    String duration = jsonObject.getString("duration");

                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
                                    populateSpinnerPojo.setPlanId(planId1);
                                    populateSpinnerPojo.setPlanName(planName);
                                    populateSpinnerPojo.setDuration(duration);
                                    populateSpinnerPojo.setPrice(price);
                                    plannames.add(planName);
                                    durationLits.add(duration + " Months");
                                    if (!durationLits.contains("1 Months")) {
                                        *//*durationLits.clear();
                                        durationLits.add(" 1 Months");
                                        //durationLits.add(duration+" Months");
                                        spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this,
                                                R.layout.my_spinner_style,
                                                durationLits));*//*
                                        //  durationNumber.add(duration + " Months");

                                    } else {

                                        spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this,
                                                R.layout.my_spinner_style,
                                                durationNumber));
                                        if (durationNumber.equals("1 Months")) {
                                            TvPrice.setEnabled(false);
                                            TvPrice.setFocusable(false);
                                            TvPrice.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(false); // user navigates with wheel and selects widget

                                        } else {
                                            TvPrice.setEnabled(true);
                                            TvPrice.setFocusable(true);
                                            TvPrice.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(true); // user navigates with wheel and selects widget

                                        }

                                    }
                                    planeList.add(populateSpinnerPojo);
                                }
                                spinnerplan.setAdapter(new ArrayAdapter(Subscription.this,
                                        R.layout.my_spinner_style,
                                        plannames));


                                spinnerplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subPlanId = planeList.get(pos).getPlanId();
                                        String subPrice = planeList.get(pos).getPrice();
                                        String subDuration = planeList.get(pos).getDuration();
                                        TvPrice.setText(" " + subPrice);
                                        //spinnerDuration.setSelection(subDuration);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                                spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subDurationId = durationNumber.get(pos);
                                        if (subDurationId.equals("1 Months")) {
                                            TvPrice.setEnabled(false);
                                            TvPrice.setFocusable(false);
                                            TvPrice.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(false); // user navigates with wheel and selects widget

                                        } else {
                                            TvPrice.setEnabled(true);
                                            TvPrice.setFocusable(true);
                                            TvPrice.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                                            TvPrice.setClickable(true); // user navigates with wheel and selects widget

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
                                durationLits.add(response1);
                                spinnerplan.setAdapter(new ArrayAdapter(Subscription.this, R.layout.my_spinner_style, plannames));
                                spinnerDuration.setAdapter(new ArrayAdapter(Subscription.this, R.layout.my_spinner_style, durationLits));
                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            sendError(e.toString(), "plan/list?parkingType=");
                            e.printStackTrace();
                            Toast.makeText(Subscription.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendError(error.toString(), "plan/list?parkingType=");
                        Log.d("Response error", error + "");
                        Toast.makeText(Subscription.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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
    }*/

    public void nfcDetailForINOut(String tagid) {
        // Toast.makeText(this, "tagid    " + tagid, Toast.LENGTH_SHORT).show();

        final ProgressDialog pDialog = new ProgressDialog(Subscription.this);
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

//                        etStateCode.setText(StateCode);
//                        etCityCode.setText(CityCode);
//                        etVehicleCode.setText(VehicleCode);
//                        etVehicleNumber.setText(vehicleNumber);

                        Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(Subscription.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.getMessage(), "parking/pass_detail?cardNo=");
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    sendError(error.getMessage(), "parking/pass_detail?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.getMessage(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.getMessage(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.getMessage(), "parking/pass_detail?cardNo=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.getMessage(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(Subscription.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(Subscription.this, printer);

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
        if (lostPass.equals("NFC")) {
            CheckInModel checkInModel = new CheckInModel();
            //checkInModel.getReceiptHeading();

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

//            printer.printText(renewableDate,
//                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

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

            cleareText();

        } else {
            CheckInModel checkInModel = new CheckInModel();
            //checkInModel.getReceiptHeading();

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
//            printer.printQrCode(barCode, 200, Printer.Gravity.CENTER);

            // printer.printBarCode(barCode,1,100,1);

//            printer.printText("", Printer.FontFamily.SONG,
//                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
//                    Printer.Gravity.CENTER);
//
//            printer.printText("ATTENDANT - " + getAgentName + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//           /* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//*/
//            printer.printText("Call Attendant:" + getReceiptMobile, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("Powered By:-" + getPoweredBy, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText(getCompanyWebsite, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//
//            printer.printText("Receipt No. -  " + getReceiptNo, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//
//
            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

//            availableSlotSevice(parkingId, parkingType12, agentId);
            cleareText();
        }


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
                Intent reprintIntent = new Intent(Subscription.this, ReprintReceipt.class);
                startActivity(reprintIntent);
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

        if (AppConstants.isInternetAvailable(Subscription.this)) {
            send(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
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

}
