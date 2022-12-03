package com.example.completeandroiduserkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class inputpass extends AppCompatActivity {



    PatternLockView mPatternLockView;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputpass);


        View decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        SharedPreferences preferences =getSharedPreferences("PREFS",0);
        password=preferences.getString("password","0");


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
               if(password.equals(PatternLockUtils.patternToString(mPatternLockView, pattern))) {

                   Intent intent = new Intent(getApplicationContext(), SecurityPin.class);
                   startActivity(intent);
                   finish();
               }
               else {
                   Toast.makeText(inputpass.this, "Wrong pattern", Toast.LENGTH_SHORT).show();
                   mPatternLockView.clearPattern();
               }
            }

            @Override
            public void onCleared() {

            }
        });
    }


    public  void emer(View view){
        String pro="Profile_Picture";
        Intent intent1=new Intent(inputpass.this,Emergency.class);
        intent1.putExtra("card",pro);
        startActivity(intent1);

    }

    public void forget(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to Reset Pattern?" +
                        "Logout")
                .setCancelable(false)
                .setPositiveButton("RESET PATTERN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences preferences =getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("password","0");
                        editor.apply();
                        logout();
                        finish();
                        Intent intent =new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent =new Intent(inputpass.this,MainActivity.class);
        startActivity(intent);
    }


    public void logout(){
        SharedPreferences preferences=getSharedPreferences("com.example.completeandroiduserkit",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Loginstatus","off");
        editor.apply();
       //moveTaskToBack(true);
    }
}
