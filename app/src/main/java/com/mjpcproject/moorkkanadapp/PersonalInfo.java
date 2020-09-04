package com.mjpcproject.moorkkanadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PersonalInfo extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    String currentUser;
    EditText name;
    AutoCompleteTextView ward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        name = findViewById(R.id.nameField);

        String[] wards = getResources().getStringArray(R.array.wards);

        ward = findViewById(R.id.wardNoField);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, wards);
        ward.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseCheck();

    }

    public void databaseCheck() {

        firebaseFirestore.collection("Users").document(currentUser).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String fname = documentSnapshot.getString("Name");
                            String fward = documentSnapshot.getString("Ward");
                            name.setText(fname);
                            ward.setText(fward);
                        }
                    }
                });

    }

    public void personalInfoAddButton(View view) {

        String mname = name.getText().toString();
        String mward = ward.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String phoneNo = sharedPreferences.getString("PhoneNo", "");

        if (mname.isEmpty()) {
            name.setError("please enter your name");
            name.requestFocus();
        } else if (mward.isEmpty()) {
            ward.setError("please select your ward");
            ward.requestFocus();
        } else {
            Map<String, Object> user = new HashMap<>();
            user.put("Name", mname);
            user.put("Phone No", phoneNo);
            user.put("Ward", mward);

            DocumentReference documentReference = firebaseFirestore.collection("Users").document(currentUser);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            });
        }

    }
}