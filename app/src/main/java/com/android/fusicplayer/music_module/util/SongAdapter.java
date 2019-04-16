package com.android.fusicplayer.music_module.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fusicplayer.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private ArrayList<SongInfo> songinfo;

    public SongAdapter(Activity activity, ArrayList<SongInfo> songinfo) {
        this.activity = activity;
        this.songinfo = songinfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_music_row, viewGroup, false);
        return new SongHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof SongHolder){
            SongHolder mHolder = (SongHolder) viewHolder;
            mHolder.tvSongName.setText(songinfo.get(i).getSongName());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return songinfo.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView tvSongName;

        public SongHolder(View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.musicName);
            tvSongName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, songinfo.get(getAdapterPosition()).getSongName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
