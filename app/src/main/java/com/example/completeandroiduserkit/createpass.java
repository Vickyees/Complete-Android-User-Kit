package com.example.completeandroiduserkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class createpass extends AppCompatActivity {



    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpass);
        View decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if(PatternLockUtils.patternToString(mPatternLockView, pattern).length()>4){
                SharedPreferences preferences =getSharedPreferences("PREFS",0);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("password", PatternLockUtils.patternToString(mPatternLockView, pattern));
                editor.apply();

                Intent intent =new Intent(getApplicationContext(),SecurityPin.class);
                startActivity(intent);
                finish();}
                else {
                    Toast.makeText(createpass.this, "pattern is too short", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCleared() {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent =new Intent(createpass.this,MainActivity.class);
        startActivity(intent);
    }
}
