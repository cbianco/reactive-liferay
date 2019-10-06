package it.cb.reactive.internal.http;

import it.cb.reactive.http.socket.WebSocketHandler;
import it.cb.reactive.http.web.Endpoint;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.internal.http.util.ClassLoaderUtil;
import it.cb.reactive.internal.http.util.Predicates;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static it.cb.reactive.internal.http.util.HttpServerOperationsUtil.withWebsocketSupport;
import static it.cb.reactive.internal.http.ws.WebSocketSessionFactory.createWebSocketSession;

public abstract class HttpEnpointRoutes<T extends Endpoint> {

	private final T _endpoint;

	private final Predicate<HttpServerRequest> _condition;

	HttpEnpointRoutes(
		T endpoint,
		Predicate<HttpServerRequest> condition) {
		_endpoint = endpoint;
		_condition = condition;
	}

	T getEndpoint() {
		return _endpoint;
	}

	Predicate<HttpServerRequest> getCondition() {
		return _condition;
	}

	public abstract boolean isWebSocket();

	public abstract boolean isNotWebSocket();

	public abstract Publisher<Void> handle(
		HttpServerRequest request, HttpServerResponse response);

	private static class WebSocketRoutes extends HttpEnpointRoutes<WebSocketHandler> {

		WebSocketRoutes(
			WebSocketHandler endpoint,
			Predicate<HttpServerRequest> condition) {
			super(endpoint, condition);
		}

		@Override
		public boolean isWebSocket() {
			return true;
		}

		@Override
		public boolean isNotWebSocket() {
			return !isWebSocket();
		}

		@Override
		public Publisher<Void> handle(
			HttpServerRequest request, HttpServerResponse response) {

			return withWebsocketSupport(
				request, getEndpoint().getPath(), getEndpoint().getProtocols(),
				getEndpoint().getMaxFramePayloadLength(),
				(in, out) ->
					getEndpoint().apply(createWebSocketSession(in, out)));

		}

	}

	private static class NoWebSocketRoutes extends HttpEnpointRoutes<HttpHandler> {

		NoWebSocketRoutes(
			HttpHandler endpoint,
			Predicate<HttpServerRequest> condition) {
			super(endpoint, condition);
		}

		@Override
		public boolean isWebSocket() {
			return false;
		}

		@Override
		public boolean isNotWebSocket() {
			return !isWebSocket();
		}

		@Override
		public Publisher<Void> handle(
			final HttpServerRequest request, HttpServerResponse response) {

			Arrays
				.stream(Predicates.getPredicateArray(getCondition()))
				.filter(p -> p.test(request))
				.findFirst()
				.filter(s ->
					_httpPredicateClass
						.isAssignableFrom(s.getClass()))
				.ifPresent(p -> request.paramsResolver(
					(Function<String, Map <String, String>>)p));

			return getEndpoint().apply(
				new HttpRequestImpl(request), new HttpResponseImpl(response));
		}

	}

	static HttpEnpointRoutes ws(
		WebSocketHandler endpoint, Predicate<HttpServerRequest> condition) {
		return new WebSocketRoutes(endpoint, condition);
	}

	static HttpEnpointRoutes noWs(
		HttpHandler endpoint, Predicate<HttpServerRequest> condition) {
		return new NoWebSocketRoutes(endpoint, condition);
	}

	private static final Class<?> _httpPredicateClass;

	static {
		try {
			_httpPredicateClass =
				ClassLoaderUtil.getClassLoader().loadClass(
					"reactor.netty.http.server.HttpPredicate");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
