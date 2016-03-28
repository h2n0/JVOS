package fls.main.h2n0.uk.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.io.FileIO;
import fls.main.h2n0.uk.interpreter.Command;
import fls.main.h2n0.uk.screens.ComputerScreen;
import fls.main.h2n0.uk.util.Facts;
import fls.main.h2n0.uk.util.Interpriter;

public class Program {
	
	
	public boolean running = false;
	private String file;
	private String[] fileLines;
	private int currentLine;
	public int[] reg;
	public HashMap<String,Variable> vars;
	public HashMap<String,Function> func;
	private ComputerScreen screen;
	private Function currentFunc;
	
	
	
	public Program(ComputerScreen s,String pos){
		this.reg = new int[64];
		this.screen = s;
		this.currentFunc = null;
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
					getValue(secs[i + 1],"num",buildVar(secs,i + 3));
					break;
				}
			}
		}else if(line.contains("bool")){
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("bool")){
					getValue(secs[i + 1],"bool",buildVar(secs,i + 3));
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
		}else if(line.contains("write")){//Write to screen
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("write")){
					write(buildVar(secs,i + 1));
					break;
				}
			}
		}else if(line.contains("print")){//Print to screen
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("print")){
					print(buildVar(secs,i + 1));
					break;
				}
			}
		}else if(line.contains("clean")){//Cleans the screen
			command("CLEAN");
		}else if(line.contains("clear")){//Clears the screen
			command("CLEAR");
		}else if(line.contains("place")){//Places a pixel on the screen
			String[] secs = line.trim().split(" ");
			for(int i = 0; i < secs.length; i++){
				String p = secs[i];
				p = p.trim();
				if(p.contains("place")){
					command("PLACE " + buildVar(secs,i+1));
					break;
				}
			}
		}else if(line.contains("pause")){//Pauses the script and waits for input
			this.screen.lineBuffer.addLine("Press any key to continue...");
			draw();
			this.screen.lineBuffer.waitForInterupt();
		}else if(line.contains("draw")){//Updates the current display
			draw();
		}else if(line.contains("?")){
			for(int i = 0; i < this.vars.size(); i++){
				String key = ""+this.vars.keySet().toArray()[i];
				System.out.println(key +  " : " + this.vars.get(key).getIValue());
			}
		}else if(line.contains("function")){//Function deceleration
			int sl = this.currentLine;
			int current = this.currentLine;
			while(!this.fileLines[current].contains("}")){
				current++;
				if(current >= this.fileLines.length){
					terminate();
					this.screen.lineBuffer.addLine("Unclosed function");
					return;
				}
			}
			int el = current;
			this.currentLine = el;
		}

		this.currentLine ++;
	}
	
	private void parse(String pos){
		this.file = FileIO.instance.loadFile(pos);
		this.vars = new HashMap<String,Variable>();
		String[] lines = this.file.split(";");
		this.fileLines = lines;
	}
	
	private void getValue(String var,String type, String line){
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
						if(getVariabeWithName(c) != null){
							value = true;
							break;
						}
					}
					if(value){
						secs.add(""+getVariabeWithName(c).getSValue());
					}else{
						secs.add(c);
					}
				}
			}
			for(int i = 0; i < secs.size(); i++){
				String pc = secs.get(i);
				pc = pc.trim();
				if(pc.isEmpty() || pc.equals(" "))continue;
				alteredLine += pc + " ";
			}
			alteredLine = alteredLine.trim();
		}
		int newLine = -1;
		if(type == "bool"){
			newLine = line.contains("true")?1:0;
		}else{
			newLine = alteredLine!=""?Interpriter.instance.compute(alteredLine):Interpriter.instance.compute(line);
		}
		Variable nvar = new Variable(var,type,newLine);
		this.vars.put(var, nvar);
	}
	
	private String buildVar(String[] secs, int s){
		String res = "";
		for(int i = s; i < secs.length; i++){
			res += secs[i] + " ";
		}
		return res.trim();
	}
	
	private void updateValue(String var, String op, String rest){
		rest = fillWithVars(rest);
		Variable cVar = this.getVariabeWithName(var);
		
		if(cVar.isNum()){
			if(op.equals("=")){
				int add = Interpriter.instance.compute(rest);
				setValueToVar(cVar, add);
			}else{
				if(op.contains("+")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setValueToVar(cVar, (current + add));
				}else if(op.contains("-")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setValueToVar(cVar, (current - add));
				}else if(op.contains("/")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setValueToVar(cVar, (current / add));
				}else if(op.contains("*")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setValueToVar(cVar, (current * add));
				}else if(op.contains("^")){
					int current = cVar.getIValue();
					int add = Interpriter.instance.compute(rest);
					setValueToVar(cVar, (current ^ add));
				}
			}
		}else{
			if(op.equals("=")){
				int add = Interpriter.instance.compute(rest);
				setValueToVar(cVar,add % 2);
			}else{
				if(op.contains("!")){
					int current = cVar.getIValue();
					int nw =  current==1?0:1;
					setValueToVar(cVar,nw);
				}
			}
		}
	}
	
	private Variable getVariabeWithName(String var){
		return this.vars.get(var);
	}
	
	private void setValueToVar(Variable var, int val){
		var.setValue(val);
	}
	
	private Function getFunctionWithName(String name){
		return this.func.get(name);
	}
	
	private String fillWithVars(String s){
		String res = "";
		String[] secs = s.split(" ");
		for(int i = 0; i < secs.length; i++){
			String c = secs[i];
			if(getVariabeWithName(c) != null){
				Variable var = getVariabeWithName(c);
				if(var.isNum()){
					res += var.getIValue() + " ";
				}else{
					res += var.getIValue();
				}
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
					res += '"' + Facts.getSDate() + '"' + " ";
				}else if(getter.equals("time")){
					res += '"' + Facts.getSTime() + '"' + " ";
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
		this.screen.lineBuffer.render();
		this.screen.rend.render();
	}
	
	private void command(String cmd){
		cmd = fillWithVars(cmd);
		Command.instance.parseSpecialCommand(screen, cmd);
	}
	
	public void setFunctions(Function f){
		this.currentFunc = f;
	}
}
