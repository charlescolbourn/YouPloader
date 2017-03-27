package at.becast.youploader.youtube.upload;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.youtube.exceptions.UploadException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class SimpleHTTP {
  private CloseableHttpClient chc;
  private HttpPut put;
  private CloseableHttpResponse response;
  private boolean aborted = false;
  private static final Logger LOG = LoggerFactory.getLogger(SimpleHTTP.class);
  private RequestConfig config;

  public SimpleHTTP() {
    this.chc = HttpClients.createDefault();
    config = RequestConfig.custom()
            .setExpectContinueEnabled(true)
            .build();
  } 

  public String[] post(String url, Map<String, String> headers, String body) throws IOException, UploadException {
    HttpPost post = new HttpPost(url);
    post.setProtocolVersion(HttpVersion.HTTP_1_1);
    post.setConfig(config);
    for (String key : headers.keySet()) {
      post.setHeader(key, headers.get(key));
    }
    post.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
    response = this.chc.execute(post);

    String[] location = new String[2];
    for (Header h : response.getAllHeaders()) {
      if (h.getName().equals("Location")) {
        location[0] = h.getValue();
      }
      if (h.getName().equals("X-Goog-Correlation-Id")){
    	location[1] = h.getValue();
      }
    }

    if (location[0] == null) {
    	StringBuilder b = new StringBuilder();
    	for (Header h : response.getAllHeaders()) {
    		b.append(h.getName() + ": " + h.getValue() + " ");
    	}
    	LOG.error("Could not create upload! Return: {} , Headers: {}, Body: {}", response.getStatusLine(), b.toString(), EntityUtils.toString(response.getEntity(), "UTF-8"));
      throw UploadException.construct(response.getEntity().getContent());
    }


    response.close();
    return location;
  }
  
  public void postPL(String url, Map<String, String> headers, String body) throws IOException, UploadException {
	    HttpPost post = new HttpPost(url);
	    post.setConfig(config);
	    post.setProtocolVersion(HttpVersion.HTTP_1_1);
	    for (String key : headers.keySet()) {
	      post.setHeader(key, headers.get(key));
	    }

	    post.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
	    response = this.chc.execute(post);
	    response.close();
  }

  @SuppressWarnings("unchecked")
public String postLog(Map<String, String> headers) throws IOException, UploadException {
	File fl = new File(System.getProperty("user.home")+"/YouPloader");
	File[] files = fl.listFiles(new FileFilter() {          
	    public boolean accept(File file) {
	        return file.isFile();
	    }
	});
	long lastMod = Long.MIN_VALUE;
	File choice = null;
	for (File file : files) {
	    if (file.lastModified() > lastMod) {
	        choice = file;
	        lastMod = file.lastModified();
	    }
	}
	HttpPost post = new HttpPost("https://api.github.com/gists");
	JSONObject obj = new JSONObject();
	JSONObject file = new JSONObject();
	JSONObject content = new JSONObject();
	content.put("content", FileUtils.readFileToString(choice, "UTF-8"));
	file.put(choice.getName(), content);
	obj.put("description", choice.getName());
	obj.put("public", true);
	obj.put("files", file);
	for (String key : headers.keySet()) {
	  post.setHeader(key, headers.get(key));
	}
	post.setHeader("Content-Type", headers.get("application/json"));
	System.out.println(obj.toJSONString());
	post.setEntity(new ByteArrayEntity(obj.toJSONString().getBytes()));
	response = this.chc.execute(post);
	HttpEntity entity = response.getEntity();
	String resp = EntityUtils.toString(entity, "UTF-8");
	response.close();
	return resp;
	
  }

  public void put(String url, Map<String, String> headers, BufferedInputStream stream, UploadEvent callback) throws IOException {
    this.put = new HttpPut(url);
    this.put.setProtocolVersion(HttpVersion.HTTP_1_1);
    this.put.setConfig(config);
    for (String key : headers.keySet()) {
    	this.put.setHeader(key, headers.get(key));
    }
    this.put.setEntity(new InputStreamEntity(stream));
    try {
    	response = this.chc.execute(this.put);
    }catch(Exception e){
    	if(!aborted){
	    	LOG.error("Upload failed ", e);
	    	if(callback != null){
	    		callback.onError(false);
	    	}
    	}else{
    		LOG.info("Upload aborted");
    	}
    }
    if(response!=null){
    	StatusLine l = response.getStatusLine();
    	if(l.getStatusCode()>=200 && l.getStatusCode()<=299){
    		callback.onFinish();
    	}
    	response.close();
    }
  }
  
  public void put(String url, Map<String, String> headers, String body) throws IOException {
	this.put = new HttpPut(url);
	this.put.setProtocolVersion(HttpVersion.HTTP_1_1);
	this.put.setConfig(config);
	for (String key : headers.keySet()) {
		this.put.setHeader(key, headers.get(key));
	}
	this.put.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
	try {
		response = this.chc.execute(this.put);
	}catch(Exception e){
		
	}
	if(response!=null){
		response.close();
	}
  }
  
  public void post(String url, Map<String, String> headers, File body) throws IOException {
	HttpPost post =  new HttpPost(url);
	post.setProtocolVersion(HttpVersion.HTTP_1_1);
	post.setConfig(config);
	for (String key : headers.keySet()) {
		post.setHeader(key, headers.get(key));
	}
	post.setEntity(new InputStreamEntity(new FileInputStream(body)));
	try {
		response = this.chc.execute(post);
	}catch(Exception e){
		
	}
	if(response!=null){
		response.close();
	}
  }

  public long put(String url, Map<String, String> headers) throws IOException {
	  this.put = new HttpPut(url);
	  this.put.setProtocolVersion(HttpVersion.HTTP_1_1);
	  this.put.setConfig(config);
    for (String key : headers.keySet()) {
    	this.put.setHeader(key, headers.get(key));
    }

    response = this.chc.execute(put);
    Header[] responseHeaders = response.getAllHeaders();
    response.close();

    for (Header header : responseHeaders) {
      if (header.getName().equals("Range")) {
        return Long.valueOf(header.getValue().split("-")[1]) + 1;
      }
    }

    return 0;
  }
  
  public boolean delete(Map<String, String> headers, String id) throws IOException {
	  HttpDelete delete = new HttpDelete("https://www.googleapis.com/youtube/v3/videos?id="+id);
    for (String key : headers.keySet()) {
    	delete.setHeader(key, headers.get(key));
    }
    response = this.chc.execute(delete);
    if(response.getStatusLine().getStatusCode()==204){
    	response.close();
    	return true;
    }else{
    	return false;
    }

  }
  
  public String get(String url, Map<String, String> headers) throws IOException {
	    HttpGet get = new HttpGet(url);

	    for (String key : headers.keySet()) {
	    	get.setHeader(key, headers.get(key));
	    }
	    response = this.chc.execute(get);
	    String responseBody = EntityUtils.toString(response.getEntity());
	
	    response.close();
	    return responseBody;
}
  
  public String get(String url) throws IOException {
	HttpGet request = new HttpGet(url);
    response = this.chc.execute(request);

    String responseBody = EntityUtils.toString(response.getEntity());
    response.close();
    return responseBody;
  }
  
  public void setAborted(boolean aborted) {
	this.aborted = aborted;
  }
  
  public void abort() {
	  this.put.abort();
  }

  public void close() throws IOException {
    this.chc.close();
  }
}
