package com.example.hermes.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Itenerary extends AppCompatActivity {


    ViewGroup linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itinerary_main);

        ArrayList<ArrayList<String>> b = new ArrayList<>();
        ArrayList<String> a = new ArrayList<>();

        a.add("Marina Bay Sands");
        b.add(a);
        a = new ArrayList<>();
        a.add("Take Public Transport");
        a.add("30 minutes");
        b.add(a);
        a = new ArrayList<>();
        a.add("ResortWorld Sentosa");
        b.add(a);


        linearLayout = (ViewGroup) findViewById(R.id.scroller);

        //call genElement here for each destination and travel method
        //format: genElement( ArrayList of string , identifierCode)
        //case 0: display destination card with just destination name
        //string contains at position 0:name of destination
        //case 1: display travel by public or foot card with time
        //string contains at position 0:travel by foot/take public transport
        //at position 1: time required
        //case 2: display clickable private transport which opens uber app with time
        //string contains at position 0: time taken


        genElement(b.get(0),0);
        genElement(b.get(1),1);
        genElement(b.get(2),0);

    }


    // function simply creates and adds view to a linear layout inside a scrollview depending on what card it needs(destination/travel method)
    public void genElement(ArrayList<String> a, int type){

        LayoutInflater inflater=LayoutInflater.from(this);
        View view;

        switch(type){
            case 0:
                view = inflater.inflate(R.layout.itinerary_destination_item, linearLayout, false);
                TextView x = (TextView) view.findViewById(R.id.textView3);
                x.setText(a.get(0));
                break;
            case 1:
                view = inflater.inflate(R.layout.itenerary_travelmethod_item, linearLayout, false);
                TextView y = (TextView) view.findViewById(R.id.textView3);
                y.setText(a.get(0));
                TextView f = (TextView) view.findViewById(R.id.textView4);
                f.setText(a.get(1));
                break;
            default:
                view = inflater.inflate(R.layout.itenerary_travelcab_interactive_item, linearLayout, false);
                TextView z = (TextView) view.findViewById(R.id.textView3);
                z.setText("Take a cab!");
                TextView b = (TextView) view.findViewById(R.id.textView4);
                b.setText(a.get(0));
                break;
        }

        linearLayout.addView(view);

    }

}
