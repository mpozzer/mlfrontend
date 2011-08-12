package com.mercadolibre.frontend

import org.apache.commons.lang.StringEscapeUtils

import com.mercadolibre.frontend.services.CompressionService

class ScriptTagLib {

	static namespace = 'ml'

	CompressionService compressionService
	
	def MLHTMLContext

	def private compressJavascript(javascript){
		if(ml.compress()){
			return compressionService.compressJavaScript(javascript)
		}
		return javascript
	}

	def geLoaderScript(){
		return "(function(d){window.scr={js:function(s,c){if(typeof s===\"string\") {s=[s];}var script=d.getElementsByTagName(\"script\")[0];var b={t:s.length,i:0};b.r=function() {return b.t===b.i;};var callback=function(){b.i++;if(c&&b.r()){c();}};var ready=(function(){if(script.readyState) {return function(n){n.onreadystatechange=function(){if(n.readyState==='loaded'||n.readyState==='complete'){n.onreadystatechange=null;callback();}};};}else{return function(n){n.onload=function(){callback();};};}}());var i=0;var e=d.createElement(\"script\");d.type=\"text/javascript\";for(i;i<b.t;i++){var n=e.cloneNode(true);ready(n);n.src=s[i];n.async=\"true\";script.parentNode.insertBefore(n,script);}}};}(document));"
	}


	def getScript(script){
		return "<script type=\"text/javascript\">${compressJavascript(script)}</script>"
	}
	def getResource(src){
		return "<script type=\"text/javascript\" src=\"${src}\"></script>"
	}

	def getResourceAsync(src){
		return  "var _async = [];(function(d, t){var g = d.createElement(t),s = d.getElementsByTagName(t)[0];g.async = g.src = '${src}';s.parentNode.insertBefore(g, s);}(document, 'script'));"
	}

//	def getScriptAsync(script){
//		return  "_async.push(function(){ ${compressJavascript(script)} });"
//	}

	
	def getScriptAsync(script){
		return  "_async.push(\"${StringEscapeUtils.escapeJavaScript(compressJavascript(script))}\");"
	}

	
	
	def getScriptOnload(script){
		return  "window.onload = function(){ ${compressJavascript(script)} } ;"
	}

	def addParameter(url,key,value){
		if(!url.contains('?')){
			url = url + "?"
		}
		return url + "&" + key + "=" + value
	}

	def script = { attrs, body ->

		def scriptDefered = [:]

		if(attrs.resources){

			def scriptResource = ml.resource(ext:'js',resources:attrs.resources,urlBase:attrs.urlBase).toString()

			if (attrs.async) {
				if(attrs.defered){
					scriptDefered['resource'] = scriptResource
				}else{
					out << getScript(getResourceAsync(addParameter(scriptResource,'async',true)))
				}
			}
			else if(attrs.onload){
				scriptDefered['resource'] = scriptResource
			}
			else{
				out << getResource(scriptResource)
			}
		}

		def script = body().toString()

		if(script){

			if (attrs.async) {
				if(attrs.defered){
					scriptDefered['script'] = script
				}else{
					out << getScript(getScriptAsync(script))
				}
			}
			else if(attrs.onload){
				scriptDefered['script'] = script
			}
			else{
				out << getScript(script)
			}
		}
		
		if(!MLHTMLContext.scripts){
			MLHTMLContext.scripts = [:]
		}

		if(scriptDefered){
			if(attrs.async){
				if(!MLHTMLContext.scripts['async']){
					MLHTMLContext.scripts['async'] = []
				}
				MLHTMLContext.scripts['async'] << scriptDefered
			}else if(attrs.onload){
				if(!MLHTMLContext.scripts['onload']){
					MLHTMLContext.scripts['onload'] = []
				}
				MLHTMLContext.scripts['onload'] << scriptDefered
			}
		}
	}

	def scriptDefered = { attrs ->

		if(MLHTMLContext.scripts){
			out << "<script type=\"text/javascript\">"

			def scriptOnload = MLHTMLContext.scripts['onload']

			if(scriptOnload){

				StringBuilder sb = new StringBuilder()

				scriptOnload.each{

					if(it.resource && it.script){

						def callback = 'callback' + Math.abs(it.resource.hashCode())

						out << "function ${callback}(){ ${it.script} };"

						sb.append(getResourceAsync(addParameter(it.resource,'callback',callback)))
					}
					else if(it.resource){
						sb.append(getResourceAsync(it.resource))
					}else if(it.script){
						sb.append(compressJavascript(it.script))
					}
				}

				out << getScriptOnload(sb.toString())
			}

			def scriptAsync = MLHTMLContext.scripts['async']

			if(scriptAsync){

				StringBuilder sb = new StringBuilder()

				scriptAsync.each{

					if(it.resource){
						sb.append(getResourceAsync(addParameter(it.resource,'async',true)))
					}
					if(it.script){
						sb.append(getScriptAsync(it.script))
					}
				}

				out << sb.toString();
			}

			out << "</script>"
		}
	}
}


