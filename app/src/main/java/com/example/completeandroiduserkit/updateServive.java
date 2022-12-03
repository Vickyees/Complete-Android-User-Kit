package com.example.completeandroiduserkit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class updateServive extends AppCompatActivity {


    FirebaseUser user1;
    String uid,key,oldimageurl;
    Uri uri;
    ImageView udp;
    String imageUrl,aloc;
    EditText uName,uExprience,uAge,uPhone,uAddress,uDescription;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    AutoCompleteTextView uLocation,uService;

    ArrayList<String> listCity=new ArrayList<String>();

    ArrayList<String> listService=new ArrayList<String>();



    RadioButton rm,rf;
    byte[] final_image;
    public static String gender="";
    private BottomSheetDialog bottomSheetDialog;
    private AdView MadView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_servive);
        setTitle("Update Profile");

        MobileAds.initialize(this,"ca-app-pub-7762392222724955~2438737873") ;
        MadView = findViewById(R.id.upadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        MadView.loadAd(adRequest);


        uName=(EditText)findViewById(R.id.uName);
        uService=(AutoCompleteTextView) findViewById(R.id.uService);
        uLocation=(AutoCompleteTextView)findViewById(R.id.uLocation);
        uPhone=(EditText)findViewById(R.id.uPhone);
        uExprience=(EditText)findViewById(R.id.uExprience);
        uAge=(EditText)findViewById(R.id.uAge);
        uAddress=(EditText)findViewById(R.id.uAddress);
        uDescription=(EditText)findViewById(R.id.uDescription);
        udp=(ImageView)findViewById(R.id.uProdp);

        rm=(RadioButton)findViewById(R.id.radio_male);
        rf=(RadioButton)findViewById(R.id.radio_female);


        user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();

        Bundle mbundle=getIntent().getExtras();

        if (mbundle!=null){

            key=mbundle.getString("Key");
           oldimageurl=mbundle.getString("Image");
            uName.setText(mbundle.getString("Name"));
            uService.setText(mbundle.getString("Service"));
            uLocation.setText(mbundle.getString("Location"));
            uPhone.setText(mbundle.getString("Phone"));
            uExprience.setText(mbundle.getString("Expr"));
            uAge.setText(mbundle.getString("Age"));
            uAddress.setText(mbundle.getString("Address"));
            uDescription.setText(mbundle.getString("Desc"));
            aloc=(mbundle.getString("Location"));
            gender=(mbundle.getString("Gender"));

            if (oldimageurl.equals("eee")){
                udp.setImageResource(R.drawable.eee);
            }
            else {
                Glide.with(this)
                        .load(mbundle.getString("Image"))
                        .into(udp);
            }

            if (gender.equals("Male")){
                rm.setChecked(true);
            }
            else {
                rf.setChecked(true);
            }



        }


        callAll();
        preparedAD();

    }

    public void preparedAD(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7762392222724955/1447776759");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    public void updateselectImage(View view) {
        CropImage.startPickImageActivity(updateServive.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);

            } else {
                startCrop(imageuri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                uri = result.getUri();
                udp.setImageURI(uri);
                Toast.makeText(this, "Image seleted", Toast.LENGTH_SHORT).show();

                File actualImage=new File(uri.getPath());

                try {
                    Bitmap compressedImage = new Compressor(updateServive.this)
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


    public void updatebtn(View view) {
        if (uLocation.getText().toString().toUpperCase().equals("AMBUR")||uLocation.getText().toString().toUpperCase().equals("VANIYAMBADI")) {

            bottomSheetDialog = new BottomSheetDialog(updateServive.this);
            bottomSheetDialog.setContentView(R.layout.dialog_layout);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            Button yes = bottomSheetDialog.findViewById(R.id.yes);
            Button No = bottomSheetDialog.findViewById(R.id.no);
            TextView t = bottomSheetDialog.findViewById(R.id.confirm);
            t.setText("Are You Sure Want To UPDATE ?");

            ImageView ynicon = bottomSheetDialog.findViewById(R.id.ynicon);
            ynicon.setImageResource(R.drawable.ic_edit_24dp);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    bottomSheetDialog.dismiss();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(uLocation.getText().toString().trim().toUpperCase()).child(key);

                    if (uri == null) {
                        imageUrl = oldimageurl;
                        uploadService();

                    } else {

                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReference().child("Service Profile Image").child(uri.getLastPathSegment());


                        UploadTask uploadTask = storageReference.putBytes(final_image);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete()) ;
                                Uri urlImage = uriTask.getResult();
                                imageUrl = urlImage.toString();
                                uploadService();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(updateServive.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                startActivity(new Intent(updateServive.this, UZMainActivity.class));
                                finish();
                            }
                        });

                    } else {
                        startActivity(new Intent(updateServive.this, UZMainActivity.class));
                        finish();
                    }

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
        else {
            Toast.makeText(this, "Location not Accepted", Toast.LENGTH_SHORT).show();
        }

    }


    public void uploadService(){



        ServiceData serviceData = new ServiceData(
                uName.getText().toString().trim().toUpperCase(),
                uService.getText().toString().trim().toUpperCase(),
                uLocation.getText().toString().trim().toUpperCase(),
                uPhone.getText().toString().trim(),
                uExprience.getText().toString().trim(),
                uAge.getText().toString().trim(),
                gender,
                uAddress.getText().toString().trim(),
                uDescription.getText().toString().trim(),
                uid,
                imageUrl
        );



            databaseReference.setValue(serviceData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (!imageUrl.equals(oldimageurl)&&!oldimageurl.equals("eee")) {
                        FirebaseStorage storage= FirebaseStorage.getInstance();
                        StorageReference storageReference=storage.getReferenceFromUrl(oldimageurl);
                        storageReference.delete();
                        Toast.makeText(updateServive.this, "Old Image deleted", Toast.LENGTH_SHORT).show();
                    }

                    if (!aloc.equals(uLocation.getText().toString().trim().toUpperCase())){
                        DatabaseReference referencedel= FirebaseDatabase.getInstance().getReference("Services").child(aloc);
                        referencedel.child(key).removeValue();
                        Toast.makeText(updateServive.this, "Location updated", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(updateServive.this, "Complete Profile Updated", Toast.LENGTH_SHORT).show();


                }
            });


        }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender="Male";
                break;
            case R.id.radio_female:
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
        adapterSetting(listCity);
    }

    void adapterSettingService(ArrayList arrayList)
    {
        ArrayAdapter<String> adapterser=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        uService.setAdapter(adapterser);
        hideKeyBoard();
    }

    void addService()
    {

        adapterSettingService(new ArrayList(listService));

    }

    void adapterSetting(ArrayList arrayList)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        uLocation.setAdapter(adapter);
        hideKeyBoard();
    }
    public void hideKeyBoard()
    {
        uLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
