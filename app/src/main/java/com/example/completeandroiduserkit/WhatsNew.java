package com.example.completeandroiduserkit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WhatsNew extends AppCompatActivity {

    RecyclerView wrecyclerView;
    List<AdsData>myAdsList;
    AdsData adsData;
    public DatabaseReference databaseReference;
    public ValueEventListener eventListener;
    ProgressBar progressBar;
    EditText txt_search;
    LinearLayout note;
    FloatingActionButton addADD;
    String uid="yqrt1I0mHqe8VcpTB0TGIIszkvf2";
    FirebaseUser user1;
    String cuid;
    public AdView NadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new);
        setTitle("Whats New ?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wrecyclerView=(RecyclerView)findViewById(R.id.adRecycler);
        progressBar=(ProgressBar)findViewById(R.id.mainprogressbar);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(WhatsNew.this,1);
        wrecyclerView.setLayoutManager(gridLayoutManager);
        txt_search=(EditText)findViewById(R.id.serbyet);
        note=(LinearLayout)findViewById(R.id.note);
        addADD=(FloatingActionButton)findViewById(R.id.addAD);


        MobileAds.initialize(this,"ca-app-pub-7762392222724955~2438737873") ;
        NadView = (AdView) findViewById(R.id.NadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        NadView.loadAd(adRequest);

        user1 = FirebaseAuth.getInstance().getCurrentUser();
        cuid = user1.getUid();

        if (cuid.equals(uid)){
            addADD.setVisibility(View.VISIBLE);
        }

        myAdsList=new ArrayList<>();

        final AdsMyAdapter adsMyAdapter=new AdsMyAdapter(WhatsNew.this,myAdsList);
        wrecyclerView.setAdapter(adsMyAdapter);

        databaseReference= FirebaseDatabase.getInstance().getReference("Advertise");
        progressBar.setVisibility(View.VISIBLE);
        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                   myAdsList.clear();
                    Toast.makeText(WhatsNew.this, "No adverticement for now", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
                else {

                    myAdsList.clear();
                    for (DataSnapshot itemsnapshot : dataSnapshot.getChildren()) {
                        AdsData adsData = itemsnapshot.getValue(AdsData.class);
                        adsData.setAdKey(itemsnapshot.getKey());
                        myAdsList.add(adsData);
                    }
                    adsMyAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());

            }

            private void filter(String text) {
                ArrayList<AdsData> filterList = new ArrayList<>();

                for (AdsData itema : myAdsList) {


                        if (itema.getAdLocation().toUpperCase().contains(text.toUpperCase().trim())) {

                            filterList.add(itema);

                        }

                    }


                adsMyAdapter.filteredList(filterList);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.whatsnew ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.info:

                if (note.getVisibility()==View.VISIBLE){
                    note.setVisibility(View.GONE);
                }
                else {
                    note.setVisibility(View.VISIBLE);
                }

                break;
                case R.id.home:
                    startActivity(new Intent(WhatsNew.this, UZMainActivity.class));
                    finish();
        }
        return true;
    }


    public void addAddebtn(View view) {
        startActivity(new Intent(WhatsNew.this,AddAdverticement.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WhatsNew.this, UZMainActivity.class));
        finish();
    }
}
