package com.onlinestore.shopping_carts_service.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI shoppingCartOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shopping Cart API")
                        .description("REST API for shopping cart management. " +
                                "Allows creating carts, associating them with users, and managing products with external service integration.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .email("rmatias.dev@gmail.com")
                                .name("Matias Alejandro Rodriguez")
                                .url("https://www.linkedin.com/in/matias-rodriguez-alejandro/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Git Hub Documentation")
                        .url("https://github.com/matias-devv/online-store-shopping-carts-service"))
                .servers(List.of(
                        new Server().url("http://localhost:8085").description("Development Server")
                ));
    }
}
