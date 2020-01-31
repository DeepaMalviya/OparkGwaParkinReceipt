package attender.oparkReceipt.vehiclelist.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import attender.oparkCard.R;
import attender.oparkReceipt.vehiclelist.fragment.ThreeWheelerCheckInFragment;
import attender.oparkReceipt.vehiclelist.fragment.ThreeWheelerFragmentOut;

/**
 * Created by Daffodil on 7/6/2018.
 */

public class ThreeWheelerListAdapter extends FragmentPagerAdapter{

        private Context context;

        public ThreeWheelerListAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new ThreeWheelerCheckInFragment();
            } else if (position == 1) {

                fragment = new ThreeWheelerFragmentOut();
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
