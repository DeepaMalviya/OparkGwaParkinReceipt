/*
package attender.opark.parkinglist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.Map;

import attender.opark.MainActivity;
import attender.opark.R;
import attender.opark.base.AppConstants;
import attender.opark.base.AppController;
import attender.opark.base.ClickListener;
import attender.opark.base.DividerItemDecoration;
import attender.opark.base.RecyclerTouchListener;
import attender.opark.parkinglist.adapter.ParkingAdapter;
import attender.opark.parkinglist.model.GateList;
import attender.opark.parkinglist.model.ParkingListMoel;
import attender.opark.subscription.model.CustomRequest;

public class ParkingListGateModule extends AppCompatActivity {
    private static final String TAG = "ParkingList";
    RecyclerView recyclerViewParkingList;
    ParkingAdapter parkingAdapter;
    ArrayList<ParkingListMoel> parkinglList = new ArrayList<>();
    ArrayList<GateList> list = new ArrayList<>();
    String vendorId, agentId, userRole, userName, userContactNo, vendorName, park, parkingId, getparkingType;
    private Toolbar toolBar;
    private TextView textToolHeader;
    String parkingTypelist;
    SharedPreferences sharedpref;
    SharedPreferences.Editor ed;
    AlertDialog dialog;
    String GateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);

        init();

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        vendorId = sharedpref.getString("vendorId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorName = sharedpref.getString("vendorName", "");
        agentId = sharedpref.getString("agentId", "");
        GateId = sharedpref.getString("GateId", "");


        Intent intent = getIntent();
        //      agentId = intent.getStringExtra("agentId");
//        userRole = intent.getStringExtra("userRole");
//        userName = intent.getStringExtra("userName");
//        userContactNo = intent.getStringExtra("userContactNo");
//       // vendorId = intent.getStringExtra("vendorId");
//        vendorName = intent.getStringExtra("vendorName");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConstants.isInternetAvailable(ParkingListGateModule.this)) {
            getDetails(vendorId);
        } else {
            Toast.makeText(ParkingListGateModule.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);
        }


    }

    public void init() {


        recyclerViewParkingList = (RecyclerView) findViewById(R.id.recyclerViewParkingList);
//        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
//
//        agentId = sharedpref.getString("agentId", "");
//        userRole = sharedpref.getString("userRole", "");
//        userName = sharedpref.getString("userName", "");
//        userContactNo = sharedpref.getString("userContactNo", "");
//        vendorId = sharedpref.getString("vendorId", "");
//        vendorName = sharedpref.getString("vendorName", "");

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Select Parking");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerViewParkingList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewParkingList.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable
                .horizontal_divider_gray)));

        recyclerViewParkingList.addOnItemTouchListener(new RecyclerTouchListener(ParkingListGateModule.this, recyclerViewParkingList, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

//                Toast.makeText(CarInActivity.this, vehicleModelList.get(position).getTransactionId(), Toast.LENGTH_SHORT).show();
                try {
                    park = parkinglList.get(position).getParkingType();
                    parkingId = parkinglList.get(position).getParkingID();
                    getparkingType = parkinglList.get(position).getParkingType();
                    for (int i = 0; i < parkinglList.size(); i++) {
                        Log.e(TAG, "onClick:parkinglList==getParkingID " + parkinglList.get(i).getParkingID());
                        ;
                        Log.e(TAG, "onClick:parkinglList== " + parkinglList.get(i).getParkingType());
                        ;
                    }
                    sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                    ed = sharedpref.edit();
                    ed.putString("getParkingID", parkinglList.get(position).getParkingID());
                    ed.putString("getParkingName", parkinglList.get(position).getParkingName());
                    ed.putString("getParkingType", parkinglList.get(position).getParkingType());
                    ed.apply();
                    ed.commit();

                    showVehicleType();


                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    RadioButton[] radioButton;

    private void getGateApi() {
        final ProgressDialog pDialog = new ProgressDialog(ParkingListGateModule.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
       */
/*http://staggingapi.opark.in/index.php/v1/parking/list?vendorId=1*//*

       */
