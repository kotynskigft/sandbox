package com.example.demo;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import lombok.val;

@Configuration
public class WebSocketConfiguration {
 
    @Bean
    WebSocketHandlerAdapter wsha() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler wsh() {
        return new MyWebSocketHandler();
    }

    @Bean
    HandlerMapping hm () {
        val suhm = new SimpleUrlHandlerMapping();
        suhm.setOrder(10);
        suhm.setUrlMap(Collections.singletonMap("/ws/oracle", wsh()));
        return suhm;
    }
}