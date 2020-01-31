package attender.oparkReceipt.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import attender.oparkReceipt.MainActivity;
import attender.oparkCard.R;
import attender.oparkReceipt.login.Login;
import attender.oparkReceipt.parkinglist.ParkingList;
import attender.oparkReceipt.towingoperator.activity.AddTowVehicle;

public class Splash extends AppCompatActivity {

    private Handler handler = null;
    private SplashThread splashThread = null;
    private long DELAY_MILLIS = 5000;
    public static String TAG = "SplashFragment";
    SharedPreferences sharedpref;
    String USER_ID, userRole, userName, userContactNo, vendorId, vendorName, parkingName, parkingType, parkingId, parkingVehicle, VehicleType1, getParkingID, getParkingType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        USER_ID = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("parkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        parkingVehicle = sharedpref.getString("parkingVehicle", "");
        VehicleType1 = sharedpref.getString("VehicleType1", "");
        getParkingID = sharedpref.getString("getParkingID", "");
        getParkingType = sharedpref.getString("VehicleType1", "");

        initviews();
    }

    private void initviews() {
        handler = new Handler();
        splashThread = new SplashThread();
        handler.postDelayed(splashThread, DELAY_MILLIS);
    }

    private class SplashThread implements Runnable {
        @Override
        public void run() {
            try {
                if (userName.equals("") && userRole.equals("") && USER_ID.equals("") && parkingId.equals("")) {
                    Intent intentSplash = new Intent(Splash.this, Login.class);
                    startActivity(intentSplash);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    if (userRole.equals("Towing Operator")) {
                        Intent intentSplash = new Intent(Splash.this, AddTowVehicle.class);
                        startActivity(intentSplash);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else {
                        if (getParkingID.equals("")) {
                            Intent intentSplash = new Intent(Splash.this, ParkingList.class);
                            startActivity(intentSplash);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            if (getParkingType.equals("")) {
                                Intent intentSplash = new Intent(Splash.this, ParkingList.class);
                                startActivity(intentSplash);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            } else {
                                Intent intentSplash = new Intent(Splash.this, MainActivity.class);
                                startActivity(intentSplash);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }

                        }
                    }

                }


            } catch (Exception err) {
                initviews();
                err.printStackTrace();
            }
        }
    }
}