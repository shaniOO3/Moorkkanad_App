package com.mjpcproject.moorkkanadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText PhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PhoneNo = findViewById(R.id.phonenofield);

    }

    public void generateOTPButton(View view) {

        String phoneno = PhoneNo.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneNo", phoneno);
        editor.apply();

        if (phoneno.isEmpty()){
            PhoneNo.setError("Please enter your number");
            PhoneNo.requestFocus();
        }
        else if (phoneno.length() != 10){
            PhoneNo.setError("Please enter a valid number");
            PhoneNo.requestFocus();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Otp.class);
            startActivity(intent);
        }

    }

}