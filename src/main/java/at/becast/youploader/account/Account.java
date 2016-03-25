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
package at.becast.youploader.account;

import at.becast.youploader.database.SQLite;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.youtube.data.Cookie;

public class Account {
	public int id;
	public String refreshToken;
	public String name;
	public List<Cookie> cdata;
	static Connection c = SQLite.getInstance();
	private static final Logger LOG = LoggerFactory.getLogger(Account.class);

	public Account(int id, String name, String refreshToken, List<Cookie> cdata) {
		this.id = id;
		this.name = name;
		this.refreshToken = refreshToken;
		this.cdata = cdata;
	}

	public Account(String name) {
		this(0,name, null, null);
	}
  
	public void setCookie(List<Cookie> cdata){
		this.cdata = cdata;
	}
  
	public List<Cookie> getCookie(){
		return this.cdata;
	}

	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}
  
	public static Account read(String name) throws IOException {
		PreparedStatement stmt;
		try {
			stmt = c.prepareStatement("SELECT * FROM `accounts` WHERE `name`=? LIMIT 1");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			ObjectMapper mapper = new ObjectMapper();
			List<Cookie> c = mapper.readValue(rs.getString("cookie"), new TypeReference<List<Cookie>>() {});
			int id = rs.getInt("id");
			String token = rs.getString("refresh_token");
			stmt.close();
			return new Account(id,name,token,c);
		} catch (SQLException e) {
			LOG.error("Account read error!",e);
			return null;
		}
	}

	public static Account read(int id) throws IOException {
		PreparedStatement stmt;
		try {
			stmt = c.prepareStatement("SELECT * FROM `accounts` WHERE `id`=? LIMIT 1");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			ObjectMapper mapper = new ObjectMapper();
			List<Cookie> c = mapper.readValue(rs.getString("cookie"), new TypeReference<List<Cookie>>() {});
			String name = rs.getString("name");
			String token = rs.getString("refresh_token");
			stmt.close();
			return new Account(id,name,token,c);
		} catch (SQLException e) {
			LOG.error("Account read error!",e);
			return null;
		}
	}
  
	public int save() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		LOG.info("Saving account");
	  	try {
			PreparedStatement stmt = c.prepareStatement("INSERT INTO `accounts` (`name`,`refresh_token`,`cookie`) VALUES(?,?,?)");
			stmt.setString(1, this.name);
			stmt.setString(2, this.refreshToken);
			stmt.setString(3, mapper.writeValueAsString(this.cdata));
			stmt.execute();
		    ResultSet rs = stmt.getGeneratedKeys();
		    stmt.close();
	        if (rs.next()){
	        	int id = rs.getInt(1);
	        	rs.close();
	        	LOG.info("Account saved");
	        	return id;
	        }else{
	        	LOG.error("Could not save account {}!",this.name);
	        	return -1;
	        }
		} catch (SQLException e) {
			LOG.error("Could not save account Ex:",e);
			return -1;
		}
	}
  
	public static boolean exists(String name) {
		Connection c = SQLite.getInstance();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM `accounts` WHERE `name`='"+name+"'"; 
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.isBeforeFirst()){
				stmt.close();
				return true;
			}else{
				stmt.close();
				return false;
			}
		} catch (SQLException e) {
			LOG.error("Account exists error!",e);
			return false;
		}
	}
}
