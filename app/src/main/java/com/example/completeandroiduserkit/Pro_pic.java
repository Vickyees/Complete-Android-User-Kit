package com.example.completeandroiduserkit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Pro_pic extends AppCompatActivity {
ImageView i;
    FirebaseUser user1;
    String uid,card;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_pic);
       i=(ImageView)findViewById(R.id.pro);


        user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();
        card=getIntent().getExtras().getString("card");
        setTitle("PROFILE PICTURE");
        FirebaseStorage storage=FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://complete-android-user-kit.appspot.com/").child(card).child(uid);

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
                    Toast.makeText(Pro_pic.this, "Image Not Available", Toast.LENGTH_SHORT).show();
                    i.setImageDrawable(getResources().getDrawable(R.drawable.noo));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
