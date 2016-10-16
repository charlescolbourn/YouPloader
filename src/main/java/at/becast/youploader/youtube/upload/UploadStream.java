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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.common.util.concurrent.RateLimiter;

public class UploadStream extends BufferedInputStream {
	private UploadEvent event;
	private long limit;
	public long size;
	private long position;
	private Boolean finished = false;
	private RateLimiter rateLimiter;
	//Buffer of 10 MB to prevent speed being limited by the file read, might limit extreme fast connections still.
	private final static int BUFFER_SIZE = 10485760;
	private static final double ONE_KILOBYTE = 1024;

	public UploadStream(File file, UploadEvent event) throws FileNotFoundException {
		super(new FileInputStream(file),BUFFER_SIZE);
		this.rateLimiter = RateLimiter.create(Double.MAX_VALUE);
		this.event = event;
		this.limit = 0;
		this.size = file.length();
		this.position = 0;
		if (event != null) {
			event.onInit();
		}
	}

	public UploadStream(File file) throws FileNotFoundException {
		this(file, null);
	}

	public UploadStream(File file, UploadEvent event, long skip) throws IOException {
		this(file, event);
		this.skip(skip);
	}

	public UploadStream(File file, long skip) throws IOException {
		this(file, null, skip);
	}

	public void setSpeedLimit(long limit) {
		if (this.event != null) {
			this.event.onSpeedLimitSet(limit);
		}
		if(limit <= 0){
			this.rateLimiter.setRate(Double.MAX_VALUE);
		}else{
			this.rateLimiter.setRate(limit * ONE_KILOBYTE);
		}
	}
	
	public long getSpeedLimit() {
		return this.limit;
	}

	@Override
	public long skip(long skip) throws IOException {
		if (this.event != null) {
			this.event.onRead(skip, this.position, this.size);
		}

		this.position += skip;
		return super.skip(skip);
	}

	@Override
	public int read(byte[] bytes) throws IOException {
		this.position += bytes.length;
		if (this.event != null) {
			this.event.onRead(bytes.length, this.position, this.size);
		}
		
		if (0 < rateLimiter.getRate()) {
			rateLimiter.acquire(bytes.length);
		}
		
		return super.read(bytes);
		
		/*if (this.limit != 0) {
			long s = System.currentTimeMillis();
			int r = super.read(bytes);

			s = System.currentTimeMillis() - s;
			s = bytes.length / this.limit - s;

			if (s > 0) {
				try {
					Thread.sleep(s);
				} catch (InterruptedException e) {
				}
			}

			return r;
		} else {
			
		}*/
	}

	public void abort() throws IOException {
		if (this.event != null) {
			this.event.onAbort();
		}
		super.close();
	}

	@Override
	public void close() throws IOException {
		super.close();
		if (this.event != null && this.finished) {
			this.event.onClose();
		}
	}

	public void setFinished() {
		this.finished = true;
	}
}
