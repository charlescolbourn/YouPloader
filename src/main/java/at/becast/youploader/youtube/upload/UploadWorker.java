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
package at.becast.youploader.youtube.upload;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.Main;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.youtube.GuiUploadEvent;
import at.becast.youploader.youtube.data.Upload;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.data.VideoMetadata;
import at.becast.youploader.youtube.exceptions.UploadException;

public class UploadWorker extends Thread {
	
	public Video videodata;
	public VideoMetadata metadata;
	public int id, acc_id;
	public int retrys = 0;
	private int speed_limit;
	public File file;
	public Date startAt;
	public UploadItem frame;
	public Upload upload;
	private UploadEvent event;
	private Uploader uploader;
	public String url, enddir;
	private AccountManager AccMgr;
	private static final Logger LOG = LoggerFactory.getLogger(UploadWorker.class);
	
	public UploadWorker(int id, File file, Video videodata, int speed_limit, VideoMetadata metadata, Date startAt){
		this.id = id;
		this.frame = metadata.getFrame();
		this.upload = null;
		this.acc_id = metadata.getAccount();
		this.file = file;
		this.videodata = videodata;
		this.metadata = metadata;
		this.startAt = startAt;
		this.speed_limit = speed_limit;
		this.enddir = metadata.getEndDirectory();
		this.AccMgr = AccountManager.getInstance();
		this.uploader = new Uploader(this.AccMgr.getAuth(this.acc_id));
		this.event = new GuiUploadEvent(this.frame);
	}
	
	public UploadWorker(int id, File file, Video videodata, int speed_limit, VideoMetadata metadata, String url, String yt_id, Date startAt){
		this.id = id;
		this.frame = metadata.getFrame();
		this.acc_id = metadata.getAccount();
		this.file = file;
		this.upload = new Upload(url,file,yt_id,videodata,metadata);
		this.videodata = videodata;
		this.metadata = metadata;
		this.startAt = startAt;
		this.speed_limit = speed_limit;
		this.enddir = metadata.getEndDirectory();
		this.AccMgr = AccountManager.getInstance();
		this.uploader = new Uploader(this.AccMgr.getAuth(this.acc_id));
		this.event = new GuiUploadEvent(this.frame);
	}
	
	public void prepare(){
		try {
			this.upload = this.uploader.prepareUpload(this.file, this.videodata);
		} catch (IOException | UploadException e) {
			LOG.error("Could not prepare upload ",e);
		}
		if(this.metadata==null){
			LOG.error("Metadata is NULL!");
		}
		this.upload.setMetadata(this.metadata);
		SQLite.prepareUpload(this.id,this.upload.url,this.upload.id);
		this.frame.getlblUrl().setText("https://www.youtube.com/watch?v="+this.upload.id);
	}
		
	public void setThumbnail(){
		try {
			this.uploader.uploadThumbnail(new File(this.metadata.getThumbnail()), upload);
		} catch (IOException | UploadException e) {
			LOG.error("Could not set Thumbnail ",e);
		}
	}
	
	public void setPlaylists(String playlist){
		try {
			this.uploader.setPlaylists(playlist, upload);
		} catch (UploadException | IOException e) {
			LOG.error("Could not set Playlists ",e);
		}
	}
	
	@Override
	public void run(){
		setName( "Uploader-" + getId() );
		try {
			if(this.upload == null){
				this.prepare();
				SQLite.startUpload(this.id,0);
				this.uploader.upload(this.upload, this.event, this.speed_limit);
				if(Main.debug){
					LOG.debug("Upload started. Limit {}", this.speed_limit);
				}
			}else{
				try{
					this.uploader.resumeUpload(this.upload, this.event, this.speed_limit);
					if(Main.debug){
						LOG.debug("Upload resumed. Limit {}", this.speed_limit);
					}
				}catch( IOException e){
					this.event.onError(true);
				}
			}
			
		} catch (IOException e) {
			LOG.error("Could not run upload",e);
		}
	}
	
	public void setSpeed(int Speed){
		this.uploader.setSpeedlimit(Speed);
	}
	
	public long getSpeed(){
		return this.uploader.getSpeedlimit();
	}
	
	public void resetUploader(){
		this.uploader = new Uploader(this.AccMgr.getAuth(acc_id));
	}
	
	
	public void abort(){
		this.uploader.abort();
	}
	public void uploadThumbnail(){
		this.setThumbnail();
	}
	
	public int getRetrys() {
		return retrys;
	}

	public boolean delete() {
		try {
			return this.uploader.delete(this.upload);
		} catch (IOException e) {
			return false;
		}
		
	}
	
	public void setRetrys(int retrys) {
		this.retrys = retrys+1;
	}
	
	public void suspend(Boolean suspend){
		synchronized (this.uploader) {
			if(!suspend){
				this.uploader.notify();
			}
		}
	}
}
