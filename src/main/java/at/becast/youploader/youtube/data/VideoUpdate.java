package at.becast.youploader.youtube.data;

public class VideoUpdate extends Video {
	public Snippet snippet;
	public Status status;
	public String id;
	
	public VideoUpdate(String id){
		super();
		this.id = id;
	}
}
