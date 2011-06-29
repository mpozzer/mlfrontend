class MLFeUrlMappings {

	static mappings = {
		
		"/$app/css/$site/$appVersion?/$resources" {
			controller = "cascadingStyleSheet"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		"/css/$site/$appVersion?/$resources" {
			controller = "cascadingStyleSheet"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		"/$app/js/$site/$appVersion?/$resources" {
			controller = "javaScript"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		"/js/$site/$appVersion?/$resources" {
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
