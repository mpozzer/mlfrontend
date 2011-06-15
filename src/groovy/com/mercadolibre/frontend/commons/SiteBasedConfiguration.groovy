package com.mercadolibre.frontend.commons

import groovy.util.ConfigObject
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * Handles configurations based on siteId.
 * Defaults to ROOT configuration if no site-specific config is found
 */
class SiteBasedConfiguration {

   public static ConfigObject getConfig(siteId) {
	   return (ConfigurationHolder.config[siteId])?:ConfigurationHolder.config["ROOT"]
   }
   
   public static def getProperty(siteId, property) {
	   return (getConfig(siteId).getAt(property))?:getConfig("ROOT").getAt(property)
   }
   
}
