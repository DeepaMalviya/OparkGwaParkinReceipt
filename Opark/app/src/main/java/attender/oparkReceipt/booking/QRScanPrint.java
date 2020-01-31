package attender.oparkReceipt.booking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import attender.oparkReceipt.MainActivity;
import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.subscription.model.CustomRequest;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class QRScanPrint extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private View view;
    public static final String TAG = "QRScanFragmentPrint";
    private QRCodeReaderView qrdecoderview;
    private Context context;
    private String mQRCode = "";
    private String data = "";
    String tagID = "";
    private TextView tvAvailableSpots;
    private TextView tvTotalSpots;
    private NfcAdapter mNfcAdapter;
    SharedPreferences sharedpref;
    String agentId, userRole, userName, userContactNo, vendorId, vendorName, parkingName, parkingType, parkingId,
            parkingVehicle, sidefour, userType = "", parkingType1234, mobile, userTypeOnline;
    String getReceiptHeadingOut, getParkingAddressOut, getCheckInDateOut, getCheckOutDateOut, getVehicleNoOut,
            getPoweredByOut, getCompanyWebsiteOut, getReceiptNoOut, getduration, getDurationUnit, getCurrencySymbol, getGrandTotal, Acticityclass = "";
    private ServiceManager mServiceManager = null;
    Printer printer;
    String getReceiptHeading, getParkingAddress, getCheckInDate, getVehicleNo, getParkingRate, getAdditionalParkingRate, barCode, onlineUserText, getQrCode, ReceiptStaticText,
            getAgentName, getReceiptMobile, getPoweredBy, getCompanyWebsite, getReceiptNo, getprintReceipt, Two_Wheeler = "", Wheeler = "";

    String[] out = new String[5];
    String getName, getType, getID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan_print);
        printer = WeiposImpl.as().openPrinter();

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("parkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        //Toast.makeText(QRScanPrint.this, "parkingId"+parkingId, Toast.LENGTH_SHORT).show();
        mobile = sharedpref.getString("mobile", "");
        getID = sharedpref.getString("getID", "");
        getName = sharedpref.getString("getName", "");
        getType = sharedpref.getString("getType", "");
        parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType1234 = sharedpref.getString("VehicleType1", "");
        Wheeler = sharedpref.getString("Wheeler", "");
        //Toast.makeText(QRScanPrint.this, "parkingType"+parkingType, Toast.LENGTH_SHORT).show();


        Intent intent = getIntent();
        userTypeOnline = intent.getStringExtra("userType");
        Two_Wheeler = intent.getStringExtra("Wheeler");
        Acticityclass = intent.getStringExtra("Acticityclass");

//        String pType[] = parkingType.toString().split(",");
//
//
//        pType2 = pType[0];
//        pType4 = pType[1];

        findViews();
        initNFC();
    }

    private void findViews() {
        qrdecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrdecoderview.setOnQRCodeReadListener(QRScanPrint.this);
        // Use this function to enable/disable decoding
        qrdecoderview.setQRDecodingEnabled(true);
        // Use this function to change the autofocus interval (default is 5 secs)
        qrdecoderview.setAutofocusInterval(2000L);
        // Use this function to enable/disable Torch
        qrdecoderview.setTorchEnabled(true);
        // Use this function to set front camera preview
        qrdecoderview.setFrontCamera();
        // Use this function to set back camera preview
        qrdecoderview.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        disableView(false);
        mQRCode = text;
        AppConstants.log(TAG, "QR code Data - " + mQRCode);
        setData(text);

    }

    private void setData(String qrCodeData) {
        data = qrCodeData.toString().trim();

        if (userTypeOnline.equals("Online")) {
            if (AppConstants.isInternetAvailable(QRScanPrint.this)) {
                Log.e(TAG, "setData:parkingId " + parkingId);
                Log.e(TAG, "setData:Two_Wheeler " + Two_Wheeler);
                Log.e(TAG, "setData:data " + data);
                Log.e(TAG, "setData:agentId " + agentId);
                Log.e(TAG, "setData:userTypeOnline " + userTypeOnline);
                Log.e(TAG, "setData:tagID " + tagID);

                vehicleCheckIn(parkingId, Two_Wheeler, data, agentId, userTypeOnline, "", tagID);
            } else {
                Toast.makeText(QRScanPrint.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            }
        } else {

            if (AppConstants.isInternetAvailable(QRScanPrint.this)) {

                out = data.split(",");
                if (out.length == 1) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(QRScanPrint.this);
                    builder1.setMessage("Invalid QRCode!").setIcon(R.drawable.oparklogonew).setTitle("Opark")
                            .setCancelable(false)
                            .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intentprint = new Intent(QRScanPrint.this, MainActivity.class);
                                    startActivity(intentprint);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();

                                }
                            });

                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                } else {
                    String mobileNumber = out[0];
                    String vehicleNumber = out[1];
                    String vehicleType = out[2];
                    String userType = out[3];
                    Log.e(TAG, "setData:parkingId " + parkingId);
                    Log.e(TAG, "setData:vehicleType " + vehicleType);
                    Log.e(TAG, "setData:vehicleNumber " + vehicleNumber);
                    Log.e(TAG, "setData:agentId " + agentId);
                    Log.e(TAG, "setData:userType " + userType);
                    Log.e(TAG, "setData:mobileNumber " + mobileNumber);
                    Log.e(TAG, "setData:tagID " + tagID);
                    vehicleCheckOut(parkingId, vehicleType, vehicleNumber, agentId, userType, mobileNumber, tagID);

                }
               /* String mobileNumber = out[0];
                String vehicleNumber = out[1];
                String vehicleType = out[2];
                String userType = out[3];*/

                // vehicleCheckOut(parkingId, vehicleType, vehicleNumber, agentId, userType, mobileNumber, tagID);


            } else {
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void vehicleCheckIn(final String parkingId, String parkingType, String vehicleNo, final String agentId, String mode, String userdata, String cardNo) {
        final ProgressDialog pDialog = new ProgressDialog(QRScanPrint.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        /*http://staggingapi.opark.in/index.php/v1/parking/inventory?parkingId=1&parkingType=2W*/

        /*http://staggingapi.opark.in/index.php/v1/parking/checkin?parkingId=9&parkingType=4Wheeler&vehicleNo=1258&agentId=&mode=Normal&userdata=&cardNo=*/

        String urlData = AppConstants.BASEURL + "parking/checkin?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo;
        Log.e(TAG, "vehicleCheckIn:urlData==== " + urlData);
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
                        CheckInModel checkInModel = new CheckInModel();

                        checkInModel.setReceiptHeading(checkINresponce.getString("receiptHeading"));
                        checkInModel.setParkingAddress(checkINresponce.getString("parkingAddress"));
                        checkInModel.setUserContactNo(checkINresponce.getString("userContactNo"));
                        checkInModel.setCheckInDate(checkINresponce.getString("checkInDate"));
                        checkInModel.setAgentId(checkINresponce.getString("agentId"));
                        checkInModel.setAvailableSlots(checkINresponce.getString("availableSlots"));
                        checkInModel.setParkingId(checkINresponce.getString("parkingId"));
                        checkInModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        checkInModel.setParkingRate(checkINresponce.getString("parkingRate"));
                        checkInModel.setAdditionalParkingRate(checkINresponce.getString("additionalParkingRate"));
                        checkInModel.setMode(checkINresponce.getString("mode"));
                        checkInModel.setReceiptStaticText(checkINresponce.getString("receiptStaticText"));
                        checkInModel.setReceiptEmail(checkINresponce.getString("receiptEmail"));
                        checkInModel.setReceiptMobile(checkINresponce.getString("receiptMobile"));
                        checkInModel.setReceiptWebsite(checkINresponce.getString("receiptWebsite"));
                        //checkInModel.setBarcode(checkINresponce.getString("barcode"));
                        checkInModel.setResponseType(checkINresponce.getString("responseType"));
                        checkInModel.setParkingType(checkINresponce.getString("parkingType"));
                        checkInModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                        checkInModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                        checkInModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                        checkInModel.setQrCode(checkINresponce.getString("barCode"));
                        checkInModel.setAgentName(checkINresponce.getString("agentName"));
                        checkInModel.setCardNo(checkINresponce.getString("cardNo"));
                        checkInModel.setOnlineUserText(checkINresponce.getString("onlineUserText"));

                        //  Toast.makeText(MainActivity.this, "Parking Done!" + message, Toast.LENGTH_LONG).show();


                        getReceiptHeading = checkInModel.getReceiptHeading();
                        getParkingAddress = checkInModel.getParkingAddress();
                        getCheckInDate = checkInModel.getCheckInDate();
                        getVehicleNo = checkInModel.getVehicleNo();
                        getParkingRate = checkInModel.getParkingRate();
                        getAdditionalParkingRate = checkInModel.getAdditionalParkingRate();
                        getQrCode = checkInModel.getQrCode();
                        getAgentName = checkInModel.getAgentName();
                        getReceiptMobile = checkInModel.getReceiptMobile();
                        getPoweredBy = checkInModel.getPoweredBy();
                        getCompanyWebsite = checkInModel.getCompanyWebsite();
                        getReceiptNo = checkInModel.getReceiptNo();
                        ReceiptStaticText = checkInModel.getReceiptStaticText();
                        barCode = checkInModel.getQrCode();
                        String cardNo = checkInModel.getCardNo();
                        String mobile = checkInModel.getUserContactNo();
                        onlineUserText = checkInModel.getOnlineUserText();

                        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("parkingPrint", "checkInModel");
                        editor.apply();
                        editor.commit();

                        if (cardNo.equals("")) {
                            printReceipt();
                        }
                        // Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();


                    } else {
                        Toast.makeText(QRScanPrint.this, message, Toast.LENGTH_LONG).show();
                        Intent intentprint = new Intent(QRScanPrint.this, MainActivity.class);
                        startActivity(intentprint);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        pDialog.dismiss();

                    }
                    // pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/checkin?parkingId=");
                    Toast.makeText(context, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Technical Error...", Toast.LENGTH_SHORT).show();
                    sendError(e.toString(), "parking/checkin?parkingId=");
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(error.toString(), "parking/checkin?parkingId=");
                // Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(QRScanPrint.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) { sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) { sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) { sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof NoConnectionError) { sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof TimeoutError) { sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ParseError) { sendError(error.toString(), "parking/checkin?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void vehicleCheckOut(final String parkingId, String parkingType, String vehicleNo, String agentId, String mode, String userdata, String cardNo) {
        final ProgressDialog pDialog = new ProgressDialog(QRScanPrint.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
         /*http://staggingapi.opark.in/index.php/v1/parking/checkout?parkingId=1&parkingType=4Wheeler&vehicleNo=6565&agentId=3&mode=Normal&userdata=9993289838*/

        String urlData = AppConstants.BASEURL + "parking/checkout?parkingId=" + parkingId + "&parkingType=" + parkingType + "&vehicleNo=" + vehicleNo + "&agentId="
                + agentId + "&mode=" + mode + "&userdata=" + userdata + "&cardNo=" + cardNo;
        Log.e(TAG, "vehicleCheckOut:urlData==== " + urlData);

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

                        //  availableSlotSevice(parkingId, parkingType1234);

                        JSONObject checkINresponce = response.getJSONObject("data");

                        CheckOutModel checkOutModel = new CheckOutModel();

                        String rh = checkINresponce.getString("receiptHeading");

                        checkOutModel.setReceiptHeading(checkINresponce.getString("receiptHeading"));
                        checkOutModel.setParkingAddress(checkINresponce.getString("parkingAddress"));
                        checkOutModel.setVehicleNo(checkINresponce.getString("vehicleNo"));
                        checkOutModel.setCheckInDate(checkINresponce.getString("checkInDate"));
                        checkOutModel.setCheckOutDate(checkINresponce.getString("checkOutDate"));
                        checkOutModel.setCheckInTime(checkINresponce.getString("checkInTime"));
                        checkOutModel.setCheckOutTime(checkINresponce.getString("checkOutTime"));
                        checkOutModel.setDuration(checkINresponce.getString("duration"));
                        checkOutModel.setDurationUnit(checkINresponce.getString("durationUnit"));
                        checkOutModel.setGrandTotal(checkINresponce.getString("grandTotal"));
                        checkOutModel.setCurrencySymbol(checkINresponce.getString("currencySymbol"));
                        checkOutModel.setUserContactNo(checkINresponce.getString("userContactNo"));
                        checkOutModel.setAgentId(checkINresponce.getString("agentId"));
                        checkOutModel.setAvailableSlots(checkINresponce.getString("availableSlots"));
                        checkOutModel.setParkingId(checkINresponce.getString("parkingId"));
                        checkOutModel.setParkingRate(checkINresponce.getString("parkingRate"));
                        checkOutModel.setTotal(checkINresponce.getString("total"));
                        checkOutModel.setMode(checkINresponce.getString("mode"));
                        //  checkOutModel.setBarcode(checkINresponce.getString("barCode"));
                        checkOutModel.setResponseType(checkINresponce.getString("responseType"));
                        checkOutModel.setMinimumAmount(checkINresponce.getString("minimumAmount"));
                        checkOutModel.setParkingType(checkINresponce.getString("parkingType"));
                        checkOutModel.setCompanyWebsite(checkINresponce.getString("companyWebsite"));
                        checkOutModel.setPoweredBy(checkINresponce.getString("poweredBy"));
                        checkOutModel.setReceiptNo(checkINresponce.getString("receiptNo"));
                        checkOutModel.setCardNo(checkINresponce.getString("cardNo"));
                        checkOutModel.setOnlineUserText(checkINresponce.getString("onlineUserText"));

                        String heading = checkOutModel.getReceiptHeading();
                        String cardNo = checkINresponce.getString("cardNo");

                        getReceiptHeadingOut = checkOutModel.getReceiptHeading();
                        getParkingAddressOut = checkOutModel.getParkingAddress();
                        getVehicleNoOut = checkOutModel.getVehicleNo();
                        getCheckInDateOut = checkOutModel.getCheckInDate();
                        getCheckOutDateOut = checkOutModel.getCheckOutDate();
                        getduration = checkOutModel.getDuration();
                        getDurationUnit = checkOutModel.getDurationUnit();
                        getCurrencySymbol = checkOutModel.getCurrencySymbol();
                        getGrandTotal = checkOutModel.getGrandTotal();
                        getPoweredByOut = checkOutModel.getPoweredBy();
                        getReceiptNoOut = checkOutModel.getReceiptNo();
                        getCompanyWebsiteOut = checkOutModel.getCompanyWebsite();
                        onlineUserText = checkOutModel.getOnlineUserText();
                        printReceipt();
                        pDialog.dismiss();


                    } else {
                        Toast.makeText(QRScanPrint.this, message, Toast.LENGTH_SHORT).show();
                        Intent intentprint = new Intent(QRScanPrint.this, MainActivity.class);
                        startActivity(intentprint);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();

                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    sendError(e.toString(), "parking/checkout?parkingId=");
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Technical Error...", Toast.LENGTH_SHORT).show();
                    sendError(e.toString(), "parking/checkout?parkingId=");
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sendError(error.toString(), "parking/checkout?parkingId=");
                // Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(QRScanPrint.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ServerError) {
                    sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof NoConnectionError) {
                    sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof TimeoutError) {
                    sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } else if (error instanceof ParseError) {
                    sendError(error.toString(), "parking/checkout?parkingId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(QRScanPrint.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(QRScanPrint.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });


            }
        });
        try {
            printNormal(QRScanPrint.this, printer);

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

        if (userTypeOnline.equals("Online")) {
            CheckInModel checkInModel = new CheckInModel();
            //checkInModel.getReceiptHeading();

            printer.printText(getReceiptHeading, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.LEFT);

            printer.printText(getParkingAddress,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("---------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("     " + getCheckInDate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("VEHICLENO.-" + getVehicleNo,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getAdditionalParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(onlineUserText,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

            printer.printQrCode(barCode, 200, Printer.Gravity.CENTER);

            // printer.printBarCode(barCode,1,100,1);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("ATTENDANT - " + getAgentName + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

           /* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
*/
            printer.printText("Call Attendant:" + getReceiptMobile, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Powered By:-" + getPoweredBy, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(getCompanyWebsite, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Receipt No. -  " + getReceiptNo, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);


            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            if (Acticityclass.equals("FourWheelerActivity")) {
                Intent intentprint = new Intent(QRScanPrint.this, FourWheeler.class);
                startActivity(intentprint);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
            if (Acticityclass.equals("TwoWheelerActivity")) {
                Intent intentprint = new Intent(QRScanPrint.this, FourWheeler.class);
                startActivity(intentprint);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
            if (Acticityclass.equals("MainActicity")) {
                Intent intentprint = new Intent(QRScanPrint.this, MainActivity.class);
                startActivity(intentprint);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
           /* Intent intentprint = new Intent(QRScanPrint.this, MainActivity.class);
            startActivity(intentprint);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();*/

            //availableSlotSevice(parkingId, parkingType, agentId);
        } else {
            printer.printText(getReceiptHeadingOut, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(getParkingAddressOut,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText("--------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(onlineUserText,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.BOLD, Printer.Gravity.LEFT);

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

            // availableSlotSevice(parkingId, parkingType1234);

            Intent intentprint = new Intent(QRScanPrint.this, MainActivity.class);
            startActivity(intentprint);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
    }

    private void disableView(boolean isEnable) {
        qrdecoderview.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        qrdecoderview.setEnabled(isEnable);
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

        AppConstants.setString(QRScanPrint.this, AppConstants.TAGID, tagID);

        //  Toast.makeText(this, tagID, Toast.LENGTH_SHORT).show();
        if (tagID.equals("")) {
            Toast.makeText(this, "Card Not register", Toast.LENGTH_SHORT).show();
        } else {
            //nfcDetailForINOut(tagID);
        }
        formatter.close();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        if (tag != null) {
            // Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_LONG).show();
            Ndef ndef = Ndef.get(tag);
        }
        if (intent.getByteArrayExtra("android.nfc.extra.ID") != null) {
            Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);

            id(intent);
        }
    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(QRScanPrint.this)) {
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
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(QRScanPrint.this,
                                "Oops. Network Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(QRScanPrint.this,
                                "Oops. Server error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(QRScanPrint.this,
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(QRScanPrint.this,
                                "Oops. Parse Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(QRScanPrint.this,
                                "Oops. No Connection Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(QRScanPrint.this,
                                "Oops. Timeout Error!",
                                Toast.LENGTH_LONG).show();
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
