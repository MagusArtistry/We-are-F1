package com.example.wearef1;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeParser {

    // Parses the date and time in UTC format and converts it to the device's local time zone
    public static String convertToLocalTime(String dateTimeUTC) {
        // Input date-time format from JSON (e.g., "2024-03-02T15:00:00Z")
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure parsing is done in UTC

        // Output date-time format for displaying in local time
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFormat.setTimeZone(TimeZone.getDefault()); // Device's local time zone

        try {
            // Parse the date-time string from the input format
            Date date = utcFormat.parse(dateTimeUTC);

            // Convert to local time zone and format
            return localFormat.format(date);
        } catch (ParseException e) {
            Log.e("DateTimeParser", "Error parsing date", e);
            return null;
        }
    }
}