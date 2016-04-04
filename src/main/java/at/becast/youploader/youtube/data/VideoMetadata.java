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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import at.becast.youploader.gui.UploadItem;

@JsonIgnoreProperties({"frame"})
public class VideoMetadata {
	private String syndication;
	private String thumbnail;
	private String endDirectory;
	private int account;
	private boolean overlay;
	private boolean trueview;
	private boolean instream;
	private boolean product;
	private boolean productplacement;
	private boolean share_fb;
	private boolean share_twitter;
	private boolean share_gplus;
	private boolean commentsEnabled;
	private String message;
	private String gametitle;
	private boolean restricted;
	private boolean monetized = false;
	private UploadItem frame;
	
	public VideoMetadata(){
		//Necessary for Jackson serialization
	}
	
	public boolean isMonetized() {
		return monetized;
	}

	public void setMonetized(boolean monetized) {
		this.monetized = monetized;
	}
	
	public boolean getProductplacement() {
		return this.productplacement;
	}
	@JsonIgnore
	public String productplacement(){
		return "{\"has_paid_product_placement\":"+productplacement+"}";
	}

	public void setProductplacement(boolean productplacement) {
		this.productplacement = productplacement;
	}
	@JsonIgnore
	public String getMonetization() {
		if(monetized){
			return "ads";
		}else{
			return "";
		}
	}
	@JsonIgnore
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isShare_fb() {
		return share_fb;
	}

	public void setShare_fb(boolean share_fb) {
		this.share_fb = share_fb;
	}

	public boolean isShare_gplus() {
		return share_gplus;
	}

	public void setShare_gplus(boolean share_gplus) {
		this.share_gplus = share_gplus;
	}

	public boolean isShare_twitter() {
		return share_twitter;
	}

	public void setShare_twitter(boolean share_twitter) {
		this.share_twitter = share_twitter;
	}

	public boolean isCommentsEnabled() {
		return commentsEnabled;
	}

	public void setCommentsEnabled(boolean commentsEnabled) {
		this.commentsEnabled = commentsEnabled;
	}

	public String getGametitle() {
		return gametitle;
	}

	public void setGametitle(String gametitle) {
		this.gametitle = gametitle;
	}

	public boolean isRestricted() {
		return restricted;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	public String getEndDirectory() {
		return endDirectory;
	}

	public void setEndDirectory(String endDirectory) {
		this.endDirectory = endDirectory;
	}

	public UploadItem getFrame() {
		return frame;
	}

	public void setFrame(UploadItem frame) {
		this.frame = frame;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

}
