package entities;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by victor on 27/12/15.
 */
public class AlarmList {

    private List<Alarm> alarms;

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public AlarmList create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AlarmList.class);
    }


}