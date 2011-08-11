package com.mercadolibre.frontend


class LinkTagLib {

	static namespace = 'ml'

	def link = { attrs ->
		def linkResource = ml.resource(ext:'css',resources:attrs.resources,urlBase:attrs.urlBase).toString()
		out << "<link rel=\"stylesheet\" type=\"text/css\" href=\"${linkResource}\" />"
	}
}


