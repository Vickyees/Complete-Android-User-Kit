package com.example.completeandroiduserkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class websplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_websplash);

        getSupportActionBar().hide();
        Thread splashTread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    SharedPreferences preferences =getSharedPreferences("URL",0);
                    String url=preferences.getString("url","0");
                    if(url.equals("0")){
                        Intent intent =new Intent(getApplicationContext(),UrlActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent =new Intent(getApplicationContext(),WebMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        splashTread.start();
    }
}
