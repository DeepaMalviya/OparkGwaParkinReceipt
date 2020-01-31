package attender.oparkReceipt.subscription.activity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.subscription.model.PassDetailModel;


/**
 * Created by Daffodil on 9/12/2018.
 */

public class CardDetailAdapter extends RecyclerView.Adapter<CardDetailAdapter.ViewHolder> {
    Context context;
    CardDetail cardDetail;
    Subscriptionctivity subscriptionctivity;
    ArrayList<PassDetailModel> vehicleModelList = new ArrayList<>();


    public CardDetailAdapter(Context context, CardDetail cardDetail, ArrayList<PassDetailModel> vehicleModelList) {
        this.context = context;
        this.cardDetail = cardDetail;
        this.vehicleModelList = vehicleModelList;
    }

    public CardDetailAdapter(Context context, Subscriptionctivity subscriptionctivity, ArrayList<PassDetailModel> vehicleModelList) {
        this.context = context;
        this.subscriptionctivity = subscriptionctivity;
        this.vehicleModelList = vehicleModelList;
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(subscriptionctivity).inflate(R.layout.card_chekin_list, parent, false);
//        //View view = LayoutInflater.from(cardDetail).inflate(R.layout.card_chekin_list, parent, false);
//        return new ViewHolder(view);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cardDetail).inflate(R.layout.card_chekin_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.checkinText.setText(vehicleModelList.get(position).getCheckinText());
        holder.checkoutText.setText(vehicleModelList.get(position).getCheckoutText());
        holder.durationText.setText(vehicleModelList.get(position).getDurationText());



    }

    @Override
    public int getItemCount() {
        return vehicleModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView checkinText, checkoutText, durationText;

        public ViewHolder(View itemView) {
            super(itemView);
            durationText = (TextView) itemView.findViewById(R.id.durationText);
            checkinText = (TextView) itemView.findViewById(R.id.checkinText);
            checkoutText = (TextView) itemView.findViewById(R.id.checkoutText);
        }
    }


}
