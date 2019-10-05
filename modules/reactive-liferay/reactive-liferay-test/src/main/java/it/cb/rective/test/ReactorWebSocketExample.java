package it.cb.rective.test;

import it.cb.reactive.http.socket.WebSocketHandler;
import it.cb.reactive.http.socket.WebSocketMessage;
import it.cb.reactive.http.socket.WebSocketSession;
import it.cb.reactive.http.web.Endpoint;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Component(
	immediate = true,
	service = Endpoint.class
)
public class ReactorWebSocketExample implements WebSocketHandler {

	@Override
	public Publisher<Void> apply(WebSocketSession webSocketSession) {
		return webSocketSession.send(
			Flux.from(webSocketSession.receive())
				.map(WebSocketMessage::getPayloadAsString)
				.map(s -> new StringBuilder(s).reverse().toString())
				.map(webSocketSession::textMessage)
		);
	}

	@Override
	public String getPath() {
		return "/reactor-test";
	}
}
