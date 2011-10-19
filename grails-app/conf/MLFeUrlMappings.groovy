class MLFeUrlMappings {

	static mappings = {
		
		"/jms/$appSite/$app/css/$site/$appVersion/$resources"(controller:"cascadingStyleSheet")
		
		"/jms/$appSite/$app/js/$site/$appVersion/$resources"(controller:"javaScript")
		
		"/css/$site/$appVersion/$resources"(controller:"cascadingStyleSheet")
		
		"/js/$site/$appVersion/$resources"(controller:"javaScript")
		
		"/captcha.jpg"(controller:"captchaImage")
		
	}
}
