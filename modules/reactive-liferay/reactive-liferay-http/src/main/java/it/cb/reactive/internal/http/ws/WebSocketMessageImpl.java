package it.cb.reactive.internal.http.ws;

import io.netty.buffer.ByteBuf;
import it.cb.reactive.http.socket.WebSocketMessage;
import it.cb.reactive.internal.http.HttpMessageImpl;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class WebSocketMessageImpl
	extends HttpMessageImpl implements WebSocketMessage {

	WebSocketMessageImpl(ByteBuf byteBuf) {
		super(byteBuf);
	}
}
