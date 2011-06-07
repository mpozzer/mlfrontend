package com.mercadolibre.captcha

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.font.TextLayout
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.HashMap
import java.util.Random

import org.apache.log4j.Logger

import com.mercadolibre.crypt.Crypter

public class ImgWordUtil {
	
	static final SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	
	private static Random generator = new SecureRandom();
	
	private static Logger log = Logger.getLogger(getClass())
	
	private static float CAPTCHA_WORD_KERNING = -1f

	private static Integer CAPTCHA_WORD_LENGTH = 5
	
	private static Integer CAPTCHA_WORD_LVAR = 0
	
	private static String CAPTCHA_FONTS = "SansSerif;Monospaced;ARIAL;VERDANA;"
	
	/**
	 * 
	 * @param imgText
	 * @param width
	 * @param height
	 * 
	 * @author mpozzer 28/07/2009 modifico tipografia y posicion del captcha
	 * 
	 * @return
	 */
	public static BufferedImage getBufferedImage(String imgText, int width, int height, Color color){
		//Crear imagen (es el container)
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
		
		//Crear el grafico 
		Graphics2D g2d = createGraphic(bi);
		
        //Desde ahora todo lo que se dibuja se dibuja en azul
        g2d.setColor(color);
		//Dibujar las letras, con el espaciado entre letras dado como parametro
		CaptchaWord captchaWord= new CaptchaWord(imgText, getRandomFontName(), CAPTCHA_WORD_KERNING, true);
		captchaWord.drawString(g2d, width, height);
		
		//Deformar el grafico
		shear(g2d, width, height, Color.WHITE);
		
		return bi;
	}
	
	/**
	 * <p>A partir de una imagen crea un grafico seteando propiedades
	 * especificas para su renderizacion, como por ejemplo SIN antiliasing.</p>
	 * 
	 * @param bi
	 * @return
	 */
	private static Graphics2D createGraphic(BufferedImage bi) {
		Graphics2D g2d = bi.createGraphics();
		
		HashMap<Object,Object> hints = new HashMap<Object,Object>();
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g2d.setRenderingHints(hints);
		//Rectangulo blanco
		g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		
		return  g2d;
	}

