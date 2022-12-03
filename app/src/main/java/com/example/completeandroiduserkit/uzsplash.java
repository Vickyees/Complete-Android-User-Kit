package com.example.completeandroiduserkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class uzsplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_u_z_splash);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences=getSharedPreferences("com.example.completeandroiduserkit",MODE_PRIVATE);
                    String check=sharedPreferences.getString("Loginstatus","off");
                    if(check.equals("on")){
                        sleep(1000);
                        Intent home = new Intent(uzsplash.this, UZMainActivity.class);
                        startActivity(home);
                        finish();                 }
                    else{
                        sleep(1000);
                        Intent home = new Intent(uzsplash.this, Login.class);
                        startActivity(home);
                        finish();
                    }

                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
