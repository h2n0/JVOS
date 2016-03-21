package fls.main.h2n0.uk.interpreters;

import java.util.Stack;

import fls.main.h2n0.uk.screens.LineBuffer;
import fls.main.h2n0.uk.util.Facts;

public class Command {

	
	public static Command instance = new Command();
	
	public Command(){
		instance = this;
	}
	
	public String parseCommand(LineBuffer s, String line){
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
			res = "Current time is: "+ Facts.getSTime();
		}else{
			res = "Unknown Command";
		}
		return res;
	}
	
	private String buildString(String[] parts,int s){
		String res="";
		if(!isSum(parts)){
			boolean started = false;
			for(int i = s; i < parts.length; i++){
				if(parts[i].indexOf("\"") != -1){
					if(!started){
						started=true;
						boolean single = (i == parts.length-1);
						if (single){
							res += parts[i].substring(parts[i].indexOf("\"")+1,parts[i].length()-1) + " ";
							break;
						}else{
							res += parts[i].substring(parts[i].indexOf("\"")+1) + " ";
						}
					}else{
						res += parts[i].substring(0,parts[i].indexOf("\""));
						break;
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
			String base = regularToPolish(res.split(" "));
			return polishSumAlgo(base.split(" "));//""+polishSumAlgo(res.trim());
		}
	}
	
	private boolean isSum(String[] secs){
		String line = "";
		for(int i = 0; i < secs.length; i++){
			line += secs[i] + " ";
		}
		line = line.trim();
		
		if(line.indexOf("\"") == -1)return true;
		else return false;
	}
	
	/**
	 * Shunting yard algorithum
	 * @param math
	 * @return
	 */
	private String regularToPolish(String[] math){
		String res= "";
		Stack<String> out = new Stack<String>();
		Stack<String> ops = new Stack<String>();
		for(String j : math){
			String c = j;

			if(c.contains("+") || c.contains("-") || c.contains("/") || c.contains("*") || c.contains("^")){//Ops
				int prec = getPresedance(c);
				boolean los = isLeftOsc(c);
				
				if(!ops.empty()){
					String bot = ops.peek();
					boolean r1 = los && getPresedance(bot) <= prec;
					boolean r2 = !los && getPresedance(bot) < prec;
					if(r1 || r2){
						out.push(ops.pop());
					}
				}
				
				ops.push(c);

				System.out.println("JIO");
			}else{//Nums
				out.push(c);
			}
		}

		while(!ops.empty()){
			out.push(ops.pop());
		}
		
		while(!out.empty()){
			res = out.pop() + " " + res;
		}
		System.out.println(res);
		return res;
	}
	
	private int getPresedance(String oper){
		int prec = 0;
		if(oper == "+" || oper == "-")prec = 2;
		else if(oper == "*" || oper == "/")prec = 3;
		else if(oper == "^")prec = 4;
		return prec;
	}
	
	private boolean isLeftOsc(String oper){
		if(oper == "+" || oper == "-" || oper == "*" || oper == "/")return true;
		else return false;
	}
	
	
	
	private String polishSumAlgo(String[] math){
		Stack<String> mathStack = new Stack<String>();
		try{
			for(String j : math){
				String c = j;
				if(c.indexOf("+") == -1 && c.indexOf("-") == -1 && c.indexOf("*") == -1 && c.indexOf("/") == -1 && c.indexOf("^") == -1){// Not an operator
					mathStack.push(c);
				}else{

					int op2 = Integer.valueOf(mathStack.pop());
					int op1 = Integer.valueOf(mathStack.pop());
					int op3 = 0;
					if(c.indexOf("+") != -1){
						op3 = op1 + op2;
					}else if(c.indexOf("-") != -1){
						op3 = op1 - op2;
					}else if(c.indexOf("*") != -1){
						op3 = op1 * op2;
					}else if(c.indexOf("/") != -1){
						op3 = op1 / op2;
					}else if(c.indexOf("^") != -1){
						op3  = (int)Math.pow(op1, op2);
					}
					mathStack.push(""+op3);
				}
			}
			
			String res = mathStack.pop();
			return res;
		}catch(Exception e){
			e.printStackTrace();
			return "incorrect use for math, compilers fault not the users".toUpperCase();
		}
	}
}