/*http://opark.in/O_par_aPi/nfc/index.php/v1/parking/gate_list*//*

        String urlData = AppConstants.BASEURL + "parking/gate_list";
        Log.e(TAG, "getGateApi: urlData==" + urlData);
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                list.clear();

                try {
                    System.out.println("JSON RETURN " + json.toString());
                    Log.e(TAG, "JSON RETURN===========: " + json.toString());

                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");
                    //int detail = json.getInt("detail");

                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);


                        Toast.makeText(ParkingListGateModule.this, message, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);
                            GateList gateList = new GateList();
                            gateList.setGateName(jsonObject.getString("gateName"));
                            gateList.setGateId(jsonObject.getString("gateId"));
                            Log.e(TAG, "onResponse: " + jsonObject.getString("gateName"));
                            Log.e(TAG, "onResponse: " + jsonObject.getString("gateId"));
                            list.add(gateList);
                        }
                        Log.e(TAG, "onResponse: ====" + list.size());
                   */
/*     radioButton = new RadioButton[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            radioButton[i] = new RadioButton(ParkingList.this);

                            //Text can be loaded here
                            radioButton[i].setText(" " +list.get (i).getGateName());

                        }
                        for (int i = 0; i < list.size(); i++) {
                            radioButton[i].setChecked(true);

                        }

                        for (int i = 0; i < list.size(); i++) {
                            radioGate.addView(radioButton[i]);
                        }*//*

                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ParkingListGateModule.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(ParkingListGateModule.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParkingListGateModule.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        Toast.makeText(ParkingListGateModule.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
    }

    public static void setOnItemClick(int position) {

    }

    public void getDetails(String vendorId) {
        final ProgressDialog pDialog = new ProgressDialog(ParkingListGateModule.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
       */
/*http://staggingapi.opark.in/index.php/v1/parking/list?vendorId=1*//*

        String urlData = AppConstants.BASEURL + "parking/list?vendorId=" + vendorId;
        Log.e(TAG, "getDetails: urlData==" + urlData);
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                parkinglList.clear();

                try {
                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");
                    int detail = json.getInt("detail");

                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);


                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);

//                            parkingIdlist = jsonObject.getString("parkingId");
                            //                           parkingNamelist = jsonObject.getString("parkingName");
                            parkingTypelist = jsonObject.getString("parkingType");

                            ParkingListMoel parkinglListModel = new ParkingListMoel();

                            parkinglListModel.setParkingID(jsonObject.getString("parkingId"));
                            parkinglListModel.setParkingName(jsonObject.getString("parkingName"));
                            parkinglListModel.setParkingType(jsonObject.getString("parkingType"));

                            parkinglList.add(parkinglListModel);
                        }
                        parkingAdapter = new ParkingAdapter(ParkingListGateModule.this, ParkingListGateModule.this, parkinglList);
                        recyclerViewParkingList.setAdapter(parkingAdapter);
                        parkingAdapter.notifyDataSetChanged();


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(ParkingListGateModule.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/list?vendorId=");
                    Log.d("NullPointerException", e + "");
                    Toast.makeText(ParkingListGateModule.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/list?vendorId=");
                    Log.d("JSONException", e + "");
                    Toast.makeText(ParkingListGateModule.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        sendError(error.toString(), "parking/list?vendorId=");
                        Toast.makeText(ParkingListGateModule.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);

    }

    public void revenewSevice(final String parkingId, final String agentId, String parkingType, String GateId) {

        final ProgressDialog pDialog = new ProgressDialog(ParkingListGateModule.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        */
