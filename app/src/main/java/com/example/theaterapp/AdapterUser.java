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

import com.example.theaterapp.databinding.RowCategoryUserBinding;

import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.HolderCategory> implements Filterable {
    public ArrayList<ModelPlay> playArrayList;
    private final Context context;
    private final ArrayList<ModelPlay> filterList;
    private RowCategoryUserBinding binding;

    private FilterUser filter;

    public AdapterUser(Context context, ArrayList<ModelPlay> playArrayList) {
        this.context = context;
        this.playArrayList = playArrayList;
        this.filterList = playArrayList;
    }


    @NonNull
    @Override
    public AdapterUser.HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AdapterUser.HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.HolderCategory holder, int position) {
        //get data
        ModelPlay model = playArrayList.get(position);
        String id = model.getId();
        String play = model.getPlay();
        long timestamp = model.getTimestamp();
        long ticket_money = model.getTicket_money();

        //set data
        holder.categoryTv.setText(play);

        //handle click
        holder.buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirm you want to buy the ticket
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Lefoglalás")
                        .setMessage("Erre az előadásra szeretnél jegyet foglalni?")
                        .setPositiveButton("Igen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Sikeres jegy foglalás", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return playArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterUser(filterList, AdapterUser.this);
        }
        return filter;
    }

    public class HolderCategory extends RecyclerView.ViewHolder {
        TextView categoryTv;
        ImageButton buyTicket;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            categoryTv = binding.categoryTv;
            buyTicket = binding.buyTicket;
        }
    }
}
