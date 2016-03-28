package fls.main.h2n0.uk.util;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;

public class Text{
	
	private static BufferedImage fontImage = new SplitImage("/images/font.png").load();
	private static BufferedImage[][] fontSecs = new SplitImage(Text.fontImage).split(8,8);
	private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"ё$%^&*()-_=+[]{},.'/\\?~#|@<>;:ье ";
	public static final String cur1 = "ь";
	public static final String cur2 = "е";
	
	public void setColor(Renderer r,int shade){
		int w = Text.fontImage.getWidth();
		int h = Text.fontImage.getHeight();
		int[] pixels = new int[w * h];
		for(int i = 0; i < pixels.length; i++){
			if(pixels[i] == 0xFFFFFF)pixels[i] = r.getShade(shade);
		}
		BufferedImage tmp = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		tmp.setRGB(0, 0, w, h, pixels, 0, w);
		Text.fontSecs = new SplitImage(tmp).split(8, 8);
	}
	
	public static void drawString(String msg, Renderer r, int x,int y, int shade){
		String[] lines = msg.split("\n");
		int i = 0;
		x*=8;
		y*=8;
		for(String line:lines){
			line = line.toUpperCase();
			int length = line.length();
			for(int j = 0; j < length; j++){
				int pos = letters.indexOf(line.charAt(j));
				if(pos == -1 || pos >= letters.length())return;
				int tx = pos % 9;
				int ty = pos / 9;
				int[] secPix = new int[8*8];
				Text.fontSecs[tx][ty].getRGB(0, 0, 8, 8, secPix, 0, 8);
				for(int l = 0; l < 8*8; l++){
					int dx = l % 8;
					int dy = l / 8;
					if(secPix[dx + dy * 8] > 0xFFFFFFFF)continue;
					r.setPixel((j*8) + x + dx, (i*8)+y + dy, shade);
				}
			}
			i++;
		}
	}

}
