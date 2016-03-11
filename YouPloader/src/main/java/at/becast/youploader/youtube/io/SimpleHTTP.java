package at.becast.youploader.youtube.io;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import at.becast.youploader.youtube.exceptions.UploadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SimpleHTTP {
  private CloseableHttpClient chc;

  public SimpleHTTP() {
    this.chc = HttpClients.createDefault();
  }

  public String[] post(String url, Map<String, String> headers, String body) throws IOException, UploadException {
    HttpPost post = new HttpPost(url);

    for (String key : headers.keySet()) {
      post.setHeader(key, headers.get(key));
    }

    post.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
    CloseableHttpResponse response = this.chc.execute(post);

    String[] location = new String[2];
    for (Header h : response.getAllHeaders()) {
      if (h.getName().equals("Location")) {
        location[0] = h.getValue();
      }
      if (h.getName().equals("X-Goog-Correlation-Id")){
    	location[1] = h.getValue();
      }
    }

    if (location == null) {
      throw UploadException.construct(response.getEntity().getContent());
    }


    response.close();
    return location;
  }

  public void put(String url, Map<String, String> headers, InputStream stream) throws IOException {
    HttpPut put = new HttpPut(url);

    for (String key : headers.keySet()) {
      put.setHeader(key, headers.get(key));
    }

    put.setEntity(new InputStreamEntity(stream));
    CloseableHttpResponse response = this.chc.execute(put);
    response.close();
  }

  public long put(String url, Map<String, String> headers) throws IOException {
    HttpPut put = new HttpPut(url);

    for (String key : headers.keySet()) {
      put.setHeader(key, headers.get(key));
    }

    CloseableHttpResponse response = this.chc.execute(put);
    Header[] responseHeaders = response.getAllHeaders();
    response.close();

    for (Header header : responseHeaders) {
      if (header.getName().equals("Range")) {
        return Long.valueOf(header.getValue().split("-")[1]) + 1;
      }
    }

    return 0;
  }

  public void close() throws IOException {
    this.chc.close();
  }
}
