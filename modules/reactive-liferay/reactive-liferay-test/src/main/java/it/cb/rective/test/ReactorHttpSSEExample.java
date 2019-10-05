package it.cb.rective.test;

import it.cb.reactive.http.web.Endpoint;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.http.web.HttpRequest;
import it.cb.reactive.http.web.HttpResponse;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Component(
	immediate = true,
	service = Endpoint.class
)
public class ReactorHttpSSEExample implements HttpHandler {

	@Override
	public String getPath() {
		return "/reactor-test-sse";
	}

	@Override
	public Publisher<Void> apply(
		HttpRequest httpRequest, HttpResponse httpResponse) {

		return httpResponse
			.sse()
			.sendString(
				Flux
					.interval(Duration.ofMillis(200))
					.map(Objects::toString)
			);

	}

	@Override
	public int method() {
		return GET;
	}
}
