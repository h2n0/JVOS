package fls.main.h2n0.uk.screens;

import fls.main.h2n0.uk.util.Renderer;

public class OsScreen extends ComputerScreen {
	
	public OsScreen(Renderer r) {
		super(r);
	}

	public void postInit() {
		this.setFillColor(Renderer.DBlue);
	}
	

	@Override
	public void draw() {
		this.fill();
		this.lineBuffer.render();
		this.rend.render();
	}

	@Override
	public void tick() {
		
	}

}
