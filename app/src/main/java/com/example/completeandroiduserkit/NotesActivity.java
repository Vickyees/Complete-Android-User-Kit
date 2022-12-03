package com.example.completeandroiduserkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {
    public Button b1;
    public EditText e1,e2;
    public TextToSpeech toSpeech;
    public androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference fNotesDatabase;
    private FirebaseAuth fauth;

    private Menu mainMenu;

    private String noteID;

    private boolean isExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Notes");

        try {
            noteID=getIntent().getStringExtra("noteId");

            if (!noteID.trim().equals("")){
                isExist=true;
            }
            else {
                isExist=false;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        b1=(Button)findViewById(R.id.addnotesbutton);
        e1=(EditText)findViewById(R.id.notestitle);
        e2=(EditText)findViewById(R.id.notesDes);
        mToolbar=(Toolbar) findViewById(R.id.toolbar2);

        toSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i!=TextToSpeech.ERROR){
                    toSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fauth=FirebaseAuth.getInstance();
        fNotesDatabase= FirebaseDatabase.getInstance().getReference().child("Notes").child(fauth.getCurrentUser().getUid());



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=e1.getText().toString().trim();
                String content=e2.getText().toString().trim();
                if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)){
                    createnotes(title,content);
                }
                else {
                    Toast.makeText(NotesActivity.this, "Fill empty Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    putData();
    }


    private void putData(){
        if(isExist) {

            fNotesDatabase.child(noteID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("title")&&dataSnapshot.hasChild("content")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();

                        e1.setText(title);
                        e2.setText(content);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toSpeech.shutdown();
    }


    public void createnotes(String title,String content) {


        if (fauth.getCurrentUser()!=null){
            if(isExist){
                Map updateMap=new HashMap();
                updateMap.put("title",title);
                updateMap.put("content",content);
                updateMap.put("timestamp",ServerValue.TIMESTAMP);
                fNotesDatabase.child(noteID).updateChildren(updateMap);

                Toast.makeText(this, "Notes updated", Toast.LENGTH_SHORT).show();
            }
            else {
                final DatabaseReference newNoteRef = fNotesDatabase.push();

                final Map noteMap = new HashMap();
                noteMap.put("title", title);
                noteMap.put("content", content);
                noteMap.put("timestamp", ServerValue.TIMESTAMP);


                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {
                                    Toast.makeText(NotesActivity.this, "Notes Created ", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(NotesActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                mainThread.start();
            }




        }
        else{
            Toast.makeText(this, "User is not signed in", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.new_notes_menu,menu);
         mainMenu=menu;
          return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId()){

             case android.R.id.home:
                 finish();
                 break;

             case R.id.new_notes_speak_btn:
                 toSpeech.speak("title   "+e1.getText().toString()+"   description   "+e2.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);

                 break;


             case R.id.new_notes_mic_btn:
                 mic();
                 break;


             case R.id.new_notes_del_btn:
                 if(isExist){
                         deleteNotes();
                 }
                 else {
                     Toast.makeText(this, "Not yet created", Toast.LENGTH_SHORT).show();
                 }


                 break;

         }
         return true;
    }

    private void mic() {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak Something");

        try {
            startActivityForResult(intent,1);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK&&null!=data){
                    ArrayList<String>result=
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String a=e2.getText().toString();

                    e2.setText(a+" "+result.get(0));
                }
                break;
        }
    }

    private void deleteNotes(){
       fNotesDatabase.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {

               if (task.isSuccessful()){
                   Toast.makeText(NotesActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                   noteID="no";
                   finish();
               }
               else {
                   Log.e("NoteActivity",task.getException().toString());
                   Toast.makeText(NotesActivity.this, "ERROR"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           }
       });
    }


}
