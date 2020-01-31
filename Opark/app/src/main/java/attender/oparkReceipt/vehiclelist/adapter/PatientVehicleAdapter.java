package attender.oparkReceipt.vehiclelist.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import attender.oparkCard.R;
import attender.oparkReceipt.vehiclelist.fragment.FourFragmentOut;
import attender.oparkReceipt.vehiclelist.fragment.FourWheelerCheckInFragment;

/**
 * Created by Daffodil on 7/6/2018.
 */

public class PatientVehicleAdapter extends FragmentPagerAdapter {

    private Context context;
    String ptype;

    public PatientVehicleAdapter(FragmentManager fm, Context context, String s) {
        super(fm);
        this.context = context;
        this.ptype = s;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new FourWheelerCheckInFragment(ptype);
        } else if (position == 1) {

            fragment = new FourFragmentOut(ptype);
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
            title = context.getString(R.string.service_station);
        } else if (position == 1) {
            title = context.getString(R.string.review);
        }
        return title;
    }
}
