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
	private String syndication;
	private String thumbnail;
	private boolean overlay;
	private boolean trueview;
	private boolean instream;
	private boolean product;
	private boolean product_placement;
	private boolean monetized = false;
	
	public VideoMetadata(){
		
	}
	
	public boolean isMonetized() {
		return monetized;
	}

	public void setMonetized(boolean monetized) {
		this.monetized = monetized;
	}
	
	public String getProductplacement() {
		return "{\"has_paid_product_placement\":"+product_placement+"}";
	}

	public void setProduct_placement(boolean product_placement) {
		this.product_placement = product_placement;
	}
	
	public String getMonetization() {
		if(monetized){
			return "ads";
		}else{
			return "";
		}
	}
	public String getAdFormats(){
		return "{\"has_overlay_ads\":"+isOverlay()+",\"has_skippable_video_ads\":"+isInstream()+",\"has_non_skippable_video_ads\":false,\"has_long_non_skippable_video_ads\":false,\"has_product_listing_ads\":"+isProduct()+"}";
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
	
	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
