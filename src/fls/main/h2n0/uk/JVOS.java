package fls.main.h2n0.uk;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import fls.main.h2n0.uk.screens.BiosScreen;
import fls.main.h2n0.uk.screens.InitiallScreen;
import fls.main.h2n0.uk.screens.OsScreen;

public class JVOS extends Init{
	
	
	public JVOS(){
		super("JVOS V0.1",640,480);
		this.useCustomBufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		this.setImageScale(1);
		this.setInput(new Input(this,Input.KEYS,Input.MOUSE));
		this.skipInit();
		this.setScreen(new InitiallScreen());
		this.showFPS();
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);
	}
	
	public static void main(String[] args){
		new JVOS().start();
	}

}
