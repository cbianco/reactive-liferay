package it.cb.reactive.internal.http.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class ClassLoaderUtil {

	public static ClassLoader getClassLoader() {

		Bundle bundle = FrameworkUtil.getBundle(ClassLoaderUtil.class);

		BundleWiring adapt = bundle.adapt(BundleWiring.class);

		return adapt.getClassLoader();
	}

}
