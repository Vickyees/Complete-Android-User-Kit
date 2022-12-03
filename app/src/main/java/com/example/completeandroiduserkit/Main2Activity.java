package com.example.completeandroiduserkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        View decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences =getSharedPreferences("PREFS",0);
                String password=preferences.getString("password","0");
                if(password.equals("0")){
                    Intent intent =new Intent(getApplicationContext(),createpass.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent =new Intent(getApplicationContext(),inputpass.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1000);
    }

}
