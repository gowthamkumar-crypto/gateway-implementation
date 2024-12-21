package org.example;

import jakarta.annotation.PostConstruct;
import org.example.config.RedisHashComponent;
import org.example.constants.AppConstants;
import org.example.dto.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot .SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
@SpringBootApplication
public class GatewayApplication {
    @Autowired
    private RedisHashComponent redisHashComponent;

    @PostConstruct
    public void initKeysToRedis(){
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("343C-ED0B-4137-B27E", Stream.of(AppConstants.STUDENT_SERVICE_KEY,
                AppConstants.COURSE_SERVICE_KEY).collect(Collectors.toList())));
        apiKeyList.add(new ApiKey("FA48-EF0C-427E-8CCF", Stream.of(AppConstants.COURSE_SERVICE_KEY).collect(Collectors.toList())));
        List<Object> lists = redisHashComponent.hValues(AppConstants.RECORD_KEY);
        if (lists.isEmpty()) {
            apiKeyList.forEach(k -> redisHashComponent.hSet(AppConstants.RECORD_KEY, k.key(), k));
        }
    }

    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder builder){
        return builder.routes().route(AppConstants.STUDENT_SERVICE_KEY,r->r.path("/api/service-01/**").filters(f->f.stripPrefix(2)).uri("http://localhost:8081"))
                .route(AppConstants.COURSE_SERVICE_KEY,r->r.path("/api/service-02/**").filters(f->f.stripPrefix(2)).uri("http://localhost:8082"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}