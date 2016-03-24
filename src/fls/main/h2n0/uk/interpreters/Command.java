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
		this.commonComands = new String[]{"PRINT","TIME","CLEAR","WRITE"};
	}
	
	public void parseSpecialCommand(ComputerScreen s,String line){
		String[] secs = line.toUpperCase().split(" ");
		String cmd = secs[0].trim();
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
			}else if(cmd.equals("PLACE")){
				if(secs.length-1 == 3){
					int x = Integer.parseInt(secs[2]);
					int y = Integer.parseInt(secs[2]);
					int col = Integer.parseInt(secs[3]);
					s.rend.setPixel(x, y, col);
				}else{
					s.lineBuffer.addLine("Usage: PLACE (x) (y) (color)");
				}
				
			}else if(cmd.equals("CLEAN")){
				s.lineBuffer.clearBuffer();
				s.fill();
			}else if(cmd.equals("LOAD")){
				if(secs.length-1 == 1){
					s.loadProgram(buildString(secs,1));
				}else{
					s.lineBuffer.addLine("Usage: LOAD (file)");
				}
					
			}else{
				s.lineBuffer.addLine("Unknown Command");
			}
		}
	}
	
	public void parseCommand(LineBuffer s, String line){
		String[] secs = line.toUpperCase().split(" ");
		String cmd = secs[0].trim();
		String res = "";
		if(cmd.equals("PRINT")){
			s.addLine(line);
			if(secs.length > 1){
				res = buildString(secs, 1);
				//Text.drawString("Answer: " + res,s.rend,5,5,Renderer.White);
			}
		}else if(cmd.equals("CLEAR")){
			s.clearBuffer();
		}else if(cmd.equals("TIME")){
			s.addLine(line);
			res = "Current time is: "+ Facts.getSTime();
		}else if(cmd.equals("WRITE")){
			res = buildString(secs,1);
		}else{
			s.addLine(line);
			res = "Unknown Command";
		}
		
		if(res != "")s.addLine(res);
	}
	
	private boolean isCommon(String cmd){
		for(String com : this.commonComands){
			if(com.equals(cmd))return true;
		}
		return false;
	}
	
	private String buildString(String[] parts,int s){
		String res="";
		if(!Interpriter.instance.isSum(parts)){
			boolean started = false;
			for(int i = s; i < parts.length; i++){
				String c = parts[i];
				if(c.equals("") || c.equals(" "))continue;
				if(parts[i].indexOf("\"") != -1){
					if(!started){
						boolean single = (i == parts.length-1);
						if (single){
							res += " "+c.substring(c.indexOf("\"")+1,c.length()-1) + " ";
							single = false;
							break;
						}else{
							res += c.substring(c.indexOf("\"")) + " ";
						}
					}else{
						res += parts[i].substring(0,c.indexOf("\""));
						started = false;
					}
				}else{
					res += c + " ";
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
