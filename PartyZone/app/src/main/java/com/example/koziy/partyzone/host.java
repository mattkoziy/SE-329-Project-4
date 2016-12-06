package com.example.koziy.partyzone;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class host extends Activity {


    private Button enter;
    private EditText address;
    private  String partyAddress;
    private ImageButton home;
    private Button cancel;
    private Button useCurrentLocation;
    private pzDataSource datasource;
    private int attendees = 10;
    private int rating = 0;
    final Context context = this;
    private Location loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_host);

        datasource = pzDataSource.getInstance(this);
        datasource.open();

        enter = (Button) findViewById(R.id.enter);
        address = (EditText) findViewById(R.id.address);
        home = (ImageButton) findViewById(R.id.home);
        cancel = (Button) findViewById(R.id.cancel);
        useCurrentLocation = (Button) findViewById(R.id.useCurrentLocation);



        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                int permCheck = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION");
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 300, mLocationListener);
                Criteria c = new Criteria();
                loc = lm.getLastKnownLocation(lm.getBestProvider(c, true));

                List<Address> adresses = null;
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                try{
                    adresses  = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

                String addr = adresses.get(0).getAddressLine(0);
                String city = adresses.get(0).getLocality();
                String state = adresses.get(0).getAdminArea();
                String pc = adresses.get(0).getPostalCode();

                partyAddress = addr + " " + city + " " + state + " " +  pc;
                Log.d("PARTY ADRES!!!!!!", partyAddress);
                addEvent();
                Intent myIntent = new Intent(host.this, showAddressList.class);
                host.this.startActivity(myIntent);
                showAddressList.ADDRESS = partyAddress;
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                partyAddress = address.getText().toString();
                addEvent();
                Intent myIntent = new Intent(host.this, showAddressList.class);
                host.this.startActivity(myIntent);
                showAddressList.ADDRESS = partyAddress;

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(host.this, hostOrFind.class);
                host.this.startActivity(myIntent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(host.this, hostOrFind.class);
                host.this.startActivity(myIntent);
            }
        });
    }

    public void addEvent() {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String endTime = timeStamp.substring(11, 13);


        //where we set the delete time for a party to expire
        int end = Integer.parseInt(endTime) + 1;

        String endFinal = String.valueOf(end);
        Log.d("current time stamp", timeStamp);
        Log.d("end time", endFinal);

        String attendeesStr = Integer.toString(attendees);
        String ratingsStr = Integer.toString(rating);
        // Creates Event object and stores its fields in database
        datasource.createEvent(partyAddress, timeStamp, endFinal, attendeesStr, ratingsStr);

        datasource.close();
        finish();
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


    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                address.setText("");
                return true;
            }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean done = false;
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_DOWN){
            address.setText("");
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            done = true;
        }

        return done;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_host, menu);
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

    /*
	 * Helper method to format the user input into a readable date and time
	 */
    private String formatDateTime(int month, int day, int year, int hour, int minute) {
        Calendar cal = new GregorianCalendar(year, month, day, hour, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, 'at' h:mm a", Locale.US);
        return sdf.format(cal.getTime());
    }


    /*
 * (non-Javadoc)
 * @see android.app.Activity#onResume()
 */
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent myIntent = new Intent(host.this, hostOrFind.class);
           host.this.startActivity(myIntent);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

}
