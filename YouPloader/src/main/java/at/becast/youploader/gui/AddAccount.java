/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.becast.youploader.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.settings.Settings;

/**
 *
 * @author Bernhard
 */
public class AddAccount extends javax.swing.JDialog {

	Settings s = Settings.getInstance();
	OAuth2 o2;
	frmMain parent;
	AccountManager AccMng = AccountManager.getInstance();
    /**
     * Creates new form AddAccount
     */
    public AddAccount(frmMain parent) {
    	this.parent = parent;
        initComponents();
        
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {

        AccName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Account");
        setMinimumSize(new java.awt.Dimension(350, 130));
        setName("Add Account"); // NOI18N
        setPreferredSize(new java.awt.Dimension(350, 130));
        setType(java.awt.Window.Type.POPUP);

        jLabel1.setText("Account Name:");

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnOkActionPerformed(evt);
				} catch (InterruptedException | IOException e) {
					JOptionPane.showMessageDialog(null,"Something went wrong with the Authorisation. Please try again.","Uh oh...", JOptionPane.ERROR_MESSAGE);
				}
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AccName, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOk))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(AccName))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) throws InterruptedException, IOException {
        if(AccName.getText()!=null && !AccName.getText().equals("")){
        	this.setVisible(false);
        	if(!Account.exists(AccName.getText())){
        		o2 = new OAuth2(s.setting.get("client_id") , s.setting.get("clientSecret"));
        		if (Desktop.isDesktopSupported()) {
        		      try {
        		        Desktop.getDesktop().browse(new URI("https://google.com/device"));
        		      } catch (IOException | URISyntaxException e) { /* TODO: error handling */ }
        		    } else { /* TODO: error handling */ }
        		parent.prep_modal(AccName.getText(),o2.getCode());
        		 Runnable runnable = new Runnable() {
        	            public void run() {
        	                try {
        						do {
        							Thread.sleep(5050);
        						} while (!o2.check());
        						new Account(AccName.getText(),o2.getRefreshToken()).save();
        						AccMng.set_active(AccName.getText());
        						parent.close_modal();
        					} catch (InterruptedException | IOException e) {
        						e.printStackTrace();
        					}
        	            }
        	        };
        	        new Thread(runnable).start();
        		parent.show_modal();
        	}else{
        		o2 = new OAuth2(s.setting.get("client_id"),s.setting.get("client_secret"), Account.read(AccName.getText()).refreshToken);
        		AccMng.set_active(AccName.getText());
        	}
        }else{
            JOptionPane.showMessageDialog(this,"The Accountname can not be empty.","Error", JOptionPane.WARNING_MESSAGE);
        }
    }


    private javax.swing.JTextField AccName;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel jLabel1;
}
