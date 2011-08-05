package com.mercadolibre.frontend.services;

import static org.junit.Assert.*

import org.junit.Test

class MLCaptchaServiceTests extends GroovyTestCase {

	def mlCaptchaService
	def captchaStorageServiceStub
	
	@Test
	public void testDoubleValidationFails() {
		def captchaKey = mlCaptchaService.getNewChallenge()
		def word = captchaStorageServiceStub.get(captchaKey)
		
		assertTrue mlCaptchaService.isValidAnswer(captchaKey, word)
		
		assertFalse mlCaptchaService.isValidAnswer(captchaKey, word) 
	}
	
	@Test
	public void testValidationOK() {
		def captchaKey = mlCaptchaService.getNewChallenge()
		def word = captchaStorageServiceStub.get(captchaKey)
		
		assertTrue mlCaptchaService.isValidAnswer(captchaKey, word)
	}
	
	@Test
	public void testDoubleValidationWithRandomCode() {
		def captchaKey = mlCaptchaService.getNewChallenge()
		def word = captchaStorageServiceStub.get(captchaKey)
		
		assertTrue mlCaptchaService.isValidAnswer(captchaKey, word)
		
		def wordWithCode = captchaStorageServiceStub.get(captchaKey)
		
		assertTrue mlCaptchaService.isValidAnswer(captchaKey, wordWithCode)
	}

}
