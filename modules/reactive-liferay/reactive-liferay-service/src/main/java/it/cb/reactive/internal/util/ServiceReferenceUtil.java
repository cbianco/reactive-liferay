package it.cb.reactive.internal.util;

import org.osgi.framework.ServiceReference;

public class ServiceReferenceUtil {

	public static String getString(
		ServiceReference sr, String key, String defaultValue) {

		Object property = sr.getProperty(key);

		return property == null ? defaultValue : (String)property;
	}

}
