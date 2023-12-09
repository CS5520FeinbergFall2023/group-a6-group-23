package edu.northeastern.s3kb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

public class PropertyListAdapterOwner extends RecyclerView.Adapter<PropertyListAdapterOwner.PropertyViewHolder> {

    private Context context;
    private ArrayList<Property> listings;

    public PropertyListAdapterOwner(Context context, ArrayList<Property> properties) {
        this.context = context;
        this.listings = properties;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.property_card, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property model = listings.get(position);
        bindPropertyToViewHolder(holder, model);
        setViewHolderClickListener(holder, model);
    }

    private void bindPropertyToViewHolder(PropertyViewHolder holder, Property model) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.location.setText(model.getHouseLocation());
        holder.rentPerRoom.setText(model.getRentPerRoom());
        holder.noOfRoom.setText(model.getNoOfRoom());
        holder.type.setText(model.getType());
        Glide.with(context).load(model.getHouseImage()).into(holder.houseImg);
        holder.itemView.startAnimation(animation);
    }

    private void setViewHolderClickListener(PropertyViewHolder holder, Property model) {
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyContents.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.houseImg, Objects.requireNonNull(ViewCompat.getTransitionName(holder.houseImg)));
            putExtrasToIntent(intent, model);
            context.startActivity(intent, options.toBundle());
        });
    }

    private void putExtrasToIntent(Intent intent, Property model) {
        intent.putExtra("houseId", model.getHouseId());
        intent.putExtra("noOfRoom", model.getNoOfRoom());
        intent.putExtra("rentPerRoom", model.getRentPerRoom());
        intent.putExtra("houseDescription", model.getHouseDescription());
        intent.putExtra("houseLocation", model.getHouseLocation());
        intent.putExtra("houseImage", model.getHouseImage());
        intent.putExtra("userId", model.getUserId());
        intent.putExtra("country", model.getCountry());
        intent.putExtra("state", model.getState());
        intent.putExtra("type", model.getType());
        intent.putExtra("address", model.getAddress());
        intent.putExtra("baths", model.getBaths());
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView noOfRoom, rentPerRoom, location, type;
        ImageView houseImg;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            houseImg = itemView.findViewById(R.id.imageview);
            noOfRoom = itemView.findViewById(R.id.tv_noOfRooms);
            rentPerRoom = itemView.findViewById(R.id.tv_rentPerRoom);
            location = itemView.findViewById(R.id.tv_location);
            type = itemView.findViewById(R.id.tv_type);
        }
    }
}