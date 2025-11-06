package com.reon.order_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customSwaggerConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("Real Time Order Tracking System")
                        .description("The project flow is very simple, when a new user visits our website, user registers " +
                                "followed by login, then he can place orders - track them - fetch all the order and so on " +
                                "If admin loggedIn then, operations will be different as compared to normal user. " +
                                "Like fetch user via id/email, fetch all users.")
                        .summary("A order tracking system that simulates e-commerce logistics workflows to deliver instant order status updates.")
                        .version("1.0"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ))
                .tags(List.of(
                        new Tag().name("Auth APIs"),
                        new Tag().name("Order APIs"),
                        new Tag().name("Admin APIs")
                ))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components().addSecuritySchemes("BearerAuth",
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
