package com.example.completeandroiduserkit;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class XOSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_x_o_splash);

        getSupportActionBar().hide();
        Thread splashTread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent =new Intent(getApplicationContext(),XOMainActivty.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        splashTread.start();



    }
}

