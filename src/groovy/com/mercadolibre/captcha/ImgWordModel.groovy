package com.mercadolibre.captcha;

import java.awt.Color
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.media.jai.JAI;

public class ImgWordModel {

	def String txt;
	def int w;
	def int h;
	def type = "JPEG"
	private String status;
	def byte[] bytes;
	def out
	def Color color = new Color(0x333399)

	public ImgWordModel(def text, def fileType, def width, def height) {
		txt = text
		type = fileType
		w = width
		h = height
	}	
	
	public byte[] getBytes() {
		return bytes;
	}

	public void generateImage() {
		try {
			BufferedImage bi = ImgWordUtil.getBufferedImage(txt, w, h, color);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		    JAI.create("encode", bi, outStream, type, null);
			out = outStream
		    bytes = outStream.toByteArray();
		    status = "O";
		} catch ( Exception e ) {
			status = "E";
			throw new RuntimeException("Error generating captcha image", e)
		}
	}
}
