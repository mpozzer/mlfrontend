package com.mercadolibre.frontend.services

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import com.mercadolibre.captcha.ImgWordModel
import com.mercadolibre.captcha.CaptchaGenerator
 
class MLCaptchaService {

	def captchaStorageService
	
	static transactional = false
	
	private String CAPTCHA_EXTENSION = "JPEG"
	private Integer DEFAULT_WIDTH = 140
	private Integer DEFAULT_HEIGHT = 85
	
	/**
	 * Dado un captchaCode, renderiza una imagen, con distorsion aleatoria, correspondiente a la palabra generada.
	 * Esta imagen se muestra al usuario, para que reconozca los caracteres y luego validarlos con 
	 * el metodo {{@link #isValidAnswer(String, String)}
	 * @param captchaCode el codigo asociado a la palabra a renderizar.
	 * @param width ancho de la imagen
	 * @param height la altura de la imagen
	 * @return
	 */
	public ImgWordModel renderImage(captchaCode, Integer width, Integer height) {
		def text = captchaStorageService.get(captchaCode)
		
		ImgWordModel model = new ImgWordModel(
		  text,
		  CAPTCHA_EXTENSION,
		  (width?width.toInteger():DEFAULT_WIDTH),
		  (height?height.toInteger():DEFAULT_HEIGHT),
		  (CH.config.captcha.fontType)?CH.config.captcha.fontType:"bold");
				
        model.generateImage();
		return model
	}

	
	/**
	 * Genera una nueva palabra con letras y/o numeros y devuelve un hash code aleatorio, unico, asociado
	 * a esta frase.
	 * Los caracteres ingresados por el usuario se validan contra este codigo.
	 * 
	 * La longitud de la palabra es configurable por el config file con la propiedad: CH.captcha.word_length
	 * @return captcha code, es aleatorio y unico para la palabra generada.
	 */
	public String generateNewCaptchaCode() {
		def word = CaptchaGenerator.generateWord(CH.config.captcha.word_length)
		
		def captchaKey = URLEncoder.encode(UUID.randomUUID().toString(),"UTF-8")

		captchaStorageService.set(captchaKey, word)

		return captchaKey
	}
	
	/**
	 * Valida los caracteres ingresados por el usuario, para una imagen dada, contra el captcha code correspondiente a la imagen.
	 * Este captcha es invalidado luego de esta validacion. No podra ser usado nuevamente por otro request.
	 * 
	 */
	public Boolean isValidAnswer(String captchaCode, String userResponse) {
		captchaCode = URLDecoder.decode(captchaCode,"UTF-8")
		
		if (!userResponse || !captchaCode)
			return false

		def code = String.valueOf(Math.random())
		
		captchaStorageService.append(captchaCode,code)
		
		def word = captchaStorageService.get(captchaCode)

		return (word).equalsIgnoreCase(userResponse+code);
	}

}
