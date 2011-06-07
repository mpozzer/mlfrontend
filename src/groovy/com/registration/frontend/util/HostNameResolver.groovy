package com.registration.frontend.util

class HostNameResolver {

	static def hostName

	def static getHostName(){
		if(!hostName){
			hostName =  System.getenv()['HOSTNAME']
		}
		return hostName
	}
}
