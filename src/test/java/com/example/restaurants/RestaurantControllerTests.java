package com.example.restaurants;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.example.restaurants.domain.Restaurant;
import com.example.restaurants.service.RestaurantProperties;
import com.example.restaurants.service.RestaurantProperties.ReferencePoint;
import com.example.restaurants.service.RestaurantService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebFluxTest
public class RestaurantControllerTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private RestaurantService restaurantService;

	@MockBean
	private RestaurantProperties restaurantProperties;

	private ObjectMapper objectMapper = new ObjectMapper();

	private final Coordinates coordinatesWithinFiveMiles = new Coordinates(37.787725, -122.452758);

	private final Coordinates coordinatesOutsideFiveMiles = new Coordinates(37.762389, -122.506833);

	@Test
	public void getRestaurantsShouldFilterRestaurantsByDistance() throws Exception {
		Restaurant restaurant1 = new Restaurant("id-1", "Spring Food",
				"475 Howard St", "2", "http://spring-food.jpeg",
				this.coordinatesWithinFiveMiles.getLatitude(), this.coordinatesWithinFiveMiles.getLongitude());
		Restaurant restaurant2 = new Restaurant("id-2", "Spring Rolls",
				"569 Geary St", "1", "http://spring-rolls.jpeg",
				this.coordinatesOutsideFiveMiles.getLatitude(), this.coordinatesOutsideFiveMiles.getLongitude());
		given(this.restaurantService.getData()).willReturn(Flux.just(restaurant1, restaurant2));
		given(this.restaurantProperties.getReferencePoint()).willReturn(ReferencePoint.from(37.784172, -122.401558));
		EntityExchangeResult<byte[]> result = this.webTestClient.get().uri("/").accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk().expectBody()
				.returnResult();
		List<Restaurant> restaurants = this.objectMapper.readValue(result.getResponseBody(), new TypeReference<List<Restaurant>>() {});
		assertThat(restaurants.size()).isEqualTo(1);
		assertThat(restaurants.get(0).getId()).isEqualTo("id-1");
	}

	@Test
	public void getRestaurantsShouldReturnRestaurantsSortedByPrice() throws Exception {
		Restaurant[] restaurants = getRestaurants();
		given(this.restaurantService.getData()).willReturn(Flux.just(restaurants));
		given(this.restaurantProperties.getReferencePoint()).willReturn(ReferencePoint.from(37.784172, -122.401558));
		this.webTestClient.get().uri("/").accept(MediaType.APPLICATION_JSON)
				.exchange().expectStatus().isOk().expectBody()
				.json(this.objectMapper.writeValueAsString(getSortedRestaurantList(restaurants)));
	}

	private Restaurant[] getRestaurants() {
		Restaurant[] restaurants = new Restaurant[5];
		for (int i = 0; i < restaurants.length; i++) {
			restaurants[i] = new Restaurant(Integer.toString(i), "Spring Food " + i, "Moscone",
					Integer.toString(new Random().nextInt(3) + 1), "http://image-url.com",
					coordinatesWithinFiveMiles.getLatitude(), coordinatesWithinFiveMiles.getLongitude());
		}
		return restaurants;
	}

	private List<Restaurant> getSortedRestaurantList(Restaurant[] restaurants) {
		List<Restaurant> restaurantsList = Arrays.asList(restaurants);
		restaurantsList.sort(Comparator.comparing(Restaurant::getPrice));
		return restaurantsList;
	}

	private class Coordinates {

		private Double latitude;

		private Double longitude;

		public Coordinates(Double latitude, Double longitude) {
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
	}

}
