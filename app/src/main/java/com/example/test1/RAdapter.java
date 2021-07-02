package com.example.test1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RAdapter extends RecyclerView.Adapter<RAdapter.Holder>{
    Context context;
    ArrayList<uploadinfo> data;
    RAdapter(Context context, ArrayList data){
        this.context=context;
        this.data=data;
    }

    static class Holder extends RecyclerView.ViewHolder{
        TextView name;
        TextView email;
        TextView quantity;
        TextView validity;
        TextView address;
        ImageView img;

        public Holder(View v) {
            super(v);
            email=v.findViewById(R.id.email);
            name=v.findViewById(R.id.fname);
            quantity=v.findViewById(R.id.fquantity);
            validity=v.findViewById(R.id.fvalidity);
            address=v.findViewById(R.id.flocation);
            img=v.findViewById(R.id.foodimage);
        }
    }
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText("Food Name: "+data.get(position).getFood_name());
        holder.validity.setText("Validity: "+data.get(position).getFood_validity());
        holder.quantity.setText("Quantity: "+data.get(position).getFood_quantity());
        holder.email.setText("Email: "+data.get(position).getUser_email());
        holder.address.setText("Address: "+data.get(position).getPickup_location());
        Log.v("url",data.get(position).getFood_image_URL());
        Picasso.get()
                .load(data.get(position).getFood_image_URL())
                .into(holder.img);
        if(position==data.size()-1){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, 0, 0, 250);
            holder.itemView.setLayoutParams(params);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
