package com.patrick.maaltijdapp.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Patrick on 16/01/2018.
 */

public class SQLiteLocalDatabase extends SQLiteOpenHelper
{
    private static final String TAG = SQLiteLocalDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MaaltijdenApp.db";

    // Tables
    private static final String TABLE_NAME_STUDENTS = "Students";
    private static final String TABLE_NAME_MEALS = "Meals";
    private static final String TABLE_NAME_FELLOW_EATERS = "FellowEaters";

    // Columns
    private static final String COLUMN_STUDENT_NUMBER = "StudentNumber";
    private static final String COLUMN_FIRST_NAME = "FirstName";
    private static final String COLUMN_INSERTION = "Insertion";
    private static final String COLUMN_LAST_NAME = "LastName";
    private static final String COLUMN_EMAIL_ADDRESS = "Email";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DISH = "Dish";
    private static final String COLUMN_DATE_TIME = "DateTime";
    private static final String COLUMN_INFO = "Info";
    private static final String COLUMN_CHEF_ID = "ChefID";
    private static final String COLUMN_PICTURE = "Picture";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_MAX_FELLOW_EATERS = "MaxFellowEaters";
    private static final String COLUMN_DOES_COOK_EAT = "DoesCookEat";
    private static final String COLUMN_AMOUNT_OF_GUESTS = "AmountOfGuests";
    private static final String COLUMN_MEAL_ID = "MealID";

    public SQLiteLocalDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS + "; DROP TABLE IF EXISTS " + TABLE_NAME_MEALS + "; DROP TABLE IF EXISTS " + TABLE_NAME_FELLOW_EATERS + "; ");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_STUDENTS + " (" +
                COLUMN_STUDENT_NUMBER + " int(11) PRIMARY KEY UNIQUE NOT NULL, " +
                COLUMN_FIRST_NAME + " varchar(50) NOT NULL, " +
                COLUMN_INSERTION + " varchar(50) DEFAULT NULL, " +
                COLUMN_LAST_NAME + " varchar(50) NOT NULL, " +
                COLUMN_EMAIL_ADDRESS + " varchar(50) NOT NULL, " +
                COLUMN_PHONE_NUMBER + " varchar(30) DEFAULT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MEALS + " (" +
                COLUMN_ID + " int(11) PRIMARY KEY UNIQUE NOT NULL, " +
                COLUMN_DISH + " varchar(100) NOT NULL, " +
                COLUMN_DATE_TIME + " datetime NOT NULL, " +
                COLUMN_INFO + " text NOT NULL, " +
                COLUMN_CHEF_ID + " int(11) NOT NULL, " +
                COLUMN_PICTURE + " longblob NOT NULL, " +
                COLUMN_PRICE + " decimal(10,0) NOT NULL, " +
                COLUMN_MAX_FELLOW_EATERS + " int(11) NOT NULL, " +
                COLUMN_DOES_COOK_EAT + " tinyint(1) NOT NULL DEFAULT '1');");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FELLOW_EATERS + " (" +
                COLUMN_ID + " int(11) PRIMARY KEY UNIQUE NOT NULL, " +
                COLUMN_AMOUNT_OF_GUESTS + " int(11) NOT NULL DEFAULT '0', " +
                COLUMN_STUDENT_NUMBER + " int(11) NOT NULL, " +
                COLUMN_MEAL_ID + " int(11) NOT NULL);");

        Log.i(TAG, String.format("Database %s created, version: %s ", DATABASE_NAME, db.getVersion()));
    }

    // Bij verandering van de db (verhoging van version) wordt onUpgrade aangeroepen.
    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MEALS  + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FELLOW_EATERS + ";");
        onCreate(db);
        Log.i(TAG, String.format("Database %s upgraded - Old version: %s New version: %s ", DATABASE_NAME, oldVersion, newVersion));
    }

    // Er bestaat ook een onDowngrade - aangeroepen bij verlaging van version.
    @Override
    public void onDowngrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MEALS  + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FELLOW_EATERS + ";");
        onCreate(db);
        Log.i(TAG, String.format("Database %s downgraded - Old version: %s New version: %s ", DATABASE_NAME, oldVersion, newVersion));
    }

    public String getName()
    {
        return DATABASE_NAME;
    }

    @Override
    public String toString()
    {
        return String.format("%s version: %s", DATABASE_NAME, DATABASE_VERSION);
    }
}