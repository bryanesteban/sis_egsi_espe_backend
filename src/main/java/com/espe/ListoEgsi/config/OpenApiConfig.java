package com.espe.ListoEgsi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:9090}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";
        
        return new OpenAPI()
                .info(new Info()
                        .title("SIEGSI - Sistema de Implementación EGSI API")
                        .version("1.0.0")
                        .description("API REST para el Sistema de Implementación del Esquema de Gobierno y Seguridad de la Información (EGSI) de la ESPE.\n\n" +
                                "## Autenticación\n" +
                                "La mayoría de los endpoints requieren autenticación JWT. Para acceder:\n" +
                                "1. Utiliza el endpoint `/login` con credenciales válidas\n" +
                                "2. Copia el token JWT de la respuesta\n" +
                                "3. Haz clic en el botón 'Authorize' arriba\n" +
                                "4. Ingresa el token en el formato: `Bearer {token}`\n" +
                                "5. Haz clic en 'Authorize' y luego 'Close'\n\n" +
                                "## Módulos del Sistema\n" +
                                "El sistema está organizado en 9 fases de implementación EGSI, más módulos de autenticación y configuración.")
                        .contact(new Contact()
                                .name("ESPE - Sistema SIEGSI")
                                .email("soporte@espe.edu.ec"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("http://localhost:9090")
                                .description("Servidor Docker")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido del endpoint de login. " +
                                                "El formato debe ser: Bearer {token}")));
    }
}
