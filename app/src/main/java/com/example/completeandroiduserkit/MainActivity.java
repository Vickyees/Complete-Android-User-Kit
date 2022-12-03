package com.example.completeandroiduserkit;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button b1,b2,b3,b4,b5,b6,b7,fb,insta,twitter,intant,dic,b13,b14,b15,b16,b17,b18,b19,b20,b21;
    private DatabaseReference rootRef;
    private String currentUserID;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();
        rootRef=FirebaseDatabase.getInstance().getReference();

        b1=(Button) findViewById(R.id.webapp);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,websplash.class);
                startActivity(i);
            }
        });

        b2=(Button)findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });

        b3=(Button)findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PdfMainActivity.class);
                startActivity(i);
            }
        });

        b4=(Button) findViewById(R.id.ocr);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,OcrMainActivity.class);
                startActivity(i);
            }
        });

        b5=(Button)findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,NewsSplash.class);
                startActivity(i);
            }
        });

        b6=(Button) findViewById(R.id.wall);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,WallMainActivity.class);
                startActivity(i);
            }
        });

        b7=(Button)findViewById(R.id.qr);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,QrMainActivity.class);
                startActivity(i);
            }
        });

        intant=(Button)findViewById(R.id.instanthelp);
        intant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,HelpMainActivity.class);
                startActivity(i);
            }
        });

        dic=(Button)findViewById(R.id.dictionary);
        dic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,DictionaryMainActivity.class);
                startActivity(i);
            }
        });

        fb=(Button)findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,FBMainActivity.class);
                startActivity(i);
            }
        });

        insta=(Button)findViewById(R.id.instagram);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,InstaMainActivity.class);
                startActivity(i);
            }
        });

        twitter=(Button)findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,TwitterMainActivity.class);
                startActivity(i);

            }
        });
        b13=findViewById(R.id.b13);
        b13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ETSplash.class);
                startActivity(i);
            }
        });
        b14=findViewById(R.id.b14);
        b14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DOMainActivity.class);
                startActivity(i);
            }
        });
        b15=findViewById(R.id.b15);
        b15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CalMainActivty.class);
                startActivity(i);
            }
        });
        b16=findViewById(R.id.b16);
        b16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SWMainActivty.class);
                startActivity(i);
            }
        });
        b17=findViewById(R.id.b17);
        b17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DialMainActivity.class);
                startActivity(i);
            }
        });
        b18=findViewById(R.id.b18);
        b18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,uzsplash.class);
                startActivity(i);
            }
        });
        b19=findViewById(R.id.b19);
        b19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DoveSplash.class);
                startActivity(i);
            }
        });
        b20=findViewById(R.id.b20);
        b20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,XOSplashActivity.class);
                startActivity(i);
            }
        });
        b21=findViewById(R.id.b21);
        b21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,WCSplash.class);
                startActivity(i);
            }
        });

        
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void logout(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences preferences1 =getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor1=preferences1.edit();
                        editor1.putString("password","0");
                        editor1.apply();
                        SharedPreferences preferences=getSharedPreferences("com.example.completeandroiduserkit",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("Loginstatus","off");
                        editor.apply();
                        finish();
                        Intent intent=new Intent(MainActivity.this,Login.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser ==  null)
        {

        }
        else
        {
            updateUserStatus("online");


        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser != null)
        {
            updateUserStatus("offline");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser != null)
        {
            updateUserStatus("offline");

        }
    }
    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/YY");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        saveCurrentTime= currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap =new HashMap<>();
        onlineStateMap.put("time",saveCurrentTime);
        onlineStateMap.put("date",saveCurrentDate);
        onlineStateMap.put("state",state);

        currentUserID=mAuth.getCurrentUser().getUid();

        rootRef.child("Dove Chat").child("Users").child(currentUserID).child("userState")
                .updateChildren(onlineStateMap);


    }

}

