package com.example.completeandroiduserkit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HelpMainActivity extends AppCompatActivity {

    private SmsManager smsManager = SmsManager.getDefault();
    Geocoder geocoder;
    List<Address> addresses;
    int i=0;


    String add,areas,city,coun,postal,fulladd;
    DatabaseHelper myDb;

    private FusedLocationProviderClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_main);

        setTitle("Instant Help");

        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);

        geocoder=new Geocoder(this, Locale.getDefault());


        myDb = new DatabaseHelper(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.view:


                Cursor cur = myDb.getAllData();

                if(cur.getCount() == 0){
                    showMessage("Frinds List","No Friend Found");
                    return true;
                }

                StringBuffer buffer = new StringBuffer();

                while (cur.moveToNext()){

                    buffer.append("ID: "+ cur.getString(0)+"\n");
                    buffer.append("Name: "+ cur.getString(1)+"\n");
                    buffer.append("Phone: "+ cur.getString(2)+"\n");

                }

                showMessage("Friends List",buffer.toString());
                break;

            case R.id.edit:

                Intent intent=new Intent(HelpMainActivity.this,HelpMain2Activity.class);
                startActivity(intent);
                break;

        }


        return true;
    }

    public void showMessage(String title, String Message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();

    }

    public void alert(View view) {
        i++;
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(i==1){
                    Toast.makeText(HelpMainActivity.this, "double  tap to ALERT !", Toast.LENGTH_SHORT).show();
                }
                else if (i==2){

                    requestPermission();
                    client.getLastLocation().addOnSuccessListener(HelpMainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double lat = location.getLatitude();
                                double longt = location.getLongitude();
                                String latitude = Double.toString(lat);
                                String longitude = Double.toString(longt);
                                String maplink = "http://maps.google.com/?q=" + latitude + "," + longitude;
                                String alert = "Instant Help: \n I need help! \n ";

                                try {
                                    addresses = geocoder.getFromLocation(lat, longt, 1);

                                    add = addresses.get(0).getAddressLine(0);
                                    areas = addresses.get(0).getLocality();
                                    city = addresses.get(0).getAdminArea();
                                    postal = addresses.get(0).getPostalCode();
                                    fulladd = add + areas + city + postal;


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                DatabaseHelper databaseHelper =new DatabaseHelper(HelpMainActivity.this);
                                Cursor cur = databaseHelper.getAllData();

                                if (cur.getCount() == 0) {
                                    Toast.makeText(HelpMainActivity.this, "No friend found", Toast.LENGTH_SHORT).show();
                                }

                                StringBuffer buffer = new StringBuffer();

                                while (cur.moveToNext()) {
                                    buffer.append(cur.getString(2));


                                    smsManager.sendTextMessage(String.valueOf(buffer), null, alert + "My Location is:" + maplink,
                                            null, null);
                                    smsManager.sendTextMessage(String.valueOf(buffer), null, "My Current Address :" + fulladd,
                                            null, null);
                                    buffer.delete(0, buffer.length());


                                }
                                Toast.makeText(HelpMainActivity.this, "An Alert with your Location " + maplink + fulladd +
                                        " has been sent to your friends Successfully!", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(HelpMainActivity.this, "Location not found!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                i=0;
            }
            },400);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(HelpMainActivity.this, new String[]
                        {
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CALL_PHONE},
                PackageManager.PERMISSION_GRANTED);

    }
}
