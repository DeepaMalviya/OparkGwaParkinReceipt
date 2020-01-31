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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.parkinglist.ParkingList;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.towingoperator.activity.AddTowVehicle;

public class ResendOtp extends AppCompatActivity {

    TextView etTimer;
    Button submitBtn, reSendotp;
    ImageButton btnLogin;
    LinearLayout etOtp;
    String mobileNo, user_Id, otp;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    EditText firstDigitOtpEdt, secondDigitOtpEdt, thirdDigitOtpEdt, fourthDigitOtpEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_otp);


        Intent intent = getIntent();
        mobileNo = intent.getStringExtra("MobileNumber");
        user_Id = intent.getStringExtra("userId");
        // otp = intent.getStringExtra("otp");
        reSendotp = (Button) findViewById(R.id.reSendotp);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        etOtp = (LinearLayout) findViewById(R.id.etOtp);

        firstDigitOtpEdt = (EditText) findViewById(R.id.etOtp1);
        secondDigitOtpEdt = (EditText) findViewById(R.id.etOtp2);
        thirdDigitOtpEdt = (EditText) findViewById(R.id.etOtp3);
        fourthDigitOtpEdt = (EditText) findViewById(R.id.etOtp4);
        getData();

        //  otp = etOtp.getText().toString();
        //   etTimer = (TextView) findViewById(R.id.etTimer);

//        new CountDownTimer(30000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                etTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
//                //here you can have your logic to set text to edittext
//            }
//
//            public void onFinish() {
//                etTimer.setText("done!");
//            }
//
//        }.start();
// TODO: 11/4/2019 uncomments
        reSendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //otp = etOtp.getText().toString();
                reSendOtp(mobileNo, user_Id);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://opark.in/O_par_aPi/gwaliorStg/index.php/v6/user/verifyotp?mobileNo=9993289838&userId=5&otp=7989
                otp = firstDigitOtpEdt.getText().toString() + "" + secondDigitOtpEdt.getText().toString() + "" + thirdDigitOtpEdt.getText().toString() + "" + fourthDigitOtpEdt.getText().toString();
                //otp = etOtp.getText().toString();
                if (otp.length() >= 4) {
                    loginSevice(mobileNo, user_Id, otp);
                } else {
                    Toast.makeText(ResendOtp.this, "Enter valid 4 digit OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getData() {

        firstDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (firstDigitOtpEdt.getText().toString().length() == 1) {
                    secondDigitOtpEdt.requestFocus();
                }
            }
        });

        secondDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (secondDigitOtpEdt.getText().toString().length() == 0) {
                    firstDigitOtpEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (secondDigitOtpEdt.getText().toString().length() == 1) {
                    thirdDigitOtpEdt.requestFocus();
                }
            }
        });

        thirdDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (thirdDigitOtpEdt.getText().toString().length() == 0) {
                    secondDigitOtpEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (thirdDigitOtpEdt.getText().toString().length() == 1) {
                    fourthDigitOtpEdt.requestFocus();
                }
            }
        });

        fourthDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (fourthDigitOtpEdt.getText().toString().length() == 0) {
                    thirdDigitOtpEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // We can call api to verify the OTP here or on an explicit button click
            }
        });
    }


    public void loginSevice(final String mobileNo, final String userId, final String otp) {

        final ProgressDialog pDialog = new ProgressDialog(ResendOtp.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
         /*http://staggingapi.opark.in/index.php/v1/user/login?username=9999999999&password=1234*/
         /*http://staggingapi.opark.in/index.php/v1/user/login?username=9999999999&password=1234*/
        String urlData = AppConstants.BASEURL + "user/verifyotp?mobileNo=" + mobileNo + "&userId=" + userId + "&otp=" + otp;

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
                            Intent intentSplash = new Intent(ResendOtp.this, ParkingList.class);

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
                            Intent intentTow = new Intent(ResendOtp.this, AddTowVehicle.class);
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
                        Toast.makeText(ResendOtp.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ResendOtp.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ResendOtp.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //com.android.volley.TimeoutError
                String er = "com.android.volley.TimeoutError";

                // com.android.volley.TimeoutError
                if (er.equals("com.android.volley.TimeoutError")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResendOtp.this);
                    builder.setMessage("Time Out");
                    builder.setPositiveButton("ReTry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AppConstants.isInternetAvailable(ResendOtp.this)) {
                                loginSevice(mobileNo, userId, otp);
                            } else {
                                Toast.makeText(ResendOtp.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifi.setWifiEnabled(true);
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
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ResendOtp.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (e instanceof NetworkError) {
                    sendError(e.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (e instanceof ServerError) {
                    sendError(e.toString(), "user/login?username=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (e instanceof AuthFailureError) {
                    sendError(e.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (e instanceof NoConnectionError) {
                    sendError(e.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (e instanceof TimeoutError) {
                    sendError(e.toString(), "user/login?username=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (e instanceof ParseError) {
                    sendError(e.toString(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void reSendOtp(final String username, final String userId) {

        final ProgressDialog pDialog = new ProgressDialog(ResendOtp.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
          /*https://opark.in/O_par_aPi/gwaliorStg/index.php/v6/user/sendotp?mobileNo=9993289838*/
        String urlData = AppConstants.BASEURL + "user/resendotp?mobileNo=" + username + "&userId=" + userId;

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

                        mobileNo = loginresponce.getString("mobileNo");
                        otp = loginresponce.getString("otp");
                        user_Id = loginresponce.getString("userId");

                    } else {
                        Toast.makeText(ResendOtp.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ResendOtp.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/login?username=");
                    Toast.makeText(ResendOtp.this, "Technical Error...", Toast.LENGTH_SHORT).show();
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
                    sendError(error.getMessage(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    sendError(error.getMessage(), "user/login?username=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.getMessage(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.getMessage(), "user/login?username=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.getMessage(), "user/login?username=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.getMessage(), "user/login?username=");

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

        if (AppConstants.isInternetAvailable(ResendOtp.this)) {
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
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ResendOtp.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ResendOtp.this,
                                "Oops. Server error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ResendOtp.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ResendOtp.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ResendOtp.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ResendOtp.this,
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
