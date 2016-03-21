package fls.main.h2n0.uk.screens;

import fls.main.h2n0.uk.interpreters.Command;
import fls.main.h2n0.uk.util.Renderer;
import fls.main.h2n0.uk.util.Text;

public class LineBuffer {

	
	private String[] lines;
	private final int ySlots;
	private int currentLine;
	public String currentBuffer;
	private Renderer rend;
	private int curTick;
	private boolean cur1;
	private boolean use;
	private int curRefresh = 30;
	private int padding;
	private final ComputerScreen screen;
	private boolean waitForInt;
	public LineBuffer(ComputerScreen s,Renderer r){
		int ys = r.h/8;
		this.lines = new String[ys];
		this.ySlots = ys;
		this.currentBuffer = "";
		clearBuffer();
		this.rend = r;
		this.curTick = curRefresh;
		this.cur1 = false;
		this.padding = 0;
		this.screen = s;
		this.waitForInt = false;
		enable();
	}
	
	public void addLine(){
		addLine(this.currentBuffer);
		this.currentBuffer = "";
	}
	
	public void addLine(String line){
		this.lines[this.currentLine] = line;
		this.currentLine++;
		if(this.currentLine >= ySlots){
			this.currentLine--;
			bumpLines();
		}
	}
	
	public void addSpace(){
		this.addLine(" ");
	}
	
	private void bumpLines(){//Bump all of the lines up one place
		for(int i = 1; i < this.lines.length; i++){
			this.lines[i-1] = this.lines[i];
		}
		this.rend.refresh();
	}
	
	public void clearBuffer(){
		for(int i = 0; i < this.lines.length; i++){
			this.lines[i] = "";
		}
		this.currentLine = 0;
	}
	
	public String getLine(int y){
		return this.lines[y];
	}
	
	public void render(){
		if(!use)return;
		for(int y = 0; y < ySlots; y++){
			String c = getLine(y);
			if(y == currentLine){
				if(this.currentBuffer == "")return;
				Text.drawString(this.currentBuffer, this.rend, padding, y, Renderer.White);
			}else{
				if(c == "")continue;
				Text.drawString(c, this.rend, padding, y, Renderer.White);
			}
		}
		
		if(this.cur1){
			int tx = this.currentBuffer.length();
			Text.drawString(Text.cur2, this.rend, tx>0?tx:padding, this.currentLine, Renderer.White);
		}
	}
	
	public void useChar(char c){
		if(!use)return;
		if(waitForInt){
			this.screen.onReturn();
			this.waitForInt = false;
			return;
		}
		if(c == '\u0008'){// Backspace
			if(this.currentBuffer.length() > 0)this.currentBuffer = this.currentBuffer.substring(0, this.currentBuffer.length()-1);
			this.rend.refresh();
		}else if(c == '\u001B'){//ESC
		}else if(c == "\n".charAt(0)){// Enter
			if(this.currentBuffer == ""){
				this.addSpace();
				return;
			}
			this.addLine(Command.instance.parseCommand(this, this.currentBuffer));
			this.currentBuffer = "";
		}else{// Anything else
			this.currentBuffer += c;
		}
		this.rend.refresh();
	}
	
	public void tick(){
		if(!this.use)return;
		if(--this.curTick <= 0){
			this.curTick = curRefresh;
			this.cur1 = !this.cur1;
			this.rend.refresh();
		}
	}
	
	public void diable(){
		this.use = false;
	}
	
	public void enable(){
		this.use = true;
	}
	
	public void setPadding(int pad){
		this.padding = pad;
	}
	
	public void waitForInterupt(){
		this.waitForInt = true;
	}
}
