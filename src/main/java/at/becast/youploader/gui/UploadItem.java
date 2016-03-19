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
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import javax.swing.ImageIcon;

import net.miginfocom.swing.MigLayout;
import javax.swing.JProgressBar;
import javax.swing.JButton;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

public class UploadItem extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int upload_id;
	private JLabel lblName;
	private JLabel lblUrl;
	private JLabel lblStart;
	private JLabel lblRelease;
	private JProgressBar progressBar;
	private JLabel lblKbs;
	private JLabel lblETA;

	/**
	 * Create the panel.
	 */
	public UploadItem() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(new Color(240, 248, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(new Color(255, 255, 255));
			}
		});
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, null));
		setLayout(new MigLayout("", "[221.00px,grow,fill][88.00,grow,fill][130.00,grow,fill][30,trailing][30,trailing][30,trailing]", "[15.00px][][30][20.00]"));
		
		lblName = new JLabel("<Name>");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblName, "cell 0 0,alignx left,aligny center");
		
		JLabel lblStartedAt = new JLabel("Started at:");
		lblStartedAt.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblStartedAt, "cell 1 0,growx");
		
		lblStart = new JLabel("Not yet started");
		add(lblStart, "cell 2 0,growx");
		
		lblUrl = new JLabel("");
		lblUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Font font = lblUrl.getFont();
				lblUrl.getParent().setBackground(new Color(240, 248, 255));
				Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) font.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				lblUrl.setFont(font.deriveFont(attributes));
				lblUrl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Font font = lblUrl.getFont();
				Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) font.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, -1);
				lblUrl.setFont(font.deriveFont(attributes));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported()) {
      		      try {
      		        Desktop.getDesktop().browse(new URI(lblUrl.getText()));
      		      } catch (IOException | URISyntaxException e1) { /* TODO: error handling */ }
      		    } else { /* TODO: error handling */ }
			}
		});
		add(lblUrl, "cell 0 1");
		
		JLabel lblReleased = new JLabel("Released:");
		lblReleased.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblReleased, "cell 1 1,growx");
		
		lblRelease = new JLabel("not set");
		add(lblRelease, "cell 2 1,growx");
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		add(progressBar, "cell 0 2 3 1,grow");
		
		JButton btnDelete = new JButton("");
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/cross.png")));
		add(btnDelete, "cell 3 2,grow");
		
		JButton btnEdit = new JButton("");
		add(btnEdit, "cell 4 2,grow");
		
		lblKbs = new JLabel("");
		lblKbs.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblKbs, "cell 0 3");
		
		lblETA = new JLabel("00:00:00");
		lblETA.setIcon(new ImageIcon(getClass().getResource("/clock-icon.png")));
		add(lblETA, "cell 2 3");
		
		JButton btnCancel = new JButton("Cancel Upload");
		btnCancel.setIcon(new ImageIcon(getClass().getResource("/cancel.png")));
		add(btnCancel, "cell 3 3 3 1,grow");

	}
	
	public void set_id(int id){
		this.upload_id = id;
	}

	public JLabel getlblName() {
		return lblName;
	}
	public JLabel getlblUrl() {
		return lblUrl;
	}
	public JLabel getlblStart() {
		return lblStart;
	}
	public JLabel getlblRelease() {
		return lblRelease;
	}
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JLabel getLblKbs() {
		return lblKbs;
	}
	public JLabel getLblETA() {
		return lblETA;
	}
}
