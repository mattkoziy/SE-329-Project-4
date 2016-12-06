package com.example.koziy.partyzone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;



import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class settings extends Activity {

    private Button logout;
    private Button cancel;
    private EditText first;
    private EditText last;
    private userDataSource datasource;
    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_settings2);



        logout = (Button) findViewById(R.id.logout);
        cancel = (Button) findViewById(R.id.back);
      /*  first = (EditText) findViewById(R.id.first);
        last = (EditText) findViewById(R.id.last);

        datasource = userDataSource.getInstance(this);
        datasource.open();

         long id = datasource.getUserId();

        user u = datasource.getUser(id);

        first.setText(u.getFirst());
        last.setText(u.getLast());*/



        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(settings.this, hostOrFind.class);
                settings.this.startActivity(myIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(settings.this, login.class);
                settings.this.startActivity(myIntent);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_signup, menu);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent myIntent = new Intent(settings.this, hostOrFind.class);
            settings.this.startActivity(myIntent);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


}
