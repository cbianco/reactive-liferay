package it.cb.reactive.internal.util;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ServiceTrackerUtil {

	public static <T1, T2> ServiceTracker<T1, T2> open(
		BundleContext bundleContext, Class<T1> clazz,
		ServiceTrackerCustomizer<T1, T2> customizer) {

		ServiceTracker<T1, T2> st =
			new ServiceTracker<>(bundleContext, clazz, customizer);

		st.open(true);

		return st;

	}

	public static <T1, T2> ServiceTracker<T1, T2> open(
		BundleContext bundleContext, Class<T1> clazz) {

		return open(bundleContext, clazz, null);

	}

}
