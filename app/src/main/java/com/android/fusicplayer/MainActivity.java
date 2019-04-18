package com.android.fusicplayer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.fusicplayer.player.RadioManager;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private RelativeLayout bottomView;
    private RadioManager radioManager;
    private TextView songName;
    private ImageButton playTrigger;
    private String mStreamUrl = "";
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        bottomView = findViewById(R.id.bottomView);
        bottomView.setVisibility(View.GONE);

        songName = findViewById(R.id.songName);
        playTrigger = findViewById(R.id.playTrigger);
        playTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                if (mStreamUrl == null || radioManager == null) return;
                radioManager.playOrPause(mStreamUrl);
                if (isPlaying) {
                    playTrigger.setImageResource(R.drawable.ic_play_arrow_black);
                    isPlaying = false;
                } else {
                    playTrigger.setImageResource(R.drawable.ic_pause_black);
                    isPlaying = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle:
                int n = viewPager.getCurrentItem();
                if (n == 0) {
                    viewPager.setCurrentItem(1);
                    String str[] = adapter.getFirstChannel();
                    manageBottomView(str[0], str[1]);
                } else {
                    viewPager.setCurrentItem(0);
                    String str[] = adapter.getFirstSong();
                    manageBottomView(str[0], str[1]);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void manageBottomView(String mSongName, String mStreamUrl) {
        if (mStreamUrl == null || mStreamUrl.equals(""))
            return;
        if (!this.mStreamUrl.equals(mStreamUrl))
            isPlaying = true;
        else
            isPlaying = !isPlaying;

        this.mStreamUrl = mStreamUrl;

        bottomView.setVisibility(View.VISIBLE);
        songName.setText(mSongName);

        radioManager = RadioManager.with(this);
        radioManager.playOrPause(mStreamUrl);

        if (isPlaying) {
            playTrigger.setImageResource(R.drawable.ic_pause_black);
        } else {
            playTrigger.setImageResource(R.drawable.ic_play_arrow_black);
        }
    }
}
