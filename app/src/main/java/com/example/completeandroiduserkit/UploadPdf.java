package com.example.completeandroiduserkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPdf extends AppCompatActivity {

    EditText editPDFName;
    Button btn_uploadpdf;
    FirebaseUser user1;
    String uid;
    StorageReference storageReference;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        setTitle("ADD PDF");



     editPDFName=(EditText)findViewById(R.id.pdfname);
     btn_uploadpdf=(Button)findViewById(R.id.uploadpdf);
        user1= FirebaseAuth.getInstance().getCurrentUser();
        uid=user1.getUid();
    storageReference= FirebaseStorage.getInstance().getReference();
    databaseReference= FirebaseDatabase.getInstance().getReference("PDF").child(uid);

    btn_uploadpdf.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectPDFFile();
        }
    });
    }

    private  void selectPDFFile(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        user1= FirebaseAuth.getInstance().getCurrentUser();
        uid=user1.getUid();
        StorageReference reference=storageReference.child("PDF").child(uid);
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url =uri.getResult();

                        Uploadpdf1 uploadpdf1= new Uploadpdf1(editPDFName.getText().toString(),url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadpdf1);
                        Toast.makeText(UploadPdf.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded : "+(int)progress+"%");
            }
        });
    }

}
