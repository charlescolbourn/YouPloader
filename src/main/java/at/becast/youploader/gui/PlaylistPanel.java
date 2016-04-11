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
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class PlaylistPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -870661666925769377L;

	/**
	 * Create the panel.
	 */
	public PlaylistPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(212dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 2, 3, 1, fill, fill");
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("dsds");
		chckbxNewCheckBox.setVerticalAlignment(SwingConstants.TOP);
		chckbxNewCheckBox.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png"))));
		panel.add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox1 = new JCheckBox("dsds");
		chckbxNewCheckBox1.setVerticalAlignment(SwingConstants.TOP);
		chckbxNewCheckBox1.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png"))));
		panel.add(chckbxNewCheckBox1);
		
		JCheckBox chckbxNewCheckBox2 = new JCheckBox("dsds");
		chckbxNewCheckBox2.setVerticalAlignment(SwingConstants.TOP);
		chckbxNewCheckBox2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png"))));
		panel.add(chckbxNewCheckBox2);
		
		JButton btnAddPlaylist = new JButton("Add Playlist");
		add(btnAddPlaylist, "4, 4");
		initComponents();
	}
	
	private void initComponents(){
	}

}
