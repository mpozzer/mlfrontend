package com.mercadolibre.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mercadolibre.frontend.commons.ParametersWrappedRequest;
import com.mercadolibre.frontend.services.MLDomainsResolver;

/**
 * Servlet filter that adds special parameters to the Request object that are common to all
 * ML front-ends.
 * @author pduranti
 *
 */
public class MLParamsAwareFilter extends OncePerRequestFilter implements ApplicationContextAware {

	Log log = LogFactory.getLog(this.getClass());
	
	private MLDomainsResolver domainsResolver;
	static final String SITE_ID = "siteId"; 
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.debug("MLParamsAwareFilter enter");
		
		request.setAttribute("com.mercadolibre.frontend.StartRequest",System.currentTimeMillis());

		ParametersWrappedRequest paramsRequest = new ParametersWrappedRequest(request);
		if (request.getParameter(SITE_ID) == null) {
			// add siteId parameter if not included in query string
            String site = domainsResolver.getRequestSite(request);
		    paramsRequest.addParameter(SITE_ID, site);
		}
		chain.doFilter(paramsRequest, response);
	}

	@Override
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		domainsResolver = (MLDomainsResolver) ac.getBean("mlDomainsResolver");
	}
	
}
