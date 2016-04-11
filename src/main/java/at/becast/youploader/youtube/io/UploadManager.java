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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.account.AccountManager;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.FrmMain;
import at.becast.youploader.youtube.VisibilityType;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.data.VideoMetadata;
import at.becast.youploader.youtube.data.VideoUpdate;
import at.becast.youploader.youtube.exceptions.UploadException;

public class UploadManager {
	private static UploadManager self;
	private int upload_limit = 1;
	private static final Logger LOG = LoggerFactory.getLogger(UploadManager.class);
	public static enum Status{NOT_STARTED,PREPARED,STOPPED,UPLOADING,FINISHED,ABORTED,FAILED};
	private FrmMain parent;
	private boolean uploading = false;
	private LinkedList<UploadWorker> _ToUpload = new LinkedList<UploadWorker>();
	private LinkedList<UploadWorker> _Uploading = new LinkedList<UploadWorker>();
	private int speed_limit = 0;
	
	private UploadManager(){
		
	}
	
	public static UploadManager getInstance(){
		if(self==null){
			self = new UploadManager();
		}
		return self;
	}
	
	public void setParent(FrmMain parent){
		this.parent = parent;
	}
	
	public void addUpload(File data, Video videodata, VideoMetadata metadata){
		LOG.info("Adding upload");
		this.speed_limit = Integer.parseInt(parent.getSpinner().getValue().toString());
		if(metadata.getFrame().upload_id == -1){
			int id = -1;
			LOG.info("Upload is not a preexisting upload: Inserting to Database");
			try {
				id = SQLite.addUpload(data, videodata, metadata);
			} catch (SQLException | IOException e) {
				LOG.error("Upload cound not be added", e);
			}
			if(id != -1){
				LOG.info("Upload is not a preexisting upload: Adding Upload");
				metadata.getFrame().setId(id);
				UploadWorker worker = new UploadWorker(id, data, videodata, speed_limit, metadata);
				_ToUpload.addLast(worker);
			}else{
				LOG.info("Upload could not be added to database.");
			}
		}else{
			LOG.info("Upload is a preexisting upload: Adding Upload");
			UploadWorker worker = new UploadWorker(metadata.getFrame().upload_id, data, videodata, speed_limit, metadata);
			_ToUpload.addLast(worker);
		}
	}
	
	public void addResumeableUpload(File data, Video videodata, VideoMetadata metadata, String url, String yt_id){
		this.speed_limit = Integer.parseInt(parent.getSpinner().getValue().toString());
		LOG.info("Adding resumed Upload");
		UploadWorker worker = new UploadWorker(metadata.getFrame().upload_id, data, videodata, speed_limit, metadata, url, yt_id);
		_ToUpload.addFirst(worker);
	}
	
	public void start(){
		LOG.info("Starting uploads");
		if(!_ToUpload.isEmpty() && !uploading){
			uploading = true;
			startNextUpload();
		}
	}
	
	public void stop(){
		LOG.info("Stopping uploads");
		if(!_Uploading.isEmpty() && uploading){
			uploading = false;
			for(int i=0;i<_Uploading.size();i++){
				UploadWorker w = _Uploading.get(i);
				w.abort();
				LOG.info("Upload {} stopped",w.videodata.snippet.title);
				_Uploading.remove(i);
			}
		}
	}
	
	public void setLimit(int limit){
		for(int i=0;i<_Uploading.size();i++){
			_Uploading.get(i).setSpeed(limit);
		}
	}
	
	public void setUploadlimit(int limit){
		if(this.upload_limit<limit){
			startNextUpload();
		}
		this.upload_limit = limit;
	}

	public void finished(int upload_id) {
		if(!_Uploading.isEmpty()){
			for(int i=0;i<_Uploading.size();i++){
				if(_Uploading.get(i).id == upload_id){
					UploadWorker w = _Uploading.get(i);
					LOG.info("Upload {} finished",w.videodata.snippet.title);
					if(w.metadata.getThumbnail()!=null && !w.metadata.getThumbnail().trim().equals("")){
						LOG.info("Uploading Thumbnail {}",w.metadata.getThumbnail());
						w.frame.getProgressBar().setString(String.format("Uploading Thumbnail"));
						w.uploadThumbnail();
					}
					LOG.info("Updating Metadata");
					w.frame.getProgressBar().setString(String.format("Updating Metadata"));
					MetadataUpdater u = new MetadataUpdater(w.acc_id,w.upload);
					try {
						u.updateMetadata();
					} catch (IOException e) {
						LOG.error("Error Updating Metadata ", e);
					}
					if(w.enddir !=null && !w.enddir.equals("")){
						LOG.info("Moving file {}",w.file.getName());
						w.frame.getProgressBar().setString(String.format("Moving File"));
						try {
							Files.move(w.file.toPath(), Paths.get(w.enddir.trim()).resolve(w.file.getName()));
						} catch (IOException e) {
							LOG.error("Could not move file {}",w.file.getName(), e);
						}
					}
					w.frame.getProgressBar().setString(String.format("Finished"));
					_Uploading.remove(i);
					startNextUpload();
				}
			}
		}
	}
	
