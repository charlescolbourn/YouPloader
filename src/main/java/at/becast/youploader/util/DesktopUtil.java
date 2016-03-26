package at.becast.youploader.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesktopUtil {
	private static final Logger LOG = LoggerFactory.getLogger(DesktopUtil.class);
	  
	public static void openBrowser(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e1) {
				LOG.error("Can't open browser");
			}
		} else {
			LOG.error("Desktop not supported.");
		}		
	}

	public static void openDir(File dir) {
		if (Desktop.isDesktopSupported() && dir.isDirectory()) {
			try {
				Desktop.getDesktop().open(dir);
			} catch (IOException e) {
				LOG.error("Can't open Directory", e);
			}
		} else {
			LOG.error("Desktop not supported.");
		}		
	}
}
