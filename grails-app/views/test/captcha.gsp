<ml:html>
  <ml:head>
	  <title>MercadoLibre Captcha words</title>
	  
  </ml:head>
  <ml:body>
		<h1>MercadoLibre Captcha words</h1>
		
		<g:if test="${msg}">
		  <h2>${msg}</h2>
		</g:if>
		
		<form name="captcha" method="post">
		Que ves en la imagen?: <ml:captcha width="300" height="120"/>
		<br>
		<input type="text" name="captcha_response" />
		<br>
		<input type="submit">
		</form>
  </ml:body>
</ml:html>