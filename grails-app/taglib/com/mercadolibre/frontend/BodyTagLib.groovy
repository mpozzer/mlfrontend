package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.mercadolibre.frontend.commons.SiteBasedConfiguration as SBC
import com.mercadolibre.frontend.util.HTMLUtil;
import com.mercadolibre.frontend.util.HostNameResolver;

class BodyTagLib {

	static namespace = 'ml'

	def javasScriptCompressionService

	def body = { attrs, body ->

		def bodyAttrs = HTMLUtil.serializeAttributes(attrs)

		out << "<body${bodyAttrs}>"
		out << '\n'
		out << body()
		out << ml.scriptDefered()
		out << "</body>"
	}

	def private compressJavascript(script){
		if(pageScope.compress){
			return javasScriptCompressionService.compress(script)
		}
		return script
	}

}
