package at.becast.youploader.gui.statusbar;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.util.UTF8ResourceBundle;

import com.jgoodies.forms.layout.FormSpecs;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class StatusBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5923823447707168982L;
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private JLabel lblMessage;
	private JProgressBar progressBar;

   public StatusBar() {
       super();
       setLayout(new FormLayout(new ColumnSpec[] {
       		FormSpecs.RELATED_GAP_COLSPEC,
       		ColumnSpec.decode("max(148dlu;default):grow"),
       		FormSpecs.RELATED_GAP_COLSPEC,
       		FormSpecs.DEFAULT_COLSPEC,
       		FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,},
       	new RowSpec[] {
       		FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
       		FormSpecs.DEFAULT_ROWSPEC,
       		FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,}));
       
       lblMessage = new JLabel(LANG.getString("Status.Ready"));
       add(lblMessage, "2, 2, fill, fill");
       
       progressBar = new JProgressBar();
       add(progressBar, "4, 2, fill, fill");
   }
    
   public void setMessage(String message){
	   lblMessage.setText(message);
   }
	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
