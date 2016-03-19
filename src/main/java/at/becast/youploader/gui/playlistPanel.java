package at.becast.youploader.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import java.awt.Font;

public class playlistPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public playlistPanel() {
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
