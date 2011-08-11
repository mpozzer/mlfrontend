<ml:html compress="true">
	<ml:head>
		<title>MercadoLibre</title>
		<ml:script resources="${['alert']}" async="true" />
		<ml:link resources="${['styles-sprite']}"/>
	</ml:head>
	
	<div id="texto" ></div>
	
	<ml:script async="true" async="true" >
		document.getElementById('texto').innerHTML = document.getElementById('texto').innerHTML + x + "Onload"
	</ml:script>
	
	<ml:script async="true"  defered="true" >
		document.getElementById('texto').innerHTML = document.getElementById('texto').innerHTML + x + "HOLA2"
	</ml:script>
	

	<ml:body>
		<h1>MercadoLibre</h1>
	</ml:body>
</ml:html>