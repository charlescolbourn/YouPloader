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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.settings.Settings;

import javax.swing.SwingConstants;

import java.awt.Font;
import javax.swing.JTextField;

public class ModalDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static String AccName = null;
	private static Frame parent = null;
	private final JPanel contentPanel = new JPanel();
	private JTextField labelcode;
	private JLabel check_label;
	Settings s = Settings.getInstance();
	OAuth2 o2;

	/**
	 * Create the dialog.
	 * @param code 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public ModalDialog(Frame parent, String AccName, String code) {
		super(parent);
		setTitle("Linking YouTube Account");
		setAlwaysOnTop(true);
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 551, 401);
		getContentPane().setLayout(new BorderLayout());
		ModalDialog.AccName = AccName;
		ModalDialog.parent = parent;
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("535px:grow"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("294px"),}));
		{
			JButton btnNewButton = new JButton("Open Browser to http://google.com/device");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (Desktop.isDesktopSupported()) {
	        		      try {
	        		        Desktop.getDesktop().browse(new URI("https://google.com/device"));
	        		      } catch (IOException | URISyntaxException e1) { /* TODO: error handling */ }
	        		    } else { /* TODO: error handling */ }
				}
			});
			contentPanel.add(btnNewButton, "2, 4, center, default");
		}
		{
			JLabel lblNewLabel = new JLabel("A Browser should have opened! If not please click the Button below.");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "2, 2, fill, top");
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Please enter the following code:");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel_1, "2, 6, fill, default");
		}
		{
			check_label = new JLabel("");
			check_label.setHorizontalAlignment(SwingConstants.CENTER);
			check_label.setIcon(new ImageIcon(getClass().getResource("/load_icon.gif")));
			contentPanel.add(check_label, "2, 8, fill, top");
		}
		{
			labelcode = new JTextField();
			labelcode.setText(code);
			labelcode.setHorizontalAlignment(SwingConstants.CENTER);
			labelcode.setFont(new Font("Tahoma", Font.BOLD, 19));
			contentPanel.add(labelcode, "2, 10, fill, fill");
			labelcode.setColumns(10);
		}
		{
			JLabel lblThisWindowWill = new JLabel("This window will close as soon as the Account is successfully linked");
			lblThisWindowWill.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblThisWindowWill, "2, 12");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						close();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
       
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
	}
	
	public void success() {
		check_label.setIcon(new ImageIcon(getClass().getResource("/load_icon.gif")));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.close();
	}

	public void setCode(String text) {
		labelcode.setText(text);
		
	}
	
}
