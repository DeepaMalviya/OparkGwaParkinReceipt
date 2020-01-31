package attender.oparkReceipt.subscription.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.PassDetailModel;
import attender.oparkReceipt.base.DividerItemDecoration;

public class CardDetail extends AppCompatActivity {
    private Toolbar toolBar;
    private TextView textToolHeader, holderName, pass, cardNumber;
    CardDetailAdapter cardDetailAdapter;
    ArrayList<PassDetailModel> vehicleModelList = new ArrayList<>();
    private RecyclerView recyclerViewVehicle;
    String fromdate = "", toDate = "", NFCCardNumber = "", agentId = "";
    SharedPreferences sharedpref;
    CardView renew_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        init();

        if (AppConstants.isInternetAvailable(CardDetail.this)) {
            getDetails(agentId, NFCCardNumber, fromdate, toDate);

        } else {
            Toast.makeText(CardDetail.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Card CheckIn/CheckOut Detail");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        recyclerViewVehicle = (RecyclerView) findViewById(R.id.recyclerViewReview);
        cardNumber = (TextView) findViewById(R.id.cardNumber);
        pass = (TextView) findViewById(R.id.pass);
        holderName = (TextView) findViewById(R.id.holderName);
        renew_card = (CardView) findViewById(R.id.renew_card);
        //addVehicle = (FloatingActionButton)view.findViewById(R.id.addVehicle);
        recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVehicle.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedpref.getString("agentId", "");

        Intent intent = getIntent();

        NFCCardNumber = intent.getStringExtra("NFCCardNumber");
        cardNumber.setEnabled(false);
        toDate = intent.getStringExtra("toDate");
        fromdate = intent.getStringExtra("fromdate");
    }


    public void getDetails(String agentId, String cardNo, String startDate, String endDate) {
        final ProgressDialog pDialog = new ProgressDialog(CardDetail.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
       /*http://api.opark.in/index.php/v1/parking/pass_record?agentId=11&cardNo=D96B3D5D&startDate=06/09/2018&endDate=14/09/2018*/
        String urlData = AppConstants.BASEURL + "parking/pass_record?agentId=" + agentId + "&cardNo=" + cardNo + "&startDate=" + startDate + "&endDate=" + endDate;

        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                vehicleModelList.clear();

                try {
                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");


                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);
                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);

                            PassDetailModel parkinglListModel = new PassDetailModel();

                            parkinglListModel.setCheckinText(jsonObject.getString("checkinText"));
                            parkinglListModel.setDurationText(jsonObject.getString("checkoutText"));
                            parkinglListModel.setDurationText(jsonObject.getString("durationText"));

                            vehicleModelList.add(parkinglListModel);
                        }
                        cardDetailAdapter = new CardDetailAdapter(CardDetail.this, CardDetail.this, vehicleModelList);
                        recyclerViewVehicle.setAdapter(cardDetailAdapter);
                        cardDetailAdapter.notifyDataSetChanged();

                        pDialog.dismiss();

                    } else {
                        Toast.makeText(CardDetail.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_record?agentId=");
                    Toast.makeText(CardDetail.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_record?agentId=");
                    Toast.makeText(CardDetail.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendError(error.toString(), "parking/pass_record?agentId=");
                        Toast.makeText(CardDetail.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        Log.d("Response error", error + "");
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL +AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(CardDetail.this)) {
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
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
