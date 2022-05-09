package com.example.firebase_google_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firebase_google_login.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    int rc_sign_in = 100;
    String tag = "GOOGLE_SIGN_IN_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        SignInButton signIn = findViewById(R.id.googlesigninbtn);
        signIn.setSize(signIn.SIZE_WIDE);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(tag,"onCreate : begin Google Signin");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,rc_sign_in);
            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            startActivity(new Intent(this,ProfileActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == rc_sign_in)
        {
            Log.d(tag,"onActivityResult: Google SignIn Intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.v("checking","in try block");
                    GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                    firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e)
            {
                Log.e(tag,e.toString());
            }
                
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.d(tag,"firebaseAuthWithGoogleAccount: begin firebase auth with google account ");
        AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credentials)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(tag,"onSuccess: Logged In");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();
                        Log.d(tag,"onSuccess: UID: " + uid);
                        Log.d(tag,"onSuccess: Email: " + email);

                        if(authResult.getAdditionalUserInfo().isNewUser())
                        {
                            Log.d(tag,"onSuccess: Account Created....\n" + email);
                            Toast.makeText(MainActivity.this,"Account Created ....\n" + email,Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.d(tag,"onSuccess: Existing User....\n" + email);
                            Toast.makeText(MainActivity.this,"Existing User ....\n" + email,Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(tag,"onFailure: Logged " + e.getMessage());
                    }
                });
    }
}