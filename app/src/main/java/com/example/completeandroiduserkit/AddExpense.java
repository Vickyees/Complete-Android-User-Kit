package com.example.completeandroiduserkit;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class AddExpense extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private EditText to_name_id,to_desc_id,to_amount_id;
    private String expensePushId;
    private Button savebtn,cancelbtn;
    private String name,desc,amount,currentUserId;

    private DatabaseReference userExpenseKeyRef;


    private TextView tvtexpense;


    private String retrievedTotalExpense="0",retrievedTotalBalance="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initializeFields();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=to_name_id.getText().toString().trim();
                desc=to_desc_id.getText().toString().trim();
                amount=to_amount_id.getText().toString().trim();




                if(TextUtils.isEmpty(name)){
                    Toast.makeText(AddExpense.this, "Please Enter the name", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(desc)){
                    Toast.makeText(AddExpense.this, "Please enter any description", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(amount)){
                    Toast.makeText(AddExpense.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    currentUserId=mAuth.getCurrentUser().getUid();
                    userExpenseKeyRef=rootRef.child("Budget").child("Users")
                            .child(currentUserId)
                            .child("budget")
                            .push();
                    expensePushId=userExpenseKeyRef.getKey();

                    String saveCurrentTime, saveCurrentDate;

                    Calendar calendar=Calendar.getInstance();

                    SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/YY");
                    saveCurrentDate= currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
                    saveCurrentTime= currentTime.format(calendar.getTime());


                    HashMap<String, Object> expenseMap = new HashMap<>();
                    expenseMap.put("userid", currentUserId);
                    expenseMap.put("name",name);
                    expenseMap.put("desc", desc);
                    expenseMap.put("amount", amount);
                    expenseMap.put("date",saveCurrentDate);
                    expenseMap.put("time",saveCurrentTime);
                    expenseMap.put("type","expense");

                    rootRef.child("Budget").child("Users").child(currentUserId).child("budget").child(expensePushId).updateChildren(expenseMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                    }
                                    else{
                                        String message=task.getException().toString();
                                        Toast.makeText(AddExpense.this, "Error:"+message, Toast.LENGTH_SHORT).show();
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

                    HashMap<String, Object> tExpenseMap = new HashMap<>();
                    Double currentExpense=Double.parseDouble(amount);
                    Double texpense=Double.parseDouble(retrievedTotalExpense)+currentExpense;
                    Double tbalance=Double.parseDouble(retrievedTotalBalance)-currentExpense;
                    tExpenseMap.put("tbalance",tbalance);
                    tExpenseMap.put("texpense",texpense);

                    rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget").updateChildren(tExpenseMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        sendUserToMainActivity();
                                        Toast.makeText(AddExpense.this, "Expense Added", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        String message=task.getException().toString();
                                        Toast.makeText(AddExpense.this, "Error:"+message, Toast.LENGTH_SHORT).show();
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
        to_name_id=findViewById(R.id.to_name_id);
        to_desc_id=findViewById(R.id.to_desc_id);
        to_amount_id=findViewById(R.id.to_amount_id);
        savebtn=findViewById(R.id.savebtn);
        cancelbtn=findViewById(R.id.cancelbtn);
        tvtexpense=findViewById(R.id.tvtexpense);


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("texpense"))
                {
                    retrievedTotalExpense=dataSnapshot.child("texpense").getValue().toString();
                    tvtexpense.setText(retrievedTotalExpense);


                }
                else
                {
                    tvtexpense.setText("0");
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToMainActivity() {
        startActivity(new Intent(AddExpense.this,ETMainActivty.class));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendUserToMainActivity();
    }



}