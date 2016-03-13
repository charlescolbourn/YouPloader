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

import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.youtube.GuiUploadEvent;
import at.becast.youploader.youtube.Uploader;
import at.becast.youploader.youtube.data.Upload;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.exceptions.UploadException;

public class UploadWorker extends Thread {
	
	private Video videodata;
	private File file;
	private UploadItem frame;
	private Upload upload;
	private UploadEvent event;
	private Uploader uploader;
	private String acc;
	private Boolean threadSuspended;
	private AccountManager AccMgr;
	
	public UploadWorker(UploadItem frame, String acc, File file, Video videodata){
		this.frame = frame;
		this.acc = acc;
		this.file = file;
		this.videodata = videodata;
		this.threadSuspended = false;
		this.AccMgr = AccountManager.getInstance();
		this.uploader = new Uploader(this.AccMgr.getAuth(acc));
		this.event = new GuiUploadEvent(frame);
	}
	
	public void prepare(){
		try {
			this.upload = this.uploader.prepareUpload(this.file, this.videodata);
		} catch (IOException | UploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.frame.getlblUrl().setText("https://www.youtube.com/watch?v="+this.upload.id);
	}
	
	@Override
	public void run(){
		setName( "Uploader-" + getId() );
		try {
			this.prepare();
			this.uploader.upload(this.upload, this.event, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSpeed(int Speed){
		this.uploader.set_speedlimit(Speed);
	}
	
	public void abort(){
		this.uploader.abort();
	}
	
	public void suspend(Boolean suspend){
		synchronized (this.uploader) {
			if(!suspend){
				this.uploader.notify();
			}
		}
	}
}
