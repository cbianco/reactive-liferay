package it.cb.reactive.http.web;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface HttpMessage {

	String getPayloadAsString(Charset charset);

	String getPayloadAsString();

	ByteBuffer getPayload();

}
