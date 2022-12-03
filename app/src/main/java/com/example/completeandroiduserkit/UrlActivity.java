package com.example.completeandroiduserkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UrlActivity extends AppCompatActivity {
EditText editText;
String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorview = getWindow().getDecorView();
        decorview.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_url);
        editText=(EditText)findViewById(R.id.text1);

    }

    public void done(View view) {
        a=editText.getText().toString();
        if(a.equals("")){
            Toast.makeText(this, "Enter URL", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SharedPreferences preferences =getSharedPreferences("URL",0);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("url",a);
            editor.apply();
            startActivity(new Intent(getApplicationContext(),WebMainActivity.class));
            finish();
        }
    }
}
