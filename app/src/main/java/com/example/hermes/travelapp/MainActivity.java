package com.example.hermes.travelapp;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import supportlib.Location;
import supportlib.NearestNeighbour;
import supportlib.PathsAndCost;
import supportlib.SearchUtils;
import supportlib.PathInfo.TRANSPORTATION;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    static ValueAnimator posAnim, opAnim, posAnim1, opAnim1, posAnim2, opAnim2, posAnim3, opAnim3, posAnimScreenOutLeft, opAnimScreenOutLeft, posAnimScreenOutRight, opAnimScreenOutRight, posAnimScreenInLeft, opAnimScreenInLeft, posAnimScreenInRight, opAnimScreenInRight;
    static int currScreen = 1;
    static int animScreen = 1;
    static int nextScreen;
    static int initial = 0;
    static ArrayList<RelativeLayout> screens = new ArrayList<RelativeLayout>();
    RelativeLayout screen1, screen2, screen3, screen4, screen5, screen6;
    ArrayList<Integer> selectedLoc = new ArrayList<Integer>();

    private GoogleMap mMap;
    private LatLng curr;

    PathsAndCost rawr;
    PathsAndCost rawr2;

    ViewGroup linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen1 = (RelativeLayout) findViewById(R.id.content_main);
        screen2 = (RelativeLayout) findViewById(R.id.budget);
        screen3 = (RelativeLayout) findViewById(R.id.destinations);
        screen4 = (RelativeLayout) findViewById(R.id.activity_itinerary);
        screen6 = (RelativeLayout) findViewById(R.id.maps);
        screen1.setVisibility(View.VISIBLE);
        screen2.setX(2000);
        screen2.setVisibility(View.VISIBLE);
        screen3.setX(2000);
        screen3.setVisibility(View.VISIBLE);
        screen4.setX(2000);
        screen4.setVisibility(View.VISIBLE);
        screen6.setX(2000);
        screen6.setVisibility(View.VISIBLE);
        screens.add(null);
        screens.add(screen1);
        screens.add(screen2);
        screens.add(screen3);
        screens.add(screen4);
        screens.add(screen5);
        screens.add(screen6);
        initial = 0;

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(! selectedLoc.contains(position)){
                    selectedLoc.add(position);
                    gridview.getChildAt(position).findViewById(R.id.imageView2).setVisibility(View.VISIBLE);
                }
                else{
                    selectedLoc.remove(selectedLoc.indexOf(position));
                    gridview.getChildAt(position).findViewById(R.id.imageView2).setVisibility(View.INVISIBLE);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        linearLayout = (ViewGroup) findViewById(R.id.scroller);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, Location> lol  = SearchUtils.getRawData(selectedLoc, getApplicationContext());
                rawr = SearchUtils.getBestPath((ArrayList) SearchUtils.generateAllPaths(selectedLoc),30,lol);
                rawr2 = NearestNeighbour.getApproximatedPath(lol,50);
                animScreen = 3;
                nextScreen = 4;
                animationStart();
                currScreen = 4;


                for (int i = 0; i < rawr.getPath().size();i++){
                    ArrayList<String> xx = new ArrayList<>();
                    xx.add(rawr.getPath().get(i).getFrom());
                    genElement(xx, 0);
                    if (rawr.getPath().get(i).getMode() == TRANSPORTATION.TAXI) {
                        xx = new ArrayList<>();
                        xx.add(Integer.toString(rawr.getPath().get(i).getDuration()));
                        genElement(xx, 2);
                    }
                    else {
                        xx = new ArrayList<>();
                        xx.add("Take Public Transport");
                        xx.add(Integer.toString(rawr.getPath().get(i).getDuration()));
                        genElement(xx,1);
                    }

                }

                ArrayList<String> xx = new ArrayList<>();
                xx.add(rawr.getPath().get(rawr.getPath().size()-1).getTo());
                genElement(xx, 0);


            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button locationSubmit = (Button) findViewById(R.id.mapSubmitBtn);
        locationSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
                onMapSearch(v);
            }
        });

        final Button nearbySubmit = (Button) findViewById(R.id.mapNearbyButton);
        nearbySubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //hideKeyboard(MapsActivity.this);
                nearbySearch(v);
            }
        });

        ArrayList<ArrayList<String>> b = new ArrayList<>();
        ArrayList<String> a = new ArrayList<>();





        //call genElement here for each destination and travel method
        //format: genElement( ArrayList of string , identifierCode)
        //case 0: display destination card with just destination name
        //string contains at position 0:name of destination
        //case 1: display travel by public or foot card with time
        //string contains at position 0:travel by foot/take public transport
        //at position 1: time required
        //case 2: display clickable private transport which opens uber app with time
        //string contains at position 0: time taken

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        posAnimScreenOutLeft = ValueAnimator.ofFloat(0, -1 * screen1.getWidth());
        posAnimScreenOutLeft.setDuration(600);
        posAnimScreenOutLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setX((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });
        opAnimScreenOutLeft = ValueAnimator.ofFloat(1, 0);
        opAnimScreenOutLeft.setDuration(600);
        opAnimScreenOutLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });

        posAnimScreenOutRight = ValueAnimator.ofFloat(0, screen1.getWidth());
        posAnimScreenOutRight.setDuration(600);
        posAnimScreenOutRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setX((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });
        opAnimScreenOutRight = ValueAnimator.ofFloat(1, 0);
        opAnimScreenOutRight.setDuration(600);
        opAnimScreenOutRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(animScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(animScreen).requestLayout();
            }
        });

        posAnimScreenInLeft = ValueAnimator.ofFloat(-1 * screen1.getWidth(), 0);
        posAnimScreenInLeft.setDuration(600);
        posAnimScreenInLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setX((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });
        opAnimScreenInLeft = ValueAnimator.ofFloat(0, 1);
        opAnimScreenInLeft.setDuration(600);
        opAnimScreenInLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });

        posAnimScreenInRight = ValueAnimator.ofFloat(screen1.getWidth(),0);
        posAnimScreenInRight.setDuration(600);
        posAnimScreenInRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setX((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });
        opAnimScreenInRight = ValueAnimator.ofFloat(0, 1);
        opAnimScreenInRight.setDuration(600);
        opAnimScreenInRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                screens.get(nextScreen).setAlpha((float) animation.getAnimatedValue());
                screens.get(nextScreen).requestLayout();
            }
        });

        if (initial == 0) {
            final TextView what = (TextView) findViewById(R.id.textView2);
            posAnim = ValueAnimator.ofFloat(screen1.getHeight() / 2 - 150, screen1.getHeight() / 2 - 350);//1000, 800);
            posAnim.setDuration(1000).setStartDelay(250);
            posAnim.start();
            posAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    what.setY((float) animation.getAnimatedValue());
                    what.requestLayout();
                }
            });
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
            initial = 1;
        }
    }

    public void budget(View v){
        animScreen = 1;
        nextScreen = 2;
        animationStart();
        currScreen = 2;
    }

    public void submitBudget(View v){
        animScreen = 2;
        nextScreen = 3;
        animationStart();
        currScreen = 3;
    }

    public void maps(View v){
        animScreen = 1;
        nextScreen = 6;
        animationStart();
        currScreen = 6;
    }

    public static void hideKeyboard(Activity act) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        curr = new LatLng(1.352083, 103.819836);
        mMap.addMarker(new MarkerOptions().position(curr).title("Marker in Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 10.0f));
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.mapsEdittext);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

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
            } catch (IOException e) {
                System.err.println(e);
                Toast toast = Toast.makeText(context, "Location Unknown", duration);
                toast.show();
            }
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;

            case R.id.action_settings2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }*/

    public void nearbySearch(View v){
        ArrayList<ArrayList<String>> nearby = getNearbyHawkers(getHawkerLocations(),curr.latitude,curr.longitude);
        if(nearby.size() ==0){
            Toast toast = Toast.makeText(getApplicationContext(), "No nearby hawkers", Toast.LENGTH_SHORT);
            toast.show();
        }
        for(int i =0;i<nearby.size();i++){
            LatLng mark = new LatLng(Double.parseDouble(nearby.get(i).get(1)),Double.parseDouble(nearby.get(i).get(0)));
            mMap.addMarker(new MarkerOptions().position(mark).title(nearby.get(i).get(2)));
        }
    }

    public ArrayList<ArrayList<String>> getHawkerLocations() {
        String line = null;
        InputStream is = null;
        ArrayList<ArrayList<String>> big = new ArrayList<ArrayList<String>>();
        try {
            is = getResources().openRawResource(R.raw.hawkers);
            //is = new FileInputStream("C:\\Users\\tycli_000\\AndroidStudioProjects\\TestAndr\\app\\src\\main\\res\\raw\\hawkers
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            boolean nameflag = false;
            ArrayList<String> smol = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                if(line.contains("<name>") && !line.contains("<name>HAWKERCENTRE")){
                    smol.add(line.trim().replace("<name>", "").replace("</name>", ""));
                    nameflag = true;
                }else if(line.contains("<coordinates>") && nameflag){
                    String[] tmp = line.trim().split(",");
                    smol.add(0,tmp[1]);
                    smol.add(0, tmp[0].split(" ")[1]);
                    big.add(smol);
                    smol = new ArrayList<String>();
                    nameflag = false;
                }
            }
            br.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        catch(Exception er){
            er.printStackTrace();
        }

        return big;
    }

    public ArrayList<ArrayList<String>> getNearbyHawkers(ArrayList<ArrayList<String>> inpt,double lat, double lon){
        //data is long lat
        double modval = 0.025;
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        for(int i =0;i<inpt.size();i++){
            //if within bounding box
            if((!(lon+modval < Double.parseDouble(inpt.get(i).get(0)) ||
                    lon-modval > Double.parseDouble(inpt.get(i).get(0))) &&
                    !(lat+modval < Double.parseDouble(inpt.get(i).get(1)) ||
                            lat-modval > Double.parseDouble(inpt.get(i).get(1))))){
                ret.add(inpt.get(i));
            }
        }
        return ret;
    }

    public void roadmap(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void satellite(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

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

    public void animationStart(){
        animScreen = currScreen;
        posAnimScreenOutLeft.start();
        opAnimScreenOutLeft.start();
        posAnimScreenInRight.start();
        opAnimScreenInRight.start();
    }

    public void onBackPressed() {
        if (currScreen == 1) super.onBackPressed();
        else if (currScreen == 6){
            animScreen = currScreen;
            nextScreen = 1;
            posAnimScreenOutRight.start();
            opAnimScreenOutRight.start();
            posAnimScreenInLeft.start();
            opAnimScreenInLeft.start();
            currScreen = 1;
        }
        else{
            animScreen = currScreen;
            nextScreen = currScreen - 1;
            posAnimScreenOutRight.start();
            opAnimScreenOutRight.start();
            posAnimScreenInLeft.start();
            opAnimScreenInLeft.start();
            currScreen -= 1;
        }
    }



}

