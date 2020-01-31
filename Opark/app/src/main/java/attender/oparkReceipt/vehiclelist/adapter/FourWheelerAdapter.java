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

public class FourWheelerAdapter extends FragmentPagerAdapter{

        private Context context;
    String s;
        public FourWheelerAdapter(FragmentManager fm, Context context, String s) {
            super(fm);
            this.context = context;
            this.s = s;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new FourWheelerCheckInFragment(s);
            } else if (position == 1) {

                fragment = new FourFragmentOut(s);
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
