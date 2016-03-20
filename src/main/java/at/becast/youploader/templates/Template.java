package at.becast.youploader.templates;

import at.becast.youploader.youtube.data.Video;

public class Template {
	
	public String name;
	public Video videodata;
	public String startdir;
	public String enddir;
	
	
	public Template(String name){
		this.name=name;
	}
}
