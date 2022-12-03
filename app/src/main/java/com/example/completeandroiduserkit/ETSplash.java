package com.example.completeandroiduserkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ETSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_e_t_splash);

        getSupportActionBar().hide();
        Thread splashTread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);

                    Intent intent =new Intent(getApplicationContext(),ETMainActivty.class);
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
