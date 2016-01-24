package utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.util.Objects;

/**
 * Created by victoraldir on 24/01/16.
 */
public class UtilCheckConnectivity {

    public static boolean isInternetAvailable(Object systemService) {

        ConnectivityManager cm = (ConnectivityManager) systemService;

        return cm.getActiveNetworkInfo() != null;

    }
}
