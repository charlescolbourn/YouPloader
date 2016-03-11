package at.becast.youploader.oauth.io;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleHTTP {
  private CloseableHttpClient chc;

  public SimpleHTTP() {
    this.chc = HttpClients.createDefault();
  }

  public String post(String url, Map<String, String> data) throws IOException {
    HttpPost post = new HttpPost(url);

    List<NameValuePair> pd = new ArrayList<>();
    for (String key : data.keySet()) {
      pd.add(new BasicNameValuePair(key, data.get(key)));
    }

    post.setEntity(new UrlEncodedFormEntity(pd));
    CloseableHttpResponse response = this.chc.execute(post);
    String text = EntityUtils.toString(response.getEntity(), "UTF-8");
    response.close();

    return text;
  }

  public void close() throws IOException {
    this.chc.close();
  }
}
