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

/*
    Adapter for destinations GridView
 */
class ImageAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private ArrayList<Location> locations = new ArrayList<Location>();

    public ImageAdapter(Context context, int hotel) {
        TravelSQL tsql = new TravelSQL(context);
        SQLiteDatabase db = tsql.getWritableDatabase();
        tsql.onCreate(db);
        tsql.onUpgrade(db,0,1);
        locations = tsql.getAllExceptHotel(hotel);                                                                          //Get locations to display, all except hotel user is staying in

        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < locations.size(); i++) {
            mItems.add(new Item(locations.get(i).getLocation(), locations.get(i).getImage(), locations.get(i).getId()));    //Initialise new Item with name, image and location ID, add to list to display
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
        return mItems.get(i).locationId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;
        ImageView tick;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
            v.setTag(R.id.imageViewTick, v.findViewById(R.id.imageViewTick));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);
        tick = (ImageView) v.getTag(R.id.imageViewTick);

        Item item = getItem(i);                                                 //Get item from mItems

        picture.setImageResource(item.drawableId);                              //Set element image resource as location image
        name.setText(item.name);                                                //Set element name as location name
        tick.setImageResource(R.mipmap.tick);                                   //Set element to have a tick image

        return v;
    }

    public static class Item {
        public final String name;       //Location name
        public final int drawableId;    //Location image
        public final int locationId;    //Location ID in database

        Item(String name, int drawableId, int locationId) {
            this.name = name;
            this.drawableId = drawableId;
            this.locationId = locationId;
        }
    }
}