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

import at.becast.youploader.database.SQLite;

public class Settings {
	public Map<String, String> setting = new HashMap<String, String>();
	static Settings instance = null;
	Connection c = SQLite.getInstance();

	private Settings() {
		try {
			load();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Unable to load settings!", "Uh oh...", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static Settings getInstance() {
		if (Settings.instance == null) {
			return new Settings();
		} else {
			return Settings.instance;
		}
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
}
