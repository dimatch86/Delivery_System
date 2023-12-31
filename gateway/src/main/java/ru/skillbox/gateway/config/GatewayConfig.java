package ru.skillbox.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.gateway.security.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Autowired
    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "order_route", r -> r.path("/api/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://ORDER-SERVICE")
                )
                .route(
                        "auth-service", r -> r.path("/auth/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service")
                )
                .route(
                        "payment-service", r -> r.path("/pay/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://payment-service")
                )
                .route(
                        "inventory-service", r -> r.path("/inv/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://inventory-service")
                )
                .route(
                        "delivery-service", r -> r.path("/ship/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://delivery-service")
                )
                .build();
    }
}
