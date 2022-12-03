package com.example.completeandroiduserkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class SecurityPin extends AppCompatActivity {
    Button ok,create;
    EditText pin;
    FirebaseUser user1;
    String cuid;
    String txt_pin="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_pin);

        ok=(Button)findViewById(R.id.btnSok);
        create=(Button)findViewById(R.id.btnSCreate);
        pin=(EditText)findViewById(R.id.pin);
        user1 = FirebaseAuth.getInstance().getCurrentUser();
        cuid = user1.getUid();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reflike = database.getReference("Security");

        reflike.child(cuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   ok.setVisibility(View.VISIBLE);
                   txt_pin=dataSnapshot.getValue().toString();
                }
                else {
                   create.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecurityPin.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void ok(View view) {
        if (pin.getText().toString().equals("")){
            Toast.makeText(this, "Enter Security Pin", Toast.LENGTH_SHORT).show();
        }
        else {
            if (pin.getText().toString().equals(txt_pin)){
                startActivity(new Intent(SecurityPin.this,PerMainActivity.class));
                finish();
            }
            else {
                Toast.makeText(this, "Security Pin not Match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Create(View view) {
        if (pin.getText().toString().equals("")){
            Toast.makeText(this, "Enter Security Pin", Toast.LENGTH_SHORT).show();
        }
        else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            String myCurrentDateTime = DateFormat.getDateTimeInstance()
                    .format(Calendar.getInstance().getTime());

            DatabaseReference reflike = database.getReference("Security");


            DatabaseReference dataRef = reflike.child(cuid);

            dataRef.setValue(pin.getText().toString(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(SecurityPin.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SecurityPin.this, "Security Pin created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SecurityPin.this,PerMainActivity.class));
                        finish();
                    }
                }
            });

        }
    }
}
