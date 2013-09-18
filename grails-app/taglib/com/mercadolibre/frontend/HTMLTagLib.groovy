package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.mercadolibre.frontend.commons.SiteBasedConfiguration as SBC
import com.mercadolibre.frontend.util.HTMLUtil;
import com.mercadolibre.frontend.util.HostNameResolver;
import org.apache.commons.lang.StringEscapeUtils

class HTMLTagLib {

	static namespace = 'ml'

	def compressionService
	
	def MLHTMLContext
	
	static returnObjectForTags = ['compress']
	
	def compress = { attrs, body ->
		return (params.compress == null || Boolean.valueOf(params.compress))
	}
	
	/**
	 * Takes the following attributes:
	 * -compress: if true, HTML is minified using {@Code CompressionService}
	 */
	def html = { attrs, body ->

		request.setAttribute('com.mercadolibre.frontend.StartRender', System.currentTimeMillis())

		def attrClass = attrs.remove('class')

		MLHTMLContext.siteId = attrs.siteId?:params.siteId
		
		def htmlAttrs = HTMLUtil.serializeAttributes(attrs)

		attrClass = (attrClass)?' ' + attrClass:''

		out << "<!doctype html>"
        out << "<!--[if lt IE 7]> <html class=\"no-js lt-ie10 lt-ie9 lt-ie8 lt-ie7 ie6${attrClass}\"${htmlAttrs} lang=\"en\"> <![endif]-->"
        out << "<!--[if IE 7]>    <html class=\"no-js lt-ie10 lt-ie9 lt-ie8 ie7${attrClass}\"${htmlAttrs} lang=\"en\"> <![endif]-->"
        out << "<!--[if IE 8]>    <html class=\"no-js lt-ie10 lt-ie9 ie8${attrClass}\"${htmlAttrs} lang=\"en\"> <![endif]-->"
        out << "<!--[if IE 9]>    <html class=\"no-js lt-ie10 ie9${attrClass}\"${htmlAttrs} lang=\"en\"> <![endif]-->"
        out << "<!--[if gt IE 9]><!--> <html class=\"no-js${attrClass}\"${htmlAttrs} lang=\"en\"> <!--<![endif]-->"
        out << "\n"

		def timeCompress = 0

		if (ml.compress(attrs)) {
			def bodyTemp = body().toString();
			timeCompress = System.currentTimeMillis()
			def compressBody = compressionService.compressHTML(bodyTemp)
			timeCompress = System.currentTimeMillis() - timeCompress
			out << compressBody
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
		out << "    Render time		: ${endRequest - startRender - timeCompress} ms\n"
		out << "    Compress time	: ${timeCompress} ms\n"
		out << "    Total time		: ${endRequest - startRequest} ms\n"
		out << "    HostName		: ${HostNameResolver.getHostName()}\n"
		out << "-->"
	}

	def resource = { attrs ->

		StringBuilder sb = new StringBuilder()

		if(attrs.urlBase){
			if(!attrs.urlBase?.endsWith("/")){
				attrs.urlBase += "/"
			}
			sb.append(attrs.urlBase)
		}
		else{
			sb.append("/")
		}

		sb.append(attrs.ext)
		sb.append("/")
		if(!MLHTMLContext.siteId || !MLHTMLContext.siteId.matches("M\\w{2}")){
			MLHTMLContext.siteId = "MLA"
		}
		sb.append(MLHTMLContext.siteId)
		sb.append("/")
		sb.append(grailsApplication.metadata['app.version'])
		sb.append("/")
		sb.append(attrs.resources.join('_'))
		sb.append(".")
		sb.append(attrs.ext)

		if(!ml.compress(attrs)){
			sb.append("?compress=false");
		}

		out << sb.toString();
	}
}
