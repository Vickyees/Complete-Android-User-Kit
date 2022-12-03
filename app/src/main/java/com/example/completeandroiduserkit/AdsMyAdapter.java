package com.example.completeandroiduserkit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdsMyAdapter extends RecyclerView.Adapter<AdsViewHolder>{

    private Context context;
    private List<AdsData> myAdList;
    private int lastPosition = -1;

    public AdsMyAdapter(Context context, List<AdsData> myAdList) {
        this.context = context;
        this.myAdList = myAdList;
    }

    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_row,parent,false);
        return new AdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdsViewHolder holder, int position) {

        Glide.with(context)
                .load(myAdList.get(position).getAdImage())
                .into(holder.adImage);
        holder.aTitle.setText(myAdList.get(position).getAdName());
        holder.aLocation.setText(myAdList.get(position).getAdLocation());
        holder.aSer.setText(myAdList.get(position).getAdService());
        holder.aDecription.setText(myAdList.get(position).getAdDescription());
        holder.aContac.setText(myAdList.get(position).getAdContact());

        holder.acardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DetailActivity.class);
                intent.putExtra("Image",myAdList.get(holder.getAdapterPosition()).getAdImage());
                intent.putExtra("Title",myAdList.get(holder.getAdapterPosition()).getAdName());
                intent.putExtra("Location",myAdList.get(holder.getAdapterPosition()).getAdLocation());
                intent.putExtra("Service",myAdList.get(holder.getAdapterPosition()).getAdService());
                intent.putExtra("Des",myAdList.get(holder.getAdapterPosition()).getAdDescription());
                intent.putExtra("Contact",myAdList.get(holder.getAdapterPosition()).getAdContact());
                intent.putExtra("Contact",myAdList.get(holder.getAdapterPosition()).getAdContact());
                intent.putExtra("Key",myAdList.get(holder.getAdapterPosition()).getAdKey());
                context.startActivity(intent);
            }
        });
        setAnimation(holder.itemView,position);
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
        return myAdList.size();
    }

    public void filteredList(ArrayList<AdsData> filterList) {
        myAdList=filterList;
        notifyDataSetChanged();
    }
}

class AdsViewHolder extends RecyclerView.ViewHolder{

    ImageView adImage;
    TextView aTitle,aLocation,aDecription,aContac,aSer;
    CardView acardView;

    public AdsViewHolder(@NonNull View itemView) {
        super(itemView);

        adImage=itemView.findViewById(R.id.adImage);
        aTitle=itemView.findViewById(R.id.adTitle);
        aLocation=itemView.findViewById(R.id.adLocation);
        aSer=itemView.findViewById(R.id.adService);
        aDecription=itemView.findViewById(R.id.adDec);
        aContac=itemView.findViewById(R.id.adcontact);
        acardView=itemView.findViewById(R.id.adCard);

    }
}
