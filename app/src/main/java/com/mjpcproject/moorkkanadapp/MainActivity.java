package com.mjpcproject.moorkkanadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private long backPressedTime;
    private Toast backToast;
    Fragment fragment;
    TextView name, phone, ward;
    View headerview;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getUid();
        firestore = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav);
        headerview = navigationView.getHeaderView(0);
        name = (TextView) headerview.findViewById(R.id.show_name);
        phone = (TextView) headerview.findViewById(R.id.show_phone_no);
        ward = (TextView) headerview.findViewById(R.id.show_ward);
        data();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home()).commit();
        navigationView.setCheckedItem(R.id.home_btn);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                moveTaskToBack(true);
                System.exit(0);

            }
            else {
                backToast = Toast.makeText(getBaseContext(),"Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_btn) {
            fragment = new Home();
        }
        if (id == R.id.change_language_btn){
            fragment = new ChangeLanguage();
        }
        if (id == R.id.about_us_btn){
            fragment = new AboutUs();
        }
        if (id == R.id.logout_btn){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void data(){
        firestore.collection("Users").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String fname = documentSnapshot.getString("Name");
                    String fphone = documentSnapshot.getString("Phone No");
                    String fward = documentSnapshot.getString("Ward");

                    name.setText(fname);
                    phone.setText(fphone);
                    ward.setText(fward);
                }
            }
        });
    }

}