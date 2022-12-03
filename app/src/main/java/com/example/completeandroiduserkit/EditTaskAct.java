package com.example.completeandroiduserkit;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;

public class EditTaskAct extends AppCompatActivity {

    private static EditText aa;
    EditText titleDoes,descDoes,dateDoes;
    Button btnupdate,btnDelete;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        aa=findViewById(R.id.edatedoes);

        titleDoes=findViewById(R.id.etitledoes);
        descDoes=findViewById(R.id.edescdoes);
        dateDoes=findViewById(R.id.edatedoes);

        btnupdate=findViewById(R.id.btnupdateTask);

        btnDelete=findViewById(R.id.btnDelete);



        titleDoes.setText(getIntent().getStringExtra("titledoes"));
        descDoes.setText(getIntent().getStringExtra("descdoes"));
        dateDoes.setText(getIntent().getStringExtra("datedoes"));
        final String keykeyDoes=getIntent().getStringExtra("keydoes");

        reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").
                child("Does" + keykeyDoes);




        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (titleDoes.getText().toString().equals("")||descDoes.getText().toString().equals("")||dateDoes.getText().toString().equals("")){
                            Toast.makeText(EditTaskAct.this, "Enter all the field", Toast.LENGTH_SHORT).show();
                        }

                        else {

                            dataSnapshot.getRef().child("titledoes").setValue(titleDoes.getText().toString());
                            dataSnapshot.getRef().child("descdoes").setValue(descDoes.getText().toString());
                            dataSnapshot.getRef().child("datedoes").setValue(dateDoes.getText().toString());
                            dataSnapshot.getRef().child("keydoes").setValue(keykeyDoes);

                            Intent a = new Intent(EditTaskAct.this, DOMainActivity.class);
                            startActivity(a);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("DoesApp").
                        child("Does" + keykeyDoes);
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(EditTaskAct.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Intent a=new Intent(EditTaskAct.this,DOMainActivity.class);
                            startActivity(a);
                        }
                        else {
                            Toast.makeText(EditTaskAct.this, "Unable to Delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void editdate(View view) {

        DialogFragment fragment = new MyDateFragment2();
        fragment.show(getSupportFragmentManager(), "DATE PICKER");
    }

    public static void populateSetDateText(int year, int month, int day) {
        aa.setText(day + "-" + month + "-" + year);

    }
}
