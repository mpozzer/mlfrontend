package com.mercadolibre.frontend.services

import java.util.regex.Matcher
import javax.servlet.http.Cookie
import org.springframework.web.context.request.RequestContextHolder

/**
 * Este servicio reemplaza {@code com.studentuniverse.grails.plugins.cookie.services.CookieService}
 * para resolver un problema con la cookie de trackinfo que contiene ':' en el value
 */
class CookieService {

    boolean transactional = false
	
	/* Gets the value of the named cookie.  Returns null if does not exist */
    
	String get(String name) {
		if ("track_info".equals(name)) {
		    return getTrackInfo()	
		}
		
		Cookie cookie = getCookie(name)
		if ( cookie ) {
			def value = cookie.getValue()
			log.info "Found cookie \"${name}\", value = \"${value}\""
            return value
		}
		else {
			log.info "No cookie found with name: \"${name}\""
			return null
		}
    }

	/**
	 * Levanta la cookie de trackinfo con una expresion regular de los headers
	 * con una expresion regular
	 * (pduranti)
	 * @return
	 */
	public getTrackInfo() {
		Matcher m = RequestContextHolder.currentRequestAttributes().request.getHeader('Cookie') =~ /track_info=(.*?);/
		def trackInfoId = (m.find())?m[0][1]:''
		return trackInfoId
	}
	
    /* Sets the cookie with name to value, with maxAge in seconds */
   
    void set(response,name,value,maxAge) {
		log.info "Setting cookie \"${name}\" to: \"${value}\" with maxAge: ${maxAge} seconds"
        setCookie(response,name,value,maxAge)
    }

    /* Deletes the named cookie. */
    
	void delete(response,name) {
        log.info "Removing cookie \"${name}\""
        setCookie(response,name,null,0)
    }

    /** ***********************************************************/
	
	private void setCookie(response,name,value,maxAge) {
		Cookie cookie = new Cookie(name, value)
        cookie.setMaxAge(maxAge)
        response.addCookie(cookie)
    }
	
	/** ***********************************************************/
    
	private Cookie getCookie(name) {
        def cookies = RequestContextHolder.currentRequestAttributes().request.getCookies()
        if (cookies == null || name == null || name.length() == 0) {
            return null
        }
        // Otherwise, we have to do a linear scan for the cookie.
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(name)) {
                return cookies[i]
            }
        }
        return null;
    }

}
