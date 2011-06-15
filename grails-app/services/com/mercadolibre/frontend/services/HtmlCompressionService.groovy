package com.mercadolibre.frontend.services

import com.googlecode.htmlcompressor.compressor.ClosureJavaScriptCompressor
import com.googlecode.htmlcompressor.compressor.HtmlCompressor

/**
 * This service is responsible for HTML Minification, delegating to {@link http://code.google.com/p/htmlcompressor/}
 * This class configures the directives to customize the minification.
 * TODO: Make the directives configurable externally thru the Config file.
 * @author pduranti
 *
 */
class HtmlCompressionService {
	
	private HtmlCompressor compressor
	
	public HtmlCompressionService() {
	    compressor = new HtmlCompressor();
	    compressor.setEnabled(true);                   //if false all compression is off (default is true)
	    compressor.setRemoveComments(true);            //if false keeps HTML comments (default is true)
		compressor.setRemoveMultiSpaces(true);         //if false keeps multiple whitespace characters (default is true)
		compressor.setRemoveIntertagSpaces(true);      //removes iter-tag whitespace characters
		compressor.setRemoveQuotes(true);              //removes unnecessary tag attribute quotes
		compressor.setSimpleDoctype(false);             //simplify existing doctype
		compressor.setRemoveScriptAttributes(true);    //remove optional attributes from script tags
		compressor.setRemoveStyleAttributes(true);     //remove optional attributes from style tags
		compressor.setRemoveLinkAttributes(true);      //remove optional attributes from link tags
		compressor.setRemoveFormAttributes(true);      //remove optional attributes from form tags
		compressor.setRemoveInputAttributes(true);     //remove optional attributes from input tags
		compressor.setSimpleBooleanAttributes(true);   //remove values from boolean tag attributes
		compressor.setRemoveJavaScriptProtocol(true);  //remove "javascript:" from inline event handlers
		compressor.setRemoveHttpProtocol(false);        //replace "http://" with "//" inside tag attributes
		compressor.setRemoveHttpsProtocol(false);       //replace "https://" with "//" inside tag attributes
		
		compressor.setCompressCss(true);               //compress inline css 
		compressor.setCompressJavaScript(false);        //compress inline javascript
		compressor.setYuiCssLineBreak(8000);             //--line-break param for Yahoo YUI Compressor 
		compressor.setYuiJsDisableOptimizations(true); //--disable-optimizations param for Yahoo YUI Compressor 
		compressor.setYuiJsLineBreak(-1);              //--line-break param for Yahoo YUI Compressor 
		compressor.setYuiJsNoMunge(true);              //--nomunge param for Yahoo YUI Compressor 
		compressor.setYuiJsPreserveAllSemiColons(true);//--preserve-semi param for Yahoo YUI Compressor 
		
		//use Google Closure Compiler for javascript compression
//		compressor.setJavaScriptCompressor(new ClosureJavaScriptCompressor(ClosureJavaScriptCompressor.COMPILATION_LEVEL_SIMPLE));
		
		compressor.setGenerateStatistics(true)           // generate statistics
		
		
	}
	
	String compress(def html) {
        return compressor.compress(html as String)
	}
	
	String getStatistics() {
		return String.format(
			"Compression time	: %,d ms (Original size: %,d bytes, Compressed size: %,d bytes)",
			compressor.getStatistics().getTime(),
			compressor.getStatistics().getOriginalMetrics().getFilesize(),
			compressor.getStatistics().getCompressedMetrics().getFilesize()
	    );
	}

}
