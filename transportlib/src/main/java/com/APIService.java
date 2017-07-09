package com;

import com.api.interfaces.IService;
import com.api.dublinbus.DublinBusStrategy;
import com.api.dublinbus.BusAdapter;
import com.api.dublinbus.DublinBusService;
import com.api.utils.Constants;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class APIService {

    private static APIService instance;
    private OkHttpClient okHttpClient;

    private APIService(){
        initClient();
    }

    public static APIService getInstance(){
        if(instance == null){
            instance = new APIService();
        }

        return instance;
    }

    public IService getProvider(int provider){

        switch (provider){
            case com.api.utils.Provider.DUBLIN_BUS:

                return new DublinBusStrategy(createService(BusAdapter.FACTORY, Constants.DUBLIN_BUS_URL));

            default:

                return null;
        }
    }

    private void initClient(){
        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(20));
        dispatcher.setMaxRequests(20);
        dispatcher.setMaxRequestsPerHost(1);

        okHttpClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(new ConnectionPool(100, 30, TimeUnit.SECONDS))
                .build();
    }


    private DublinBusService createService(Converter.Factory factory, String url){

        Retrofit retrofit= buildRetrofit(factory,url);
        return retrofit.create(DublinBusService.class);

    }

    private Retrofit buildRetrofit(Converter.Factory factory, String url){
        Retrofit retrofit= new Retrofit.Builder()
                .addConverterFactory(factory)
                .baseUrl(url)
                .client(okHttpClient)
                .build();

        return  retrofit;
    }

}