/**
/*http://staggingapi.opark.in/index.php/v1/user/track?parkingId=1&agentId=3*//*


        String urlData = AppConstants.BASEURL + "user/track?parkingId=" + parkingId + "&vendorId=" + vendorId + "&parkingType=" + parkingType + "&agentId=" + agentId + "&gateId=" + GateId;
        Log.e(TAG, "revenewSevice: urlData" + urlData);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                try {
                    System.out.println("JSON RETURN " + response.toString());
                    Log.e(TAG, "onResponse:==== " + response.toString());
                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

//                    if (status == 0) {
//
//                        JSONObject availableresponce = response.getJSONObject("data");
//
////                        pDialog.dismiss();
//
//
//                    } else {
//                        Toast.makeText(ParkingList.this, message, Toast.LENGTH_SHORT).show();
//                    }
                    //  pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/track?parkingId=");
                    Log.d("NullPointerException", e + "");
                    Toast.makeText(ParkingListGateModule.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "user/track?parkingId=");
                    Toast.makeText(ParkingListGateModule.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    Log.d("JSONException", e + "");
                    //  pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(ParkingList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Response error", error + "");
                // Toast.makeText(ParkingList.this, "Server Error...", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString(), "user/track?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                } else if (error instanceof ServerError) {
                    sendError(error.toString(), "user/track?parkingId=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString(), "user/track?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString(), "user/track?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString(), "user/track?parkingId=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString(), "user/track?parkingId=");

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

    RadioGroup radioGate;

    public void showVehicleType() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);

        final Button btnSubmit = (Button) alertLayout.findViewById(R.id.btnSubmit);
        final RadioButton twoWheeler = (RadioButton) alertLayout.findViewById(R.id.twowheeler);
        final RadioButton fourWheeler = (RadioButton) alertLayout.findViewById(R.id.fourwheeler);
        final RadioButton threeWheler = (RadioButton) alertLayout.findViewById(R.id.threeewheeler);
        final RadioButton Patient_Vehicle = (RadioButton) alertLayout.findViewById(R.id.Patient_Vehicle);


        final RadioButton Gate1 = (RadioButton) alertLayout.findViewById(R.id.Gate1);
        final RadioButton Gate2 = (RadioButton) alertLayout.findViewById(R.id.Gate2);
        final RadioButton Gate3 = (RadioButton) alertLayout.findViewById(R.id.Gate3);
        final RadioButton Gate4 = (RadioButton) alertLayout.findViewById(R.id.Gate4);
        final RadioGroup radio = (RadioGroup) alertLayout.findViewById(R.id.radio);
        radioGate = (RadioGroup) alertLayout.findViewById(R.id.radioGate);
        getGateApi();
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        String parkingType = sharedpref.getString("getParkingType", "");
        final String[] parkings = parkingType.split(",");
        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        ed = sharedpref.edit();
        ed.putString("agentId", agentId);
        ed.putString("userRole", userRole);
        ed.putString("userName", userName);
        ed.putString("userContactNo", userContactNo);
        ed.putString("vendorId", vendorId);
        ed.putString("vendorName", vendorName);
        ed.putString("parkingId", parkingId);
        ed.putString("getparkingType", getparkingType);

        ed.apply();
        ed.commit();

        if (list.size() == 4) {
            String Gate11 = list.get(0).getGateName();
            String Gate12 = list.get(1).getGateName();
            String Gate13 = list.get(2).getGateName();
            String Gate14 = list.get(3).getGateName();
            Gate1.setText(Gate11);
            Gate2.setText(Gate12);
            Gate3.setText(Gate13);
            Gate4.setText(Gate14);
            radioGate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radio.check(checkedId);

                    if (checkedId == R.id.Gate1) {
                        GateId = "Gate1";
                        Gate1.setChecked(true);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(0).getGateId());
                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateId = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateId);
                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    } else if (checkedId == R.id.Gate2) {
                        GateId = "Gate2";
                        Gate1.setChecked(false);
                        Gate2.setChecked(true);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(1).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateId = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    } else if (checkedId == R.id.Gate3) {
                        GateId = "Gate3";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(true);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(2).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateId = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    } else if (checkedId == R.id.Gate4) {
                        GateId = "Gate4";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(true);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(3).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateId = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    } else {
                        Toast.makeText(ParkingListGateModule.this, "Please Select Gate No", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        String pType2 = "";
        String pType4 = "";
        String pType3 = "";
        String pType5 = "";
        Log.e(TAG, "showVehicleType:parkings.length " + parkings.length);
        if (parkings.length == 3) {
            {


                pType2 = parkings[0];
                pType4 = parkings[1];
                pType3 = parkings[2];
                for (int i = 0; i < parkings.length; i++) {
                    Log.e(TAG, "showVehicleType:parkings[ " + i + " ] " + parkings[i]);

                }
                Patient_Vehicle.setVisibility(View.GONE);
                twoWheeler.setText(pType2);
                fourWheeler.setText(pType4);
                threeWheler.setText(pType3);
                ed.commit();


                final String finalPType = pType2;
                final String finalPType1 = pType4;
                radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radio.check(checkedId);

                        if (checkedId == R.id.twowheeler) {
                            fourWheeler.setChecked(false);
                            threeWheler.setChecked(false);
                            twoWheeler.setChecked(true);

                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("VehicleType1", parkings[0]);
                            //   ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");

                           */
