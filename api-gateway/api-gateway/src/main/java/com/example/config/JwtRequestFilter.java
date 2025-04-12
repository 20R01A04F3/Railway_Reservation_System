package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.example.dto.ErrorResponse;
import com.example.feign.AuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;

@EnableFeignClients(basePackages = "com.*")
@Component
@Slf4j
public class JwtRequestFilter implements WebFilter {

    private final AuthServiceClient authServiceClient;
    private final ObjectMapper objectMapper;
    private final String secretKey = "your-secret-key"; // Use a secure key in production

    @Autowired
    public JwtRequestFilter(AuthServiceClient authServiceClient, ObjectMapper objectMapper) {
        this.authServiceClient = authServiceClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.warn("In JWT Filter");
        System.out.println("************* Filter ****************");
        String requestTokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        System.out.println(requestTokenHeader);
//    7
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);

            // Validate the token and get the user details
            // Wrap the blocking call in a Mono
            return Mono.fromCallable(() -> authServiceClient.validateToken("Bearer " + jwtToken))
                    .flatMap(validationResponse -> {
                        // Map the single role from ValidationResponse to SimpleGrantedAuthority
                        SimpleGrantedAuthority authority = mapRoleToAuthority(validationResponse.role());

                        // Set the security context with user details and role
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                                        new UsernamePasswordAuthenticationToken(
                                                validationResponse.username(),
                                                null,
                                                Collections.singleton(authority)
                                        )
                                ));
                    })
                    .onErrorResume(e -> {
                        log.error("JWT Filter Error");
                        e.printStackTrace();
                        return createErrorResponse(exchange, "Unauthorized: Invalid or expired token", HttpStatus.UNAUTHORIZED);
                    });
        }
        log.error(requestTokenHeader+" hero");
        return chain.filter(exchange);
    }

    private SimpleGrantedAuthority mapRoleToAuthority(String role) {
        switch (role) {
            case "ADMIN":
                return new SimpleGrantedAuthority("ROLE_ADMIN");
            case "USER":
                return new SimpleGrantedAuthority("ROLE_USER");
            default:
                return new SimpleGrantedAuthority("ROLE_UNKNOWN");
        }
    }
    private Mono<Void> createErrorResponse(ServerWebExchange exchange, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, status.value());

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
