package com.example.completeandroiduserkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotesMainActivity extends AppCompatActivity {

    private RecyclerView mNotesList;

    private DatabaseReference fNotesDatabase;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
        setTitle("Notes");


        mNotesList=findViewById(R.id.main_notes_list);

        GridLayoutManager gridLayoutManager =new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);

        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);
        mNotesList.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(10),true));

        fAuth=FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser()!=null){
            fNotesDatabase= FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }



        //loaddata();
    }

    @Override
    public void onStart() {
        super.onStart();

    }


  /* private void loaddata(){

        Query query=fNotesDatabase.orderByChild("timestamp");

        FirebaseRecyclerAdapter<NoteModel,NotesViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<NoteModel, NotesViewHolder>(
                NoteModel.class,
                R.layout.single_note_layout,
                NotesViewHolder.class,
                query

        ) {



            @Override
            protected void populateViewHolder(final NotesViewHolder notesViewHolder, NoteModel noteModel, int i) {
                final String noteId=getRef(i).getKey();


                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {

                            String title = dataSnapshot.child("title").getValue().toString();
                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();

                            notesViewHolder.setNoteTitle(title);

                            GetTimeAgo getTimeAgo=new GetTimeAgo();
                            notesViewHolder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(timestamp),getApplicationContext()));

                            notesViewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(NotesMainActivity.this, NotesActivity.class);
                                    intent.putExtra("noteId", noteId);
                                    startActivity(intent);

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };

        mNotesList.setAdapter(firebaseRecyclerAdapter);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.notes_menu,menu);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         switch (item.getItemId()){

             case R.id.main_new_notes_button:
                 Intent intent=new Intent(NotesMainActivity.this,NotesActivity.class);
                 startActivity(intent);
                 break;
         }


         return true;
    }


    public int dpToPx(int dp){
        Resources r =getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));
    }
}
