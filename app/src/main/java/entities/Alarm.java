package entities;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 27/12/15.
 */
public class Alarm implements ParentListItem {

    private int id;

    private int idNextAlarm;

    //private String timeDue;

    private String time;

    private String tag;

    private Bus bus;

    private boolean isActive;

    private boolean isVibrate;

    private boolean isSound;

    private boolean isRepeat;

    public Alarm() {
    }

    public Alarm(int id, Bus bus, String time, String tag, boolean isActive,
                 boolean isVibrate, boolean isSound, boolean isRepeat) {
        this.id = id;
        //this.timeDue = timeDue;
        this.isActive = isActive;
        this.isVibrate = isVibrate;
        this.isSound = isSound;
        this.bus = bus;
        this.time = time;
        this.tag = tag;
    }

    public Alarm(int id, int idNextAlarm, Bus bus, String time, String tag, boolean isActive,
                 boolean isVibrate, boolean isSound, boolean isRepeat) {
        this.id = id;
        //this.timeDue = timeDue;
        this.isActive = isActive;
        this.isVibrate = isVibrate;
        this.isSound = isSound;
        this.bus = bus;
        this.idNextAlarm = idNextAlarm;
        this.time = time;
        this.tag = tag;
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public Alarm create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Alarm.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdNextAlarm() {
        return idNextAlarm;
    }

    public void setIdNextAlarm(int idNextAlarm) {
        this.idNextAlarm = idNextAlarm;
    }

//    public String getTimeDue() {
//        return timeDue;
//    }
//
//    public void setTimeDue(String timeDue) {
//        this.timeDue = timeDue;
//    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alarm alarm = (Alarm) o;

        if (id != alarm.id) return false;
        if (isActive != alarm.isActive) return false;
        if (isVibrate != alarm.isVibrate) return false;
        if (isSound != alarm.isSound) return false;
        if (isRepeat != alarm.isRepeat) return false;
        if (!time.equals(alarm.time)) return false;
        if (!tag.equals(alarm.tag)) return false;
        return bus.equals(alarm.bus);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + time.hashCode();
        result = 31 * result + tag.hashCode();
        result = 31 * result + bus.hashCode();
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (isVibrate ? 1 : 0);
        result = 31 * result + (isSound ? 1 : 0);
        result = 31 * result + (isRepeat ? 1 : 0);
        return result;
    }

    @Override
    public List<?> getChildItemList() {
        List list = new ArrayList<Alarm>();
        list.add(new Alarm(id, idNextAlarm, bus, time, tag, isActive,isVibrate,isSound, isRepeat));
        return list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}