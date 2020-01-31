package attender.oparkReceipt.vehiclelist.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.webkit.WebView;
import android.widget.Toast;

import attender.oparkCard.R;

import attender.oparkReceipt.base.AppConstants;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.ServiceManager;
import cn.weipass.pos.sdk.Weipos;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class WebViewPrint extends AppCompatActivity {

    //  String transactionId, parkingId, parkingType, mode, receipt;

    private String RECEIPT_URL = "";
    private WebView webview;
    Button btnPrint;
    //  String qrCode;
    protected ProgressDialog progressDialog;
    SharedPreferences sharedpref;
    //  String name, password, role, sparkingId1, AttendantProfileName, sAgentId1;
    private ServiceManager mServiceManager = null;
    Printer printer;
    Intent intent;


    String transactionId = "", receiptHeading = "", parkingAddress = "", userContactNo = "", checkInDate = "", agentId = "", availableSlots = "", parkingId = "", vehicleNo1 = "", parkingRate = "",
            additionalParkingRate = "", mode = "", receiptStaticText = "", receiptEmail = "", receiptMobile = "", receiptWebsite = "", receipt = "", barcode, responseType = "", parkingType = "",
            companyWebsite = "", poweredBy = "", receiptType = "", receiptNo = "", qrCode = "", agentName = "", checkOutDate = "", duration = "", durationUnit = "", currencySymbol = "",
            grandTotal = "", printReceipt = "", lastLine = "", reprintText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        printer = WeiposImpl.as().openPrinter();

        init();
        initSdk();


    }

    public void init() {
        intent = getIntent();

        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);

        if (storeAllValues.getString("parkingPrint", "").equals("checkInModel")) {
            transactionId = intent.getStringExtra("transactionId");
            agentName = intent.getStringExtra("aName");
            qrCode = intent.getStringExtra("qrCode");
            receiptNo = intent.getStringExtra("receiptNo");
            receiptType = intent.getStringExtra("receiptType");
            poweredBy = intent.getStringExtra("poweredBy");
            companyWebsite = intent.getStringExtra("companyWebsite");
            responseType = intent.getStringExtra("responseType");
            receiptWebsite = intent.getStringExtra("receiptWebsite");
            receiptMobile = intent.getStringExtra("receiptMobile");
            receiptEmail = intent.getStringExtra("receiptEmail");
            receiptStaticText = intent.getStringExtra("receiptStaticText");
            additionalParkingRate = intent.getStringExtra("additionalParkingRate");
            parkingRate = intent.getStringExtra("parkingRate");
            vehicleNo1 = intent.getStringExtra("vn");
            availableSlots = intent.getStringExtra("availableSlots");
            agentId = intent.getStringExtra("agentId");
            checkInDate = intent.getStringExtra("checkInDate");
            userContactNo = intent.getStringExtra("userContactNo");
            parkingAddress = intent.getStringExtra("parkingAddress");
            receiptHeading = intent.getStringExtra("receiptHeading");
            parkingId = intent.getStringExtra("parkingId");
            parkingType = intent.getStringExtra("parkingType");
            mode = intent.getStringExtra("mode");
            receipt = intent.getStringExtra("receipt");
            lastLine = intent.getStringExtra("lastLine");
            reprintText = intent.getStringExtra("reprintText");
            //  printReceipt = intent.getStringExtra("printReceipt");

            RECEIPT_URL = receipt;
        }
        if (storeAllValues.getString("parkingPrint", "").equals("checkOUTModel")) {

            receiptHeading = intent.getStringExtra("receiptHeading");
            parkingAddress = intent.getStringExtra("parkingAddress");
            vehicleNo1 = intent.getStringExtra("vehicleNo1");
            checkInDate = intent.getStringExtra("checkInDate");
            checkOutDate = intent.getStringExtra("checkOutDate");
            duration = intent.getStringExtra("duration");
            durationUnit = intent.getStringExtra("durationUnit");
            currencySymbol = intent.getStringExtra("currencySymbol");
            grandTotal = intent.getStringExtra("grandTotal");
            receipt = intent.getStringExtra("receipt");
            poweredBy = intent.getStringExtra("poweredBy");
            receiptNo = intent.getStringExtra("receiptNo");
            companyWebsite = intent.getStringExtra("companyWebsite");
            reprintText = intent.getStringExtra("reprintText");
            RECEIPT_URL = receipt;
        }


        btnPrint = (Button) findViewById(R.id.bill);
