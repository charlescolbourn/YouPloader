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
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Font;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JTextPane;

public class FrmAbout extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8005016163820525203L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public FrmAbout() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("About YouPloader");
        ResourceBundle bundle = ResourceBundle.getBundle( "build" );
        String rev = bundle.getString( "git-sha-1" );
        String build = bundle.getString( "jenkins-build" );
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
		setBounds(100, 100, 500, 397);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(164dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("210px:grow"),},
			new RowSpec[] {
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("103px"),}));
		
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon(getClass().getResource("/yp.png")));
		contentPanel.add(label, "1, 2, 1, 9, left, fill");
		
		JLabel lblYouploader = new JLabel("YouPloader "+FrmMain.VERSION);
		lblYouploader.setFont(new Font("Arial Black", Font.PLAIN, 11));
		contentPanel.add(lblYouploader, "3, 2, left, default");
		
		JLabel lblGenuineparts = new JLabel(" © 2016 genuineparts");
		lblGenuineparts.setFont(new Font("Arial Black", Font.PLAIN, 11));
		contentPanel.add(lblGenuineparts, "3, 4, left, default");
		
		JLabel lblRevision = new JLabel("Revision "+rev);
		lblRevision.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(lblRevision, "3, 6");
		
		JLabel lblBuild = new JLabel("Build "+build);
		lblBuild.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(lblBuild, "3, 8");
		
		JLabel lblHttpsgithubcombecastyouploader = new JLabel("https://github.com/becast/youploader");
		lblHttpsgithubcombecastyouploader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(new URI(
									"https://github.com/becast/youploader"));
						} catch (IOException | URISyntaxException e1) {
							/* TODO: error handling */ }
					} else {
						/* TODO: error handling */ }
			}
		});
		lblHttpsgithubcombecastyouploader.setForeground(Color.BLUE);
		lblHttpsgithubcombecastyouploader.setFont(new Font("Arial", Font.PLAIN, 13));
		contentPanel.add(lblHttpsgithubcombecastyouploader, "3, 10");
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "1, 18, 3, 1, fill, fill");
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
