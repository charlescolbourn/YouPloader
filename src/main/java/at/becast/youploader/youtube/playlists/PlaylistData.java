package at.becast.youploader.youtube.playlists;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.io.SimpleHTTP;

public class PlaylistData {

	private SimpleHTTP http;
	private OAuth2 oAuth2;

	public PlaylistData(OAuth2 oAuth2){
		this.oAuth2 = oAuth2;
	}
	
	public Map<String,String> get(){
		this.http = new SimpleHTTP();


		Map<String, String> headers = new HashMap<>();
		try {
			headers.put("Authorization", this.oAuth2.getHeader());
			headers.put("Content-Type", "application/json; charset=UTF-8");
			String result = http.get(
					"https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=50&mine=true",
					headers);
			System.out.println(result);
			this.http.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Upload url = new Upload(result[0], file, result[1], video);

		return null;
	}
}
