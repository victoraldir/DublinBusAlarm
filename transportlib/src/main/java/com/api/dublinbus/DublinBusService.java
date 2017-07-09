package com.api.dublinbus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victoraldir on 09/07/2017.
 */

public interface DublinBusService {

    @GET("/en/RTPI/Sources-of-Real-Time-Information/?searchtype=view")
    Call<BusStop> getHtmlTabeBusesByStation(@Query("searchquery") String query);

}
