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
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import at.becast.youploader.account.AccountManager;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.settings.Settings;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * @author Bernhard
 */
public class EditAccount extends javax.swing.JDialog {

	private static final long serialVersionUID = 6117673731267538015L;
	Settings s = Settings.getInstance();
	OAuth2 o2;
	frmMain parent;
	AccountManager AccMng = AccountManager.getInstance();
	private int id;
	private String name;
	private JTextField AccName;
	private JButton btnOk;
	private JLabel jLabel1;
	private static final Logger LOG = LoggerFactory.getLogger(EditAccount.class);
	private static final ResourceBundle LANG = ResourceBundle.getBundle("lang", Locale.getDefault());
	/**
	 * Creates new form AddAccount
	 * @param name 
	 */
	public EditAccount(frmMain parent, String name, int id) {
		this.parent = parent;
		this.id = id;
		this.name = name;
		initComponents();
		this.setLocationRelativeTo(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
	}

	private void initComponents() {

		AccName = new JTextField(this.name);
		jLabel1 = new JLabel();
		btnOk = new JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(LANG.getString("EditAccount.title"));
		setMinimumSize(new java.awt.Dimension(350, 130));
		setName("EditAccount"); // NOI18N
		setPreferredSize(new java.awt.Dimension(350, 130));
		setType(java.awt.Window.Type.POPUP);

		jLabel1.setText(LANG.getString("Account.name"));

		btnOk.setText(LANG.getString("Button.save"));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkActionPerformed(e);
			}
		});
		
		JButton btnDelete = new JButton(LANG.getString("Button.delete"));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteActionPerformed(e);
			}
		});
		
		JButton btnCancel = new JButton(LANG.getString("Button.cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancelActionPerformed(e);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnDelete)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOk))
						.addGroup(layout.createSequentialGroup()
							.addGap(24)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(AccName, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
								.addComponent(jLabel1))))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(AccName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOk)
						.addComponent(btnCancel)
						.addComponent(btnDelete))
					.addContainerGap())
		);
		getContentPane().setLayout(layout);
	}

	private void btnOkActionPerformed(ActionEvent e) {
		if (AccName.getText() != null && !AccName.getText().equals("")) {
			LOG.info("Renaming Account {} to {}",  this.id, AccName.getText());
			AccMng.rename(AccName.getText(), this.id);
			parent.refresh_accounts();
			LOG.info("Account renamed");
			this.setVisible(false);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, "The Accountname can not be empty.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void btnDeleteActionPerformed(ActionEvent e) {
		LOG.info("Deleting Account");
		int n= JOptionPane.showConfirmDialog(this, LANG.getString("EditAccount.confirmdelete.message"),LANG.getString("EditAccount.confirmdelete.title"),JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
		if (n == JOptionPane.OK_OPTION) {
			AccMng.delete(this.id);
			parent.refresh_accounts();
			LOG.info("Account deleted");
			this.setVisible(false);
			this.dispose();
		}
		LOG.info("Account delete canceled");
	}
	
	private void btnCancelActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

}
