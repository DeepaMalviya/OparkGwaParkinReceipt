package attender.oparkReceipt.shiftreport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.parkinglist.model.ParkingListMoel;

/**
 * Created by Daffodil on 7/14/2018.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    Context context;
    ShiftReportctivity shiftReportctivity;
    ArrayList<ParkingListMoel> respnseArraylist = new ArrayList<>();
    int count = 1;

    public ShiftAdapter(Context context, ShiftReportctivity shiftReportctivity, ArrayList<ParkingListMoel> respnseArraylist) {
        this.context = context;
        this.shiftReportctivity = shiftReportctivity;
        this.respnseArraylist = respnseArraylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(shiftReportctivity).inflate(R.layout.parkinglistadapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.parkingName.setText(count++ + ". " + "VehicleNo                  : " + respnseArraylist.get(position).getVehicleNo());
        holder.parkingType.setText("CheckInDateTime        : " + respnseArraylist.get(position).getCheckInDateTime());



        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingListclass.setOnItemClick(position);
            }
        });
*/
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
