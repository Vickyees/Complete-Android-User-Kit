package com.example.completeandroiduserkit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    public List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String message;


    public MessageAdapter(List<Messages> userMessagesList)
    {
        this.userMessagesList=userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture,messageReceiverPicture;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText=(TextView)itemView.findViewById(R.id.sender_message_text);
            receiverMessageText=(TextView)itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage=(CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture=itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture=itemView.findViewById(R.id.message_sender_image_view);

        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout,parent,false);

        mAuth= FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position)
    {
        String messageSenderId=mAuth.getCurrentUser().getUid();
        Messages messages=userMessagesList.get(position);

        String fromUserID= messages.getFrom();
        String fromMessageType=messages.getType();

        usersRef= FirebaseDatabase.getInstance().getReference().child("Dove Chat").child("Users").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image"))
                {
                    String receiverImage= dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile_image).into(holder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.receiverMessageText.setVisibility(View.GONE);
        holder.receiverProfileImage.setVisibility(View.GONE);
        holder.senderMessageText.setVisibility(View.GONE);

        holder.messageSenderPicture.setVisibility(View.GONE);

        holder.messageReceiverPicture.setVisibility(View.GONE);

        if(fromMessageType.equals("text"))
        {
            if(fromUserID.equals(messageSenderId))
            {
                holder.senderMessageText.setVisibility(View.VISIBLE);
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.senderMessageText.setTextColor(Color.BLACK);
                message=messages.getMessage() + "\t\t" + messages.getTime().toLowerCase().trim() + " - "
                        + messages.getDate().toLowerCase().trim();

                SpannableString string = new SpannableString(message);
                string.setSpan(new RelativeSizeSpan(0.6f), string.length()-19, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                string.setSpan(new ForegroundColorSpan(Color.DKGRAY), string.length()-19, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.senderMessageText.setText(string);

                /*SpannableString spannable = SpannableString(“Text is spantastic!”);
                spannable.setSpan(
                        new ForegroundColorSpan(Color.RED),
                        8, 12,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                spannable.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        8, spannable.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );*/

            }
            else{

                holder.receiverProfileImage.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.receiverMessageText.setTextColor(Color.BLACK);
                //holder.receiverMessageText.setText(messages.getMessage() + "\n\n" + messages.getTime().toLowerCase() + " - " + messages.getDate().toLowerCase());
                message=messages.getMessage() + "\t\t" + messages.getTime().toLowerCase().trim() + " - " + messages.getDate().toLowerCase().trim();

                SpannableString string = new SpannableString(message);
                string.setSpan(new RelativeSizeSpan(0.6f), string.length()-19, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                string.setSpan(new ForegroundColorSpan(Color.GRAY), string.length()-19, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.receiverMessageText.setText(string);
            }

        }
        else if(fromMessageType.equals("image"))
        {
            if(fromUserID.equals(messageSenderId))
            {
                holder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageSenderPicture);
            }
            else
                {
                    holder.receiverProfileImage.setVisibility(View.VISIBLE);
                    holder.messageReceiverPicture.setVisibility(View.VISIBLE);

                    Picasso.get().load(messages.getMessage()).into(holder.messageReceiverPicture);
            }
        }
        else
        {
            if (fromUserID.equals(messageSenderId)){
                holder.messageSenderPicture.setVisibility(View .VISIBLE);
                holder.messageSenderPicture.setBackgroundResource(R.drawable.file);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
            else {
                holder.receiverProfileImage.setVisibility(View .VISIBLE);
                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                holder.messageReceiverPicture.setBackgroundResource(R.drawable.file);
            }
        }
        if(fromUserID.equals(messageSenderId))
        {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx"))
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Download and View this document",
                                        "Cancel",
                                        "Delete for Everyone"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    deleteSentMessage(position,holder);

                                }
                                else if(which==1)
                                {
                                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                    holder.itemView.getContext().startActivity(intent);
                                }
                                else if(which==3)
                                {
                                    deleteMessageForEveryOne(position,holder);

                                }
                            }
                        });
                        builder.show();
                    }

                    else if(userMessagesList.get(position).getType().equals("text"))
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Cancel",
                                        "Delete for Everyone"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    deleteSentMessage(position,holder);

                                }
                                else if(which==2)
                                {
                                    deleteMessageForEveryOne(position,holder);

                                }
                            }
                        });
                        builder.show();

                    }
                    else if(userMessagesList.get(position).getType().equals("image"))
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "View this Image",
                                        "Cancel",
                                        "Delete for Everyone"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    deleteSentMessage(position,holder);

                                }
                                else if(which==1)
                                {
                                    Intent intent=new Intent(holder.itemView.getContext(),ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                                    holder.itemView.getContext().startActivity(intent);
                                }
                                else if(which==3)
                                {
                                    deleteMessageForEveryOne(position,holder);

                                }
                            }
                        });
                        builder.show();
                    }
                    return true;
                }
            });
           holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx"))
                    {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);
                    }


                    else if(userMessagesList.get(position).getType().equals("image"))
                    {
                        Intent intent=new Intent(holder.itemView.getContext(),ImageViewerActivity.class);
                        intent.putExtra("url", userMessagesList.get(position).getMessage());
                        holder.itemView.getContext().startActivity(intent);

                    }
                }
            });
        }
        else
        {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx"))
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Download and View this document",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    deleteReceivedMessage(position,holder);
                                }
                                else if(which==1)
                                {
                                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }

                    else if(userMessagesList.get(position).getType().equals("text"))
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    deleteReceivedMessage(position,holder);


                                }
                            }
                        });
                        builder.show();

                    }
                    else if(userMessagesList.get(position).getType().equals("image"))
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "View this Image",
                                        "Cancel"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete Message?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    deleteReceivedMessage(position,holder);

                                }
                                else if(which==1)
                                {
                                    Intent intent=new Intent(holder.itemView.getContext(),ImageViewerActivity.class);
                                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(userMessagesList.get(position).getType().equals("pdf") || userMessagesList.get(position).getType().equals("docx"))
                    {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        holder.itemView.getContext().startActivity(intent);

                    }

                    else if(userMessagesList.get(position).getType().equals("image"))
                    {
                        Intent intent=new Intent(holder.itemView.getContext(),ImageViewerActivity.class);
                        intent.putExtra("url", userMessagesList.get(position).getMessage());
                        holder.itemView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }

    private void deleteSentMessage(final int position, final MessageViewHolder holder)
    {
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference().child("Dove Chat");
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getMessageId())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(holder.itemView.getContext(), "Message deleted!", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(), "Unable to Delete!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void deleteReceivedMessage(final int position, final MessageViewHolder holder)
    {
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference().child("Dove Chat");
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getMessageId())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(holder.itemView.getContext(), "Message deleted!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(holder.itemView.getContext(), "Unable to Delete!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void deleteMessageForEveryOne(final int position, final MessageViewHolder holder)
    {
        final DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference().child("Dove Chat");
        rootRef.child("Messages")
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getMessageId())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    rootRef.child("Messages")
                            .child(userMessagesList.get(position).getFrom())
                            .child(userMessagesList.get(position).getTo())
                            .child(userMessagesList.get(position).getMessageId())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(holder.itemView.getContext(), "Message deleted!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }); }
                else
                {
                    Toast.makeText(holder.itemView.getContext(), "Unable to Delete!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



}
