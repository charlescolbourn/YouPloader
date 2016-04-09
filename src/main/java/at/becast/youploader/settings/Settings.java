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
package at.becast.youploader.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.database.SQLite;

public class Settings {
	public Map<String, String> setting = new HashMap<String, String>();
	private static final Logger LOG = LoggerFactory.getLogger(SQLite.class);
	private static Settings instance = null;
	private Connection c = SQLite.getInstance();

	private Settings() {
		try {
			load();
		} catch (SQLException e) {
			LOG.error("Error loading settings!", e);
			JOptionPane.showMessageDialog(null, "Unable to load settings!", "Uh oh...", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public boolean load() throws SQLException {
		Statement stmt = c.createStatement();
		String sql = "SELECT * FROM `settings`";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			setting.put(rs.getString("name"), rs.getString("value"));
		}
		stmt.close();
		return true;
	}
	
	public String get(String value, String defaultValue){
		if(this.setting.containsKey(value) && this.setting.get(value)!=null){
			return this.setting.get(value);
		}else if(!this.setting.containsKey(value)){
			setting.put(value, defaultValue);
			insert(value,defaultValue);
			return defaultValue;
		}else{
			setting.put(value, defaultValue);
			save(value);
			return defaultValue;
		}
	}
	
	public String get(String value){
		return this.get(value,"0");
	}

	public boolean save(String name) {
		PreparedStatement prest = null;
		String sql = "UPDATE `settings` SET `value`=? WHERE `name`=?";
		try {
			prest = c.prepareStatement(sql);
			prest.setString(1, this.setting.get(name));
			prest.setString(2, name);
			boolean res = prest.execute();
			prest.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean insert(String name, String value) {
		PreparedStatement prest = null;
		String sql = "INSERT INTO `settings` VALUES(?,?)";
		try {
			prest = c.prepareStatement(sql);
			prest.setString(1, name);
			prest.setString(2, value);
			boolean res = prest.execute();
			prest.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void put(String string, String valueOf) {
		this.setting.put(string, valueOf);
		this.save(string);
	}
}
