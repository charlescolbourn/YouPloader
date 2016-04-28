package at.becast.youploader.youtube.playlists;


public class PlaylistAdd {
	public Snippet snippet;
	public Status status;
	
	public PlaylistAdd() {
		
	}
	
	public PlaylistAdd(String name) {
		this.snippet = new Snippet();
		this.status = new Status();
		this.snippet.title = name;
		this.status.privacyStatus = "public";
	}
	
	public static class Snippet{
		public String title;
		public Snippet(){
			//Empty for Jackson
		}
	}
	
	public static class Status{
		public String privacyStatus;
		public Status(){
			//Empty for Jackson
		}
	}
}
