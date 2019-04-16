package com.android.fusicplayer.fm_module.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.fusicplayer.R;

import java.util.ArrayList;

public class ShoutcastListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Shoutcast> shoutcasts;

    public ShoutcastListAdapter(Activity activity, ArrayList<Shoutcast> shoutcasts) {
        this.activity = activity;
        this.shoutcasts = shoutcasts;
    }

    @Override
    public int getCount() {
        return shoutcasts.size();
    }

    @Override
    public Object getItem(int position) {
        return shoutcasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int mPosition, View mView, ViewGroup mParent) {
        View mItemView = LayoutInflater.from(mParent.getContext()).inflate(R.layout.fragment_fm_row, mParent, false);
        TextView mFmName = mItemView.findViewById(R.id.tvFmName);
        mFmName.setText(shoutcasts.get(mPosition).getName());
        return mItemView;
    }
}