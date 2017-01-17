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

package at.becast.youploader.gui;

import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.settings.Settings;
import at.becast.youploader.util.UTF8ResourceBundle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Bernhard
 */
public class AddTemplate extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2959579909274103655L;
	private FrmMain parent;
	private Settings s = Settings.getInstance();
	private JTextField templateName;
	private static final Logger LOG = LoggerFactory.getLogger(AddTemplate.class);
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());

	/**
	 * Creates new form AddAccount
	 */
	public AddTemplate(FrmMain parent) {
		setAlwaysOnTop(true);
		this.parent = parent;
		initComponents();
		this.setLocationRelativeTo(null);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
	}

	private void initComponents() {

		templateName = new JTextField();
		templateName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					btnOkActionPerformed();
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
				btnOkActionPerformed();
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(24, 24, 24)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(templateName, javax.swing.GroupLayout.PREFERRED_SIZE, 249,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(btnOk))
								.addComponent(lblAccName))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblAccName)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnOk).addComponent(templateName))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}

	private void btnOkActionPerformed() {
		if (templateName.getText() != null && !templateName.getText().equals("")) {
			this.setVisible(false);
			LOG.info("Adding Template {}", templateName.getText());
			
		} else {
			JOptionPane.showMessageDialog(this, LANG.getString("AddAccount.nameerror.message"), LANG.getString("AddAccount.nameerror.title"),
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void cancel() {

	}

}
