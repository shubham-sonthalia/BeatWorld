package com.example.firebase_google_login;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class storage extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    private static final int PICK_AUDIO =123;
    Button audioupload;
    EditText audioName;
    String audioNameText;
    private firestoreRecyclerVewAdapter adapter;
    String getLocationFromMap;
    static long createdAt;

    JcPlayerView jcplayerView;
    ArrayList<JcAudio> jcAudios;
    private static storage storageInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

//        audioupload=findViewById(R.id.uploadAudioButton);
//        audioName=findViewById(R.id.audioName);

        jcplayerView =  findViewById(R.id.jcplayer);
        jcAudios = new ArrayList<>();
        storageInstance=this;
        double lat, lng;
        String title;
        Intent intent = this.getIntent();
        lat = intent.getDoubleExtra("lat", 38.8951);
        lng = intent.getDoubleExtra("lng", -77.0364);
        title = intent.getStringExtra("title");

        //getLocationFromMap=String.valueOf(lat) + "," + String.valueOf(lng) + " " + title;
        getLocationFromMap=title;
        Log.i("storage activity","current location = "+getLocationFromMap);

       // getLocationFromMap="38.8951,-77.0364";
        if(lat == 38.8951 && lng == -77.0364){
            getLocationFromMap="38.8951,-77.0364";
        }

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirestoreRecyclerOptions<AudioFile> options = new FirestoreRecyclerOptions.Builder<AudioFile>()
                .setQuery(firebaseFirestore.collection(getLocationFromMap), AudioFile.class)
                .build();
        //.orderBy("createdAt", Query.Direction.DESCENDING)
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        adapter=new firestoreRecyclerVewAdapter(options,this);
        recyclerView.setAdapter(adapter);

//        FirebaseFirestore.getInstance().collection(getLocationFromMap).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                //ArrayList<MusicPlayerSorting> tempArr=new ArrayList<>();
//                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
//                    String songName=documentSnapshot.getId();
//                    AudioFile audioFile=documentSnapshot.toObject(AudioFile.class);
//                    jcAudios.add(JcAudio.createFromURL(songName,audioFile.getDownloadUrl()));
//                    Log.i("AllMusic",songName);
//                    //tempArr.add(new MusicPlayerSorting(songName,audioFile));
//                }
////                Collections.sort(tempArr);
////                for(int i=0;i<tempArr.size();i++){
////                    jcAudios.add(JcAudio.createFromURL(tempArr.get(i).songName,tempArr.get(i).audioFile.getDownloadUrl()));
////                    Log.i("AllMusic",tempArr.get(i).songName);
////                }
//                jcplayerView.initPlaylist(jcAudios, null);
//
//            }
//        });

//        FirebaseFirestore.getInstance().collection(getLocationFromMap).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error==null) {
//                    jcAudios = new ArrayList<>();
//                    for (QueryDocumentSnapshot documentSnapshot : value) {
//                        String songName = documentSnapshot.getId();
//                        AudioFile audioFile = documentSnapshot.toObject(AudioFile.class);
//                        jcAudios.add(JcAudio.createFromURL(songName, audioFile.getDownloadUrl()));
//                        Log.i("realTimeMusic",songName);
//                    }
//                    jcplayerView.initPlaylist(jcAudios, null);
//                }
//                //Log.i("realTimeMusic","error.toString()");
//            }
//        });

        getRealTimeSongs();
    }
    public void getRealTimeSongs(){
        FirebaseFirestore.getInstance().collection(getLocationFromMap).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //ArrayList<MusicPlayerSorting> tempArr=new ArrayList<>();
                jcAudios = new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    String songName=documentSnapshot.getId();
                    AudioFile audioFile=documentSnapshot.toObject(AudioFile.class);
                    jcAudios.add(JcAudio.createFromURL(songName,audioFile.getDownloadUrl()));
                    Log.i("AllMusic",songName);
                    //tempArr.add(new MusicPlayerSorting(songName,audioFile));
                }
//                Collections.sort(tempArr);
//                for(int i=0;i<tempArr.size();i++){
//                    jcAudios.add(JcAudio.createFromURL(tempArr.get(i).songName,tempArr.get(i).audioFile.getDownloadUrl()));
//                    Log.i("AllMusic",tempArr.get(i).songName);
//                }
                if(jcAudios.size()>0) {
                    jcplayerView.initPlaylist(jcAudios, null);
                }

            }
        });
    }

    public void playAudioUsingMediaPlayer(int position){
        //final int position1=position;
        if(position<jcAudios.size()) {
            Log.d("MusicNumber", position + "");
            jcplayerView.playAudio(jcAudios.get(position));
            jcplayerView.createNotification();
        }
        //position=Integer.MAX_VALUE;

    }
    public static storage getStorageInstance(){
        return storageInstance;
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

    @Override
    public void applyTexts(String username, String password) {

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





    public static void uploadAudioFile(Activity activity){
        //audioNameText=audioName.getText().toString().trim();
        //createdAt=System.currentTimeMillis();
        //if(audioNameText.length()>0) {
            //audioNameText+="-"+createdAt;
        createdAt=System.currentTimeMillis();
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, "Select Audio"), PICK_AUDIO);
//            audioName.setText("");
//        }
//        else{
//            Toast.makeText(this, "Give Audio a Name", Toast.LENGTH_SHORT).show();
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_AUDIO){
            if(resultCode==RESULT_OK){
                Uri audioUri=data.getData();


                Cursor mcursor = getApplicationContext().getContentResolver().query(audioUri,null,null,null,null);
                int indexedname = mcursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                mcursor.moveToFirst();
                String songName = mcursor.getString(indexedname)+" - "+createdAt;
                mcursor.close();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.show();


                Toast.makeText(this, "Audio select", Toast.LENGTH_SHORT).show();
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Aduio").child(songName+".mp3");
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

                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                                Date resultdate = new Date(createdAt);
                                String date=(sdf.format(resultdate));

                                AudioFile audioFile = new AudioFile(userName,photoUrl,userUid,100,audioUrl,createdAt,date, "", "");
                                //String getLocationFromMap="38.8951,-77.0364";
                                audioFile.uploadAudioFile(getLocationFromMap,songName,audioFile);

                                AudioFile tempAudioFile=new AudioFile(getLocationFromMap,photoUrl,userUid,100,audioUrl,createdAt,date, "", "");
                                tempAudioFile.uploadUserData(userUid,songName,tempAudioFile);

                                Toast.makeText(storage.this, "audio uploaded to Firestore", Toast.LENGTH_SHORT).show();
                                Log.i("URL",audioUrl);
                                progressDialog.dismiss();
                                getRealTimeSongs();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //progressBar.setVisibility(View.GONE);
                                Toast.makeText(storage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                       }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progres = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        int currentProgress = (int)progres;
                        progressDialog.setMessage("Uploaded: "+currentProgress+"%");
                    }
                });

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.upload_icon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.uploadMenuIcon){
            openDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
}