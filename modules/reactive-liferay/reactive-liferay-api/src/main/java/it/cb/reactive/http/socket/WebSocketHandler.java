package it.cb.reactive.http.socket;

import it.cb.reactive.http.web.Endpoint;
import org.reactivestreams.Publisher;

import java.util.function.Function;

public interface WebSocketHandler
	extends Function<WebSocketSession, Publisher<Void>>, Endpoint {

	default String getProtocols() {
		return null;
	}

	default int getMaxFramePayloadLength() {
		return 65536;
	}

}
