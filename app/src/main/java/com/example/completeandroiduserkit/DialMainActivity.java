package com.example.completeandroiduserkit;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DialMainActivity extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT =1;
    EditText text;
    TextView n;
    ImageButton btncall,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_main);
        setTitle("Dialer");

        n=(TextView)findViewById(R.id.n);
        text=(EditText)findViewById(R.id.no);
        btncall=findViewById(R.id.call);
        contact=findViewById(R.id.contact);

        contact.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult (in, RESULT_PICK_CONTACT);
            }
        });


        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentcall =new Intent(Intent.ACTION_CALL);
                String number =text.getText().toString();

                if(number.trim().equals("")){
                    Toast.makeText(DialMainActivity.this, "Please Enter Your Number", Toast.LENGTH_SHORT).show();
                }

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(DialMainActivity.this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
                    requestpermission();
                }
                else {
                    intentcall.setData(Uri.parse("tel:"+number));
                    startActivity(intentcall);
                }
            }
        });


    }

    private void requestpermission(){
        ActivityCompat.requestPermissions(DialMainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString (phoneIndex);
            String Name= cursor.getString (name);

            text.setText (phoneNo);
            n.setText(Name);


        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void btn1(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"1");
    }

    public void btn2(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"2");
    }

    public void btn3(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"3");
    }

    public void btn4(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"4");
    }
    public void btn5(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"5");
    }
    public void btn6(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"6");
    }
    public void btn7(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"7");
    }

    public void btn8(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"8");
    }

    public void btn9(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"9");
    }

    public void btns(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"*");
    }

    public void btn0(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"0");
    }

    public void btna(View view) {
        String a=text.getText().toString().trim();
        text.setText(a+"#");
    }

    public void btnb(View view) {
        int length = text.getText().length();
        if (length > 0) {
            text.getText().delete(length - 1, length);
        }
    }
}
