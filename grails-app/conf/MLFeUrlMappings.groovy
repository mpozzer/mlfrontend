class MLFeUrlMappings {

	static mappings = {
		
		"/css/$site/$appVersion/$resources"(controller:"cascadingStyleSheet")
		
		"/js/$site/$appVersion/$resources"(controller:"javaScript")
		
		"/captcha.jpg"(controller:"captchaImage")
		
	}
}
