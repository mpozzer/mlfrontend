class MLFeUrlMappings {

	static mappings = {
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/"(view:"/index")
		"500"(view:'/error')
		
		"/$app/css/$site/$appVersion/$resources" {
			controller = "cascadingStyleSheet"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		"/$app/js/$site/$appVersion/$resources" {
			controller = "javaScript"
			constraints {
				site(matches: /[A-Z]{3}/)
			}
		}
		
		
		"/test/captcha"(controller:"sample"){
			action = [GET:"show", POST:"validateCaptcha"]
		}
	}
}
