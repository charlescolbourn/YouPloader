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


	/**
	 * Sends this system to hibernation mode
	 */
	public static void hibernateComputer() {
		final String command;
		if (PlatformUtil.isWindows()) {
			command = "rundll32 powrprof.dll,SetSuspendState";
		} else if (PlatformUtil.isLinux()) {
			command = "pm-hibernate";
		} else if (PlatformUtil.isMac()) {
			command = "osascript -e 'tell application \"Finder\" to sleep'";
		} else {
			return;
		}

		execute(command);
	}

	private static void execute(final String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(30000);
				} catch (final InterruptedException ignored) {
				}
				System.exit(0);
			}
		}, "Exitmanager");
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Sends this system to shutdown mode
	 */
	public static void shutdownComputer() {
		final String command;
		if (PlatformUtil.isWindows()) {
			command = "shutdown -t 60 -s -f";
		} else if (PlatformUtil.isLinux()) {
			command = "shutdown -t 60 -h -f";
		} else if (PlatformUtil.isMac()) {
			command = "osascript -e 'tell application\"Finder\" to shut down'";
		} else {
			return;
		}

		execute(command);
	}

	public void customCommand(final String command) {
		execute(command);
	}

}
