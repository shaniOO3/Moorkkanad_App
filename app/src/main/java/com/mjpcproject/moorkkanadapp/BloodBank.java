package com.mjpcproject.moorkkanadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class BloodBank extends AppCompatActivity {

    private static final int request_call = 1;
    String cuser, bloodgroup, no;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    RecyclerView bloodbank;
    FirestoreRecyclerAdapter adapter;
    FloatingActionButton add, remove;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        auth = FirebaseAuth.getInstance();
        cuser = auth.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        bloodbank = findViewById(R.id.firestore_blood_list);
        add = findViewById(R.id.blood_add_btn);
        remove = findViewById(R.id.blood_remove_btn);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.bloodgroups, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgroup = adapterView.getItemAtPosition(i).toString();
                recyclerview_bloodbank();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        firebaseFirestore.collection("BloodBank").document(cuser).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            remove.setVisibility(View.VISIBLE);
                        } else {
                            add.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    public void recyclerview_bloodbank() {

        Query query = firebaseFirestore.collection("BloodBank").whereEqualTo("blood", bloodgroup);

        FirestoreRecyclerOptions<FirestoreBloodList> options = new FirestoreRecyclerOptions.Builder<FirestoreBloodList>()
                .setQuery(query, FirestoreBloodList.class).build();

        adapter = new FirestoreRecyclerAdapter<FirestoreBloodList, BloodlistViewHolder>(options) {
            @NonNull
            @Override
            public BloodlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new BloodlistViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final BloodlistViewHolder holder, int position, @NonNull FirestoreBloodList model) {
                holder.list_name.setText(model.getName());
                holder.list_blood.setText(model.getBlood());
                holder.list_phone.setText(model.getPhone());

                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no = "tel:" + holder.list_phone.getText().toString();
                        makecall();
                    }
                });

            }
        };

        bloodbank.setHasFixedSize(true);
        bloodbank.setLayoutManager(new LinearLayoutManager(this));
        bloodbank.setAdapter(adapter);
        adapter.startListening();
    }

    private void makecall() {
        if (ContextCompat.checkSelfPermission(BloodBank.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BloodBank.this, new String[]{Manifest.permission.CALL_PHONE}, request_call);
        } else {
            Intent intent = new Intent((Intent.ACTION_CALL));
            intent.setData(Uri.parse(no));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == request_call) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makecall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BloodlistViewHolder extends RecyclerView.ViewHolder {

        TextView list_name;
        TextView list_blood;
        TextView list_phone;
        ImageView call;

        public BloodlistViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.donarname);
            list_blood = itemView.findViewById(R.id.donarblood);
            list_phone = itemView.findViewById(R.id.donarphone);
            call = itemView.findViewById(R.id.call);

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}