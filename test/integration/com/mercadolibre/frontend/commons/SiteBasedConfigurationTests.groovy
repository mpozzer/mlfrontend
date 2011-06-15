package com.mercadolibre.frontend.commons;

import static org.junit.Assert.*

import org.junit.Test

class SiteBasedConfigurationTests extends GroovyTestCase {

	@Test
	void testGetConfig() {
		ConfigObject config = SiteBasedConfiguration.getConfig("MLA")
		assertNotNull(config)
		
		ConfigObject rootConfig = SiteBasedConfiguration.getConfig("ROOT")
		assertNotNull(rootConfig)
		
		ConfigObject neConfig = SiteBasedConfiguration.getConfig("NOEXISTE")
		assertEquals(rootConfig, neConfig)
	}
	
	@Test
	void testGetProperties() {
		String baseStatic = SiteBasedConfiguration.getConfigProperty("MLA", "url").baseStatic
		assertEquals("http://registration.dev.mercadolibre.com.ar:8080/", baseStatic)
		
		baseStatic = SiteBasedConfiguration.getConfigProperty("MTC", "url").baseStatic
		assertEquals("http://localhost:8080/", baseStatic)
	}
}
