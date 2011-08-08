package com.mercadolibre.captcha;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.security.SecureRandom;

/**
 * <p>Representa una string que se puede dibujar con una separacion
 * determinada de caracteres</p>
 * 
 * @author mpozzer
 * @date 15/09/2009
 */
public class CaptchaWord {
	private static Random generator = new SecureRandom();
	private static float STANDARD_KERNING = 1.0f;
	private float kerning;
	private boolean useRandomKerning;
	private String fontName;
	private final String word;
	private final String[] characters;
	private int fontType
	
	/**
	 * 
	 * @param word
	 * 			la palabra secreta
	 * @param fontName
	 * 			el nombre de la letra
	 * @param kerning
	 * 			Setea la separacion entre cada letra. Este parametro adopta valores variable
	 * 			cuando el flag useRandomKerning esta activado.
	 * @param useRandomKerning
	 * 			Cuando este flag esta activado el kerning especificado varia entre el 70% y 
	 * 			el 100% del su valor absoluto.
	 */
	public CaptchaWord(String word, String fontName, int fontType, float kerning, boolean useRandomKerning) {
		this.fontName = fontName;
		this.kerning = kerning;
		this.useRandomKerning = useRandomKerning;
		this.word = word;
		this.fontType = fontType
		
		//Desarmar la palabra en letras
		int wordLength= word.length();
		characters = new String[wordLength];
		for(int i= 0; i < wordLength; i++){
			characters[i] = word.substring(i, i+1);
		}
	}

	/**
	 * <p>Dibuja la palabra en una posicion aletatoria dentro del grafico.<br/> 
	 * 
	 * @param g2d
	 * @param width
	 * 			es el ancho del grafico
	 * @param height
	 * 			alto del grafico
	 */
	public void drawString(Graphics2D g2d, int width, int height) {
		Font font= createFont(g2d, width);
		
		Rectangle2D[] charactersBounds= recalculateCharacterBounds(g2d, font, width, height);
		for (int i = 0; i < characters.length; i++) {
			float x = (float) charactersBounds[i].getX();
			float y = (float) charactersBounds[i].getY();
			g2d.drawString(characters[i], x, y);
			//System.out.println("x: " + x + " y " + y);
		}
	}
	
	/**
	 * <p>Retorna un array de perimetros rectangulares que representan
	 * los limites de cada letra de la palabra usando la tipografia especificada.</p> 
	 * 
	 * @param g2d
	 * 			es el grafico en el cual se va a dibujar la palabra (se utiliza para obtener datos del contexto) 
	 * @param font
	 * 			es la tipografia a utilizar
	 * @return
	 */
	private Rectangle2D[] getStandardCharacterBounds(Graphics2D g2d, Font font) {
		FontRenderContext frc= g2d.getFontRenderContext();
		int wordLength= word.length();
		Rectangle2D[] charactersBounds = new Rectangle2D[wordLength];
		for (int i = 0; i < wordLength; i++) {
			//Describe caracteristicas espaciales del texto
			TextLayout textLayout= new TextLayout(characters[i], font, frc);
			charactersBounds[i] = textLayout.getBounds();
		}
		
		return charactersBounds;
	}	
	
	/**
	 * <p>Recalcula la posicion de cada caracter para contemplar el kerning dado por el usuario</p>
	 * 
	 * @param g2d
	 * @param font
	 * @param width
	 * @param height
	 * @return
	 */
	private Rectangle2D[] recalculateCharacterBounds(Graphics2D g2d, Font font, int width, int height) {
		Rectangle2D[] charactersBounds= getStandardCharacterBounds(g2d, font);
		
		double x = getRandomX0(g2d, font, width);
		double y = getRandomY0(g2d, font, height);
		double letterWidth = charactersBounds[0].getWidth();
		double letterHeight = charactersBounds[0].getHeight();
		charactersBounds[0].setRect(x, y, letterWidth, letterHeight);
		
		for (int i = 1; i < charactersBounds.length; i++) {
			double actualKerning = calculateKerning();
			x = charactersBounds[i - 1].getMaxX() + actualKerning;
			letterWidth = charactersBounds[i].getWidth();
			letterHeight = charactersBounds[i].getHeight();
			charactersBounds[i].setRect(x, y, letterWidth, letterHeight);
		}
		
		return charactersBounds;
	}

	private double calculateKerning() {
		double actualKerning;
		if (useRandomKerning) {
			double mitigationFactor = 0.7d + generator.nextDouble() /10d * 3d;
			actualKerning = kerning * mitigationFactor;
		} else {
			actualKerning = kerning;
		}
		return actualKerning;
	}

	private double getRandomX0(Graphics2D g2d, Font font, int width) {
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(word, font, frc);
		Rectangle2D bounds = layout.getBounds();
		
		return generator.nextDouble() * ((STANDARD_KERNING - kerning) * (word.length() -1) + (width - bounds.getMaxX()));
	}
	
	private double getRandomY0(Graphics2D g2d, Font font, int height) {
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(word, font, frc);
		Rectangle2D bounds = layout.getBounds();
		
		return (height - bounds.getMaxY())/2*((1.6f)+(0.23f*generator.nextDouble()));
	}

	/**
	 * <p>Define una tipografia a utilizar.<br/>El tamanio seteado es una aproximacion
	 * para maximar el tama�o dentro la imagen.</p>
	 * 
	 * @param g2d
	 * @param width
	 * @return
	 */
	private Font createFont(Graphics2D g2d, int width) {
		Font font= null;
		float wordWidth = width + 1;
		int fontSize = 100; //max
		while (  wordWidth > width * 0.8f) {
//        do{
        	fontSize -= 2;
        	font = new Font(fontName, fontType, fontSize);
        	g2d.setFont( font );
        	wordWidth = g2d.getFontMetrics().stringWidth(word) - ((STANDARD_KERNING - kerning) * (word.length() - 1));
//        } while (  wordWidth > width * .8f);
		}
		
        return font;
	}

	public float getKerning() {
		return kerning;
	}
	
	/**
	 * <p>Setea la separacion entre cada letra. Este parametro 
	 * adopta valores variable cuando el flag useRandomKerning
	 * esta activado.</p>
	 */
	public void setKerning(float kerning) {
		this.kerning = kerning;
	}

	public boolean isUseRandomKerning() {
		return useRandomKerning;
	}

	/**
	 * <p>Cuando este flag esta activado el kerning especificado
	 * varia entre el 70% y el 100% del su valor absoluto.</p> 
	 */
	public void setUseRandomKerning(boolean useRandomKerning) {
		this.useRandomKerning = useRandomKerning;
	}

	/**
	 * Es el nombre de la tipografia a utilizar
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public String getFontName() {
		return fontName;
	}
}
