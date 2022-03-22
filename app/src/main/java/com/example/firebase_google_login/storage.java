package com.example.firebase_google_login;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class storage extends AppCompatActivity {

    private static final int PICK_AUDIO =123;
    Button audioupload;
    EditText audioName;
    String audioNameText;
    private firestoreRecyclerVewAdapter adapter;
    String getLocationFromMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        audioupload=findViewById(R.id.uploadAudioButton);
        audioName=findViewById(R.id.audioName);

        getLocationFromMap="38.8951,-77.0364";


        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirestoreRecyclerOptions<AudioFile> options = new FirestoreRecyclerOptions.Builder<AudioFile>()
                .setQuery(firebaseFirestore.collection(getLocationFromMap).orderBy("views", Query.Direction.DESCENDING), AudioFile.class)
                .build();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        adapter=new firestoreRecyclerVewAdapter(options,this);
        recyclerView.setAdapter(adapter);


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





    public void uploadAudioFile(View v){
        audioNameText=audioName.getText().toString().trim();
        if(audioNameText.length()>0) {
            audioNameText+="-"+System.currentTimeMillis();
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Audio"), PICK_AUDIO);
            audioName.setText("");
        }
        else{
            Toast.makeText(this, "Give Audio a Name", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_AUDIO){
            if(resultCode==RESULT_OK){
                Uri audioUri=data.getData();
                Toast.makeText(this, "Audio select", Toast.LENGTH_SHORT).show();
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Aduio").child(audioNameText+".mp3");
                storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                        if(firebaseUser!=null) {
                        String userName=firebaseUser.getDisplayName();
                        String photoUrl=firebaseUser.getPhotoUrl().toString();
                        String userUid=firebaseUser.getUid();


                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String audioUrl=task.getResult().toString();
                                AudioFile audioFile = new AudioFile(userName,photoUrl,userUid,100,audioUrl);
                                //String getLocationFromMap="38.8951,-77.0364";
                                audioFile.uploadAudioFile(getLocationFromMap,audioNameText,audioFile);
                                Toast.makeText(storage.this, "audio uploaded to Firestore", Toast.LENGTH_SHORT).show();
                                Log.i("URL",audioUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //progressBar.setVisibility(View.GONE);
                                Toast.makeText(storage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                       }
                    }
                });

            }
        }
    }
}