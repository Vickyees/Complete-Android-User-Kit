package com.example.completeandroiduserkit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

import id.zelory.compressor.Compressor;

public class AddAdverticement extends AppCompatActivity {
    ImageView ADDImage;
    EditText et,el,ed,ec,es;
    private int STORAGE_CODE=1;
    Uri uri;
    byte[] final_image;
    String imageUrl;
    private BottomSheetDialog bottomSheetDialog;
    String myCurrentDateTime,timekey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adverticement);

        ADDImage=(ImageView)findViewById(R.id.ADDimage);
        et=(EditText)findViewById(R.id.ettitle);
        el=(EditText)findViewById(R.id.etLocation);
        ed=(EditText)findViewById(R.id.etDes);
        es=(EditText)findViewById(R.id.etService);
        ec=(EditText)findViewById(R.id.etContact);
    }



    public void selectIamge(View view) {

        if (ContextCompat.checkSelfPermission(AddAdverticement.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            CropImage.startPickImageActivity(AddAdverticement.this);
        }
        else {
            requestStorage();
        }

    }

    public void requestStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This permission is required to fetch image from storage")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(AddAdverticement.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==STORAGE_CODE){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                //requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {
                startCrop(imageuri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                uri = result.getUri();
                ADDImage.setImageURI(uri);
                Toast.makeText(this, "Image seleted", Toast.LENGTH_SHORT).show();

                File actualImage=new File(uri.getPath());

                try {
                    Bitmap compressedImage = new Compressor(AddAdverticement.this)
                            .setMaxWidth(500)
                            .setMaxHeight(300)
                            .setQuality(80)
                            .compressToBitmap(actualImage);


                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG,60,baos);
                    final_image=baos.toByteArray();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void uploadImage(){

        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("Advertice").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adverticement Uploading...");
        progressDialog.show();

        UploadTask uploadTask=storageReference.putBytes(final_image);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadService();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddAdverticement.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }


    public void uploadService(){

        AdsData adsData;
        adsData = new AdsData(
                et.getText().toString().toUpperCase(),
        el.getText().toString().toUpperCase(),
        es.getText().toString().toUpperCase(),
         ed.getText().toString(),
         ec.getText().toString(),
         imageUrl
        );


        myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        timekey=myCurrentDateTime;


        FirebaseDatabase.getInstance().getReference("Advertise")
                .child(timekey).setValue(adsData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(AddAdverticement.this, "Your Profile Uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddAdverticement.this, WhatsNew.class));
                    finish();

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddAdverticement.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void UploadAdd(View view) {
        if (uri==null){
            Toast.makeText(AddAdverticement.this, "Please Select picture", Toast.LENGTH_SHORT).show();
        }
        else {

            if (et.getText().toString().equals("")||el.getText().toString().equals("")||ed.getText().toString().equals("")||ed.getText().toString().equals("")){
                Toast.makeText(AddAdverticement.this, "Enter All the Fields", Toast.LENGTH_SHORT).show();
            }

            else {
                bottomSheetDialog=new BottomSheetDialog(AddAdverticement.this);
                bottomSheetDialog.setContentView(R.layout.dialog_layout);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                Button yes=bottomSheetDialog.findViewById(R.id.yes);
                Button No=bottomSheetDialog.findViewById(R.id.no);
                TextView t=bottomSheetDialog.findViewById(R.id.confirm);
                t.setText("Are You Sure Want To Add Advertisement ?");

                ImageView ynicon=bottomSheetDialog.findViewById(R.id.ynicon);
                ynicon.setImageResource(R.drawable.ic_person_add);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();
                        uploadImage();

                    }

                });

                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();

            }
        }
    }
}
