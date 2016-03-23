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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SpringLayout;
import java.awt.Font;

public class PlaylistPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -870661666925769377L;
	private static final Logger LOG = LoggerFactory.getLogger(PlaylistPanel.class);
	private frmMain parent;

	/**
	 * Create the panel.
	 */
	public PlaylistPanel(frmMain parent) {
		this.parent = parent;
		initComponents();
	}
	
	private void initComponents(){
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		JLabel lblNotImplementedYet = new JLabel("Not implemented yet :( Sorry!");
		springLayout.putConstraint(SpringLayout.NORTH, lblNotImplementedYet, 131, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblNotImplementedYet, 32, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNotImplementedYet, -147, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblNotImplementedYet, -34, SpringLayout.EAST, this);
		lblNotImplementedYet.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNotImplementedYet.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNotImplementedYet);
	}

}
