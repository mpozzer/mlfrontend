package com.mercadolibre.taglib

/**
 * TagLib for ML Captcha words
 * @author pablo
 *
 */
class CaptchaTagLib {

	static namespace = 'captcha'
	
	def mlCaptchaService
	
	def word = { attrs ->
		def height = attrs.height
		def width = attrs.width

		def challengePhrase = mlCaptchaService.getNewChallenge()

		out << "<span id=\"captcha_challenge_phrase_holder\" style=\"display: none;\">"
		out << "  <input type=\"hidden\" name=\"captcha_word\" id=\"captcha_word\" value=\"${challengePhrase}\">"
		out << "</span>"


		out << "<div id=\"captcha_image\">"
		out << "  <img height=\"${height}\" width=\"${width}\" src=\"/captcha.jpg?id=${challengePhrase}&height=${height}&width=${width}\" style=\"display: block;\">"
		out << "</div>"
	}
}
