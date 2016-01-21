package entities;

import com.google.gson.Gson;

/**
 * Created by victor on 27/12/15.
 */
public class Alarm {

    private int id;

    private String time;

    private String timeDue;

    private String bus;

    private String busStop;

    private boolean isActive;

    public Alarm() {
    }

    public Alarm(int id, String time, String bus, String busStop, String timeDue, boolean isActive) {
        this.id = id;
        this.time = time;
        this.bus = bus;
        this.busStop = busStop;
        this.timeDue = timeDue;
        this.isActive = isActive;
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

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeDue() {
        return timeDue;
    }

    public String getBus() {
        return bus;
    }



    public String getBusStop() {
        return busStop;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", timeDue='" + timeDue + '\'' +
                ", bus='" + bus + '\'' +
                ", busStop='" + busStop + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alarm alarm = (Alarm) o;

        if (id != alarm.id) return false;
        if (!time.equals(alarm.time)) return false;
        if (!timeDue.equals(alarm.timeDue)) return false;
        if (!bus.equals(alarm.bus)) return false;
        return busStop.equals(alarm.busStop);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + time.hashCode();
        result = 31 * result + timeDue.hashCode();
        result = 31 * result + bus.hashCode();
        result = 31 * result + busStop.hashCode();
        return result;
    }
}