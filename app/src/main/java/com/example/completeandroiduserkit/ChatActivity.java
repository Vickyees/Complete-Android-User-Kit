package com.example.completeandroiduserkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;
    private TextView userName,userLastSeen;
    private CircleImageView userImage;
    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ImageButton SendMessageButton,SendFilesButton;
    private EditText MessageInputText;

        private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;

    private String saveCurrentTime, saveCurrentDate;
    private String checker="", myUrl="";
    private Uri fileUri;
    private StorageTask uploadTask;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth= FirebaseAuth.getInstance();
        messageSenderID=mAuth.getCurrentUser().getUid();
        rootRef= FirebaseDatabase.getInstance().getReference();

        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName=getIntent().getExtras().get("visit_user_name").toString();
        messageReceiverImage=getIntent().getExtras().get("visit_user_image").toString();


        InitializeControllers();

        userName.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.profile_image).into(userImage);

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        DisplayLastSeen();

        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]
                        {
                                "Images",
                                "PDF Files",
                                "MS Word Files"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select File");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(which==0)
                        {
                            checker="image";

                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Select Image file"), 438);
                        }
                        if(which==1)
                        {
                            checker="pdf";

                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent, "Select PDF file"), 438);

                        }
                        if(which==2)
                        {
                            checker="docx";

                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword*");
                            startActivityForResult(intent.createChooser(intent, "Select Word file"), 438);
                        }
                    }
                });
                builder.show();
            }
        });
        rootRef.child("Dove Chat").child("Messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Messages messages=dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();

                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
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

    private void InitializeControllers() {


        ChatToolBar=(Toolbar)findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatToolBar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView= layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView );

        userImage=findViewById(R.id.custom_profile_IMAGE);
        userName=findViewById(R.id.custom_profile_name);
        userLastSeen=findViewById(R.id.custom_user_last_seen);

        SendMessageButton=findViewById(R.id.send_message_btn);
        MessageInputText=findViewById(R.id.input_message);

        messageAdapter=new MessageAdapter(messagesList);
        userMessagesList=(RecyclerView)findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager=new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
        SendFilesButton=findViewById(R.id.send_files_btn);

        loadingBar = new ProgressDialog(this);

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/YY");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        saveCurrentTime= currentTime.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==438 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            loadingBar.setTitle("Sending File");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileUri= data.getData();

            if(!checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Document Files");

                final String messageSenderRef = "Messages/" + messageSenderID +"/" +messageReceiverID;
                final String messageReceiverRef = "Messages/" + messageReceiverID +"/" + messageSenderID;

                DatabaseReference userMessageKeyRef=rootRef.child("Dove Chat").child("Messages")
                        .child(messageSenderID)
                        .child(messageReceiverID)
                        .push();

                final String messagePushID=userMessageKeyRef.getKey();

                final StorageReference filePath= storageReference.child(messagePushID + "." + checker);

                filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                Map messageImageBody = new HashMap();
                                messageImageBody.put("message",downloadUrl);
                                messageImageBody.put("name",fileUri.getLastPathSegment());
                                messageImageBody.put("type",checker);
                                messageImageBody.put("from",messageSenderID);
                                messageImageBody.put("to", messageReceiverID);
                                messageImageBody.put("messageId", messagePushID);
                                messageImageBody.put("time", saveCurrentTime);
                                messageImageBody.put("date", saveCurrentDate);


                                Map messageBodyDetail = new HashMap();
                                messageBodyDetail.put(messageSenderRef+ "/" + messagePushID, messageImageBody);
                                messageBodyDetail.put(messageReceiverRef+ "/" +messagePushID, messageImageBody);

                                rootRef.child("Dove Chat").updateChildren(messageBodyDetail);
                                loadingBar.dismiss();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingBar.dismiss();
                                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double p = (100.0* taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        loadingBar.setMessage((int) p + " % sending...");
                    }
                });
            }
            else if(checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messageSenderRef = "Messages/" + messageSenderID +"/" +messageReceiverID;
                final String messageReceiverRef = "Messages/" + messageReceiverID +"/" + messageSenderID;

                DatabaseReference userMessageKeyRef=rootRef.child("Dove Chat").child("Messages")
                        .child(messageSenderID)
                        .child(messageReceiverID)
                        .push();

                final String messagePushID=userMessageKeyRef.getKey();

                final StorageReference filePath= storageReference.child(messagePushID + "." + "jpg" );

                uploadTask =filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl = task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageTextBody= new HashMap();
                            messageTextBody.put("message",myUrl);
                            messageTextBody.put("name",fileUri.getLastPathSegment());
                            messageTextBody.put("type",checker);
                            messageTextBody.put("from",messageSenderID);
                            messageTextBody.put("to",messageReceiverID);
                            messageTextBody.put("messageId",messagePushID);
                            messageTextBody.put("time",saveCurrentTime);
                            messageTextBody.put("date",saveCurrentDate);


                            Map messageBodyDetails= new HashMap();
                            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

                            rootRef.child("Dove Chat").updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        loadingBar.dismiss();

                                        Toast.makeText(ChatActivity.this, "Message not Sent", Toast.LENGTH_SHORT).show();
                                    }
                                    MessageInputText.setText("");
                                }
                            });

                        }
                    }
                });

            }
            else
            {
                loadingBar.dismiss();
                Toast.makeText(ChatActivity.this, "Select the Image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DisplayLastSeen()
    {
        rootRef.child("Dove Chat").child("Users").child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("userState").hasChild("state"))
                        {
                            String state=dataSnapshot.child("userState").child("state").getValue().toString();
                            String date=dataSnapshot.child("userState").child("date").getValue().toString();
                            String time=dataSnapshot.child("userState").child("time").getValue().toString();

                            if(state.equals("online"))
                            {
                                userLastSeen.setText("online");

                            }
                            else if(state.equals("offline"))
                            {
                                userLastSeen.setText("Last seen: " + date +" " +time);
                            }

                        }
                        else
                        {
                           userLastSeen.setText("offline");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    @Override
    protected void onStart()
    {

        super.onStart();

    }



    private void SendMessage()
    {
        String messageText=MessageInputText.getText().toString().trim();
        if(TextUtils.isEmpty(messageText)){
        }
        else{
            String messageSenderRef = "Messages/" + messageSenderID +"/" +messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID +"/" + messageSenderID;

            DatabaseReference userMessageKeyRef=rootRef.child("Dove Chat").child("Messages")
                    .child(messageSenderID)
                    .child(messageReceiverID)
                    .push();

            String messagePushID=userMessageKeyRef.getKey();

            Map messageTextBody= new HashMap();
            messageTextBody.put("message",messageText);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderID);
            messageTextBody.put("to",messageReceiverID);
            messageTextBody.put("messageId",messagePushID);
            messageTextBody.put("time",saveCurrentTime);
            messageTextBody.put("date",saveCurrentDate);


            Map messageBodyDetails= new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            rootRef.child("Dove Chat").updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        MessageInputText.setText("");
                    }
                    else{
                        Toast.makeText(ChatActivity.this, "Message not Sent", Toast.LENGTH_SHORT).show();
                        MessageInputText.setText("");

                    }
                    MessageInputText.setText("");
                }
            });


        }
    }
}
