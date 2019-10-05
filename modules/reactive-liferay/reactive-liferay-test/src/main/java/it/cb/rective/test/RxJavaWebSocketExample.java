package it.cb.rective.test;

import io.reactivex.Flowable;
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
public class RxJavaWebSocketExample implements WebSocketHandler {

	@Override
	public Publisher<Void> apply(WebSocketSession webSocketSession) {
		return webSocketSession.send(
			Flowable.fromPublisher(webSocketSession.receive())
				.map(WebSocketMessage::getPayloadAsString)
				.map(s -> new StringBuilder(s).reverse().toString())
				.map(webSocketSession::textMessage)
		);
	}

	@Override
	public String getPath() {
		return "/rxjava-test";
	}
}
