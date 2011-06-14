<ml:html>
<ml:head>
<ml:link resources="${['styles-sprite', 'wp-base']}" />
</ml:head>
<ml:body>
<ml: script resources="${['test-js', 'test2-js']}" />

<script>
function redirectSearch(query) {
	window.location = '${ml.url(t:"base")}jm/search?siteId=${params.siteId}&as_word=' + escape(query);
}
</script>
</script>
</ml:body>
</ml:html>