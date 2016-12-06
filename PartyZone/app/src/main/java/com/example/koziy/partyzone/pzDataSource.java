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
public class pzDataSource {

    /**
     * Singleton instance of AgendaDataSource
     */
    private static pzDataSource dsInstance = null;

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    /**
     * Array of all column titles in Events table
     */
    private  String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_LOCATION,
            SQLiteHelper.COLUMN_START,
            SQLiteHelper.COLUMN_END,
            SQLiteHelper.COLUMN_ATTENDEES,
            SQLiteHelper.COLUMN_RATINGS
    };

    /**
     * Returns an instance of AgendaDataSource if it exists, otherwise creates
     * a new AgendaDataSource object and returns it
     * @param context
     * The Activity that called this method
     * @return
     * An instance of AgendaDataSource
     */
    public static pzDataSource getInstance(Context context) {
        if (dsInstance == null) {
            dsInstance = new pzDataSource(context.getApplicationContext());
        }
        return dsInstance;
    }

    /**
     * Constructor that should never be called by user
     * @param context
     * The Activity that called this method
     */
    private pzDataSource(Context context) {

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
     * Creates new row in database and stores all of the event's details. Then creates
     * an Event object from the details stored in the database and returns it.
     * @param title
     * @param location
     * @param start
     * @param end
     * @param details
     * @return
     * Event object that was created
     */
    public Party createEvent(String title, String location, String start, String end,
                             String details) {

        // Put keys (row columns) and values (parameters) into ContentValues object
        ContentValues values = new ContentValues();

        values.put(allColumns[1], title);
        values.put(allColumns[2], location);
        values.put(allColumns[3], start);
        values.put(allColumns[4], end);
        values.put(allColumns[5], details);


        Log.d("UGH","VALUESSSSS" +values);
        // Insert ContentValues into row in events table and obtain row ID
        long id = database.insert(SQLiteHelper.TABLE_EVENTS, null, values);


        // Query database for event row just added using the getEvent(...) method
        // NOTE: You need to write a query to get an event by id at the to-do marker
        //		 in the getEvent(...) method
        Party newEvent = getEvent(id);

        database.close();
        return newEvent;
    }

    public void deleteEvent(Party event) {
        long id = event.getId();
        String id_string = String.valueOf(id);
        database.delete(SQLiteHelper.TABLE_EVENTS, "_id=?",
                new String[] { id_string });

        database.close();
    }

    /**
     * Queries and returns event based on ID
     * @param id
     * ID of event to return
     * @return
     * Event with ID "id"
     */
    public Party getEvent(long id) {
        Cursor cursor = database.rawQuery("select * from events where _id = ?", new String[]{Long.toString(id)});

        // TODO: Create query for single event here
        Party toReturn = new Party();

        if (cursor.moveToFirst()) {
           // Log.d("GET all events", Integer.toString(cursor.getPosition()));
            toReturn = cursorToEvent(cursor);

        }


        cursor.close();
        return toReturn;
    }

    public void updateAttendees(long id, int attendees){
        Party p = getEvent(id);

        // datasource.createEvent(partyAddress, timeStamp, endFinal, attendeesStr, ratingsStr);

         ContentValues args = new ContentValues();
        Log.d("update location", p.getLocation());


        args.put("location", p.getLocation());
        args.put("start", p.getReadableStartTime());
        args.put("end", p.getReadableEndTime());
        args.put("attendees", Integer.toString(attendees));
        args.put("ratings", p.getRating());


        String strFilter = "_id=" + id;
        int x = database.update("events", args, strFilter, null);
        Log.d("x is", Integer.toString(x));
        database.close();
    }

    /**
     * Queries database for all events stored and creates list of Event objects
     * from returned data.
     * @return
     * List of all Event objects in database
     */
    public List<Party> getAllEvents() {
        List<Party> events = new ArrayList<Party>();

        // Query of all events
        Cursor cursor = database.rawQuery("select * from events ", null);


        if (cursor.moveToFirst()) {
            do {
                Log.d("GET all events", Integer.toString(cursor.getPosition()));
                Party event = cursorToEvent(cursor);
                events.add(event);
            } while (cursor.moveToNext()); }

        cursor.close();
        return events;
    }

    /*
     * Helper method to convert row data into Event
     */
    private Party cursorToEvent(Cursor cursor) {
        // TODO: Fill event object with data from Cursor
        Party event = new Party();
        //cursor.moveToFirst();

        //Log.d("CURSOR", Long.toString(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID))));

        event.setId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)));


        event.setLocation(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_LOCATION)));


        event.setStartTime(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_START)));

        event.setEndTime(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_END)));

        event.setAttendees(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_ATTENDEES)));

        event.setRating(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_RATINGS)));


        return event;
    }
}
