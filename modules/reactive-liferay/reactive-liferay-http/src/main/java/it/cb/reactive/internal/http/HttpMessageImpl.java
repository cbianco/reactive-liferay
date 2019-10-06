package it.cb.reactive.internal.http;

import io.netty.buffer.ByteBuf;
import it.cb.reactive.http.web.HttpMessage;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpMessageImpl implements HttpMessage {

	public HttpMessageImpl(ByteBuf byteBuf) {
		_byteBuf = byteBuf;
	}

	@Override
	public String getPayloadAsString(Charset charset) {
		return _byteBuf.toString(charset);
	}

	@Override
	public String getPayloadAsString() {
		return _byteBuf.toString(StandardCharsets.UTF_8);
	}

	@Override
	public ByteBuffer getPayload() {
		return _byteBuf.nioBuffer();
	}

	public ByteBuf getByteBuf() {
		return _byteBuf.duplicate().retain();
	}

	private final ByteBuf _byteBuf;
}
