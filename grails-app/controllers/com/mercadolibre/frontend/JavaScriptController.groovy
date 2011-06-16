package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ApplicationHolder
import com.yahoo.platform.yui.compressor.JavaScriptCompressor

class JavaScriptController extends ResourceController {

	def javasScriptCompressionService
	
	@Override
	def getContentType() {
		"text/javascript"
	}

	@Override
	def getResourceSplit() {
		'\n'
	}

	@Override
	def getExtension() {
		'js'
	}

	@Override
	def minimize(script) {
		if (noCompress()) {
			return script
		} else {
			return javasScriptCompressionService.compress(script)
		}
	}
}
