<ml:html compress="true">
<ml:head>
<ml:link resources="${['wp-base']}" />
</ml:head>
<ml:body>
<ml:script resources="${['test-js']}" onload="true" >
  alert('callback 1 called')
</ml:script>

<ml:script resources="${['test2-js']}" async="true" >
  alert('callback 2 called')
</ml:script>

<script>
function redirectSearch(query) {
	window.location = "http://www.google.com"
}
</script>
</ml:body>
</ml:html>