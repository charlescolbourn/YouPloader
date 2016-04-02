/* 
 * YouPloader Copyright (c) 2016 genuineparts (itsme@genuineparts.org)
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

package at.becast.youploader.gui;

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.youtube.data.Cookie;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

/**
 *
 * @author Bernhard
 */
public class EditAccount extends JDialog {

	private static final long serialVersionUID = 6117673731267538015L;
	private FrmMain parent;
	private AccountManager AccMng = AccountManager.getInstance();
	private int id;
	private String name;
	private JTextField AccName;
	private static final Logger LOG = LoggerFactory.getLogger(EditAccount.class);
	private static final ResourceBundle LANG = ResourceBundle.getBundle("lang", Locale.getDefault());
	/**
	 * Creates new form AddAccount
	 * @param name 
	 */
	public EditAccount(FrmMain parent, String name, int id) {
		this.parent = parent;
		this.id = id;
		this.name = name;
		initComponents();
		this.setLocationRelativeTo(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
	}

	private void initComponents() {

		AccName = new JTextField(this.name);
		JLabel lblAccName = new JLabel();
		JButton btnOk = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(LANG.getString("EditAccount.title"));
		setMinimumSize(new java.awt.Dimension(350, 130));
		setName("EditAccount"); // NOI18N
		setPreferredSize(new java.awt.Dimension(350, 130));
		setType(java.awt.Window.Type.POPUP);

		lblAccName.setText(LANG.getString("Account.name"));

		btnOk.setText(LANG.getString("Button.save"));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed();
			}
		});
		
		JButton btnDelete = new JButton(LANG.getString("Button.delete"));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteActionPerformed();
			}
		});
		
		JButton btnCancel = new JButton(LANG.getString("Button.cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed();
			}
		});
		
		JButton btnRefresh = new JButton("refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRefreshActionPerformed();
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnRefresh)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOk))
						.addGroup(layout.createSequentialGroup()
							.addGap(24)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(AccName, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
								.addComponent(lblAccName))))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAccName)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(AccName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOk)
						.addComponent(btnCancel)
						.addComponent(btnDelete)
						.addComponent(btnRefresh))
					.addContainerGap())
		);
		getContentPane().setLayout(layout);
	}

	private void btnOkActionPerformed() {
		if (AccName.getText() != null && !AccName.getText().equals("")) {
			LOG.info("Renaming Account {} to {}",  this.id, AccName.getText());
			this.setVisible(false);
			AccMng.rename(AccName.getText(), this.id);
			parent.refreshAccounts();
			LOG.info("Account renamed");
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, "The Accountname can not be empty.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void btnDeleteActionPerformed() {
		LOG.info("Deleting Account");
		int n= JOptionPane.showConfirmDialog(this, LANG.getString("EditAccount.confirmdelete.message"),LANG.getString("EditAccount.confirmdelete.title"),JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
		if (n == JOptionPane.OK_OPTION) {
			this.setVisible(false);
			AccMng.delete(this.id);
			parent.refreshAccounts();
			LOG.info("Account deleted");
			this.dispose();
		}else{
			LOG.info("Account delete canceled");
		}
	}
	
	private void btnCancelActionPerformed() {
		this.setVisible(false);
		this.dispose();
	}
	
	private void btnRefreshActionPerformed() {
		try {
			Browser b = new Browser(Account.read(this.id), false);
			b.setVisible(true);
			b.loadURL("https://www.youtube.com");
			Account acc = null;
			try {
				acc = Account.read(4);
			} catch (IOException e) {
				LOG.error("Unable to read Account ", e);
			}
			CookieStore cookieStore = new BasicCookieStore();
			for (Cookie serializableCookie : acc.getCookie()) {
				BasicClientCookie cookie = new BasicClientCookie(serializableCookie.getCookie().getName(), serializableCookie.getCookie().getValue());
				cookie.setDomain(serializableCookie.getCookie().getDomain());
				cookieStore.addCookie(cookie);
			}
			HttpClient client = HttpClientBuilder.create().useSystemProperties().setDefaultCookieStore(cookieStore).setUserAgent(FrmMain.APP_NAME+" "+FrmMain.VERSION).build();
			Unirest.setHttpClient(client);
			HttpResponse<String> response = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("User-Agent", FrmMain.APP_NAME+" "+FrmMain.VERSION);
			try {			
				response = Unirest.get("http://becast.at/test.php").asString();
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOG.debug(response.getBody());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
