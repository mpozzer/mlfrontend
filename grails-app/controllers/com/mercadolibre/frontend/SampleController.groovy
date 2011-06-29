package com.mercadolibre.frontend

class SampleController {

	def mlCaptchaService
	
   /**
	* sample captcha validation action
	*/
   def validateCaptcha = {
	   if (mlCaptchaService.isValidAnswer(params.captcha_word, params.captcha_response))
		render(view:'/test/captcha', model: ['msg':"La respuesta es correcta!"])
	   else
		render(view:'/test/captcha', model: ['msg':"La respuesta NO es correcta"])
   }
   
   def show = {
	   render(view: "/test/captcha")
   }
}
