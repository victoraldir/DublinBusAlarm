package utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import entities.Alarm;

/**
 * Created by victor on 28/12/15.
 */
public class AlarmPersistence {

    private static Gson gson = new Gson();


    public static void saveAlarm(Alarm alarm, Context ctx){

        List<Alarm> alarms = readStoredAlarms(ctx);

        if(alarms == null){
            alarms = new ArrayList<Alarm>();
        }

        alarms.add(alarm);

        String serializedData = gson.toJson(alarms);

        // Save the serialized data into a shared preference
        SharedPreferences preferencesReader = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesReader.edit();
        editor.putString(Constants.PREFS_KEY, serializedData);
        editor.commit();
    }

    public static void deleteAlarm(Alarm alarm, Context ctx){

        List<Alarm> alarms = readStoredAlarms(ctx);

        Iterator it = alarms.iterator();

        while (it.hasNext()){
            Alarm alarmCurr = (Alarm) it.next();

            if(alarmCurr.equals(alarm)){
                it.remove();
            }
        }

        String serializedData = gson.toJson(alarms);

        // Save the serialized data into a shared preference
        SharedPreferences preferencesReader = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesReader.edit();
        if(!alarms.isEmpty()){
            editor.putString(Constants.PREFS_KEY, serializedData);
        }else{
            editor.putString(Constants.PREFS_KEY, null);
        }

        editor.commit();
    }

    public static List<Alarm> readStoredAlarms(Context ctx) {

        SharedPreferences preferencesReader = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Read the shared preference value
        String serializedDataFromPreference = preferencesReader.getString(Constants.PREFS_KEY, null);

        // Create a new object from the serialized data with the same state
        List<Alarm> alarms = gson.fromJson(serializedDataFromPreference, new TypeToken<List<Alarm>>(){}.getType());

        //List<Alarm> alarms = new ArrayList<Alarm>();

        //alarms.add(restoredMyData);

        return  alarms;
    }

}
