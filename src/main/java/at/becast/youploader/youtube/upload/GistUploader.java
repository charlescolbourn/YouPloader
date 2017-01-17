package at.becast.youploader.youtube.upload;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.gui.FrmMain;
import at.becast.youploader.util.DesktopUtil;
import at.becast.youploader.util.UTF8ResourceBundle;
import at.becast.youploader.youtube.exceptions.UploadException;

public class GistUploader implements Runnable {

	private FrmMain parent;
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private static final Logger LOG = LoggerFactory.getLogger(GistUploader.class);
	public GistUploader(FrmMain parent){
		this.parent = parent;
	}
	@Override
	public void run() {
		SimpleHTTP http = new SimpleHTTP();
		parent.getStatusBar().getProgressBar().setIndeterminate(true);
		parent.getStatusBar().setMessage(LANG.getString("Status.UploadingLogfile"));
    	String data;
		try {
			data = http.postLog(new HashMap<String,String>());
			JSONParser jsonParser = new JSONParser();
			JSONObject result = (JSONObject) jsonParser.parse(data);
			DesktopUtil.openBrowser(result.get("html_url").toString());
		} catch (IOException | UploadException | org.json.simple.parser.ParseException e) {
			LOG.error("Upload exception ",e);
		}
		parent.getPlaylistPanel().clearPlaylists();
		parent.getPlaylistPanel().loadPlaylists();
		parent.getStatusBar().getProgressBar().setIndeterminate(false);
		parent.getStatusBar().setMessage(LANG.getString("Status.Ready"));
	}

}
