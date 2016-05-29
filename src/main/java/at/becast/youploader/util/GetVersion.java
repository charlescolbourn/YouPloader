package at.becast.youploader.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.youtube.upload.SimpleHTTP;


public class GetVersion {
	private static final Logger LOG = LoggerFactory.getLogger(GetVersion.class);
	
	public GetVersion(){
		
	}
	
	public static String[] get(){
		try {
			SimpleHTTP http = new SimpleHTTP();
			String data = http.get("http://version.youploader.com/latest_version?"+System.currentTimeMillis()); //Placeholder until Java Update/Beta 0.8
			return data.split(";");
		} catch (IOException e) {
			LOG.error("Could not get version! ",e);
			return null;
		}
	}
}
