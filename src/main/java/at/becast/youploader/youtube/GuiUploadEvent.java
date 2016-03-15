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

import at.becast.youploader.gui.UploadItem;
import at.becast.youploader.youtube.io.UploadEvent;

public class GuiUploadEvent implements UploadEvent {
  private long step;
  public UploadItem frame;
  private long timeDelta;
  private long starttime;
  private long dataDelta;
  private long lasttime;
  private long lastdata;
  private boolean aborted = false;

  public GuiUploadEvent(UploadItem frame) {
	  this.frame=frame;
	  this.step = 0;
  }

  @Override
  public void onInit() {
	this.starttime = System.currentTimeMillis();
	this.lastdata = 0;
	this.lasttime = this.starttime;
	Date in = new Date(this.starttime);
	LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
	Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,Locale.getDefault());
	frame.getlblStart().setText(formatter.format(out));
    frame.getProgressBar().setString("0,00 %");
    frame.getProgressBar().setValue(0);
    frame.getProgressBar().revalidate();
    frame.revalidate();
    frame.repaint();
  }

  @Override
  public void onSpeedLimitSet(long limit) {
  }

  @Override
  public void onRead(long length, long position, long size) {
    if (this.step < System.currentTimeMillis() - 900) {
    	frame.getProgressBar().setString(String.format("%6.2f%%",(float) position / size * 100));
    	frame.getProgressBar().setValue((int)((float) position / size * 100));
    	frame.getProgressBar().revalidate();
        frame.revalidate();
        frame.repaint();
    	this.step = System.currentTimeMillis();
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
    }
  }

  @Override
  public void onClose() {
  	if(!this.aborted){
	  	frame.getProgressBar().setString("100,00%");
	  	frame.getProgressBar().setValue(100);
	  	frame.getProgressBar().revalidate();
	    	frame.revalidate();
  	}
  }

@Override
public void onAbort() {
	this.aborted = true;
	frame.getProgressBar().setString("Aborted");
  	frame.getProgressBar().setValue(0);
  	frame.getProgressBar().revalidate();
}
}
