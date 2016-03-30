package at.becast.youploader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class GetVersion {
	private static final Logger LOG = LoggerFactory.getLogger(GetVersion.class);
	
	public GetVersion(){
		
	}
	
	public static String get(){
		try {
			HttpResponse<String> response = Unirest.get("https://raw.githubusercontent.com/becast/YouPloader/master/version").asString();
			return response.getBody();
		} catch (UnirestException e) {
			LOG.error("Could not get version! ",e);
			return "0.0";
		}
	}
}
