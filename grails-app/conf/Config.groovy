// configuration for plugin testing - will not be included in the plugin zip

grails.app.context = "/"

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
	
}

plugin {
  excludes = "hibernate, domain"
}

captcha {
	fontType = "plain"
	word_length = 5
    alphabet = 'aGFYMeBPWEsLZcJOUpXgHkQrtv123456789'
	memcached {
		hostname = "172.16.139.87"
		port = 11211
		expirationTime = 1800 //30min
	}
}

ROOT {
	url {
		baseStatic = 'http://localhost:8080'
	}
}
MLA {
	url {
		baseStatic = 'http://registration.dev.mercadolibre.com.ar:8080'
	}
}
