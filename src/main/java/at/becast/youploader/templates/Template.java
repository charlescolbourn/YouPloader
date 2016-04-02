package at.becast.youploader.templates;

import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.data.VideoMetadata;

public class Template {
	
	public String name;
	public Video videodata;
	public VideoMetadata metadata;
	public String startdir;
	public String enddir;
	
	public Template(String name){
		this.name=name;
	}
	
	public Template(){

	}
	
	public Template(String name, String startdir, String enddir, Video videodata, VideoMetadata metadata){
		this.name=name;
		this.startdir=startdir;
		this.enddir=enddir;
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


	public String getEnddir() {
		return enddir;
	}


	public void setEnddir(String enddir) {
		this.enddir = enddir;
	}

	
	@Override
	public String toString(){
		return name;
	}
}
