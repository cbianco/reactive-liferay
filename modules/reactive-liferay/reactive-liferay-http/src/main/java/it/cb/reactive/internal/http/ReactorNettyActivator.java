package it.cb.reactive.internal.http;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vavr.collection.List;
import it.cb.reactive.http.socket.WebSocketHandler;
import it.cb.reactive.http.web.Endpoint;
import it.cb.reactive.http.web.HttpHandler;
import it.cb.reactive.internal.http.util.HttpPredicateUtil;
import it.cb.reactive.internal.http.util.ServiceTrackerProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.util.tracker.ServiceTracker;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.resources.LoopResources;

@Component(immediate = true, service = ReactorNettyActivator.class)
public class ReactorNettyActivator {

	@interface Config {
		int port() default 8888;
		String name() default "liferay-reactive";
	}

	@Activate
	public void init(BundleContext bundleContext, Config config) {

		LoopResources loopResources = LoopResources
			.create(
				config.name(),
				1,
				LoopResources.DEFAULT_IO_WORKER_COUNT,
				true);

		HttpServer
			.create()
			.tcpConfiguration(tcp -> tcp.runOn(loopResources))
			.handle(this::_handle)
			.port(config.port())
			.bindNow();

		_dispose = loopResources::dispose;

		_serviceTracker =
			ServiceTrackerProcessor
				.create(bundleContext, Endpoint.class)
				.map(b -> s -> {

						Endpoint service = b.getService(s);

						if (service instanceof WebSocketHandler) {
							return HttpEnpointRoutes.ws(
								(WebSocketHandler)service,
								HttpPredicateUtil.get(service.getPath()));
						}
						else {

							HttpHandler httpHandler =(HttpHandler)service;

							return HttpEnpointRoutes.noWs(
								(HttpHandler)service,
								HttpPredicateUtil
									.getPredicate(httpHandler.method())
									.apply(httpHandler.getPath())
							);
						}

					},
					b -> (r, o) -> b.ungetService(r)
				)
				.open();

	}

	@Deactivate
	public void destroy() {
		_dispose.run();
		_dispose = _EMPTY_RUNNABLE;
		_serviceTracker.close();
		_serviceTracker = null;
	}

	@Modified
	public void modified(BundleContext bundleContext, Config config) {
		destroy();
		init(bundleContext, config);
	}

	private Publisher<Void> _handle(
		HttpServerRequest req, HttpServerResponse res) {

		HttpEnpointRoutes[] services =
			_serviceTracker.getServices(
				_EMPTY_WEBSOCKET_HANDLER_ARRAY);

		boolean isWebSocketHttpRequest =
			req
				.requestHeaders()
				.containsValue(
					HttpHeaderNames.CONNECTION,
					HttpHeaderValues.UPGRADE, true);

		List<HttpEnpointRoutes> list =
			List
				.of(services)
				.filter(s -> s.getCondition().test(req));

		if (isWebSocketHttpRequest) {
			return _handleFirstHER(
				req, res, list.filter(HttpEnpointRoutes::isWebSocket));
		}
		else {
			return _handleFirstHER(
				req, res, list.filter(HttpEnpointRoutes::isNotWebSocket));

		}

	}

	private Publisher<Void> _handleFirstHER(
		HttpServerRequest req, HttpServerResponse res,
		List<HttpEnpointRoutes> list) {

		return list
			.map(s -> s.handle(req, res))
			.getOrElse(res::sendNotFound);
	}

	private ServiceTracker<Endpoint, HttpEnpointRoutes> _serviceTracker;

	private Runnable _dispose = _EMPTY_RUNNABLE;

	private static final Runnable _EMPTY_RUNNABLE = () -> {};

	private static final HttpEnpointRoutes[] _EMPTY_WEBSOCKET_HANDLER_ARRAY =
		{};

}
