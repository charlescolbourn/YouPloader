package at.becast.youploader.gui;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;

public class PlaylistItem extends JPanel {

	private int id;
	private String YTId;
	private static final long serialVersionUID = 5097223862797550174L;
	private JCheckBox chkIsSelected;

	/**
	 * Create the panel.
	 */
	public PlaylistItem(int id, String YTId, String name, ImageIcon Image) {
		this.id = id;
		this.setYTId(YTId);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("221px:grow"),
				ColumnSpec.decode("97px:grow"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:46px:grow"),
				FormSpecs.LINE_GAP_ROWSPEC,}));
		
		chkIsSelected = new JCheckBox(name);
		add(chkIsSelected, "1, 2, left, fill");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(Image);
		add(lblNewLabel, "2, 2, fill, fill");

	}

	public int getId() {
		return id;
	}

	public String getYTId() {
		return YTId;
	}

	public void setYTId(String yTId) {
		YTId = yTId;
	}

	public boolean isSelected() {
		return chkIsSelected.isSelected();
	}
	
	public void setSelected(boolean selected) {
		chkIsSelected.setSelected(selected);
	}
}
