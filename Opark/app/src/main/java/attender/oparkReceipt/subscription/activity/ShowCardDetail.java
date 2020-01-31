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

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.PassDetailModel;
import attender.oparkReceipt.subscription.adapter.ShowCardDetailAdapter;
import attender.oparkReceipt.base.DividerItemDecoration;

public class ShowCardDetail extends AppCompatActivity {
    private static final String TAG = "ShowCardDetail";
    private Toolbar toolBar;
    private TextView textToolHeader, holderName, pass, cardNumber, pass_heading;
    private RecyclerView recyclerViewVehicle;
    String fromdate = "", toDate = "", NFCCardNumber = "", agentId = "";
    SharedPreferences sharedpref;
    CardView renew_card;
    ShowCardDetailAdapter showCardDetailAdapter;

    String cardNo, plan, passHeading, holderName1;

    ArrayList<PassDetailModel> cardList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card_detail);

        init();

        showCardDetail(agentId, NFCCardNumber, fromdate, toDate);
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Card ChekIn Detail");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


        cardNumber = (TextView) findViewById(R.id.cardNumber);
        pass = (TextView) findViewById(R.id.pass);
        holderName = (TextView) findViewById(R.id.holderName);
        pass_heading = (TextView) findViewById(R.id.pass_heading);
        renew_card = (CardView) findViewById(R.id.renew_card);

        recyclerViewVehicle = (RecyclerView) findViewById(R.id.recyclerViewReview);
        recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVehicle.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedpref.getString("agentId", "");

        Intent intent = getIntent();

        NFCCardNumber = intent.getStringExtra("NFCCardNumber");
        toDate = intent.getStringExtra("toDate");
        fromdate = intent.getStringExtra("fromdate");
    }

    public void setData() {
        pass.setText(plan);
        holderName.setText(holderName1);
        cardNumber.setText(cardNo);
        pass_heading.setText(passHeading);

    }

    public void showCardDetail(String agentId, String cardNumber, String startDate, String endDate) {
        final ProgressDialog pDialog = new ProgressDialog(ShowCardDetail.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        String urlData = AppConstants.BASEURL + "pass/record?agentId=" + agentId + "&cardNo=" + cardNumber + "&startDate=" + startDate + "&endDate=" + endDate;
        Log.e(TAG, "showCardDetail:urlData "+urlData );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {

                // cardList.clear();
                try {

                    renew_card.setVisibility(View.VISIBLE);
                    System.out.println("JSON RETURN " + json.toString());
                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    cardNo = String.valueOf(json.get("cardNo"));
                    String agentId = String.valueOf(json.get("agentId"));
                    plan = String.valueOf(json.get("plan"));
                    holderName1 = String.valueOf(json.get("holderName"));
                    passHeading = String.valueOf(json.get("passHeading"));
                    int status = json.getInt("status");


                    setData();
                    if (status == 0) {
                        final JSONArray arrayObj = new JSONArray(data);
                        for (int i = 0; i < arrayObj.length(); i++) {
                            JSONObject jsonObject = arrayObj.getJSONObject(i);

                            PassDetailModel passDetailModel = new PassDetailModel();

                            passDetailModel.setCheckinText(jsonObject.getString("checkinText"));
                            passDetailModel.setCheckoutText(jsonObject.getString("checkoutText"));
                            passDetailModel.setDurationText(jsonObject.getString("durationText"));

                            cardList.add(passDetailModel);

                        }
                        showCardDetailAdapter = new ShowCardDetailAdapter(ShowCardDetail.this, cardList);
                        recyclerViewVehicle.setAdapter(showCardDetailAdapter);

                        pDialog.dismiss();
                    } else {
                        Toast.makeText(ShowCardDetail.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_record?agentId=");
                    Toast.makeText(ShowCardDetail.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_record?agentId=");
                    Toast.makeText(ShowCardDetail.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response error", error + "");
                sendError(error.toString(), "parking/pass_record?agentId=");
                Toast.makeText(ShowCardDetail.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(ShowCardDetail.this)) {
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
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ShowCardDetail.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ShowCardDetail.this,
                                "Oops. Server error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ShowCardDetail.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ShowCardDetail.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ShowCardDetail.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(ShowCardDetail.this,
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
