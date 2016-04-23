package at.becast.youploader.youtube.playlists;


public class PlaylistItem {
	public Snippet snippet;
	
	public PlaylistItem(){
		this.snippet = new Snippet();
	}
	
	public PlaylistItem(String playlist, String video){
		this.snippet = new Snippet(video);
		this.snippet.playlistId = playlist;
	}
	
	public static class Snippet{
		public ResourceId resourceId;
		public String playlistId;
		
		public Snippet(){
			this.resourceId = new ResourceId();
		}
		
		public Snippet(String video){
			this.resourceId = new ResourceId(video);
		}
		
		public static class ResourceId{
			public String kind = "youtube#video";
			public String videoId;
			
			public ResourceId(){
				
			}
			
			public ResourceId(String videoId){
				this.videoId = videoId;
			}
		}
	}


}
