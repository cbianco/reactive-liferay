package it.cb.reactive.internal.http;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import it.cb.reactive.http.web.Cookie;
import it.cb.reactive.http.web.HttpResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class HttpResponseImpl implements HttpResponse {

	private HttpServerResponse _httpServerResponse;

	public HttpResponseImpl(HttpServerResponse httpServerResponse) {
		_httpServerResponse = httpServerResponse;
	}

	@Override
	public HttpResponse addCookie(Cookie cookie) {
		//TODO da implementare
		return this;
	}

	@Override
	public HttpResponse addHeader(CharSequence name, CharSequence value) {
		_httpServerResponse.addHeader(name, value);
		return this;
	}

	@Override
	public HttpResponse chunkedTransfer(boolean chunked) {
		_httpServerResponse.chunkedTransfer(chunked);
		return this;
	}

	@Override
	public HttpResponse compression(boolean compress) {
		_httpServerResponse.compression(compress);
		return this;
	}

	@Override
	public boolean hasSentHeaders() {
		return _httpServerResponse.hasSentHeaders();
	}

	@Override
	public HttpResponse header(CharSequence name, CharSequence value) {
		_httpServerResponse.header(name, value);
		return this;
	}

	@Override
	public HttpResponse headers(
		Iterable<Map.Entry<String, String>> headers) {

		_httpServerResponse.headers(
			StreamSupport.stream(headers.spliterator(), false)
				.reduce(
					(HttpHeaders)new DefaultHttpHeaders(),
					(u, t) -> u.set(t.getKey(), t.getValue()),
					HttpHeaders::add));

		return this;
	}

	@Override
	public HttpResponse keepAlive(boolean keepAlive) {
		_httpServerResponse.keepAlive(keepAlive);
		return this;
	}

	@Override
	public Iterable<Map.Entry<String, String>> responseHeaders() {
		return _httpServerResponse.responseHeaders();
	}

	@Override
	public Publisher<Void> send() {
		return _httpServerResponse.send();
	}

	@Override
	public Publisher<Void> sendHeaders() {
		return _httpServerResponse.sendHeaders();
	}

	@Override
	public Publisher<Void> sendNotFound() {
		return _httpServerResponse.sendNotFound();
	}

	@Override
	public Publisher<Void> sendRedirect(String location) {
		return _httpServerResponse.sendRedirect(location);
	}

	@Override
	public HttpResponse sse() {
		_httpServerResponse.sse();
		return this;
	}

	@Override
	public String status() {
		return _httpServerResponse.status().toString();
	}

	@Override
	public HttpResponse status(int status) {
		_httpServerResponse.status(status);
		return this;
	}

	@Override
	public Publisher<Void> send(
		Publisher<? extends ByteBuffer> dataStream) {
		return _httpServerResponse.send(
			Flux.from(dataStream)
				.map(Unpooled::wrappedBuffer));
	}

	@Override
	public Publisher<Void> send(
		Publisher<? extends ByteBuffer> dataStream,
		Predicate<ByteBuffer> predicate) {
		return _httpServerResponse.send(
			Flux.from(dataStream).map(Unpooled::wrappedBuffer),
			s -> predicate.test(s.nioBuffer()));
	}

	@Override
	public Publisher<Void> sendByteArray(
		Publisher<? extends byte[]> dataStream) {
		return _httpServerResponse.sendByteArray(dataStream);
	}

	@Override
	public Publisher<Void> sendFile(Path file) {
		return _httpServerResponse.sendFile(file);
	}

	@Override
	public Publisher<Void> sendFile(Path file, long position, long count) {
		return _httpServerResponse.sendFile(file, position, count);
	}

	@Override
	public Publisher<Void> sendObject(
		Publisher<?> dataStream, Predicate<Object> predicate) {
		return _httpServerResponse.sendObject(dataStream, predicate);
	}

	@Override
	public Publisher<Void> sendObject(Object message) {
		return _httpServerResponse.sendObject(message);
	}

	@Override
	public Publisher<Void> sendString(
		Publisher<? extends String> dataStream, Charset charset) {
		return _httpServerResponse.sendString(dataStream, charset);
	}

	public HttpServerResponse getHttpServerResponse() {
		return _httpServerResponse;
	}

}
