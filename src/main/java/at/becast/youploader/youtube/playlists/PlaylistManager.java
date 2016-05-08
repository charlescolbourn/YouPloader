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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.becast.youploader.account.AccountManager;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.EditPanel;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.exceptions.UploadException;
import at.becast.youploader.youtube.io.SimpleHTTP;
import at.becast.youploader.youtube.playlists.Playlists.Item;

public class PlaylistManager {

	private SimpleHTTP http;
	private OAuth2 oAuth2;
	private int account;
	private ObjectMapper mapper = new ObjectMapper();
	private AccountManager AccMgr = AccountManager.getInstance();
	private static final Logger LOG = LoggerFactory.getLogger(EditPanel.class);
	private HashMap<Integer,List<Playlist>> playlists = new HashMap<Integer,List<Playlist>>();

	public PlaylistManager(int account){
		this.account = account;
		this.oAuth2 = this.AccMgr.getAuth(account);
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
		for(int i=0;i<lists.items.size();i++){
			if(lists.items.get(i).id.startsWith("FL")){
				lists.items.remove(i);
			}
		}
		return lists;
	}
	
	public void save(){
		this.playlists.clear();
		this.load();
		Playlists lists = this.get();
		if(this.playlists.get(this.account)==null || this.playlists.get(this.account).isEmpty()){
			try {
				SQLite.savePlaylists(lists,this.account);
			} catch (SQLException | IOException e) {
				LOG.error("Error saving Playlists: ",e);
			}
		}else{
			for(Item s : lists.items){
				String id = s.id;
				boolean found = false;
				for(int i=0; i<this.playlists.get(this.account).size();i++){
					if(this.playlists.get(this.account).get(i).ytId.equals(id)){
						found = true;
						try {
							SQLite.updatePlaylist(s);
						} catch (SQLException | IOException e) {
							LOG.error("Error updating Playlists: ",e);
						}
					}
				}
				if(!found){
					try {
						SQLite.insertPlaylist(s,this.account);
					} catch (SQLException | IOException e) {
						LOG.error("Error adding Playlists: ",e);
					}
				}
			}
		}
		
	}

	public void add(String name){
		this.http = new SimpleHTTP();
		Map<String, String> headers = new HashMap<>();
		try {
			headers.put("Authorization", this.oAuth2.getHeader());
			headers.put("Content-Type", "application/json; charset=UTF-8");
			PlaylistAdd add = new PlaylistAdd(name);
			http.postPL(
						"https://www.googleapis.com/youtube/v3/playlists?part=snippet,status&fields=snippet%2Cstatus",
						headers, new ObjectMapper().writeValueAsString(add));
			this.http.close();
		} catch (UploadException | IOException e) {
			LOG.error("Exception while adding Playlists: ", e);
		}
	}

	
	public void load() {
		Connection c = SQLite.getInstance();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM `playlists`"; 
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.isBeforeFirst()){
				while(rs.next()){
					String shown;
					if(rs.getString("shown")==null){
						shown = "1";
						SQLite.setPlaylistHidden(rs.getInt("id"), shown);
					}else{
						shown = rs.getString("shown");
					}
					Playlist l = new Playlist(rs.getInt("id"), rs.getString("playlistid"), rs.getString("name"), rs.getBytes("image"), shown);
					if(playlists.get(rs.getInt("account"))==null){
						List<Playlist> list = new ArrayList<Playlist>();
						list.add(l);
						playlists.put(rs.getInt("account"),list);
					}else{
						playlists.get(rs.getInt("account")).add(l);
					}
				}
				rs.close();
				stmt.close();
			}else{
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
					"https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=50&fields=items(id%2Csnippet)&mine=true"+getpage,
					headers);
			this.http.close();
			return result;
		} catch (IOException e) {
			LOG.error("Exception while getting Playlists: ", e);
		}
		return null;
	}
	
	public HashMap<Integer, List<Playlist>> getPlaylists() {
		return playlists;
	}
}
