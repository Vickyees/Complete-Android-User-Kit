package com.example.completeandroiduserkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class OcrMainActivity extends AppCompatActivity {

    EditText mresult;
    ImageView mpriview;

    private static final int CAMERA_REQEST_CODE = 200;
    private static final int STORAGE_REQEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String camerapermission[];
    String storagepermission[];

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_main);
        ActionBar actionBar=getSupportActionBar();
        setTitle("Image To Text");

        mresult=findViewById(R.id.result);
        mpriview=findViewById(R.id.ocrimage);

        camerapermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ocr_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.addimage){
            showImageImportDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        String[] items = {"Camera","Gallery"};
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickCamera();
                    }

                }
                if(which == 1){

                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickGalery();
                    }

                }
            }
        });
        dialog.create().show();
    }

    private void pickGalery() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }


    private void pickCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagepermission,STORAGE_REQEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,camerapermission,CAMERA_REQEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result&&result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQEST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]==
                            PackageManager.PERMISSION_GRANTED;

                    boolean writestorageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted&&writestorageAccepted){
                        pickCamera();
                    }
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case STORAGE_REQEST_CODE:
                if(grantResults.length>0){

                    boolean writestorageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;

                    if (writestorageAccepted){
                        pickGalery();
                    }
                    else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);

            if (resultCode==RESULT_OK){
                Uri resultUri = result.getUri();

                mpriview.setImageURI(resultUri);
                BitmapDrawable bitmapDrawable =(BitmapDrawable)mpriview.getDrawable();
                Bitmap bitmap =bitmapDrawable.getBitmap();


                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if(!recognizer.isOperational()){
                    Toast.makeText(this, "Not Recognize", Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    for (int i=0;i<items.size();i++){
                        TextBlock myitem=items.valueAt(i);
                        sb.append(myitem.getValue());
                        sb.append("\n");

                    }

                    if(sb.toString().equals("")) {
                        Toast.makeText(this, "There is no text", Toast.LENGTH_SHORT).show();
                        mresult.setText("");
                    }

                    else {

                        mresult.setText(sb.toString());
                    }

                }

            }
            else {
                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
