package service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Alarm;
import entities.Bus;
import entities.Constants;
import utils.AlarmPersistence;

/**
 * Created by victoraldir on 31/01/16.
 */
public class WSDublinBusService extends IntentService {

    public static String ALARM_SERIALIZED = "alarmSerialized";
    //public static String BUS_NUMER = "busNumber";
    //public static String BUS_STOP = "busStop";
    public static String INTERVAL = "interVal";

    public WSDublinBusService() {
        super("WSDublinBusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int interval =Integer.parseInt(intent.getStringExtra(INTERVAL));
        Alarm myAlarm = Alarm.create(intent.getStringExtra(ALARM_SERIALIZED));
        //List<Bus> buses = listBusByBusAndStop(intent.getStringExtra(BUS_STOP), intent.getStringExtra(BUS_NUMER));
        List<Bus> buses = listBusByBusAndStop(myAlarm.getBus().getStop(), myAlarm.getBus().getRoute());

        for (Bus bus: buses) {

            int minDiff = LocalTime.parse(bus.getTime()).getMillisOfDay() - LocalTime.now().getMillisOfDay();
            if(minDiff / 1000.0 > interval){

                saveAlarm(minDiff - interval, bus, myAlarm);
                break;
            }
        }



    }

    private void saveAlarm(int minutes, Bus bus, Alarm myAlarm) {
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        DateTime date = new DateTime();

        int alarmId = LocalTime.now().getMillisOfDay();

        LocalTime timeBus = LocalTime.parse(bus.getTime());

        if (minutes != 0) {

            LocalTime busTime = timeBus.minusHours(LocalTime.now().getHourOfDay()).minusMinutes(LocalTime.now().getMinuteOfHour()).minusSeconds(LocalTime.now().getSecondOfMinute());

            int diff = busTime.getMinuteOfHour() - minutes;

            date = date.plusMinutes(diff);
        }

        //Alarm myData;

//        myData = new Alarm(alarmId, bus, String.valueOf(minutes),
//                true, true, true);

        Intent alarmIntent;

        alarmIntent = new Intent("EXECUTE_ALARM_BUS");

        alarmIntent.putExtra("myDataSerialized", myAlarm.serialize());

        PendingIntent appIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

        manager.set(AlarmManager.RTC_WAKEUP, date.getMillis(), appIntent);

        AlarmPersistence.saveAlarm(myAlarm, getApplicationContext());

    }




    private List<Bus> listBusByBusAndStop(String busStop, String busNumber){


        List<Bus> buses = new ArrayList<>();

        try {

            // Connect to the web site
            Document document = Jsoup.connect(Constants.URL_DUBLIN_BUS + busStop).get();
            //Document document = Jsoup.connect(Constants.URL_DUBLIN_BUS).get();


            Iterator<Element> table;

            table = document.select("table[id=rtpi-results]").select("tr:contains(" + busNumber + " )").iterator();


            if (table != null) {

                //Iterator<Element> ite = table.select("tr:contains(" + params[1] + " )").iterator();

                while (table.hasNext()) {
                    Element ele = table.next();
                    //String curr = ele.select("td").get(Constants.DESTINATION).text();

                    if (!ele.className().contains("yellow")) {
                        if (!ele.select("td").get(Constants.TIME).text().equalsIgnoreCase("Due") &&
                                !ele.select("td").get(Constants.TIME).text().equalsIgnoreCase("0") &&
                                !ele.select("td").get(Constants.TIME).text().equalsIgnoreCase("1")) {

                            Bus newBus = new Bus();

                            newBus.setTime(ele.select("td").get(Constants.TIME).text());
                            newBus.setRoute(ele.select("td").get(Constants.ROUTE).text());
                            newBus.setDestination(ele.select("td").get(Constants.DESTINATION).text());
                            newBus.setStop(busNumber);

                            buses.add(newBus);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return buses;

    }
}
