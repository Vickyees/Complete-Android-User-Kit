package com.example.completeandroiduserkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class PDF extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        setTitle("PDF");

    }

    public void view(View view) {
        startActivity(new Intent(getApplicationContext(),View_PDF_File.class));
    }

    public void add(View view) {
        startActivity(new Intent(getApplicationContext(),UploadPdf.class));
    }
}
