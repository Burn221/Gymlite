package ru.burn221.gymlite.configuration;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI gymliteOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Gymlite API specification")
                        .description("REST API for booking,equipment and zones in gym")
                        .version("v 1"))
                .externalDocs(new ExternalDocumentation()
                        .description("README file for REST API")
                        .url("https://github.com/Burn221/Gymlite/blob/main/README.md"));


    }
}
