package fls.main.h2n0.uk.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.main.h2n0.uk.program.Program;
import fls.main.h2n0.uk.util.Renderer;

public abstract class ComputerScreen extends Screen{
	
	
	public Renderer rend;
	protected final int w,h;
	public LineBuffer lineBuffer;
	protected Program prog;
	private int fillColor;
	public ComputerScreen(Renderer r) {
		this.rend = r;
		this.w = r.w;
		this.h = r.h;
		this.lineBuffer = new LineBuffer(this,r);
		this.setFillColor(15);
	}

	@Override
	public void render(Graphics g) {
		this.draw();
		g.drawImage(this.rend.getImage(),0, 0, null);
	}

	@Override
	public void update() {
		this.lineBuffer.tick();
		this.tick();
	}
	
	public void fill(){
		this.rend.fill(this.fillColor);
	}
	
	public abstract void draw();
	
	public abstract void tick();
	
	public void onReturn(){
		
	}
	
	public void keyTyped(char c){
		this.lineBuffer.useChar(c);
	}
	
	public void setFillColor(int col){
		this.fillColor = col;
	}

}
