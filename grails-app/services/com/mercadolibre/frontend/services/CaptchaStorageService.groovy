package com.mercadolibre.frontend.services

import net.spy.memcached.AddrUtil
import net.spy.memcached.MemcachedClient

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class CaptchaStorageService {

	static transactional = false

	MemcachedClient client = new MemcachedClient(new InetSocketAddress(CH.config.captcha.memcached.hostname, CH.config.captcha.memcached.port))
	
	static def expirationTime = CH.config.captcha.memcached.expirationTime
	static def invalidationTime = CH.config.captcha.memcached.invalidationTime

	public get(key) {
		client.get(key)
	}
	
	public set(key, value){
		client.set(key, expirationTime, value)
	}
	
	public append(key, append){
		client.append(0L, key, append)
	}
	
	public delete(key){
		client.delete(key)
	}

}