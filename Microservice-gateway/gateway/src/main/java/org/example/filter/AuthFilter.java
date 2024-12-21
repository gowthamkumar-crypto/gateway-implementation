package org.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.config.RedisHashComponent;
import org.example.constants.AppConstants;
import org.example.dto.ApiKey;
import org.example.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    RedisHashComponent redisHashComponent;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> apiKeysHeader = exchange.getRequest().getHeaders().get("gatewaykey");
        log.info("api key {}",apiKeysHeader);
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String routeId=route!=null?route.getId():null;

        if(routeId==null || CollectionUtils.isEmpty(apiKeysHeader) || !isAuthrization(routeId,apiKeysHeader.get(0)))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access denied");

        return chain.filter(exchange);
    }

    private boolean isAuthrization(String routeId,String apiKey){
        Object apiKeyObject = redisHashComponent.hGet(AppConstants.RECORD_KEY,apiKey);
        if(apiKeyObject != null){
            ApiKey key = MapperUtils.objectMapper(apiKeyObject, ApiKey.class);
            return key.services().contains(routeId);
        }
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
