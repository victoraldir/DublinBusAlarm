//package service;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.support.v4.app.NotificationCompat;
//
//import org.joda.time.DateTime;
//import org.joda.time.LocalTime;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import entities.AlarmChild;
//import entities.AlarmParent;
//import entities.Bus;
//import entities.Constants;
//import quartzo.com.dublinbusalarm.R;
//import utils.AlarmPersistence;
//import utils.UtilCheckConnectivity;
//
///**
// * Created by victoraldir on 31/01/16.
// */
//public class WSDublinBusService extends IntentService {
//
//    private final int CONNECTION_PROBLEM = 1;
//    private final int NO_BUSES_FOUND = 2;
//
//    public static String ALARM_SERIALIZED = "alarmSerialized";
//    //public static String BUS_NUMER = "busNumber";
//    //public static String BUS_STOP = "busStop";
//    public static String INTERVAL = "interVal";
//
//    public WSDublinBusService() {
//        super("WSDublinBusService");
//
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        // Gets an instance of the NotificationManager service
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int interval =Integer.parseInt(intent.getStringExtra(INTERVAL));
//        AlarmChild myAlarm = AlarmChild.create(intent.getStringExtra(ALARM_SERIALIZED));
//        //List<Bus> buses = listBusByBusAndStop(intent.getStringExtra(BUS_STOP), intent.getStringExtra(BUS_NUMER));
//        List<Bus> buses = listBusByBusAndStop(myAlarm.getBus().getStop(), myAlarm.getBus().getRoute());
//
//
//        if(!UtilCheckConnectivity.isInternetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))){
//            mNotifyMgr.notify(myAlarm.getId(), lauchNotification(myAlarm, CONNECTION_PROBLEM));
//            return;
//        }
//
//        if(!buses.isEmpty()) {
//
//            for (Bus bus : buses) {
//
//                DateTime timeBus =  DateTime.now().withTime(LocalTime.parse(bus.getTime()));
//
//                long minDiff = timeBus.getMillis() - DateTime.now().getMillis();
//
//                LocalTime dif = LocalTime.fromMillisOfDay(minDiff);
//
//                if (dif.getMinuteOfHour() >= interval) {
//
//                    saveAlarm(dif, bus, myAlarm);
//
//                    return;
//                }
//            }
//
//        }
//
//        mNotifyMgr.notify(myAlarm.getId(), lauchNotification(myAlarm,NO_BUSES_FOUND));
//
//    }
//
//    private Notification lauchNotification(AlarmParent myData, int type){
//        long[] pattern = {1000, 1000, 1000, 1000, 1000};
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//
//        int diff = LocalTime.parse(myData.getBus().getTime()).getMillisOfDay() - LocalTime.now().getMillisOfDay();
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(getApplicationContext())
//                        .setCategory(Notification.CATEGORY_ALARM)
//                        .setSmallIcon(R.drawable.ic_notification)
//                        .setPriority(Notification.PRIORITY_HIGH)
//                        //.setContentIntent(resultPendingIntent)
//                        //.addAction(android.R.drawable.ic_menu_view, "View details", resultPendingIntent)
//                        .setLights(Color.RED, 1, 1);
//
//        if (myData.getChildItemList().get(0).isVibrate()) {
//            mBuilder.setVibrate(pattern);
//        }
//
//        if (myData.getChildItemList().get(0).isSound()) {
//            mBuilder.setSound(uri);
//        }
//
//        switch (type){
//            case CONNECTION_PROBLEM:
//                mBuilder.setContentTitle("Connection error")
//                .setContentText("We tried, but we couldn't. There's something wrong with your connection ");
//                break;
//            case NO_BUSES_FOUND:
//                mBuilder.setContentTitle("No buses has been found")
//                .setContentText("We tried, but we couldn't. There's no buses this time.");
//                break;
//        }
//        return mBuilder.build();
//    }
//
//    private void saveAlarm(LocalTime timeAlarm, Bus bus, AlarmChild myAlarm) {
//        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//
//        DateTime date = DateTime.now().plusHours(timeAlarm.getHourOfDay())
//                .plusMinutes(timeAlarm.getMinuteOfHour())
//                .plusSeconds(timeAlarm.getSecondOfMinute());
//
//
//
//        int alarmId = LocalTime.now().getMillisOfDay();
//
//        myAlarm.setIdNextAlarm(alarmId);
//
//        //LocalTime timeBus = LocalTime.parse(bus.getTime());
//
//        myAlarm.getBus().setTime(bus.getTime());
//
////        if (minutes != 0) {
////
////            LocalTime busTime = timeBus.minusHours(LocalTime.now().getHourOfDay()).minusMinutes(LocalTime.now().getMinuteOfHour()).minusSeconds(LocalTime.now().getSecondOfMinute());
////
////            int diff = busTime.getMinuteOfHour() - minutes;
////
////            date = date.plusMinutes(diff);
////        }
//
//        //Alarm myData;
//
////        myData = new Alarm(alarmId, bus, String.valueOf(minutes),
////                true, true, true);
//
//        Intent alarmIntent;
//
//        alarmIntent = new Intent("EXECUTE_ALARM_BUS");
//
//        alarmIntent.putExtra("myDataSerialized", myAlarm.serialize());
//
//        PendingIntent appIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
//
//        manager.set(AlarmManager.RTC_WAKEUP, date.getMillis(), appIntent);
//
//        AlarmPersistence.saveAlarm(myAlarm, getApplicationContext());
//
//    }
//
//
//
//
//    private List<Bus> listBusByBusAndStop(String busStop, String busNumber){
//
//
//        List<Bus> buses = new ArrayList<>();
//
//        try {
//
//            // Connect to the web site
//            Document document = Jsoup.connect(Constants.URL_DUBLIN_BUS + busStop).get();
//            //Document document = Jsoup.connect(Constants.URL_DUBLIN_BUS).get();
//
//
//            Iterator<Element> table;
//
//            table = document.select("table[id=rtpi-results]").select("tr:contains(" + busNumber + " )").iterator();
//
//
//            if (table != null) {
//
//                //Iterator<Element> ite = table.select("tr:contains(" + params[1] + " )").iterator();
//
//                while (table.hasNext()) {
//                    Element ele = table.next();
//                    //String curr = ele.select("td").get(Constants.DESTINATION).text();
//
//                    if (!ele.className().contains("yellow")) {
//                        if (!ele.select("td").get(Constants.TIME).text().equalsIgnoreCase("Due") &&
//                                !ele.select("td").get(Constants.TIME).text().equalsIgnoreCase("0") &&
//                                !ele.select("td").get(Constants.TIME).text().equalsIgnoreCase("1")) {
//
//                            Bus newBus = new Bus();
//
//                            newBus.setTime(ele.select("td").get(Constants.TIME).text());
//                            newBus.setRoute(ele.select("td").get(Constants.ROUTE).text());
//                            newBus.setDestination(ele.select("td").get(Constants.DESTINATION).text());
//                            newBus.setStop(busNumber);
//
//                            buses.add(newBus);
//                        }
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return buses;
//
//    }
//}
