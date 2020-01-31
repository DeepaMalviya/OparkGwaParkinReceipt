package attender.oparkReceipt.subscription.activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.InputValidation;
import attender.oparkReceipt.subscription.model.SubscriptionModel;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class ReprintReceipt extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ReprintReceipt";
    private Toolbar toolBar;
    private TextView textToolHeader;
    private AppCompatButton appCompatButtonRrprint;

    private TextInputLayout textInputLayoutnfcnumber;
    private TextInputEditText textInputEditnfcnumber;
    private InputValidation inputValidation;

    private NfcAdapter mNfcAdapter;
    private ServiceManager mServiceManager = null;
    Printer printer;

    String tagID, cardNo, subCardNo, subVehicleNo, plan, total, holderName, passHeading, AgentName, ParkingName, renewableDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprint_receipt);

        printer = WeiposImpl.as().openPrinter();

        init();
        clickListener();
        initObjects();
        initNFC();
        initSdk();
    }

    private void initObjects() {
        inputValidation = new InputValidation(ReprintReceipt.this);
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("RePrint Receipt");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        appCompatButtonRrprint = (AppCompatButton) findViewById(R.id.appCompatButtonRrprint);
        textInputLayoutnfcnumber = (TextInputLayout) findViewById(R.id.textInputLayoutnfcnumber);
        textInputEditnfcnumber = (TextInputEditText) findViewById(R.id.textInputEditnfcnumber);
    }

    private void clickListener() {
        appCompatButtonRrprint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.appCompatButtonRrprint:
                if (!inputValidation.isInputEditTextFilled(textInputEditnfcnumber, textInputLayoutnfcnumber, "Enter NFS Card Number")) {
                    return;
                } else {
                    cardNo = textInputEditnfcnumber.getText().toString().trim();
                    rePrintApi(cardNo);
                }

        }
    }

    private void rePrintApi(String cardNo) {

        final ProgressDialog pDialog = new ProgressDialog(ReprintReceipt.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v1/pass/reprint?cardNo=25896333
        String urlData = AppConstants.BASEURL + "pass/reprint?cardNo=" + cardNo;

        Log.e(TAG, "rePrintApi:urlData "+urlData );
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    System.out.println("JSON RETURN " + response.toString());

                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {

                        JSONObject availableresponce = response.getJSONObject("data");

                        subCardNo = availableresponce.getString("cardNo");
                        subVehicleNo = availableresponce.getString("vehicleNo");
                        plan = availableresponce.getString("plan");
                        total = availableresponce.getString("total");
                        holderName = availableresponce.getString("holderName");
                        passHeading = availableresponce.getString("passHeading");
                        renewableDate = availableresponce.getString("renewableDate");
                        //AgentName = availableresponce.getString("parkingName");
                        ParkingName = availableresponce.getString("parkingName");
                        AgentName = availableresponce.getString("agentName");

                        printReceipt();
                        Toast.makeText(ReprintReceipt.this, message, Toast.LENGTH_SHORT).show();

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ReprintReceipt.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "pass/reprint?cardNo=");
                    Toast.makeText(ReprintReceipt.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "pass/reprint?cardNo=");
                    Toast.makeText(ReprintReceipt.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(ReprintReceipt.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString(), "pass/reprint?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    sendError(error.toString(), "pass/reprint?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString(), "pass/reprint?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString(), "pass/reprint?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString(), "pass/reprint?cardNo=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString(), "pass/reprint?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

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
        AppConstants.setString(ReprintReceipt.this, AppConstants.TAGID, tagID);
        // Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        //   nfcDetail(tagID);
        textInputEditnfcnumber.setText(tagID);

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

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(ReprintReceipt.this, new Weipos.OnInitListener() {

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

    public void nfcDetail(String tagid) {
        // Toast.makeText(this, "tagid    " + tagid, Toast.LENGTH_SHORT).show();

        final ProgressDialog pDialog = new ProgressDialog(ReprintReceipt.this);
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
                        String subVehicleNo = subscriptionModel.getVehicleNo();

                        String[] totalsubVehicleNo = subVehicleNo.toString().split(",");

                        String StateCode = totalsubVehicleNo[0];
                        String CityCode = totalsubVehicleNo[1];
                        String VehicleCode = totalsubVehicleNo[2];
                        String vehicleNumber = totalsubVehicleNo[3];

//                        etStateCode.setText(StateCode);
//                        etCityCode.setText(CityCode);
//                        etVehicleCode.setText(VehicleCode);
//                        etVehicleNumber.setText(vehicleNumber);

                        Toast.makeText(ReprintReceipt.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(Subscription.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(ReprintReceipt.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ReprintReceipt.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //  Toast.makeText(ReprintReceipt.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ReprintReceipt.this, "Server Error...", Toast.LENGTH_SHORT).show();

                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString(), "parking/pass_detail?cardNo=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString(), "parking/pass_detail?cardNo=");

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
            Toast.makeText(ReprintReceipt.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ReprintReceipt.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {


            printNormal(ReprintReceipt.this, printer);

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

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(ReprintReceipt.this)) {
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
