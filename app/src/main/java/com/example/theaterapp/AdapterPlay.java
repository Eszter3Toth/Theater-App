package com.example.theaterapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theaterapp.databinding.RowCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterPlay extends RecyclerView.Adapter<AdapterPlay.HolderCategory> implements Filterable {
    private Context context;
    public ArrayList<ModelPlay> playArrayList, filterList;

    //view binding
    private RowCategoryBinding binding;

    private FilterPlay filter;

    public AdapterPlay(Context context, ArrayList<ModelPlay> playArrayList) {
        this.context = context;
        this.playArrayList = playArrayList;
        this.filterList = playArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        //get data
        ModelPlay model = playArrayList.get(position);
        String id = model.getId();
        String play = model.getPlay();
        long timestamp = model.getTimestamp();
        long ticket_money = model.getTicket_money();

        //set data
        holder.categoryTv.setText(play);

        //handle click
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirm you want to delete and you are not just a noob
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Törlés")
                        .setMessage("Biztosan ki akarod törölni?")
                        .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Törlés", Toast.LENGTH_SHORT).show();
                                deletePlay(model, holder);
                            }
                        })
                        .setNegativeButton("Nem", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void deletePlay(ModelPlay model, HolderCategory holder) {
        String id = model.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plays");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //delete successfully
                        Toast.makeText(context, "Sikeresen kitörlődött", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return playArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterPlay(filterList, this);
        }
        return filter;
    }


    class HolderCategory extends RecyclerView.ViewHolder {
        TextView categoryTv;
        ImageButton deleteBtn;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            categoryTv = binding.categoryTv;
            deleteBtn = binding.deleteBtn;
        }
    }
}
