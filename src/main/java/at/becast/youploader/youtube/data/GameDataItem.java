package at.becast.youploader.youtube.data;

import org.apache.commons.lang3.StringEscapeUtils;

public class GameDataItem {
	public String id;
	public String name;
	
	public GameDataItem(String name, String id){
		this.name = name;
		this.id = id;
	}
	
	public GameDataItem(String[] d) {
		this.name = StringEscapeUtils.unescapeJava(d[1].replace("[", "").replace("]", "").replace("\"", ""));
		this.id = d[0].replace("[", "").replace("]", "").replace("\\", "").replace("\"", "");
	}

	public String getId(){
		return this.id;
	}
	
	public String toString(){
		return this.name;
	}
}
