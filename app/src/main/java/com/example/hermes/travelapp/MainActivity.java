package com.example.hermes.travelapp;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import supportlib.HawkerUtils;
import supportlib.ItineraryStoreSQL;
import supportlib.Location;
import supportlib.NearestNeighbour;
import supportlib.PathsAndCost;
import supportlib.SearchUtils;
import supportlib.PathInfo.TRANSPORTATION;
import supportlib.TravelSQL;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    /*
    All variable initializers
     */
    static ValueAnimator posAnim, opAnim, posAnim1, opAnim1, posAnim2, opAnim2, posAnim3, opAnim3, posAnimScreenOutLeft, opAnimScreenOutLeft, posAnimScreenOutRight, opAnimScreenOutRight, posAnimScreenInLeft, opAnimScreenInLeft, posAnimScreenInRight, opAnimScreenInRight;
    static int currScreen = 1;
    static int animScreen = 1;
    static int nextScreen;
    static int initial = 0;
    static int hotel;
    static ArrayList<RelativeLayout> screens = new ArrayList<RelativeLayout>();
    RelativeLayout screen1, screen2, screen3, screen4, screen5, screen6, screen7, screen8;
    ArrayList<Integer> selectedLoc = new ArrayList<Integer>();
    static double budget = 0;
    private GoogleMap mMap;
    private LatLng curr;
    String copy2clip;
    PathsAndCost resultPnC;
    ArrayList<PathsAndCost> pncList;
    ViewGroup linearLayout;
    ItineraryStoreSQL isql;


    /*
    Main onCreate class
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screens.clear(); //Initialise screens ArrayList
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create database for storing itinerary
        isql = new ItineraryStoreSQL(getApplicationContext());
        final SQLiteDatabase isdb = isql.getWritableDatabase();
        isql.onCreate(isdb);

        //Create database for fetching locations
        TravelSQL tsql = new TravelSQL(getApplicationContext());
        SQLiteDatabase db = tsql.getWritableDatabase();
        tsql.onUpgrade(db,1,2);

        //Assign screens
        screen1 = (RelativeLayout) findViewById(R.id.content_main);
        screen2 = (RelativeLayout) findViewById(R.id.budget);
        screen3 = (RelativeLayout) findViewById(R.id.hotels);
        screen4 = (RelativeLayout) findViewById(R.id.destinations);
        screen5 = (RelativeLayout) findViewById(R.id.activity_itinerary);
        screen6 = (RelativeLayout) findViewById(R.id.maps);
        screen7 = (RelativeLayout) findViewById(R.id.itinerary_list);
        screen8 = (RelativeLayout) findViewById(R.id.activity_itinerary);

        //Put other screens out of view
        screen1.setVisibility(View.VISIBLE);
        screen2.setX(2000);
        screen2.setVisibility(View.VISIBLE);
        screen3.setX(2000);
        screen3.setVisibility(View.VISIBLE);
        screen4.setX(2000);
        screen4.setVisibility(View.VISIBLE);
        screen5.setX(2000);
        screen5.setVisibility(View.VISIBLE);
        screen6.setX(2000);
        screen6.setVisibility(View.VISIBLE);
        screen7.setX(2000);
        screen7.setVisibility(View.VISIBLE);

        //Add screens to screens ArrayList
        screens.add(null);
        screens.add(screen1);
        screens.add(screen2);
        screens.add(screen3);
        screens.add(screen4);
        screens.add(screen5);
        screens.add(screen6);
        screens.add(screen7);
        screens.add(screen8);

        //App initialisation variable
        initial = 0;

        //Hotels screen gridview
        final GridView hotelsview = (GridView) findViewById(R.id.hotelsview);
        hotelsview.setAdapter(new HotelsAdapter(this)); //Set adapter to populate hotelsview

        //Destinations screen gridview
        final GridView gridview = (GridView) findViewById(R.id.gridview);

        hotelsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                hotel = (int) hotelsview.getAdapter().getItemId(position);                          //Get starting location
                selectedLoc.clear();                                                                //Clear selected destinations
                gridview.setAdapter(new ImageAdapter(MainActivity.this, hotel));                    //Set adapter to populate destinations gridview

                //Remove all pre-existing ticks on destinations screen
                for (int i = 0; i < gridview.getChildCount(); i++) {
                    gridview.getChildAt(i - gridview.getFirstVisiblePosition()).findViewById(R.id.imageViewTick).setVisibility(View.INVISIBLE);
                }

                //Set up screens for animation
                animScreen = 3;
                nextScreen = 4;
                animationStart();
                currScreen = 4;
            }
        });

        //When an element in destinations is clicked
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(! selectedLoc.contains((int) gridview.getAdapter().getItemId(position))){        //If not already selected, add to selectedLoc and display tick
                    selectedLoc.add((int) gridview.getAdapter().getItemId(position));
                    gridview.getChildAt(position - gridview.getFirstVisiblePosition()).findViewById(R.id.imageViewTick).setVisibility(View.VISIBLE);
                }
                else{                                                                               //If already selected, remove from selectedLoc and hide tick
                    selectedLoc.remove(selectedLoc.indexOf((int) gridview.getAdapter().getItemId(position)));
                    gridview.getChildAt(position - gridview.getFirstVisiblePosition()).findViewById(R.id.imageViewTick).setVisibility(View.INVISIBLE);
                }
            }
        });

        //FAST ALGORITHM
        FloatingActionButton fastFab = (FloatingActionButton) findViewById(R.id.fastFab);
        linearLayout = (ViewGroup) findViewById(R.id.scroller);
        fastFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLoc.size() == 0){                                                                                           //Check if no elements are selected
                    Toast toast = Toast.makeText(MainActivity.this,"Select at least one location!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{                                                                                                                   //Else input into pathfinder
                    ArrayList<Integer> feedArray = new ArrayList<Integer>();
                    for (int i = 0; i < selectedLoc.size(); i++) feedArray.add(selectedLoc.get(i));                                     //Deepcopy ArrayList to prevent unintended modofications
                    HashMap<Integer, Location> allLocations  = SearchUtils.getRawData(feedArray, getApplicationContext(), hotel);       //Initialise data for algorithm
                    resultPnC = NearestNeighbour.getApproximatedPath(allLocations,budget,hotel);                                        //Run algorithm
                    SQLiteDatabase isdb = isql.getWritableDatabase();
                    isql.insertItinerary(resultPnC, isdb, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date()));    //Write path and cost into itinerary history
                    isdb.close();
                    generateItinerary(resultPnC);                                                                                       //Generate itinerary for next screen

                    //Set up screens for animation
                    animScreen = 4;
                    nextScreen = 5;
                    animationStart();
                    currScreen = 5;
                }
            }
        });

        //EXAUSTIVE ALGORITHM
        FloatingActionButton normalFab = (FloatingActionButton) findViewById(R.id.normalFab);
        linearLayout = (ViewGroup) findViewById(R.id.scroller);
        normalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLoc.size() == 0){                                                                                               //Check if no elements are selected
                    Toast toast = Toast.makeText(MainActivity.this,"Select at least one location!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {                                                                                                                      //Else input into pathfinder
                    ArrayList<Integer> feedArray = new ArrayList<Integer>();
                    for (int i = 0; i < selectedLoc.size(); i++) feedArray.add(selectedLoc.get(i));                                         //Deepcopy ArrayList to prevent unintended modofications
                    HashMap<Integer, Location> allLocations = SearchUtils.getRawData(feedArray, getApplicationContext(), hotel);            //Initialise data for algorithm
                    resultPnC = SearchUtils.getBestPath((ArrayList) SearchUtils.generateAllPaths(feedArray), budget, allLocations, hotel);  //Run algorithm
                    SQLiteDatabase isdb = isql.getWritableDatabase();
                    isql.insertItinerary(resultPnC, isdb, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date()));        //Write path and cost into itinerary history
                    isdb.close();
                    generateItinerary(resultPnC);                                                                                           //Generate itinerary for next screen

                    //Set up screens for animation
                    animScreen = 4;
                    nextScreen = 5;
                    animationStart();
                    currScreen = 5;
                }
            }
        });

        //Initialise map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Button to submit location for search
        final Button locationSubmit = (Button) findViewById(R.id.mapSubmitBtn);
        locationSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);                                                    //Hide keyboard
                onMapSearch(v);
            }
        });

        //Button to find nearby locations
        final Button nearbySubmit = (Button) findViewById(R.id.mapNearbyButton);
        nearbySubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //hideKeyboard(MapsActivity.this);
                nearbySearch(v);
            }
        });

        //ListView for itinerary history
        final ListView itineraryList = (ListView) findViewById(R.id.itineraryList);

        //Button to check itinerary history
        final Button checkItinerary = (Button) findViewById(R.id.checkItinerary);
        checkItinerary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItineraryStoreSQL isql = new ItineraryStoreSQL(getApplicationContext());
                pncList = isql.getAllItineraries();                                                 //Get all past itineraries
                itineraryList.setAdapter(new ListAdapter(MainActivity.this, pncList));              //Set adapter to populate itineraryList

                //Set up screens for animation
                animScreen = 1;
                nextScreen = 7;
                animationStart();
                currScreen = 7;
            }
        });

        //Button to clear history
        FloatingActionButton clearHistory = (FloatingActionButton) findViewById(R.id.clearHistory);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pncList.clear();                                                                    //Clear display array

                //Reinitialise database for history
                ItineraryStoreSQL isql = new ItineraryStoreSQL(getApplicationContext());
                SQLiteDatabase db = isql.getWritableDatabase();
                isql.deleteTable();
                isql.onCreate(db);

                itineraryList.setAdapter(new ListAdapter(MainActivity.this, pncList));              //Reset adapter to populate itineraryList
            }
        });

        //When a ListView element is clicked
        itineraryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                resultPnC = pncList.get(position);                                                  //Fetch PathsAndCost object associated with history item
                generateItinerary(resultPnC);                                                       //Generate itinerary based on selected item

                //Set up screens for animation
                animScreen = 7;
                nextScreen = 8;
                animationStart();
                currScreen = 8;
            }
        });

    }


    /*
    Functionality, UI and animation methods
    */
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //ANIMATES A SCREEN OUT OF THE LEFT SIDE OF THE SCREEN
        //Position
        posAnimScreenOutLeft = ValueAnimator.ofFloat(0, -1 * screen1.getWidth());
        posAnimScreenOutLeft.setDuration(600);
        posAnimScreenOutLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setX((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });
        //Alpha
        opAnimScreenOutLeft = ValueAnimator.ofFloat(1, 0);
        opAnimScreenOutLeft.setDuration(600);
        opAnimScreenOutLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });

        //ANIMATES A SCREEN OUT OF THE RIGHT SIDE OF THE SCREEN
        //Position
        posAnimScreenOutRight = ValueAnimator.ofFloat(0, screen1.getWidth());
        posAnimScreenOutRight.setDuration(600);
        posAnimScreenOutRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setX((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });
        //Alpha
        opAnimScreenOutRight = ValueAnimator.ofFloat(1, 0);
        opAnimScreenOutRight.setDuration(600);
        opAnimScreenOutRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });

        //ANIMATES A SCREEN IN FROM THE LEFT SIDE OF THE SCREEN
        //Position
        posAnimScreenInLeft = ValueAnimator.ofFloat(-1 * screen1.getWidth(), 0);
        posAnimScreenInLeft.setDuration(600);
        posAnimScreenInLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setX((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });
        //Alpha
        opAnimScreenInLeft = ValueAnimator.ofFloat(0, 1);
        opAnimScreenInLeft.setDuration(600);
        opAnimScreenInLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });

        //ANIMATES A SCREEN IN FROM THE RIGHT SIDE OF THE SCREEN
        //Position
        posAnimScreenInRight = ValueAnimator.ofFloat(screen1.getWidth(),0);
        posAnimScreenInRight.setDuration(600);
        posAnimScreenInRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setX((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });
        //Alpha
        opAnimScreenInRight = ValueAnimator.ofFloat(0, 1);
        opAnimScreenInRight.setDuration(600);
        opAnimScreenInRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });

        //INITIAL ANIMATION
        if (initial == 0) {

            //Prompting textview
            //Position
            final TextView what = (TextView) findViewById(R.id.textView2);
            posAnim = ValueAnimator.ofFloat(screen1.getHeight() / 2 - 150, screen1.getHeight() / 2 - 350);//1000, 800); PREV DATA
            posAnim.setDuration(1000).setStartDelay(250);
            posAnim.start();
            posAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    what.setY((float) animation.getAnimatedValue());
                    what.requestLayout();
                }
            });
            //Alpha
            opAnim = ValueAnimator.ofFloat(0, 1);
            opAnim.setDuration(1000).setStartDelay(250);
            opAnim.start();
            opAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    what.setAlpha((float) animation.getAnimatedValue());
                    what.requestLayout();
                }
            });

            //First button
            //Position
            final Button newTrip = (Button) findViewById(R.id.newTrip);
            posAnim1 = ValueAnimator.ofFloat(screen1.getHeight() / 2 + 200, screen1.getHeight() / 2);//1000, 800);
            posAnim1.setDuration(800).setStartDelay(750);
            posAnim1.start();
            posAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    newTrip.setY((float) animation.getAnimatedValue());
                    newTrip.requestLayout();
                }
            });
            //Alpha
            opAnim1 = ValueAnimator.ofFloat(0, 1);
            opAnim1.setDuration(800).setStartDelay(750);
            opAnim1.start();
            opAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    newTrip.setAlpha((float) animation.getAnimatedValue());
                    newTrip.requestLayout();
                }
            });

            //Second button
            //Position
            final Button checkItinerary = (Button) findViewById(R.id.checkItinerary);
            posAnim2 = ValueAnimator.ofFloat(screen1.getHeight() / 2 + 350, screen1.getHeight() / 2 + 150);//1150, 950);
            posAnim2.setDuration(800).setStartDelay(900);
            posAnim2.start();
            posAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    checkItinerary.setY((float) animation.getAnimatedValue());
                    checkItinerary.requestLayout();
                }
            });
            //Alpha
            opAnim2 = ValueAnimator.ofFloat(0, 1);
            opAnim2.setDuration(800).setStartDelay(900);
            opAnim2.start();
            opAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    checkItinerary.setAlpha((float) animation.getAnimatedValue());
                    checkItinerary.requestLayout();
                }
            });

            //Third Button
            //Position
            final Button settings = (Button) findViewById(R.id.settings);
            posAnim3 = ValueAnimator.ofFloat(screen1.getHeight() / 2 + 500, screen1.getHeight() / 2 + 300);//1300, 1100);
            posAnim3.setDuration(800).setStartDelay(1050);
            posAnim3.start();
            posAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    settings.setY((float) animation.getAnimatedValue());
                    settings.requestLayout();
                }
            });
            //Alpha
            opAnim3 = ValueAnimator.ofFloat(0, 1);
            opAnim3.setDuration(800).setStartDelay(1050);
            opAnim3.start();
            opAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    settings.setAlpha((float) animation.getAnimatedValue());
                    settings.requestLayout();
                }
            });
            initial = 1; //Only do initial animations once
        }
    }

    //Start animations
    public void animationStart(){
        animScreen = currScreen;                        //Move out current screen
        posAnimScreenOutLeft.start();                   //Move current screen out to the left side
        opAnimScreenOutLeft.start();                    //Decrease current screen alpha to 0
        posAnimScreenInRight.start();                   //Move next screen in from the right side
        opAnimScreenInRight.start();                    //Increase next screen alpha to 1

        //Move all other screens out of view
        for (int i = 1; i < screens.size(); i++){
            if (i != animScreen && i != nextScreen){
                screens.get(i).setX(2000);
            }
        }
    }

    //When back button is pressed
    public void onBackPressed() {
        if (currScreen == 1) super.onBackPressed();     //If at main screen, exit app
        else if (currScreen == 6 || currScreen == 7){   //If at maps screen or itinerary history screen
            animScreen = currScreen;                    //Move out current screen
            nextScreen = 1;                             //Move in main screen
            posAnimScreenOutRight.start();              //Move current screen out to the right side
            opAnimScreenOutRight.start();               //Decrease current screen alpha to 0
            posAnimScreenInLeft.start();                //Move main screen in from the left side
            opAnimScreenInLeft.start();                 //Increase main screen alpha to 1

            //Move all other screens out of view
            for (int i = 1; i < screens.size(); i++){
                if (i != animScreen && i != nextScreen){
                    screens.get(i).setX(2000);
                }
            }
            currScreen = 1;                             //Current screen is now main screen
        }
        else{
            animScreen = currScreen;
            nextScreen = currScreen - 1;
            posAnimScreenOutRight.start();              //Move current screen out to the right side
            opAnimScreenOutRight.start();               //Decrease current screen alpha to 0
            posAnimScreenInLeft.start();                //Move next screen in from the left side
            opAnimScreenInLeft.start();                 //Increase next screen alpha to 1

            //Move all other screens out of view
            for (int i = 1; i < screens.size(); i++){
                if (i != animScreen && i != nextScreen){
                    screens.get(i).setX(2000);
                }
            }
            currScreen -= 1;                            //Decrement current screen
        }



    }

    //To hide keyboard
    public static void hideKeyboard(Activity act) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }

    //When new trip is selected
    public void budget(View v){
        animScreen = 1;
        nextScreen = 2;
        animationStart();
        currScreen = 2;
    }

    //To submit budget
    public void submitBudget(View v){
        if ((((EditText) findViewById(R.id.editText2)).getText().toString()).equals("")){                   //If TextView is empty, prompt for entry
            Toast toast = Toast.makeText(this,"Invalid entry!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            budget = Double.parseDouble(((EditText) findViewById(R.id.editText2)).getText().toString());    //Convert entry to double, store as budget
            hideKeyboard(MainActivity.this);

            //Set up screens for animation
            animScreen = 2;
            nextScreen = 3;
            animationStart();
            currScreen = 3;
        }

    }


    /*
    Methods to generate itinerary after processing
    */
    public void genElement(ArrayList<String> a, int type){

        /*
        The UI for itinerary is made up of several 'cards' of which the layout XMLs have been defined.
        The code below takes the variable 'type' and then decides which layout to add to the itinerary View.
        0: Destination card for displaying destination name
        1: Travel method card to display all travel methods except Taxi.
        2: Travel method card for taxi (implements Uber app functionality)
         */

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
                TextView k = (TextView) view.findViewById(R.id.textView5);
                k.setText(a.get(2));
                break;
            default:
                view = inflater.inflate(R.layout.itenerary_travelcab_interactive_item, linearLayout, false);
                TextView z = (TextView) view.findViewById(R.id.textView3);
                z.setText("Take a cab!");
                TextView b = (TextView) view.findViewById(R.id.textView4);
                b.setText(a.get(0));
                TextView m = (TextView) view.findViewById(R.id.textView5);
                m.setText(a.get(1));
                break;
        }

        linearLayout.addView(view);
    }

    //To generate itinerary to display
    public void generateItinerary(PathsAndCost resultPnC){

        copy2clip = "M Y   I T I N E R A R Y\n\n"; // String to store the text to copy onto clipboard.
        linearLayout.removeAllViews();             // Removes all views before adding elements to Itinerary View.

        for (int i = 0; i < resultPnC.getPath().size();i++){
            ArrayList<String> xx = new ArrayList<>();
            xx.add(resultPnC.getPath().get(i).getFrom());
            genElement(xx, 0);


            // Code below calls functions based on the input object resultPnC. Uses info to generate cards for travel <from 1 location> <using which mode of transport> <to which location> <time> and <cost>
            if(i==0)
                copy2clip = copy2clip + "Start from hotel!\n\n";
            if(i!=0)
                copy2clip = copy2clip + "Destination "+(i)+": "+resultPnC.getPath().get(i).getFrom()+"\n\n";

            Double cost = resultPnC.getPath().get(i).getCost();
            cost = round(cost * 100.00)/100.00;

            if (resultPnC.getPath().get(i).getMode() == TRANSPORTATION.TAXI) {
                xx = new ArrayList<>();
                xx.add(Integer.toString(resultPnC.getPath().get(i).getDuration()));
                xx.add("$"+Double.toString(cost));
                copy2clip = copy2clip+"Take a cab for around "+"$"+Double.toString(cost)+" ("+Integer.toString(resultPnC.getPath().get(i).getDuration())+" mins)";
                genElement(xx, 2);
            }
            else {
                xx = new ArrayList<>();
                if (resultPnC.getPath().get(i).getMode() == TRANSPORTATION.BUS) {
                    xx.add("Take Public Transport");
                    copy2clip = copy2clip+"Take public transport for around "+"$"+Double.toString(cost)+" ("+Integer.toString(resultPnC.getPath().get(i).getDuration())+" mins)";
                }
                else {
                    xx.add("Take a Walk!");
                    copy2clip = copy2clip+"Walk from here ("+Integer.toString(resultPnC.getPath().get(i).getDuration())+" mins)";
                }
                xx.add(Integer.toString(resultPnC.getPath().get(i).getDuration()));
                xx.add("$"+Double.toString(cost));
                genElement(xx,1);
            }
            copy2clip = copy2clip + "\n";

        }

        ArrayList<String> xx = new ArrayList<>();
        xx.add(resultPnC.getPath().get(resultPnC.getPath().size()-1).getTo());
        genElement(xx, 0);
        copy2clip = copy2clip + "Back at your hotel!\n";
    }


    /*
    Additional functionality
     */

    //When places of interest is selected
    public void maps(View v){
        //Set up screens for animation
        animScreen = 1;
        nextScreen = 6;
        animationStart();
        currScreen = 6;
    }

    //Initialise Google Maps
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        curr = new LatLng(1.352083, 103.819836);
        mMap.addMarker(new MarkerOptions().position(curr).title("Marker in Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 10.0f));
    }

    //Search for location on map
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.mapsEdittext);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        //Call Google Maps functions
        if (location != null || !location.equals("") || location.length() != 0) {
            Geocoder geocoder = new Geocoder(this);
            try {
                //only sets preference, not a restriction
                addressList = geocoder.getFromLocationName(location, 1, 1.216988, 103.589401, 1.475781, 104.099579);

                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    curr = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(curr));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(curr));
