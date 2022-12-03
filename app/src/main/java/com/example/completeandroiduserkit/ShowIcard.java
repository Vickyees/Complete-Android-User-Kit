package com.example.completeandroiduserkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ShowIcard extends AppCompatActivity {
ImageView imageView,imageView2;

    String uid;
    String card;
    TextView textView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_icard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        card=getIntent().getExtras().getString("card");
        textView=(TextView)findViewById(R.id.t) ;
        textView.setText(card);
        setTitle(card);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://complete-android-user-kit.appspot.com/");


        imageView = (ImageView)findViewById(R.id.card);

        try {
            final File file;
            file = File.createTempFile("image","jpg");
            storageReference.child(card).child(uid).child("front").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShowIcard.this, " Front Image Not Available", Toast.LENGTH_SHORT).show();
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.not));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        imageView2 = (ImageView)findViewById(R.id.card2);

        try {
            final File file;
            file = File.createTempFile("image","jpg");
            storageReference.child(card).child(uid).child("back").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView2.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShowIcard.this, "Back Image Not Available", Toast.LENGTH_SHORT).show();
                    imageView2.setImageDrawable(getResources().getDrawable(R.drawable.not));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void AddIcard(View view) {
        Intent intent=new Intent(ShowIcard.this,Icard.class);
        intent.putExtra("card",card);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void delete(View view) {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.del)
                .setTitle("DELETE")
                .setMessage("Are you sure you want to DELETE"+"   "+card+"?")
                .setCancelable(false)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        storageReference.child(card).child(uid).child("front").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                storageReference.child(card).child(uid).child("back").delete();
                                Toast.makeText(ShowIcard.this, card+"  "+"Deleted Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                               // Log.d(TAG, "onFailure: did not delete file");
                                Toast.makeText(ShowIcard.this, "Image not Found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }




}
