package com.example.firebase_google_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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