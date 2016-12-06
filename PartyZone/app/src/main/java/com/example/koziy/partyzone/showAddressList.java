package com.example.koziy.partyzone;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.jar.Manifest;

import com.example.koziy.partyzone.host;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by koziy on 11/16/2015.
 */
public class showAddressList extends ListActivity{

    public ArrayList<String> addressList = new ArrayList<String>();
    private Button map;
    private String address;
    public static String ADDRESS;
    private ImageButton home;
    private ImageButton settings;
    private pzDataSource datasource;
    double lattitude;
    double longtitude;
    private Location loc;
    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_list_parties);

        datasource = pzDataSource.getInstance(this);
        datasource.open();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        int permCheck = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION");
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 300, mLocationListener);
        Criteria c = new Criteria();
        loc = lm.getLastKnownLocation(lm.getBestProvider(c, true));

        home = (ImageButton) findViewById(R.id.home);
        settings = (ImageButton) findViewById(R.id.settings);
        map = (Button) findViewById(R.id.Map);

        /*LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        String provider = lm.getBestProvider(c, true);*/





        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(showAddressList.this, showAddress.class);
                showAddressList.this.startActivity(myIntent);
            }
        });



        // List of Events in database
        List<Party> values = datasource.getAllEvents();
        Party delete = null;
        int flag = 0;

        for(int i = 0; i < values.size(); i++){
            String addr = values.get(i).getLocation();
            String end = values.get(i).getReadableEndTime();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            String endTime = timeStamp.substring(11, 13);

            CoordGPS(addr, context);
            LatLng dloc = new LatLng(lattitude, longtitude);
            LatLng sloc = new LatLng(loc.getLatitude(), loc.getLongitude());

            double dist = CalculationByDistance(sloc, dloc);
            String dis = String.valueOf(dist);

            if (Integer.parseInt(end) > Integer.parseInt(endTime)) {
                addressList.add(addr + "\n\t" + dis + " miles away");
                Log.d("i", Integer.toString(i));
                Log.d("addr h", addr);


            }
            else{
                delete = values.get(i);
                flag = 1;
                break;
            }

        }

        if(flag==1 && values.size() >=1){
            datasource.deleteEvent(delete);
        }

        flag = 0;

        sortDist(addressList);


        // Set source of ListView to List of Events in database
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, addressList);

        setListAdapter(adapter);

        ListView lv = getListView();


        // listening to single song_list_item click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long eventId = datasource.getAllEvents().get(position).getId();
                String eid = Long.toString(eventId);
                Log.d("eid", eid);
                // Starting new intent
                Intent intent = new Intent(showAddressList.this,showParty.class);
                intent.putExtra("data", eid);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(showAddressList.this, settings.class);
                showAddressList.this.startActivity(myIntent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(showAddressList.this, hostOrFind.class);
                showAddressList.this.startActivity(myIntent);
            }
        });


    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);


       double ret = round(Radius * c, 2);
        return ret;
    }

    public void CoordGPS(String address,Context context){
        Geocoder gc=new Geocoder(context, Locale.US);
        List<Address> addresses;

        try {
            addresses=gc.getFromLocationName(address,5);
            if (addresses.size() > 0) {
                lattitude = addresses.get(0).getLatitude();
                longtitude = addresses.get(0).getLongitude();

            }
        }
        catch (  IOException e) {
            e.printStackTrace();
        }
    }

    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loc = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public static double round(double d, int places){
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
    public void sortDist(ArrayList<String> a){
        HashMap<Double, String> dlist = new HashMap<Double, String>();
        ArrayList<Double> dl = new ArrayList<Double>();

        for (int i = 0; i < a.size(); i++){
            String addr = a.get(i);
            String dist[] = addr.split("\t");
            String dist2[] = dist[1].split(" ");
            double val = Double.parseDouble(dist2[0]);

            dlist.put(val, addr);
            dl.add(val);
            Collections.sort(dl);

        }

        addressList.clear();

        for (int j = 0; j < dl.size(); j++){
            for (Double d : dlist.keySet()){
                int comp = Double.compare(d, dl.get(j));
                if(comp == 0){
                    addressList.add(dlist.get(d));

                }
            }
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent myIntent = new Intent(showAddressList.this, hostOrFind.class);
           showAddressList.this.startActivity(myIntent);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }





}
