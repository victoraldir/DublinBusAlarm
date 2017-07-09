package com.api.dublinbus;


import com.api.interfaces.IService;
import com.entities.Station;
import com.entities.Transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * Created by victoraldir on 08/07/2017.
 */

public class DublinBusStrategy implements IService {

    private DublinBusService dublinBusService;

    public DublinBusStrategy(DublinBusService dublinBusService){
        this.dublinBusService = dublinBusService;
    }

    @Override
    public Station getStationById(String id) {

        Station station = null;

        try {
            Call<BusStop> busStop = dublinBusService.getHtmlTabeBusesByStation(id);
            BusStop body = busStop.execute().body();

            if(body != null) {

                List<Transport> transportList = new ArrayList<>();
                List<Bus> busList = body.getBusList();

                for (int x = 0; x < busList.size(); x++) {

                    Transport transport = new Transport();
                    Bus bus = busList.get(x);

                    transport.setDestination(bus.getDestination());
                    transport.setRoute(bus.getRoute());
                    transport.setExpectedTime(bus.getTime());
                    transportList.add(transport);
                }

                station = new Station();
                station.setTag(id);
                station.setTransportList(transportList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return station;
    }
}
