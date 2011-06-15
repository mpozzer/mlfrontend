package com.mercadolibre.frontend.services

import com.yahoo.platform.yui.compressor.JavaScriptCompressor

/**
 * 
 * @author pmoretti
 */
class JavasScriptCompressionService {
	
	String compress(script) {
		JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(script), null)
		StringWriter write = new StringWriter()
		compressor.compress(write, 8000, false, false, true, false)
		write.flush()
		return write.getBuffer().toString()
	}
	
}
