package attender.oparkReceipt.parkinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.parkinglist.ParkingList;
import attender.oparkReceipt.parkinglist.model.ParkingListMoel;

/**
 * Created by Daffodil on 7/14/2018.
 */


public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    Context context;
    ParkingList parkingListclass;

    ArrayList<ParkingListMoel> respnseArraylist = new ArrayList<>();

    public ParkingAdapter(Context context, ParkingList parkingListclass, ArrayList<ParkingListMoel> respnseArraylist) {
        this.context = context;
        this.parkingListclass = parkingListclass;
        this.respnseArraylist = respnseArraylist;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parkingListclass).inflate(R.layout.parkinglistadapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.parkingName.setText("Parking Name   : " + respnseArraylist.get(position).getParkingName());
        holder.parkingType.setText("Parking Type     : " + respnseArraylist.get(position).getParkingType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingListclass.setOnItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return respnseArraylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView parkingName, parkingType;

        public ViewHolder(View itemView) {
            super(itemView);
            parkingName = (TextView) itemView.findViewById(R.id.parkinglogin);
            parkingType = (TextView) itemView.findViewById(R.id.vehicleTypelogin);
        }
    }


}
