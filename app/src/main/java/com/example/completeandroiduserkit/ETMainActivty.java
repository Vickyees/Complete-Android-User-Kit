package com.example.completeandroiduserkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ETMainActivty extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private FloatingActionButton add_income_btn,add_expense_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private RecyclerView recyclerView;

    private List<ListItem> listData;
    private ItemAdapter adapter;

    private TextView tincomeid,texpenseid,tbalanceid;

    String currentUserId;

    private DatabaseReference budgetRef;
    private View budgetView;
    private String date,retrievedDate,newDate;
    private View view;
    private AlertDialog dialog;
    int incomeFlag,expenseFlag,ascendingFlag,descendingFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_t_main);
        setTitle("Budget");

        initializeFields();




        add_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToIncomeActivity();
            }
        });
        add_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToExpenseActivity();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.filterbtn){
            showFilterDialog();

        }
        return true;
    }

    private void showFilterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ETMainActivty.this);
        view= getLayoutInflater().inflate(R.layout.set_filter, null);


        //check box
        final RadioButton nameCheck=view.findViewById(R.id.nameCheck);
        final RadioButton dateCheck=view.findViewById(R.id.dateCheck);
        final RadioButton typeCheck=view.findViewById(R.id.typeCheck);
        final RadioButton amountCheck=view.findViewById(R.id.amountCheck);
        final RadioButton showAll=view.findViewById(R.id.showAll);

        //RadioGroup - gone at default
        final RadioGroup typeRG=view.findViewById(R.id.typeRG);

        //RadioButton
        final RadioButton incomeid=view.findViewById(R.id.incomeid);
        final RadioButton expenseid=view.findViewById(R.id.expenseid);


        //LinearLayout - gone at default
        final LinearLayout dateLayout=view.findViewById(R.id.dateLayout);

        final LinearLayout amountLayout=view.findViewById(R.id.amountLayout);

        //EditText - gone at default
        final EditText amountStartid=view.findViewById(R.id.amountStartid);
        final EditText amountEndid=view.findViewById(R.id.amountEndid);
        final EditText nameInput=view.findViewById(R.id.nameInput);


        //Button
        final Button cancelbtn=view.findViewById(R.id.cancelbtn);
        final Button applybtn=view.findViewById(R.id.applybtn);
        //gone at default
        final Button startDatebtn=view.findViewById(R.id.startDatebtn);
        final Button endDatebtn=view.findViewById(R.id.endDatebtn);

        final TextView startDate=view.findViewById(R.id.startDate);
        final TextView endDate=view.findViewById(R.id.endDate);

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showAll.isChecked())
                {
                    nameCheck.setChecked(false);
                    nameInput.setVisibility(View.GONE);
                    dateCheck.setChecked(false);
                    dateLayout.setVisibility(View.GONE);
                    typeCheck.setChecked(false);
                    typeRG.setVisibility(View.GONE);
                    amountCheck.setChecked(false);
                    amountLayout.setVisibility(View.GONE);
                }
                else{

                }
            }
        });

        nameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameCheck.isChecked()){
                    showAll.setChecked(false);
                    nameInput.setVisibility(View.VISIBLE);
                    dateCheck.setChecked(false);
                    dateLayout.setVisibility(View.GONE);
                    typeCheck.setChecked(false);
                    typeRG.setVisibility(View.GONE);
                    amountCheck.setChecked(false);
                    amountLayout.setVisibility(View.GONE);

                }
                else{
                    nameInput.setVisibility(View.GONE);
                    if(!nameCheck.isChecked() && !typeCheck.isChecked() && !amountCheck.isChecked() && !dateCheck.isChecked())
                    {
                        showAll.setChecked(true);
                    }
                }

            }
        });
        dateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateCheck.isChecked())
                {
                    showAll.setChecked(false);
                    dateLayout.setVisibility(View.VISIBLE);
                    nameCheck.setChecked(false);
                    nameInput.setVisibility(View.GONE);
                    typeCheck.setChecked(false);
                    typeRG.setVisibility(View.GONE);
                    amountCheck.setChecked(false);
                    amountLayout.setVisibility(View.GONE);

                    startDatebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startDate.setText(setDate());
                            showDatePickerDialog();
                        }
                    });
                    startDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startDate.setText(setDate());
                        }
                    });

                    endDatebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            endDate.setText(setDate());
                            showDatePickerDialog();

                        }
                    });
                    endDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            endDate.setText(setDate());
                        }
                    });
                }
                else{
                    dateLayout.setVisibility(View.GONE);
                    if(!nameCheck.isChecked() && !typeCheck.isChecked() && !amountCheck.isChecked() && !dateCheck.isChecked())
                    {
                        showAll.setChecked(true);
                    }
                }
            }
        });

        typeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeCheck.isChecked()){
                    showAll.setChecked(false);
                    typeRG.setVisibility(View.VISIBLE);
                    nameCheck.setChecked(false);
                    nameInput.setVisibility(View.GONE);
                    dateCheck.setChecked(false);
                    dateLayout.setVisibility(View.GONE);
                    amountCheck.setChecked(false);
                    amountLayout.setVisibility(View.GONE);

                    incomeid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    expenseid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
                else{
                    typeRG.setVisibility(View.GONE);
                    if(!nameCheck.isChecked() && !typeCheck.isChecked() && !amountCheck.isChecked() && !dateCheck.isChecked())
                    {
                        showAll.setChecked(true);
                    }
                }
            }
        });
        amountCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountCheck.isChecked()){
                    showAll.setChecked(false);
                    amountLayout.setVisibility(View.VISIBLE);
                    nameCheck.setVisibility(View.VISIBLE);
                    nameCheck.setChecked(false);
                    nameInput.setVisibility(View.GONE);
                    dateCheck.setChecked(false);
                    dateLayout.setVisibility(View.GONE);
                    typeCheck.setChecked(false);
                    typeRG.setVisibility(View.GONE);

                }
                else{
                    amountLayout.setVisibility(View.GONE);
                    if(!nameCheck.isChecked() && !typeCheck.isChecked() && !amountCheck.isChecked() && !dateCheck.isChecked())
                    {
                        showAll.setChecked(true);
                    }
                }
            }
        });
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showAll.isChecked())
                {
                    listData.clear();

                    getDataFirebase();
                }
                if(nameCheck.isChecked())
                {
                    String name=nameInput.getText().toString().trim();
                    if(name.isEmpty())
                    { }
                    else{

                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("name")
                                .equalTo(name);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                }
                else if(amountCheck.isChecked())
                {
                    Double amountStart,amountEnd;
                    if(amountStartid.getText().toString().isEmpty() && amountEndid.getText().toString().isEmpty())
                    { }
                    else if(amountStartid.getText().toString().isEmpty())
                    {

                        amountEnd=Double.parseDouble(amountEndid.getText().toString().trim());
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("amount")
                                .endAt(amountEnd);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else if (amountEndid.getText().toString().isEmpty())
                    {
                        amountStart=Double.parseDouble(amountStartid.getText().toString().trim());
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("amount")
                                .startAt(amountStart);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else
                    {
                        amountStart=Double.parseDouble(amountStartid.getText().toString().trim());
                        amountEnd=Double.parseDouble(amountEndid.getText().toString().trim());
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("amount")
                                .startAt(amountStart)
                                .endAt(amountEnd);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }


                }
                else if(typeCheck.isChecked())
                {
                    if(incomeid.isChecked())
                    {
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("type")
                                .equalTo("income");
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else if(expenseid.isChecked())
                    {
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("type")
                                .equalTo("expense");
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else
                    { }
                }
                else if(dateCheck.isChecked())
                {
                    String d1=startDate.getText().toString();
                    String d2=endDate.getText().toString().trim();
                    if(d1.isEmpty() && d2.isEmpty())
                    { }
                    else if(d1.isEmpty())
                    {
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("date")
                                .endAt(d2);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else if(d2.isEmpty())
                    {
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("date")
                                .startAt(d1);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else{
                        currentUserId=mAuth.getCurrentUser().getUid();
                        listData=new ArrayList<>();
                        adapter=new ItemAdapter(listData);
                        recyclerView.setAdapter(adapter);
                        listData.clear();
                        Query query=budgetRef
                                .orderByChild("date")
                                .startAt(d1)
                                .endAt(d2);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }

                }

                dialog.dismiss();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog=builder.create();
        dialog.show();
    }

    private void sendUserToIncomeActivity() {
        startActivity(new Intent(ETMainActivty.this,AddIncome.class));
    }
    private void sendUserToExpenseActivity()
    {
        startActivity(new Intent(ETMainActivty.this,AddExpense.class));

    }

    private void initializeFields() {
        add_expense_btn=findViewById(R.id.add_expense_btn);
        add_income_btn=findViewById(R.id.add_income_btn);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();
        budgetRef= FirebaseDatabase.getInstance().getReference()
                .child("Budget").child("Users")
                .child(currentUserId)
                .child("budget");

        recyclerView=findViewById(R.id.budgetViewid);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(LM);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        //for reverse order START
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        //END


        listData=new ArrayList<>();

        adapter=new ItemAdapter(listData);


        tincomeid=findViewById(R.id.tincomeid);
        texpenseid=findViewById(R.id.texpenseid);
        tbalanceid=findViewById(R.id.tbalanceid);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            SendUserToLoginActivity();
        } else {

            updateUserStatus("online");

            VerifyUserExistanse();
            listData.clear();
            getDataFirebase();

            retrieveTotalBalance();
            retrieveTotalIncome();
            retrieveTotalExpense();


        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser != null)
        {
            updateUserStatus("offline");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser != null)
        {
            updateUserStatus("offline");
        }
    }
    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + month + "/" + year;

        getDate(date);

    }
    private String getDate(String retrievedDate)
    {
        newDate=retrievedDate;
        return newDate;

    }
    private String setDate()
    {
        return newDate;
    }

    private void VerifyUserExistanse() {
        String currentUserID=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())){
                }
                else{
                    //SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void SendUserToLoginActivity() {
        Intent loginIntent= new Intent(ETMainActivty.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void getDataFirebase()
    {

        String currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserId).child("budget").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                ListItem data=dataSnapshot.getValue(ListItem.class);

                //Now adding this to array
                listData.add(data);

                //Now List into Adapter/RecyclerView
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/YY");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        saveCurrentTime= currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap =new HashMap<>();
        onlineStateMap.put("time",saveCurrentTime);
        onlineStateMap.put("date",saveCurrentDate);
        onlineStateMap.put("state",state);

        String currentUserID=mAuth.getCurrentUser().getUid();

        rootRef.child("Budget").child("Users").child(currentUserID).child("userState")
                .updateChildren(onlineStateMap);


    }




    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>
    {
        List<ListItem> ListArray;
        private ItemAdapter(List<ListItem> List)
        {
            this.ListArray=List;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ListItem data=ListArray.get(position);

            String budgetType=data.getType();

            String amount;


            holder.nameid.setText(data.getName());
            holder.descid.setText(data.getDesc());
            holder.timeid.setText(data.getTime());
            holder.dateid.setText(data.getDate());
            holder.typeid.setText(data.getType());



            if(budgetType.equals("income")){

                holder.amountid.setTextColor(Color.GREEN);
                amount=("+") + (data.getAmount());
                holder.amountid.setText(amount);
                holder.fromtoid.setText("From:");

            }
            else if(budgetType.equals("expense"))
            {

                holder.amountid.setTextColor(Color.RED);
                amount=("-") + (data.getAmount());
                holder.amountid.setText(amount);
                holder.fromtoid.setText("To:");

            }

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

            return new MyViewHolder(view);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView nameid,descid,amountid,dateid,timeid,typeid,fromtoid;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                nameid=itemView.findViewById(R.id.nameid);
                descid=itemView.findViewById(R.id.descid);
                amountid=itemView.findViewById(R.id.amountid);
                dateid=itemView.findViewById(R.id.dateid);
                timeid=itemView.findViewById(R.id.timeid);
                typeid=itemView.findViewById(R.id.typeid);
                fromtoid=itemView.findViewById(R.id.fromtoid);


            }
        }

        @Override
        public int getItemCount() {
            return ListArray.size();
        }
    }

    private void retrieveTotalIncome(){
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("tincome"))
                        {
                            tincomeid.setText(dataSnapshot.child("tincome").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void retrieveTotalExpense(){
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("texpense"))
                        {
                            texpenseid.setText(dataSnapshot.child("texpense").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void retrieveTotalBalance(){
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef.child("Budget").child("Users").child(currentUserId).child("totalBudget")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.hasChild("tbalance"))
                        {
                            tbalanceid.setText(dataSnapshot.child("tbalance").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    ValueEventListener valueEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                listData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ListItem listItem=snapshot.getValue(ListItem.class);
                    listData.add(listItem);
                }
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void sendUserToMainActivity() {
        startActivity(new Intent(ETMainActivty.this,MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendUserToMainActivity();
    }
}
