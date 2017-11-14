package com.example.restaurants.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "restaurants")
public class RestaurantProperties {

	private String city = "San Francisco";

	private ReferencePoint referencePoint = new ReferencePoint();

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ReferencePoint getReferencePoint() {
		return this.referencePoint;
	}

	public void setReferencePoint(ReferencePoint referencePoint) {
		this.referencePoint = referencePoint;
	}

	public static class ReferencePoint {

		private Double latitude = 37.784172;

		private Double longitude = -122.401558;

		public ReferencePoint() {
		}

		public ReferencePoint(Double latitude, Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public Double getLatitude() {
			return latitude;
		}

		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}

		public static ReferencePoint from(Double latitude, Double longitude) {
			return new ReferencePoint(latitude, longitude);
		}
	}

}
