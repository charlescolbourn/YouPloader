package at.becast.youploader.youtube.playlists;

import java.util.Locale;
import java.util.ResourceBundle;

import at.becast.youploader.account.AccountType;
import at.becast.youploader.gui.FrmMain;
import at.becast.youploader.util.UTF8ResourceBundle;

public class PlaylistUpdater implements Runnable {

	private FrmMain parent;
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	public PlaylistUpdater(FrmMain parent){
		this.parent = parent;
	}
	@Override
	public void run() {
		parent.getStatusBar().getProgressBar().setIndeterminate(true);
		parent.getStatusBar().setMessage("Updating Playlists");
		AccountType acc = (AccountType) FrmMain.getCmbAccount().getSelectedItem();
		PlaylistManager pm = PlaylistManager.getInstance();
		if(acc!=null){
			pm.save(acc.getValue());
			pm.load();
		}
		parent.getPlaylistPanel().clearPlaylists();
		parent.getPlaylistPanel().loadPlaylists();
		parent.getStatusBar().getProgressBar().setIndeterminate(false);
		parent.getStatusBar().setMessage(LANG.getString("Status.Ready"));
	}

}
