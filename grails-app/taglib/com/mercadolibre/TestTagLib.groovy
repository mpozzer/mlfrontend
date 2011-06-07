package com.mercadolibre

class TestTagLib {
	def parent = { attrs, body ->
     	out << "My CHILD says ${pageScope.pepe}"
	}
	def child = { attrs, body ->
		pageScope.pepe = "EL HIJO"
	}

}
