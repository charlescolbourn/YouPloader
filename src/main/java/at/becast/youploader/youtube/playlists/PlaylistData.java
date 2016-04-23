/* 
 * YouPloader Copyright (c) 2016 genuineparts (itsme@genuineparts.org)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package at.becast.youploader.youtube.playlists;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.becast.youploader.gui.EditPanel;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.io.SimpleHTTP;

public class PlaylistData {

	private SimpleHTTP http;
	private OAuth2 oAuth2;
	private ObjectMapper mapper = new ObjectMapper();
	private static final Logger LOG = LoggerFactory.getLogger(EditPanel.class);
	public PlaylistData(OAuth2 oAuth2){
		this.oAuth2 = oAuth2;
	}
	
	public Playlists get(){
		Playlists lists = null;
		try {
			lists = mapper.readValue(get(null),Playlists.class);
			if(lists.nextPageToken!=null){
				Playlists next;
				String token = lists.nextPageToken;
				do{
					next = mapper.readValue(get(token),Playlists.class);
					lists.items.addAll(next.items);
					token = next.nextPageToken;
				}while(next.nextPageToken!=null);
			}
		} catch (IOException e) {
			LOG.error("Exception while getting Playlists: ", e);
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
			this.http.close();
			return result;
		} catch (IOException e) {
			LOG.error("Exception while getting Playlists: ", e);
		}
		return null;
	}
}
