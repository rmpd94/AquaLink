package com.aqualink.iot.rgb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RgbDeviceAdapter  extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    public RgbDeviceAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RgbManualActivity manualFragment = new RgbManualActivity();
                return manualFragment;
            case 1:
                RgbAutoActivity autoFragment = new RgbAutoActivity();
                return autoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
