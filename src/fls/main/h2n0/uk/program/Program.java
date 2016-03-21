package fls.main.h2n0.uk.program;

import java.util.HashMap;

import fls.engine.main.io.FileIO;
import fls.main.h2n0.uk.screens.ComputerScreen;

public class Program {
	
	
	public boolean running = false;
	private String file;
	public int[] reg;
	public HashMap<String,Integer> vars;
	private ComputerScreen screen;
	public Program(ComputerScreen s,String pos){
		this.reg = new int[64];
		this.screen = s;
		parse(pos + ".prg");
		start();
	}
	
	public void terminate(){
		this.running = false;
	}
	
	public void start(){
		running = true;
	}
	
	public void nextLine(){
		if(!running)return;
	}
	
	private void parse(String pos){
		this.file = FileIO.instance.loadFile(pos);
		this.vars = new HashMap<String,Integer>();
		String[] lines = this.file.split(";");
		for(String line : lines){
			int next = -1;
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(i == next){
					System.out.println(p);
					next = -1;
				}
				if(p.contains("num"))next=i+3;
			}
		}
	}
}
