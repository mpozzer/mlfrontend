package com.mercadolibre.frontend

import com.mercadolibre.frontend.util.HTMLUtil

class BodyTagLib {

	static namespace = 'ml'

	def body = { attrs, body ->

		def bodyAttrs = HTMLUtil.serializeAttributes(attrs)

		out << "<body${bodyAttrs}>"
		out << '\n'
		out << body()
		out << ml.scriptDefered()
		out << "</body>"
	}


}
