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
	
	protected def domains = [ "mla":"www.mercadolibre.com.ar", "mlb":"www.mercadolivre.com.br", "mco": "www.mercadolibre.com.co",
							   "mcr":"www.mercadolibre.co.cr", "mlc":"www.mercadolibre.cl", "mrd":"www.mercadolibre.com.do",
							   "mec":"www.mercadolibre.com.ec", "mlm":"www.mercadolibre.com.mx", "mpa":"www.mercadolibre.com.pa", 
							   "mpe":"www.mercadolibre.com.pe", "mpt":"www.mercadolivre.pt", "mlu":"www.mercadolibre.com.uy",
							   "mlv":"www.mercadolibre.com.ve" ]
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
		String serverName = resolveDomain(request)
		def matchingDomain = SITES_DOMAINS.find({ d ->
			serverName.endsWith(d.id)
			})
		return matchingDomain?.site_id?.toString()
	}
	
	private String resolveDomain(req){
		String domain = req.serverName;
		String forwardURI = req.forwardURI;
		String siteIdPattern = ""
		Boolean isDotComDomain = false
		if (domain.endsWith("mercadopago.com")){

			siteIdPattern = ".*/(\\w{3})/.*";
			isDotComDomain = forwardURI.matches(siteIdPattern)

		}else{
			siteIdPattern = ".*/jms/(\\w{3})/.*";
			isDotComDomain = forwardURI.matches(siteIdPattern)
		}


		if(!StringUtils.isEmpty(domain) && domain.endsWith(".com") && isDotComDomain){
			String siteId = null
			forwardURI.eachMatch(siteIdPattern){
				siteId = it[1]
			}
			domain = domains[siteId]
		}

		return domain
	}
}
