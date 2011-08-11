package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ApplicationHolder
import com.yahoo.platform.yui.compressor.JavaScriptCompressor

class JavaScriptController extends ResourceController {

	def compressionService

	@Override
	def getContentType() {
		"text/javascript"
	}

	@Override
	def getResourceSplit() {
		';\n'
	}

	@Override
	def getExtension() {
		'js'
	}
	@Override
	def getAdditionalContent(){
		if(params.async){
			return """
				if(typeof _async != 'undefined'){
					for (i=0;i<_async.length;i++){
						_async[i]();
					}
				}
				var _async = function(){
				  return {
					push: function(q) {
					  q();
					}
				  };
				}();
			"""
		}
		else if(params.callback){
			return "if(typeof ${params.callback} == 'function') { ${params.callback}();}"
		}
		else{
			return ""
		}
	}

	@Override
	def minimize(script) {
		return compressionService.compressJavaScript(script)
	}
}
