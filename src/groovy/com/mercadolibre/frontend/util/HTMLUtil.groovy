package com.mercadolibre.frontend.util

class HTMLUtil {

	static String serializeAttributes(attrs) {

		StringBuilder sb = new StringBuilder()

		attrs.each { k,v ->
			sb << " " << k << "=\"" << v.encodeAsHTML() << "\""
		}

		return sb.toString();
	}
}
