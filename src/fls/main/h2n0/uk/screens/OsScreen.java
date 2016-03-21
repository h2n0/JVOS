package fls.main.h2n0.uk.screens;

import fls.main.h2n0.uk.program.Program;
import fls.main.h2n0.uk.util.Renderer;

public class OsScreen extends ComputerScreen {
	
	public OsScreen(Renderer r) {
		super(r);
	}

	public void postInit() {
		this.prog = new Program(this,"test");
	}
	

	@Override
	public void draw() {
		this.rend.fill(Renderer.DBlue);
		this.lineBuffer.render();
		this.rend.render();
	}

	@Override
	public void tick() {
		
	}

}
