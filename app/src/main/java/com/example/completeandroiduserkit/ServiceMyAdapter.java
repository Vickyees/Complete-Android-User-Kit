package com.example.completeandroiduserkit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ServiceMyAdapter extends RecyclerView.Adapter<ServiceViewHolder> {

    private Context mContext;
    private List<ServiceData>mServiceList;
    private int lastPosition = -1;

    public ServiceMyAdapter(Context mContext, List<ServiceData> mServiceList) {
        this.mContext = mContext;
        this.mServiceList = mServiceList;
    }


    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleservice,parent,false);
        return new ServiceViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceViewHolder holder, int i) {

        if (mServiceList.get(i).getsImage().equals("eee")){
          holder.propic.setImageResource(R.drawable.eee);
        }
        else {
            Glide.with(mContext)
                    .load(mServiceList.get(i).getsImage())
                    .into(holder.propic);
        }
           holder.mName.setText(mServiceList.get(i).getsName());
        holder.mService.setText(mServiceList.get(i).getsService());
        holder.mLocation.setText(mServiceList.get(i).getsLocation());
        holder.mPhone.setText(mServiceList.get(i).getsPhone());
        holder.mExprience.setText(mServiceList.get(i).getsExperience());
        holder.mAge.setText(mServiceList.get(i).getsAge());
        holder.mGender.setText(mServiceList.get(i).getsGender());
        holder.mAddress.setText(mServiceList.get(i).getsAddress());
        holder.mDescription.setText(mServiceList.get(i).getsDescription());
        holder.mUid.setText(mServiceList.get(i).getsUid());


        holder.profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Intent intent=new Intent(mContext,ServiceDetailActivity.class);
                    intent.putExtra("Image",mServiceList.get(holder.getAdapterPosition()).getsImage());
                    intent.putExtra("Name",mServiceList.get(holder.getAdapterPosition()).getsName());
                    intent.putExtra("Ser",mServiceList.get(holder.getAdapterPosition()).getsService());
                    intent.putExtra("Location",mServiceList.get(holder.getAdapterPosition()).getsLocation());
                    intent.putExtra("Phone",mServiceList.get(holder.getAdapterPosition()).getsPhone());
                    intent.putExtra("Exp",mServiceList.get(holder.getAdapterPosition()).getsExperience());
                    intent.putExtra("Age",mServiceList.get(holder.getAdapterPosition()).getsAge());
                    intent.putExtra("Add",mServiceList.get(holder.getAdapterPosition()).getsAddress());
                    intent.putExtra("Desc",mServiceList.get(holder.getAdapterPosition()).getsDescription());
                    intent.putExtra("Gender",mServiceList.get(holder.getAdapterPosition()).getsGender());
                    intent.putExtra("uid",mServiceList.get(holder.getAdapterPosition()).getsUid());
                    intent.putExtra("keyValue",mServiceList.get(holder.getAdapterPosition()).getKey());
                    mContext.startActivity(intent);


                }catch (Exception e){
                    Toast.makeText(mContext, "Make sure your Internet Connection is Good", Toast.LENGTH_SHORT).show();
                }

            }
        });
        setAnimation(holder.itemView,i);

    }

    public void setAnimation(View viewToAnimate, int position ) {

        if (position > lastPosition) {

            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(800);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;


        }

    }



    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public void filteredList(ArrayList<ServiceData> filterList) {
        mServiceList=filterList;
        notifyDataSetChanged();
    }
}

