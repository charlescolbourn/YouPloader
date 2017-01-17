package at.becast.youploader.templates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.data.VideoMetadata;

@JsonIgnoreProperties({"enddir"})
public class Template {
	
	public String name;
	public Video videodata;
	public boolean titlefromfile = false;
	public VideoMetadata metadata;
	public String startdir;
	
	public Template(String name){
		this.name=name;
	}
	
	public Template(){

	}
	
	public Template(String name, String startdir, Video videodata, VideoMetadata metadata){
		this.name=name;
		this.startdir=startdir;
		this.videodata=videodata;
		this.metadata=metadata;
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Video getVideodata() {
		return videodata;
	}


	public void setVideodata(Video videodata) {
		this.videodata = videodata;
	}

	public VideoMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(VideoMetadata metadata) {
		this.metadata = metadata;
	}

	public String getStartdir() {
		return startdir;
	}


	public void setStartdir(String startdir) {
		this.startdir = startdir;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean isTitlefromfile() {
		return titlefromfile;
	}

	public void setTitlefromfile(boolean titlefromfile) {
		this.titlefromfile = titlefromfile;
	}
}
