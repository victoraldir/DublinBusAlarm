package com.api.service;

import com.APIService;
import com.api.interfaces.IService;
import com.api.utils.Provider;
import com.entities.Station;
import com.entities.Transport;

import org.junit.Test;

import java.util.List;

/**
 * Created by victoraldir on 09/07/2017.
 */
public class APIServiceTest {

    @Test
    public void shouldFetchDublinBusData(){

        IService service = APIService.getInstance().getProvider(Provider.DUBLIN_BUS);
        Station station = service.getStationById("235");

        System.out.print(station.getTag());

        List<Transport> transportList = station.getTransportList();

        for(int x = 0; x<transportList.size(); x++){

            Transport transport = transportList.get(x);

            System.out.println("Destination: " + transport.getDestination());
            System.out.println("Route: " + transport.getRoute());
            System.out.println("Expected Time: " + transport.getExpectedTime());

        }

    }

}