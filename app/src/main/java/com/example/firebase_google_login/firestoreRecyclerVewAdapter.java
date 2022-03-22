package com.example.firebase_google_login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.IOException;

public class firestoreRecyclerVewAdapter extends FirestoreRecyclerAdapter<AudioFile,firestoreRecyclerVewAdapter.PostViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public firestoreRecyclerVewAdapter(@NonNull FirestoreRecyclerOptions<AudioFile> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AudioFile model) {
        //holder.userImage.setImageResource();
        holder.title.setText(getSnapshots().getSnapshot(position).getId());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LinearLayout", "RecyclerView Clicked id is "+getItem(position));
                Log.d("LinearLayout", "RecyclerView Clicked id is "+getItem(position).getUserName());
                Log.d("LinearLayout", "RecyclerView Clicked id is "+getSnapshots().getSnapshot(position).getId());

                MediaPlayer mp=new MediaPlayer();
                try {
                    //mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/project-for-mc.appspot.com/o/Aduio%2Ffavourite%20song1647518773673.mp3?alt=media&token=3e2a6e9a-1868-4495-b169-af15409ab10f");
                    mp.setDataSource(getItem(position).getDownloadUrl());
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        PostViewHolder postViewHolder=new PostViewHolder(view);
        return postViewHolder;
    }
    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImage;
        private TextView title;
        private TextView userName;
        private TextView createdAt;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.userImage);
            title=itemView.findViewById(R.id.postTitle);
            userName=itemView.findViewById(R.id.userName);
            createdAt=itemView.findViewById(R.id.createdAt);
        }
    }
}


