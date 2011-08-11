class UrlMappings {
	static mappings = {
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/test/$test"(controller:"test",action:"test")
		"/test/$test/assert"(controller:"test",action:"testAssert")
		
		"/"(view:"/index")
		
		"500"(view:'/error')

	}
	
}
