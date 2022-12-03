package com.example.completeandroiduserkit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class PerMainActivity extends AppCompatActivity {
    public String pro="Profile_Picture";
    public String voter = "Voter_ID";
    public String card = "ID_card";
    public String smart = "Smart_Card";
    public String w1 = "Adhaar_Card";
    public String w2 = "Driving_Licence";
    public String w3 = "Birth_Certificate";
    public String w4 = "Income_Certificate";
    public String w5 = "Pan_Card";
    public String w6 = "Passbook";
    public String w7 = "Other_1";
    public String w8 = "Other_2";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);




    }

    public void id(View view){
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",card);
        startActivity(intent1);

    }

    public void profile(View view) {
        Intent intent1=new Intent(PerMainActivity.this,Profile.class);
        intent1.putExtra("card",pro);
        startActivity(intent1);
        // startActivity(new Intent(getApplicationContext(),Profile.class));
    }

    public void pdf(View view) {
        startActivity(new Intent(getApplicationContext(),PDF.class));
    }

    public void voter(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",voter);
        startActivity(intent1);

    }

    public void smart(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",smart);
        startActivity(intent1);
    }

    public void adhaar(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w1);
        startActivity(intent1);
    }

    public void dl(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w2);
        startActivity(intent1);
    }

    public void passbook(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w6);
        startActivity(intent1);
    }

    public void pan(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w5);
        startActivity(intent1);
    }

    public void birth(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w3);
        startActivity(intent1);
    }

    public void income(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w4);
        startActivity(intent1);
    }

    public void other1(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w7);
        startActivity(intent1);
    }
    public void other2(View view) {
        Intent intent1=new Intent(PerMainActivity.this,ShowIcard.class);
        intent1.putExtra("card",w8);
        startActivity(intent1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent1=new Intent(PerMainActivity.this,MainActivity.class);
        startActivity(intent1);

    }
}
