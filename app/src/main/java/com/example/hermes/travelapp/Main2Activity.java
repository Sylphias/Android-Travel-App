package com.example.hermes.travelapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import supportlib.NearestNeighbour;
import supportlib.SearchUtils;
import supportlib.Location;
import supportlib.PathsAndCost;
import supportlib.TravelSQL;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TravelSQL tsql = new TravelSQL(getApplicationContext());
        ArrayList<Integer> test_location = new ArrayList<Integer>();
        SQLiteDatabase db = tsql.getWritableDatabase();
        tsql.onCreate(db);
        test_location.add(2);
        test_location.add(3);
        test_location.add(4);
        List<Location> meme =  tsql.getAllEntries();
//        HashMap<Integer,Location> lol  = SearchUtils.getRawData(test_location,this.getApplicationContext());
//        PathsAndCost rawr = SearchUtils.getBestPath((ArrayList) SearchUtils.generateAllPaths(test_location),30,lol);
//        PathsAndCost rawr2 = NearestNeighbour.getApproximatedPath(lol,50);
//        //ArrayList<PathInfo> paths,double cost, double budget, HashMap<Integer,Location> locations
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
