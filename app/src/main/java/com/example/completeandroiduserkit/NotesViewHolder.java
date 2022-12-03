package com.example.completeandroiduserkit;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView textTitle,textTime;
    CardView noteCard;

    public NotesViewHolder(View itemView) {
        super(itemView);

        mView=itemView;
        textTitle=mView.findViewById(R.id.notes_title);
        textTime=mView.findViewById(R.id.notes_time);
        noteCard=mView.findViewById(R.id.note_card);


    }

    public void setNoteTitle(String title){
        textTitle.setText(title);

    }

    public void setNoteTime(String time){

        textTime.setText(time);
    }


}
