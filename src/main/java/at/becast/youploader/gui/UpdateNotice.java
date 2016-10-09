package at.becast.youploader.gui;

import java.awt.BorderLayout;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class UpdateNotice extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277493043241496978L;
	private final JPanel contentPanel = new JPanel();
	//private static final Logger LOG = LoggerFactory.getLogger(UpdateNotice.class);
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private JProgressBar progressBar;
	private JButton dlButton;
	private JButton cancelButton;

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
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				dlButton = new JButton(LANG.getString("UpdateNotice.download"));
				dlButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					}
				});
				{
					progressBar = new JProgressBar();
					progressBar.setStringPainted(true);
				}
				dlButton.setActionCommand("OK");
				getRootPane().setDefaultButton(dlButton);
			}
			{
				cancelButton = new JButton(LANG.getString("Button.cancel"));
				cancelButton.setActionCommand("Cancel");
			}
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(205)
						.addComponent(dlButton)
						.addGap(5)
						.addComponent(cancelButton))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(dlButton)
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addComponent(cancelButton))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}
}
