package it.cb.reactive.internal.http;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import it.cb.reactive.http.web.Cookie;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.http.web.HttpMessage;
import it.cb.reactive.http.web.HttpRequest;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class HttpRequestImpl implements HttpRequest {

	private final Map<String, List<String>> _queryParams;

	public HttpRequestImpl(HttpServerRequest request) {
		_httpServerRequest = request;

		_queryParams = Collections.unmodifiableMap(
			new QueryStringDecoder(request.uri()).parameters());
	}

	@Override
	public String pathParam(CharSequence key) {
		return _httpServerRequest.param(key);
	}

	@Override
	public Map<String, String> pathParams() {

		Map<String, String> params = _httpServerRequest.params();

		return params == null ? Collections.emptyMap() : params;
	}

	@Override
	public Optional<String> firstParam(String key) {
		return _queryParams
			.getOrDefault(key, Collections.emptyList())
			.stream()
			.findFirst();
	}

	@Override
	public List<String> params(String key) {
		return Collections.unmodifiableList(
			_queryParams.getOrDefault(key, Collections.emptyList()));
	}

	@Override
	public Map<String, List<String>> params() {
		return _queryParams;
	}

	@Override
	public Iterable<Map.Entry<String, String>> requestHeaders() {
		return _httpServerRequest.requestHeaders();
	}

	@Override
	public String scheme() {
		return _httpServerRequest.scheme();
	}

	@Override
	public InetSocketAddress hostAddress() {
		return _httpServerRequest.hostAddress();
	}

	@Override
	public InetSocketAddress remoteAddress() {
		return _httpServerRequest.remoteAddress();
	}

	@Override
	public Map<CharSequence, Set<Cookie>> cookies() {
		//return _httpServerRequest.cookies(); TODO da implementare
		return Collections.emptyMap();
	}

	@Override
	public boolean isKeepAlive() {
		return _httpServerRequest.isKeepAlive();
	}

	@Override
	public boolean isWebsocket() {
		return _httpServerRequest.isWebsocket();
	}

	@Override
	public int method() {
		return _httpServerRequest.method() == HttpMethod.GET ? HttpHandler.GET
			: _httpServerRequest.method() == HttpMethod.POST ? HttpHandler.POST
			: _httpServerRequest.method() == HttpMethod.PATCH ? HttpHandler.PATCH
			: _httpServerRequest.method() == HttpMethod.PUT ? HttpHandler.PUT
			: _httpServerRequest.method() == HttpMethod.DELETE ? HttpHandler.DELETE
			: _httpServerRequest.method() == HttpMethod.OPTIONS ? HttpHandler.OPTIONS
			: 0;
	}

	@Override
	public String uri() {
		return _httpServerRequest.uri();
	}

	@Override
	public String version() {
		return _httpServerRequest.version().text();
	}

	@Override
	public Publisher<HttpMessage> receive() {
		return _httpServerRequest
			.receive()
			.retain()
			.map(HttpMessageImpl::new);
	}

	public HttpServerRequest getHttpServerRequest() {
		return _httpServerRequest;
	}

	private final HttpServerRequest _httpServerRequest;

}
