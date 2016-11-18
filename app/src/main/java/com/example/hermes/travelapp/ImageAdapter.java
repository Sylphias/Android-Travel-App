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
import supportlib.TravelSQL;

import static java.lang.Integer.parseInt;

class ImageAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private ArrayList<Location> locations = new ArrayList<Location>();

    public ImageAdapter(Context context) {
        TravelSQL tsql = new TravelSQL(context);
        SQLiteDatabase db = tsql.getWritableDatabase();
        tsql.onCreate(db);
        tsql.onUpgrade(db,0,1);
        locations = tsql.getAllEntries();

        mInflater = LayoutInflater.from(context);

        for (int i = 0; i < locations.size(); i++) {
            mItems.add(new Item(locations.get(i).getLocation(), locations.get(i).getImage()));
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return v;
    }

    private static class Item {
        public final String name;
        public final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}