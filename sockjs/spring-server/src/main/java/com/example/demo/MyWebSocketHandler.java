package com.example.demo;


import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
public class MyWebSocketHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(final WebSocketSession session) {

        session.receive().subscribe(
            it -> {
                log.info(it.getPayloadAsText());
                session.send(Mono.just(session.textMessage("42"))).subscribe();
            },
            null,
            () -> log.info("WebSocket session finished")
        );

        // returns 'never' to prevent immediate closing the session.
		return Mono.never();
	}

}