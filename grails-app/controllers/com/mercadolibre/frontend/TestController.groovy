package com.mercadolibre.frontend

class TestController {

   def test = {
	   render(view:"/test/${params.test}")
   }
   
   def testAssert = {
	   render(view:"/test/assert/${params.test}")
   }
   
}
