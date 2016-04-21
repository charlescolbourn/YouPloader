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

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.account.AccountManager;
import at.becast.youploader.account.AccountType;
import at.becast.youploader.util.UTF8ResourceBundle;
import at.becast.youploader.youtube.playlists.PlaylistData;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;

public class PlaylistPanel extends JPanel {

	//private static final Logger LOG = LoggerFactory.getLogger(PlaylistPanel.class);
	//private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private static final long serialVersionUID = -870661666925769377L;
	private FrmMain parent;
	private AccountManager AccMgr = AccountManager.getInstance();
	/**
	 * Create the panel.
	 */
	public PlaylistPanel(FrmMain parent) {
		this.parent = parent;
		initComponents();
	}
	
	protected void refreshPlaylists() {
		AccountType acc = (AccountType) parent.getCmbAccount().getSelectedItem();
		PlaylistData pl = new PlaylistData(this.AccMgr.getAuth(acc.getValue()));
		pl.get();		
	}

	private void initComponents(){
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(122dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:max(70dlu;default)"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 2, 3, 1, fill, fill");
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		PlaylistItem i = new PlaylistItem("1", "Test 1", "");
		panel.add(i);
		
		PlaylistItem i1 = new PlaylistItem("2", "Test 2", "");
		panel.add(i1);
		
		JButton btnGetPlaylists = new JButton("Get Playlists");
		btnGetPlaylists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshPlaylists();
			}
		});
		add(btnGetPlaylists, "2, 4");
		
		JButton btnAddPlaylist = new JButton("Add Playlist");
		add(btnAddPlaylist, "4, 4");
	}

}