	private static void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}
	
	/**
	 * <p>Obtiene en forma aleatoria el nombre de uno de los fonts
	 * definidos en el objeto visual: CAPTCHA_FONTS</p>
	 * 
	 * @return nombre de un font
	 * @throws Exception 
	 */
	private static String getRandomFontName(){
		String fontName;
		try {
			String fontsString = CAPTCHA_FONTS;
			String[] fontNames= fontsString.split(";");

			//Excluye el valor length por lo tanto no hay IndexOutOfBounds
			int i= generator.nextInt(fontNames.length); 
			fontName= fontNames[i].trim();
		} catch (Exception e) {
			try {
				log.error(e);
			} catch (Exception e1) {}//No logging
			fontName= "Serif";
		}
		return fontName;
	} 
	
	public static void shearX(Graphics g, int w1, int h1, Color color) {

		int period = generator.nextInt(10) + 5;

		boolean borderGap = true;
		int frames = 15;
		int phase = generator.nextInt(5) + 2;

		for (int i = 0; i < h1; i++) {
			double d = (period >> 1) * Math.sin(
						(double) i / (double) period
							+ (6.2831853071795862D * phase)
								/ frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}
	
	public static void shearY(Graphics g, int w1, int h1, Color color) {

		int period = generator.nextInt(30) + 10; // 50;

		boolean borderGap = true;
		int frames = 15;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (period >> 1) * Math.sin(
						(double) i / (double) period
							+ (6.2831853071795862D * phase)
								/ frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}
    public static void storeImage(byte[] bytes, String fileName) throws Exception {
        File fout = new File(fileName);
        FileOutputStream fos = new FileOutputStream(fout);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }
    
    public static void drawWordWithKerning(Graphics2D g2d, double x0, double y0, String word, float kerning, boolean useRandomKerning) {
    	
    	//Coordenadas para ubicacion espacial
    	Font font = g2d.getFont();
    	FontRenderContext frc= g2d.getFontRenderContext();
    	
    	int wordLength= word.length();
    	Rectangle2D[] charactersBounds= new Rectangle2D[wordLength];
    	String[] characters= new String[wordLength];
    	for (int i = 0; i < wordLength ; i++) {
    		characters[i] = word.substring(i, i+1);
    		charactersBounds[i] = new TextLayout(characters[i], font, frc).getBounds();
        }
    	
    	float actualKerning = 0f;
    	charactersBounds[0].setRect(x0, y0+charactersBounds[0].getY(), charactersBounds[0].getWidth(), charactersBounds[0].getHeight());
        for (int i = 1; i < wordLength; i++) {
        	if(useRandomKerning){
        		float mitigationFactor= 0.5f + generator.nextFloat() / 2f;
        		actualKerning = kerning * mitigationFactor;
        	}else{
        		actualKerning = kerning;
        	}
        	double x= charactersBounds[i-1].getMaxX() + actualKerning; 
        	double y= y0+charactersBounds[i].getHeight() - charactersBounds[i-1].getMaxY(); 
        	double width= charactersBounds[i].getWidth(); 
        	double height= charactersBounds[i].getHeight(); 
            charactersBounds[i].setRect(x, y, width, height);
        }
        
        for(int i= 0; i < wordLength; i++){
        	float x= (float) charactersBounds[i].getX();
        	float y= (float) charactersBounds[i].getY();
        	g2d.drawString(characters[i], x, 50);
        	System.out.println("x: "+x+" y "+y);
        }   
    }
    
    /**
     * <p>Genera una palabra de largo variable. Parametrizable a traves de:
     * <ul><li>CAPTCHA_WORD_LENGTH</li><li>CAPTCHA_WORD_LENGTH_VAR</li></ul>
     * </p>
     * 
     * @author mpozzer 28/07/2009 cantidad de caracteres variables
     * 
     * @return
     * @throws Exception
     */
    public static String generateWord() throws Exception {
    	int minWordLength = CAPTCHA_WORD_LENGTH;
		int wordLengthVariation= CAPTCHA_WORD_LVAR;
		int wordLength = minWordLength
		
		// randomize the work length 
		if (wordLengthVariation != 0) {
		  wordLength = minWordLength + generator.nextInt(wordLengthVariation + 1);
		}
		
    	StringBuilder word = new StringBuilder(wordLength);
		
		for (int j = 0; j < wordLength; j++) {
			word.append(captchars[generator.nextInt(captchars.length)]);
		}
   
    	return word.toString();
    }
	

    public static String encodeUrl(String word, boolean encrypt) throws UnsupportedEncodingException {
    	String kwd;
    	if (encrypt) {
          kwd = URLEncoder.encode(this.encrypt(word), "UTF-8");
        } else {
    	  kwd = URLEncoder.encode(word, "UTF-8");
        }
    	return kwd;
    }
//    public static String URLEncode(String word, String encoding, boolean encrypt) throws UnsupportedEncodingException {
//    	String kwd;
//    	if( encrypt ) {
//    		try {
//    			kwd = URLEncoder.encode(encode(word), encoding);
//			} catch (Exception e) {
//				try {				
//					LibWeb.logError("WordUtil", "URLEncode", "MLA", e);
//				} catch( Exception e1) {
//					e.printStackTrace();
//				}
//				kwd = null;
//			}
//    	} else 
//    		kwd = URLEncoder.encode(word, encoding);
//    	
//    	return kwd;
//    }
	
    public static String decodeURL(String word, boolean decrypt) throws UnsupportedEncodingException {
    	String kwd;
    	if (decrypt) {    		
          kwd = this.decrypt(URLDecoder.decode(word, "UTF-8"));
    	} else 
    	  kwd = URLDecoder.decode(word, "UTF-8");   	
    	return kwd;
    }
	
//    public static String getNewWordURLCoded() throws UnsupportedEncodingException, Exception {
//    	return URLEncode(generateWord(), true);
//    }
//	
//    public static String getNewWordURLCoded(String encoding) throws UnsupportedEncodingException, Exception {
//    	return URLEncode(generateWord(), encoding, true);
//    }
	
    public static String getCurrentPsw() throws Exception {
    	return "MercadoLibre" + fmt.format(System.currentTimeMillis());
    }
	
	public static String decrypt(String crypted) throws Exception {
		return Crypter.decryptDES(crypted, getCurrentPsw());	
	}
	
	public static String encrypt(String code) throws Exception {
		return Crypter.encryptDES(code, getCurrentPsw());
	}
	
    private static char[] captchars = 
		[
		'a',
        'G',
        'F',
		'Y',
		'M',
		'e',
		'b',
		'P',
		'W',
		'E',
		's',
		'L',
		'Z',
		'c',
		'J',
		'O',
		'U',
		'p',
		]
    
    
//	public static void main(String[] args) throws Exception {
//		for(int i=0; i < 6; i++){
//			long start = System.currentTimeMillis();
//	
//			String test = generateWord();
//	
//			BufferedImage bi = ImgWordUtil.getBufferedImage(test, 140, 70);
//			
//			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//			JAI.create("encode", bi, outStream, "PNG", null);
//			//ImageIO.write(bi, "JPG", outStream);
//		    byte[] out = outStream.toByteArray();	
//		    System.out.println("ln: "+out.length);
//
//		    System.out.println("ms: "+(System.currentTimeMillis()-start));
//		
//			storeImage(out, "C:\\Documents and Settings\\mpozzer\\Escritorio\\captcha"+i+"v1.jpg");
//		}
//
//	}
}

