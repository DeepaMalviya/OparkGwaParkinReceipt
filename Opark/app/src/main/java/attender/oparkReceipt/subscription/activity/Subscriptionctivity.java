package attender.oparkReceipt.subscription.activity;

import android.app.DatePickerDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import attender.oparkReceipt.subscription.model.PassDetailModel;
import attender.oparkReceipt.base.DividerItemDecoration;

public class Subscriptionctivity extends AppCompatActivity implements View.OnClickListener {
    CardView add_card, renew_card, card_detail;
    private Toolbar toolBar;
    private TextView textToolHeader, holderName, pass, cardNumber;
    TextView from_date, to_date;
    DatePickerDialog datePickerDialog;
    Calendar myCalendar;
    SimpleDateFormat sdf;
    String tagID = "", parkingId, agentId = "", message = "";
    private NfcAdapter mNfcAdapter;
    TextInputLayout textInputLayoutnfcnumber;
    TextInputEditText textInputEditnfcnumber;
    SharedPreferences sharedpref;

    CardDetailAdapter cardDetailAdapter;
    CardView renew_card12;
    ArrayList<PassDetailModel> vehicleModelList = new ArrayList<>();
    private RecyclerView recyclerViewVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptionctivity);

        init();
        initListeners();
        initNFC();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");
        textToolHeader = (TextView) toolBar.findViewById(R.id.toolbar_title);
        textToolHeader.setText("Card detail");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        add_card = (CardView) findViewById(R.id.add_card);
        renew_card = (CardView) findViewById(R.id.renew_card);
        card_detail = (CardView) findViewById(R.id.card_detail);
        renew_card12 = (CardView) findViewById(R.id.renew_card12);
        to_date = (TextView) findViewById(R.id.to_date);
        from_date = (TextView) findViewById(R.id.from_date);
        textInputLayoutnfcnumber = (TextInputLayout) findViewById(R.id.textInputLayoutnfcnumber);
        textInputEditnfcnumber = (TextInputEditText) findViewById(R.id.textInputEditnfcnumber);
        textInputEditnfcnumber.setSelection(textInputEditnfcnumber.getText().length());
        textInputEditnfcnumber.setEnabled(false);

        recyclerViewVehicle = (RecyclerView) findViewById(R.id.recyclerViewReview);
        cardNumber = (TextView) findViewById(R.id.cardNumber);
        pass = (TextView) findViewById(R.id.pass);
        holderName = (TextView) findViewById(R.id.holderName);
        //addVehicle = (FloatingActionButton)view.findViewById(R.id.addVehicle);
        recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVehicle.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedpref.getString("agentId", "");
        agentId = sharedpref.getString("agentId", "");
        parkingId = sharedpref.getString("parkingId", "");


    }

    public void initListeners() {
        add_card.setOnClickListener(this);
        renew_card.setOnClickListener(this);
        card_detail.setOnClickListener(this);
        to_date.setOnClickListener(this);
        from_date.setOnClickListener(this);
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


    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        to_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        from_date.setText(sdf.format(myCalendar.getTime()));

        if (to_date.getText().toString().equals("")) {
            myCalendar = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateLabel1();
                }

            };
            datePickerDialog = new DatePickerDialog(Subscriptionctivity.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

            Date newDate = myCalendar.getTime();
            datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());

        }
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

    public void id(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("android.nfc.extra.ID");
        Appendable stringBuilder = new StringBuilder(byteArrayExtra.length * 2);
        Formatter formatter = new Formatter(stringBuilder);
        for (int length = byteArrayExtra.length - 1; length > -1; length--) {
            formatter.format("%02X", new Object[]{Byte.valueOf(byteArrayExtra[length])});
        }

        tagID = stringBuilder.toString();
        AppConstants.setString(Subscriptionctivity.this, AppConstants.TAGID, tagID);
        // Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        //nfcDetailForINOut(tagID);
        textInputEditnfcnumber.setText(tagID);
        AppConstants.setString(Subscriptionctivity.this, AppConstants.TAGID, tagID);

        //  Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        if (tagID.equals("")) {
            Toast.makeText(this, "Card Not registered", Toast.LENGTH_SHORT).show();
        } else {
            nfcDetailForINOut(tagID);
        }

        formatter.close();

    }

    public void showdata() {
        String str = textInputEditnfcnumber.getText().toString();
        String fromDate = from_date.getText().toString();
        String toDate = to_date.getText().toString();

        textInputEditnfcnumber.setSelection(textInputEditnfcnumber.getText().length());

        if (str.equals("")) {
            //  textInputEditnfcnumber.setError("Enter NFC Card Number!");
            Toast.makeText(this, "Enter NFC Card Number!", Toast.LENGTH_SHORT).show();
        } else if (fromDate.equals("")) {
            Toast.makeText(this, "Select From Date!", Toast.LENGTH_SHORT).show();
            // from_date.setError("Select Date!");
        } else if (toDate.equals("")) {
            // to_date.setError("Select Date!");
            Toast.makeText(this, "Select To Date!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Subscriptionctivity.this, ShowCardDetail.class);
            intent.putExtra("fromdate", from_date.getText().toString());
            intent.putExtra("toDate", to_date.getText().toString());
            intent.putExtra("NFCCardNumber", textInputEditnfcnumber.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_card:
                Intent intentnav_sub = new Intent(Subscriptionctivity.this, Subscription.class);
                startActivity(intentnav_sub);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.renew_card:
                Intent intentnav = new Intent(Subscriptionctivity.this, RenewCard.class);
                startActivity(intentnav);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.card_detail:
                if (message.equals("Card Not Exist !")) {
                    tagID = "";
                    Toast.makeText(this, "Card Not Registrated", Toast.LENGTH_SHORT).show();
                } else {
                    showdata();
                }


                //else {
////                    if (AppConstants.isInternetAvailable(Subscriptionctivity.this)) {
////                        getDetails(agentId, textInputEditnfcnumber.getText().toString(), from_date.getText().toString(),  to_date.getText().toString());
////
////                    } else {
////                        Toast.makeText(Subscriptionctivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
////                    }
//                    Intent intent = new Intent(Subscriptionctivity.this, ShowCardDetail.class);
//                    intent.putExtra("fromdate", from_date.getText().toString());
//                    intent.putExtra("toDate", to_date.getText().toString());
//                    intent.putExtra("NFCCardNumber", textInputEditnfcnumber.getText().toString());
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//
//                }
                break;

            case R.id.from_date:
                myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateLabel();
                    }

                };
                datePickerDialog = new DatePickerDialog(Subscriptionctivity.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

                Date newDate = myCalendar.getTime();
                datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());
                break;
            case R.id.to_date:
                myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date12 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateLabel1();
                    }

                };
                datePickerDialog = new DatePickerDialog(Subscriptionctivity.this, date12, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

                Date newDate1 = myCalendar.getTime();
                datePickerDialog.getDatePicker().setMaxDate(newDate1.getTime());
                break;
        }
    }


    public void nfcDetailForINOut(String tagid) {
        // Toast.makeText(this, "tagid    " + tagid, Toast.LENGTH_SHORT).show();

        final ProgressDialog pDialog = new ProgressDialog(Subscriptionctivity.this);
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
                    message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");

                    if (status == 0) {
                        Toast.makeText(Subscriptionctivity.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    } else {

                        pDialog.dismiss();

                        Toast.makeText(Subscriptionctivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(Subscriptionctivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_detail?cardNo=");
                    Toast.makeText(Subscriptionctivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(Subscriptionctivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Subscriptionctivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
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


    public void getDetails(String agentId, String cardNo, String startDate, String endDate) {
        final ProgressDialog pDialog = new ProgressDialog(Subscriptionctivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

         /*http://api.opark.in/index.php/v1/parking/pass_uses?agentId=3&cardNo=D96B3D5D&startDate=06/09/2018&endDate=11/09/2018*/
        String urlData = AppConstants.BASEURL + "parking/pass_uses?agentId=" + agentId + "&cardNo=" + cardNo + "&startDate=" + startDate + "&endDate=" + endDate;

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
                        renew_card12.setVisibility(View.VISIBLE);

                        JSONObject checkINresponce = json.getJSONObject("data");

                        holderName.setText(checkINresponce.getString("holderName"));
                        pass.setText(checkINresponce.getString("passHeading"));
                        cardNumber.setText(checkINresponce.getString("cardNo"));

                        JSONArray subArray = checkINresponce.getJSONArray("tableData");

                        for (int i = 0; i < subArray.length(); i++) {

                            JSONObject jsonObject = subArray.getJSONObject(i);

                            PassDetailModel passDetailModel = new PassDetailModel();


                            passDetailModel.setCheckinText(jsonObject.getString("checkinText"));
                            passDetailModel.setCheckoutText(jsonObject.getString("checkoutText"));
                            passDetailModel.setDurationText(jsonObject.getString("durationText"));

                            vehicleModelList.add(passDetailModel);


                        }

                        cardDetailAdapter = new CardDetailAdapter(Subscriptionctivity.this, Subscriptionctivity.this, vehicleModelList);
                        recyclerViewVehicle.setAdapter(cardDetailAdapter);
                        cardDetailAdapter.notifyDataSetChanged();
//                        towAdapter = new TowAdapter(TowedvehicleFragment.this, vehicleModelList);
//                        recyclerViewVehicle.setAdapter(adapter);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(Subscriptionctivity.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_uses?agentId=");
                    Toast.makeText(Subscriptionctivity.this, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/pass_uses?agentId=");
                    Toast.makeText(Subscriptionctivity.this, "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        sendError(error.toString(), "parking/pass_uses?agentId=");
                        Toast.makeText(Subscriptionctivity.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
        pDialog.dismiss();

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(Subscriptionctivity.this)) {
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
