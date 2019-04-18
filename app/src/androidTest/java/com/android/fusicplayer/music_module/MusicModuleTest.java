package com.android.fusicplayer.music_module;

import android.app.Instrumentation;
import android.support.design.widget.TabLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.fusicplayer.MainActivity;
import com.android.fusicplayer.R;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MusicModuleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityRule.getActivity();
    }

    @Test
    public void testLaunchTab() {
        TabLayout tabLayout = mainActivity.findViewById(R.id.tabs);
        assertNotNull(tabLayout);
    }

    @Test
    public void testFMListPresence(){
        ListView listView = mainActivity.findViewById(R.id.listFmDisplay);

        assertNotNull(listView);
        assertThat(listView.getCount(), CoreMatchers.is(8) );
    }

    @Test
    public void testFMItemIsClicked(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        final ListView listview = mainActivity.findViewById(R.id.listFmDisplay);

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 3;
                listview.performItemClick(listview.getChildAt(position), position, listview.getAdapter().getItemId(position));
            }
        });

        TextView detailView = mainActivity.findViewById(R.id.songName);
        assertThat(detailView.getText().toString(), CoreMatchers.is("Radio - Virsa"));
    }

    @Test
    public void testMPListPresence(){
        RecyclerView recyclerView = mainActivity.findViewById(R.id.listMusicDisplay);
        assertNotNull(recyclerView);
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}