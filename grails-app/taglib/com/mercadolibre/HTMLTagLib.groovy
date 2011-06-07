package com.mercadolibre

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import com.registration.frontend.util.HTMLUtil
import com.registration.frontend.util.HostNameResolver

class HTMLTagLib {

	static namespace = 'ml'

	def mlCaptchaService

	def html = { attrs, body ->

		request.setAttribute('com.mercadolibre.frontend.StartRender',System.currentTimeMillis())

		def attrClass = attrs.remove('class')

		def htmlAttrs = HTMLUtil.serializeAttributes(attrs)

		attrClass = (attrClass)?' ' + attrClass:''

		out << "<!DOCTYPE html>\n"
		out << "<!--[if IE]><![endif]--> <!--[if lt IE 7 ]> <html lang=\"en\" class=\"no-js ie6${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 7 ]> <html lang=\"en\" class=\"no-js ie7${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 8 ]> <html lang=\"en\" class=\"no-js ie8${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if IE 9 ]> <html lang=\"en\" class=\"no-js ie9${attrClass}\"${htmlAttrs}> <![endif]-->"
		out << "<!--[if (gt IE 9)|!(IE)]> <!--> <html lang=\"en\" class=\"no-js${attrClass}\"${htmlAttrs}> <!--<![endif]-->"
		out << '\n'
		out << body()
		out << '</html>'

		def startRequest = request.getAttribute('com.mercadolibre.frontend.StartRequest')
		def startRender = request.getAttribute('com.mercadolibre.frontend.StartRender')

		def endRequest = System.currentTimeMillis()

		out << '\n'
		out << "<!--\n"
		out << "	Stats\n"
		out << "	Generate Time	: ${(endRequest - startRequest) - (endRequest - startRender)} ms\n"
		out << "	Render Time	: ${endRequest - startRender} ms\n"
		out << "	Total Time	: ${endRequest - startRequest} ms\n"
		out << "	HostName	: ${HostNameResolver.getHostName()}\n"
		out << "-->"
	}


	def body = { attrs, body ->
		def bodyAttrs = HTMLUtil.serializeAttributes(attrs)

		pageScope.scripts = []
		
		out << "<body${bodyAttrs}>"
		out << '\n'
		out << body()

		//		pageScope.script
		//		out << "<script>"
		//		out << loader
		//		out << "</script>"

		out << "</body>"
	}

	def static loader = """
	/* scr.js 0B 0.1.2 - March 17 2011 - Little tiny loader for javascript sources. */
	(function(d){window.scr={js:function(s,c){if(typeof s==="string") {s=[s];}var script=d.getElementsByTagName("script")[0];var b={t:s.length,i:0};b.r=function() {return b.t===b.i;};var callback=function(){b.i++;if(c&&b.r()){c();}};var ready=(function(){if(script.readyState) {return function(n){n.onreadystatechange=function(){if(n.readyState==='loaded'||n.readyState==='complete'){n.onreadystatechange=null;callback();}};};}else{return function(n){n.onload=function(){callback();};};}}());var i=0;var e=d.createElement("script");d.type="text/javascript";for(i;i<b.t;i++){var n=e.cloneNode(true);ready(n);n.src=s[i];script.parentNode.insertBefore(n,script);}}};}(document));
	"""

	def static onLoad = """
	
	window.onload = function() {
	 scr.js(sources, function() {
	 
	 }
	
	"""
	
	def script = { attrs, body ->
		
		out << "<script type=\"text/javascript\" src=\""
		out << "${ConfigurationHolder.config[params.siteId].url['baseStatic']}app/js/${params.siteId}/${grailsApplication.metadata['app.version']}/${attrs.resources.join('_')}.js"
		out << "\"/>"
		out << "</script>"
	}

	def link = { attrs ->
		out << "<link rel=\"stylesheet\" type=\"text/css\" href=\""
		out << "${ConfigurationHolder.config[params.siteId].url['baseStatic']}app/css/${params.siteId}/${grailsApplication.metadata['app.version']}/${attrs.resources.join('_')}.css"
		out << "\""
		out << "/>"
	}
	

	def head = { attrs, body ->
		out << '<head>\n'
		out << '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>'
		out << '\n'
		out << body()
		out << '<!--[if lt IE 7 ]>'
		out << '<script src="js/dd_belatedpng.js"></script>'
		out << '<script> DD_belatedPNG.fix(\'img, .ico, .png24fix, .ch-expando-trigger\'); //fix any <img> or .ico background-images </script>'
		out << '<![endif]-->'
		out << '<link rel="shortcut icon" href="https://www.mercadolibre.com/favicon.ico" />\n'
		out << '</head>'
	}


	//	<script type="text/javascript" src="http://www.w3schools.com/tags/demo_script_src.js" >
	
    def captcha = { attrs ->
        def height = attrs.height
        def width = attrs.width
		
        def challengePhrase = mlCaptchaService.getNewChallenge()
		
        out << "<span id=\"captcha_challenge_phrase_holder\" style=\"display: none;\">"
        out << "  <input type=\"hidden\" name=\"captcha_challenge_phrase\" id=\"captcha_challenge_phrase\" value=\"${challengePhrase}\">"
        out << "</span>"
		
		  
        out << "<div id=\"captcha_image\">"
        out << "  <img height=\"${height}\" width=\"${width}\" src=\"/captchaImage?id=${challengePhrase}&height=${height}&width=${width}\" style=\"display: block;\">"
        out << "</div>"
		
    }
}
