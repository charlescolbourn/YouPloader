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
package at.becast.youploader.youtube.io;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.youtube.data.Video;

public class UploadManager {
	private int upload_limit = 1;
	private enum Status{STOPPED, RUNNING,FINISHED};
	private Status status;
	private LinkedList<UploadWorker> _ToUpload = new LinkedList<UploadWorker>();
	private LinkedList<UploadWorker> _Uploading = new LinkedList<UploadWorker>();
	private int speed_limit = 0;
	static Connection c = SQLite.getInstance();
	
	public UploadManager(){
		this.status=Status.STOPPED;
	}
	
	public void add_upload(UploadItem frame, File data, Video videodata, String acc){
		try {
			SQLite.addUpload(acc, data, videodata);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UploadWorker worker = new UploadWorker(frame,acc, data, videodata);
		_ToUpload.addLast(worker);
	}
	
	public void start(){
		if(!_ToUpload.isEmpty()){
			this.status=Status.RUNNING;
			for(int i=0;i<=upload_limit-_Uploading.size();i++){
				if(!_ToUpload.isEmpty()){
					UploadWorker w = _ToUpload.removeFirst();
					w.start();
					_Uploading.add(w);
				}
			}
		}
	}
	
	public void stop(){
		if(!_Uploading.isEmpty()){
			this.status=Status.STOPPED;
			for(int i=0;i<_Uploading.size();i++){
				_Uploading.get(i).abort();
			}
		}
	}
	
	public void set_limit(int limit){
		for(int i=0;i<_Uploading.size();i++){
			_Uploading.get(i).setSpeed(limit);
		}
	}
	
	public void set_uploadlimit(int limit){
		this.upload_limit = limit;
	}
	
}
