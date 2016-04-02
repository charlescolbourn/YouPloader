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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import at.becast.youploader.account.Account;
import at.becast.youploader.youtube.data.Cookie;
import at.becast.youploader.youtube.data.Upload;

public class MetadataUpdater {
	private Upload upload;
	private Account acc;
	private CookieStore cookieStore;
	private static final Logger LOG = LoggerFactory.getLogger(MetadataUpdater.class);
	
	public MetadataUpdater(int acc_id, Upload upload) {
		this.upload = upload;
		try {
			this.acc = Account.read(acc_id);
		} catch (IOException e) {
			LOG.error("Unable to read Account ", e);
		}
	    this.cookieStore = new BasicCookieStore();
		for (Cookie serializableCookie : this.acc.getCookie()) {
			BasicClientCookie cookie = new BasicClientCookie(serializableCookie.getCookie().getName(), serializableCookie.getCookie().getValue());
			cookie.setDomain(serializableCookie.getCookie().getDomain());
			cookieStore.addCookie(cookie);
		}
	}

	public void updateMetadata() throws UnirestException {
		HttpClient client = HttpClientBuilder.create().useSystemProperties().setDefaultCookieStore(cookieStore).build();
		Unirest.setHttpClient(client);
		HttpResponse<String> response = Unirest.get(String.format("http://www.youtube.com/edit?o=U&ns=1&video_id=%s", upload.id)).asString();
		
		updateAsBrowser(response.getBody(), upload);
		
	}

	private void updateAsBrowser(String body, Upload upload) {
		LOG.info("Updating Metadata for Video {}",upload.id);
		Pattern pattern = Pattern.compile("var session_token=\"(.*?)\"");
		LOG.debug(body);
		Matcher m = pattern.matcher(body);
		String token;
		if(m.find()){
			token = m.group(0);
		}else{
			LOG.error("Could not extract Session token!");
			return;
		}
		//String token = String.format("%s", body.substring(body.indexOf("var session_token = \"") + "var session_token = \"".length(), body.indexOf("\"", body.indexOf("var session_token = \"") + "var session_token = \"".length())));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("session_token", token);
		params.put("modified_fields", "creator_share_feeds,video_monetization_style,ad_formats,syndication");
		params.put("creator_share_feeds", "yes");
		params.put("video_id", upload.id);
		params.put("video_monetization_style", upload.metadata.getMonetization());
		params.put("ad_formats", upload.metadata.getAdFormats());
		params.put("syndication", upload.metadata.getSyndication());
		HttpClient client = HttpClientBuilder.create().useSystemProperties().setDefaultCookieStore(cookieStore).build();
		Unirest.setHttpClient(client);
		try {
			HttpResponse<String> response = Unirest.post("https://www.youtube.com/metadata_ajax?action_edit_video=1")
					 .fields(params)
					 .asString();
			LOG.info("Updated Metadata! {}", response);
		} catch (UnirestException e) {
			LOG.error("Could not Update Metadata! ", e);
		}
	}
		
}
