package org.feather.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlCraigslist {

	private static String url = "https://montgomery.craigslist.org/";

	private String createAutoSearchUrl(String url) {
		return url + "search/cta";
	}

	private String createNextPageUrl(String url, int i) {
		return createNextPageUrl(url, i, 100);
	}

	private String createNextPageUrl(String url, int i, int count) {
		return url + "?s=" + i * count;
	}

	List<String> getDetailedPageUrl(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return getAbsUrls(doc.select(".content > p > a[href]"));
	}

	List<String> getNearCitys() throws IOException {
		Document doc = Jsoup.connect(url).get();
		Element nearBlock = doc.select(".acitem").first();
		Elements cityURLs = nearBlock.select("a[href]");
		return getAbsUrls(cityURLs);
	}

	private List<String> getAbsUrls(Elements urlElements) {
		List<String> result = new ArrayList<>();
		for (Element link : urlElements) {
			String href = link.attr("abs:href");
			result.add(href);
		}
		return result;
	}

	public void getDetailedInfo(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) throws IOException {
		CrawlCraigslist craigslist = new CrawlCraigslist();

		craigslist.getNearCitys();

		List<String> list = craigslist
				.getDetailedPageUrl("https://albanyga.craigslist.org/search/cta?s=800");
		System.out.println(list);
	}

}
