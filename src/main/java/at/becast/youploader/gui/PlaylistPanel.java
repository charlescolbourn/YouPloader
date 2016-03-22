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
