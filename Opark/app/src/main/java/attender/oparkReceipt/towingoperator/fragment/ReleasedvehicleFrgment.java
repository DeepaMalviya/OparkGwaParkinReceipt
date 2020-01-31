package attender.oparkReceipt.towingoperator.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.base.AppConstants;
import attender.oparkReceipt.base.AppController;
import attender.oparkReceipt.towingoperator.model.TowingOperatoreModel;
import attender.oparkReceipt.towingoperator.adapter.ReleaseAdapter;
import attender.oparkReceipt.base.ClickListener;
import attender.oparkReceipt.base.DividerItemDecoration;
import attender.oparkReceipt.base.RecyclerTouchListener;


public class ReleasedvehicleFrgment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ReleaseAdapter releaseAdapter;
    private RecyclerView recyclerViewVehicle;
    FloatingActionButton addVehicle;
    private ArrayList<TowingOperatoreModel> vehicleModelList = new ArrayList<>();
    // protected ProgressDialog progressDialog;
    SharedPreferences sharedpref;
    Fragment fragment = null;

    String agentId;

    private OnFragmentInteractionListener mListener;

    public ReleasedvehicleFrgment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReleasedvehicleFrgment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReleasedvehicleFrgment newInstance(String param1, String param2) {
        ReleasedvehicleFrgment fragment = new ReleasedvehicleFrgment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_towedvehicle, container, false);

        findVievId(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.isInternetAvailable(getActivity())) {
            getDetails(agentId);
        } else {
            Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    public void findVievId(View view) {

        sharedpref = getActivity().getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedpref.getString("agentId", "");

        recyclerViewVehicle = (RecyclerView) view.findViewById(R.id.recyclerViewReview);
        //addVehicle = (FloatingActionButton)view.findViewById(R.id.addVehicle);
        recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewVehicle.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));

        recyclerViewVehicle.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewVehicle, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getDetails(String operatorId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/towing/towvehicle_list?operatorId=1*/
        /*http://staggingapi.opark.in/index.php/v1/towing/towvehicle_list?operatorId=8*/
        String urlData = AppConstants.BASEURL + "towing/releasedvehicle_list?operatorId=" + operatorId;

        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {
                vehicleModelList.clear();

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

                            String towId = jsonObject.getString("towId");
                            String storeName = jsonObject.getString("storeName");
                            String vehicleNo = jsonObject.getString("vehicleNo");
                            String vehicleType = jsonObject.getString("vehicleType");
                            String vehicleImage = jsonObject.getString("vehicleImage");
                            String towingDateTime = jsonObject.getString("towingDateTime");
                            String operatorId = jsonObject.getString("operatorId");
                            String releaseDateTime = jsonObject.getString("releaseDateTime");
                            String location = jsonObject.getString("location");

                            //   Toast.makeText(DetailActivity.this, transactionId +"/n"+ parkingName +"/n"+checkInDateTime +"/n"+vehicleNo+mobileNo, Toast.LENGTH_SHORT).show();

                            TowingOperatoreModel towingOperatoreModel = new TowingOperatoreModel();

                            towingOperatoreModel.setTowId(towId);
                            towingOperatoreModel.setStoreName(storeName);
                            towingOperatoreModel.setVehicleNo(vehicleNo);
                            towingOperatoreModel.setVehicleType(vehicleType);
                            towingOperatoreModel.setVehicleImage(vehicleImage);
                            towingOperatoreModel.setTowingDateTime(towingDateTime);
                            towingOperatoreModel.setOperatorId(operatorId);
                            towingOperatoreModel.setReleaseDateTime(releaseDateTime);
                            towingOperatoreModel.setLocation(location);

                            vehicleModelList.add(towingOperatoreModel);


                        }

                        releaseAdapter = new ReleaseAdapter(ReleasedvehicleFrgment.this, vehicleModelList);
                        recyclerViewVehicle.setAdapter(releaseAdapter);
//                        towAdapter = new TowAdapter(TowedvehicleFragment.this, vehicleModelList);
//                        recyclerViewVehicle.setAdapter(adapter);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
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
        },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
        pDialog.dismiss();

    }
}
