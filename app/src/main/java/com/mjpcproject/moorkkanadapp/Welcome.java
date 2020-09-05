package com.mjpcproject.moorkkanadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {

    //welcome
    Timer timer;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    databaseCheck();
                } else {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            }
        }, 4000);

    }

    public void databaseCheck() {

        firebaseFirestore.collection("Users").document(currentUser).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), PersonalInfo.class));
                            finish();
                        }
                    }
                });

    }

}