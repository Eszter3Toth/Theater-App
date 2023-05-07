package com.example.theaterapp;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterPlay extends Filter {

    ArrayList<ModelPlay> filterList;
    AdapterPlay adapterPlay;

    public FilterPlay(ArrayList<ModelPlay> filterList, AdapterPlay adapterPlay) {
        this.filterList = filterList;
        this.adapterPlay = adapterPlay;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results= new FilterResults();
        if(constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelPlay> filteredModels = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getPlay().toUpperCase().contains(constraint)){
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values =filteredModels;
        } else {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterPlay.playArrayList = (ArrayList<ModelPlay>)results.values;
        adapterPlay.notifyDataSetChanged();

    }
}
