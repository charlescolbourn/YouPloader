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

public class Video {
  public final Snippet snippet;
  public final Status status;

  public static class Snippet {
    public String title;
    public String description;
    public String[] tags;
    public int categoryId;

    public Snippet(String title, String description, String[] tags, int categoryId) {
      this.title = title;
      this.description = description;
      this.tags = tags;
      this.categoryId = categoryId;
    }
    
    public Snippet(){
    	
    }
  }

  public static class Status {
    public String privacyStatus;
    public boolean embeddable;
    public boolean publicStatsViewable;
    public String license;
    public String publishAt;

    public Status(String privacyStatus, boolean embeddable, String license, boolean publicStatsViewable) {
      this.privacyStatus = privacyStatus;
      this.embeddable = embeddable;
      this.license = license;
      this.publicStatsViewable = publicStatsViewable;
    }
    
    public Status(){
    	
    }
    
    public void setPublishDate(String date){
    	this.privacyStatus = "private";
    			//String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
    	this.publishAt = date;
    }

  }
  

  public Video() {
    this.snippet = new Snippet(
      null,
      "",
      new String[0],
      20
    );
    this.status = new Status(
      "private",
      true,
      "youtube",
      true
    );
  }
}
