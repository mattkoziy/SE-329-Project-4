package com.example.koziy.partyzone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class showParty extends Activity {

    private ImageButton home;
    private ImageButton settings;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private Button back;
    private Button showMap;
    private pzDataSource datasource;
    private int stars = 0;
    final Context context = this;
    private Location loc;
    public int ratingCount = 0;
    private Party party = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_show_party);


        datasource = pzDataSource.getInstance(this);
        datasource.open();

        home = (ImageButton) findViewById(R.id.home);
        settings = (ImageButton) findViewById(R.id.settings);
        back = (Button) findViewById(R.id.back);
        showMap = (Button) findViewById(R.id.showMap);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);


        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        int permCheck = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION");
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 300, mLocationListener);
        Criteria c = new Criteria();
        loc = lm.getLastKnownLocation(lm.getBestProvider(c, true));

        final double lon = loc.getLongitude();
        final double lat = loc.getLatitude();

        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(showParty.this, settings.class);
                showParty.this.startActivity(myIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(showParty.this, showAddressList.class);
                showParty.this.startActivity(myIntent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(showParty.this, hostOrFind.class);
                showParty.this.startActivity(myIntent);
            }
        });







        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Log.d("extra", data);
        long id = Long.parseLong(data);

         party = datasource.getEvent(id);
        final String address = party.getLocation();
        Log.d("ADRESSSSSS", address);
        String attendees = party.getAttendees();
        String rating = party.getRating();

        TextView addr = (TextView) findViewById(R.id.address);
        TextView attend = (TextView) findViewById(R.id.attendees);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10,0,0);


        if(Integer.parseInt(attendees) < 20){
            stars = 0;
        }
        if(Integer.parseInt(attendees) >= 20 && Integer.parseInt(attendees) < 40){
            stars = 1;
            star1.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star1.setLayoutParams(lp);

        }
        if(Integer.parseInt(attendees) >= 40 && Integer.parseInt(attendees) < 60){
            stars = 2;
            star1.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star1.setLayoutParams(lp);
            star2.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star2.setLayoutParams(lp);
        }

        if(Integer.parseInt(attendees) >= 60 && Integer.parseInt(attendees) < 80){
            stars = 3;
            star1.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star1.setLayoutParams(lp);
            star2.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star2.setLayoutParams(lp);
            star3.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star3.setLayoutParams(lp);
        }
        if(Integer.parseInt(attendees) >= 80){
           stars = 4;
            star1.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star1.setLayoutParams(lp);
            star2.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star2.setLayoutParams(lp);
            star3.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
           star3.setLayoutParams(lp);
            star4.setBackground(getResources().getDrawable(R.drawable.str_lit_new));
            star4.setLayoutParams(lp);
        }


        showMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //increment attendees

                long iid = party.getId();
                int add = Integer.parseInt(party.getAttendees()) + 13;
                party.setAttendees(Integer.toString(add));
                Log.d("atendees", party.getAttendees());

                datasource.updateAttendees(party.getId(), add);
                
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr= " + String.valueOf(lat) + "," + String.valueOf(lon) + "&daddr= " + address));
                startActivity(intent);
            }
        });


        addr.setText(address);
        attend.setText("Attendees: " + attendees);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_party, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent myIntent = new Intent(showParty.this, showAddressList.class);
            showParty.this.startActivity(myIntent);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }




}
