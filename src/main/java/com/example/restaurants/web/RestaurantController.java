package com.example.restaurants.web;

import java.util.Comparator;

import com.example.restaurants.domain.Restaurant;
import com.example.restaurants.service.RestaurantProperties;
import com.example.restaurants.service.RestaurantService;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController {

	private RestaurantService service;

	private final RestaurantProperties properties;

	public RestaurantController(RestaurantService service, RestaurantProperties properties) {
		this.service = service;
		this.properties = properties;
	}

	@RequestMapping("/")
	public Flux<Restaurant> getRestaurants() {
		return this.service.getData()
					.map(restaurant -> {
						restaurant.setDistanceFromRefPoint(getDistance(restaurant.getLat(), restaurant.getLng()));
						return restaurant;
					})
					.filter(restaurant -> restaurant.getDistanceFromRefPoint() < 5)
					.sort(Comparator.comparing(Restaurant::getPrice));
	}

	public double getDistance(double lat1, double lon2) {
		final int R = 6371;
		double latDistance = Math.toRadians(lat1 - this.properties.getReferencePoint().getLatitude());
		double lonDistance = Math.toRadians(lon2 - this.properties.getReferencePoint().getLongitude());
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat1))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000;
		distance = Math.pow(distance, 2) + Math.pow(0, 2);
		return Math.sqrt(distance) * 0.000621371;
	}

}
