package entities;

import android.support.v7.app.AlertDialog;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 27/12/15.
 */
public class AlarmChild extends AlarmParent {

    private boolean isVibrate;

    private boolean isSound;

    private boolean isRepeat;

    private Set<DaysOfWeek> days;

    public AlarmChild(int id, int idNextAlarm, Bus bus, String time, String tag, boolean isActive, boolean isVibrate, boolean isSound, boolean isRepeat, Set<DaysOfWeek> days) {
        super(id, idNextAlarm, bus, time, tag, isActive);
        this.isVibrate = isVibrate;
        this.isSound = isSound;
        this.isRepeat = isRepeat;
        this.days = days;
        setAlarmChild(this);
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setIsSound(boolean isSound) {
        this.isSound = isSound;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public Set<DaysOfWeek> getDays() {
        return days;
    }

    public void setDays(Set<DaysOfWeek> days) {
        this.days = days;
    }

    @Override
    public List<AlarmChild> getChildItemList() {

        List<AlarmChild> alarmChildList = new ArrayList<>();

        alarmChildList.add(this);

        return alarmChildList;
    }

    public static Set<DaysOfWeek> generateDaysWeek(){
        Set<DaysOfWeek> newDaysWeek = new HashSet<>();

        for(int x= 0; x<DaysOfWeek.values().length; x++){

            newDaysWeek.add(DaysOfWeek.values()[x]);

        }

        return newDaysWeek;
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public AlarmChild create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AlarmChild.class);
    }
}