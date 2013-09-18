package com.mercadolibre.frontend.services

import grails.util.Environment

import org.springframework.beans.BeansException
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import com.mercadolibre.opensource.frameworks.restclient.SimpleRestClient

/**
 * Resolves the country-specific siteId and domain for an HTTP request.
 * Relies on {@link https://api.mercadolibre.com/site_domains} upon startup and caches the domain list
 * for the rest of its life-cycle.
 * 
 * @author pduranti
 *
 */
class MLDomainsResolver implements InitializingBean, ApplicationContextAware {

	static transactional = false

	SimpleRestClient simpleRestClient

	def connectionRetries = 5

	private def SITES_DOMAINS = []
	
	def siteDomainStub = grails.converters.JSON.parse('[{id:"mercadolidesa.com.ar",site_id:"MLA",country_id:"AR",locale:"es_AR",tag:"ML"},{id:"mercadolidesa.com.br",site_id:"MLB",country_id:"BR",locale:"pt_BR",tag:"ML"},{id:"deremate.com.ve",site_id:"MLV",country_id:"VE",locale:"es_VE",tag:"DR"},{id:"deremate.com.uy",site_id:"MLU",country_id:"UY",locale:"es_UY",tag:"DR"},{id:"deremate.com.mx",site_id:"MLM",country_id:"MX",locale:"es_MX",tag:"DR"},{id:"deremate.com.pe",site_id:"MPE",country_id:"PE",locale:"es_PE",tag:"DR"},{id:"deremate.com.ec",site_id:"MEC",country_id:"EC",locale:"es_EC",tag:"DR"},{id:"deremate.com.co",site_id:"MCO",country_id:"CO",locale:"es_CO",tag:"DR"},{id:"deremate.cl",site_id:"MLC",country_id:"CL",locale:"es_CL",tag:"DR"},{id:"arremate.com",site_id:"MLB",country_id:"BR",locale:"pt_BR",tag:"DR"},{id:"deremate.com.ar",site_id:"MLA",country_id:"AR",locale:"es_AR",tag:"DR"},{id:"mercadolibre.com.ar",site_id:"MLA",country_id:"AR",locale:"es_AR",tag:"ML"},{id:"mercadolivre.com.br",site_id:"MLB",country_id:"BR",locale:"pt_BR",tag:"ML"},{id:"mercadolibre.cl",site_id:"MLC",country_id:"CL",locale:"es_CL",tag:"ML"},{id:"mercadolibre.com.co",site_id:"MCO",country_id:"CO",locale:"es_CO",tag:"ML"},{id:"mercadolibre.co.cr",site_id:"MCR",country_id:"CR",locale:"es_CR",tag:"ML"},{id:"mercadolibre.com.ec",site_id:"MEC",country_id:"EC",locale:"es_EC",tag:"ML"},{id:"mercadolibre.com.mx",site_id:"MLM",country_id:"MX",locale:"es_MX",tag:"ML"},{id:"mercadolibre.com.uy",site_id:"MLU",country_id:"UY",locale:"es_UY",tag:"ML"},{id:"mercadolibre.com.ve",site_id:"MLV",country_id:"VE",locale:"es_VE",tag:"ML"},{id:"mercadolibre.com.pa",site_id:"MPA",country_id:"PA",locale:"es_PA",tag:"ML"},{id:"mercadolibre.com.pe",site_id:"MPE",country_id:"PE",locale:"es_PE",tag:"ML"},{id:"mercadolivre.pt",site_id:"MPT",country_id:"PT",locale:"pt_PT",tag:"ML"},{id:"mercadolibre.com.do",site_id:"MRD",country_id:"DO",locale:"es_DO",tag:"ML"},{id:"dereto.com.co",site_id:"MCO",country_id:"CO",locale:"es_CO",tag:"DR"},{id:"dereto.com.mx",site_id:"MLM",country_id:"MX",locale:"es_MX",tag:"DR"},{id:"myml.mercadolibre.com.dev.gz",site_id:"MLA",country_id:"AR",locale:"es_AR",tag:"ML"},{id:"arremate.com.br",site_id:"MLB",country_id:"BR",locale:"pt_BR",tag:"DR"},{id:"tucarro.com.ve",site_id:"MLV",country_id:"VE",locale:"es_VE",tag:"TC"},{id:"tucarro.com.co",site_id:"MCO",country_id:"CO",locale:"es_CO",tag:"TC"},{id:"mercadolivre.com",site_id:"MLB",country_id:"BR",locale:"pt_BR",tag:"ML"}]');
	
	public void afterPropertiesSet() {
		if(Environment.PRODUCTION == Environment.getCurrent()){
			loadDomains()
		}
		else{
			SITES_DOMAINS = siteDomainStub;
		}
	}

	private void loadDomains() {
		for(;;) {
			try {
				simpleRestClient.get(uri:"/site_domains".toString(),
						success: {
							//preprocesar y objetos no json
							SITES_DOMAINS = it.data
						}, failure:{
							throw new RuntimeException("Failed to obtain ML domain from /site_domains ${it?.data}")
						})
				return
			} catch (Exception e) {
				connectionRetries--;
				if (connectionRetries == 0) {
					throw new RuntimeException("/site_domain API failed, Cannot resolve domains", e)
				}
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ac)
	throws BeansException {
		simpleRestClient = (SimpleRestClient) ac.getBean("simpleRestClient");
	}

	static def siteIdRegEx = "(?:mercadopago.com|jms)/(\\w\\w\\w)/?"

	public String getRequestSite(request) {

		String siteId = null
		
		def siteIdMatcher = (request.serverName + request.forwardURI) =~ siteIdRegEx

		if(siteIdMatcher){
			siteId = siteIdMatcher[0][1].toUpperCase()
		}else{
			String serverName = request.serverName

			siteId = SITES_DOMAINS.find( {
				d -> serverName.endsWith(d.id)
			})?.site_id?.toString()
		}
		return siteId
	}
}
