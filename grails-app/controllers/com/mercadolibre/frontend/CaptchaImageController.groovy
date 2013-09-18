
package com.mercadolibre.frontend

import com.mercadolibre.captcha.ImgWordModel


/**
 * Controller to create and render a captcha image
 * @author pablo.duranti@mercadolibre.com
 *
 */
class CaptchaImageController {

	private String CONTENT_TYPE = "image/jpg"
	
	def mlCaptchaService
	
	def index = {
		String captchaCode = params.id;
		Integer w = null;
		Integer h = null;
		try{
			w = params.width.toInteger()
			h = params.height.toInteger()
			w = w <= 350 ? w : 350;
			h = h <= 150 ? h : 150;
		}catch(Exception e){
			log.error("Error parsing captcha parameters width: ${params.width} and height: ${params.height}",e)
		}	
		ImgWordModel imgModel = mlCaptchaService.renderImage(captchaCode, w, h)

		response.setHeader("Content-type", CONTENT_TYPE)
		response.setIntHeader("Content-length", imgModel.bytes.length)
		response.setHeader("Cache-Control","public, max-age=86400")
		response.outputStream << imgModel.bytes
		response.outputStream.flush()
	}

	def getContentType() {
		CONTENT_TYPE
	}
	
}
