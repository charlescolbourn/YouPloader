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

import at.becast.youploader.youtube.data.Cookie;

public class Account {
  public String refreshToken;
  public String name;
  public List<Cookie> cdata;
  static Connection c = SQLite.getInstance();


  public Account(String name, String refreshToken, List<Cookie> cdata) {
	this.name = name;
    this.refreshToken = refreshToken;
    this.cdata = cdata;
  }

  public Account(String name) {
    this(name, null, null);
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
		String token = rs.getString("refresh_token");
		stmt.close();
		return new Account(name,token,c);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
  }

  public void save() throws IOException {
	  ObjectMapper mapper = new ObjectMapper();
	  try {
		PreparedStatement stmt = c.prepareStatement("INSERT INTO `accounts` (`name`,`refresh_token`,`cookie`) VALUES(?,?,?)");
		stmt.setString(1, this.name);
		stmt.setString(2, this.refreshToken);
		stmt.setString(3, mapper.writeValueAsString(this.cdata));
		stmt.executeUpdate();
		stmt.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
