package com.example.firebase_google_login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserData extends AppCompatActivity {

    private static UserData userDataInstance;
    JcPlayerView jcplayerView;
    ArrayList<JcAudio> jcAudios;
    private UserDataFirestoreRecyclerViewAdapter adapter;
    private String userUid;
    private long createdAt;

    public static UserData getUserDataInstance() {
        return userDataInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        jcplayerView = findViewById(R.id.jcplayerForUserData);
        jcAudios = new ArrayList<>();
        userDataInstance = this;


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userUid = firebaseUser.getUid();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewForUserData);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirestoreRecyclerOptions<AudioFile> options = new FirestoreRecyclerOptions.Builder<AudioFile>()
                .setQuery(firebaseFirestore.collection(userUid), AudioFile.class)
                .build();
        recyclerView.setLayoutManager(new UserData.CustomLinearLayoutManager(this));
        adapter = new UserDataFirestoreRecyclerViewAdapter(options, this);
        recyclerView.setAdapter(adapter);


        getRealTimeSongs();


    }

    private void getRealTimeSongs() {
        FirebaseFirestore.getInstance().collection(userUid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                jcAudios = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String songName = documentSnapshot.getId();
                    AudioFile audioFile = documentSnapshot.toObject(AudioFile.class);
                    jcAudios.add(JcAudio.createFromURL(songName, audioFile.getDownloadUrl()));
                    Log.i("AllMusic", songName);
                }
                if (jcAudios.size() > 0) {
                    jcplayerView.initPlaylist(jcAudios, null);
                }

            }
        });
    }

    public void playAudioUsingMediaPlayer(int position) {
        if (position < jcAudios.size()) {
            Log.d("MusicNumber", position + "");
            jcplayerView.playAudio(jcAudios.get(position));
            jcplayerView.createNotification();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private class CustomLinearLayoutManager extends LinearLayoutManager {

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("LinearLayout", "LinearLayout exception in RecyclerView");
            }
        }
    }
}