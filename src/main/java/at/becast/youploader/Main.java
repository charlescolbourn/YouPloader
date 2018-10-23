/* 
 * YouPloader Copyright (c) 2017 genuineparts (itsme@genuineparts.org)
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

package at.becast.youploader;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.FrmMain;
import at.becast.youploader.gui.UpdateNotice;
import at.becast.youploader.settings.Settings;
import at.becast.youploader.util.GetVersion;
import at.becast.youploader.util.PlatformUtil;
import at.becast.youploader.util.UTF8ResourceBundle;
import at.becast.youploader.util.VersionComparator;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;

/**
 * The YouPloader main class.
 * 
 *  All the Magic and Bugs start here.
 * 
 * @author genuineparts
 * @version 0.7
 *
 */
public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	public static boolean debug = false;
	public static final String DB_FILE = System.getProperty("user.home") + "/YouPloader/data/data.db";
	public static final String APP_NAME = "YouPloader";
	public static final String VERSION = "0.9.6";
	public static final int DB_VERSION = 11;
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	public static Settings s;
	public static Boolean firstlaunch = false;
	
	/**
	 * The YouPloader main Method
	 * 
	 * @param args  the command line arguments
	 * 
	 */
	public static void main(String[] args) {
		LOG.info(APP_NAME + " " + VERSION + " starting.", Main.class);
		if (PlatformUtil.isWindows())
		{
			setCurrentProcessExplicitAppUserModelID("BeCast.YouPloader");
		}
		
		//We want IPv4
		System.setProperty("java.net.preferIPv4Stack" , "true");
		//Debug Switch
		if (args.length > 0 && args[0].equalsIgnoreCase("-debug")) {
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			// print logback's internal status
			StatusPrinter.print(lc);
			debug = true;
			LOG.debug("Debug MODE");
		}
		Runtime runtime = Runtime.getRuntime();
		//Info about System, useful for Issue resolving
		LOG.info("############################# VM INFO #############################");
		LOG.info("# OS Name:      " + System.getProperty("os.name"));
		LOG.info("# OS Arch:      " + System.getProperty("os.arch"));
		LOG.info("# OS Vers:      " + System.getProperty("os.version"));
		LOG.info("# Java Vers:    " + System.getProperty("java.version"));
		LOG.info("# Java Home:    " + System.getProperty("java.home"));
		LOG.info("# User Name:    " + System.getProperty("user.name"));
		LOG.info("# User Home:    " + System.getProperty("user.home"));
		LOG.info("# Cur dir:      " + System.getProperty("user.dir"));
		LOG.info("#	Used Memory:  " + FileUtils.byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()));
		LOG.info("#	Free Memory:  " + FileUtils.byteCountToDisplaySize(runtime.freeMemory()));
		LOG.info("#	Total Memory: " + FileUtils.byteCountToDisplaySize(runtime.totalMemory()));
		LOG.info("#	Max Memory:   " + FileUtils.byteCountToDisplaySize(runtime.maxMemory()));
		LOG.info("# Date:         " + new Date().toString());
		LOG.info("# Data dir:     " + DB_FILE);
		LOG.info("# Version:      " + VERSION);
		LOG.info("####################################################################");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			LOG.error("Look and Feel exception", e);
		}
		if (PlatformUtil.isWindows()) {
			setCurrentProcessExplicitAppUserModelID("BeCast.YouPloader");
		}
		VersionComparator v = new VersionComparator();
		int updateAvaiable = v.compare("1.8.0_111",System.getProperty("java.version"));
		if(updateAvaiable > 0){
			if(JOptionPane.showConfirmDialog(null,
					LANG.getString("frmMain.errorjava.Message"),
					LANG.getString("frmMain.errorjava.title"), JOptionPane.DEFAULT_OPTION)!=-1){
				System.exit(0);
			}
		}
		File dataDir = new File(System.getProperty("user.home") + "/YouPloader/data/");
		if (!dataDir.exists()) {
			LOG.info(APP_NAME + " " + VERSION + " first launch. Database folder not found.", FrmMain.class);
			dataDir.mkdirs();
			if (!SQLite.setup()) {
				JOptionPane.showMessageDialog(null,
						String.format(LANG.getString("frmMain.errordatabase.Message"),
								System.getProperty("user.home") + "/YouPloader/data/"),
						LANG.getString("frmMain.errordatabase.title"), JOptionPane.ERROR_MESSAGE);
			}
			firstlaunch = true;
		} else {
			SQLite.getInstance();
			try {
				if(SQLite.getVersion()<DB_VERSION){
					SQLite.close();
					SQLite.makeBackup();
					SQLite.getInstance();
					SQLite.update();
				}
			} catch (SQLException e) {
				LOG.info("Failed to get DB Version ", e);
			}
		}
		s = Settings.getInstance();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FrmMain();
			}
		});
		
		if(s.setting.get("notify_updates").equals("1")){
			checkUpdates();
		}
	}
	
	private static void checkUpdates() {
		String[] gitVersion = GetVersion.get();
		if(gitVersion!=null){
			VersionComparator v = new VersionComparator();
			int updateAvaiable = v.compare(gitVersion[0], VERSION);
			if(updateAvaiable > 0){
				LOG.info("Update {} avaiable!", gitVersion[0]);
				UpdateNotice un = new UpdateNotice(gitVersion[0],gitVersion[1],gitVersion[2]);
				un.setVisible(true);
			}
		}
		
	}
	
	public static int getDBVersion() {
		return DB_VERSION;
	}

	public static void setCurrentProcessExplicitAppUserModelID(final String appID) {
		if (SetCurrentProcessExplicitAppUserModelID(new WString(appID)).longValue() != 0)
			throw new RuntimeException("unable to set current process explicit AppUserModelID to: " + appID);
	}

	private static native NativeLong GetCurrentProcessExplicitAppUserModelID(PointerByReference appID);
	private static native NativeLong SetCurrentProcessExplicitAppUserModelID(WString appID);

	static {
		if (PlatformUtil.isWindows())
		{
			Native.register("shell32");
		}
	}

}
