package attender.oparkReceipt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.subscription.model.CustomRequest;


public class TransationActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolBar;
    TextView textToolHeader;
    Button ok;
    SharedPreferences sharedPreferences;
    String agentId, parkingId, parkingType;
    private String mobileNumber = "";
    private String vehicleNumber = "";
    private String vipvehicleNumber = "";
    private String StateCode = "";
    private String CityCode = "";
    private String VehicleCode = "";
    private String transactionId = "";

    EditText etMobileNo, etStateCode, etCityCode, etVehicleCode, etVehicleNumber, vipVehicleNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transation);

        init();
        clickListener();
    }

    public void init() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textToolHeader = (TextView) findViewById(R.id.toolbar_title);
        etVehicleNumber = (EditText) findViewById(R.id.etVehicleNumber);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etStateCode = (EditText) findViewById(R.id.etStateCode);
        etCityCode = (EditText) findViewById(R.id.etCityCode);
        etVehicleCode = (EditText) findViewById(R.id.etVehicleCode);
        etVehicleNumber = (EditText) findViewById(R.id.etVehicleNumber);
        ok = (Button) findViewById(R.id.ok);
        textToolHeader.setText("Transaction ID");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        parkingType = intent.getStringExtra("parkingType");

        sharedPreferences = getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedPreferences.getString("agentId", "");
        parkingId = sharedPreferences.getString("parkingId", "");

        etVehicleNumber.requestFocus();
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
                    etMobileNo.requestFocus();
                    // AppConstants.hideKeyboard(TransationActivity.this, etVehicleNumber);
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
        etMobileNo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
//                etMobileNo.setSelection(etVehicleNumber.getText().length());

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (etVehicleNumber.getText().toString().equals("")) {
                    Toast.makeText(this, "Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                } else if (etVehicleNumber.getText().toString().length() < 4) {
                    //  Toast.makeText(TransationActivity.this, "Enter Vailid Vehicle Number", Toast.LENGTH_SHORT).show();
                    etVehicleNumber.requestFocus();
                    etVehicleNumber.setError("Enter Correct Vehicle Number");
                } else if (etMobileNo.getText().toString().equals("")) {
                    Toast.makeText(this, "Enter Transaction Id ", Toast.LENGTH_SHORT).show();
                } else {


                    StateCode = etStateCode.getText().toString();
                    CityCode = etCityCode.getText().toString();
                    VehicleCode = etVehicleCode.getText().toString();
                    vehicleNumber = etVehicleNumber.getText().toString();
                    transactionId = etMobileNo.getText().toString();

                    if (!AppConstants.isBlank(StateCode) || !AppConstants.isBlank(CityCode) || !AppConstants.isBlank(VehicleCode)) {

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
                    } else {
                        apiCall();
                    }


//                    String vehicleNumber = etVehicleNumber.getText().toString();
//                    String transactionId = etMobileNo.getText().toString();
//                    //  transactionIdServices(vehicleNumber, transactionId, venderId, parkingId);
//                    String url = AppConstants.BASEURL + "parking/paytm";
//                    Map<String, String> param = new HashMap();
//                    param.put("parkingId", parkingId);
//                    param.put("vehicleNo", vehicleNumber);
//                    param.put("paytmId", transactionId);
//                    param.put("agentId", agentId);
//                    param.put("parkingType", parkingType);
//                    if (AppConstants.isNetworkProviderAvailable(this)) {
//                        transactionIdServices(url, param);
//                    } else {
//                        Toast.makeText(TransationActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
//                    }

                }

        }
    }

    public void apiCall() {
        String url = AppConstants.BASEURL + "parking/paytm";
        vehicleNumber = StateCode + CityCode + VehicleCode + vehicleNumber;

        Map<String, String> param = new HashMap();
        param.put("parkingId", parkingId);
        param.put("vehicleNo", vehicleNumber);
        param.put("paytmId", transactionId);
        param.put("agentId", agentId);
        param.put("parkingType", parkingType);
        if (AppConstants.isNetworkProviderAvailable(this)) {
            transactionIdServices(url, param);
        } else {
            Toast.makeText(TransationActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);
        }
    }

    private void showAlert(String message) {
        AppConstants.showToast(TransationActivity.this, message);
    }


    public void clickListener() {
        ok.setOnClickListener(this);
    }

    private void transactionIdServices(String url, final Map<String, String> param) {
        final ProgressDialog progressDialog = new ProgressDialog(TransationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress_bar);

        try {
            Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    processJsontransactionIdServices(response);
                    progressDialog.dismiss();

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("RESPONSE ERROR", error.toString());
                    sendError(error.toString(), "parking/paytm");
                    Toast.makeText(TransationActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), "error==>  " + volleyError.toString(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            };
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, param, response, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(customRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            sendError(e.toString(), "parking/paytm");
            progressDialog.dismiss();
        }


    }

    private void processJsontransactionIdServices(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");
            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                if (status == 0) {
                    Intent intent = new Intent(TransationActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    Toast.makeText(TransationActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TransationActivity.this, message, Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
                sendError(e.toString(), "parking/paytm");
                Toast.makeText(TransationActivity.this, "Tecnical Error...", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(TransationActivity.this)) {
            send(url, parameterData);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
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
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
