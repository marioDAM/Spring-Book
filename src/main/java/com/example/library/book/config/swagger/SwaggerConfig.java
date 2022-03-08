package com.example.library.book.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

// http://localhost:6969/swagger-ui/index.html
// Importante con el nuevo Swagger3 y Spring 2.6.x
@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.example.library.book.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }
    private ApiInfo metaInfo() {

        return new ApiInfo(
                "API REST Book library 2DAM 2021/2022",
                "Desarrollo de una api de una biblioteca para ser consumida por una aplicación Android por un cliente",
                "1.1",
                "Terms of Service",
                new Contact("Mario Valverde Camaño", "https://github.com/marioDAM",
                        "valverdemario.2410@gmail.com"),
                "MIT",
                "https://github.com/marioDAM/Spring-Book", new ArrayList<>()
        );
    }
}