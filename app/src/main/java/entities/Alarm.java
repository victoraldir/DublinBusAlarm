package entities;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by victor on 27/12/15.
 */
public class Alarm implements ParentListItem {

    private int id;

    //private String time;

    private String timeDue;

    //private String bus;

    //private String busStop;

    private Bus bus;

    private boolean isActive;

    private boolean isVibrate;

    private boolean isSound;

    public Alarm() {
    }

    public Alarm(int id, Bus bus, String timeDue, boolean isActive, boolean isVibrate, boolean isSound) {
        this.id = id;
        //this.time = time;
        //this.bus = bus;
        //this.busStop = busStop;
        this.timeDue = timeDue;
        this.isActive = isActive;
        this.isVibrate = isVibrate;
        this.isSound = isSound;
        this.bus = bus;
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

//    public String getTime() {
//        return time;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeDue() {
        return timeDue;
    }

//    public String getBus() {
//        return bus;
//    }

    public boolean isSound() {
        return isSound;
    }

    public void setIsSound(boolean isSound) {
        this.isSound = isSound;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
    }

//    public String getBusStop() {
//        return busStop;
//    }


    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setTimeDue(String timeDue) {
        this.timeDue = timeDue;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", timeDue='" + timeDue + '\'' +
                ", bus=" + bus +
                ", isActive=" + isActive +
                ", isVibrate=" + isVibrate +
                ", isSound=" + isSound +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alarm alarm = (Alarm) o;

        if (id != alarm.id) return false;
        if (!timeDue.equals(alarm.timeDue)) return false;
        return bus.equals(alarm.bus);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + timeDue.hashCode();
        result = 31 * result + bus.hashCode();
        return result;
    }

    @Override
    public List<?> getChildItemList() {
        List list = new ArrayList<Alarm>();
        list.add(new Alarm(id,bus,timeDue,isActive,isVibrate,isSound));
        //list.add(new Object());
        return list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}