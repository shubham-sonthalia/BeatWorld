package com.example.firebase_google_login;

import com.google.firebase.firestore.FirebaseFirestore;

public class AudioFile {
    private String userName;
    private String photoUrl;
    private String userUid;
    private long views;
    private String downloadUrl;

    public AudioFile(){

    }


    public AudioFile(String userName, String photoUrl, String userUid, long views, String downloadUrl){
        this.userName=userName;
        this.photoUrl=photoUrl;
        this.userUid=userUid;
        this.views=views;
        this.downloadUrl=downloadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUserUid() {
        return userUid;
    }

    public long getViews() {
        return views;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void uploadAudioFile(String location,String audioFileName,AudioFile audioFile){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection(location).document(audioFileName).set(audioFile);
    }
}
