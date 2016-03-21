package fls.main.h2n0.uk.util;

import java.awt.image.BufferedImage;

public class Renderer {

	private BufferedImage image;
	public int w, h;
	private int[] pixles;
	private int[] color;
	private boolean[] dirtyFlags;

	// All the colors for simplers drawing
	public static final int Black = 0;
	public static final int DBlue = 1;
	public static final int DGreen = 2;
	public static final int DCyan = 3;
	public static final int DRed = 4;
	public static final int DMagenta = 5;
	public static final int DYellow = 6;
	public static final int LGray = 7;
	public static final int HBlue = 9;
	public static final int HGreen = 10;
	public static final int HCyan = 11;
	public static final int HRed = 12;
	public static final int HMagenta = 13;
	public static final int HYellow = 14;
	public static final int White = 15;

	private final int max, min;

	public Renderer(BufferedImage i) {
		this.max = 255;
		this.min = this.max / 2;
		this.image = i;
		this.w = i.getWidth();
		this.h = i.getHeight();
		this.pixles = new int[w * h];
		this.dirtyFlags = new boolean[this.h];
		setupColor();
		refresh();
	}

	public void render() {
		for (int i = 0; i < this.h; i++) {
			if (this.dirtyFlags[i]) {
				this.renderRow(i);
			}
		}
		this.clearDirtyFlags();
	}

	public void fill(int col) {
		for (int y = 0; y < h; y++) {
			if (this.dirtyFlags[y]) {
				for (int x = 0; x < w; x++) {
					if (getPixel(x, y) == shadeToHex(col))
						continue;
					this.setPixel(x, y, col);
				}
			}
		}
	}

	public void clearDirtyFlags() {
		for (int i = 0; i < this.dirtyFlags.length; i++) {
			this.dirtyFlags[i] = false;
		}
	}

	public void refresh() {
		for (int i = 0; i < this.dirtyFlags.length; i++) {
			this.dirtyFlags[i] = true;
		}
	}

	public void setPixel(int x, int y, int col) {
		if (!isValid(x, y))
			return;
		if (col > this.color.length)
			col = this.color.length;
		if (col < 0)
			col = 0;
		this.pixles[x + y * w] = this.color[col];
		this.dirtyFlags[y] = true;
	}

	public BufferedImage getImage() {
		return this.image;
	}

	private int shadeToHex(int shade) {
		int i = (shade & 8) > 0 ? max : min;
		int r = (shade & 4) > 0 ? i : 0;
		int g = (shade & 2) > 0 ? i : 0;
		int b = (shade & 1) > 0 ? i : 0;

		int rgb = makeRGB(r, g, b);
		return rgb;
	}

	private int hexToShade(int hex) {
		int res = 0;
		int r = (hex >> 16) & 0xFF;
		int g = (hex >> 8) & 0xFF;
		int b = (hex) & 0xFF;

		if (r > 0)
			res |= 4;
		if (g > 0)
			res |= 2;
		if (b > 0)
			res |= 1;

		if (r > min || g > min || b > min)
			res |= 8;
		// System.out.println(b);
		return res;
	}

	private int getPixel(int x, int y) {
		if (!isValid(x, y))
			return -1;
		else
			return this.pixles[x + y * w];
	}

	private void setupColor() {
		this.color = new int[16];
		for (int j = 0; j < 16; j++) {
			int i = (j & 8) > 0 ? max : min;
			int r = (j & 4) > 0 ? i : 0;
			int g = (j & 2) > 0 ? i : 0;
			int b = (j & 1) > 0 ? i : 0;
			int rgbi = makeRGB(r, g, b);
			this.color[j] = rgbi;
		}
	}

	public int getShade(int col) {
		if (col < 0)
			return this.color[0];
		if (col > this.color.length)
			return this.color[this.color.length];
		return this.color[col];
	}

	public int[] getPixels(int x, int y, int w, int h) {
		int[] res = new int[w * h];
		for (int xx = 0; xx < w; xx++) {
			for (int yy = 0; yy < h; yy++) {
				int ax = x + xx;
				int ay = y + yy;
				if (!isValid(ax, ay))
					continue;
				res[xx + yy * w] = this.pixles[ax + ay * this.w];
			}
		}
		return res;
	}

	public void renderRow(int y) {
		if (y < 0 || y > this.h)
			return;
		int ry = y;
		int[] rowPix = getPixels(0, ry, this.w, 1);
		this.image.setRGB(0, ry, this.w, 1, rowPix, 0, this.w);
	}
	
	public void renderRowSec(int y){
		for(int i = 0; i <= 8; i++){
			renderRow(y + i);
		}
	}

	private boolean isValid(int x, int y) {
		if (x < 0 || y < 0 || x >= this.w || y >= this.h)
			return false;
		return true;
	}

	private int makeRGB(int r, int g, int b) {
		return (r << 16) | (g << 8) | b;
	}
	
	private int makeBGR(int r, int g, int b){
		return makeRGB(b,g,r);
	}

}