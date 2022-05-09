package com.example.firebase_google_login;

import com.google.firebase.firestore.FirebaseFirestore;

public class AudioFile implements Comparable{
    private String userName;
    private String photoUrl;
    private String userUid;
    private long views;
    private String downloadUrl;
    private long createdAt;
    private String date;



    public AudioFile(){

    }


    public AudioFile(String userName, String photoUrl, String userUid, long views, String downloadUrl,long createdAt,String date){
        this.userName=userName;
        this.photoUrl=photoUrl;
        this.userUid=userUid;
        this.views=views;
        this.downloadUrl=downloadUrl;
        this.createdAt=createdAt;
        this.date=date;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public String getDate() {
        return date;
    }

    public void uploadAudioFile(String location,String audioFileName,AudioFile audioFile){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection(location).document(audioFileName).set(audioFile);
    }

    @Override
    public int compareTo(Object o) {
        AudioFile o1=(AudioFile) o;
        if(this.createdAt>o1.createdAt){
            return 1;
        }
        else if(this.createdAt<o1.createdAt){
            return -1;
        }
        return 0;
    }
}

class MusicPlayerSorting implements Comparable{
    String songName;
    AudioFile audioFile;

    public MusicPlayerSorting(String songName, AudioFile audioFile) {
        this.songName = songName;
        this.audioFile = audioFile;
    }

    @Override
    public int compareTo(Object o) {
        MusicPlayerSorting o1=(MusicPlayerSorting) o;
        if(this.audioFile.getCreatedAt()>o1.audioFile.getCreatedAt()){
            return 1;
        }
        else if(this.audioFile.getCreatedAt()<o1.audioFile.getCreatedAt()){
            return -1;
        }
        return 0;
    }
}
