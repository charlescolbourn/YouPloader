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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.database.SQLite;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.settings.Settings;

public class AccountManager {
	
	public static AccountManager accMng;
	private Settings s = Settings.getInstance();
	public static OAuth2 currentaccount;
	private static final Logger LOG = LoggerFactory.getLogger(AccountManager.class);
	
	private AccountManager(){
		
	}
	
	public static AccountManager getInstance(){
        if(accMng == null)
        	accMng = new AccountManager();
 
        return accMng;
    }
	
	public void setActive(String Username){
		Connection c = SQLite.getInstance();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "UPDATE `accounts` SET `active`=0"; 
			stmt.executeUpdate(sql);
			sql = "UPDATE `accounts` SET `active`=1 WHERE `name`='"+Username+"'"; 
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			LOG.error("Error in set_active", e);
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
			LOG.error("Error in load", e);
			return new HashMap<AccountType, Integer>();
		}
	}

	public OAuth2 getAuth(String acc) {
		try {
			return new OAuth2(s.setting.get("client_id"),s.setting.get("clientSecret"), Account.read(acc).refreshToken);
		} catch (IOException e) {
			LOG.error("Error in getAuth", e);
			return null;
		}
	}
	
	public OAuth2 getAuth(int id) {
		try {
			return new OAuth2(s.setting.get("client_id"),s.setting.get("clientSecret"), Account.read(id).refreshToken);
		} catch (IOException e) {
			LOG.error("Error in getAuth", e);
			return null;
		}
	}

	public void rename(String name, int id) {
		Connection c = SQLite.getInstance();
		PreparedStatement stmt = null;
		String sql = "UPDATE `accounts` SET `name`=? WHERE `id`=?";
		try {
			stmt = c.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setInt(2, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			LOG.error("Error in rename", e);
		}		
	}

	public void delete(int id) {
		OAuth2 auth = this.getAuth(id);
		try {
			auth.revoke();
		} catch (IOException e1) {
			LOG.error("Error in delete while revoking", e1);
		}
		Connection c = SQLite.getInstance();
		PreparedStatement stmt = null;
		String sql = "DELETE FROM `accounts` WHERE `id`=?";
		try {
			stmt = c.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			LOG.error("Error in delete during DB Delete", e);
		}		
		
	}

	public int getId(String name) {
		Connection c = SQLite.getInstance();
		PreparedStatement stmt = null;
		int id = 0;
		String sql = "SELECT `id` FROM `accounts` WHERE `name`=? LIMIT 1";
		try {
			stmt = c.prepareStatement(sql);
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if(rs.isBeforeFirst()){
				rs.next();
				id = rs.getInt("id");
			}
			rs.close();
			stmt.close();
			return id;
		} catch (SQLException e) {
			LOG.error("Error in getId", e);
			return id;
		}	
	}
}
