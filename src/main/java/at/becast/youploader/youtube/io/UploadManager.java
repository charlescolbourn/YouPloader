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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import at.becast.youploader.account.AccountManager;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.gui.frmMain;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.data.VideoUpdate;
import at.becast.youploader.youtube.data.VisibilityType;
import at.becast.youploader.youtube.exceptions.UploadException;

public class UploadManager {
	private int upload_limit = 1;
	public static enum Status{NOT_STARTED,PREPARED,STOPPED,UPLOADING,FINISHED,ABORTED};
	private Status status;
	private frmMain parent;
	private LinkedList<UploadWorker> _ToUpload = new LinkedList<UploadWorker>();
	private LinkedList<UploadWorker> _Uploading = new LinkedList<UploadWorker>();
	private int speed_limit = 0;
	static Connection c = SQLite.getInstance();
	
	public UploadManager(frmMain parent){
		this.parent = parent;
	}
	
	public void add_upload(UploadItem frame, File data, Video videodata, String acc){
		if(frame.upload_id == -1){
			int id = -1;
			try {
				id = SQLite.addUpload(acc, data, videodata);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(id != -1){
				frame.set_id(id);
				UploadWorker worker = new UploadWorker(id, frame,acc, data, videodata, speed_limit);
				_ToUpload.addLast(worker);
			}else{
				// TODO Error Handling
			}
		}else{
			UploadWorker worker = new UploadWorker(frame.upload_id, frame,acc, data, videodata, speed_limit);
			_ToUpload.addLast(worker);
		}
	}
	
	public void add_resumeable_upload(UploadItem frame, File data, Video videodata, String acc, String url, String yt_id){
		UploadWorker worker = new UploadWorker(frame.upload_id, frame,acc, data, videodata, speed_limit, url, yt_id);
		_ToUpload.addFirst(worker);
	}
	
	public void start(){
		if(!_ToUpload.isEmpty()){
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
			for(int i=0;i<_Uploading.size();i++){
				UploadWorker w = _Uploading.get(i);
				w.abort();
				_Uploading.remove(i);
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

	public void cancel(int upload_id) {
		if(!_Uploading.isEmpty()){
			for(int i=0;i<_Uploading.size();i++){
				if(_Uploading.get(i).id == upload_id){
					_Uploading.get(i).abort();
					_Uploading.remove(i);
				}
			}
		}
		
	}

	public void delete(int upload_id) {
		if(!_ToUpload.isEmpty()){
			for(int i=0;i<_ToUpload.size();i++){
				if(_ToUpload.get(i).id == upload_id){
					_ToUpload.remove(i);
				}
			}
		}
		this.parent.removeItem(upload_id);
		SQLite.deleteUpload(upload_id);
	}
	
	public void editUpload(int upload_id) {
		try {
			this.parent.editUpload(upload_id);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update_upload(int upload_id, File data, Video v, String account) {
		if(!_Uploading.isEmpty()){
			for(int i=0;i<_Uploading.size();i++){
				if(_Uploading.get(i).id == upload_id){
					UploadWorker w = _Uploading.get(i);
					w.videodata = v;
					w.frame.getlblName().setText(v.snippet.title);
					String release = "";
					if(v.status.privacyStatus == VisibilityType.SCHEDULED.getData()){
			        	if(!v.status.publishAt.equals("")){
			        		String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
			        		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			        		Date date;
							try {
								date = formatter.parse(v.status.publishAt);
								DateFormat formatters = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
								release = formatters.format(date);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	}else{
			        		release = VisibilityType.PRIVATE.toString();
			        	}
			        }else{
			        	release = VisibilityType.valueOf(v.status.privacyStatus).toString();
			        }
					w.frame.getlblRelease().setText(release);
					UploadUpdater updater = new UploadUpdater(AccountManager.accMng.getAuth(w.acc));
					VideoUpdate s = new VideoUpdate(w.upload.id);
					s.snippet = v.snippet;
					s.status = v.status;
					try {
						updater.updateUpload(s);
					} catch (IOException | UploadException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			}
			if(!_ToUpload.isEmpty()){
				for(int i=0;i<_ToUpload.size();i++){
					if(_ToUpload.get(i).id == upload_id){
						UploadWorker w = _Uploading.get(i);
						w.videodata = v;
						w.frame.getlblName().setText(v.snippet.title);
						String release = "";
						if(v.status.privacyStatus == VisibilityType.SCHEDULED.getData()){
				        	if(!v.status.publishAt.equals("")){
				        		String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
				        		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
				        		Date date;
								try {
									date = formatter.parse(v.status.publishAt);
									DateFormat formatters = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
									release = formatters.format(date);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				        	}else{
				        		release = VisibilityType.PRIVATE.toString();
				        	}
				        }else{
				        	release = VisibilityType.valueOf(v.status.privacyStatus).toString();
				        }
						w.frame.getlblRelease().setText(release);
						w.file = data;
						w.acc = account;
						w.reset_uploader();
						return;
					}
				}
			}
		}
		
	}
	
}
