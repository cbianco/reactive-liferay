package it.cb.reactive.internal;

import it.cb.reactive.http.socket.WebSocketHandler;
import it.cb.reactive.http.web.Endpoint;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.internal.http.HttpRequestImpl;
import it.cb.reactive.internal.http.HttpResponseImpl;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.function.Predicate;

import static it.cb.reactive.internal.util.HttpServerOperationsUtil.withWebsocketSupport;
import static it.cb.reactive.internal.ws.WebSocketSessionFactory.createWebSocketSession;

public abstract class HttpEnpointRoutes<T extends Endpoint> {

	private final T _endpoint;

	private final Predicate<HttpServerRequest> _condition;

	protected HttpEnpointRoutes(
		T endpoint,
		Predicate<HttpServerRequest> condition) {
		_endpoint = endpoint;
		_condition = condition;
	}

	public T getEndpoint() {
		return _endpoint;
	}

	public Predicate<HttpServerRequest> getCondition() {
		return _condition;
	}

	public abstract boolean isWebSocket();

	public abstract boolean isNotWebSocket();

	public abstract Publisher<Void> handle(
		HttpServerRequest request, HttpServerResponse response);

	private static class WebSocketRoutes extends HttpEnpointRoutes<WebSocketHandler> {

		protected WebSocketRoutes(
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

		protected NoWebSocketRoutes(
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
			HttpServerRequest request, HttpServerResponse response) {

			return getEndpoint().apply(
				new HttpRequestImpl(request), new HttpResponseImpl(response));
		}

	}

	public static HttpEnpointRoutes ws(
		WebSocketHandler endpoint, Predicate<HttpServerRequest> condition) {
		return new WebSocketRoutes(endpoint, condition);
	}

	public static HttpEnpointRoutes noWs(
		HttpHandler endpoint, Predicate<HttpServerRequest> condition) {
		return new NoWebSocketRoutes(endpoint, condition);
	}

}
