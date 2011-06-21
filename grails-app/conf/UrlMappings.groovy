class UrlMappings {
	static mappings = {
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		
		"500"(view:'/error')
		
		"/test/captcha"(controller:"sample"){
			action = [GET:"show", POST:"validateCaptcha"]
		}
		
		"/test/tags"(view: "/test/htmltags")
	}
	
}
