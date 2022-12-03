package com.example.completeandroiduserkit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class HelpMain2Activity extends AppCompatActivity {


    DatabaseHelper myDb;
    public  static final int RequestPermissionCode  = 1 ;
    Intent intent ;

    Button contact;
    EditText addname,addno,upid,upname,upno,delid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_main2);
        setTitle("Instant Help");

        addname=(EditText) findViewById(R.id.addname);
        addno=(EditText) findViewById(R.id.addphn);

        upno=(EditText)findViewById(R.id.upphn);
        upname=(EditText)findViewById(R.id.upname);
        upid=(EditText)findViewById(R.id.upid);

        delid=(EditText)findViewById(R.id.delid);
        contact=(Button)findViewById(R.id.contact);

        EnableRuntimePermission();

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);

            }
        });


    }


    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(HelpMain2Activity.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(HelpMain2Activity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(HelpMain2Activity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    return;


                } else {

                    Toast.makeText(HelpMain2Activity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();


                }
                break;
        }
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                addname.setText(TempNameHolder);

                                addno.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
        }
    }


    public void delete(View view) {
        DatabaseHelper databaseHelper=new DatabaseHelper(this);

        String del=delid.getText().toString();
        if (del.equals("")){
            Toast.makeText(this, "Enter id", Toast.LENGTH_SHORT).show();
        }
        else {
            Integer deletedRow = databaseHelper.deleteData(del);

            if (deletedRow > 0) {
                Toast.makeText(HelpMain2Activity.this, "Friend Removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HelpMain2Activity.this, "Friend not Found", Toast.LENGTH_SHORT).show();
            }
            delid.setText("");
        }
    }

    public void update(View view) {

        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        String id=upid.getText().toString();
        String name=upname.getText().toString();
        String phone= upno.getText().toString();

        if (id.equals("")||name.equals("")||phone.equals("")){
            Toast.makeText(this, "Enter All the fields", Toast.LENGTH_SHORT).show();
        }

        else {

            boolean isUpdate = databaseHelper.updateData(id, name, phone);

            if (isUpdate == true) {
                Toast.makeText(HelpMain2Activity.this, "Friend Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HelpMain2Activity.this, "Friend not Updated", Toast.LENGTH_SHORT).show();
            }
            upname.setText("");
            upno.setText("");
            upid.setText("");
        }

    }

    public void add(View view) {
        String name=addname.getText().toString();
        String phone=addno.getText().toString();

        if (name.equals("")||phone.equals("")){
            Toast.makeText(HelpMain2Activity.this, "enter all the fields", Toast.LENGTH_SHORT).show();
        }

        else {

            DatabaseHelper databaseHelper=new DatabaseHelper(this);
            boolean inserted =  databaseHelper.insertData(name,phone);

            if (inserted == true) {
                Toast.makeText(HelpMain2Activity.this, "Friend Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HelpMain2Activity.this, "Friend Not Added", Toast.LENGTH_SHORT).show();
            }
            addname.setText("");
            addno.setText("");
        }
    }



}
