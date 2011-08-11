package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.mercadolibre.frontend.commons.SiteBasedConfiguration as SBC
import com.mercadolibre.frontend.util.HTMLUtil;
import com.mercadolibre.frontend.util.HostNameResolver;

class UtilTagLib {

	static namespace = 'ml'

	def compressionService

	/**
	 * Takes the following attributes:
	 * -compress: if true, HTML is minified using {@Code HtmlCompressionService}
	 */
	def html = { attrs, body ->

		request.setAttribute('com.mercadolibre.frontend.StartRender', System.currentTimeMillis())

		def attrClass = attrs.remove('class')
		
		pageScope.mlhtml = [:]
		
		pageScope.mlhtml.compress = Boolean.valueOf(attrs.remove('compress')) && Boolean.valueOf(params.compress)

		def htmlAttrs = HTMLUtil.serializeAttributes(attrs)

		attrClass = (attrClass)?' ' + attrClass:''

		out << "<!DOCTYPE html>\n"
		out << "<!--[if IE]><![endif]--> <!--[if lt IE 7 ]> <html lang=\"en\" class=\"no-js ie6${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 7 ]> <html lang=\"en\" class=\"no-js ie7${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 8 ]> <html lang=\"en\" class=\"no-js ie8${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 9 ]> <html lang=\"en\" class=\"no-js ie9${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if (gt IE 9)|!(IE)]> <!--> <html lang=\"en\" class=\"no-js${attrClass}\"${htmlAttrs}> <!--<![endif]-->"
		out << '\n'

		def resutlHtmlCompress

		if (pageScope.mlhtml.compress) {
			resutlHtmlCompress = compressionService.compress(body())
			out << resutlHtmlCompress.html
		} else {
			out << body()
		}

		out << '</html>'

		def startRequest = request.getAttribute('com.mercadolibre.frontend.StartRequest')

		def startRender = request.getAttribute('com.mercadolibre.frontend.StartRender')

		def endRequest = System.currentTimeMillis()

		out << '\n'
		out << "<!--\n"
		out << "    Stats\n"
		out << "    Generate time	: ${(endRequest - startRequest) - (endRequest - startRender)} ms\n"
		out << "    Render time		: ${endRequest - startRender} ms\n"
		out << "    ${resutlHtmlCompress?.statistics}"
		out << "    Total time		: ${endRequest - startRequest} ms\n"
		out << "    HostName		: ${HostNameResolver.getHostName()}\n"
		out << "-->"
	}
}
