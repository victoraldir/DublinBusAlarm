package utils;

/**
 * Created by victoraldir on 15/07/2017.
 */

public class StringUtils {

    public static String sanitizeBusStation(String rawBusStation){
        return rawBusStation.substring(rawBusStation.indexOf(":") + 2,rawBusStation.length());
    }

}
