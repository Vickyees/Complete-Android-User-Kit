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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import id.zelory.compressor.Compressor;


public class AddService extends AppCompatActivity {
    FirebaseUser user1;
    String uid;
    Uri uri;
    ImageView adp;
    String imageUrl,gender="Male";
    public static String smile="0";
    EditText mName,mExprience,mAge,mPhone,mAddress,mDescription;
    String myCurrentDateTime,timekey;
    AutoCompleteTextView mLocation,mService;
    private BottomSheetDialog bottomSheetDialog;
    byte[] final_image;
    ArrayList<String> listCity=new ArrayList<String>();
    ArrayList<String> listService=new ArrayList<String>();
    private AdView MadView;
    private int STORAGE_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        setTitle("Add New Profile");

        MobileAds.initialize(this,"ca-app-pub-7762392222724955~2438737873") ;
        MadView = findViewById(R.id.AddadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        MadView.loadAd(adRequest);

        mName=(EditText)findViewById(R.id.dName);
        mService=(AutoCompleteTextView)findViewById(R.id.dService);
        mLocation=(AutoCompleteTextView) findViewById(R.id.dLocation);
        mPhone=(EditText)findViewById(R.id.dPhone);
        mExprience=(EditText)findViewById(R.id.dExprience);
        mAge=(EditText)findViewById(R.id.dAge);
        mAddress=(EditText)findViewById(R.id.dAddress);
        mDescription=(EditText)findViewById(R.id.dDescription);
        adp=(ImageView)findViewById(R.id.aProdp);

        user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();

        callAll();

    }

    public void select(View view) {

        if (ContextCompat.checkSelfPermission(AddService.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            CropImage.startPickImageActivity(AddService.this);
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
                            ActivityCompat.requestPermissions(AddService.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);
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
                adp.setImageURI(uri);
                Toast.makeText(this, "Image seleted", Toast.LENGTH_SHORT).show();

                File actualImage=new File(uri.getPath());

                try {
                    Bitmap compressedImage = new Compressor(AddService.this)
                            .setMaxWidth(100)
                            .setMaxHeight(100)
                            .setQuality(60)
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
                .getReference().child("Service Profile Image").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Profile Uploading...");
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
                Toast.makeText(AddService.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }


    public void uploadService(){

        ServiceData serviceData;
        serviceData = new ServiceData(
                mName.getText().toString().trim().toUpperCase(),
                mService.getText().toString().trim().toUpperCase(),
                mLocation.getText().toString().trim().toUpperCase(),
                mPhone.getText().toString().trim(),
                mExprience.getText().toString().trim(),
                mAge.getText().toString().trim(),
                gender,
                mAddress.getText().toString().trim(),
                mDescription.getText().toString().trim(),
                uid,
                imageUrl
        );


         myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

         timekey=uid+myCurrentDateTime;


        String Loc=mLocation.getText().toString().trim().toUpperCase();

        FirebaseDatabase.getInstance().getReference("Services").child(Loc)
                .child(timekey).setValue(serviceData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    newSmile();
                    Toast.makeText(AddService.this, "Your Profile Uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddService.this, UZMainActivity.class));
                    finish();

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddService.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addprofile(View view) {

        if (!mLocation.getText().toString().equals("")) {

            if (uri == null) {
                if (mName.getText().toString().equals("") || mService.getText().toString().equals("") || mLocation.getText().toString().equals("") || mPhone.getText().toString().equals("")
                        || mExprience.getText().toString().equals("") || mAge.getText().toString().equals("") || mAddress.getText().toString().equals("") ||
                        mDescription.getText().toString().equals("")) {
                    Toast.makeText(AddService.this, "Enter All the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    bottomSheetDialog = new BottomSheetDialog(AddService.this);
                    bottomSheetDialog.setContentView(R.layout.dialog_layout);
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                    Button yes = bottomSheetDialog.findViewById(R.id.yes);
                    Button No = bottomSheetDialog.findViewById(R.id.no);
                    TextView t = bottomSheetDialog.findViewById(R.id.confirm);
                    t.setText("Are You Sure Want To Add New Profile ?");

                    ImageView ynicon = bottomSheetDialog.findViewById(R.id.ynicon);
                    ynicon.setImageResource(R.drawable.ic_person_add);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            bottomSheetDialog.dismiss();
                            imageUrl = "eee";
                            uploadService();

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
            } else {

                if (mName.getText().toString().equals("") || mService.getText().toString().equals("") || mLocation.getText().toString().equals("") || mPhone.getText().toString().equals("")
                        || mExprience.getText().toString().equals("") || mAge.getText().toString().equals("") || mAddress.getText().toString().equals("") ||
                        mDescription.getText().toString().equals("")) {
                    Toast.makeText(AddService.this, "Enter All the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    bottomSheetDialog = new BottomSheetDialog(AddService.this);
                    bottomSheetDialog.setContentView(R.layout.dialog_layout);
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                    Button yes = bottomSheetDialog.findViewById(R.id.yes);
                    Button No = bottomSheetDialog.findViewById(R.id.no);
                    TextView t = bottomSheetDialog.findViewById(R.id.confirm);
                    t.setText("Are You Sure Want To Add New Profile ?");

                    ImageView ynicon = bottomSheetDialog.findViewById(R.id.ynicon);
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
        else if (mName.getText().toString().equals("") || mService.getText().toString().equals("") || mLocation.getText().toString().equals("") || mPhone.getText().toString().equals("")
                || mExprience.getText().toString().equals("") || mAge.getText().toString().equals("") || mAddress.getText().toString().equals("") ||
                mDescription.getText().toString().equals("")) {
            Toast.makeText(AddService.this, "Enter All the Fields", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Location not Accepted", Toast.LENGTH_SHORT).show();
        }

    }



    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    gender="Male";
                    break;
            case R.id.radio_ninjas:
                if (checked)
                    gender="Female";
                    break;
        }
    }




    public String getJson()
    {
        String json=null;
        try
        {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    void obj_list()
    {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("city");
                String service=object.getString("service");
                listCity.add(city);
                listService.add(service);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void callAll()
    {
        obj_list();
        addCity();
        addService();
    }

    void addCity()
    {
        mLocation=(AutoCompleteTextView)findViewById(R.id.dLocation);
        adapterSetting(listCity);
    }

    void adapterSettingService(ArrayList arrayList)
    {
        ArrayAdapter<String> adapterser=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        mService.setAdapter(adapterser);
        hideKeyBoard();
    }

    void addService()
    {

        adapterSettingService(new ArrayList(listService));

    }

    void adapterSetting(ArrayList arrayList)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        mLocation.setAdapter(adapter);
        hideKeyBoard();
    }
    public void hideKeyBoard()
    {
        mLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }


    public  void newSmile(){
        FirebaseDatabase.getInstance().getReference("CountSmile")
                .child(timekey).setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(AddService.this, "Thanks, Share others ", Toast.LENGTH_SHORT).show();

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddService.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
