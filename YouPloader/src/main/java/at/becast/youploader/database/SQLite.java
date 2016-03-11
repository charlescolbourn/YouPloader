package at.becast.youploader.database;

import java.sql.*;

import at.becast.youploader.gui.frmMain;

public class SQLite {
	
	public static Connection c;
	
	private SQLite( String database ){
	try {
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:"+database);
	} catch ( Exception e ) {
	  System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	}
	System.out.println("Opened database successfully");
	}
	
    public static Connection getInstance(){
        if(c == null)
            new SQLite(frmMain.DB_FILE);
        return c;
    }
}