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

import java.io.IOException;

import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.youtube.GuiUploadEvent;
import at.becast.youploader.youtube.Uploader;
import at.becast.youploader.youtube.data.Upload;
import at.becast.youploader.youtube.data.Video;

public class UploadWorker extends Thread {
	
	private Video video;
	private UploadItem frame;
	private Upload upload;
	private UploadEvent event;
	private Uploader uploader;
	private String acc;
	private AccountManager AccMgr;
	
	public UploadWorker(Video video, UploadItem frame, Upload upload, String acc){
		this.video = video;
		this.frame = frame;
		this.upload = upload;
		this.acc = acc;
		this.AccMgr = AccountManager.getInstance();
		this.uploader = new Uploader(this.AccMgr.getAuth(acc));
		this.event = new GuiUploadEvent(frame);
	}
	
	@Override
	public void run(){
		setName( "Uploader-" + getId() );
		try {
			this.uploader.upload(this.upload, this.event, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSpeed(int Speed){
		this.uploader.set_speedlimit(Speed);
	}
	
}
