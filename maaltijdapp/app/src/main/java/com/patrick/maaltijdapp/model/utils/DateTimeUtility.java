package com.patrick.maaltijdapp.model.utils;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Patrick on 18/01/2018.
 */

/**
 * Provides specific operations for DateTime.
 */
public class DateTimeUtility
{
    private static final String TAG = DateTimeUtility.class.getSimpleName();

    /**
     * Converts a DateTime object to a String in the ISO8601 format.
     *
     * @param value The DateTime to convert.
     * @return Returns a String of the DateTime in the ISO8601 format.
     */
    public static String dateTimeToISO8601(DateTime value)
    {
        String dateTime = value.toString("yyyy-MM-dd HH:mm:ss");

        StringBuilder iso8601Format = new StringBuilder(dateTime.substring(0, 16));
        iso8601Format.setCharAt(10, 'T');

        return iso8601Format.toString();
    }

    /**
     * Converts a String in the ISO 8601 format to a DateTime object.
     *
     * @param value The String to convert.
     * @return Returns DateTime from the String in the ISO8601 format.
     */
    public static DateTime iso8601ToDateTime(String value)
    {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime dateTime = null;

        try
        {
            if (Character.valueOf(Character.toUpperCase(value.charAt(10))).equals('T'))
            {
                StringBuilder iso8601Format = new StringBuilder(value.substring(0, 16));
                iso8601Format.setCharAt(10, ' ');
                dateTime = dateTimeFormat.parseDateTime(iso8601Format.toString());
            }
            else
            {
                dateTime = dateTimeFormat.parseDateTime(value);
            }
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return dateTime;
    }

    /**
     * Converts a String in the NEN ISO 8601 format to a DateTime object.
     *
     * @param value The String to convert.
     * @return Returns DateTime from the String in the NEN ISO 8601 format.
     */
    public static DateTime nenISO8601ToDateTime(String value)
    {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
        DateTime dateTime = null;

        try
        {
            dateTime = dateTimeFormat.parseDateTime(value);
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, e.getMessage());

        }

        return dateTime;
    }
}
