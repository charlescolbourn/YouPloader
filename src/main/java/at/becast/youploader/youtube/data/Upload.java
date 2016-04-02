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
package at.becast.youploader.youtube.data;

import java.io.File;

public class Upload {
  public final String url;
  public final File file;
  public final String id;
  public final Video video;
  public VideoMetadata metadata;

  public Upload(String url, File file, String id, Video video, VideoMetadata metadata) {
    this.url = url;
    this.file = file;
    this.id = id;
    this.video = video;
    this.metadata = metadata;
  }

  public Upload() {
    this(null, null, null, null, null);
  }
  
  public Upload(String url, File file, String id, Video video) {
	  this(url, file, id, video, null);
  }
  
  public void setMetadata(VideoMetadata metadata){
	  this.metadata = metadata;
  }
  
}
