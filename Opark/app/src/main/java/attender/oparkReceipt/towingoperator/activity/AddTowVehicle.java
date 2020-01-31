package attender.oparkReceipt.towingoperator.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.provider.Settings;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mukesh.permissions.AppPermissions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.base.VolleyMultipartRequest;
import attender.oparkReceipt.base.VolleySingleton;
import attender.oparkReceipt.booking.AvailableSlotModel;
import attender.oparkReceipt.login.Login;
import attender.oparkReceipt.subscription.model.PopulateSpinnerPojo;

import attender.oparkReceipt.towingoperator.model.SingleShotLocationProvider;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddTowVehicle extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "AddTowVehicle";
    Toolbar toolBar;
    private NavigationView navigationView;
    private TextView textToolHeader;
    CircleImageView toolBarUserAvatar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    ProgressDialog dialog;
    String vendorId, agentId, userRole, userName, userContactNo, vendorName, subPlanId, vehicleNumber, getparkingType;
    SharedPreferences sharedpref;
    Uri imageUri;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap bitmapImage;
    Bitmap bm;
    ImageView camera;
    private String selectedPath = "";
    ImageView car_logo;
    RadioGroup radioGroup;
    RadioButton twowheeler, fourwheeler;
    Button submit;
    String vehicleType = "";
    EditText vehicleno;
    Spinner storelocation;
    protected Location mLastLocation;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private String mLat = "";
    private String mLong = "";
    private static final int REQUEST_CODE_GPS_SETTINGS = 101;
    private TextView tvAvailableSpots, nameHeader, headerEmail;
    private TextView tvTotalSpots, attenderName;
    String latt, lng;
    String value = "";
    String latitude = "";
    String longitude = "";
    String source_lat, source_long;
    int cid, mcc, mnc, lac;
    String networkOperator;
    ArrayList<String> plannames = new ArrayList<>();
    ArrayList<PopulateSpinnerPojo> planeList = new ArrayList<>();
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int ALL_REQUEST_CODE = 0;
    GPSTracker gps;
    private AppPermissions mRuntimePermission;
    private static final String[] ALL_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_tow_vehicle);

        mRuntimePermission = new AppPermissions(this);

        gps = new GPSTracker(getApplicationContext());

        findViews();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        networkOperator = telephonyManager.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            mcc = Integer.parseInt(networkOperator.substring(0, 3));
            mnc = Integer.parseInt(networkOperator.substring(3));
        }//40478

        cid = cellLocation.getCid();
        lac = cellLocation.getLac();


        if (AppConstants.isInternetAvailable(AddTowVehicle.this)) {
            planService(vendorId);
        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkGpsAndGetLocation() {
        if (AppConstants.isGPSEnabled(AddTowVehicle.this)) {
            getLocation(AddTowVehicle.this);
        } else if (AppConstants.isNetworkProviderAvailable(AddTowVehicle.this)) {
            getLocation(AddTowVehicle.this);
        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_GPS_SETTINGS);
        }
    }

    public void getLocation(final Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        AppConstants.log("Location", "my location is " + location.latitude + "  " + location.longitude);
                        mLat = location.latitude + "";
                        mLong = location.longitude + "";

                        //   Toast.makeText(AddTowVehicle.this, mLat + " " + mLong, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddTowVehicle.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            } else {
                checkGpsAndGetLocation();
            }
        } else {
            checkGpsAndGetLocation();
        }

        if (AppConstants.isInternetAvailable(AddTowVehicle.this)) {
            availableSlotSevice(vendorId);
        } else {
            Toast.makeText(AddTowVehicle.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
//            } else {
//                checkGpsAndGetLocation();
//            }
//        } else {
//            checkGpsAndGetLocation();
//        }

    }


    private void findViews() {

        tvAvailableSpots = (TextView) findViewById(R.id.tvAvailableSpots);
        tvTotalSpots = (TextView) findViewById(R.id.tvTotalSpots);
        vehicleno = (EditText) findViewById(R.id.branch);
        storelocation = (Spinner) findViewById(R.id.storelocation);
        fourwheeler = (RadioButton) findViewById(R.id.fourwheeler);
        twowheeler = (RadioButton) findViewById(R.id.twowheeler);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        camera = (ImageView) findViewById(R.id.camera);
        submit = (Button) findViewById(R.id.submit);

        twowheeler.setChecked(true);
        vehicleType = "2Wheeler";


        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        textToolHeader = (TextView) findViewById(R.id.toolbar_title);
        toolBarUserAvatar = (CircleImageView) toolBar.findViewById(R.id.toolBarUserAvatar1);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(AddTowVehicle.this, DividerItemDecoration.VERTICAL));

        sharedpref = getSharedPreferences("opark", Context.MODE_PRIVATE);

        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");


        car_logo = (ImageView) findViewById(R.id.car_logo);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleNumber = vehicleno.getText().toString();

                if (vehicleNumber.isEmpty()) {
                    vehicleno.setError("Enter Vehicle Number!");
                    Toast.makeText(AddTowVehicle.this, "Enter Vehicle Number!", Toast.LENGTH_SHORT).show();
                   // vehicleno.requestFocus();
                } else {
                    if (setSpinnerError(storelocation, "Select Parking")) {

                        if (imageUri != null) {
                            if (AppConstants.isInternetAvailable(AddTowVehicle.this)) {
                                Toast.makeText(AddTowVehicle.this, latitude + "  ,  " + longitude, Toast.LENGTH_SHORT).show();

                                AddTowVehicleservice(vehicleType, agentId, mLat, mLong, subPlanId, vehicleNumber);
                            } else {
                                Toast.makeText(AddTowVehicle.this, "Internet Connection Required", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddTowVehicle.this, "Take Vehicle Image", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (mRuntimePermission.hasPermission(Manifest.permission.CAMERA)) {
                    Toast.makeText(AddTowVehicle.this, "Camera permission already given", Toast.LENGTH_SHORT).show();
                    selectImage();
                } else {
                    mRuntimePermission.requestPermission(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE);
                }*/
                if (mRuntimePermission.hasPermission(ALL_PERMISSIONS)) {
                    selectImage();
                    // Toast.makeText(AddTowVehicle.this, "All permission already given", Toast.LENGTH_SHORT).show();
                } else {
                    mRuntimePermission.requestPermission(AddTowVehicle.this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
                }
            }
        });


        if (userRole.equals("Towing Operator")) {
            textToolHeader.setText("Towing Operator");
            navigationView.getMenu().findItem(R.id.nav_twowheeler).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_carInActivityTwo).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_carOutActivityFour).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_fourwheeler).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_detail).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_renew).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_shiftreport).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_sub).setVisible(false);

            //  toolBarUserAvatar.setImageResource(R.mipmap.twobike);
        }

        setListener();


    }

    public void showAlertDialog(String title, String message, final String posBtnTitle) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddTowVehicle.this);

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
                    startActivity(intent);
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

    private void setListener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twowheeler) {
                    fourwheeler.setChecked(false);
                    vehicleType = "2W";
                }
                if (checkedId == R.id.fourwheeler) {
                    twowheeler.setChecked(false);
                    vehicleType = "4W";

                }

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {

                    case R.id.towvhicle:
                        Intent intentnav_sub = new Intent(AddTowVehicle.this, TowingOperator.class);
                        startActivity(intentnav_sub);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_download:
                        new DownloadNewVersion().execute();
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTowVehicle.this);
                        builder.setMessage("Are you sure you want to Logout?").setIcon(R.drawable.oparklogonew).setTitle("Opark")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //getDetails(agentId, "1");

                                        getSharedPreferences("opark", Context.MODE_PRIVATE).edit().remove("agentId").
                                                remove("userRole").remove("userName").remove("userContactNo").remove("vendorId").remove("vendorName").commit();

                                        Intent logout_intent1 = new Intent(AddTowVehicle.this, Login.class);
                                        logout_intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(logout_intent1);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                }
                return true;
            }

        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,
                R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();


        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddTowVehicle.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = AppConstants.checkPermission(AddTowVehicle.this);

                cameraIntent();

                /*if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } *//*else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                }*//* else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }*/
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GPS_SETTINGS && resultCode == 0) {
            String provider = Settings.Secure.getString(AddTowVehicle.this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider != null) {
                AppConstants.log(TAG, " Location providers: " + provider);
                //Start searching for location and update the location text when update available.
                getLocation(AddTowVehicle.this);
            } else {
                //Users did not switch on the GPS
                AppConstants.showToast(AddTowVehicle.this, "Enable gps to get better service.");
            }
        }


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (data != null) {
                    onSelectFromGalleryResult(data);
                } else {
                    Toast.makeText(AddTowVehicle.this, "Select Vehicle Image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                if (data != null) {
                    onCaptureImageResult(data);
                } else {
                    Toast.makeText(AddTowVehicle.this, "Select Vehicle Image", Toast.LENGTH_SHORT).show();
                }
            }


        }


    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Uri selectedImageUri = getImageUri(getActivity(), thumbnail);
        //selectedPath = getRealPathFromURI(selectedImageUri);
        imageUri = Uri.fromFile(destination);
        selectedPath = destination.getAbsolutePath();

        // Toast.makeText(getActivity(), destination.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        car_logo.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                //baseString = Utility.encodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);

                imageUri = data.getData();
                selectedPath = getPath(AddTowVehicle.this, imageUri);

                car_logo.setImageBitmap(bm);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (AppConstants.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }


            // DownloadsProvider
            else if (AppConstants.isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return AppConstants.getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (AppConstants.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return AppConstants.getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return AppConstants.getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (userChoosenTask.equals("Take Photo"))
//                        cameraIntent();
//                    else if (userChoosenTask.equals("Choose from Library"))
//                        galleryIntent();
//                } else {
//                    //code for deny
//                }
//                break;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkGpsAndGetLocation();
                        break;
                    }
                }

