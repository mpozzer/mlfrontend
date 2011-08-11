package com.mercadolibre.frontend.util

class RequestUtil {

	static boolean isHttps(request) {
		return request.getRequestURL().toString().startsWith("https")
	}
	
	static String getProtocol(request) {
		return isHttps(request)?'https':'http'
	}
}
