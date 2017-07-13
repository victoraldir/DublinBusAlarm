package utils;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by victoraldir on 13/07/2017.
 */

public class DateUtils {

    private static final String mask = "HH:mm";

    public static String getIntervalMinutes(String timeIn){

        String minutesResult = "";

        DateFormat df = new SimpleDateFormat(mask, Locale.ENGLISH);

        try {
            DateTime dt = new DateTime(df.parse(timeIn).getTime());

            Period period = new Period(dt,DateTime.now());

            minutesResult = String.valueOf(period.getMinutes());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return minutesResult;
    }

}
