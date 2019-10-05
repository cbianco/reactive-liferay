package it.cb.reactive.http.web;

import org.reactivestreams.Publisher;

import java.util.function.BiFunction;

public interface HttpHandler
	extends Endpoint, BiFunction<HttpRequest, HttpResponse, Publisher<Void>> {

	default int method() {
		return GET;
	}

	int GET =  		0b0_0_0_0_0_1;

	int POST = 		0b0_0_0_0_1_0;

	int PUT = 		0b0_0_0_1_0_0;

	int DELETE = 	0b0_0_1_0_0_0;

	int PATCH = 	0b0_1_0_0_0_0;

	int OPTIONS = 	0b1_0_0_0_0_0;

}
