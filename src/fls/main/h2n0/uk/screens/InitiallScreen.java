package fls.main.h2n0.uk.screens;

import java.awt.Graphics;

import fls.engine.main.screen.Screen;
import fls.main.h2n0.uk.util.Renderer;

public class InitiallScreen extends Screen {
	
	private Renderer r;
	public void postInit(){
		this.r = new Renderer(this.game.image);
	}

	@Override
	public void render(Graphics g) {

	}

	@Override
	public void update() {
		setScreen(new BiosScreen(this.r));
	}

}
