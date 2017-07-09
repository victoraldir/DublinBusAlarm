//package utils;
//
//import android.content.Context;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import entities.Bus;
//import entities.Constants;
//
///**
// * Created by victoraldir on 22/02/16.
// */
//public class Utils {
//
//    public static boolean is24Hours(Context ctx){
//        return android.text.format.DateFormat.is24HourFormat(ctx);
//    }
//
//    public static List<Bus> requestListBus(String... params){
//        List<Bus> buses = new ArrayList<>();
//
//        try {
//
//            // Connect to the web site
//            //when it is null so that means it is a test
//            Document document;
//            if(!Constants.URL_DUBLIN_BUS.contains("amazonaws")){
//                document = Jsoup.connect(Constants.URL_DUBLIN_BUS + params[0]).get();
//            }else{
//                document = Jsoup.connect(Constants.URL_DUBLIN_BUS).get();
//            }
//
//            Iterator<Element> table;
//
//            // Get the html document title
//            if(params.length > 1 && !params[1].equals("")) {
//                table = document.select("table[id=rtpi-results]").select("tr:contains(" + params[1] + " )").iterator();
//
//            }else{
//                table = document.select("table[id=rtpi-results]").select("tr").iterator();
//                params[0] = "235";
//            }
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
//                            newBus.setStop(params[0]);
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
//    }
//}
