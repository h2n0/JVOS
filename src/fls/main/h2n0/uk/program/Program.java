package fls.main.h2n0.uk.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.io.FileIO;
import fls.main.h2n0.uk.screens.ComputerScreen;
import fls.main.h2n0.uk.util.Interpriter;

public class Program {
	
	
	public boolean running = false;
	private String file;
	private String[] fileLines;
	private int currentLine;
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
		this.currentLine ++;
		if(this.currentLine >= this.fileLines.length){
			this.terminate();
			return;
		}
		String line = this.fileLines[this.currentLine];
		String[] secs = line.trim().split(" ");
		for(int i = 0; i < secs.length; i++){
			String p = secs[i];
			p = p.trim();
			if(p.contains("num")){
				getValue(secs[i + 1],buildVar(secs,i + 3));
				break;
			}
		}
	}
	
	private void parse(String pos){
		this.file = FileIO.instance.loadFile(pos);
		this.vars = new HashMap<String,Integer>();
		String[] lines = this.file.split(";");
		this.fileLines = lines;
	}
	
	private void getValue(String var, String line){
		String alteredLine = "";
		if(line.contains("+") || line.contains("-") || line.contains("*") || line.contains("/") || line.contains("^")){//Using ops look for previous functions and resolve
			List<String> secs = new ArrayList<String>();
			String res = "";
			String[] parts = line.split("");
			for(int i = 0; i < parts.length; i++){
				String c = parts[i];
				if(c.equals("") || c.equals(" "))continue;
				if(c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^")){
					secs.add(res);
					secs.add(c);
				}else{
					boolean value = false;
					for(int j = 0; j < this.vars.size(); j++){
						if(this.vars.containsKey(c)){
							value = true;
							break;
						}
					}
					if(value){
						secs.add(""+this.vars.get(c));
					}else{
						secs.add(c);
					}
				}
			}
			for(int i = 0; i < secs.size(); i++){
				String pc = secs.get(i);
				pc = pc.trim();
				if(pc.isEmpty() || pc.equals(" "))continue;
				alteredLine +=pc + " ";
			}
			alteredLine.trim();
		}
		
		if(alteredLine != ""){
			this.vars.put(var,Integer.valueOf(Interpriter.instance.compute(alteredLine)));
		}else this.vars.put(var,Integer.valueOf(line));
	}
	
	private String buildVar(String[] secs, int s){
		String res = "";
		for(int i = s; i < secs.length; i++){
			res += secs[i] + " ";
		}
		return res.trim();
	}
	
	private void addVar(String line){
		
	}
}
