package com.example.firebase_google_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebase_google_login.databinding.ActivityProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{

    private ActivityProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    public ImageView pfp;
    DatabaseReference mRef;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        firebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("User");
        pfp=findViewById(R.id.pfp);
        checkUser();

        binding.logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                googleSignInClient.signOut();
                checkUser();
            }
        });
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),storage.class));
                Intent intent=new Intent(ProfileActivity.this,UserData.class);
                startActivity(intent);
            }
        });
        binding.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
        binding.explorePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),find_frnds.class));
            }
        });
    }
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    private void checkUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else
        {
            binding.email.setText(firebaseUser.getEmail());
            binding.name.setText(firebaseUser.getDisplayName());
            Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).circleCrop().into(pfp);

            HashMap hashMap = new HashMap();
            hashMap.put("email",firebaseUser.getEmail());
            hashMap.put("username",firebaseUser.getDisplayName());
            hashMap.put("image",firebaseUser.getPhotoUrl().toString());
            hashMap.put("uid",firebaseUser.getUid());
            mRef.child(firebaseUser.getDisplayName()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener(){
                @Override
                public void onSuccess(Object o)
                {
                    Toast.makeText(ProfileActivity.this,"added to DB",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this,"error to DB",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void goToUserData(View v){
        Intent intent=new Intent(this,UserData.class);
        startActivity(intent);
    }

    @Override
    public void applyTexts(String username, String password) {

    }
}