package com.example.maptesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {
    ArrayList<Location_Attr> location_attrs;
    private Context context;
    Activity search;
    public BookMarkAdapter(ArrayList<Location_Attr> location_attrs, Context context, search search){
        this.context=context;
        this.location_attrs = location_attrs;
        this.search = search;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarkdesign,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Picasso.get().load(location_attrs.get(position).getImageUrl()).into(holder.profileImage);
        if(location_attrs.get(position).getType().equals("Food")){
            Picasso.get().load(R.drawable.khana).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Atm")){
            Picasso.get().load(R.drawable.atm).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Bank")){
            Picasso.get().load(R.drawable.bank).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Parking")){
            Picasso.get().load(R.drawable.parking).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Fuel")){
            Picasso.get().load(R.drawable.fuel).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Hospital")){
            Picasso.get().load(R.drawable.hospit).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Hotel")){
            Picasso.get().load(R.drawable.hotel).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Police Station")){
            Picasso.get().load(R.drawable.polic).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Park")){
            Picasso.get().load(R.drawable.park).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Pharmacy")){
            Picasso.get().load(R.drawable.pharm).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Toilet")){
            Picasso.get().load(R.drawable.toilett).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Train")){
            Picasso.get().load(R.drawable.train).into(holder.profileImage);
        }
        else if(location_attrs.get(position).getType().equals("Education")){
            Picasso.get().load(R.drawable.university).into(holder.profileImage);
        }
        holder.name.setText(location_attrs.get(position).getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String Id = location_attrs.get(position).getName();
                //Toast.makeText(context , Id , Toast.LENGTH_SHORT).show();


                            Intent i = new Intent(search, route.class);
                            i.putExtra("id", Id);
                            search.startActivity(i);
                            search.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return location_attrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.user_dp);
            name = itemView.findViewById(R.id.name);


        }
    }
}
