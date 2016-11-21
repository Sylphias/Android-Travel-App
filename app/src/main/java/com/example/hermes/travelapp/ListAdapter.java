package com.example.hermes.travelapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import supportlib.Location;
import supportlib.PathsAndCost;

/**
 * Created by Wei Ren on 22/11/2016.
 */

public class ListAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private ArrayList<PathsAndCost> pncList = new ArrayList<PathsAndCost>();

    public ListAdapter (Context context, ArrayList<PathsAndCost> pncList){
        mInflater = LayoutInflater.from(context);
        this.pncList = pncList;
    }

    @Override
    public int getCount() {
        return pncList.size();
    }

    @Override
    public PathsAndCost getItem(int i) {
        return pncList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.listItemName, v.findViewById(R.id.listItemName));
        }

        name = (TextView) v.getTag(R.id.listItemName);

        PathsAndCost item = getItem(i);

        name.setText(((PathsAndCost) item).getName());

        return v;
    }

}
