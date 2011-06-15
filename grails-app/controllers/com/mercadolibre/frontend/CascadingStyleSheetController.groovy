package com.mercadolibre.frontend

import com.yahoo.platform.yui.compressor.CssCompressor

class CascadingStyleSheetController extends ResourceController {

	@Override
	def getContentType() {
		"text/css"
	}

	@Override
	def getExtension() {
		'css'
	}

	@Override
	def minimize(css) {
		if (noCompress()) {
			return css
		} else {
			CssCompressor compressor = new CssCompressor(new StringReader(css))
			StringWriter write = new StringWriter()
			compressor.compress(write, 8000)
			write.flush()
			
			return write.getBuffer().toString()
		}
	}
}
