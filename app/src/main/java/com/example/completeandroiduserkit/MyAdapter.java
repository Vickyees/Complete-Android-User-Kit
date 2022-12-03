package com.example.completeandroiduserkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    public MyAdapter(Context mContext, List<Feedback> myFeedbackList) {
        this.mContext = mContext;
        this.myFeedbackList = myFeedbackList;
    }

    private Context mContext;
    private List<Feedback> myFeedbackList;



    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_feedback,parent,false);
       return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int i) {

        holder.feedback.setText(myFeedbackList.get(i).getFeedback());
    }

    @Override
    public int getItemCount() {
        return myFeedbackList.size();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{

    TextView feedback;
    CardView mcardview;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        feedback=itemView.findViewById(R.id.singlefb);

        mcardview=itemView.findViewById(R.id.singlecard);
    }
}