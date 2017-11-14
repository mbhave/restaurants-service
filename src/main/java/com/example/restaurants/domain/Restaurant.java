package com.example.restaurants.domain;

/**
 * @author Madhura Bhave
 */
public class Restaurant {

	private String id;

	private String name;

	private String address;

	private String price;

	private String image_url;

	private Double distanceFromRefPoint;

	private Double lat;

	private Double lng;

	public Restaurant() {
	}

	public Restaurant(String id, String name, String address, String price,
			String image_url, Double lat, Double lng) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.price = price;
		this.image_url = image_url;
		this.lat = lat;
		this.lng = lng;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public Double getDistanceFromRefPoint() {
		return distanceFromRefPoint;
	}

	public void setDistanceFromRefPoint(Double distanceFromRefPoint) {
		this.distanceFromRefPoint = distanceFromRefPoint;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
}
