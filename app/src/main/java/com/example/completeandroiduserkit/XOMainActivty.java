package com.example.completeandroiduserkit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class XOMainActivty extends AppCompatActivity {

    EditText p1,p2;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_x_o_main_activty);
        getSupportActionBar().hide();

        p1=(EditText)findViewById(R.id.p1);
        p2=(EditText)findViewById(R.id.p2);
        t=(TextView)findViewById(R.id.t);

    }


    public void Start(View view) {
        if (p1.getText().toString().equals("")||p2.getText().toString().equals("")||t.getText().toString().equals("")){
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Please enter Player Names");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
        else {
            Intent intent = new Intent(XOMainActivty.this, XOGameActivity.class);
            intent.putExtra("p1", p1.getText().toString());
            intent.putExtra("p2", p2.getText().toString());
            intent.putExtra("t", t.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alert =new AlertDialog.Builder(XOMainActivty.this);
        View mView =getLayoutInflater().inflate(R.layout.quit,null);

        final Button quit =(Button)mView.findViewById(R.id.quit);
        final Button cancel =(Button)mView.findViewById(R.id.cancel);

        alert.setView(mView);
        final AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(XOMainActivty.this,MainActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void plus(View view) {
        int a=Integer.parseInt(t.getText().toString());
        a=a+5;
        t.setText(String.valueOf(a));

    }

    public void minus(View view) {
        int ab=Integer.parseInt(t.getText().toString());
        if (ab>10) {
            ab = ab - 5;
            t.setText(String.valueOf(ab));
        }
        else {
            LayoutInflater inflater=getLayoutInflater();
            View layout=inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_root));
            TextView toastText=layout.findViewById(R.id.text);
            toastText.setText("Minimum target is 10");
            Toast toast=new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }
}
