package at.becast.youploader.gui;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JProgressBar;
import javax.swing.JButton;

public class UploadItem extends JPanel {

	/**
	 * Create the panel.
	 */
	public UploadItem() {
		setLayout(new MigLayout("", "[221.00px,grow,fill][88.00,grow,fill][130.00,grow,fill][30,trailing][30,trailing][30,trailing]", "[14px][14.00][][30][]"));
		
		JLabel label = new JLabel("<Name>");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		add(label, "cell 0 0,alignx left,aligny center");
		
		JLabel lblStartedAt = new JLabel("Started at:");
		lblStartedAt.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblStartedAt, "cell 1 0,growx");
		
		JLabel label_1 = new JLabel("<Startdate>");
		add(label_1, "cell 2 0,growx");
		
		JLabel label_2 = new JLabel("<url>");
		add(label_2, "cell 0 2");
		
		JLabel lblReleased = new JLabel("Released:");
		lblReleased.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblReleased, "cell 1 2,growx");
		
		JLabel label_3 = new JLabel("<released>");
		add(label_3, "cell 2 2,growx");
		
		JButton btnNewButton_1 = new JButton("");
		add(btnNewButton_1, "cell 3 3,grow");
		
		JButton btnNewButton_2 = new JButton("");
		add(btnNewButton_2, "cell 4 3,grow");
		
		JButton btnNewButton_3 = new JButton("");
		add(btnNewButton_3, "cell 5 3,grow");
		
		JProgressBar progressBar = new JProgressBar();
		add(progressBar, "cell 0 4 3 1,grow");
		
		JButton btnNewButton = new JButton("New button");
		add(btnNewButton, "cell 3 4 3 1,growx");

	}

}
