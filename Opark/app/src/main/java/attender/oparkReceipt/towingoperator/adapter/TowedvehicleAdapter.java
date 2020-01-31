package attender.oparkReceipt.towingoperator.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import attender.oparkCard.R;
import attender.oparkReceipt.towingoperator.fragment.ReleasedvehicleFrgment;
import attender.oparkReceipt.towingoperator.fragment.TowedvehicleFragment;

/**
 * Created by Daffodil on 7/17/2018.
 */

public class TowedvehicleAdapter extends FragmentPagerAdapter {

    private Context context;

    public TowedvehicleAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if (position == 0) {
            fragment = new TowedvehicleFragment();
        } else if (position == 1) {
            fragment = new ReleasedvehicleFrgment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = context.getString(R.string.towedvehicle);
        } else if (position == 1) {
            title = context.getString(R.string.releasedvehicle);
        }
        return title;
    }
}
