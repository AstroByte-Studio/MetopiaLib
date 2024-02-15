package de.metopia.lib.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static String prettyDate(Date date) {
        return sdf.format(date);
    }

    public static String prettyDate(long millis) {
        return prettyDate(new Date(millis));
    }

    public static String prettyRemainingTime(long millis) {
        return prettyDate(System.currentTimeMillis()-millis);
    }

}
