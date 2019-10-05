package it.cb.reactive.internal.http;

import io.netty.handler.codec.http.HttpMethod;
import it.cb.reactive.http.web.Cookie;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.http.web.HttpRequest;
import reactor.netty.http.server.HttpServerRequest;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class HttpRequestImpl implements HttpRequest {

	public HttpRequestImpl(HttpServerRequest request) {
		_httpServerRequest = request;
	}

	@Override
	public String param(CharSequence key) {
		return _httpServerRequest.param(key);
	}

	@Override
	public Map<String, String> params() {
		return _httpServerRequest.params();
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

	public HttpServerRequest getHttpServerRequest() {
		return _httpServerRequest;
	}

	private final HttpServerRequest _httpServerRequest;

}
