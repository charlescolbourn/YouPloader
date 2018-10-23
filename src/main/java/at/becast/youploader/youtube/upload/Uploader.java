/* 
 * YouPloader Copyright (c) 2017 genuineparts (itsme@genuineparts.org)
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

import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.data.Upload;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.exceptions.UploadException;
import at.becast.youploader.youtube.playlists.PlaylistItem;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

public class Uploader {
	private static final Logger LOG = LoggerFactory.getLogger(Uploader.class);
	private OAuth2 oAuth2;
	private UploadStream stream;
	private SimpleHTTP http;

	public Uploader(OAuth2 oAuth2) {
		this.oAuth2 = oAuth2;
	}

	public Upload prepareUpload(File file, Video video) throws IOException, UploadException {
		this.http = new SimpleHTTP();

		if (video.snippet.title == null) {
			video.snippet.title = file.getName();
		}

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", this.oAuth2.getHeader());
		headers.put("Content-Type", "application/json; charset=UTF-8");
		headers.put("Slug", URLEncoder.encode(file.getName(),"UTF-8"));
		headers.put("X-Upload-Content-Length", String.valueOf(file.length()));
		headers.put("X-Upload-Content-Type", "video/*");

		String[] result = http.post(
				"https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status",
				headers, new ObjectMapper().writeValueAsString(video));
		LOG.debug("Upload prep result: {}",Joiner.on(" ").join(result));
		Upload url = new Upload(result[0], file, result[1], video);

		this.http.close();
		return url;
	}

	public void upload(Upload upload, UploadEvent event, long limit) throws IOException {
		this.http = new SimpleHTTP();

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", this.oAuth2.getHeader());
		headers.put("Content-Type", "video/*");

		stream = new UploadStream(upload.file, event);
		stream.setSpeedLimit(limit);
		int restarts = 0;
		try{
			this.http.put(upload.url, headers, stream, event);
		} catch(ClientProtocolException e){
			LOG.error("Client Protocol Exception restarted " + restarts + " times", e);
			if (restarts<5){
				restarts++;
				this.resumeUpload(upload, event, limit);
			}
		}
		stream.setFinished();
		stream.close();
		this.http.close();
	}

	public void setSpeedlimit(int limit) {
		this.stream.setSpeedLimit(limit);
	}
	
	public long getSpeedlimit() {
		return this.stream.getSpeedLimit();
	}
	
	public void uploadThumbnail(File thumbnail, Upload upload) throws IOException, UploadException {
		LOG.info("Uploading Thumbnail {}",upload.metadata.getThumbnail().replaceAll("%ep%", upload.metadata.getEp().trim()));
		this.http = new SimpleHTTP();
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", this.oAuth2.getHeader());
		headers.put("Content-Type", "application/octet-stream; charset=UTF-8");		this.http.post("https://www.googleapis.com/upload/youtube/v3/thumbnails/set?videoId="+upload.id, headers, thumbnail);
		this.http.close();
	}
	
	public void setPlaylists(String playlist, Upload upload) throws IOException, UploadException {
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", this.oAuth2.getHeader());
		headers.put("Content-Type", "application/json; charset=UTF-8");
		this.http = new SimpleHTTP();
		this.http.postPL("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&fields=snippet", headers, new ObjectMapper().writeValueAsString(new PlaylistItem(playlist,upload.id)));
		this.http.close();
	}
	
	public void abort() {
		try {
			this.stream.close();
			this.http.close();
		} catch (Exception e) {
			LOG.error("Abort ", e);
		}
	}

	public boolean delete(Upload upload) throws IOException {
		SimpleHTTP del = new SimpleHTTP();
		if(upload.id!=null){
			Map<String, String> headers = new HashMap<>();
			try {
				headers.put("Authorization", this.oAuth2.getHeader());
			} catch (NullPointerException e) {
				return false;
			}
			return del.delete(headers, upload.id);
		}
		return false;
	}

	public void resumeUpload(Upload upload, UploadEvent event, long limit) throws IOException {
		this.http = new SimpleHTTP();

		long uploaded;
		{
			Map<String, String> headers = new HashMap<>();
			try {
				headers.put("Authorization", this.oAuth2.getHeader());
			} catch (NullPointerException e) {
				event.onError(true);
			}
			headers.put("Content-Range", "bytes */" + String.valueOf(upload.file.length()));

			uploaded = http.put(upload.url, headers);
		}

		if (uploaded == 0) {
			this.upload(upload, event, limit);
		} else {
			long length = upload.file.length();

			Map<String, String> headers = new HashMap<>();
			headers.put("Authorization", this.oAuth2.getHeader());
			headers.put("Content-Range", String.format("bytes %d-%d/%d", uploaded, length - 1, length));

			stream = new UploadStream(upload.file, event, uploaded);
			stream.setSpeedLimit(limit);
			http.put(upload.url, headers, stream, event);
			stream.setFinished();
			stream.close();
		}

		http.close();
	}
}
