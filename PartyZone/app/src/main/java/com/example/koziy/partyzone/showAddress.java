package com.example.koziy.partyzone;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.renderscript.Element;
import android.renderscript.Sampler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class showAddress extends FragmentActivity {

    private GoogleMap map;
    double lattitude;
    double longtitude;
    private pzDataSource datasource;
    final Context context = this;
    private int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_show_address);

        datasource = pzDataSource.getInstance(this);
        datasource.open();


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();


        // List of Events in database
        final List<Party> values = datasource.getAllEvents();
        ArrayList<String> addrs = new ArrayList<String>();
        HashMap hm = new HashMap();

        for(int i = 0; i < values.size(); i++) {
            final String addr = values.get(i).getLocation();
            CoordGPS(addr, context);
            LatLng party = new LatLng(lattitude, longtitude);


            j =i;


            if (map!=null){
                Marker mark = map.addMarker(new MarkerOptions()
                        .position(party)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pizza2)));

            }
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    //set attendees
                    int add = Integer.parseInt(values.get(j).getAttendees()) + 3;
                    values.get(j).setAttendees(Integer.toString(add));

                    LatLng pos = arg0.getPosition();
                    double lon = pos.longitude;
                    double lat = pos.latitude;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr= " + String.valueOf(lat) + "," + String.valueOf(lon)));
                    startActivity(intent);
                    return true;
                }

            });

        }



        final LatLng loc  = new LatLng(42.02, -93.65);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 5));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }


    public void CoordGPS(String address,Context context){
        Geocoder gc=new Geocoder(context, Locale.US);
        List<Address> addresses;

        try {
            addresses=gc.getFromLocationName(address,5);
            if (addresses.size() > 0) {
                lattitude = addresses.get(0).getLatitude();
                longtitude = addresses.get(0).getLongitude();
                Log.d("lat", String.valueOf(longtitude));

            }
        }
        catch (  IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();




        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
