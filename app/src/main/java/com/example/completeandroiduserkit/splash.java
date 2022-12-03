package com.example.completeandroiduserkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class splash extends AppCompatActivity {

    ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash=(ImageView)findViewById(R.id.imageSplash);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.myanimation);
        splash.startAnimation(animation);


        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences=getSharedPreferences("com.example.completeandroiduserkit",MODE_PRIVATE);
                    String check=sharedPreferences.getString("Loginstatus","off");
                    if(check.equals("on")){
                        sleep(2000);
                        Intent home = new Intent(splash.this,MainActivity.class);
                        startActivity(home);
                        finish();                 }
                    else{
                        sleep(2000);
                        Intent home = new Intent(splash.this,Login.class);
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
