package fls.main.h2n0.uk.program;

public class Variable {

	
	private enum Type{
		num,
		bool;
	}
	
	private Type type;
	private int value;
	private String nm;
	
	public Variable(String name,String type, int value){
		if(type.equals("bool")){
			this.type = Type.bool;
		}else{
			this.type = Type.num;
		}
		
		this.value =  value;
		this.nm = name;
	}
	
	public String getSValue(){
		if(this.type == Type.bool){
			if(this.value == 1)return "1";
			else return "0";
		}else{
			return ""+this.value;
		}
	}
	
	public int getIValue(){
		if(this.type == Type.bool){
			if(this.value == 1)return 1;
			else return 0;
		}else{
			return this.value;
		}
	}
	
	public void setValue(int value){
		if(this.type == Type.bool){
			this.value = value;
		}else if(this.type == Type.num){
			this.value = value;
		}
	}
	
	public boolean isNum(){
		if(this.type == Type.num)return true;
		else return false;
	}
	
	public String getName(){
		return this.nm;
	}
}
