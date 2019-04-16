package com.android.fusicplayer;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.fusicplayer.fm_module.FmModule;
import com.android.fusicplayer.music_module.MusicModule;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                MusicModule musicModule = new MusicModule();
                return musicModule;

            case 1:
                FmModule fmModule = FmModule.newInstance();
                return fmModule;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
            return "Music";
        else if (position == 1)
            return  "FM";
        else
            return null;
    }
}
