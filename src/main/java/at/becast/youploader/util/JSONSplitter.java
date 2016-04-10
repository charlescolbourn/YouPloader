package at.becast.youploader.util;

import java.util.ArrayList;
import java.util.List;

public class JSONSplitter {
	
	public static String[] split(String data){
		List<String> tokens=new ArrayList<>();
		StringBuilder buffer=new StringBuilder();
	
		int parenthesesCounter=0;
	
		for (char c : data.toCharArray()){
		    if (c=='[') parenthesesCounter++;
		    if (c==']') parenthesesCounter--;
		    if (c==',' && parenthesesCounter==0){
		        //lets add token inside buffer to our tokens
		        tokens.add(buffer.toString());
		        //now we need to clear buffer  
		        buffer.delete(0, buffer.length());
		    }
		    else 
		        buffer.append(c);
		}
		//lets not forget about part after last comma
		tokens.add(buffer.toString());
	
		return tokens.toArray(new String[tokens.size()]);
	}
}
