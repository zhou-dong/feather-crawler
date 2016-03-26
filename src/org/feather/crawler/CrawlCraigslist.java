package org.feather.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlCraigslist {

	private static String url = "https://montgomery.craigslist.org/";

	public static void main(String[] args) throws IOException {
		print("Fetching %s...", url);

		Document doc = Jsoup.connect(url).get();

		Element regions = doc.select(".acitem").first();

		Elements list = regions.select("a[href]");
		int index = 0;

		for (Element link : list) {
			String href = link.attr("abs:href");
			System.out.println(href);
		}
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
