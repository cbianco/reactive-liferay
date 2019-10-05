package it.cb.reactive.http.web;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.Set;

public interface HttpRequest {

	/**
	 * URI parameter captured via {} "/test/{var}"
	 *
	 * @param key param var name
	 *
	 * @return the param captured value
	 */
	String param(CharSequence key);

	/**
	 * Returns the param captured key/value map
	 *
	 * @return the param captured key/value map
	 */
	Map<String, String> params();

	Iterable<Map.Entry<String, String>> requestHeaders();

	/**
	 * Returns the current protocol scheme
	 *
	 * @return the protocol scheme
	 */
	String scheme();

	/**
	 * Returns the address of the host peer.
	 *
	 * @return the host's address
	 */
	InetSocketAddress hostAddress();

	/**
	 * Returns the address of the remote peer.
	 *
	 * @return the peer's address
	 */
	InetSocketAddress remoteAddress();


	/**
	 * Returns resolved HTTP cookies
	 *
	 * @return Resolved HTTP cookies
	 */
	Map<CharSequence, Set<Cookie>> cookies();


	/**
	 * Is the request keep alive
	 *
	 * @return is keep alive
	 */
	boolean isKeepAlive();

	/**
	 * Returns true if websocket connection (upgraded)
	 *
	 * @return true if websocket connection
	 */
	boolean isWebsocket();

	/**
	 * Returns the resolved request method (HTTP 1.1 etc)
	 *
	 * @return the resolved request method (HTTP 1.1 etc)
	 */
	int method();

	/**
	 * Returns a normalized {@link #uri()} without the leading and trailing '/' if present
	 *
	 * @return a normalized {@link #uri()} without the leading and trailing
	 */
	default String path() {
		String uri = URI.create(uri()).getPath();
		if (!uri.isEmpty()) {
			if(uri.charAt(0) == '/'){
				uri = uri.substring(1);
				if(uri.length() <= 1){
					return uri;
				}
			}
			if(uri.charAt(uri.length() - 1) == '/'){
				return uri.substring(0, uri.length() - 1);
			}
		}
		return uri;
	}

	/**
	 * Returns the resolved target address
	 *
	 * @return the resolved target address
	 */
	String uri();

	/**
	 * Returns the resolved request version (HTTP 1.1 etc)
	 *
	 * @return the resolved request version (HTTP 1.1 etc)
	 */
	String version();

}
