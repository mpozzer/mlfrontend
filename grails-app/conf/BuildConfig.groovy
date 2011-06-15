grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        mavenRepo "http://git.ml.com:8081/nexus/content/groups/Arquitectura"
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "https://oss.sonatype.org/content/groups/public"
        mavenRepo "http://repository.jboss.com/maven2/"
		mavenRepo "http://10.10.42.47:8180/nexus/content/groups/public"
		mavenRepo "http://git.ml.com:8081/nexus/content/groups/Arquitectura"
		mavenRepo "http://git.ml.com:8081/nexus/content/groups/public"
		mavenRepo "http://git.ml.com:8081/nexus/content/groups/public_"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    	compile ("com.mercadolibre.opensource.frameworks:restclient:0.5.11") { excludes "groovy" }
		compile 'com.yahoo.platform.yui:yuicompressor:2.3.6'
		compile 'javax.media:jai-core:1.1.3'
		compile 'com.sun.media:jai-codec:1.1.3'
		compile('com.google.guava:guava:r09')
		compile('com.google.javascript:closure-compiler:r1043') { excludes "ant" }
		
        // runtime 'mysql:mysql-connector-java:5.1.13'
    }
}
