package pl.edu.pg.eti.aui.SocialPosting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class SocialPostingAPIGateway {
	public static void main(String[] args) {
		SpringApplication.run(SocialPostingAPIGateway.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("posts", r -> r.host("apigateway")
						.and()
						.path("/api/posts", "/api/posts/{id}", "/api/posts/by/{email}", "/api/posts/followed/{email}")
						.uri("http://postmicroservice:80"))
				.route("posts_v2", r -> r.host("localhost:8080")
						.and()
						.path("/api/posts", "/api/posts/{id}", "/api/posts/by/{email}", "/api/posts/followed/{email}")
						.uri("http://postmicroservice:80"))
				.route("users", r -> r.host("apigateway")
						.and()
						.path("/api/users", "/api/users/{email}", "/api/users/{email}/profile-pic",
								"/api/users/{email}/profile-pic/metadata", "/api/users/follow", "/api/users/unfollow",
								"/api/users/password/{email}", "/api/users/login")
						.uri("http://usermicroservice:80"))
				.route("users_v2", r -> r.host("localhost:8080")
						.and()
						.path("/api/users", "/api/users/{email}", "/api/users/{email}/profile-pic",
								"/api/users/{email}/profile-pic/metadata", "/api/users/follow", "/api/users/unfollow",
								"/api/users/password/{email}", "/api/users/login")
						.uri("http://usermicroservice:80"))
				.build();
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		final CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Collections.singletonList("*"));
		corsConfig.setMaxAge(3600L);
		corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		corsConfig.addAllowedHeader("*");

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}
}
