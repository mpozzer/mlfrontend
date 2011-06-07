package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ApplicationHolder
import com.yahoo.platform.yui.compressor.JavaScriptCompressor

class JavaScriptController extends ResourceController {

	@Override
	def getContentType() {
		"text/javascript"
	}

	@Override
	def getResourceSplit() {
		';'
	}

	@Override
	def getExtension() {
		'js'
	}

	@Override
	def minimize(script) {
		JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(script), null)
		StringWriter write = new StringWriter()
		compressor.compress(write, 8000, false, false, true, false)
		write.flush()
		return write.getBuffer().toString()
	}
}
