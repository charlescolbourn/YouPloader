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
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.account.Account;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.settings.Settings;
import at.becast.youploader.util.UTF8ResourceBundle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Bernhard
 */
public class AddAccount extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4804877236112916847L;
	private Settings s = Settings.getInstance();
	private OAuth2 o2;
	private FrmMain parent;
	private JTextField AccName;
	private Thread t;
	private Browser browser;
	private static final Logger LOG = LoggerFactory.getLogger(AddAccount.class);
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());

	/**
	 * Creates new form AddAccount
	 */
	public AddAccount(FrmMain parent) {
		setAlwaysOnTop(true);
		this.parent = parent;
		initComponents();
		this.setLocationRelativeTo(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
	}

	private void initComponents() {

		AccName = new JTextField();
		AccName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					try {
						btnOkActionPerformed();
					} catch (InterruptedException | IOException e) {
						LOG.error("Error linking Account", e);
					}
				}
			}
		});
		JLabel lblAccName = new JLabel();
		JButton btnOk = new JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(LANG.getString("AddAccount.title"));
		setMinimumSize(new java.awt.Dimension(350, 130));
		setName("Add Account"); // NOI18N
		setPreferredSize(new java.awt.Dimension(350, 130));
		setType(Type.UTILITY);

		lblAccName.setText(LANG.getString("Account.name"));

		btnOk.setText(LANG.getString("Button.ok"));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnOkActionPerformed();
				} catch (InterruptedException | IOException e) {
					LOG.error("Error linking Account", e);
					JOptionPane.showMessageDialog(null,
							LANG.getString("AddAccount.autherror.message"), LANG.getString("AddAccount.autherror.title"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(24, 24, 24)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(AccName, javax.swing.GroupLayout.PREFERRED_SIZE, 249,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(btnOk))
								.addComponent(lblAccName))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblAccName)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnOk).addComponent(AccName))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}

	private void btnOkActionPerformed() throws InterruptedException, IOException {
		if (AccName.getText() != null && !AccName.getText().equals("")) {
			this.setVisible(false);
			LOG.info("Linking Account {}", AccName.getText());
			if (!Account.exists(AccName.getText())) {
				LOG.info("Account does not exist");
				o2 = new OAuth2(s.setting.get("client_id"), s.setting.get("clientSecret"));
				Account account = new Account(AccName.getText());
				String code = o2.getCode();
				LOG.info("Created Auth code {}", code);
				browser = new Browser(account, true);
				browser.setVisible(true);
				browser.loadURL("https://accounts.google.com/ServiceLogin?service=youtube&hl=en&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Ffeature%3Dedit_video%26next%3Dmy_videos_upload%26hl%3Dde%26action_handle_signin%3Dtrue%26app%3Ddesktop&uilel=3");
				parent.prepModal(account, code, this);
				Runnable runnable = new Runnable() {
					public void run() {
						try {
							do {
								Thread.sleep(5050);
							} while (!o2.check() && !Thread.currentThread().isInterrupted());
							account.setRefreshToken(o2.getRefreshToken());
							LOG.info("Got refresh token {}", account.refreshToken);
							account.loadCookie();
							account.save();
							browser.dispose();
							parent.refreshAccounts();
							parent.closeModal();
						} catch (InterruptedException | IOException e) {
							LOG.error("Error while linking", e);
						}
					}
				};
				t = new Thread(runnable);
				t.start();
				parent.showModal();
			} else {
				LOG.info("Account {} already exists", AccName.getText());
				JOptionPane.showMessageDialog(this, LANG.getString("AddAccount.nameexists.message"), LANG.getString("AddAccount.nameexists.title"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, LANG.getString("AddAccount.nameerror.message"), LANG.getString("AddAccount.nameerror.title"),
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void cancel() {
		t.interrupt();
		browser.setVisible(false);
		browser.dispose();
	}

}
