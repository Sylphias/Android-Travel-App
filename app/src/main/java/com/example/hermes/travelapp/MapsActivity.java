package com.example.hermes.travelapp;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hermes.travelapp.R;
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
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button locationSubmit = (Button) findViewById(R.id.mapSubmitBtn);
        locationSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard(MapsActivity.this);
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
    }

    public static void hideKeyboard(Activity act) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
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

    @Override
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
    }

//    public ArrayList<ArrayList<String>> getHawkerLocations() {
//        String line = null;
//        InputStream is = getResources().openRawResource(R.raw.hawkers);
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        try {
//            while ((line = br.readLine()) != null) {
//
//            }
//        }catch(IOException ioe){
//            ioe.printStackTrace();
//        }
//    }
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
}