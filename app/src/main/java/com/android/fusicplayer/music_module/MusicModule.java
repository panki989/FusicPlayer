package com.android.fusicplayer.music_module;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fusicplayer.R;
import com.android.fusicplayer.music_module.util.SongAdapter;
import com.android.fusicplayer.music_module.util.SongInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicModule extends Fragment {
//    private TextView textView;

    private ArrayList<SongInfo> songs;
    SongAdapter sngAdapter;

    @BindView(R.id.listMusicDisplay)
    RecyclerView recyclerView;

    @BindView(R.id.playButton)
    ImageButton trigger;

    @BindView(R.id.songName)
    TextView textView;

    @BindView(R.id.music_player)
    View subPlayer;

    public MusicModule() {
        // Required empty public constructor
    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        }
        loadSongs();
    }

    public void onRequestPermissionsResult(int mRequestCode, @NonNull String[] mPermissions, @NonNull int[] mResult) {
        switch (mRequestCode) {
            case 123:
                if (mResult.length > 0 && mResult[0] == PackageManager.PERMISSION_GRANTED)
                    loadSongs();
                else {
                    Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
        super.onRequestPermissionsResult(mRequestCode, mPermissions, mResult);
    }

    private void loadSongs() {
        songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    if (name != null)
                        songs.add(new SongInfo(name, url));

                } while (cursor.moveToNext());
            }

            cursor.close();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, view);

        checkUserPermission();

        if (songs != null) {
            sngAdapter = new SongAdapter(getActivity(), songs);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(sngAdapter);
        }

        return view;
    }

}
