class MLFeUrlMappings {

	static mappings = {
		
		"/css/$site/$appVersion/$resources" {
			controller = "cascadingStyleSheet"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		"/js/$site/$appVersion/$resources" {
			controller = "javaScript"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		"/captcha.jpg" {
			controller = "captchaImage"
		}
		
	}
}
