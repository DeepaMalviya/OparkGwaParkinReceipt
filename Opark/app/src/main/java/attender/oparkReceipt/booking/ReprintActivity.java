package attender.oparkReceipt.booking;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Formatter;

import attender.oparkReceipt.MainActivity;
import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.SubscriptionModelNew;
import attender.oparkReceipt.vehiclelist.activity.WebViewPrint;
import attender.oparkReceipt.vehiclelist.model.VehicleModelDetails;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class ReprintActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ReprintActivity";
    private Toolbar toolBar;
    private TextView tvAvailableSpots, textToolHeader;
    Button btnSubmit;
    private RadioButton TwoType, OtherType, FourType, ThreeType;

    private ServiceManager mServiceManager = null;
    EditText etMobileNo, etStateCode, etCityCode, etVehicleCode, etVehicleNumber;
    String vehicleNo;
    private RadioGroup radioGroup, radioGroupType;
    private String vehicleNumber = "";
    private String StateCode = "";
    private String CityCode = "";
    private String VehicleCode = "";
    SharedPreferences sharedpref;
    String agentId = "", userRole = "", userName = "", userContactNo = "", vendorId = "", vendorName = "", parkingName = "", parkingType = "", parkingId = "", userType = "", parkingType11, parkingType12 = "", subVehicleNo = "";
    String Gate, GateId, poweredBy = "", companyWebsite = "", parkingAddress = "", receiptHeading = "", printError = "", message1 = "";
    private NfcAdapter mNfcAdapter;
    Printer printer;
    String tagID = "";
    String getReceiptHeading, getParkingAddress, getCheckInDate, getVehicleNo, getParkingRate, getAdditionalParkingRate, barCode, getQrCode, ReceiptStaticText,
            getAgentName, getReceiptMobile, getPoweredBy, lastLine, getCompanyWebsite, getReceiptNo, getprintReceipt, onlineUserText;
    String getReceiptHeadingOut, getParkingAddressOut, getCheckInDateOut, getCheckOutDateOut, getVehicleNoOut, getPoweredByOut, getCompanyWebsiteOut, getReceiptNoOut,
            getduration, getDurationUnit, getCurrencySymbol, getGrandTotal, parkingType2, getParkingID, getParkingName;
    SharedPreferences.Editor ed;
    private ArrayList<VehicleModelDetails> vehicleModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprint);
        getSharedPreferencesDAta();
        findViewByIdMethod();
        initNFC();

        btnSubmit.setOnClickListener(this);
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


    }

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(ReprintActivity.this, new Weipos.OnInitListener() {

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
            // Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
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
        Log.e(TAG, "id:tagID=================" + tagID);
        AppConstants.setString(ReprintActivity.this, AppConstants.TAGID, tagID);

        if (tagID.equals("")) {
            Toast.makeText(this, "Card Not register", Toast.LENGTH_SHORT).show();
        } else {

            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
            if (AppConstants.isNetworkAvailable(ReprintActivity.this)) {
                etStateCode.setEnabled(true);
                etCityCode.setEnabled(true);
                etVehicleCode.setEnabled(true);
                etVehicleNumber.setEnabled(true);
                updateToolBar();
                etStateCode.setBackgroundColor(getResources().getColor(R.color.white));
                etCityCode.setBackgroundColor(getResources().getColor(R.color.white));
                etVehicleCode.setBackgroundColor(getResources().getColor(R.color.white));
                etVehicleNumber.setBackgroundColor(getResources().getColor(R.color.white));
                btnSubmit.setBackground(getResources().getDrawable(R.drawable.buttongradient));
                btnSubmit.setEnabled(true);
                passDetailsCheckoutApi(tagID);
            } else {
                etStateCode.setEnabled(false);
                etCityCode.setEnabled(false);
                etVehicleCode.setEnabled(false);
                etVehicleNumber.setEnabled(false);
                updateToolBar();
                etStateCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                etCityCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                etVehicleCode.setBackgroundColor(getResources().getColor(R.color.light_grey));
                etVehicleNumber.setBackgroundColor(getResources().getColor(R.color.light_grey));
                btnSubmit.setBackground(getResources().getDrawable(R.drawable.buttongradient_gray));
                btnSubmit.setEnabled(false);

                Toast.makeText(ReprintActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
            }


        }
        formatter.close();

    }

    boolean checkin;

    public void passDetailsCheckoutApi(String tagID) {
        Log.e(TAG, "passDetailsCheckoutApi: ");
        final ProgressDialog pDialog = new ProgressDialog(ReprintActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/parking/pass_detail?cardNo=4BHH5678*/
//cardNo=CARD578&parkingId=30&parkingType=2Wheeler
        String urlData = AppConstants.BASEURL + "parking/pass_detail_checkout?cardNo=" + tagID + "&parkingId=" + parkingId + "&parkingType=" + parkingType12;
        Log.e(TAG, "passDetailsCheckoutApi:urlData " + urlData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    System.out.println("JSON RETURN " + response.toString());
                    Log.e(TAG, "onResponse: passDetailsCheckoutApi==========" + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");
                    //Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                    if (status == 0) {
                        updateToolBar();
                        // toolBar.setTitle("");
                        etStateCode.setEnabled(true);
                        etCityCode.setEnabled(true);
                        etVehicleCode.setEnabled(true);
                        etVehicleNumber.setEnabled(true);
                        etStateCode.setEnabled(true);

                        etStateCode.setBackgroundColor(getResources().getColor(R.color.white));
                        etCityCode.setBackgroundColor(getResources().getColor(R.color.white));
                        etVehicleCode.setBackgroundColor(getResources().getColor(R.color.white));
                        etVehicleNumber.setBackgroundColor(getResources().getColor(R.color.white));
                        // btnCheckIn.setBackground(getResources().getDrawable(R.drawable.buttongradient));
                        // btnCheckout.setBackground(getResources().getDrawable(R.drawable.gradientback));
                        //nfcDetailForINOut(tagID);
                        textToolHeader.setVisibility(View.VISIBLE);

                        // Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        JSONObject checkINresponce = response.getJSONObject("data");

                        SubscriptionModelNew subscriptionModel = new SubscriptionModelNew();

                        /*subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subscriptionModel.setAgentId(checkINresponce.getString("agentId"));*/
                        subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
                        subscriptionModel.setCheckin(checkINresponce.getBoolean("checkin"));
                        subscriptionModel.setIsPass(checkINresponce.getString("isPass"));
                        subVehicleNo = subscriptionModel.getVehicleNo();
                        checkin = checkINresponce.getBoolean("checkin");
                        String isPass = subscriptionModel.getIsPass();
                        Log.e(TAG, "vehicleNo:=-------== " + checkINresponce.getString("vehicleNo"));
                        Log.e(TAG, "cardNo:==-----= " + checkINresponce.getString("cardNo"));
                        Log.e(TAG, "checkin:==-----= " + checkINresponce.getBoolean("checkin"));
                        Log.e(TAG, "isPass:==-----= " + checkINresponce.getString("isPass"));
                        Log.e(TAG, "checkin:==-----= " + checkin);
                        Log.e(TAG, "isPass:==-----= " + isPass);
                        //etVehicleNumber.setText(checkINresponce.getString("vehicleNo"));


                    }

                    String[] totalsubVehicleNo = subVehicleNo.toString().split(",");
                    //String[] totalsubVehicleNo = subVehicleNo.toString().split(" ");

                    try {
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

                    // vehicleCheckIn(parkingId, parkingType1234, vehicleNo1, agentId, userType, mobileNumber, subscriptionModel.getCardNo());

                    //    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    pDialog.dismiss();


                    pDialog.dismiss();


                   /* e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(MainActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();*/
                } catch (
                        JSONException e)

                {
                 /*   e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(MainActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();*/
                }
            }
        }, new Response.ErrorListener()

        {

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
                } else {
                   /* sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
*/
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);
    }


    private void updateToolBar() {
        parkingType12 = sharedpref.getString("VehicleType1", "");
        String Gate = sharedpref.getString("Gate", "");

        if (parkingType12.equals("2Wheeler")) {
            textToolHeader.setVisibility(View.VISIBLE);
            //toolBarUserAvatar1.setVisibility(View.VISIBLE);

            textToolHeader.setText("" + parkingType12);
            //toolBarUserAvatar1.setImageResource(R.mipmap.twobike);
        } else if (parkingType12.equals("3Wheeler")) {
            textToolHeader.setVisibility(View.VISIBLE);
            textToolHeader.setText("" + parkingType12);
            // toolBarUserAvatar1.setVisibility(View.VISIBLE);

            // toolBarUserAvatar1.setImageResource(R.drawable.ic_tuk_tuk);
        } else if (parkingType12.equals("other")) {
            textToolHeader.setVisibility(View.VISIBLE);
            textToolHeader.setText("" + parkingType12);
            // toolBarUserAvatar1.setVisibility(View.VISIBLE);

            // toolBarUserAvatar1.setImageResource(R.drawable.ic_ambulance);
        } else {
            textToolHeader.setVisibility(View.VISIBLE);
            textToolHeader.setText("" + parkingType12);
            //  toolBarUserAvatar1.setVisibility(View.VISIBLE);
            // textToolHeader.setText("Four Wheeler");
            //  toolBarUserAvatar1.setImageResource(R.drawable.car_trip);
        }
    }


    private void findViewByIdMethod() {
        etStateCode = (EditText) findViewById(R.id.etStateCodee);
        etCityCode = (EditText) findViewById(R.id.etCityCodee);
        etVehicleCode = (EditText) findViewById(R.id.etVehicleCodee);
        etVehicleNumber = (EditText) findViewById(R.id.etVehicleNumbere);
        Log.e(TAG, "validate: tagID        " + tagID);
        TwoType = (RadioButton) findViewById(R.id.TwoType);
        ThreeType = (RadioButton) findViewById(R.id.ThreeType);
        FourType = (RadioButton) findViewById(R.id.FourType);
        OtherType = (RadioButton) findViewById(R.id.OtherType);
        radioGroupType = (RadioGroup) findViewById(R.id.radioGroupType);

        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler,other")) {
            OtherType.setChecked(true);
            TwoType.setChecked(true);
            FourType.setChecked(true);
            ThreeType.setChecked(true);
            TwoType.setVisibility(View.VISIBLE);
            ThreeType.setVisibility(View.VISIBLE);
            FourType.setVisibility(View.VISIBLE);
            OtherType.setVisibility(View.VISIBLE);
        }else
        if (parkingType.equals("2Wheeler,4Wheeler,3Wheeler")) {
            TwoType.setChecked(true);
            FourType.setChecked(true);
            ThreeType.setChecked(true);
            TwoType.setVisibility(View.VISIBLE);
            ThreeType.setVisibility(View.VISIBLE);
            FourType.setVisibility(View.VISIBLE);
            OtherType.setVisibility(View.GONE);
        }
        else
        if (parkingType.equals("2Wheeler,4Wheeler")) {
            TwoType.setChecked(true);
            FourType.setChecked(true);
            TwoType.setVisibility(View.VISIBLE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.VISIBLE);
            OtherType.setVisibility(View.GONE);
        }else
        if (parkingType.equals("4Wheeler")) {
            FourType.setChecked(true);
            TwoType.setVisibility(View.GONE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.VISIBLE);
            OtherType.setVisibility(View.GONE);
        }else
        if (parkingType.equals("2Wheeler")) {
            TwoType.setChecked(true);
            TwoType.setVisibility(View.VISIBLE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.GONE);
            OtherType.setVisibility(View.GONE);

        }
        else
        if (parkingType.equals("4Wheeler")) {
            FourType.setChecked(true);
            TwoType.setVisibility(View.GONE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.VISIBLE);
            OtherType.setVisibility(View.GONE);

        }else
        if (parkingType.equals("other")) {
            OtherType.setChecked(true);
            TwoType.setVisibility(View.GONE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.GONE);
            OtherType.setVisibility(View.VISIBLE);

        }else
        if (parkingType.equals("2Wheeler")) {
            TwoType.setChecked(true);
            TwoType.setVisibility(View.VISIBLE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.GONE);
            OtherType.setVisibility(View.GONE);


        }else
        if (parkingType.equals("2Wheeler")) {
            TwoType.setChecked(true);
            TwoType.setVisibility(View.VISIBLE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.GONE);
            OtherType.setVisibility(View.GONE);
            TwoType.setVisibility(View.GONE);
        }else
        if (parkingType.equals("4Wheeler")) {
            FourType.setChecked(true);
            TwoType.setVisibility(View.GONE);
            ThreeType.setVisibility(View.GONE);
            FourType.setVisibility(View.VISIBLE);
            OtherType.setVisibility(View.GONE);

        }

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.TwoType) {

                    TwoType.setChecked(true);
                    parkingType12 = "2Wheeler";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", parkingType12);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                    Log.e(TAG, "id: parkingType12" + parkingType12);

                    updateToolBar();
                }

                if (checkedId == R.id.ThreeType) {
                    ThreeType.setChecked(true);
                    parkingType12 = "3Wheeler";

                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", parkingType12);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                    Log.e(TAG, "id: parkingType12" + parkingType12);
                    updateToolBar();
                }

                if (checkedId == R.id.FourType) {
                    FourType.setChecked(true);
                    parkingType12 = "4Wheeler";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", parkingType12);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                    Log.e(TAG, "id: parkingType12" + parkingType12);
                    updateToolBar();

                }
                if (checkedId == R.id.OtherType) {
                    OtherType.setChecked(true);
                    parkingType12 = "other";
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("VehicleType1", parkingType12);
                    // ed.putString("VehicleType1", Type12);
                    //   ed.putString("VehicleType2", Type12[1]);
                    ed.apply();
                    ed.commit();

                    parkingType12 = sharedpref.getString("VehicleType1", "");
                    Log.e(TAG, "id: parkingType12" + parkingType12);
                    updateToolBar();

                }
            }
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        textToolHeader = (TextView) findViewById(R.id.toolbar_title);
        textToolHeader.setText("Reprint");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ReprintActivity.this, MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
    }

    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(ReprintActivity.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ReprintActivity.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(ReprintActivity.this, printer);

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
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        String Gate = sharedpref.getString("Gate", "");
        Log.e(TAG, "printNormal: Gate======" + Gate);
        /*sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        String parkingPrint = sharedpref.getString("parkingPrint", "");*/

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

            printer.printText("GATE NO. -  " + Gate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.CENTER);
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

           /* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
*/
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

            //availableSlotSevice(parkingId, parkingType12, agentId);
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
            printer.printText("GATE NO. -  " + Gate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.CENTER);
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

            // availableSlotSevice(parkingId, parkingType12, agentId);
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
            printer.printText("GATE NO. -  " + Gate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.CENTER);
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

    public void cleareText() {
        etStateCode.setText("");
        etCityCode.setText("");
        etVehicleCode.setText("");
        etVehicleNumber.setText("");
    }

    private void getSharedPreferencesDAta() {
        printer = WeiposImpl.as().openPrinter();


        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        parkingType12 = sharedpref.getString("VehicleType1", "");
        Gate = sharedpref.getString("Gate", "");
        GateId = sharedpref.getString("GateId", "");
        Log.e(TAG, "onCreate:Gate " + Gate);
        Log.e(TAG, "onCreate:GateId " + GateId);
        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("getparkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        //parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType12 = sharedpref.getString("VehicleType1", "");
        Log.e(TAG, "onCreate: parkingType12--===========-" + parkingType12);
        Log.e(TAG, "onCreate: parkingType--==============-" + parkingType);
        getParkingID = sharedpref.getString("getParkingID", "");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                StateCode = etStateCode.getText().toString();
                CityCode = etCityCode.getText().toString();
                VehicleCode = etVehicleCode.getText().toString();
                vehicleNumber = etVehicleNumber.getText().toString();
                vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;

                if (AppConstants.isBlank(vehicleNo)) {
                    GetPrintDataApi();
                } else {
                    if (parkingType12.equals("2Wheeler,4Wheeler,3Wheeler")) {
                        Toast.makeText(this, "Please select any wheeler type!", Toast.LENGTH_SHORT).show();
                    }  else {
                        GetPrintDataApi();
                    }

                }
                break;
        }
    }

    private void GetPrintDataApi() {
        vehicleNo = StateCode + CityCode + VehicleCode + vehicleNumber;
        final ProgressDialog pDialog = new ProgressDialog(ReprintActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
         /*http://staggingapi.opark.in/index.php/v1/parking/checkout?parkingId=1&parkingType=4Wheeler&vehicleNo=6565&agentId=3&mode=Normal&userdata=9993289838*/
        String urlData = AppConstants.BASEURL + "parking/reprint_vehicle?vehicleNo=" + vehicleNo + "&parkingId=" + getParkingID + "&parkingType=" + parkingType12;
        Log.e(TAG, "vehicleCheckOut: urlData--==========" + urlData);
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {

                vehicleModelList.clear();
                try {
                    System.out.println("JSON " + "ETURN " + json.toString());
                    Log.e(TAG, "onResponse:====== "+json.toString() );

                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");

                    if (status == 0) {

                        final JSONObject arrayObj = new JSONObject(data);

                        String transactionId = arrayObj.getString("transactionId");
                        String receiptHeading = arrayObj.getString("receiptHeading");
                        String parkingAddress = arrayObj.getString("parkingAddress");
                        String vehicleNo1 = arrayObj.getString("vehicleNo");
                        String checkInDate = arrayObj.getString("checkInDate");
                        String checkOutDate = arrayObj.getString("checkOutDate");
                        String checkInTime = arrayObj.getString("checkInTime");
                        String checkOutTime = arrayObj.getString("checkOutTime");
                        String duration = arrayObj.getString("duration");
                        String durationUnit = arrayObj.getString("durationUnit");
                        String grandTotal = arrayObj.getString("grandTotal");
                        String currencySymbol = arrayObj.getString("currencySymbol");
                        String userContactNo = arrayObj.getString("userContactNo");
                        String agentId = arrayObj.getString("agentId");
                        String availableSlots = arrayObj.getString("availableSlots");
                        String parkingId = arrayObj.getString("parkingId");
                        String parkingRate = arrayObj.getString("parkingRate");
                        String tax = arrayObj.getString("tax");
                        String total = arrayObj.getString("total");
                        String barcode = arrayObj.getString("barcode");
                        String mode = arrayObj.getString("mode");
                        String responseType = arrayObj.getString("responseType");
                        String minimumAmount = arrayObj.getString("minimumAmount");
                        String parkingName = arrayObj.getString("parkingName");
                        String receipt = arrayObj.getString("receipt");
                        String companyWebsite = arrayObj.getString("companyWebsite");
                        String poweredBy = arrayObj.getString("poweredBy");
                        String receiptType = arrayObj.getString("receiptType");
                        String receiptNo = arrayObj.getString("receiptNo");
                        String parkingType = arrayObj.getString("parkingType");
                        String reprintText = arrayObj.getString("reprintText");
                        //String parkingType = arrayObj.getString("printReceipt");


                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();

                        editor.putString("parkingPrint", "checkOUTModel");
                        editor.apply();
                        editor.commit();
                        Intent intentWeb = new Intent(ReprintActivity.this, WebViewPrint.class);

                        intentWeb.putExtra("receiptHeading", receiptHeading);
                        intentWeb.putExtra("parkingAddress", parkingAddress);
                        intentWeb.putExtra("vehicleNo1", vehicleNo1);
                        intentWeb.putExtra("checkInDate", checkInDate);
                        intentWeb.putExtra("checkOutDate", checkOutDate);
                        intentWeb.putExtra("duration", duration);
                        intentWeb.putExtra("durationUnit", durationUnit);
                        intentWeb.putExtra("currencySymbol", currencySymbol);
                        intentWeb.putExtra("grandTotal", grandTotal);
                        intentWeb.putExtra("poweredBy", poweredBy);
                        intentWeb.putExtra("receiptNo", receiptNo);
                        intentWeb.putExtra("companyWebsite", companyWebsite);
                        intentWeb.putExtra("receipt", receipt);
                        intentWeb.putExtra("reprintText", reprintText);
                   startActivity(intentWeb);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ReprintActivity.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(ReprintActivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ReprintActivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                      //  sendError(error.toString(), "parking/receipt?parkingId=");
                        Toast.makeText(ReprintActivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
    }



    private void showAlert(String message) {
        AppConstants.showToast(ReprintActivity.this, message);
    }

}