//        VehicleModelDetails vehicleModelDetails = new VehicleModelDetails();
//        String aname = vehicleModelDetails.getAgentName();
//        String vNumber = vehicleModelDetails.getVehicleNo();
//        String pType = vehicleModelDetails.getParkingType();
//        String vN = vNumber + pType;

//        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);
//        name = sharedpref.getString("username", "");
//        password = sharedpref.getString("userpassword", "");
//        role = sharedpref.getString("role", "");
//        sparkingId1 = sharedpref.getString("parkingId", "");
//        sAgentId1 = sharedpref.getString("AgentId", "");
//        AttendantProfileName = sharedpref.getString("AttendantProfileName", "");

        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        if (AppConstants.isInternetAvailable(WebViewPrint.this) && !AppConstants.isBlank(RECEIPT_URL)) {
            showProgressDialog();
            webview.loadUrl(RECEIPT_URL);

        } else {
            getFragmentManager().popBackStack();
        }

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printReceipt();
            }
        });
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissProgressDialog();
        }
    }

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(WebViewPrint.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initSdk() {

        mServiceManager = WeiposImpl.as().openServiceManager();
        WeiposImpl.as().init(WebViewPrint.this, new Weipos.OnInitListener() {

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

    private void printReceipt() {
        if (printer == null) {
            Toast.makeText(WebViewPrint.this, "No Printer found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WebViewPrint.this, message, Toast.LENGTH_SHORT).show();
//                        showResultInfo("Print Exception", "Error", message);
                    }
                });
            }
        });
        try {
            printNormal(WebViewPrint.this, printer);

        } catch (Exception e) {

        }
    }

    public void printNormal(Context newEntryActivity, Printer printer) {

        SharedPreferences storeAllValues = getSharedPreferences("opark", Context.MODE_PRIVATE);

        if (storeAllValues.getString("parkingPrint", "").equals("checkInModel")) {
            printer.printText(receiptHeading, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.LEFT);

            printer.printText(parkingAddress,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("---------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText("     " + checkInDate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("VEHICLENO.-" + vehicleNo1,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(parkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(additionalParkingRate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printQrCode(qrCode, 230, Printer.Gravity.CENTER);

            // printer.printBarCode(barCode,1,100,1);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.SMALL, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);


            printer.printText("ATTENDANT - " + agentName + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

           /* printer.printText(ReceiptStaticText + "\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
*/
            printer.printText("Helpline No:" + receiptMobile, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Powered By:-" + poweredBy, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(companyWebsite, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("Receipt No. -  " + receiptNo, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText(lastLine, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD, Printer.Gravity.CENTER);

            printer.printText("        "+reprintText, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD, Printer.Gravity.LEFT);


            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);
        }
        if (storeAllValues.getString("parkingPrint", "").equals("checkOUTModel")) {
            printer.printText(receiptHeading, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);

            printer.printText(parkingAddress,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText("--------------", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.BOLD,
                    Printer.Gravity.CENTER);
//
            printer.printText("VNO.-" + vehicleNo1,
                    Printer.FontFamily.SONG, Printer.FontSize.LARGE,
                    Printer.FontStyle.BOLD, Printer.Gravity.RIGHT);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("In-  " + checkInDate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);
            printer.printText("Out- " + checkOutDate,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.RIGHT);

            printer.printText("", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText(duration + " " + durationUnit + " " + currencySymbol + " " + String.valueOf(grandTotal),
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);
            printer.printText("Powered By:- " + poweredBy,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

            printer.printText(companyWebsite, Printer.FontFamily.SONG,
                    Printer.FontSize.MEDIUM, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);

            printer.printText("Receipt No. -  " + receiptNo,
                    Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

            printer.printText(reprintText, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM, Printer.FontStyle.BOLD, Printer.Gravity.CENTER);


            printer.printText("\n", Printer.FontFamily.SONG,
                    Printer.FontSize.EXTRALARGE, Printer.FontStyle.NORMAL,
                    Printer.Gravity.CENTER);
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

}
