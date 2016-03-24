package fls.main.h2n0.uk.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.io.FileIO;
import fls.main.h2n0.uk.interpreters.Command;
import fls.main.h2n0.uk.screens.ComputerScreen;
import fls.main.h2n0.uk.util.Facts;
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
		if(!running || this.screen.lineBuffer.waitingForInterupt())return;
		if(this.currentLine >= this.fileLines.length){
			this.terminate();
			return;
		}
		String line = this.fileLines[this.currentLine];
		
		if(line.contains("num")){//Deceleration of variables
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("num")){
					getValue(secs[i + 1],buildVar(secs,i + 3));
					break;
				}
			}
		}else if(line.contains("=")){//Updating variables
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("=")){
					updateValue(secs[i - 1],secs[i],buildVar(secs,i + 1));
					break;
				}
			}
		}else if(line.contains("write")){
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("write")){
					write(buildVar(secs,i + 1));
					break;
				}
			}
		}else if(line.contains("print")){
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("print")){
					print(buildVar(secs,i + 1));
					break;
				}
			}
		}else if(line.contains("clean")){
			command("CLEAN");
		}else if(line.contains("clear")){
			command("CLEAR");
		}else if(line.contains("place")){
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("place")){
					command("PLACE " + buildVar(secs,i+1));
					break;
				}
			}
		}else if(line.contains("pause")){
			this.screen.lineBuffer.addLine("Press any key to continue...");
			draw();
			this.screen.lineBuffer.waitForInterupt();
		}else if(line.contains("draw")){
			draw();
		}

		this.currentLine ++;
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
			alteredLine = alteredLine.trim();
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
	
	private void updateValue(String var, String op, String rest){
		if(op.contains("+")){
			int current = getValueFromVar(var);
			int add = Interpriter.instance.compute(rest);
			setValueToVar(var, current + add);
		}else if(op.contains("-")){
			int current = getValueFromVar(var);
			int add = Interpriter.instance.compute(rest);
			setValueToVar(var, current - add);
		}
	}
	
	private int getValueFromVar(String var){
		return this.vars.get(var);
	}
	
	private void setValueToVar(String var, int val){
		this.vars.put(var, val);
	}
	
	private String fillWithVars(String s){
		String res = "";
		String[] secs = s.split(" ");
		for(int i = 0; i < secs.length; i++){
			String c = secs[i];
			if(this.vars.get(c) != null){
				res += this.getValueFromVar(c) + " ";
			}else{
				res += c + " ";
			}
		}
		return lookForSpecials(res);
	}
	
	private String lookForSpecials(String line){
		String res = "";
		String[] secs = line.split(" ");
		for(int i = 0; i < secs.length; i++){
			String c = secs[i];
			if(c.contains("$")){
				String getter = c.substring(1);
				getter = getter.toLowerCase();
				if(getter.equals("date")){
					res += Facts.getSDate() + " ";
				}else if(getter.equals("time")){
					res += "'" + Facts.getSTime() + "'" + " ";
				}
			}else{
				res += c + " ";
			}
		}
		return res.trim();
	}
	
	private void print(String phrase){
		phrase = fillWithVars(phrase);
		command("PRINT "+phrase);
	}
	
	private void write(String phrase){
		phrase = fillWithVars(phrase);
		command("WRITE "+phrase);
	}
	
	private void draw(){
		this.screen.fill();
		this.screen.lineBuffer.render();
		this.screen.rend.render();
	}
	
	private void command(String cmd){
		cmd = fillWithVars(cmd);
		System.out.println(cmd);
		Command.instance.parseSpecialCommand(screen, cmd);
	}
}
