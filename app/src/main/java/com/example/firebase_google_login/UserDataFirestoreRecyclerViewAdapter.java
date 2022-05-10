package com.example.firebase_google_login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//public class UserDataFirestoreRecyclerViewAdapter {
//}


public class UserDataFirestoreRecyclerViewAdapter extends FirestoreRecyclerAdapter<AudioFile,UserDataFirestoreRecyclerViewAdapter.PostViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public UserDataFirestoreRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<AudioFile> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AudioFile model) {
        //holder.userImage.setImageResource();
        holder.title.setText(getSnapshots().getSnapshot(position).getId());
        holder.userName.setText(getItem(position).getUserName());
        holder.createdAt.setText(getItem(position).getDate());
        Glide.with(context).load(getItem(position).getPhotoUrl()).into(holder.userImage);


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("recyclerViewUserData","delete is clicked");
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LinearLayout", "RecyclerView Clicked id is "+getItem(position));
                Log.d("LinearLayout", "RecyclerView Clicked id is "+getItem(position).getUserName());
                Log.d("LinearLayout", "RecyclerView Clicked id is "+getSnapshots().getSnapshot(position).getId());



                UserData storageInstance=UserData.getUserDataInstance();
                if(storageInstance != null){
                    storageInstance.playAudioUsingMediaPlayer(position);
                }
            }
        });
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_for_user_data,parent,false);
        PostViewHolder postViewHolder=new PostViewHolder(view);
        return postViewHolder;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImage;
        private TextView title;
        private TextView userName;
        private TextView createdAt;
        private ImageButton deleteButton;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.userImageForUserData);
            title=itemView.findViewById(R.id.postTitleForUserData);
            userName=itemView.findViewById(R.id.userNameForUserData);
            createdAt=itemView.findViewById(R.id.createdAtForUserData);
            deleteButton=itemView.findViewById(R.id.deleteForUserData);
        }
    }
}



