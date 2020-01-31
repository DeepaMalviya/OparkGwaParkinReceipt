package attender.oparkReceipt.subscription.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkReceipt.subscription.model.PassDetailModel;
import attender.oparkReceipt.subscription.activity.ShowCardDetail;

/**
 * Created by Daffodil on 9/14/2018.
 */

public class ShowCardDetailAdapter extends RecyclerView.Adapter<ShowCardDetailAdapter.ViewHolder> {

    Context context;
    ShowCardDetail showCardDetail;
    ArrayList<PassDetailModel> passDetailModels = new ArrayList<>();

    public ShowCardDetailAdapter(ShowCardDetail showCardDetail, ArrayList<PassDetailModel> passDetailModels) {

        this.showCardDetail = showCardDetail;
        this.passDetailModels = passDetailModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(showCardDetail).inflate(R.layout.card_chekin_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        holder.checkinText.setText(passDetailModels.get(position).getCheckinText());
        holder.checkoutText.setText(passDetailModels.get(position).getCheckoutText());
        holder.durationText.setText(passDetailModels.get(position).getDurationText());

    }

    @Override
    public int getItemCount() {
        return passDetailModels.size();
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
