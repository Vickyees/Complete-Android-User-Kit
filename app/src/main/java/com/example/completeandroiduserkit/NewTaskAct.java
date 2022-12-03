package com.example.completeandroiduserkit;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.Random;

public class NewTaskAct extends AppCompatActivity {

    private static EditText dateText;
    TextView titlepage, addtitle, adddesc, adddate;
    EditText titledoes, descdoes, datedoes;
    Button btnSaveTask, btnCancel;
    DatabaseReference reference;
    Integer doesNum = new Random().nextInt();
    String keydoes = Integer.toString(doesNum);

    ImageView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        dateText = (EditText) findViewById(R.id.datedoes);

        titlepage = findViewById(R.id.titlepage);

        addtitle = findViewById(R.id.addtitle);
        adddesc = findViewById(R.id.adddesc);
        adddate = findViewById(R.id.adddate);

        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = (EditText)findViewById(R.id.datedoes);

        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert data to databaseinpu
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").
                        child("Does" + doesNum);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (titledoes.getText().toString().equals("")||descdoes.getText().toString().equals("")||datedoes.getText().toString().equals("")){
                            Toast.makeText(NewTaskAct.this, "Enter all the field", Toast.LENGTH_SHORT).show();
                        }

                        else {

                            dataSnapshot.getRef().child("titledoes").setValue(titledoes.getText().toString());
                            dataSnapshot.getRef().child("descdoes").setValue(descdoes.getText().toString());
                            dataSnapshot.getRef().child("datedoes").setValue(datedoes.getText().toString());
                            dataSnapshot.getRef().child("keydoes").setValue(keydoes);

                            Intent a = new Intent(NewTaskAct.this, DOMainActivity.class);
                            startActivity(a);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        titlepage.setTypeface(MMedium);

        addtitle.setTypeface(MLight);
        titledoes.setTypeface(MMedium);

        adddesc.setTypeface(MLight);
        descdoes.setTypeface(MMedium);

        adddate.setTypeface(MLight);
        datedoes.setTypeface(MMedium);

        btnSaveTask.setTypeface(MMedium);
        btnCancel.setTypeface(MLight);

        a=findViewById(R.id.datebtn);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = new MyDateFragment();
                fragment.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });



    }

    public static void populateSetDateText(int year, int month, int day) {
        dateText.setText(day + "-" + month + "-" + year);

    }

    public void cancel(View view) {
        finish();
        Intent a = new Intent(NewTaskAct.this, DOMainActivity.class);
        startActivity(a);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent a = new Intent(NewTaskAct.this, DOMainActivity.class);
        startActivity(a);
    }
}
