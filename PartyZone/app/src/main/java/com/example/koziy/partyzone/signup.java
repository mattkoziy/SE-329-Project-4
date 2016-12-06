package com.example.koziy.partyzone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class signup extends Activity {


    private Button Submit;
    private Button cancel;
    private EditText first;
    private EditText last;
    private EditText uname;
    private EditText password;
    private EditText confirm;
    final Context context = this;
    private userDataSource datasource;
    private user u;
    public String fname;
    public String lname;

    public signup(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_signup);

        datasource = userDataSource.getInstance(this);
        datasource.open();

        Submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        first = (EditText) findViewById(R.id.username);
        last = (EditText) findViewById(R.id.lastName);
        uname = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);





        Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user u = datasource.getUserCredentials(uname.getText().toString());

                if(password.getText().toString().length() == 0 || confirm.getText().toString().length()==0 || first.getText().toString().length() == 0 || last.getText().toString().length() ==0){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set title
                    alertDialogBuilder.setTitle("Invalid inputs");

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
               else if (password.getText().toString().length() < 8) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set title
                    alertDialogBuilder.setTitle("Invalid Password");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Password must contain at least 8 characters!")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
               else if (!password.getText().toString().equals(confirm.getText().toString())) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set title
                    alertDialogBuilder.setTitle("Invalid Passwords");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Passwords don't match")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }


                else if (u.getLast() != null){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set title
                    alertDialogBuilder.setTitle("Invalid username");

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setMessage("user name already exists")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else try {


                        setfname(first.getText().toString());
                        setlname(last.getText().toString());


                        if(!checkISU(fname, lname)){

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            // set title
                            alertDialogBuilder.setTitle("Invalid Name");

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setMessage("Name is not in ISU Directory")
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, just close
                                            // the dialog box and do nothing
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }
                        else {

                            addUser();
                            /*String name = first.getText().toString() + last.getText().toString();
                            String pw = password.getText().toString();
                            Intent intent = new Intent(signup.this, login.class);
                            intent.putExtra("data", name);
                            intent.putExtra("pw", pw);
                            Log.d("data sent", name);*/
                            Intent intent = new Intent(signup.this, login.class);
                           // String id = Long.toString(u.getId());
                            //intent.putExtra("data", id);
                            //setResult(RESULT_OK, intent);
                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }


        });


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(signup.this, HomeActivity.class);
                signup.this.startActivity(myIntent);
            }
        });







    }

    public void addUser() {

        String f = first.getText().toString();
        String l = last.getText().toString();


        //used for settings

        setfname(f);
        setlname(l);
        String name = uname.getText().toString();
        String pw = password.getText().toString();
        // Creates Event object and stores its fields in database
        u = datasource.createUser(f, l, name, pw);

        datasource.close();
        finish();
    }


    private void setfname(String s){
        fname = s;
    }
    private void setlname(String s){
        lname = s;
    }

    public String getFname(){
        return fname;
    }

    public String getLname(){
        return lname;
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


    private boolean checkISU(String first, String last) throws IOException {
        File file = new File("directory.txt");


        first = first.toLowerCase();
        last = last.toLowerCase();

        AssetManager am = context.getAssets();

        try {

            InputStream is = am.open("directory.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null){
                String line2[] = line.split("\\s+");
                if(line2[0].toLowerCase().equals(first) && line2[1].toLowerCase().equals(last)){
                    return true;
                }
            }

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent myIntent = new Intent(signup.this, HomeActivity.class);
            signup.this.startActivity(myIntent);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }




}
