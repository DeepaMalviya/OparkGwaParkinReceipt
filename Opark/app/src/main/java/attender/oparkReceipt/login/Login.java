package attender.oparkReceipt.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;


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

import java.util.HashMap;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.parkinglist.ParkingList;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.towingoperator.activity.AddTowVehicle;


public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    EditText etPassword, etUserName;
    ImageButton btnLogin;
    Button reSendotp, sendotp;
    private String android_id, vehicleType, pType2, pType4;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        // getDeviceID(android_id);


    }

    private void initViews() {

        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        sendotp = (Button) findViewById(R.id.sendotp);
        // reSendotp = (Button)findViewById(R.id.reSendotp);

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


//        home_fragment_container = (LinearLayout) findViewById(R.id.home_fragment_container);
        //setDummyData(true);


        setListener();
    }

    private void setListener() {
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUserName.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(Login.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    if (AppConstants.isInternetAvailable(Login.this)) {
                        sendOtp(username);
                    } else {
                        Toast.makeText(Login.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();

                        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);

                    }

                }
                // finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validateLogin();

            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etUserName.getText().toString().length() == 10)     //size as per your requirement
                {
                    // sendotp.requestFocus();

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
        etPassword.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etPassword.getText().toString().length() == 4)     //size as per your requirement
                {

                    AppConstants.hideKeyboard(Login.this, etPassword);
                    /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(home_fragment_container.getWindowToken(), 0);*/
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
    }

    private void validateLogin() {


        final String username = etUserName.getText().toString();
        final String userPassword = etPassword.getText().toString();

        if (AppConstants.isBlank(username)) {
            AppConstants.showToast(Login.this, getString(R.string.username_is_required));
        } else if (AppConstants.isBlank(userPassword)) {
            AppConstants.showToast(Login.this, getString(R.string.password_field_is_required));
        } else {
            if (AppConstants.isInternetAvailable(Login.this)) {
                loginSevice(username, userPassword);
            } else {
                Toast.makeText(Login.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();

                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);

            }


        }
    }

    public void loginSevice(final String username, final String userPassword) {

        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
         /*http://staggingapi.opark.in/index.php/v1/user/login?username=9999999999&password=1234*/
         /*http://staggingapi.opark.in/index.php/v1/user/login?username=9999999999&password=1234*/
        String urlData = AppConstants.BASEURL + "user/login?username=" + username + "&password=" + userPassword;

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

                        JSONObject loginresponce = response.getJSONObject("data");


                        LoginModel loginModel = new LoginModel();

                        loginModel.setUserId(loginresponce.getString("agentId"));
                        loginModel.setUserRole(loginresponce.getString("userRole"));
                        loginModel.setUserName(loginresponce.getString("userName"));
                        loginModel.setUserContactNo(loginresponce.getString("userContactNo"));
                        loginModel.setVendorId(loginresponce.getString("vendorId"));
                        loginModel.setVendorName(loginresponce.getString("vendorName"));


                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("agentId", loginModel.getUserId());
                        ed.putString("userRole", loginModel.getUserRole());
                        ed.putString("userName", loginModel.getUserName());
                        ed.putString("userContactNo", loginModel.getUserContactNo());
                        ed.putString("vendorId", loginModel.getVendorId());
                        ed.putString("vendorName", loginModel.getVendorName());
                        ed.apply();
                        ed.commit();
                        String userRole = "Normal Agent";
                        if (loginModel.getUserRole().equals("Normal Agent")) {
                            Intent intentSplash = new Intent(Login.this, ParkingList.class);

                            intentSplash.putExtra("agentId", loginModel.getUserId());
                            intentSplash.putExtra("userRole", loginModel.getUserRole());
                            intentSplash.putExtra("userName", loginModel.getUserName());
                            intentSplash.putExtra("userContactNo", loginModel.getUserContactNo());
                            intentSplash.putExtra("vendorId", loginModel.getVendorId());
                            intentSplash.putExtra("vendorName", loginModel.getVendorName());

                            startActivity(intentSplash);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            Intent intentTow = new Intent(Login.this, AddTowVehicle.class);
                            intentTow.putExtra("agentId", loginModel.getUserId());
                            intentTow.putExtra("userRole", loginModel.getUserRole());
                            intentTow.putExtra("userName", loginModel.getUserName());
                            intentTow.putExtra("userContactNo", loginModel.getUserContactNo());
                            intentTow.putExtra("vendorId", loginModel.getVendorId());
                            intentTow.putExtra("vendorName", loginModel.getVendorName());
                            startActivity(intentTow);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();

                        }

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(Login.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(Login.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";

                // com.android.volley.TimeoutError
                if (er.equals("com.android.volley.TimeoutError")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage("Time Out");
                    builder.setPositiveButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AppConstants.isInternetAvailable(Login.this)) {
                                loginSevice(username, userPassword);
                            } else {
                                Toast.makeText(Login.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    sendError(error.toString(), "user/login?username=");
                    Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    sendError(error.toString(), "user/login?username=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString(), "user/login?username=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    public void sendOtp(final String username) {
        Log.e(TAG, "sendOtp: inside sendOtp method");
        final ProgressDialog pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
          /*https://opark.in/O_par_aPi/gwaliorStg/index.php/v6/user/sendotp?mobileNo=9993289838*/
        String urlData = AppConstants.BASEURL + "user/sendotp?mobileNo=" + username;
        Log.e(TAG, "sendOtp: urlData-----------" + urlData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                Log.e(TAG, "onResponse: inside try block");

                try {
                    Log.e(TAG, "onResponse: inside try block");
                    System.out.println("JSON RETURN " + response.toString());
                    Log.e(TAG, "onResponse: JSON RETURN " + response.toString());
                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");
                    Log.e(TAG, "onResponse: data" + data);
                    if (status == 0) {
                        Log.e(TAG, "onResponse: status" + status);
                        JSONObject loginresponce = response.getJSONObject("data");
                        Intent intent = new Intent(Login.this, ResendOtp.class);
                        intent.putExtra("MobileNumber", loginresponce.getString("mobileNo"));
                        intent.putExtra("otp", loginresponce.getString("otp"));
                        intent.putExtra("userId", loginresponce.getString("userId"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        pDialog.dismiss();
                        finish();

                    } else {
                        Log.e(TAG, "onResponse: status" + status);

                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/sendotp?mobileNo=");
                    Toast.makeText(Login.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/sendotp?mobileNo=");
                    Toast.makeText(Login.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";

                // com.android.volley.TimeoutError

                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString(), "user/sendotp?mobileNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    sendError(error.toString(), "user/sendotp?mobileNo=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString(), "user/sendotp?mobileNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString(), "user/sendotp?mobileNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString(), "user/sendotp?mobileNo=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString(), "user/sendotp?mobileNo=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(Login.this)) {
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
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "Login");
                        Toast.makeText(Login.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "Login");
                        Toast.makeText(Login.this,
                                "Oops. Server error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "Login");
                        Toast.makeText(Login.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "Login");
                        Toast.makeText(Login.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "Login");
                        Toast.makeText(Login.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "Login");
                        Toast.makeText(Login.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_SHORT).show();
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
