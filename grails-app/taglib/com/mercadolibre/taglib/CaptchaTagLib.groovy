package com.mercadolibre.taglib

/**
 * TagLib for ML Captcha words
 * @author pablo
 *
 */
class CaptchaTagLib {

	static namespace = 'ml'
	
	def mlCaptchaService
	
	def captcha = { attrs ->
	    def urlBase = attrs.remove('urlBase')?:'/'
		
		def height = attrs.height
		def width = attrs.width

		def challengePhrase = mlCaptchaService.getNewChallenge()

		out << "  <img name=\"captcha_image\" height=\"${height}\" width=\"${width}\" src=\"${urlBase}captcha.jpg?id=${challengePhrase}&height=${height}&width=${width}\" >"
		out << "  <input type=\"hidden\" name=\"captcha_word\" value=\"${challengePhrase}\">"
	}
}
