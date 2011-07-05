package com.mercadolibre.taglib

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.mercadolibre.frontend.commons.SiteBasedConfiguration as SBC
import com.mercadolibre.frontend.util.HTMLUtil;
import com.mercadolibre.frontend.util.HostNameResolver;

class HTMLTagLib {

	static namespace = 'ml'

	def htmlCompressionService
	def javasScriptCompressionService

	/**
	 * Takes the following attributes:
	 * -compress: if true, HTML is minified using {@Code HtmlCompressionService}
	 */
	def html = { attrs, body ->

		request.setAttribute('com.mercadolibre.frontend.StartRender', System.currentTimeMillis())

		def attrClass = attrs.remove('class')
		
		pageScope.compress = Boolean.valueOf(attrs.remove('compress')) && !Boolean.valueOf(params.noCompress) 
		
		def htmlAttrs = HTMLUtil.serializeAttributes(attrs)

		attrClass = (attrClass)?' ' + attrClass:''

		out << "<!DOCTYPE html>\n"
		out << "<!--[if IE]><![endif]--> <!--[if lt IE 7 ]> <html lang=\"en\" class=\"no-js ie6${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 7 ]> <html lang=\"en\" class=\"no-js ie7${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 8 ]> <html lang=\"en\" class=\"no-js ie8${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 9 ]> <html lang=\"en\" class=\"no-js ie9${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if (gt IE 9)|!(IE)]> <!--> <html lang=\"en\" class=\"no-js${attrClass}\"${htmlAttrs}> <!--<![endif]-->"
		out << '\n'

		String compressStats = ''
		if (pageScope.compress) {
			out << htmlCompressionService.compress(body())
			compressStats = htmlCompressionService.getStatistics()
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
		out << "    ${compressStats}\n"
		out << "    Total time		: ${endRequest - startRequest} ms\n"
		out << "    HostName		: ${HostNameResolver.getHostName()}\n"
		out << "-->"
	}


	def body = { attrs, body ->
		def bodyAttrs = HTMLUtil.serializeAttributes(attrs)
		
		pageScope.scripts = ['onload':[], 'async':[]]

		out << "<body${bodyAttrs}>"
		out << '\n'
		out << body()

		
		if(pageScope.scripts){
			// write javascript elements
			out << "<script type=\"text/javascript\" >"
	
			if (pageScope.scripts.onload){
				out << jsLoader
				
				// onload js files
				out << "window.onload = function() {"
					pageScope.scripts['onload'].each{
						out << "  scr.js('${it.resource}', function() {${compressJavascript(it.body)}})"
					}
				out << "}\n"
			}
			out << "</script>\n"

		}
		out << "</body>"
	}

	def private compressJavascript(script){
		if(pageScope.compress){
			return javasScriptCompressionService.compress(script)
		}
		return script
	}
	
	
	
	/**
	 * Javascript loader function that writes a js source file asynchronously
	 */
	def static jsLoader = """
	(function(d){window.scr={js:function(s,c){if(typeof s==="string") {s=[s];}var script=d.getElementsByTagName("script")[0];var b={t:s.length,i:0};b.r=function() {return b.t===b.i;};var callback=function(){b.i++;if(c&&b.r()){c();}};var ready=(function(){if(script.readyState) {return function(n){n.onreadystatechange=function(){if(n.readyState==='loaded'||n.readyState==='complete'){n.onreadystatechange=null;callback();}};};}else{return function(n){n.onload=function(){callback();};};}}());var i=0;var e=d.createElement("script");d.type="text/javascript";for(i;i<b.t;i++){var n=e.cloneNode(true);ready(n);n.src=s[i];n.async="true";script.parentNode.insertBefore(n,script);}}};}(document));
	"""

	/**
	 * Tag that handles external JavaScript files
	 * Takes the following attributes:
	 * -resources: the list of js files to include separated by '_'
	 * -onload: if true the javascript loading is defered to the document onload event.
	 * -async: if true the javascript starts loading asynchronously as soon as the tag is read by the browser.
	 * -Tag body: write the callback function that will be called once the js is loaded.
	 */
	def script = { attrs, body ->
		
		def urlBase = (attrs.urlBase != null)?attrs.urlBase:SBC.getConfig(params.siteId).url['baseStatic'];

		String scriptResource = "${urlBase}/js/${params.siteId}/${grailsApplication.metadata['app.version']}/${attrs.resources.join('_')}.js${(params.noCompress)?'?noCompress=true':''}"

		def onload = Boolean.valueOf(attrs.onload)
		if (onload) {
			pageScope.scripts['onload']  <<  ['resource':scriptResource,'body':body()]
		} else {
			out << "<script type=\"text/javascript\" src=\"${scriptResource}\"></script>"
			out << "<script type=\"text/javascript\">${body()}</script>"
		}

	}


	def link = { attrs ->
		
		def urlBase = (attrs.urlBase != null)?attrs.urlBase:SBC.getConfig(params.siteId).url['baseStatic'];
		
		def noCompress = Boolean.valueOf(params.noCompress)
		out << "<link rel=\"stylesheet\" type=\"text/css\" href=\""
		out << "${urlBase}/css/${params.siteId}/${grailsApplication.metadata['app.version']}/${attrs.resources.join('_')}.css${(params.noCompress)?'?noCompress=true':''}"
		out << "\""
		out << "/>"
	}


	//TODO cambiar url dependiendo si es http o https ( evaluar que las url favicon y .js esten en el plugin )
	def head = { attrs, body ->
		out << '<head>\n'
		out << '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>'
		out << '\n'
		out << body()
		out << '<!--[if lt IE 7 ]>'
		out << '<script src="https://www.mercadolibre.com/org-img/pcorner/js/dd_belatedPNG.min.js"></script>'
		out << '<script> DD_belatedPNG.fix(\'img, .ico, .png24fix, .ch-expando-trigger\'); //fix any <img> or .ico background-images </script>'
		out << '<![endif]-->'
		out << '<link rel="shortcut icon" href="https://www.mercadolibre.com/favicon.ico" />\n'
		out << '</head>'
	}

}
