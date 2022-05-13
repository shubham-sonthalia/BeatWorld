package com.example.firebase_google_login;
import com.example.firebase_google_login.storage;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExampleDialog extends AppCompatDialogFragment {
    Button image_upload,music_upload;
    EditText quoteText;
    private ExampleDialogListener listener;
    private static final int SELECT_AUDIO = 2;

    Uri selectedImageUri=null;
    Uri audioUri=null;
    static long createdAt;
    final String[] imageUrl = new String[1];

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Uri uri = data.getData();
//        Log.i("imagePicker",uri.toString());


       // if (resultCode == Context.RESULT_OK) {
            if (requestCode == SELECT_AUDIO) {
                selectedImageUri = data.getData();
//                selectedImageUri = handleImageUri(selectedImageUri);
//                String selectedPath = getRealPathFromURI(selectedImageUri);
//                tvStatus.setText("Selected Path :: " + selectedPath);
                Log.i("imagePicker", " Path :: " + selectedImageUri);
                createdAt = System.currentTimeMillis();
                StorageReference storageReferenceImage = FirebaseStorage.getInstance().getReference().child("Images").child(createdAt + ".jpg");
                storageReferenceImage.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReferenceImage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                imageUrl[0] = task.getResult().toString();
                                Log.i("URL", imageUrl[0]);

                            }
                        });
                    }
                });
            }
            Log.i("upload","gfgfggf");
        if (requestCode == 3) {
            audioUri = data.getData();
//                selectedImageUri = handleImageUri(selectedImageUri);
//                String selectedPath = getRealPathFromURI(selectedImageUri);
//                tvStatus.setText("Selected Path :: " + selectedPath);
            Log.i("imagePicker", " Path :: " + audioUri);
        }
        //}
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        image_upload = (Button) view.findViewById(R.id.upload_btn);
        music_upload = (Button) view.findViewById(R.id.music_upload);
        quoteText=view.findViewById(R.id.quote);


        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImagePicker.with(ExampleDialog.this)
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image "), SELECT_AUDIO);

            }
        });

        music_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //storage.uploadAudioFile(getActivity());
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), 3);
            }
        });

        builder.setView(view)
                .setTitle("Login")
                .setCancelable(false)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {




                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(audioUri!=null && selectedImageUri!=null){
                            createdAt = System.currentTimeMillis();






//                            Thread temp=new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    StorageReference storageReferenceImage = FirebaseStorage.getInstance().getReference().child("Images").child(createdAt + ".jpg");
//                                    storageReferenceImage.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                            storageReferenceImage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Uri> task) {
//                                                    imageUrl[0] = task.getResult().toString();
//                                                    Log.i("URL", imageUrl[0]);
//
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            });
//                            temp.start();
//                            try {
//                                temp.join();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }




                            Cursor mcursor = storage.getStorageInstance().getApplicationContext().getContentResolver().query(audioUri, null, null, null, null);
                            int indexedname = mcursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            mcursor.moveToFirst();
                            String songName = mcursor.getString(indexedname) + " - " + createdAt;
                            mcursor.close();
                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                            progressDialog.show();


                            Toast.makeText(storage.getStorageInstance(), "Audio select", Toast.LENGTH_SHORT).show();
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Aduio").child(songName + ".mp3");
                            storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (firebaseUser != null) {
                                        String userName = firebaseUser.getDisplayName();
                                        String photoUrl = firebaseUser.getPhotoUrl().toString();
                                        String userUid = firebaseUser.getUid();




                                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                String audioUrl = task.getResult().toString();

                                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                                                Date resultdate = new Date(createdAt);
                                                String date = (sdf.format(resultdate));

                                                AudioFile audioFile = new AudioFile(userName, photoUrl, userUid, 100, audioUrl, createdAt, date,imageUrl[0],quoteText.getText().toString());
                                                //String getLocationFromMap="38.8951,-77.0364";
                                                audioFile.uploadAudioFile(storage.getStorageInstance().getLocationFromMap, songName, audioFile);

                                                AudioFile tempAudioFile = new AudioFile(storage.getStorageInstance().getLocationFromMap, photoUrl, userUid, 100, audioUrl, createdAt, date,imageUrl[0],quoteText.getText().toString());
                                                tempAudioFile.uploadUserData(userUid, songName, tempAudioFile);

                                                Toast.makeText(storage.getStorageInstance(), "audio uploaded to Firestore", Toast.LENGTH_SHORT).show();
                                                Log.i("URL", audioUrl);
                                                progressDialog.dismiss();
                                                storage.getStorageInstance().getRealTimeSongs();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    double progres = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                    int currentProgress = (int) progres;
                                    progressDialog.setMessage("Uploaded: " + currentProgress + "%");
                                }
                            });
                        }
                        else{
                            Toast.makeText(storage.getStorageInstance(), "Give image and audio both", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String username, String password);
    }
}
