package it.cb.rective.test;

import io.reactivex.Flowable;
import it.cb.reactive.http.web.Endpoint;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.http.web.HttpRequest;
import it.cb.reactive.http.web.HttpResponse;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Component(
	immediate = true,
	service = Endpoint.class
)
public class RxJavaHttpExample implements HttpHandler {

	@Override
	public String getPath() {
		return "/rxjava-test";
	}

	@Override
	public Publisher<Void> apply(
		HttpRequest httpRequest, HttpResponse httpResponse) {

		return httpResponse.sendString(Flowable.just("rxjava-test"));

	}

	@Override
	public int method() {
		return GET;
	}
}
