package com.mercadolibre.frontend.services

import org.apache.commons.lang.StringUtils
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

	SimpleRestClient simpleRestClient
	def connectionRetries = 3
	

	private static def SITES_DOMAINS = []

	public void afterPropertiesSet() {
		loadDomains()
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
	
	public String getRequestSite(request) {
		String serverName = request.serverName;
		def matchingDomain = SITES_DOMAINS.find({ d ->
			serverName.endsWith(d.id)
			})
		return matchingDomain?.site_id?.toString()
	}
	
}
