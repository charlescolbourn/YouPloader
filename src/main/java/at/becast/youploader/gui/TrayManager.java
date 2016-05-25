package at.becast.youploader.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.Main;
import at.becast.youploader.util.UTF8ResourceBundle;

public class TrayManager {
    private TrayIcon trayIcon;
    private SystemTray tray;
    private FrmMain parent;
    private static final Logger LOG = LoggerFactory.getLogger(TrayManager.class);
    private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
    
    public TrayManager(FrmMain parent){
    	this.parent = parent;
		if(SystemTray.isSupported()){
			if(Main.debug){
				LOG.debug("Tray supported");
			}
			tray = SystemTray.getSystemTray();
			Image image=Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png"));
			ActionListener exitListener=new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
			        System.exit(0);
				}
			};
			PopupMenu popup=new PopupMenu();
			MenuItem defaultItem=new MenuItem(LANG.getString("tray.exit"));
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);
			defaultItem=new MenuItem(LANG.getString("tray.open"));
			defaultItem.addActionListener(new ActionListener() {
				@Override
			    public void actionPerformed(ActionEvent e) {
					normalize();
			    }
			
			});
			popup.add(defaultItem);
			trayIcon=new TrayIcon(image, "YouPloader", popup);
			trayIcon.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent evt) {
			        if (evt.getClickCount() == 2) {
			        	normalize();
			        }
			    }

			});
			trayIcon.setImageAutoSize(true);
		}else{
			if(Main.debug){
				LOG.debug("Tray not supported");
			}
		}
    }
    
    private void normalize(){
		this.parent.setVisible(true);
		this.parent.setExtendedState(JFrame.NORMAL);
    }
    
    public boolean traySupported(){
    	return SystemTray.isSupported();
    }


	public void add() {
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			LOG.error("Error adding tray icon", e);
		}
		
	}


	public void remove() {
		tray.remove(trayIcon);
	}
}
