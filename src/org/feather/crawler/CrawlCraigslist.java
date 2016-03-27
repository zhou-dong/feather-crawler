package org.feather.crawler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private long getTime(String source) {
		source = source.replace("T", " ");
		source = source.replace("-0500", "");
		try {
			return sdf.parse(source).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0l;
		}
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

	public DetailedInfo getDetailedInfo(String url) {
		Document doc = null;
		DetailedInfo info = new DetailedInfo();
		try {
			doc = Jsoup.connect(url).get();

			Element idElement = doc.select("#titletextonly").first();

			String id = "";
			if (idElement != null) {
				id = idElement.text();
			}

			Element priceElement = doc.select(".postingtitletext > .price").first();

			String price = "";
			if (priceElement != null) {
				price = priceElement.text();
				if (price.startsWith("$")) {
					price = price.substring(1);
				}
			}

			Elements attrgroup = doc.select(".attrgroup");

			Element titleElement = attrgroup.first().select("span > b").first();
			String title = "";
			if (titleElement != null) {
				title = titleElement.text();
			}

			Elements carDetails = attrgroup.get(1).select("span");

			Element description = doc.select("#postingbody").first();
			if (description != null) {
				info.setDescription(description.text());
			}

			Elements times = doc.select(".postinginfo > time");

			String updateTime = "";
			if (times.size() > 2) {
				updateTime = times.get(2).attr("datetime");
				info.setUpdatedTime(getTime(updateTime));
			}

			String postTime = times.get(1).attr("datetime");

			Map<String, String> carInfo = new HashMap<>();
			for (Element detail : carDetails) {
				String[] pair = detail.text().split(":");
				String key = pair[0].trim();
				String val = pair[1].trim();
				carInfo.put(key, val);

			}

			info.setCarInfo(carInfo);
			info.setCarName(title);
			info.setTitle(id);

			if (!price.isEmpty()) {
				info.setPrice(Double.parseDouble(price));
			}

			info.setPostedTime(getTime(postTime));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public static void main(String[] args) throws IOException {
		CrawlCraigslist craigslist = new CrawlCraigslist();

		List<String> cityUrls = craigslist.getNearCitys();

		for (String cityUrl : cityUrls) {
			String autoUrl = craigslist.createAutoSearchUrl(cityUrl);
			for (int i = 0; i < 20; i++) {
				autoUrl = craigslist.createNextPageUrl(autoUrl, i);
				List<String> pages = craigslist.getDetailedPageUrl(autoUrl);
				if (pages.isEmpty())
					break;
				for (String page : pages) {
					DetailedInfo info = craigslist.getDetailedInfo(page);
					System.out.println(info);
				}
			}
		}
	}

}
