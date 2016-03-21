package fls.main.h2n0.uk.interpreters;

import java.util.HashMap;

import fls.engine.main.io.FileIO;
import fls.main.h2n0.uk.screens.ComputerScreen;
import fls.main.h2n0.uk.util.Renderer;
import fls.main.h2n0.uk.util.Text;

public class Program {
	
	private boolean running;
	private boolean finished;
	private int currentLine;
	private int finalLine;
	private HashMap<Integer, String> lines;
	private ComputerScreen screen;
	
	public Program(ComputerScreen s, String pos){
		this.screen = s;
		this.running = false;
		this.finished = false;
		this.currentLine = 0;
		this.lines = new HashMap<Integer, String>();
		String[] fileConts = FileIO.instance.loadFile(pos).split("\n");
		for(int i = 0; i < fileConts.length; i++){
			String line = fileConts[i];
			line = line.trim();
			String[] parts = line.split(" ");
			int lineNum = Integer.parseInt(parts[0]);
			String cmd = "";
			for(int j = 1; j < parts.length; j++){
				cmd +=  parts[j] + " ";
			}
			if(lineNum > this.finalLine)this.finalLine = lineNum;
			lines.put(lineNum, cmd.trim());
		}
	}
	
	public void exec(){
		this.running = true;
	}
	
	public void nextLine(){
		if(this.finished)return;
		if(this.lines.get(++currentLine) != null){
			parseLine(this.lines.get(this.currentLine));
		}
		if(this.currentLine >= this.finalLine){
			this.finished = true;
		}
	}
	
	private void parseLine(String line){
		String[] segs = line.split(" ");
		for(int i = 0; i < segs.length; i++){
			String c = segs[i].toLowerCase();
			if(c == "print"){
				Text.drawString(makeString(segs,i+1), this.screen.rend, 0, 0, Renderer.White);
			}
		}
		
	}
	
	private String makeString(String[] parts, int start){
		String res = "";
		for(int i = start; i < parts.length; i++){
			res += parts[i] + " ";
		}
		return res.trim();
	}
}
