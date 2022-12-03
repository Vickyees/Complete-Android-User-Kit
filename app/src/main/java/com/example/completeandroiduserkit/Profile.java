package com.example.completeandroiduserkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Profile extends AppCompatActivity {
    TextView name,c_no,c_add,p_add,email,gender,dob,blood,f_name,f_no;
    Button btn;
    ImageView i;
    DatabaseReference reff;
    FirebaseUser user1;
    String uid,card;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        card=getIntent().getExtras().getString("card");
        setTitle("PROFILE");


        name=(TextView) findViewById(R.id.e1);
        c_no=(TextView) findViewById(R.id.e2);
        c_add=(TextView)findViewById(R.id.e3);
        p_add=(TextView) findViewById(R.id.e4);
        email=(TextView) findViewById(R.id.e5);
        gender=(TextView) findViewById(R.id.e6);
        dob=(TextView) findViewById(R.id.e7);
        blood=(TextView) findViewById(R.id.e8);
        f_name=(TextView) findViewById(R.id.e9);
        f_no=(TextView) findViewById(R.id.e10);
        btn=(Button) findViewById(R.id.btn1);
        i=(ImageView) findViewById(R.id.propic);
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();


        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://complete-android-user-kit.appspot.com/").child(card).child(uid).child("front");




    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            final File file;
            file = File.createTempFile("image","jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    i.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile.this, "Image Not Available", Toast.LENGTH_SHORT).show();
                    i.setImageDrawable(getResources().getDrawable(R.drawable.noo));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



        show();


    }



    public void show() {
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();
        reff = FirebaseDatabase.getInstance().getReference().child("profile").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String t1 = dataSnapshot.child("name").getValue().toString();
                        String t2 = dataSnapshot.child("c_no").getValue().toString();
                        String t3 = dataSnapshot.child("c_add").getValue().toString();
                        String t4 = dataSnapshot.child("p_add").getValue().toString();
                        String t5 = dataSnapshot.child("email").getValue().toString();
                        String t6 = dataSnapshot.child("gender").getValue().toString();
                        String t7 = dataSnapshot.child("dob").getValue().toString();
                        String t8 = dataSnapshot.child("blood").getValue().toString();
                        String t9 = dataSnapshot.child("f_name").getValue().toString();
                        String t10 = dataSnapshot.child("f_no").getValue().toString();



                        name.setText(t1.toUpperCase());
                        c_no.setText(t2);
                        c_add.setText(t3.toUpperCase());
                        p_add.setText(t4.toUpperCase());
                        email.setText(t5);
                        gender.setText(t6.toUpperCase());
                        dob.setText(t7);
                        blood.setText(t8.toUpperCase());
                        f_name.setText(t9.toUpperCase());
                        f_no.setText(t10);

                    }
                    else {
                        Toast.makeText(Profile.this, "add the data", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Profile.this, "network error", Toast.LENGTH_SHORT).show();
                }

            });
        }

    public void btn_add_profile(View view) {
        startActivity(new Intent(getApplicationContext(),Add_Profile.class));
    }

    public void propic(View view) {
        String card1="Profile_Picture";
        Intent intent=new Intent(Profile.this,Icard.class);
        intent.putExtra("card",card1);
        startActivity(intent);
        finish();
    }

    public void pro(View view) {
        String card1="Profile_Picture";
        Intent intent=new Intent(Profile.this,Pro_pic.class);
        intent.putExtra("card",card1);
        startActivity(intent);
        finish();
    }
}
