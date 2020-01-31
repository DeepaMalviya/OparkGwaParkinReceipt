package attender.oparkReceipt.vehiclelist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.vehiclelist.model.VehicleModelDetails;

/**
 * Created by Daffodil on 4/16/2018.
 */

public class VehicleModelAdapter extends RecyclerView.Adapter<VehicleModelAdapter.ViewHolder> {
   // DetailActivity detailActivity;
    ArrayList<VehicleModelDetails> vehicleModelList;
    private Context context;

//    public VehicleModelAdapter(DetailActivity detailActivity, ArrayList<VehicleModelDetails> vehicleModelList) {
//        this.vehicleModelList = vehicleModelList;
//        this.detailActivity = detailActivity;
//        context = detailActivity;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_mange_vehicle,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VehicleModelAdapter.ViewHolder holder, int position) {
        holder.bind(vehicleModelList.get(position));
    }


    @Override
    public int getItemCount() {
        return vehicleModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewVehicleName;
        private TextView vehicleNumber;
        private TextView vehicleNoOut;
        private TextView checkdate,chkOut;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewVehicleName = (TextView) itemView.findViewById(R.id.textViewVehicleName);
            vehicleNumber = (TextView) itemView.findViewById(R.id.mobileNumber);
            vehicleNoOut = (TextView) itemView.findViewById(R.id.vehicleNo);
            checkdate = (TextView) itemView.findViewById(R.id.checkdate);
            chkOut = (TextView) itemView.findViewById(R.id.chkOut);
        }

        public void bind(VehicleModelDetails vehicleModelDetails) {

         //   textViewVehicleName.setText("Parking Name :"+vehicleModelDetails.getParkingName());
            vehicleNumber.setText("Mobile Number :"+vehicleModelDetails.getMobileNo());
            vehicleNoOut.setText("VehicleNo :"+vehicleModelDetails.getVehicleNo());
            checkdate.setText("Check In Date :"+vehicleModelDetails.getCheckInDateTime());
            chkOut.setText("Check Out :"+vehicleModelDetails.getCheckOutDateTime());
        }
    }
}
