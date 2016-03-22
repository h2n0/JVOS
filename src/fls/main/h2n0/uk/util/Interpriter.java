package fls.main.h2n0.uk.util;

import java.util.Stack;

public class Interpriter {

	
	public static Interpriter instance = new Interpriter();
	
	public boolean isSum(String[] secs){
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
	public String regularToPolish(String[] math){
		String res= "";
		Stack<String> out = new Stack<String>();
		Stack<String> ops = new Stack<String>();
		for(int i = 0; i < math.length; i++){
			String c = math[i];

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
			}else if(c.contains("(")){
				ops.push(c);
			}else if(c.contains(")")){
				String part = "";
				while(!(part = ops.pop()).contains("(")){
					out.push(part);
				}
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
		boolean res = false;
		if(oper == "+" || oper == "-" || oper == "*" || oper == "/")return !res;
		else return res;
	}
	
	public int compute(String math){
		String[] smath = math.split(" ");
		String pol = this.regularToPolish(smath);
		String out = this.polishSumAlgo(pol.split(" "));
		return Integer.parseInt(out);
	}
	
	
	
	public String polishSumAlgo(String[] math){
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