/* revenewSevice(getParkingID, agentId, VehicleType1);
                            Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        } else if (checkedId == R.id.threeewheeler) {
                            threeWheler.setChecked(true);
                            twoWheeler.setChecked(false);
                            fourWheeler.setChecked(false);
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("VehicleType1", parkings[2]);
                            //   ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");
                            String GateId = sharedpref.getString("GateId", "");

                            revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                          */
/*  Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        } else {
                            fourWheeler.setChecked(true);
                            threeWheler.setChecked(false);
                            twoWheeler.setChecked(false);

                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("VehicleType1", parkings[1]);
                            // ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");
                            String GateId = sharedpref.getString("GateId", "");

                            revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                          */
/*  Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        }
                    }
                });
                radioGate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radio.check(checkedId);

                        if (checkedId == R.id.Gate1) {
                            GateId = "Gate1";
                            Gate1.setChecked(true);
                            Gate2.setChecked(false);
                            Gate3.setChecked(false);
                            Gate4.setChecked(false);

                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("Gate", GateId);
                            //   ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");
                            String Gate = sharedpref.getString("Gate", "");
                            String GateId = sharedpref.getString("GateId", "");

                            revenewSevice(getParkingID, agentId, VehicleType1, GateId);
                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        } else if (checkedId == R.id.Gate2) {
                            GateId = "Gate2";
                            Gate1.setChecked(false);
                            Gate2.setChecked(true);
                            Gate3.setChecked(false);
                            Gate4.setChecked(false);
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("Gate", GateId);
                            //   ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");
                            String Gate = sharedpref.getString("Gate", "");
                            String GateId = sharedpref.getString("GateId", "");

                            revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        } else if (checkedId == R.id.Gate3) {
                            GateId = "Gate3";

                            Gate1.setChecked(false);
                            Gate2.setChecked(false);
                            Gate3.setChecked(true);
                            Gate4.setChecked(false);
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("Gate", GateId);
                            // ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");
                            String Gate = sharedpref.getString("Gate", "");
                            String GateId = sharedpref.getString("GateId", "");

                            revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        } else if (checkedId == R.id.Gate4) {
                            GateId = "Gate4";

                            Gate1.setChecked(false);
                            Gate2.setChecked(false);
                            Gate3.setChecked(false);
                            Gate4.setChecked(true);

                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                            ed = sharedpref.edit();
                            ed.putString("Gate", GateId);
                            // ed.putString("VehicleType2", parkings[1]);
                            ed.apply();
                            ed.commit();
                            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                            String VehicleType1 = sharedpref.getString("VehicleType1", "");
                            String getParkingID = sharedpref.getString("getParkingID", "");
                            String Gate = sharedpref.getString("Gate", "");
                            String GateId = sharedpref.getString("GateId", "");

                            revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                        } else {
                            Toast.makeText(ParkingListGateModule.this, "Please Select Gate No", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick:btnSubmit ");
                        if (sharedpref.getString("VehicleType1", "").equals("")) {
                            Toast.makeText(ParkingListGateModule.this, "Please select Vehicle type", Toast.LENGTH_SHORT).show();
                        } else if (sharedpref.getString("GateId", "").equals("")) {
                            Toast.makeText(ParkingListGateModule.this, "Please select Gate ", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();
                        }
                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Select Vehicle Type");
                alert.setView(alertLayout);
                dialog = alert.create();
                if (dialog != null) {
                    dialog.show();
                }

            }
        } else if (parkings.length == 1) {
            Patient_Vehicle.setVisibility(View.GONE);
            //  twoWheeler.setChecked(true);
            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
            ed = sharedpref.edit();
            ed.putString("VehicleType1", parkings[0]);
            ed.apply();
            ed.commit();
            sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

            String VehicleType1 = sharedpref.getString("VehicleType1", "");
            String getParkingID = sharedpref.getString("getParkingID", "");
            String GateIdd = sharedpref.getString("GateId", "");

            revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);
            radioGate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radio.check(checkedId);

                    if (checkedId == R.id.Gate1) {
                        GateId = "Gate1";
                        Gate1.setChecked(true);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(0).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);
                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate2) {
                        GateId = "Gate2";
                        Gate1.setChecked(false);
                        Gate2.setChecked(true);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(1).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate3) {
                        GateId = "Gate3";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(true);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(2).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate4) {
                        GateId = "Gate4";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(true);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(3).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else {
                        Toast.makeText(ParkingListGateModule.this, "Please Select Gate No", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick:btnSubmit ");
                    if (sharedpref.getString("VehicleType1", "").equals("")) {
                        Toast.makeText(ParkingListGateModule.this, "Please select Vehicle type", Toast.LENGTH_SHORT).show();
                    } else if (sharedpref.getString("GateId", "").equals("")) {
                        Toast.makeText(ParkingListGateModule.this, "Please select Gate ", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    }
                }
            });
         */
