package at.becast.youploader.templates;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import at.becast.youploader.account.AccountType;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.youtube.data.Video;

public class TemplateManager {
	private static TemplateManager manager = null;
	private HashMap<String, Template> templates = new HashMap<String, Template>();
	
	private TemplateManager(){
		this.load_templates();
	}
	
	public void load_templates() {
		Connection c = SQLite.getInstance();
		ObjectMapper mapper = new ObjectMapper();
		Statement stmt;
		try {
			stmt = c.createStatement();
			String sql = "SELECT * FROM `templates`"; 
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.isBeforeFirst()){
				while(rs.next()){
					try {
						templates.put(rs.getString("name"), mapper.readValue(rs.getString("data"), new TypeReference<Video>() {}));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				rs.close();
				stmt.close();
			}else{
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static TemplateManager getInstance(){
		if(manager==null){
			manager = new TemplateManager();
		}
		return manager;
	}
}
