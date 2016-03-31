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

public class VideoMetadata {
	private String video_monetization_style;
	private String syndication;
	private boolean overlay;
	private boolean trueview;
	private boolean instream;
	private boolean product;
	
	public VideoMetadata(){
		
	}
	
	public VideoMetadata(String video_monetization_style, String syndication){
		this.video_monetization_style = video_monetization_style;
		this.syndication = syndication;
	}
	
	public String getVideo_monetization_style() {
		return video_monetization_style;
	}

	public void setVideo_monetization_style(String video_monetization_style) {
		this.video_monetization_style = video_monetization_style;
	}

	public String getSyndication() {
		return syndication;
	}

	public void setSyndication(String syndication) {
		this.syndication = syndication;
	}

	public boolean isOverlay() {
		return overlay;
	}

	public void setOverlay(boolean overlay) {
		this.overlay = overlay;
	}

	public boolean isTrueview() {
		return trueview;
	}

	public void setTrueview(boolean trueview) {
		this.trueview = trueview;
	}

	public boolean isInstream() {
		return instream;
	}

	public void setInstream(boolean instream) {
		this.instream = instream;
	}

	public boolean isProduct() {
		return product;
	}

	public void setProduct(boolean product) {
		this.product = product;
	}

}
