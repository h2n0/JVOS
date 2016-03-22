package fls.main.h2n0.uk.interpreters;

import fls.main.h2n0.uk.screens.ComputerScreen;
import fls.main.h2n0.uk.screens.LineBuffer;
import fls.main.h2n0.uk.util.Facts;
import fls.main.h2n0.uk.util.Interpriter;

public class Command {

	
	public static Command instance = new Command();
	
	private String[] commonComands;
	
	public Command(){
		instance = this;
		this.commonComands = new String[]{"PRINT","TIME","CLEAR"};
	}
	
	public String parseSpecialCommand(ComputerScreen s,String line){
		String[] secs = line.toUpperCase().split(" ");
		String cmd = secs[0].trim();
		String res = "";
		System.out.println(cmd);
		if(isCommon(cmd)){
			parseCommand(s.lineBuffer, line);
		}else{
			s.lineBuffer.addLine(line);
			if(cmd.equals("COLOR")){
				if(secs.length-1 == 2){
					s.setFillColor(Integer.parseInt(secs[1]));
					s.lineBuffer.setTextColor(Integer.parseInt(secs[2]));
				}else{
					s.lineBuffer.addLine("Usage: COLOR (background) (forground)");
				}
			}
		}
		
		return res;
	}
	
	public void parseCommand(LineBuffer s, String line){
		String[] secs = line.toUpperCase().split(" ");
		
		
		String cmd = secs[0].trim();
		String res = "";
		s.addLine(line);
		if(cmd.equals("PRINT")){
			if(secs.length > 1){
				res = buildString(secs, 1);
				//Text.drawString("Answer: " + res,s.rend,5,5,Renderer.White);
			}
		}else if(cmd.equals("CLEAR")){
			s.clearBuffer();
		}else if(cmd.equals("TIME")){
			res = "Current time is: "+ Facts.getSTime();
		}else{
			res = "Unknown Command";
		}
		s.addLine(res);
	}
	
	private boolean isCommon(String cmd){
		for(String com : this.commonComands){
			if(com.contains(cmd))return true;
		}
		return false;
	}
	
	private String buildString(String[] parts,int s){
		String res="";
		if(!Interpriter.instance.isSum(parts)){
			boolean started = false;
			for(int i = s; i < parts.length; i++){
				if(parts[i].indexOf("\"") != -1){
					if(!started){
						started=true;
						boolean single = (i == parts.length-1);
						if (single){
							res += " "+parts[i].substring(parts[i].indexOf("\"")+1,parts[i].length()-1) + " ";
							break;
						}else{
							res += parts[i].substring(parts[i].indexOf("\"")) + " ";
						}
					}else{
						res += parts[i].substring(0,parts[i].indexOf("\""));
						started = false;
					}
				}else{
					res += parts[i] + " ";
				}
			}
			return res.trim();
		}else{
			for(int i = s; i < parts.length; i++){
				res += parts[i] + " ";
			}
			res.trim();
			return ""+Interpriter.instance.compute(res);
		}
	}
}
