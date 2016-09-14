package entities;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by victor on 27/12/15.
 */
public class AlarmParent implements ParentListItem {

    private int id;

    private int idNextAlarm;

    private String time;

    private String tag;

    private int interval;

    private Bus bus;

    //private List<AlarmChild> mChildrenList;

    private AlarmChild alarmChild;

    private boolean isActive;

    public AlarmParent() {

    }

//    public AlarmParent(int id, Bus bus, String time, String tag) {
//        this.id = id;
//        //this.timeDue = timeDue;
////        this.isActive = isActive;
////        this.isVibrate = isVibrate;
////        this.isSound = isSound;
//        this.bus = bus;
//        this.time = time;
//        this.tag = tag;
////        this.isRepeat = isRepeat;
////        this.days = days;
//
//    }

    protected AlarmParent(int id, int idNextAlarm, Bus bus, String time, String tag, boolean isActive) {
        this.id = id;
        //this.timeDue = timeDue;
        this.isActive = isActive;
//        this.isVibrate = isVibrate;
//        this.isSound = isSound;
        this.bus = bus;
        this.idNextAlarm = idNextAlarm;
        this.time = time;
        this.tag = tag;
        this.alarmChild = alarmChild;
//        this.isRepeat = isRepeat;
//        this.days = days;

    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

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

//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setIsActive(boolean isActive) {
//        this.isActive = isActive;
//    }
//
//    public boolean isVibrate() {
//        return isVibrate;
//    }
//
//    public void setIsVibrate(boolean isVibrate) {
//        this.isVibrate = isVibrate;
//    }
//
//    public boolean isSound() {
//        return isSound;
//    }
//
//    public void setIsSound(boolean isSound) {
//        this.isSound = isSound;
//    }
//
//    public boolean isRepeat() {
//        return isRepeat;
//    }
//
//    public void setIsRepeat(boolean isRepeat) {
//        this.isRepeat = isRepeat;
//    }
//
//    public Set<DaysOfWeek> getDays() {
//        return days;
//    }
//
//    public void setDays(Set<DaysOfWeek> days) {
//        this.days = days;
//    }


    public AlarmChild getAlarmChild() {
        return alarmChild;
    }

    public void setAlarmChild(AlarmChild alarmChild) {
        this.alarmChild = alarmChild;
    }

    @Override
    public List<AlarmChild> getChildItemList() {

        List<AlarmChild> alarmChildList = new ArrayList<>();

        alarmChildList.add(alarmChild);

        return alarmChildList;
    }

//    public static Set<DaysOfWeek> generateDaysWeek(){
//        Set<DaysOfWeek> newDaysWeek = new HashSet<>();
//
//        for(int x= 0; x<DaysOfWeek.values().length; x++){
//
//            newDaysWeek.add(DaysOfWeek.values()[x]);
//
//        }
//
//        return newDaysWeek;
//    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmParent alarm = (AlarmParent) o;

        return id == alarm.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}