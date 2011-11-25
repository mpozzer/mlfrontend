package com.mercadolibre.frontend.services

import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import com.googlecode.htmlcompressor.compressor.HtmlCompressorStatistics
import com.yahoo.platform.yui.compressor.JavaScriptCompressor

/**
 * This service is responsible for compress 
 * HTML delegating to {@link http://code.google.com/p/htmlcompressor/}
 * @author pduranti
 * @author pmoretti
 *
 */
class CompressionService {

	static transactional = false
	
	def htmlCompressor = buildHtmlCompressor()

	def compressJavaScript(Reader javascript) {
		JavaScriptCompressor compressor = new JavaScriptCompressor(javascript, null)
		StringWriter write = new StringWriter()
		compressor.compress(write, 8000, false, false, true, false)
		write.flush()
		return write.getBuffer().toString()
	}

	def compressJavaScript(String script) {
		return compressJavaScript(new StringReader(script.toString()));
	}

	def compressHTML(String html) {
		HtmlCompressor htmlCompressor =  buildHtmlCompressor();
		return htmlCompressor.compress(html);
	}


	private HtmlCompressor buildHtmlCompressor(){

		HtmlCompressor compressor = new HtmlCompressor();
		compressor.setRemoveComments(true);            //if false keeps HTML comments (default is true)
		compressor.setCompressCss(true);               //compress inline css
		compressor.setEnabled(true);                   //if false all compression is off (default is true)
//		compressor.setRemoveIntertagSpaces(true);      //removes iter-tag whitespace characters
//		compressor.setRemoveScriptAttributes(true);    //remove optional attributes from script tags
//		compressor.setRemoveStyleAttributes(true);     //remove optional attributes from style tags
//		compressor.setRemoveLinkAttributes(true);      //remove optional attributes from link tags
//		compressor.setRemoveJavaScriptProtocol(true);  //remove "javascript:" from inline event handlers
//
//		compressor.setRemoveMultiSpaces(false);         //if false keeps multiple whitespace characters (default is true)
//		compressor.setRemoveQuotes(false);              //removes unnecessary tag attribute quotes
//		compressor.setSimpleDoctype(false);             //simplify existing doctype
//		compressor.setRemoveFormAttributes(false);      //remove optional attributes from form tags
//		compressor.setRemoveInputAttributes(false);     //remove optional attributes from input tags
//		compressor.setSimpleBooleanAttributes(false);   //remove values from boolean tag attributes
//		compressor.setRemoveHttpProtocol(false);        //replace "http://" with "//" inside tag attributes
//		compressor.setRemoveHttpsProtocol(false);       //replace "https://" with "//" inside tag attributes
//
//		compressor.setCompressJavaScript(false);        //compress inline javascript
//		compressor.setYuiCssLineBreak(8000);             //--line-break param for Yahoo YUI Compressor
//		compressor.setYuiJsDisableOptimizations(true); //--disable-optimizations param for Yahoo YUI Compressor
//		compressor.setYuiJsLineBreak(-1);              //--line-break param for Yahoo YUI Compressor
//		compressor.setYuiJsNoMunge(true);              //--nomunge param for Yahoo YUI Compressor
//		compressor.setYuiJsPreserveAllSemiColons(true);//--preserve-semi param for Yahoo YUI Compressor
//
//		compressor.setGenerateStatistics(false)           // generate statistics
		return compressor
	}
}
