package org.feather.crawler;

import java.util.Map;

public class DetailedInfo {

	private String title;
	private String description;
	private String carName;
	private long postedTime;
	private long updatedTime;
	private double price;
	private Map<String, String> carInfo;

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("title: ").append(title).append("---");
		result.append("price: ").append(price);
		return result.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public long getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(long postedTime) {
		this.postedTime = postedTime;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Map<String, String> getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(Map<String, String> carInfo) {
		this.carInfo = carInfo;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
