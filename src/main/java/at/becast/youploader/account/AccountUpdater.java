package at.becast.youploader.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.Main;
import at.becast.youploader.account.AccountType;
import at.becast.youploader.gui.FrmMain;
import at.becast.youploader.util.UTF8ResourceBundle;

public class AccountUpdater implements Runnable {

	private FrmMain parent;
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private static final Logger LOG = LoggerFactory.getLogger(AccountUpdater.class);
	public AccountUpdater(FrmMain parent){
		this.parent = parent;
	}
	@Override
	public void run() {
		parent.getStatusBar().getProgressBar().setIndeterminate(true);
		parent.getStatusBar().setMessage("Updating Accounts");
		AccountManager am = AccountManager.getInstance();
		HashMap<AccountType,Integer> accdata = am.load();
		for(Map.Entry<AccountType,Integer> entry : accdata.entrySet()){
			AccountType acc = entry.getKey();
			if(Main.debug)
				LOG.debug("Updating " + acc.toString());
			
			parent.getStatusBar().setMessage("Updating " + acc.toString());
			Account a = null;
			try {
				a = Account.read(acc.getValue());
				a.loadCookie();
				a.updateCookie(a.id);
				
				if(Main.debug)
					LOG.debug("Updated Cookie: ", a.name);
			} catch (IOException e) {
				LOG.error("Error updating Cookie", e);
			}
		}
		parent.getStatusBar().getProgressBar().setIndeterminate(false);
		parent.getStatusBar().setMessage(LANG.getString("Status.Ready"));
	}

}
