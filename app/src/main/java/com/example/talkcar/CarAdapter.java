package com.example.talkcar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterFactory;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private Context mContext;
    private List<Car> mCars;

    public CarAdapter(Context mContext,List<Car> mCars){

        this.mContext = mContext;
        this.mCars = mCars;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent,false);
            return new CarAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Car car = mCars.get(position);
        holder.carnumber.setText(car.getCarNumber());
        holder.profileImage.setImageResource(MainActivity.emojiMap.get(car.getEmojiId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChatActivity.class);
                intent.putExtra("chattedCar",car);
                ApplicationModel.setLastCarNumberSearch(car);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView carnumber;
        public ImageView profileImage;

        public ViewHolder(View itemView){
            super(itemView);
            carnumber = itemView.findViewById(R.id.car_number);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }

}
