package it.cb.reactive.http.socket;

import org.reactivestreams.Publisher;

import java.nio.ByteBuffer;

public interface WebSocketSession {

	Publisher<Void> send(Publisher<WebSocketMessage> message);

	Publisher<WebSocketMessage> receive();

	WebSocketMessage textMessage(String payload);

	WebSocketMessage byteMessage(ByteBuffer byteBuffer);

	WebSocketMessage pingMessage(ByteBuffer byteBuffer);

	WebSocketMessage pongMessage(ByteBuffer byteBuffer);

	String getSessionId();

	Publisher<Void> close(CloseStatus status);

}