	public void cancel(int upload_id) {
		if(!_Uploading.isEmpty()){
			for(int i=0;i<_Uploading.size();i++){
				if(_Uploading.get(i).id == upload_id){
					UploadWorker w = _Uploading.get(i);
					w.abort();
					_Uploading.remove(i);
					w.delete();
					startNextUpload();
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
			LOG.error("Could not edit upload", e);
		}
	}
	
	public void startNextUpload(){
		if(!_ToUpload.isEmpty()){
			for(int i=0;i<=upload_limit-_Uploading.size();i++){
				UploadWorker w = _ToUpload.removeFirst();
				w.start();
				LOG.info("Upload {} started",w.videodata.snippet.title);
				_Uploading.add(w);
			}
		}else{
			uploading = false;
		}
	}

	public void updateUpload(int upload_id, File data, Video v, VideoMetadata metadata, int acc_id) {
		if(!_Uploading.isEmpty()){
			for(int i=0;i<_Uploading.size();i++){
				if(_Uploading.get(i).id == upload_id){
					UploadWorker w = _Uploading.get(i);
					w.videodata = v;
					w.metadata = metadata;
					w.frame.getlblName().setText(v.snippet.title);
					String release = "";
					if(v.status.privacyStatus == VisibilityType.SCHEDULED.getData()){
			        	if(v.status.publishAt!= null && !v.status.publishAt.equals("")){
			        		String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
			        		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			        		Date date;
							try {
								date = formatter.parse(v.status.publishAt);
								DateFormat formatters = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
								release = formatters.format(date);
							} catch (ParseException e) {
								LOG.error("Date parse exception at editing Upload", e);
							}
			        	}else{
			        		release = VisibilityType.PRIVATE.toString();
			        	}
			        }else{
			        	release = VisibilityType.valueOf(v.status.privacyStatus).toString();
			        }
					w.frame.getlblRelease().setText(release);
					UploadUpdater updater = new UploadUpdater(AccountManager.accMng.getAuth(w.acc_id));
					VideoUpdate s = new VideoUpdate(w.upload.id);
					s.snippet = v.snippet;
					s.status = v.status;
					try {
						updater.updateUpload(s);
					} catch (IOException | UploadException e) {
						LOG.error("Error while sending update to YouTube", e);
					}
					return;
				}
			}
			if(!_ToUpload.isEmpty()){
				for(int i=0;i<_ToUpload.size();i++){
					if(_ToUpload.get(i).id == upload_id){
						UploadWorker w = _Uploading.get(i);
						w.videodata = v;
						w.metadata = metadata;
						w.frame.getlblName().setText(v.snippet.title);
						String release = "";
						if(v.status.privacyStatus == VisibilityType.SCHEDULED.getData()){
				        	if(!"".equals(v.status.publishAt)){
				        		String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
				        		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
				        		Date date;
								try {
									date = formatter.parse(v.status.publishAt);
									DateFormat formatters = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
									release = formatters.format(date);
								} catch (ParseException e) {
									LOG.error("Date parse exception at editing Upload", e);
								}
				        	}else{
				        		release = VisibilityType.PRIVATE.toString();
				        	}
				        }else{
				        	release = VisibilityType.valueOf(v.status.privacyStatus).toString();
				        }
						w.frame.getlblRelease().setText(release);
						w.file = data;
						w.acc_id = acc_id;
						w.resetUploader();
						return;
					}
				}
			}
		}
		
	}

	public void restart(int upload_id) {
		for(int i=0;i<_Uploading.size();i++){
			if(_Uploading.get(i).id == upload_id){
				UploadWorker o = _Uploading.get(i);
				if(o.retrys<5){
					try {
						for(int s = 10; s>0;s--){
							o.frame.getProgressBar().setString(String.format("Error - Retrying in %s seconds", s));
							Thread.sleep(1000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	
					LOG.info("Restarting Upload {}",o.videodata.snippet.title);
					UploadWorker worker = new UploadWorker(upload_id, o.file, o.videodata, speed_limit, o.metadata, o.upload.url, o.upload.id);
					worker.setRetrys(o.getRetrys());
					_Uploading.set(i, worker);
					worker.start();
				}else{
					o.frame.getProgressBar().setString("Failed - You can try restarting");
					o.frame.getProgressBar().setValue(0);
					LOG.info("Retried 5 times. Failing Upload {}",o.videodata.snippet.title);
					_Uploading.remove(i);
					UploadWorker worker = new UploadWorker(upload_id, o.file, o.videodata, speed_limit, o.metadata, o.upload.url, o.upload.id);
					_ToUpload.add(worker);
					SQLite.setUploadFinished(upload_id,Status.STOPPED);
				}
			}
		}
	}

	public void hardfail(int upload_id) {
		for(int i=0;i<_Uploading.size();i++){
			if(_Uploading.get(i).id == upload_id){
				UploadWorker o = _Uploading.get(i);
				_Uploading.remove(i);
				o.delete();
				startNextUpload();
			}
		}		
	}
	
}