//                    String loc = address.getLocality()+" in "+address.getCountryName();
//                    Toast toast = Toast.makeText(context,loc,duration);
//                    toast.show();
                } else {
                    Toast toast = Toast.makeText(context, "Location Unknown", duration);
                    toast.show();
                }
            } catch (IOException e) {                                                               //Exception handler
                System.err.println(e);
                Toast toast = Toast.makeText(context, "Location Unknown", duration);
                toast.show();
            }
        }
    }

    //Search all locations near current location
    public void nearbySearch(View v){
        ArrayList<ArrayList<String>> nearby = HawkerUtils.getNearbyHawkers(HawkerUtils.getHawkerLocations(getResources()),curr.latitude,curr.longitude);
        if(nearby.size() ==0){
            Toast toast = Toast.makeText(getApplicationContext(), "No nearby hawkers", Toast.LENGTH_SHORT);
            toast.show();
        }
        for(int i =0;i<nearby.size();i++){
            LatLng mark = new LatLng(Double.parseDouble(nearby.get(i).get(1)),Double.parseDouble(nearby.get(i).get(0)));
            mMap.addMarker(new MarkerOptions().position(mark).title(nearby.get(i).get(2)));
        }
    }

    //Change map view to roadmap view
    public void roadmap(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    //Change map view to satellite view
    public void satellite(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    //Call Uber app
    public void StartUber(View v){

        // Method is used as an onClick for the imagebutton on the "take a cab" card in the itinerary list. Simply opens the uber app if it is installed on the host mobile.

        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri = "uber://?action=setPickup&pickup=my_location";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
            }
        }
    }

    public void Copy2Clip(View v){

        // This method simply copies the formatted String created in generateItinerary() onto the clipboard

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("TravelItineraryAppStuff", copy2clip);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Your itinerary has been copied to the clipboard!", Toast.LENGTH_SHORT).show();
    }

}

