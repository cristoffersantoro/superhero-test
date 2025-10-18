package com.example.superhero.infrastructure.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Superhero API",
                version = "v1",
                description = "API de Heróis e Superpoderes",
                contact = @Contact(name = "Teste", email = "cristoffer@cristoffer.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        }
)
@Configuration
public class OpenAPIConfig {

}
