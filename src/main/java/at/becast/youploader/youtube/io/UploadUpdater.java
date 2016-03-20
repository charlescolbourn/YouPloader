package at.becast.youploader.youtube.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.data.VideoUpdate;
import at.becast.youploader.youtube.exceptions.UploadException;

public class UploadUpdater {
	  private OAuth2 oAuth2;
	  SimpleHTTP http;
	  public UploadUpdater(OAuth2 oAuth2) {
	    this.oAuth2 = oAuth2;
	  }

	  public void updateUpload(VideoUpdate video) throws IOException, UploadException {
	    this.http = new SimpleHTTP();

	    Map<String, String> headers = new HashMap<>();
	    headers.put("Authorization", this.oAuth2.getHeader());
	    headers.put("Content-Type", "application/json; charset=UTF-8");
	    http.put("https://www.googleapis.com/youtube/v3/videos?part=id,snippet,status",headers,new ObjectMapper().writeValueAsString(video));
	    this.http.close();
	  }
}
