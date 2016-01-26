package entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by victor on 27/12/15.
 */
public class Constants {

    public static final String PREFS_NAME = "MyPrefName";
    public static final String PREFS_KEY = "MyPrefKey";

    public static final int ROUTE = 0;
    public static final int DESTINATION = 1;
    public static final int TIME = 2;

    //public static final String URL_DUBLIN_BUS = "https://s3.amazonaws.com/othersdev/DUBLIN_BUS.HTML";
    public static final String URL_DUBLIN_BUS = "http://www.dublinbus.ie/en/RTPI/Sources-of-Real-Time-Information/?searchtype=view&searchquery=";

    public static final int NO_CONNECTION = 0;
    public static final int NO_DATA_UNAVAILABLE = 1;
    public static final int NO_DATA_UNAVAILABLE_SWITCHER = 2;


}
