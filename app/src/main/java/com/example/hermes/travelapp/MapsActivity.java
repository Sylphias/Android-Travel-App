package com.example.reganandela.testandr;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button locationSubmit = (Button)findViewById(R.id.mapSubmitBtn);
        locationSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                hideKeyboard(MapsActivity.this);
                onMapSearch(v);
            }
        });
    }

    public static void hideKeyboard(Activity act){
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
        LatLng singapore = new LatLng(1.352083, 103.819836);
        mMap.addMarker(new MarkerOptions().position(singapore).title("Marker in Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 10.0f));
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.mapsEdittext);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (location != null || !location.equals("") || location.length()!=0) {
            Geocoder geocoder = new Geocoder(this);
            try {
                    //only sets preference, not a restriction
                    addressList = geocoder.getFromLocationName(location, 1,1.216988,103.589401,1.475781,104.099579);
                    if(addressList.size() > 0){
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                    String loc = address.getLocality()+" in "+address.getCountryName();
//                    Toast toast = Toast.makeText(context,loc,duration);
//                    toast.show();
                }else{
                    Toast toast = Toast.makeText(context,"Location Unknown",duration);
                    toast.show();
                }
            } catch (IOException e) {
                System.err.println(e);
                Toast toast = Toast.makeText(context,"Location Unknown",duration);
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
}
