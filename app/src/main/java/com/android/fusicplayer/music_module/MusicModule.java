package com.android.fusicplayer.music_module;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ArrayList<SongInfo> songsList;
    SongAdapter sngAdapter;
    private final int PERM_CODE = 1;

    @BindView(R.id.listMusicDisplay)
    RecyclerView recyclerView;

    public static MusicModule newInstance() {
        return new MusicModule();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_CODE);
            return;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (sngAdapter == null) loadSongs();
        } else {
            Toast.makeText(getContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSongs();
                    return;
                }
                break;

            default:
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void loadSongs() {
        songsList = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.IS_MUSIC + "!=0", null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do
                songsList.add(new SongInfo(cursor.getString(0), cursor.getString(1)));
            while (cursor.moveToNext());
        }
        cursor.close();

        // Initialize adapter and set recycler view adapter
        sngAdapter = new SongAdapter(getActivity(), songsList);
        recyclerView.setAdapter(sngAdapter);
    }

    public String[] getFirstSong() {
        String[] str = new String[2];
        if (songsList != null) {
            str[0] = songsList.get(0).getSongName();
            str[1] = songsList.get(0).getSongURL();
            return str;
        } else
            return null;
    }
}