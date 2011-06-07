package com.mercadolibre.frontend.util

class DomainResolver {

	protected def siteDomains = []

	def getRequestSiteDomain(request){
		def restClient = ctx.getBean("restClientMLApp")
		restClient.get(uri:"/site_domains".toString(), 
			success: {
				siteDomain = it.data
			}, failure:{
				siteDomain =  [id: "mercadolibre.com.ar",site_id: "MLA", country_id: "AR",locale: "es_AR"]
			})
		//println "********************* " + locale
		return siteDomain
	}
}
