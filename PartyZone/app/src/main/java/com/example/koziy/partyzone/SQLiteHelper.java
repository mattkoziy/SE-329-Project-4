package com.example.koziy.partyzone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * This class helps with database setup. None of its methods (except for the constructor)
 * should be called directly.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Table name
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_USERS = "users";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_ATTENDEES = "attendees";
    public static final String COLUMN_RATINGS = "ratings";

    public static final String COLUMN_UID = "_id";
    public static final String COLUMN_FIRST = "first";
    public static final String COLUMN_LAST = "last";
    public static final String COLUMN_PW = "pw";
    public static final String COLUMN_UNAME = "uname";

    // Database name
    private static final String DATABASE_NAME = "agenda.db";

    // Increment this number to clear everything in database
    private static final int DATABASE_VERSION = 29;

    /**
     * Returns an instance of this helper object given the activity
     * @param context
     */
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
		/*
		 * TODO: Create database table "events"
		 * COLUMN_ID should be of type "integer primary key autoincrement"
		 * All other columns should be of type "text not null"
		 * Columns names have been created as constants at top of this class
		 */
        String str = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOCATION + " TEXT NOT NULL, " +
                COLUMN_START + " TEXT NOT NULL, " +
                COLUMN_END + " TEXT NOT NULL, " +
                COLUMN_ATTENDEES + " TEXT NOT NULL, " +
                COLUMN_RATINGS + " TEXT NOT NULL" +
                ")";

        Log.d("ON_CREATE", str);


        db.execSQL(str);


        String str2 = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST + " TEXT NOT NULL, " +
                COLUMN_LAST + " TEXT NOT NULL, " +
                COLUMN_PW + " TEXT NOT NULL, " +
                COLUMN_UNAME + " TEXT NOT NULL" +
                ")";


        db.execSQL(str2);
     }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + SQLiteHelper.TABLE_USERS);
        onCreate(db);
    }

}