package fls.main.h2n0.uk.util;

import java.util.Calendar;

public class Facts {

	
	private static Calendar cal = Calendar.getInstance();
	
	public static int getDay(){
		updateTime();
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMonth(){
		updateTime();
		return cal.get(Calendar.MONTH);
	}
	
	public static int getYear(){
		updateTime();
		return cal.get(Calendar.YEAR);
	}
	
	public static int getHour(){
		updateTime();
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute(){
		updateTime();
		return cal.get(Calendar.MINUTE);
	}
	
	public static int getSecond(){
		updateTime();
		return cal.get(Calendar.SECOND);
	}
	
	public static String getSTime(){
		String h = ""+getHour();
		String m = ""+getMinute();
		String s = ""+getSecond();
		
		if(h.length() == 1)h = "0"+h;
		if(m.length() == 1)m = "0"+m;
		if(s.length() == 1)s = "0"+s;
		return h+":"+m+":"+s;
	}
	
	public static String getSDate(){
		String y = ""+getYear();
		String m = ""+getMonth();
		String d = ""+getDay();
		
		if(y.length() == 1)y = "0"+y;
		if(m.length() == 1)m = "0"+m;
		if(d.length() == 1)d = "0"+d;
		return d+"/"+m+"/"+y;
	}
	
	private static void updateTime(){
		cal = Calendar.getInstance();
	}
}
