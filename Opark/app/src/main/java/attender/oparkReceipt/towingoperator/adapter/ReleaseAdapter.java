package attender.oparkReceipt.towingoperator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.towingoperator.fragment.ReleasedvehicleFrgment;
import attender.oparkReceipt.towingoperator.model.TowingOperatoreModel;

/**
 * Created by Daffodil on 7/19/2018.
 */

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {
    private ArrayList<TowingOperatoreModel> vehicleModelList = new ArrayList<>();
    ReleasedvehicleFrgment releasedvehicleFrgment;
    Context context;
    String image;


    public ReleaseAdapter(ReleasedvehicleFrgment releasedvehicleFrgment, ArrayList<TowingOperatoreModel> vehicleModelList) {
        this.vehicleModelList = vehicleModelList;
        this.releasedvehicleFrgment = releasedvehicleFrgment;
        this.context = releasedvehicleFrgment.getContext();
    }

   /* public ReleaseAdapter(ReleasedvehicleFrgment releasedvehicleFrgment, ArrayList<TowingOperatoreModel> vehicleModelList) {

    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_chekin_list, parent, false);

        return new ReleaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReleaseAdapter.ViewHolder holder, int position) {
        holder.bind(vehicleModelList.get(position));

    }

    @Override
    public int getItemCount() {
        return vehicleModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView vechileno, type, location, towndate, relaedate;
        ImageView image;
        String image1;
        CardView card;
        LinearLayout date, loc;

        public ViewHolder(View itemView) {
            super(itemView);
            vechileno = (TextView) itemView.findViewById(R.id.vechileno);
            type = (TextView) itemView.findViewById(R.id.type);
            location = (TextView) itemView.findViewById(R.id.location);
            towndate = (TextView) itemView.findViewById(R.id.towndate);
            relaedate = (TextView) itemView.findViewById(R.id.relaedate);
            card = (CardView) itemView.findViewById(R.id.card);
            image = (ImageView) itemView.findViewById(R.id.image);
            loc = (LinearLayout) itemView.findViewById(R.id.loc);
            relaedate.setVisibility(View.VISIBLE);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(TowingOperatoreModel towingOperatoreModel) {


            vechileno.setText(towingOperatoreModel.getVehicleNo());
            type.setText(towingOperatoreModel.getVehicleType());
            location.setText(towingOperatoreModel.getLocation());
            towndate.setText(towingOperatoreModel.getTowingDateTime());

            if (towingOperatoreModel.getLocation().isEmpty()) {
                loc.setVisibility(View.GONE);
            }


            relaedate.setText(towingOperatoreModel.getReleaseDateTime());

            if (towingOperatoreModel.getVehicleImage().isEmpty()) {
                Picasso.with(context).load(R.mipmap.police_car512).into(image);
            } else {
                Picasso.with(context).load(towingOperatoreModel.getVehicleImage()).into(image);
            }
            image1 = towingOperatoreModel.getVehicleImage();

        }
    }
}
