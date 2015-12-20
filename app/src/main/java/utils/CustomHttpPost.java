package utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class CustomHttpPost {


	public static String getData(String url) {

		// Connect to the web site
		try {
			Document document = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}