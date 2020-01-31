package attender.oparkReceipt.towingoperator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import attender.oparkReceipt.towingoperator.activity.ReleasedTowVehicle;
import attender.oparkReceipt.towingoperator.fragment.ReleasedvehicleFrgment;
import attender.oparkReceipt.towingoperator.fragment.TowedvehicleFragment;
import attender.oparkReceipt.towingoperator.model.TowingOperatoreModel;

/**
 * Created by Daffodil on 7/17/2018.
 */

public class TowAdapter extends RecyclerView.Adapter<TowAdapter.ViewHolder> {
    private ArrayList<TowingOperatoreModel> vehicleModelList = new ArrayList<>();
    TowedvehicleFragment towedvehicleFragment;
    ReleasedvehicleFrgment releasedvehicleFrgment;
    Context context;
    String image;

    public TowAdapter(TowedvehicleFragment towedvehicleFragment, ArrayList<TowingOperatoreModel> vehicleModelList) {
        this.vehicleModelList = vehicleModelList;
        this.towedvehicleFragment = towedvehicleFragment;
        this.context = towedvehicleFragment.getContext();
    }


    @Override
    public TowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.towadapter, parent, false);
        return new TowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TowAdapter.ViewHolder holder, int position) {
        holder.bind(vehicleModelList.get(position));

    }

    @Override
    public int getItemCount() {
        return vehicleModelList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vechileno, type, location, towndate, relaedate;
        ImageView image;
        String image1;
        CardView card;
        LinearLayout date,loc;

        public ViewHolder(View itemView) {
            super(itemView);

            vechileno = (TextView) itemView.findViewById(R.id.vechileno);
            type = (TextView) itemView.findViewById(R.id.type);
            location = (TextView) itemView.findViewById(R.id.location);
            towndate = (TextView) itemView.findViewById(R.id.towndate);
            relaedate = (TextView) itemView.findViewById(R.id.relaedate);
            card = (CardView) itemView.findViewById(R.id.card);
            image = (ImageView) itemView.findViewById(R.id.image);
            date = (LinearLayout) itemView.findViewById(R.id.date);
            loc = (LinearLayout) itemView.findViewById(R.id.loc);


            relaedate.setVisibility(View.GONE);

            itemView.setOnClickListener(this);
            card.setOnClickListener(this);
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

            if (towingOperatoreModel.getReleaseDateTime().isEmpty()) {
                date.setVisibility(View.GONE);
            } else {
                towndate.setVisibility(View.VISIBLE);
            }

            if (towingOperatoreModel.getVehicleImage().isEmpty()) {
                Picasso.with(context).load(R.mipmap.police_car512).into(image);
            } else {
                Picasso.with(context).load(towingOperatoreModel.getVehicleImage()).into(image);
            }
            image1 = towingOperatoreModel.getVehicleImage();

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == card.getId()) {
                TowingOperatoreModel towingOperatoreMode = new TowingOperatoreModel();
                Intent intent = new Intent(context, ReleasedTowVehicle.class);
                String vechileNo = vechileno.getText().toString();
                String Type = type.getText().toString();
                String Towndate = towndate.getText().toString();
                String Location = location.getText().toString();
                intent.putExtra("getVehicleNo", vechileNo);
                intent.putExtra("type", Type);
                intent.putExtra("image", image1);
                intent.putExtra("Towndate", Towndate);
                //intent.putExtra("Location", Location);
                context.startActivity(intent);
            }

        }

    }
}
