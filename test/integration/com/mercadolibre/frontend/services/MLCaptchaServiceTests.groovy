package com.mercadolibre.frontend.services;

import static org.junit.Assert.*;

import org.junit.Test;

class MLCaptchaServiceTests {

	def mlCaptchaService
	
	@Test
	public void testGetNewChallenge() {
		def captchaKey = mlCaptchaService.getNewChallenge()
	}

	@Test
	public void testIsValidAnswer() {
		fail("Not yet implemented");
	}

}