//            case ALL_REQUEST_CODE:
//                List<Integer> permissionResults = new ArrayList<>();
//                for (int grantResult : grantResults) {
//                    permissionResults.add(grantResult);
//                }
//                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
//                    Toast.makeText(this, "All Permissions not granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    // cameraIntent();
//                    Toast.makeText(this, "All Permissions granted", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
    }

    @Override
    public void onClick(View v) {

    }

    class DownloadNewVersion extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(AddTowVehicle.this);
            dialog.setCancelable(false);

            dialog.setMessage("Downloading...");

            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgress(progress[0]);
            String msg = "";
            if (progress[0] > 99) {

                msg = "Finishing... ";

            } else {

                msg = "Downloading... " + progress[0] + "%";
            }
            dialog.setMessage(msg);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            dialog.dismiss();

            if (result) {

                Toast.makeText(getApplicationContext(), "Update Done",
                        Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(getApplicationContext(), "No updates availabel",
                        Toast.LENGTH_SHORT).show();

            }

        }


        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;

            try {
                //https://opark.in/O_par_aPi/stg/apk/apkname.apk


                URL url = new URL("https://opark.in/O_par_aPi/stg/apk/app-debug.apk");


                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();


                String PATH = Environment.getExternalStorageDirectory() + "/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file, "app-debug.apk");

                if (outputFile.exists()) {
                    outputFile.delete();
                }


                InputStream is = c.getInputStream();

                int total_size = 1431692;//size of apk

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded = 0;

                FileOutputStream fos = new FileOutputStream(outputFile);

                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded += len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                // Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
            }
            return flag;
        }
    }

    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + "app-debug.apk")), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void getDetails(String AgentId, String parkingId) {
        final ProgressDialog pDialog = new ProgressDialog(AddTowVehicle.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);


        /*http://api.parkoye.com/index.php/parking/transaction_detail?userdata&parkingId=4&agentId=111&transactionId=389&vehicleNo=DL11CZ7000&mode=Normal*/

        String urlData = AppConstants.BASEURL + "user/logout?userId=" + AgentId + "&parkingId=" + parkingId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlData, null, new Response.Listener<JSONObject>() {

            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(JSONObject response) {
                //  Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String data = String.valueOf(response.get("data"));
                    String message = String.valueOf(response.get("message"));
                    int status = response.getInt("status");


                    if (status == 0) {

                        JSONObject loginresponce = response.getJSONObject("data");

                        String userId = loginresponce.getString("userId");
                        String isLogOut = loginresponce.getString("isLogOut");

                        getSharedPreferences("opark", Context.MODE_PRIVATE).edit().remove("userContactNo").
                                remove("agentId").remove("userRole").remove("userName").remove("vendorId").remove("parkingName").remove("userId").
                                remove("vendorName").remove("getparkingType").remove("parkingId").remove("vendorName").remove("VehicleType1").remove("tagID").commit();

                        Intent logout_intent1 = new Intent(AddTowVehicle.this, Login.class);
                        logout_intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout_intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                    pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    // Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(AddTowVehicle.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                // hide the progress dialog
                //      pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }  else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }else if (error instanceof ParseError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public void planService(String vendorId) {

       // String urlData = AppConstants.BASEURL + "towing/store_list";
       // String urlData = AppConstants.BASEURL + "tparking_list?vendorId=" + vendorId;
        String urlData = AppConstants.BASEURL + "towing/parking_list?vendorId=" + vendorId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlData, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {

                        Log.d("Response", response + "");

                        planeList.clear();
                        plannames.add("Select Parking");
                        PopulateSpinnerPojo populateSpinnerPojo1 = new PopulateSpinnerPojo();
                        populateSpinnerPojo1.setStoreId("0");
                        populateSpinnerPojo1.setStoreName("Plane");
                        planeList.add(populateSpinnerPojo1);

                        try {

                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(response.get("message"));
                            int status = response.getInt("status");
                            String response1 = String.valueOf(response.get("data"));
                            if (status == 0) {

                                final JSONArray arrayObj = new JSONArray(response1);

                                for (int i = 0; i < arrayObj.length(); i++) {

                                    JSONObject jsonObject = arrayObj.getJSONObject(i);

                                    String storeId = jsonObject.getString("parkingId");
                                    String storeName = jsonObject.getString("parkingName");

                                    PopulateSpinnerPojo populateSpinnerPojo = new PopulateSpinnerPojo();
                                    populateSpinnerPojo.setStoreId(storeId);
                                    populateSpinnerPojo.setStoreName(storeName);
                                    plannames.add(storeName);
                                    planeList.add(populateSpinnerPojo);
                                }
                                storelocation.setAdapter(new ArrayAdapter(AddTowVehicle.this,
                                        R.layout.my_spinner_style,
                                        plannames));


                                storelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        subPlanId = planeList.get(pos).getStoreId();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                            } else {
                                planeList.clear();
                                plannames.add(response1);
                                storelocation.setAdapter(new ArrayAdapter(AddTowVehicle.this, R.layout.my_spinner_style, plannames));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");


                    }
                });
        {


            request.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().getRequestQueue().add(request);

        }
    }

    public void AddTowVehicleservice(final String vehicleType1, final String operatorId, final String source_lat, final String source_long, final String subPlanId, final String vehicleNumber) {

        String urlData = AppConstants.BASEURL + "towing/vehicletow";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setContentView(R.layout.custom_progress_bar);


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, urlData,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {

                            JSONObject result = new JSONObject(resultResponse);
                            //int res = result.getInt("RESPONSECODE");

                            System.out.println("JSON RETURN " + response.toString());
                            String message = String.valueOf(result.get("message"));
                            int status = result.getInt("status");
                            String response1 = String.valueOf(result.get("data"));


                            if (status == 0) {
                                availableSlotSevice(vendorId);
                                vehicleno.setText("");
                                car_logo.setVisibility(View.VISIBLE);
                                Picasso.with(AddTowVehicle.this).load(R.mipmap.police_car512).into(car_logo);
                                twowheeler.setChecked(true);
                                vehicleType = "Two Wheeler";

//                                final JSONArray arrayObj = new JSONArray(response1);
//                                for (int i = 0; i < arrayObj.length(); i++) {
//
//                                    JSONObject jsonObject = arrayObj.getJSONObject(i);
//
//                                    String towId = jsonObject.getString("towId");
//                                    String storeName = jsonObject.getString("storeName");
//                                    String vehicleNo = jsonObject.getString("vehicleNo");
//                                    String vehicleType = jsonObject.getString("vehicleType");
//                                    String vehicleImage = jsonObject.getString("vehicleImage");
//                                    String towingDateTime = jsonObject.getString("towingDateTime");
//                                    String operatorId = jsonObject.getString("operatorId");
//                                    String releaseDateTime = jsonObject.getString("releaseDateTime");
//                                    String location = jsonObject.getString("location");
//
//
//                                }
                                pDialog.dismiss();
                                // String respo = result.getString("RESPONSE");
                                Toast.makeText(AddTowVehicle.this, message, Toast.LENGTH_SHORT).show();


                            } else {
                                pDialog.dismiss();
                                Toast.makeText(AddTowVehicle.this, message, Toast.LENGTH_SHORT).show();
                            }

                            //  Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                        pDialog.dismiss();
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                        pDialog.dismiss();
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                            pDialog.dismiss();
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                            pDialog.dismiss();
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                            pDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        pDialog.dismiss();
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                pDialog.dismiss();
                error.printStackTrace();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {

                //  String user_id = ConstantData.getString(getApplicationContext(),  USER_ID, "");


                java.util.Map<String, String> params = new HashMap<>();
                params.put("parkingId", subPlanId);
                params.put("vehicleType", vehicleType);
                params.put("vehicleNo", vehicleNumber);
                params.put("operatorId", agentId);
                params.put("vendorId", vendorId);


                //.addFileToUpload(path, "file") //Adding file
                return params;
            }

            @Override
            protected java.util.Map<String, DataPart> getByteData() {

                java.util.Map<String, DataPart> params = new HashMap<>();
                String uploadId = UUID.randomUUID().toString();
                try {
                    if (imageUri != null) {
                        InputStream iStream = getApplicationContext().getContentResolver().openInputStream(imageUri);
                        byte[] inputData = getBytes(iStream);
                        params.put("vehicleImage", new DataPart(uploadId + ".jpg", inputData, "image/*"));
                    } else {
                        Toast.makeText(AddTowVehicle.this, "Take Vehicle Image", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void availableSlotSevice(final String vendorId) {

        final ProgressDialog pDialog = new ProgressDialog(AddTowVehicle.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);
        /*http://staggingapi.opark.in/index.php/v1/towing/inventory?operatorId=8*/

        String urlData = AppConstants.BASEURL + "towing/inventory?vendorId=" + vendorId;

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

                        JSONObject availableresponce = response.getJSONObject("data");

                        // JSONObject loginresponce = response.getJSONObject("data");

                        AvailableSlotModel availableSlotModel = new AvailableSlotModel();
                        availableSlotModel.setParkingId(availableresponce.getString("vendorId"));
                        availableSlotModel.setParkingType(availableresponce.getString("towVehicle"));
                        availableSlotModel.setBookedSlots(availableresponce.getString("releasedVehicle"));
                        //availableSlotModel.setAvailableSlots(availableresponce.getString("availableSlots"));

                       /* if (parkingType.equals("2W")){
                            tvAvailableSpots.setText(availableresponce.getString("availableSlots"));
                            tvTotalSpots.setText(availableresponce.getString("bookedSlots"));
                        }*/
                        tvAvailableSpots.setText(availableresponce.getString("towVehicle"));
                        tvTotalSpots.setText(availableresponce.getString("releasedVehicle"));


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(AddTowVehicle.this, message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(AddTowVehicle.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                   // sendError(error.toString(), "towing/inventory?vendorId=");

                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }  else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }else if (error instanceof ParseError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private boolean setSpinnerError(Spinner spinner, String error) {

        boolean flag = true;

        View selectedView = spinner.getSelectedView();

        if (!spinner.getSelectedItem().toString().equals(error)) {

            flag = true;

        } else {
            if (selectedView != null && selectedView instanceof TextView) {
                spinner.requestFocus();
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError("error"); // any name of the error will do
                selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
                selectedTextView.setText("Select Parking"); // actual error message
                spinner.performClick(); // to open the spinner list if error is found.
                flag = false;
            }
        }
        return flag;
    }

}
/*final LocationManager manager = (LocationManager) MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MapActivity.this)) {
            Toast.makeText(MapActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
            finish();
        }
        // Todo Location Already on  ... end

        if(!hasGPSDevice(MapActivity.this)){
            Toast.makeText(MapActivity.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MapActivity.this)) {
            Log.e("keshav","Gps already enabled");
            Toast.makeText(MapActivity.this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }else{
            Log.e("keshav","Gps already enabled");
            Toast.makeText(MapActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }*/