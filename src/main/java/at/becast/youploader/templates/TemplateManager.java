/* 
 * YouPloader Copyright (c) 2017 genuineparts (itsme@genuineparts.org)
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
