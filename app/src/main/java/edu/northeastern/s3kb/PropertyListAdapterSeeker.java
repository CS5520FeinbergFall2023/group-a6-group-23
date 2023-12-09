package edu.northeastern.s3kb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PropertyListAdapterSeeker extends RecyclerView.Adapter<PropertyListAdapterSeeker.PropertyListViewHolder>{

    private Context context;
    private ArrayList<Property> listings = new ArrayList<>();

    public PropertyListAdapterSeeker(Context context, ArrayList<Property> properties) {
        this.context = context;
        this.listings = properties;
    }

    @NonNull
    @Override
    public PropertyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PropertyListViewHolder(LayoutInflater.from(context).inflate(R.layout.seeker_property_card,
                null));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyListViewHolder holder, int position) {
        Property model = listings.get(position);

        Glide.with(context).load(model.getHouseImage()).into(holder.houseImg);
        holder.city.setText(model.getHouseLocation());
        holder.address.setText(model.getAddress());
        holder.state.setText(model.getState());
        holder.country.setText(model.getCountry());
        holder.numberOfBeds.setText(model.getNoOfRoom());
        holder.propertyType.setText(model.getType());
        holder.rentPerRoom.setText(model.getRentPerRoom());
    }

    @Override
    public int getItemCount() {
        if(listings == null){
            return 0;
        }
        return listings.size();
    }

    static class PropertyListViewHolder extends RecyclerView.ViewHolder {
        ImageView houseImg;
        TextView address ,city, state, country;
        TextView numberOfBeds, numberOfBaths, propertyType;
        TextView rentPerRoom;

        public PropertyListViewHolder(@NonNull View itemView) {
            super(itemView);
            houseImg = itemView.findViewById(R.id.prop_image);
            address = itemView.findViewById(R.id.property_address);
            city = itemView.findViewById(R.id.prop_city_tv);
            state = itemView.findViewById(R.id.prop_state_tv);
            country = itemView.findViewById(R.id.prop_country_tv);
            numberOfBeds = itemView.findViewById(R.id.number_of_beds);
            numberOfBaths = itemView.findViewById(R.id.number_of_baths);
            propertyType = itemView.findViewById(R.id.prop_type_tv);
            rentPerRoom = itemView.findViewById(R.id.property_rent);
        }
    }
}
