package utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.AlarmChild;
import entities.AlarmParent;
import entities.Bus;
import entities.Constants;

/**
 * Created by victor on 28/12/15.
 */
public class AlarmPersistence extends IntentService {

    private static final String TAG = AlarmPersistence.class.getSimpleName();

    private static Gson gson = new Gson();

    public AlarmPersistence() {
        super(TAG);
    }


    public static void saveAlarm(AlarmChild alarm, Context ctx){

        List<AlarmChild> alarms = readStoredAlarms(ctx);

        if(alarms == null){
            alarms = new ArrayList<AlarmChild>();
        }

        if(alarms.contains(alarm)){
            alarms.set(alarms.indexOf(alarm), alarm);
        }else{
            alarms.add(alarm);
        }


        String serializedData = gson.toJson(alarms);

        // Save the serialized data into a shared preference
        SharedPreferences preferencesReader = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesReader.edit();
        editor.putString(Constants.PREFS_KEY, serializedData);
        editor.commit();
    }

    public static void deleteAlarm(AlarmParent alarm, Context ctx){

        List<AlarmChild> alarms = readStoredAlarms(ctx);

        Iterator it = alarms.iterator();

        while (it.hasNext()){
            AlarmParent alarmCurr = (AlarmParent) it.next();

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

    public static List<AlarmChild> readStoredAlarms(Context ctx) {

        List<AlarmChild> alarms = new ArrayList<>();

        SharedPreferences preferencesReader = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Read the shared preference value
        String serializedDataFromPreference = preferencesReader.getString(Constants.PREFS_KEY, null);

        // Create a new object from the serialized data with the same state
        List alarmsDb = gson.fromJson(serializedDataFromPreference, new TypeToken<List<AlarmChild>>() {
        }.getType());

        if(alarmsDb != null){
            alarms.addAll(alarmsDb);

        }

        //List<Alarm> alarms = new ArrayList<Alarm>();

        //alarms.add(restoredMyData);

        return  alarms;
    }

    public static AlarmChild getAlarmById(int id, Context ctx) {

        List<AlarmChild> alarms = new ArrayList<>();

        SharedPreferences preferencesReader = ctx.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);

        // Read the shared preference value
        String serializedDataFromPreference = preferencesReader.getString(Constants.PREFS_KEY, null);

        // Create a new object from the serialized data with the same state
        List alarmsDb = gson.fromJson(serializedDataFromPreference, new TypeToken<List<AlarmChild>>(){}.getType());

        if(alarmsDb != null){
            alarms.addAll(alarmsDb);

        }

        if(!alarms.isEmpty()){
            for (AlarmChild alarm: alarms) {
                if(alarm.getId() == id){
                    return alarm;
                }
            }
        }

        //List<Alarm> alarms = new ArrayList<Alarm>();

        //alarms.add(restoredMyData);

        return  null;
    }

    public static void saveAlarm(int minutes, Bus bus) {

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
