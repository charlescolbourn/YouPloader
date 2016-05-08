package at.becast.youploader.gui;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.database.SQLite;

import com.jgoodies.forms.layout.FormSpecs;

import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PlaylistPanelItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7026650769642399964L;
	private int id;
	private boolean shown;
	/**
	 * Create the panel.
	 */
	public PlaylistPanelItem(String name, int id, boolean shown) {
		this.id = id;
		this.setShown(shown);
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		JCheckBox chkboxHidden = new JCheckBox(name);
		chkboxHidden.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
    			String shown;
    			if(chkboxHidden.isSelected()){
    				shown = "1";
    			}else{
    				shown = "0";
    			}
    			try {
					SQLite.setPlaylistHidden(id, shown);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
		});
		add(chkboxHidden, "2, 2, fill, fill");
		chkboxHidden.setSelected(this.isShown());
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(getClass().getResource("/delete.png")));
		add(btnNewButton, "4, 2, fill, default");

	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

}
