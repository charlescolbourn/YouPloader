package at.becast.youploader.settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import at.becast.youploader.database.SQLite;

public class Settings {
	public Map<String, String> setting = new HashMap<String, String>();
	static Settings instance = null;
	Connection c = SQLite.getInstance();
	private Settings(){
		try {
			load();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Unable to load settings!","Uh oh...", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static Settings getInstance(){
		if(Settings.instance==null){
			return new Settings();
		}else{
			return Settings.instance;
		}
	}
	
	public boolean load() throws SQLException{
		Statement stmt = c.createStatement();
		  String sql = "SELECT * FROM `settings`"; 
		  ResultSet rs = stmt.executeQuery(sql);
	      while ( rs.next() ) {
	    	  setting.put(rs.getString("name"), rs.getString("value"));
	      }
		  stmt.close();
		  return true;
	}
}
