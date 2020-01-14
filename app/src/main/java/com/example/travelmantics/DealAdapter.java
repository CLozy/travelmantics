package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{
    ArrayList<TravelDeal> deal;
    private FirebaseDatabase cFirebaseDatabase;
    private DatabaseReference cDatabaseReference;
    private ChildEventListener cChildListener;

    public DealAdapter(){
        FirebaseUtil.openFbReference("Travel Deals");
        cFirebaseDatabase = FirebaseUtil.cFirebaseDatabase;
        cDatabaseReference = FirebaseUtil.cDatabaseReference;
        deal=FirebaseUtil.cDeals;
        cChildListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal:",td.getTitle());
                td.setId(dataSnapshot.getKey());
                deal.add(td);
                notifyItemInserted(deal.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        cDatabaseReference.addChildEventListener(cChildListener);

    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemview = LayoutInflater.from(context)
                .inflate(R.layout.rv_row,parent,false);
        return new DealViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deals = deal .get(position);
        holder.bind(deals);

    }

    @Override
    public int getItemCount() {
        return deal.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txttitle);
            tvDescription = itemView.findViewById(R.id.txtdescription);
            tvPrice =  itemView.findViewById(R.id.txtprice);
            itemView.setOnClickListener(this);
        }
        public void bind(TravelDeal deal){

            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            TravelDeal selectedDeal = deal.get(position);
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            intent.putExtra("Deal", selectedDeal);
            v.getContext().startActivity(intent);
        }
    }
}
