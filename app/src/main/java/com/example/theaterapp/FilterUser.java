package com.example.theaterapp;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterUser extends Filter {

    ArrayList<ModelPlay> filterList;
    AdapterUser adapterUser;

    public FilterUser(ArrayList<ModelPlay> filterList, AdapterUser adapterUser) {
        this.filterList = filterList;
        this.adapterUser = adapterUser;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelPlay> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getPlay().toUpperCase().contains(constraint)) {
                    filteredModels.add(filterList.get(i));
                }
            }

            results.count = filteredModels.size();
            results.values = filteredModels;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterUser.playArrayList = (ArrayList<ModelPlay>) results.values;
        adapterUser.notifyDataSetChanged();

    }
}
