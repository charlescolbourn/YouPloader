package at.becast.youploader.account;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.IMainMenu;
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
			return new OAuth2(s.setting.get("client_id"),s.setting.get("client_secret"), Account.read(rs.getString("name")).refreshToken);
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
	
	public HashMap<String,Integer> load(){
		Connection c = SQLite.getInstance();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM `accounts`"; 
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.isBeforeFirst()){
				HashMap<String, Integer> data = new HashMap<String, Integer>();;
				while(rs.next()){
					data.put(rs.getString("name"),rs.getInt("active"));
				}
				rs.close();
				return data;
			}else{
				stmt.close();
				return new HashMap<String, Integer>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new HashMap<String, Integer>();
		}
	}

	public void change_user(String actionCommand) {
		try {
			currentaccount = new OAuth2(s.setting.get("client_id"),s.setting.get("client_secret"), Account.read(actionCommand).refreshToken);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
