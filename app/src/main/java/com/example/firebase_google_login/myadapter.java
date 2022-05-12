package com.example.firebase_google_login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_google_login.R;
import com.example.firebase_google_login.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model)
    {
        holder.username.setText(model.getUsername());
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView username,email;
        CircleImageView img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            username=(TextView)itemView.findViewById(R.id.usernametxt);
            email=(TextView)itemView.findViewById(R.id.emailtxt);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
        }
    }
}
