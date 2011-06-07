package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * TODO: optimizar manejo de stings
 */
abstract class ResourceController {

	def beforeInterceptor = {
		params.siteId = request.getParameter("siteId")
	}
	
	def index = {

		response.setHeader("Content-type", contentType)
		response.setHeader("Cache-Control","public, max-age=2592000")

		StringBuilder sb = new StringBuilder()

		def fileNames = params.resources?.split('_').each{
			sb.append(minimize(readFile(it)))
			sb.append(resourceSplit)
		}

		render sb.toString()
	}

	def minimize(script){
		return script
	}

	def getContentType(){
		"text/plain"
	}

	def getResourceSplit() {
		"\n"
	}

	def getExtension() {
		"txt"
	}

	protected def readFile(fileName){
		def file = ApplicationHolder.application.parentContext.servletContext.getRealPath(extencion + "/" + fileName + "." + extencion)
		File f = new File(file)
		if(f.exists()){
			return f.text
		}
		else{
			return ""
		}
	}
}
