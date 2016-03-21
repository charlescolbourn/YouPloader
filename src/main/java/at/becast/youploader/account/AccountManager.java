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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.settings.Settings;

public class AccountManager {
	
	public static AccountManager accMng;
	Settings s = Settings.getInstance();
	public static OAuth2 currentaccount;
	
	private AccountManager(){
		
	}
	
	public static AccountManager getInstance(){
        if(accMng == null)
        	accMng = new AccountManager();
        
        try {
			currentaccount=accMng.load_active();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return accMng;
    }
	
	private OAuth2 load_active() throws SQLException, IOException {
		Connection c = SQLite.getInstance();
		Statement stmt;
		stmt = c.createStatement();
		String sql = "SELECT * FROM `accounts` WHERE `active`"; 
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.isBeforeFirst()){
			return new OAuth2(s.setting.get("client_id"),s.setting.get("clientSecret"), Account.read(rs.getString("name")).refreshToken);
		}else{
			stmt.close();
			return null;
		}
	}

	public void set_active(String Username){
		Connection c = SQLite.getInstance();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "UPDATE `accounts` SET `active`=0"; 
			stmt.executeUpdate(sql);
			sql = "UPDATE `accounts` SET `active`=1 WHERE `name`='"+Username+"'"; 
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public HashMap<AccountType,Integer> load(){
		Connection c = SQLite.getInstance();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM `accounts`"; 
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.isBeforeFirst()){
				HashMap<AccountType, Integer> data = new HashMap<AccountType, Integer>();
				while(rs.next()){
					data.put(new AccountType(rs.getInt("id"), rs.getString("name")),rs.getInt("active"));
				}
				rs.close();
				stmt.close();
				return data;
			}else{
				rs.close();
				stmt.close();
				return new HashMap<AccountType, Integer>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new HashMap<AccountType, Integer>();
		}
	}

	public OAuth2 getAuth(String acc) {
		try {
			return new OAuth2(s.setting.get("client_id"),s.setting.get("clientSecret"), Account.read(acc).refreshToken);
		} catch (IOException e) {
			return null;
		}
	}
	
	public OAuth2 getAuth(int id) {
		try {
			return new OAuth2(s.setting.get("client_id"),s.setting.get("clientSecret"), Account.read(id).refreshToken);
		} catch (IOException e) {
			return null;
		}
	}
}
