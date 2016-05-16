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
package at.becast.youploader.youtube;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.Main;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.youtube.upload.UploadEvent;
import at.becast.youploader.youtube.upload.UploadManager.Status;
import at.becast.youploader.gui.FrmMain;

public class GuiUploadEvent implements UploadEvent {
  private static final Logger LOG = LoggerFactory.getLogger(GuiUploadEvent.class);
  private long step;
  public UploadItem frame;
  private long timeDelta;
  private long starttime;
  private long dataDelta;
  private long lasttime;
  private long lastdata;
  private long lastdb;
  
  public GuiUploadEvent(UploadItem frame) {
	  this.frame=frame;
	  this.step = 0;
  }

  @Override
  public void onInit() {
	this.starttime = System.currentTimeMillis();
	this.lastdata = 0;
	this.lasttime = this.starttime;
	this.lastdb = this.starttime;
	Date in = new Date(this.starttime);
	LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
	Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,Locale.getDefault());
	frame.getlblStart().setText(formatter.format(out));
    frame.getProgressBar().setString("0,00 %");
    frame.getProgressBar().setValue(0);
    frame.getProgressBar().revalidate();
	frame.getBtnCancel().setEnabled(true);
	frame.getBtnEdit().setEnabled(true);
	frame.getBtnDelete().setEnabled(false);
    frame.revalidate();
    frame.repaint();
  }

  @Override
  public void onSpeedLimitSet(long limit) {
		if(Main.debug){
			LOG.debug("Speed Limit set {}", limit);
		}
  }

  @Override
  public void onRead(long length, long position, long size) {
	long now = System.currentTimeMillis();
    if (this.step < now - 2000) {
    	frame.getProgressBar().setString(String.format("%6.2f%%",(float) position / size * 100));
    	frame.getProgressBar().setValue((int)((float) position / size * 100));
    	frame.getProgressBar().revalidate();
        frame.revalidate();
        frame.repaint();
        if(lastdb < now - 10000){
        	SQLite.updateUploadProgress(frame.upload_id, position);
        	this.lastdb = now;
        }
    	this.step = now;
    	this.dataDelta = position - this.lastdata;
    	this.timeDelta = this.step - this.lasttime;
    	this.lasttime  = this.step;
    	this.lastdata = position;
    	long speed = this.dataDelta / (this.timeDelta + 1) * 1000 + 1;
		long duration = ((size-position)/speed)*1000;
		String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration), TimeUnit.MILLISECONDS
					.toMinutes(duration) - TimeUnit.HOURS
					.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)), TimeUnit.MILLISECONDS
					.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    	frame.getLblKbs().setText(FileUtils.byteCountToDisplaySize(speed)+"/s");
    	frame.getLblETA().setText(time);
    	if(Main.debug){
    		LOG.debug("Took {} ms to refresh, Uploaded {} bytes, Speed {} ",System.currentTimeMillis()-now,this.dataDelta,FileUtils.byteCountToDisplaySize(speed));
    	}
    }
  }

  @Override
  public void onClose() {
	frame.getBtnCancel().setEnabled(false);
	frame.getBtnEdit().setEnabled(false);
	frame.getBtnDelete().setEnabled(true);
	frame.revalidate();
  }

  @Override
	public void onAbort() {
		frame.getProgressBar().setString("Aborted");
	  	frame.getProgressBar().setValue(0);
	  	frame.getProgressBar().revalidate();
	  	frame.getBtnCancel().setEnabled(false);
		frame.getBtnEdit().setEnabled(true);
		frame.getBtnDelete().setEnabled(true);
	  	SQLite.setUploadFinished(frame.upload_id,Status.ABORTED);
  	}

	@Override
	public void onError(boolean hardfail) {
		if(hardfail){
			frame.getProgressBar().setString("Failed");
		  	frame.getProgressBar().setValue(0);
		  	frame.getProgressBar().revalidate();
			frame.getBtnCancel().setEnabled(false);
			frame.getBtnEdit().setEnabled(false);
			frame.getBtnDelete().setEnabled(true);
			FrmMain.UploadMgr.hardfail(frame.upload_id);
		  	SQLite.failUpload(frame.upload_id);
		}else{
			FrmMain.UploadMgr.restart(frame.upload_id);
		}
	}
	
	@Override
	public void onFinish() {
		frame.getProgressBar().setString("100,00%");
	  	frame.getProgressBar().setValue(100);
	  	frame.getProgressBar().revalidate();
    	frame.revalidate();
    	SQLite.setUploadFinished(frame.upload_id,Status.FINISHED);
    	FrmMain.UploadMgr.finished(frame.upload_id);
	}
	
}
