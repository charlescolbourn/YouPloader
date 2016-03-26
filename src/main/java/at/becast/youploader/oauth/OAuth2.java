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
package at.becast.youploader.oauth;

import at.becast.youploader.oauth.io.SimpleHTTP;
import at.becast.youploader.oauth.json.Auth;
import at.becast.youploader.oauth.json.Code;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuth2 {
  private String clientId;
  private String clientSecret;

  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private long expires;
  private static final Logger LOG = LoggerFactory.getLogger(OAuth2.class);
  private String deviceCode;


  public OAuth2(String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public OAuth2(String clientId, String clientSecret, String refreshToken) throws IOException {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.refreshToken = refreshToken;
    this.expires = 0;

    this.refresh();
  }


  private OAuth2 refresh() throws IOException {
    if (this.expires < System.currentTimeMillis()) {
      SimpleHTTP http = new SimpleHTTP();
      LOG.info("Token expired. Refreshing.");
      Map<String, String> post = new HashMap<>();
      post.put("client_id", this.clientId);
      post.put("client_secret", this.clientSecret);
      post.put("refresh_token", this.refreshToken);
      post.put("grant_type", "refresh_token");

      Auth auth = new ObjectMapper().readValue(
        http.post(
          "https://www.googleapis.com/oauth2/v3/token",
          post
        ),
        Auth.class
      );
      http.close();
      
      LOG.info("Got Access Token: {}", auth.access_token);
      this.accessToken = auth.access_token;
      this.tokenType = auth.token_type;
      this.expires = System.currentTimeMillis() + (auth.expires_in - 60) * 1000;

      if (auth.refresh_token != null) {
        this.refreshToken = auth.refresh_token;
        LOG.info("Got Refresh Token: {}", auth.refresh_token);
      }
    }

    return this;
  }


  public String getAccessToken() throws IOException {
    return this.refresh().accessToken;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public String getTokenType() {
    return this.tokenType;
  }

  public String getHeader() throws IOException {
    return this.getTokenType() + " " + this.getAccessToken();
  }


  public String getCode() throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    Map<String, String> post = new HashMap<>();
    post.put("client_id", this.clientId);
    post.put("scope", "https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtube.upload");

    Code code = new ObjectMapper().readValue(
      http.post(
        "https://accounts.google.com/o/oauth2/device/code",
        post
      ),
      Code.class
    );
    http.close();

    this.deviceCode = code.device_code;
    return code.user_code;
  }
  
  public void revoke() throws IOException {
	  	SimpleHTTP http = new SimpleHTTP();
	  	Map<String, String> post = new HashMap<>();
	    post.put("token", this.getAccessToken());
	    http.post("https://accounts.google.com/o/oauth2/revoke",post);
	  }

  public boolean check() throws IOException {
    Map<String, String> post = new HashMap<>();
    post.put("client_id", this.clientId);
    post.put("client_secret", this.clientSecret);
    post.put("code", this.deviceCode);
    post.put("grant_type", "http://oauth.net/grant_type/device/1.0");

    Auth auth = new ObjectMapper().readValue(
      new SimpleHTTP().post(
        "https://www.googleapis.com/oauth2/v3/token",
        post
      ),
      Auth.class
    );

    if (auth.error == null) {
      this.accessToken = auth.access_token;
      this.tokenType = auth.token_type;
      this.refreshToken = auth.refresh_token;
      this.expires = System.currentTimeMillis() + (auth.expires_in - 60) * 1000;

      return true;
    }
    else {
      LOG.info("Auth access error: {}", auth.error);
      return false;
    }
  }
}
