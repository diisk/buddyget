package br.dev.diisk.infra.configurations.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocumentationConfiguration {

    @Bean
    OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .components(
                                                new Components()
                                                                .addSecuritySchemes(
                                                                                "bearer-key",
                                                                                new SecurityScheme()
                                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("bearer")
                                                                                                .bearerFormat("JWT")))
                                .info(
                                                new Info()
                                                                .title("Buddyget API")
                                                                .description(
                                                                                "Buddyget API Rest with clean architecture adaptation")
                                                                .contact(
                                                                                new Contact()
                                                                                                .name("Lucas Rafael")
                                                                                                .email("lucas-rafael2011@hotmail.com"))
                                // .license(
                                // new License()
                                // .name("Apache 2.0")
                                // .url("http://voll.med/api/licenca"))
                                );
        }

}
