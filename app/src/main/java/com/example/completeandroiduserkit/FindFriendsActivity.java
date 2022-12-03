package com.example.completeandroiduserkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;


public class FindFriendsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerList;

    private DatabaseReference UsersRef;
    private EditText input;
    private Button find;
    private String name, currentUserId;
    private List<Contacts> listData;
    private FindAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        input=findViewById(R.id.etSearch);
        find=findViewById(R.id.btnFind);
        mAuth=FirebaseAuth.getInstance();
        FindFriendsRecyclerList=findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setHasFixedSize(true);
        RecyclerView.LayoutManager LM=new LinearLayoutManager(getApplicationContext());
        FindFriendsRecyclerList.setLayoutManager(LM);
        FindFriendsRecyclerList.setItemAnimator(new DefaultItemAnimator());
        FindFriendsRecyclerList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        //for reverse order START
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        FindFriendsRecyclerList.setLayoutManager(layoutManager);
        //END


        listData=new ArrayList<>();

        adapter=new FindAdapter(listData);
        name=input.getText().toString();

        UsersRef= FirebaseDatabase.getInstance().getReference().child("Dove Chat").child("Users");
        reference= FirebaseDatabase.getInstance().getReference().child("Dove Chat").child("Users");

        FindFriendsRecyclerList=findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mToolbar=findViewById(R.id.find_friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void find()
    {
        name=input.getText().toString().trim();
        if(!name.equals("")){
        currentUserId=mAuth.getCurrentUser().getUid();
        listData=new ArrayList<>();
        adapter=new FindAdapter(listData);
        FindFriendsRecyclerList.setAdapter(adapter);
        listData.clear();
        Query query=reference
                .orderByChild("name")
                .equalTo(name);
        query.addListenerForSingleValueEvent(valueEventListener);
        }
        else {
            Toast.makeText(FindFriendsActivity.this, "Enter the user name to search!", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void getDataFirebase() {
        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(UsersRef, Contacts.class)
                        .build();


        FirebaseRecyclerAdapter<Contacts,FindFriendsViewHolder> adapter=new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, final int position, @NonNull Contacts model) {
                holder.userName.setText(model.getName());
                holder.userStatus.setText(model.getStatus());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id=getRef(position).getKey();

                        Intent profileIntent=new Intent(FindFriendsActivity.this, DoveProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }


            @NonNull
            @Override
            public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                FindFriendsViewHolder viewHolder =new FindFriendsViewHolder(view);
                return viewHolder;
            }
        };

        FindFriendsRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }*/
    public class FindAdapter extends RecyclerView.Adapter<FindAdapter.FindFriendsViewHolder>
    {
        List<Contacts> ListArray;
        private FindAdapter(List<Contacts> List)
        {
            this.ListArray=List;
        }

        @Override
        public void onBindViewHolder(FindFriendsViewHolder holder, int position) {
            final Contacts data=ListArray.get(position);
            holder.userName.setText(data.getName());
            holder.userStatus.setText(data.getStatus());
            Picasso.get().load(data.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String visit_user_id=data.getUid();

                    Intent profileIntent=new Intent(FindFriendsActivity.this, DoveProfileActivity.class);
                    profileIntent.putExtra("visit_user_id", visit_user_id);
                    startActivity(profileIntent);
                }
            });

        }

        @NonNull
        @Override
        public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout,parent,false);

            return new FindFriendsViewHolder(view);
        }


        public class FindFriendsViewHolder extends RecyclerView.ViewHolder{

            TextView userName, userStatus;
            CircleImageView profileImage;

            public FindFriendsViewHolder(@NonNull View itemView) {
                super(itemView);

                userName=itemView.findViewById(R.id.user_profile_name);
                userStatus=itemView.findViewById(R.id.user_status);
                profileImage=itemView.findViewById(R.id.user_profile_image);
            }
        }

        @Override
        public int getItemCount() {
            return ListArray.size();
        }
    }
    ValueEventListener valueEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                listData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contacts listItem=snapshot.getValue(Contacts.class);
                    listData.add(listItem);
                }
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
