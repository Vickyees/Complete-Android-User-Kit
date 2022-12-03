package com.example.completeandroiduserkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UZMainActivity extends AppCompatActivity {

    RecyclerView mRecyclerview;
    List<ServiceData>myServiceList;
    ServiceData mserviceData;
    FirebaseUser user1;
    String uid;
    AutoCompleteTextView sService;
    ImageView updown;
    public  static String option="S";
    public DatabaseReference databaseReference;
    public ValueEventListener eventListener,eventListener2;
    ServiceMyAdapter myAdapter;
    LinearLayout serLayout,serbyradio;
    RelativeLayout serbylayout;
    EditText txt_search;
    ProgressBar progressBar;
    AutoCompleteTextView sLocation;
    ArrayList<String> listCity=new ArrayList<String>();
    ArrayList<String> listService=new ArrayList<String>();
    private BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_z_main);
        setTitle("Services");

        checkConnection();



        txt_search=(EditText)findViewById(R.id.serbyet);
        sLocation=(AutoCompleteTextView) findViewById(R.id.serCity);
        sService=(AutoCompleteTextView) findViewById(R.id.serProfession);
        serLayout=(LinearLayout)findViewById(R.id.serLayout);
        serbyradio=(LinearLayout)findViewById(R.id.serbyradio);
        serbylayout=(RelativeLayout)findViewById(R.id.serbylayout);
        updown=(ImageView)findViewById(R.id.updown);
        mRecyclerview=(RecyclerView)findViewById(R.id.mrecyclerview);
        progressBar=(ProgressBar)findViewById(R.id.mainprogressbar);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(UZMainActivity.this,1);
        mRecyclerview.setLayoutManager(gridLayoutManager);

        myServiceList=new ArrayList<>();

        myAdapter=new ServiceMyAdapter(UZMainActivity.this,myServiceList);
        mRecyclerview.setAdapter(myAdapter);

        user1 = FirebaseAuth.getInstance().getCurrentUser();
        uid = user1.getUid();

        callAll();


    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.servicemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /*case R.id.mlogout:
                bottomSheetDialog = new BottomSheetDialog(UZMainActivity.this);
                bottomSheetDialog.setContentView(R.layout.dialog_layout);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                Button yes = bottomSheetDialog.findViewById(R.id.yes);
                Button No = bottomSheetDialog.findViewById(R.id.no);
                TextView t = bottomSheetDialog.findViewById(R.id.confirm);
                t.setText("Are You Sure Want To LOGOUT ?");


                ImageView ynicon = bottomSheetDialog.findViewById(R.id.ynicon);
                ynicon.setImageResource(R.drawable.ic_logout);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences preferences1 = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.putString("password", "0");
                        editor1.apply();
                        SharedPreferences preferences = getSharedPreferences("com.usamastar.ultraeedz", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Loginstatus", "off");
                        editor.apply();
                        finish();
                        Intent intent = new Intent(UZMainActivity.this, Login.class);
                        startActivity(intent);
                    }
                });

                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
                break;*/
            case R.id.ads:
                startActivity(new Intent(UZMainActivity.this,WhatsNew.class));
                finish();
                break;
            case R.id.msearch:

                if (serLayout.getVisibility() == View.VISIBLE) {
                    serLayout.setVisibility(View.GONE);
                    if (!myServiceList.isEmpty()){
                        serbylayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    serbylayout.setVisibility(View.GONE);
                    serLayout.setVisibility(View.VISIBLE);
                    serbyradio.setVisibility(View.GONE);
                }

                break;

            case R.id.msave:
                myServiceList.clear();
                serLayout.setVisibility(View.GONE);
                serbylayout.setVisibility(View.GONE);
                serbyradio.setVisibility(View.GONE);
                txt_search.setText("");
                progressBar.setVisibility(View.VISIBLE);
                databaseReference = FirebaseDatabase.getInstance().getReference("save").child(uid);

                eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()){
                            myServiceList.clear();
                            Toast.makeText(UZMainActivity.this, "Not yet Saved any profile", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                        else {

                            myServiceList.clear();

                            serbylayout.setVisibility(View.VISIBLE);

                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                                mserviceData = itemSnapshot.getValue(ServiceData.class);
                                mserviceData.setKey(itemSnapshot.getKey());
                                myServiceList.add(mserviceData);

                            }
                            myAdapter.notifyDataSetChanged();
                            txt_search.setText("  ");
                            progressBar.setVisibility(View.GONE);
                            txt_search.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UZMainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });


                txt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        filter(editable.toString());

                    }

                    private void filter(String text) {
                        ArrayList<ServiceData> filterList = new ArrayList<>();

                        for (ServiceData item : myServiceList) {

                            if (option.equals("S")) {

                                if (item.getsService().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else if (option.equals("Name")) {

                                if (item.getsName().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else if (option.equals("A")) {

                                if (item.getsAddress().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else {
                                if (item.getsPhone().toUpperCase().contains(text.toUpperCase())) {

                                    filterList.add(item);

                                }
                            }


                        }

                        myAdapter.filteredList(filterList);

                    }
                });

                break;

        }

        return true;
    }


    public void addservicebtn(View view) {
        startActivity(new Intent(this,AddService.class));
    }


    public void serbtn(View view) {
        myServiceList.clear();
        serLayout.setVisibility(View.GONE);
        serbylayout.setVisibility(View.GONE);
        txt_search.setText("");

        if (!equals(sLocation.getText().toString().equals("")))
        {

            if (!sLocation.getText().toString().equals("") && (sService.getText().toString().equals(""))) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(sLocation.getText().toString().trim().toUpperCase());

                progressBar.setVisibility(View.VISIBLE);
                eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()) {
                            Toast.makeText(UZMainActivity.this, "Yet to Add Profiles to Your Location", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {

                            serbylayout.setVisibility(View.VISIBLE);

                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                                mserviceData = itemSnapshot.getValue(ServiceData.class);
                                mserviceData.setKey(itemSnapshot.getKey());
                                myServiceList.add(mserviceData);
                            }
                            myAdapter.notifyDataSetChanged();
                            txt_search.setText("  ");
                            progressBar.setVisibility(View.GONE);
                            txt_search.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UZMainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });


                txt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        filter(editable.toString());

                    }

                    private void filter(String text) {
                        ArrayList<ServiceData> filterList = new ArrayList<>();

                        for (ServiceData item : myServiceList) {

                            if (option.equals("A")) {

                                if (item.getsAddress().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else if (option.equals("Name")) {

                                if (item.getsName().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else if (option.equals("S")) {

                                if (item.getsService().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else {
                                if (item.getsPhone().toUpperCase().contains(text.toUpperCase())) {

                                    filterList.add(item);

                                }
                            }


                        }

                        myAdapter.filteredList(filterList);

                    }
                });


            }
            else if (!sLocation.getText().toString().equals("") && (!sService.getText().toString().equals(""))) {
                progressBar.setVisibility(View.VISIBLE);
                mRecyclerview.setVisibility(View.VISIBLE);

                databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(sLocation.getText().toString().trim().toUpperCase());

                Query query = databaseReference
                        .orderByChild("sService")
                        .equalTo(sService.getText().toString().trim().toUpperCase());
                query.addListenerForSingleValueEvent(valueEventListener);


                txt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        filter(editable.toString());

                    }

                    private void filter(String text) {
                        ArrayList<ServiceData> filterList = new ArrayList<>();

                        for (ServiceData item : myServiceList) {

                            if (option.equals("A")) {

                                if (item.getsAddress().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else if (option.equals("Name")) {

                                if (item.getsName().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else if (option.equals("S")) {

                                if (item.getsService().toUpperCase().contains(text.toUpperCase().trim())) {

                                    filterList.add(item);

                                }
                            } else {
                                if (item.getsPhone().toUpperCase().contains(text.toUpperCase())) {

                                    filterList.add(item);

                                }
                            }


                        }

                        myAdapter.filteredList(filterList);

                    }
                });


            }
            else {
                Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show();
            }

        }
        else if (sLocation.getText().toString().toUpperCase().equals("")){
            Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Only for Ambur and Vaniyambadi", Toast.LENGTH_SHORT).show();
        }



    }


    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (!dataSnapshot.exists()) {
                myServiceList.clear();
                Toast.makeText(UZMainActivity.this, "Yet to Add Profiles to Your Location", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else {

                serbylayout.setVisibility(View.VISIBLE);

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                    mserviceData = itemSnapshot.getValue(ServiceData.class);
                    mserviceData.setKey(itemSnapshot.getKey());
                    myServiceList.add(mserviceData);

                }

                myAdapter.notifyDataSetChanged();
                txt_search.setText("  ");
                progressBar.setVisibility(View.GONE);
                txt_search.setText("");


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(UZMainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    };


    public void arrow(View view) {
        if (serbyradio.getVisibility()==View.VISIBLE){
            serbyradio.setVisibility(View.GONE);
            updown.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

        }
        else {
            serbyradio.setVisibility(View.VISIBLE);
            updown.setImageResource(R.drawable.ic_up_24dp);
        }

    }



    public void RadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_ser:
                if (checked)
                    option="S";
                txt_search.setInputType(InputType.TYPE_CLASS_TEXT);
                txt_search.setText("");
                txt_search.setHint("Search By Service");
                    break;
            case R.id.radio_name:
                if (checked)
                    option="Name";
                txt_search.setText("");
                txt_search.setHint("Search By Name");
                txt_search.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;

            case R.id.radio_phone:
                if (checked)
                    option="Phone";
                txt_search.setText("");
                txt_search.setInputType(InputType.TYPE_CLASS_PHONE);
                txt_search.setHint("Search By Phone");
                    break;
            case R.id.radio_area:
                if (checked)
                    option="A";
                txt_search.setText("");
                txt_search.setInputType(InputType.TYPE_CLASS_TEXT);
                txt_search.setHint("Search By Area");
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UZMainActivity.this,MainActivity.class));
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

    void addService()
    {

      adapterSettingService(new ArrayList(listService));

    }


    void addCity()
    {
        adapterSetting(listCity);
    }

    void adapterSettingService(ArrayList arrayList)
    {
        ArrayAdapter<String> adapterser=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        sService.setAdapter(adapterser);
        hideKeyBoard();
    }

    void adapterSetting(ArrayList arrayList)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        sLocation.setAdapter(adapter);
        hideKeyBoard();
    }
    public void hideKeyBoard()
    {
        sLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }





}
