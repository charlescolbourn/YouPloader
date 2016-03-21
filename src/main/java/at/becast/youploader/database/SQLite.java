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

package at.becast.youploader.database;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import at.becast.youploader.gui.frmMain;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.io.UploadManager;
import at.becast.youploader.youtube.io.UploadManager.Status;

public class SQLite {
	
	public static Connection c;
	
	private SQLite( String database ){
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+database);
		} catch ( Exception e ) {
		  System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
    public static Connection getInstance(){
        if(c == null)
            new SQLite(frmMain.DB_FILE);
        return c;
    }
    
    public static int addUpload(int account, File file, Video data) throws SQLException, JsonGenerationException, JsonMappingException, IOException{
    	PreparedStatement prest = null;
    	ObjectMapper mapper = new ObjectMapper();
    	String sql	= "INSERT INTO `uploads` (`account`, `file`, `lenght`, `data`, `status`) " +
    			"VALUES (?,?,?,?,?)";
    	prest = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    	prest.setInt(1, account);
    	prest.setString(2, file.getAbsolutePath());
    	prest.setLong(3, file.length());
    	prest.setString(4, mapper.writeValueAsString(data));
    	prest.setString(5, UploadManager.Status.NOT_STARTED.toString());
    	prest.execute();
        ResultSet rs = prest.getGeneratedKeys();
        prest.close();
        if (rs.next()){
        	int id = rs.getInt(1);
        	rs.close();
        	return id;
        }else{
        	return -1;
        }
    }
    
    public static Boolean prepareUpload(int id, String Url, String yt_id){
    	PreparedStatement prest = null;
    	String sql	= "UPDATE `uploads` SET `status`=?,`url`=?,`yt_id`=? WHERE `id`=?";
    	try {
			prest = c.prepareStatement(sql);
	    	prest.setString(1, UploadManager.Status.PREPARED.toString());
	    	prest.setString(2, Url);
	    	prest.setString(3, yt_id);
	    	prest.setInt(4, id);
	    	boolean res = prest.execute();
	    	prest.close();
	    	return res;     
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public static Boolean startUpload(int id, long progress){
    	PreparedStatement prest = null;
    	String sql	= "UPDATE `uploads` SET `status`=?,`uploaded`=? WHERE `id`=?";
    	try {
			prest = c.prepareStatement(sql);
	    	prest.setString(1, UploadManager.Status.UPLOADING.toString());
	    	prest.setLong(2, progress);
	    	prest.setInt(3, id);
	    	boolean res = prest.execute();
	    	prest.close();
	    	return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public static Boolean updateUploadProgress(int id, long progress){
    	PreparedStatement prest = null;
    	String sql	= "UPDATE `uploads` SET `uploaded`=? WHERE `id`=?";
    	try {
			prest = c.prepareStatement(sql);
	    	prest.setLong(1, progress);
	    	prest.setInt(2, id);
	    	boolean res = prest.execute();
	    	prest.close();
	    	return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    }

	public static Boolean setUploadFinished(int upload_id, Status Status) {
		PreparedStatement prest = null;
    	String sql	= "UPDATE `uploads` SET `status`=?,`url`=?,`uploaded`=`lenght` WHERE `id`=?";
    	try {
			prest = c.prepareStatement(sql);
	    	prest.setString(1, Status.toString());
	    	prest.setString(2, "");
	    	prest.setInt(3, upload_id);
	    	boolean res = prest.execute();
	    	prest.close();
	    	return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}

	public static Boolean deleteUpload(int upload_id) {
		PreparedStatement prest = null;
    	String sql	= "DELETE FROM `uploads` WHERE `id`=?";
    	try {
			prest = c.prepareStatement(sql);
	    	prest.setInt(1, upload_id);
	    	boolean res = prest.execute();
	    	prest.close();
	    	return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}				
	}
	
	public static Boolean updateUpload(int account, File file, Video data, int id) throws SQLException, JsonGenerationException, JsonMappingException, IOException{
    	PreparedStatement prest = null;
    	String sql	= "UPDATE `uploads` SET `account`=?, `file`=?, `lenght`=? WHERE `id`=?";
    	prest = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    	prest.setInt(1, account);
    	prest.setString(2, file.getAbsolutePath());
    	prest.setLong(3, file.length());
    	prest.setInt(4, id);
    	prest.execute();
    	boolean res = prest.execute();
    	prest.close();
    	return res && updateUploadData(data, id);
    }
	
    public static Boolean updateUploadData(Video data, int id) throws SQLException, JsonGenerationException, JsonMappingException, IOException{
    	PreparedStatement prest = null;
    	ObjectMapper mapper = new ObjectMapper();
    	String sql	= "UPDATE `uploads` SET `data`=? WHERE `id`=?";
    	prest = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    	prest.setString(1, mapper.writeValueAsString(data));
    	prest.setInt(2, id);
    	prest.execute();
    	boolean res = prest.execute();
        prest.close();
        return res;
    }

    
}