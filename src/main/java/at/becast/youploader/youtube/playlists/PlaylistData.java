package at.becast.youploader.youtube.playlists;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.io.SimpleHTTP;

public class PlaylistData {

	private SimpleHTTP http;
	private OAuth2 oAuth2;
	private ObjectMapper mapper = new ObjectMapper();
	public PlaylistData(OAuth2 oAuth2){
		this.oAuth2 = oAuth2;
	}
	
	public Playlists get(){
		Playlists lists = null;
		try {
			lists = mapper.readValue(get(null),Playlists.class);
			System.out.println(lists);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}
	
	public String get(String page){
		this.http = new SimpleHTTP();
		String getpage="";
		if(page!=null && !page.equals("")){
			getpage ="&pageToken="+page;
		}
		Map<String, String> headers = new HashMap<>();
		try {
			headers.put("Authorization", this.oAuth2.getHeader());
			headers.put("Content-Type", "application/json; charset=UTF-8");
			String result = http.get(
					"https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=50&mine=true"+getpage,
					headers);
			System.out.println(result);
			this.http.close();
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Upload url = new Upload(result[0], file, result[1], video);

		return null;
	}
}
