package com.mercadolibre.frontend

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * Base class for resource controllers. Reads a resource file and handles minification optimizing
 * the static resources compression and loading, 
 * 
 * By convention the different resource types should be placed in default locations:
 * -CascadeStyleSheets: web-app/css
 * -Javascript: web-app/js
 * 
 * Minification:
 * -By default it will try to minimize JS and CSS resources. To avoid it, use {@code ?noCompress=true}
 *   parameter in the query string
 * -To provide a custom minification -avoiding the controller automatic minification- provide a file called '.min' next 
 * to the original file. e.g. web-app/css/base.min.css
 * @author pduranti
 */
abstract class ResourceController {

	def beforeInterceptor = {
		params.siteId = request.getParameter("siteId")
	}
	
	def index = {

		response.setHeader("Content-type", contentType)

		if (params.appVersion != grailsApplication.metadata['app.version']) {
		  response.setHeader("Cache-Control","max-age=10,public")
		} else {
		  response.setHeader("Cache-Control", "max-age=2592000,public")
		}

		StringBuilder sb = new StringBuilder()

		def fileNames = params.resources?.split('_').each{
			sb.append(readFile(it))
			sb.append(resourceSplit)
		}
		
		sb.append(minimize(additionalContent))

		render sb.toString()
	}
	
	def getAdditionalContent(){
		return ""
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
	
	def compress(){
		return params.compress == null || Boolean.valueOf(params.compress)
	}

	/**
	 * Reads the resource file given and handles minification.
	 */
	protected def readFile(fileName) {
		fileName = removeExtension(fileName)
		
		def minVersion = ApplicationHolder.application.parentContext.servletContext.getRealPath(extension + "/" + fileName  + ".min." + extension)
		File f = new File(minVersion)
		if (f.exists()) {
		    return processTemplate(f.text)
		} else {
		    def normal = ApplicationHolder.application.parentContext.servletContext.getRealPath(extension + "/" + fileName  + "." + extension)
			f = new File(normal)
			if (f.exists()) {
				if (compress()) {
					return minimize(processTemplate(f.text))
				}else{
					return processTemplate(f.text)
				}
			} else {
			    return ""
			}
		}
	}
	
	private removeExtension(fileName) {
		if (fileName.endsWith(".${extension}")) 
		  fileName = fileName.substring(0, fileName.lastIndexOf('.'))
		return fileName
	}
	
	/**
	 * Process the string
	 */
	private processTemplate(text) {
		return text
	}
}
