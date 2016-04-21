package at.becast.youploader.gui;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;

public class PlaylistItem extends JPanel {

	private String YTId;
	private static final long serialVersionUID = 5097223862797550174L;

	/**
	 * Create the panel.
	 */
	public PlaylistItem(String YTId, String name, String Image) {
		this.setYTId(YTId);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("310px:grow"),
				ColumnSpec.decode("97px"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:23px:grow"),
				FormSpecs.LINE_GAP_ROWSPEC,}));
		
		JCheckBox chckbxNewCheckBox = new JCheckBox(name);
		add(chckbxNewCheckBox, "1, 2, left, fill");
		
		JLabel lblNewLabel = new JLabel("");
		add(lblNewLabel, "2, 2, fill, fill");

	}

	public String getYTId() {
		return YTId;
	}

	public void setYTId(String yTId) {
		YTId = yTId;
	}

}
