package com.android.fusicplayer.fm_module;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.fusicplayer.MainActivity;
import com.android.fusicplayer.R;
import com.android.fusicplayer.fm_module.util.Shoutcast;
import com.android.fusicplayer.fm_module.util.ShoutcastListAdapter;
import com.android.fusicplayer.player.PlaybackStatus;
import com.android.fusicplayer.player.RadioManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FmModule extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Shoutcast> mList;
    private ShoutcastListAdapter mAdapter;


    @BindView(R.id.listFmDisplay)
    ListView listView;

    RadioManager radioManager;

    public static FmModule newInstance() {
        return new FmModule();
    }

    private void retrieveShoutcasts() {
        try {
            Reader reader = new InputStreamReader(getContext().getResources().openRawResource(R.raw.shoutcasts));
            mList = new Gson().fromJson(reader, new TypeToken<List<Shoutcast>>() {
            }.getType());
        } catch (Exception mException) {
            Log.d("FmModule", "Error reading JSON data, Error:: " + mException.getMessage());
        }
    }

    public String[] getFirstChannel() {
        String[] str = new String[2];
        if (mList != null) {
            str[0] = mList.get(0).getName();
            str[1] = mList.get(0).getUrl();
            return str;
        } else
            return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_fm, container, false);

        ButterKnife.bind(this, mView);

        retrieveShoutcasts();
        radioManager = RadioManager.with(getActivity());

        if (mList != null) {
            mAdapter = new ShoutcastListAdapter(getActivity(), mList);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(this);
        }
        return mView;
    }

    @OnItemClick(R.id.listFmDisplay)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);
        if (shoutcast == null) return;
        ((MainActivity) getActivity()).manageBottomView(shoutcast.getName(), shoutcast.getUrl());
    }

    @Override
    public void onStart() {

        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        radioManager.unbind();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        radioManager.bind();
    }


    @Subscribe
    public void onEvent(String status) {
        switch (status) {
            case PlaybackStatus.LOADING:
                break;

            case PlaybackStatus.ERROR:
                break;
        }
    }
}