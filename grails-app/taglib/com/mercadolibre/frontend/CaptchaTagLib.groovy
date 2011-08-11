package com.mercadolibre.frontend

/**
 * TagLib to render MercadoLibre Captcha
 * @author pduranti
 *
 */
class CaptchaTagLib {

	static namespace = 'ml'
	
	def mlCaptchaService
	
	def captcha = { attrs ->
	    def urlBase = attrs.remove('urlBase')?:'/'
		
		def height = attrs.height
		def width = attrs.width

		def captchaCode = mlCaptchaService.generateNewCaptchaCode()

		out << "  <img name=\"captcha_image\" height=\"${height}\" width=\"${width}\" src=\"${urlBase}captcha.jpg?id=${captchaCode}&height=${height}&width=${width}\" >"
		out << "  <input type=\"hidden\" name=\"captcha_word\" value=\"${captchaCode}\">"
	}
}
