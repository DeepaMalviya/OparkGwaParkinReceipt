package attender.oparkReceipt.towingoperator.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.InputValidation;
import attender.oparkReceipt.subscription.model.SubscriptionModel;

public class ReleasedTowVehicle extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolBar;
    private TextView textToolHeader;
    Button releas;
    private TextInputLayout textInputLayoutChalan;
    private TextInputEditText textInputEditChalanNo;
    TextView vechileno, type, location, towndate;
    ImageView image;
    String getVehicleType, getVehicleImage, getVehicleNo, Towndate;
    SharedPreferences sharedpref;
    String agentId;
    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_released_tow_vehicle);

        init();
        initListeners();


    }

    public void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Releas Vehicle");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        agentId = sharedpref.getString("agentId", "");

        final Intent intent = getIntent();

        getVehicleNo = intent.getStringExtra("getVehicleNo");
        getVehicleType = intent.getStringExtra("type");
        getVehicleImage = intent.getStringExtra("image");
        Towndate = intent.getStringExtra("Towndate");

        textInputLayoutChalan = (TextInputLayout) findViewById(R.id.textInputLayoutChalan);
        textInputEditChalanNo = (TextInputEditText) findViewById(R.id.textInputEditChalanNo);

        vechileno = (TextView) findViewById(R.id.vechileno);
        type = (TextView) findViewById(R.id.type);
        location = (TextView) findViewById(R.id.location);
        towndate = (TextView) findViewById(R.id.towndate);
        image = (ImageView) findViewById(R.id.image);
        releas = (Button) findViewById(R.id.releas);

        vechileno.setText("  " + getVehicleNo);
        type.setText("  " + getVehicleType);
        location.setText("  " + agentId);
        towndate.setText("  " + Towndate);

        Picasso.with(this).load(getVehicleImage).into(image);


    }

    private void initListeners() {
        releas.setOnClickListener(this);
//        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.releas:
                releaseVehicle();
                break;
        }
    }

    public void releaseVehicle() {
        String chalanNo = textInputEditChalanNo.getText().toString();


        if (chalanNo.equals("")) {
            textInputEditChalanNo.setError("Enter Chalan Number!");
            textInputEditChalanNo.requestFocus();
            return;
        } else {
            if (AppConstants.isInternetAvailable(ReleasedTowVehicle.this)) {
                String url = AppConstants.BASEURL + "towing/vehiclerelease";
                Map<String, String> parameterData = new HashMap<>();
                parameterData.put(("vehicleType"), getVehicleType);
                parameterData.put(("operatorId"), agentId);
                parameterData.put(("vehicleNo"), getVehicleNo);
                parameterData.put(("chalanNo"), chalanNo);
                releaseVehicleServices(url, parameterData);
            } else {
                Toast.makeText(ReleasedTowVehicle.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            }


        }


    }

    public void releaseVehicleServices(String url, final Map<String, String> params) {

        // pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.rinion));
        final ProgressDialog pDialog = new ProgressDialog(ReleasedTowVehicle.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObjectReleased(jsonObject);
                    pDialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("RESPONSE ERROR", volleyError.toString());
                    //Toast.makeText(getActivity(), "error==>  " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
            pDialog.dismiss();
        }
    }

    private void processJsonObjectReleased(JSONObject response) {

        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

                if (status == 0) {

                    JSONObject checkINresponce = response.getJSONObject("data");
                    SubscriptionModel subscriptionModel = new SubscriptionModel();
                    vechileno.setText("");
                    type.setText("");
                    location.setText("");
                    towndate.setText("");
                    textInputEditChalanNo.setText("");
                    //   image.setImageResource(Integer.parseInt(""));
                    Picasso.with(this).load(R.mipmap.police_car512).into(image);
//                    subscriptionModel.setCardNo(checkINresponce.getString("cardNo"));
//                    subscriptionModel.setAgentId(checkINresponce.getString("agentId"));
//                    subscriptionModel.setVehicleNo(checkINresponce.getString("vehicleNo"));

                   /* subCardNo = subscriptionModel.getCardNo();
                    subAgentNo = subscriptionModel.getAgentId();
                    subVehicleNo = subscriptionModel.getVehicleNo();
                    cleareText();*/
                    Toast.makeText(ReleasedTowVehicle.this, message, Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ReleasedTowVehicle.this, message, Toast.LENGTH_SHORT).show();
                    //pDialog.dismiss();

                }


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
