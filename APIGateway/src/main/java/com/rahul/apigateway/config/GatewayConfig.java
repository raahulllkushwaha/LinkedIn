package com.rahul.apigateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return GatewayRouterFunctions.route("user-service")
                .route(RequestPredicates.path("/api/v1/users/**"),
                        HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("USERSERVICE"))
                .before(BeforeFilterFunctions.rewritePath("/api/v1/users/(?<segment>.*)", "/users/${segment}"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> postServiceRoute() {
        return GatewayRouterFunctions.route("posts-service")
                .route(RequestPredicates.path("/api/v1/posts/**"),
                        HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("POSTSERVICE"))
                .before(BeforeFilterFunctions.rewritePath("/api/v1/posts/(?<segment>.*)", "/posts/${segment}"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> connectionsServiceRoute() {
        return GatewayRouterFunctions.route("connections-service")
                .route(RequestPredicates.path("/api/v1/connections/**"),
                        HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("CONNECTIONSSERVICE"))
                .before(BeforeFilterFunctions.rewritePath("/api/v1/connections/(?<segment>.*)", "/connections/${segment}"))
                .build();
    }
}