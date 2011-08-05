package com.mercadolibre.frontend.services

class CacheServiceStub {

	static transactional = false
	
	private static cache = [:]

	public get(key) {
		cache[key]
	}
	
	public set(key, value){
		cache[key] = value
	}
	
	public append(key, append){
		cache[key] = cache[key] + append
	}
}