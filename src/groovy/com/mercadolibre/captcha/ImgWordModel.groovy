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
	def Color fontColor = new Color(0x333399)
    def fontType
	
	public ImgWordModel(def text, def fileType, def width, def height, String fontType) {
		txt = text
		type = fileType
		w = width
		h = height
		this.fontType = fontType
	}	
	
	public byte[] getBytes() {
		return bytes;
	}

	public void generateImage() {
		try {
			BufferedImage bi = CaptchaGenerator.getBufferedImage(txt, w, h, fontColor, fontType);
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
