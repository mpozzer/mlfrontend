package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.mercadolibre.frontend.commons.SiteBasedConfiguration as SBC
import com.mercadolibre.frontend.util.HTMLUtil;
import com.mercadolibre.frontend.util.HostNameResolver;

class HTMLTagLib {

	static namespace = 'ml'

	def compressionService
	
	def MLHTMLContext
	
	static returnObjectForTags = ['compress']
	
	def compress = { attrs, body ->
		return Boolean.valueOf(attrs.remove('compress')) && (params.compress == null || Boolean.valueOf(params.compress))
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

		out << "<!DOCTYPE html>\n"
		out << "<!--[if IE]><![endif]--> <!--[if lt IE 7 ]> <html lang=\"en\" class=\"no-js ie6${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 7 ]> <html lang=\"en\" class=\"no-js ie7${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 8 ]> <html lang=\"en\" class=\"no-js ie8${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 9 ]> <html lang=\"en\" class=\"no-js ie9${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if (gt IE 9)|!(IE)]> <!--> <html lang=\"en\" class=\"no-js${attrClass}\"${htmlAttrs}> <!--<![endif]-->"
		out << '\n'

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
