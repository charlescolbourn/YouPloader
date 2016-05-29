package at.becast.youploader.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import at.becast.youploader.util.UTF8ResourceBundle;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

public class UpdateNotice extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277493043241496978L;
	private final JPanel contentPanel = new JPanel();
	//private static final Logger LOG = LoggerFactory.getLogger(UpdateNotice.class);
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());

	/**
	 * Create the dialog.
	 */
	public UpdateNotice(String gitVersion, String changelog) {
		setTitle(LANG.getString("frmMain.newVersion.title"));
		setBounds(100, 100, 551, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(41dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
		{
			JTextPane textPane = new JTextPane();
			textPane.setText(String.format(LANG.getString("frmMain.newVersion.Message"), gitVersion));
			textPane.setBackground(UIManager.getColor("Button.background"));
			contentPanel.add(textPane, "2, 2, fill, fill");
		}
		{
			JLabel lblChangelog = new JLabel(LANG.getString("UpdateNotice.changelog"));
			contentPanel.add(lblChangelog, "2, 4");
		}
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "2, 6, fill, fill");
		{
			JTextPane txtChangelog = new JTextPane();
			txtChangelog.setContentType("text/html");
			txtChangelog.setText(changelog);
			scrollPane.setViewportView(txtChangelog);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton dlButton = new JButton(LANG.getString("UpdateNotice.download"));
				dlButton.setActionCommand("OK");
				buttonPane.add(dlButton);
				getRootPane().setDefaultButton(dlButton);
			}
			{
				JButton cancelButton = new JButton(LANG.getString("Button.cancel"));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
