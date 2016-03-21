package fls.main.h2n0.uk.screens;

import fls.main.h2n0.uk.util.Facts;
import fls.main.h2n0.uk.util.Renderer;

public class BiosScreen extends ComputerScreen {

	public BiosScreen(Renderer r) {
		super(r);
	}
	
	public void postInit(){
		this.lineBuffer.addSpace();
		this.lineBuffer.addLine("Welcome to JVOS V0.1");
		this.lineBuffer.addLine("The Current time is: "+Facts.getSTime());
		this.lineBuffer.addSpace();
		this.lineBuffer.addLine("This is a small tech demo for hopefully somthign bigger");
		this.lineBuffer.addLine("I started to make this back in March (3rd... ish) 2016");
		this.lineBuffer.addSpace();
		this.lineBuffer.addLine("Press any key to continue");
		this.lineBuffer.setPadding(1);
		this.lineBuffer.waitForInterupt();
		this.rend.fill(Renderer.Black);
	}

	@Override
	public void draw() {
		this.lineBuffer.render();
		this.rend.render();
	}

	@Override
	public void tick() {
		
	}
	
	public void onReturn(){
		this.setScreen(new OsScreen(this.rend));
	}

}
