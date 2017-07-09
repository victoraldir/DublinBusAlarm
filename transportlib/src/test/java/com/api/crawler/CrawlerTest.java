package com.api.crawler;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by victoraldir on 09/07/2017.
 */
public class CrawlerTest {

    @Test
    public void shouldCrawlerWikipedia() throws InterruptedException {

        String baseUrl = "https://en.wikipedia.org";
        String url = "https://en.wikipedia.org/wiki/Retrofitting";

        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(20));
        dispatcher.setMaxRequests(20);
        dispatcher.setMaxRequestsPerHost(1);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(new ConnectionPool(100, 30, TimeUnit.SECONDS))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl.parse(baseUrl))
                .addConverterFactory(Crawler.PageAdapter.FACTORY)
                .client(okHttpClient)
                .build();

        Crawler.PageService pageService = retrofit.create(Crawler.PageService.class);

        Crawler crawler = new Crawler(pageService);

        crawler.crawlPage(HttpUrl.parse(url));

        Thread.sleep(15000);

    }

}