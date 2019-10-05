package it.cb.rective.test;

import io.reactivex.Flowable;
import it.cb.reactive.http.web.Endpoint;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.http.web.HttpRequest;
import it.cb.reactive.http.web.HttpResponse;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component(
	immediate = true,
	service = Endpoint.class
)
public class RxJavaHttpSSEExample implements HttpHandler {

	@Override
	public String getPath() {
		return "/rxjava-test-sse";
	}

	@Override
	public Publisher<Void> apply(
		HttpRequest httpRequest, HttpResponse httpResponse) {

		return httpResponse
			.sse()
			.sendString(
				Flowable
					.interval(200, TimeUnit.MILLISECONDS)
					.map(Objects::toString)
			);

	}

	@Override
	public int method() {
		return GET;
	}
}
