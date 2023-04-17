package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableOpenApi
public class Swagger3Config {
    /**
     * Swagger3 API location :http://localhost:5100/swagger-ui/index.html
     *
     * @return
     */

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.web.rest"))
                .paths(PathSelectors.any())
                .build()
                .protocols(newHashSet("https", "http"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Simple Rate Limiter API doc")
                .description("Simple-Rate-Limiter API documentation")
                .version("v1.0.0")
                .build();
    }

    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new HashSet<>(Arrays.asList(ts));
        }
        return null;
    }
}