/*   Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
            startActivity(intentSplash);
            finish();*//*

        } else if (parkings.length == 2) {
            Patient_Vehicle.setVisibility(View.GONE);
            threeWheler.setVisibility(View.GONE);
            pType2 = parkings[0];
            pType4 = parkings[1];
            for (int i = 0; i < parkings.length; i++) {
                Log.e(TAG, "showVehicleType:parkings[ " + i + " ] " + parkings[i]);

            }
            //  twoWheeler.setChecked(true);
            twoWheeler.setText(pType2);
            fourWheeler.setText(pType4);


            final String finalPType = pType2;
            final String finalPType1 = pType4;
            radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radio.check(checkedId);

                    if (checkedId == R.id.twowheeler) {
                        fourWheeler.setChecked(false);
                        twoWheeler.setChecked(true);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("VehicleType1", parkings[0]);
                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                       */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();*//*

                    } else {
                        fourWheeler.setChecked(true);
                        twoWheeler.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("VehicleType1", parkings[1]);
                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                       */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();*//*

                    }
                }
            });
            radioGate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radio.check(checkedId);

                    if (checkedId == R.id.Gate1) {
                        GateId = "Gate1";
                        Gate1.setChecked(true);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(0).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);
                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate2) {
                        GateId = "Gate2";
                        Gate1.setChecked(false);
                        Gate2.setChecked(true);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(1).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate3) {
                        GateId = "Gate3";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(true);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(2).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate4) {
                        GateId = "Gate4";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(true);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(3).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else {
                        Toast.makeText(ParkingListGateModule.this, "Please Select Gate No", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick:btnSubmit ");
                    if (sharedpref.getString("VehicleType1", "").equals("")) {
                        Toast.makeText(ParkingListGateModule.this, "Please select Vehicle type", Toast.LENGTH_SHORT).show();
                    } else if (sharedpref.getString("GateId", "").equals("")) {
                        Toast.makeText(ParkingListGateModule.this, "Please select Gate ", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    }
                }
            });
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Select Vehicle Type");
            alert.setView(alertLayout);
            dialog = alert.create();
            if (dialog != null) {
                dialog.show();
            }

        } else if (parkings.length == 4) {

            // threeWheler.setVisibility(View.GONE);
            pType2 = parkings[0];
            pType4 = parkings[1];
            pType3 = parkings[2];
            pType5 = parkings[3];
            for (int i = 0; i < parkings.length; i++) {
                Log.e(TAG, "showVehicleType:parkings[ " + i + " ] " + parkings[i]);

            }
            //  twoWheeler.setChecked(true);
            twoWheeler.setText(pType2);
            fourWheeler.setText(pType4);
            threeWheler.setText(pType3);
            Patient_Vehicle.setText(pType5);


            final String finalPType2 = pType2;
            final String finalPType1 = pType4;
            final String finalPType3 = pType3;
            final String finalPType5 = pType5;
            radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radio.check(checkedId);

                    if (checkedId == R.id.twowheeler) {
                        fourWheeler.setChecked(false);
                        threeWheler.setChecked(false);
                        Patient_Vehicle.setChecked(false);
                        twoWheeler.setChecked(true);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("VehicleType1", parkings[0]);
                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                       */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();*//*

                    } else if (checkedId == R.id.threeewheeler) {
                        twoWheeler.setChecked(false);
                        fourWheeler.setChecked(false);
                        Patient_Vehicle.setChecked(false);

                        threeWheler.setChecked(true);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("VehicleType1", parkings[2]);
                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                       */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();*//*

                    } else if (checkedId == R.id.Patient_Vehicle) {
                        Patient_Vehicle.setChecked(true);
                        threeWheler.setChecked(false);
                        fourWheeler.setChecked(false);
                        twoWheeler.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("VehicleType1", parkings[3]);
                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                       */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();*//*

                    } else {
                        Patient_Vehicle.setChecked(false);
                        fourWheeler.setChecked(true);
                        twoWheeler.setChecked(false);
                        threeWheler.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("VehicleType1", parkings[1]);
                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                       */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();*//*

                    }
                }
            });
            radioGate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radio.check(checkedId);

                    if (checkedId == R.id.Gate1) {
                        GateId = "Gate1";
                        Gate1.setChecked(true);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(0).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);
                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate2) {
                        GateId = "Gate2";
                        Gate1.setChecked(false);
                        Gate2.setChecked(true);
                        Gate3.setChecked(false);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(1).getGateId());

                        //   ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate3) {
                        GateId = "Gate3";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(true);
                        Gate4.setChecked(false);
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(2).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateIdd);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else if (checkedId == R.id.Gate4) {
                        GateId = "Gate4";

                        Gate1.setChecked(false);
                        Gate2.setChecked(false);
                        Gate3.setChecked(false);
                        Gate4.setChecked(true);

                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        ed = sharedpref.edit();
                        ed.putString("Gate", GateId);
                        ed.putString("GateId", list.get(3).getGateId());

                        // ed.putString("VehicleType2", parkings[1]);
                        ed.apply();
                        ed.commit();
                        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

                        String VehicleType1 = sharedpref.getString("VehicleType1", "");
                        String getParkingID = sharedpref.getString("getParkingID", "");
                        String Gate = sharedpref.getString("Gate", "");
                        String GateIdd = sharedpref.getString("GateId", "");

                        revenewSevice(getParkingID, agentId, VehicleType1, GateId);

                           */
/* Intent intentSplash = new Intent(ParkingList.this, MainActivity.class);
                            startActivity(intentSplash);
                            finish();*//*

                    } else {
                        Toast.makeText(ParkingListGateModule.this, "Please Select Gate No", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick:btnSubmit ");
                    if (sharedpref.getString("VehicleType1", "").equals("")) {
                        Toast.makeText(ParkingListGateModule.this, "Please select Vehicle type", Toast.LENGTH_SHORT).show();
                    } else if (sharedpref.getString("GateId", "").equals("")) {
                        Toast.makeText(ParkingListGateModule.this, "Please select Gate ", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intentSplash = new Intent(ParkingListGateModule.this, MainActivity.class);
                        startActivity(intentSplash);
                        finish();
                    }
                }
            });
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Select Vehicle Type");
            alert.setView(alertLayout);
            dialog = alert.create();
            if (dialog != null) {
                dialog.show();
            }

        }


    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(ParkingListGateModule.this)) {
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
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. Server error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString() + "\n TimeoutError", "parking/pass_add");
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        sendError(volleyError.toString() + "\n NetworkError", "parking/pass_add");
                        Toast.makeText(ParkingListGateModule.this,
                                "Oops. Network Error !",
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
*/
