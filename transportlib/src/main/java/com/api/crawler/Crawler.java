package com.api.crawler;

/**
 * Created by victoraldir on 09/07/2017.
 */

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/** A simple web crawler that uses a Retrofit service to turn URLs into webpages. */
public final class Crawler {
    private final Set<HttpUrl> fetchedUrls = Collections.synchronizedSet(
            new LinkedHashSet<HttpUrl>());
    private final ConcurrentHashMap<String, AtomicInteger> hostnames = new ConcurrentHashMap<>();
    private final PageService pageService;

    public Crawler(PageService pageService) {
        this.pageService = pageService;
    }

    public void crawlPage(HttpUrl url) {
        // Skip hosts that we've visited many times.
        AtomicInteger hostnameCount = new AtomicInteger();
        AtomicInteger previous = hostnames.putIfAbsent(url.host(), hostnameCount);
        if (previous != null) hostnameCount = previous;
        if (hostnameCount.incrementAndGet() > 100) return;

        // Asynchronously visit URL.
        pageService.get(url).enqueue(new Callback<Page>() {
            @Override public void onResponse(Call<Page> call, Response<Page> response) {
                if (!response.isSuccessful()) {
                    System.out.println(call.request().url() + ": failed: " + response.code());
                    return;
                }

                // Print this page's URL and title.
                Page page = response.body();
                HttpUrl base = response.raw().request().url();
                System.out.println(base + ": " + page.title);

                // Enqueue its links for visiting.
                for (String link : page.links) {
                    HttpUrl linkUrl = base.resolve(link);
                    if (linkUrl != null && fetchedUrls.add(linkUrl)) {
                        crawlPage(linkUrl);
                    }
                }
            }

            @Override public void onFailure(Call<Page> call, Throwable t) {
                System.out.println(call.request().url() + ": failed: " + t);
            }
        });
    }

    interface PageService {
        @GET Call<Page> get(@Url HttpUrl url);
    }

    static class Page {
        final String title;
        final List<String> links;

        Page(String title, List<String> links) {
            this.title = title;
            this.links = links;
        }
    }

    static final class PageAdapter implements Converter<ResponseBody, Page> {

        static final Converter.Factory FACTORY = new Converter.Factory() {
            @Override public Converter<ResponseBody, ?> responseBodyConverter(
                    Type type, Annotation[] annotations, Retrofit retrofit) {
                if (type == Page.class) return new PageAdapter();
                return null;
            }

        };

        @Override public Page convert(ResponseBody responseBody) throws IOException {
            Document document = Jsoup.parse(responseBody.string());
            List<String> links = new ArrayList<>();
            for (Element element : document.select("a[href]")) {
                links.add(element.attr("href"));
            }
            return new Page(document.title(), Collections.unmodifiableList(links));
        }
    }
}
