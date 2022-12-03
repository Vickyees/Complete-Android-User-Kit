package com.example.completeandroiduserkit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class ServiceViewHolder extends RecyclerView.ViewHolder{

    ImageView propic;
    TextView mName,mService,mLocation,mExprience,mAge,mPhone,mAddress,mDescription,mGender,mUid,mSmile;
    CardView profileCard;

    public ServiceViewHolder(@NonNull View itemView) {
        super(itemView);

        propic=itemView.findViewById(R.id.rdp);
        mName=itemView.findViewById(R.id.rname);
        mService=itemView.findViewById(R.id.rservice);
        mLocation=itemView.findViewById(R.id.rlocation);

        mPhone=itemView.findViewById(R.id.rPhone);
        mExprience=itemView.findViewById(R.id.rExprience);
        mAge=itemView.findViewById(R.id.rAge);
        mGender=itemView.findViewById(R.id.rGender);
        mAddress=itemView.findViewById(R.id.rAddress);
        mDescription=itemView.findViewById(R.id.rDescription);
        mUid=itemView.findViewById(R.id.rUid);
        profileCard=itemView.findViewById(R.id.profileCard);



    }
}
