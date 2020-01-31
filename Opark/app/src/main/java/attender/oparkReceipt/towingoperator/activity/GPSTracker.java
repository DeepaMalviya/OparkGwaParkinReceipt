package attender.oparkReceipt.towingoperator.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GPSTracker extends Service implements LocationListener {

    private Context mContext = this;
    private int battryPer;
    private JSONObject json, json2;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private SharedPreferences pref, spf_user_id;
    private String userid, parentid;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 1 minute
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    LocationListener locationListener;
    private ProgressDialog progress;


    public GPSTracker() {
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        //progress = new ProgressDialog(getApplicationContext());
        getLocation();
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                if (isGPSEnabled) {
                    if (location == null) {


                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                //Toast.makeText(mContext, "GPS", Toast.LENGTH_LONG).show();
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    if (isNetworkEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                            Log.d("Network", "Network Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    // Toast.makeText(mContext, "Network", Toast.LENGTH_LONG).show();
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

     @Override
    public void onDestroy() {
        super.onDestroy();
        // stopUsingGPS();
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            NumberFormat formatter = new DecimalFormat("#0.0000000");
            latitude = location.getLatitude();
            String lat = formatter.format(latitude);
            latitude = Double.parseDouble(lat);

        }

        // return latitude
        return latitude;
    }

    public Location fetchLocationShowDialog() {
        Location loc = getLocation();
        if (loc == null) {

            progress = ProgressDialog.show(getApplicationContext(), "Fetching Location...",
                    "Your current location is fetching Please wait..", true);

        }
        return loc;

    }


    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            //longitude = location.getLongitude();
            NumberFormat formatter = new DecimalFormat("#0.0000000");
            longitude = location.getLongitude();
            String log = formatter.format(longitude);
            longitude = Double.parseDouble(log);
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showAlertDialog(String title, String message, final String posBtnTitle) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        // alertDialog.setTitle("Fetching Location...");
        alertDialog.setTitle(title);

        // Setting Dialog Message
        //
        // alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setMessage(message);

        // On pressing Settings button "Settings"
        alertDialog.setPositiveButton(posBtnTitle, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (posBtnTitle.equals("Ok")) {
                    dialog.cancel();

                } else {

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        //progress.dismiss();
        progress = new ProgressDialog(mContext);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }




}
