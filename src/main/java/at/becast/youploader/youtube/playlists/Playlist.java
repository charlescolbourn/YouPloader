package at.becast.youploader.youtube.playlists;

import javax.swing.ImageIcon;

public class Playlist {
	public int id;
	public String ytId;
	public String name;
	public ImageIcon image;
	
	public Playlist(int id, String ytId, String name, byte[] image){
		this.id = id;
		this.ytId = ytId;
		this.name = name;
		this.image = new ImageIcon(image);
	}
}
