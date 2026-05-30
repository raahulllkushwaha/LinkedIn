package com.rahul.apigateway.filter;

import com.rahul.apigateway.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final JwtService jwtService;

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {

        log.info("Auth request: {}", request.uri());

        final String tokenHeader = request.headers().firstHeader("Authorization");

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        final String token = tokenHeader.split("Bearer ")[1];

        try {
            String userId = jwtService.getUserIdFromToken(token);
            ServerRequest mutatedRequest = ServerRequest.from(request)
                    .header("X-User-Id", userId)
                    .build();
            return next.handle(mutatedRequest);
        } catch (JwtException e) {
            log.error("JWT Exception {}", e.getLocalizedMessage());
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}