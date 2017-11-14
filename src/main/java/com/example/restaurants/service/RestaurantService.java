package com.example.restaurants.service;

import java.util.List;

import com.example.restaurants.domain.Restaurant;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RestaurantService {

	private static final String RESTAURANTS_URL = "http://opentable.herokuapp.com/api/restaurants?city={city}&per_page=100";

	private final WebClient webClient;

	private final RestaurantProperties restaurantProperties;

	public RestaurantService(WebClient.Builder webClientBuilder, RestaurantProperties restaurantProperties) {
		this.webClient = webClientBuilder.build();
		this.restaurantProperties = restaurantProperties;
	}

	public Flux<Restaurant> getData() {
		return this.webClient.get().uri(RESTAURANTS_URL, this.restaurantProperties.getCity())
				.retrieve().bodyToMono(RestaurantsResponse.class)
				.flatMapIterable(RestaurantsResponse::getRestaurants);
	}

	static class RestaurantsResponse {

		private List<Restaurant> restaurants;

		public List<Restaurant> getRestaurants() {
			return restaurants;
		}

		public void setRestaurants(List<Restaurant> restaurants) {
			this.restaurants = restaurants;
		}
	}

}
