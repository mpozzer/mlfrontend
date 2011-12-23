import grails.util.Environment

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.aop.scope.ScopedProxyFactoryBean

import com.mercadolibre.frontend.MLHTMLContext

class MlfrontendGrailsPlugin {
	// the plugin version
	def version = "1.1.2"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3.7 > *"
	// the other plugins this plugin depends on
	def dependsOn = [:]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp",
		"grails-app/views/html/index.gsp",
		"grails-app/conf/UrlMappings.groovy",
		"grails-app/controllers/com/mercadolibre/frontend/TestController.groovy",
		"grails-app/controllers/com/mercadolibre/frontend/CaptchaTestController",
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
			'filter-mapping'{
				'filter-name'('charEncodingFilter')
				'url-pattern'('/*')
			}
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

		if(Environment.current == Environment.PRODUCTION){
			captchaStorage(com.mercadolibre.frontend.services.CaptchaStorageService) { bean ->
				hostname = CH.config.captcha.memcached.hostname ?: "localhost"
				port = CH.config.captcha.memcached.port ?: 11211
				expirationTime = CH.config.captcha.memcached.expirationTime ?: 1800 //30min
			}
		} else {
			captchaStorage(com.mercadolibre.frontend.services.CaptchaStorageServiceStub)
		}

		mlCaptchaService(com.mercadolibre.frontend.services.MLCaptchaService) { captchaStorageService = ref("captchaStorage") }

		mlParamsAwareFilter(com.mercadolibre.filters.MLParamsAwareFilter)

		mlDomainsResolver(com.mercadolibre.frontend.services.MLDomainsResolver) { bean ->
			bean.dependsOn = ["simpleRestClient"]
		}

		compressionService(com.mercadolibre.frontend.services.CompressionService)


		MLHTMLContextNoProxy(MLHTMLContext) { bean ->
			bean.scope = "request"
		}

		MLHTMLContext(ScopedProxyFactoryBean)  {
			targetBeanName = 'MLHTMLContextNoProxy'
			proxyTargetClass = true
		}

	}

	def doWithDynamicMethods = { ctx ->
		/**
		 * Encodes a URI using UTF-8 if no other encoding is provided
		 */
		String.metaClass.encodeURIComponent = { anEncode ->
			return URLEncoder.encode(delegate,(anEncode?:CH.config.grails.views.gsp.encoding).toString())
		}

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
