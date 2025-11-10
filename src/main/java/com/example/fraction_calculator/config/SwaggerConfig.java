package com.example.fraction_calculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Fraction Calculator API")
                .version("1.0.0")
                .description("API tính toán phân số: cộng, trừ, nhân, chia")
                .contact(new Contact()
                    .name("Nhóm 6")
                    .email("nhom6@example.com")));
    }
}