package com.mercadolibre.frontend

import com.mercadolibre.frontend.util.RequestUtil

class HeadTagLib {

	static namespace = 'ml'

	def head = { attrs, body ->
		out << "<head>"
		out << "<meta content=\"text/html; charset=UTF-8\"/>"
		out << body()
		out << "<!--[if lt IE 7 ]>"
		out << "<script src=\"${RequestUtil.getProtocol(request)}://www.mercadolibre.com/org-img/pcorner/js/dd_belatedPNG.min.js\"></script>"
		out << "<script> DD_belatedPNG.fix(\'img, .ico, .png24fix, .ch-expando-trigger\'); //fix any <img> or .ico background-images </script>"
		out << "<![endif]-->"
		out << "<link rel=\"shortcut icon\" href=\"${RequestUtil.getProtocol(request)}://www.mercadolibre.com/favicon.ico\" />\n"
		out << "</head>"
	}

}
