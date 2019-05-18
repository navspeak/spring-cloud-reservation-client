package com.example.reservationclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.stream.Collectors;

//Edge service
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {
	@Bean
	RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}


@RestController
@RequestMapping("/reservations")
class ReservationApiGatewayRestController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(method = RequestMethod.GET, value = "/names")
	private Collection<String> getReservationNames() {

		//Type token parameter
		ParameterizedTypeReference<Resources<Reservation>> ptr =
				new ParameterizedTypeReference<Resources<Reservation>>() {};
		// FIX FIX FIX - ain't able to resolve the URL
		ResponseEntity<Resources<Reservation>> entity = this.restTemplate.
				exchange("http://reservation-service/reservations", HttpMethod.GET, null, ptr);
		return entity
				.getBody()
				.getContent()
				.stream()
				.map(Reservation::getReservation)
				.collect(Collectors.toList());
	}

}

class Reservation {
	private String reservation;
	public String getReservation() {
		return reservation;
	}
}