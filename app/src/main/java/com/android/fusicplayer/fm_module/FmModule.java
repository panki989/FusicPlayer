package com.android.fusicplayer.fm_module;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.android.fusicplayer.R;
import com.android.fusicplayer.player.PlaybackStatus;
import com.android.fusicplayer.player.RadioManager;
import com.android.fusicplayer.fm_module.util.Shoutcast;
import com.android.fusicplayer.fm_module.util.ShoutcastListAdapter;

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
import butterknife.OnClick;
import butterknife.OnItemClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FmModule extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList<Shoutcast> mList;
    private ShoutcastListAdapter mAdapter;

    @BindView(R.id.playTrigger)
    ImageButton trigger;

    @BindView(R.id.listFmDisplay)
    ListView listView;

    @BindView(R.id.name)
    TextView textView;

    @BindView(R.id.sub_player)
    View subPlayer;

    RadioManager radioManager;

    String streamURL;

    public static FmModule newInstance() {
        return new FmModule();
    }

    public FmModule() {

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
        if (shoutcast == null)
            return;
        textView.setText(shoutcast.getName());
        subPlayer.setVisibility(View.VISIBLE);
        streamURL = shoutcast.getUrl();
        radioManager.playOrPause(streamURL);
    }

    @OnClick(R.id.playTrigger)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playTrigger:
                if(TextUtils.isEmpty(streamURL))
                    return;
                radioManager.playOrPause(streamURL);
                break;

                default:
                    break;
        }
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
        radioManager.bind();
        super.onResume();

    }


    @Subscribe
    public void onEvent(String status){

        switch (status){

            case PlaybackStatus.LOADING:
                //loading
                break;

            case PlaybackStatus.ERROR:
                break;

        }

        trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);

    }
}