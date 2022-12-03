package com.example.completeandroiduserkit;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SWMainActivty extends AppCompatActivity {

    Button btnStart,btnfinish;
    Chronometer chronometer;
    Animation rotate;
    ImageView imageView;
    TextView tv;
    long pause;
    String time="00.00";
    ImageButton btnreset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_w_main_activty);

        tv=findViewById(R.id.tv);
        btnStart = findViewById(R.id.btnStart);
        btnfinish = findViewById(R.id.btnFinish);
        chronometer = findViewById(R.id.chronometer);
        imageView = findViewById(R.id.imageView);

        rotate = AnimationUtils.loadAnimation(SWMainActivty.this,R.anim.rotation);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(rotate);
                chronometer.setFormat("%s");
                chronometer.setBase(SystemClock.elapsedRealtime()-pause);
                chronometer.start();
                btnStart.setVisibility(View.INVISIBLE);
                btnfinish.setVisibility(View.VISIBLE);
            }
        });

        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time= chronometer.getText().toString();
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                pause = 0;
                rotate.cancel();
                imageView.setAnimation(rotate);
                btnStart.setVisibility(View.VISIBLE);
                btnfinish.setVisibility(View.INVISIBLE);
                tv.setText(time);
            }
        });
        btnreset=findViewById(R.id.btnreset);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=time="00.00";
                tv.setText(time);
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                pause = 0;
                rotate.cancel();
                imageView.setAnimation(rotate);
                btnStart.setVisibility(View.VISIBLE);
                btnfinish.setVisibility(View.INVISIBLE);

            }
        });
    }
}
