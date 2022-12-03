package com.example.completeandroiduserkit;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddIncome extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private EditText from_name_id,from_desc_id,from_amount_id;
    private String incomePushId;
    private Button savebtn,cancelbtn;
    private String name,desc,amount,currentUserId;

    private DatabaseReference userIncomeKeyRef;


    private TextView tvtincome;


    private String retrievedTotalIncome="0",retrievedTotalBalance="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        initializeFields();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=from_name_id.getText().toString().trim();
                desc=from_desc_id.getText().toString().trim();
                amount=from_amount_id.getText().toString().trim();




                if(TextUtils.isEmpty(name)){
                    Toast.makeText(AddIncome.this, "Please Enter the name", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(desc)){
                    Toast.makeText(AddIncome.this, "Please enter any description", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(amount)){
                    Toast.makeText(AddIncome.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    currentUserId=mAuth.getCurrentUser().getUid();
                    userIncomeKeyRef=rootRef.child("Budget").child("Users")
                            .child(currentUserId)
                            .child("budget")
                            .push();
                    incomePushId=userIncomeKeyRef.getKey();

                    String saveCurrentTime, saveCurrentDate;

                    Calendar calendar=Calendar.getInstance();

                    SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/YY");
                    saveCurrentDate= currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
                    saveCurrentTime= currentTime.format(calendar.getTime());


                    HashMap<String, Object> incomeMap = new HashMap<>();
                    incomeMap.put("userid", currentUserId);
                    incomeMap.put("name",name);
                    incomeMap.put("desc", desc);
                    incomeMap.put("amount", amount);
                    incomeMap.put("date",saveCurrentDate);
                    incomeMap.put("time",saveCurrentTime);
                    incomeMap.put("type","income");

                    rootRef.child("Budget").child("Users").child(currentUserId).child("budget").child(incomePushId).updateChildren(incomeMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }
                                    else{
                                        String message=task.getException().toString();
                                        Toast.makeText(AddIncome.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild("tbalance"))
                            {
                                retrievedTotalBalance=dataSnapshot.child("tbalance").getValue().toString();
                            }
                            else
                            {
                                retrievedTotalBalance="0";
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    HashMap<String, Object> tIncomeMap = new HashMap<>();
                    Double currentIncome=Double.parseDouble(amount);
                    Double tincome=Double.parseDouble(retrievedTotalIncome)+currentIncome;
                    Double tbalance=Double.parseDouble(retrievedTotalBalance)+currentIncome;
                    tIncomeMap.put("tbalance",tbalance);
                    tIncomeMap.put("tincome",tincome);

                    rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget").updateChildren(tIncomeMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        sendUserToMainActivity();
                                        Toast.makeText(AddIncome.this, "Income Added", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        String message=task.getException().toString();
                                        Toast.makeText(AddIncome.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMainActivity();
            }
        });

    }

    private void initializeFields() {

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        from_name_id=findViewById(R.id.from_name_id);
        from_desc_id=findViewById(R.id.from_desc_id);
        from_amount_id=findViewById(R.id.from_amount_id);
        savebtn=findViewById(R.id.savebtn);
        cancelbtn=findViewById(R.id.cancelbtn);
        tvtincome=findViewById(R.id.tvtincome);


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("tincome"))
                    {
                        retrievedTotalIncome=dataSnapshot.child("tincome").getValue().toString();
                        tvtincome.setText(retrievedTotalIncome);


                    }
                    else
                    {
                        tvtincome.setText("0");
                    }
                    if(dataSnapshot.hasChild("tbalance"))
                    {
                        retrievedTotalBalance=dataSnapshot.child("tbalance").getValue().toString();
                    }
                    else
                    {
                        retrievedTotalBalance="0";
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToMainActivity() {
        startActivity(new Intent(AddIncome.this,ETMainActivty.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendUserToMainActivity();
    }
}
