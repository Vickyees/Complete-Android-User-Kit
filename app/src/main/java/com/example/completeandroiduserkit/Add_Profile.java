package com.example.completeandroiduserkit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Profile extends AppCompatActivity {
    EditText name,c_no,c_add,p_add,email,gender,dob,blood,f_name,f_no;
    FirebaseUser user1;
    String uid;


    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__profile);
        setTitle("ADD PROFILE");

        MobileAds.initialize(this, "ca-app-pub-2605022458231805~9173069248");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        name=(EditText)findViewById(R.id.e1);
        c_no=(EditText)findViewById(R.id.e2);
        c_add=(EditText)findViewById(R.id.e3);
        p_add=(EditText)findViewById(R.id.e4);
        email=(EditText)findViewById(R.id.e5);
        gender=(EditText)findViewById(R.id.e6);
        dob=(EditText)findViewById(R.id.e7);
        blood=(EditText)findViewById(R.id.e8);
        f_name=(EditText)findViewById(R.id.e9);
        f_no=(EditText)findViewById(R.id.e10);



    }

    public void btn_upload_profile(View view) {

        String t1=name.getText().toString();
        String t2=c_no.getText().toString();
        String t3=c_add.getText().toString();
        String t4=p_add.getText().toString();
        String t5=email.getText().toString();
        String t6=gender.getText().toString();
        String t7=dob.getText().toString();
        String t8=blood.getText().toString();
        String t9=f_name.getText().toString();
        String t10=f_no.getText().toString();

        if(t1.equals("")|t2.equals("")|t3.equals("")|t4.equals("")|t5.equals("")|t6.equals("")|t7.equals("")|t8.equals("")|t9.equals("")|t10.equals("")){
            Toast.makeText(this, "please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        if(t2.length()!=10){
            Toast.makeText(this, "enter correct contact number", Toast.LENGTH_SHORT).show();
        }
        if(t10.length()!=10){
            Toast.makeText(this, "enter correct friend's number", Toast.LENGTH_SHORT).show();
        }
        else{
            User user =new User();
            user.setName(t1);
            user.setC_no(t2);
            user.setC_add(t3);
            user.setP_add(t4);
            user.setEmail(t5);
            user.setGender(t6);
            user.setDob(t7);
            user.setBlood(t8);
            user.setF_name(t9);
            user.setF_no(t10);

            user1= FirebaseAuth.getInstance().getCurrentUser();
            uid=user1.getUid();



            //String current = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            FirebaseDatabase.getInstance().getReference("profile").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Add_Profile.this, "uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Add_Profile.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }
            });

        }





    }
}
