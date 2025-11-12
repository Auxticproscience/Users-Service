package com.userService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI intranetUserServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Intranet User Service API")
                        .description("Servicio de gestión de usuarios para la intranet corporativa. " +
                                "Maneja guardado, actualiziacion, eliminación y visualización de usuarios. ")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Luis Eduardo Velez Posada")
                                .email("luiseduardovelez88@gmail.com")
                                .url("https://github.com/LuisVelez1")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Desarrollo - SERVICE "),
                        new Server().url("http://localhost:8080").description("Desarrollo - API Gateway"),
                        new Server().url("https://api.empresa.com").description("Producción")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        ));
    }
}
