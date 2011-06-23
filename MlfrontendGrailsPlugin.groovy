import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import com.mercadolibre.frontend.ResourceController
import grails.util.Environment

class MlfrontendGrailsPlugin {
	// the plugin version
	def version = "0.8.4"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3.7 > *"
	// the other plugins this plugin depends on
	def dependsOn = [:]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp",
		"grails-app/views/html/index.gsp",
		"grails-app/conf/UrlMappings.groovy"
	]

	// TODO Fill in these fields
	def author = "Pablo Moretti / Pablo Duranti"
	def authorEmail = "pablo.moretti@mercadolibre.com / pablo.duranti@mercadolibre.com "
	def title = "MercadoLibre Frontend"
	def description = ""

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/mlfrontend"

	def doWithWebDescriptor = { xml ->
		def filters = xml.'filter'
		filters[filters.size() - 1] + {
			'filter'{
				'filter-name'('MlFilter')
				'filter-class'('org.springframework.web.filter.DelegatingFilterProxy')
				'init-param' {
					'param-name'('targetBeanName')
					'param-value'('mlParamsAwareFilter')
				}
				'init-param' {
					'param-name'('targetFilterLifecycle')
					'param-value'('true')
				}
			}
		}

		def mappings = xml.'filter-mapping'
		mappings[mappings.size() - 1] + {
			'filter-mapping'{
				'filter-name'('MlFilter')
				'url-pattern'('/*')
				'dispatcher'('REQUEST')
				'dispatcher'('FORWARD')
			}
		}
	}

	def doWithSpring = {

		simpleRestClient(com.mercadolibre.opensource.frameworks.restclient.SimpleRestClient) { bean ->

			if(Environment.current == Environment.PRODUCTION){
				baseUrl = "http://internal.mercadolibre.com"
			}
			else{
				baseUrl = "https://api.mercadolibre.com"
			}
			soTimeout = 1000
		}

		mlCaptchaService(com.mercadolibre.captcha.MLCaptchaService)

		mlParamsAwareFilter(com.mercadolibre.filters.MLParamsAwareFilter)

		mlDomainsResolver(com.mercadolibre.frontend.services.MLDomainsResolver) { bean ->
			bean.dependsOn = ["simpleRestClient"]
		}

		htmlCompressionService(com.mercadolibre.frontend.services.HtmlCompressionService)

		groovyTemplateEngine(org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine)

	}

	def doWithDynamicMethods = { ctx ->
		/**
		 * Encodes a URI using UTF-8 if no other encoding is provided
		 */
		String.metaClass.encodeURIComponent = { anEncode ->
			return URLEncoder.encode(delegate,(anEncode?:CH.config.grails.views.gsp.encoding).toString())
		}
		ResourceController.metaClass.noCompress = {
			->
			return Boolean.valueOf(params.noCompress)
		}
		// TODO Implement registering dynamic methods to classes (optional)
	}

	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)
	}

	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}
}
