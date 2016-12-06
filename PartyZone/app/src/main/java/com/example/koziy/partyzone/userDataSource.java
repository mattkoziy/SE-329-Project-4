package com.example.koziy.partyzone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * This class contains methods that open, close, and query the agenda database.
 */
public class userDataSource {

    /**
     * Singleton instance of AgendaDataSource
     */
    private static userDataSource dsInstance = null;

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private long uid = 0;

    /**
     * Array of all column titles in Events table
     */
    private  String[] allColumns = {
            SQLiteHelper.COLUMN_UID,
            SQLiteHelper.COLUMN_FIRST,
            SQLiteHelper.COLUMN_LAST,
            SQLiteHelper.COLUMN_UNAME,
            SQLiteHelper.COLUMN_PW,

    };

    /**
     * Returns an instance of AgendaDataSource if it exists, otherwise creates
     * a new AgendaDataSource object and returns it
     * @param context
     * The Activity that called this method
     * @return
     * An instance of AgendaDataSource
     */
    public static userDataSource getInstance(Context context) {
        if (dsInstance == null) {
            dsInstance = new userDataSource(context.getApplicationContext());
        }
        return dsInstance;
    }

    /**
     * Constructor that should never be called by user
     * @param context
     * The Activity that called this method
     */
    private userDataSource(Context context) {

        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Opens the Agenda database for writing
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the Agenda database
     */
    public void close() {

        dbHelper.close();
    }

    /**
     * Creates new row in database and stores all of the user's details. Then creates
     * an Event object from the details stored in the database and returns it.
     * @param first
     * @param last
     * @return
     * Event object that was created
     */
    public user createUser(String first, String last, String uname, String pw) {

        // Put keys (row columns) and values (parameters) into ContentValues object
        ContentValues values = new ContentValues();

        values.put(allColumns[1], first);
        values.put(allColumns[2], last);
        values.put(allColumns[3], uname);
        values.put(allColumns[4], pw);


        Log.d("UGH","VALUESSSSS" +values);
        // Insert ContentValues into row in events table and obtain row ID
        long id = database.insert(SQLiteHelper.TABLE_USERS, null, values);
        setUid(id);

        // Query database for event row just added using the getEvent(...) method
        // NOTE: You need to write a query to get an event by id at the to-do marker
        //		 in the getEvent(...) method
        user newUser = getUser(id);

        database.close();
        return newUser;
    }

    public long getUserId(){
        return uid;
    }

    public void setUid(long id){
        uid = id;
    }


    public void deleteUser(user event) {
        long id = event.getId();
        String id_string = "" + id;
        database.delete(SQLiteHelper.TABLE_USERS, "? = ?",
                new String[]{SQLiteHelper.COLUMN_UID, id_string});
    }

    /**
     * Queries and returns event based on ID
     * @param id
     * ID of event to return
     * @return
     * Event with ID "id"
     */
    public user getUser(long id) {
        Cursor cursor = database.rawQuery("select * from users where _id = ?", new String[]{Long.toString(id)});

        // TODO: Create query for single event here
        user toReturn = new user();

        if (cursor.moveToFirst()) {
            Log.d("GET all events", Integer.toString(cursor.getPosition()));
            toReturn = cursorToEvent(cursor);

        }


        cursor.close();
        return toReturn;
    }

    public user getUserCredentials(String username) {

        Cursor cursor = database.rawQuery("select * from users where uname = ?", new String[]{username});
        // TODO: Create query for single event here
        user toReturn = new user();

        if (cursor.moveToFirst()) {
            toReturn = cursorToEvent(cursor);

        }



        cursor.close();
        return toReturn;
    }

    /**
     * Queries database for all events stored and creates list of Event objects
     * from returned data.
     * @return
     * List of all Event objects in database
     */
    public List<user> getAllEvents() {
        List<user> events = new ArrayList<user>();

        // Query of all events
        Cursor cursor = database.rawQuery("select * from users ", null);


        if (cursor.moveToFirst()) {
            do {
                Log.d("GET all events", Integer.toString(cursor.getPosition()));
                user event = cursorToEvent(cursor);
                events.add(event);
            } while (cursor.moveToNext()); }

        cursor.close();
        return events;
    }



    /*
     * Helper method to convert row data into Event
     */
    private user cursorToEvent(Cursor cursor) {
        // TODO: Fill event object with data from Cursor
        user event = new user();
        //cursor.moveToFirst();

        //Log.d("CURSOR", Long.toString(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID))));

        event.setId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_UID)));


        event.setFirst(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_FIRST)));


        event.setLast(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_LAST)));

        event.setUname(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_UNAME)));

        event.setPw(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_PW)));



        return event;
    }
}
