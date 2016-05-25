package at.becast.youploader.templates;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.becast.youploader.database.SQLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateManager {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateManager.class);

	private static TemplateManager manager = null;
	public HashMap<Integer, Template> templates = new HashMap<Integer, Template>();
	
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
						templates.put(rs.getInt("id"), mapper.readValue(rs.getString("data"), new TypeReference<Template>() {}));
					} catch (IOException e) {
						LOG.error("Error loading template data", e);
					}
				}
				rs.close();
				stmt.close();
			}else{
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			LOG.error("Error loading templates", e);
		}
	}

	public static TemplateManager getInstance(){
		if(manager==null){
			manager = new TemplateManager();
		}
		return manager;
	}
	
	public Template get(int id) {
		return templates.get(id);
	}
	
	public void update(int id, Template t) {
		try {
			SQLite.updateTemplate(id, t);
			templates.put(id, t);
		} catch (SQLException | IOException e) {
			LOG.error("Error updating template", e);
		}
	}
	
	public void delete(int id) {
		SQLite.deleteTemplate(id);
		templates.remove(id);
	}
	
	public void save(Template t) {
		try {
			int id = SQLite.saveTemplate(t);
			templates.put(id, t);
		} catch (SQLException | IOException e) {
			LOG.error("Error saving template", e);
		}
	}
}
