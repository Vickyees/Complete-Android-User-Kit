package com.example.completeandroiduserkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class DoveMainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;


    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dove_main);

        mToolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dove Chat");

        myViewPager=findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout=findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
        mAuth= FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.dove_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId()==R.id.main_settings_option){
            SendUserToSettingsActivity();

        }
        if(item.getItemId()==R.id.main_find_friends_option){
            SendUserToFindFriendsActivity();
        }
        return true;
    }



    private void SendUserToSettingsActivity() {
        Intent settingsIntent= new Intent(DoveMainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
    private void SendUserToFindFriendsActivity() {
        Intent findFriendsIntent= new Intent(DoveMainActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }
    private void VerifyUserExistanse() {
        String currentUserID=mAuth.getCurrentUser().getUid();
        rootRef.child("Dove Chat").child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())){
                }
                else{
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser ==  null)
        {
        }
        else
        {

            VerifyUserExistanse();

        }
    }




}
