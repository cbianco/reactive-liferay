package it.cb.reactive.http.socket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface WebSocketMessage {

	String getPayloadAsString(Charset charset);

	String getPayloadAsString();

	ByteBuffer getPayload();

}
