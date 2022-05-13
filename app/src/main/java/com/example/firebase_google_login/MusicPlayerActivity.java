package com.example.firebase_google_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

    JcPlayerView jcplayerView;
    ArrayList<JcAudio> jcAudios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        jcplayerView =  findViewById(R.id.jcplayerMusicPlayer);
        jcAudios = new ArrayList<>();


        Bundle extra=getIntent().getExtras();

        String quote=extra.getString("quote");

        TextView songName=findViewById(R.id.musicTitle);
        songName.setText(extra.getString("songName"));

        ImageView uploadedImage=findViewById(R.id.songImage);
        Glide.with(this).load(extra.getString("imageUrl")).into(uploadedImage);


//        int position=extra.getInt("position");

//        storage storageInstance=storage.getStorageInstance();
//        if(storageInstance != null){
//            storageInstance.playAudioUsingMediaPlayer(position);
//        }


        jcAudios.add(JcAudio.createFromURL(extra.getString("songName"),extra.getString("downloadLink")));

        jcplayerView.initPlaylist(jcAudios, null);
        jcplayerView.playAudio(jcAudios.get(0));
        jcplayerView.createNotification();
    }
}