package com.mercadolibre.frontend.services

import net.spy.memcached.MemcachedClient

import org.springframework.beans.factory.InitializingBean

class CaptchaStorageService implements InitializingBean {

	static transactional = false
	
	def hostname
	def port
	def expirationTime

	MemcachedClient client
	

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
	
	public void afterPropertiesSet() throws Exception {
		client = new MemcachedClient(new InetSocketAddress(hostname, port))
	}

}