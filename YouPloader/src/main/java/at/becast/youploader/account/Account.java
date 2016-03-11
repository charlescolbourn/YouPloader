package at.becast.youploader.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import at.becast.youploader.database.SQLite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Account {
  public final String refreshToken;
  public String name;
  static Connection c = SQLite.getInstance();


  public Account(String name, String refreshToken) {
	this.name = name;
    this.refreshToken = refreshToken;
  }

  public Account(String name) {
    this(name, null);
  }


  public static Account read(String name) throws IOException {
	Statement stmt;
	try {
		stmt = c.createStatement();
		String sql = "SELECT * FROM `accounts` WHERE `name`='"+name+"' LIMIT 1"; 
		ResultSet rs = stmt.executeQuery(sql);
		return new Account(name,rs.getString("refresh_token"));
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
  }

  public void save() throws IOException {
	  try {
		Statement stmt = c.createStatement();
		String sql = "INSERT INTO `accounts` (`name`,`refresh_token`) VALUES('"+this.name+"','"+this.refreshToken+"')"; 
		stmt.executeUpdate(sql);
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
