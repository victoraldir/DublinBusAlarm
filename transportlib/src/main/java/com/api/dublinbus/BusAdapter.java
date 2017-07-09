package com.api.dublinbus;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by victoraldir on 09/07/2017.
 */

public class BusAdapter implements Converter<ResponseBody,BusStop> {

    public static final int ROUTE = 0;
    public static final int DESTINATION = 1;
    public static final int TIME = 2;

    public static final Converter.Factory FACTORY = new Converter.Factory() {
        @Override public Converter<ResponseBody, ?> responseBodyConverter(
                Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == BusStop.class) return new BusAdapter();
            return null;
        }

    };


    @Override
    public BusStop convert(ResponseBody value) throws IOException {

        BusStop station = new BusStop();

        station.setDescription("Set something here!");

        Document document = Jsoup.parse(value.string());

        Iterator<Element> table = document.select("table[id=rtpi-results]").select("tr").iterator();

        List<Bus> busList = new ArrayList<>();

        while (table.hasNext()) {
            Element ele = table.next();

            if (!ele.className().contains("yellow")) {
                if (!ele.select("td").get(TIME).text().equalsIgnoreCase("Due") &&
                        !ele.select("td").get(TIME).text().equalsIgnoreCase("0") &&
                        !ele.select("td").get(TIME).text().equalsIgnoreCase("1")) {

                    Bus newBus = new Bus();

                    newBus.setTime(ele.select("td").get(TIME).text());
                    newBus.setRoute(ele.select("td").get(ROUTE).text());
                    newBus.setDestination(ele.select("td").get(DESTINATION).text());

                    busList.add(newBus);
                }
            }
        }

        station.setBusList(busList);

        return station;
    }
}
