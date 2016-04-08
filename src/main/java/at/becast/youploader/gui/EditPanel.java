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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.templates.Item;
import at.becast.youploader.templates.Template;
import at.becast.youploader.templates.TemplateManager;
import at.becast.youploader.util.NativeJFileChooser;
import at.becast.youploader.util.UTF8ResourceBundle;
import at.becast.youploader.youtube.LicenseType;
import at.becast.youploader.youtube.VisibilityType;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;

/**
 *
 * @author Bernhard
 */
public class EditPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2023946504262191056L;
	private static final Logger LOG = LoggerFactory.getLogger(EditPanel.class);
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private JComboBox<Item> cmbTemplate;
	private JTextField txtStartDir;
	private JTextField txtEndDir;
	private FrmMain parent;
	private Boolean adding = false;
	private JComboBox<VisibilityType> cmbVisibility;
	private DateTimePicker dateTimePicker;
	private JComboBox<LicenseType> cmbLicense;
	private TemplateManager TemplateMgr = TemplateManager.getInstance();
	private JTextField txtThumbnail;
	private JTextField txtGameTitle;
	private DateTimePicker dateTimePickerStart;
	private JCheckBox chckbxAllowEmbedding;
	private JCheckBox chckbxMakeStatisticsPublic;
	private JTextPane txtMessage;
	private JCheckBox chckbxGoogle;
	private JCheckBox chckbxTwitter;
	private JCheckBox chckbxFacebook;
	private JCheckBox chckbxAllowComments;
	private JCheckBox chckbxAgeRestriction;
	

	/**
	 * Creates new form editPanel
	 */
	public EditPanel(FrmMain parent) {
		this.parent = parent;
		initComponents();
	}

	private void initComponents() {
		JLabel lblTemplate = new JLabel();
		cmbTemplate = new JComboBox<Item>();
		loadTemplates();
		cmbTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				templateCmbChanged(e);
			}
		});
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("79px"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("center:max(51dlu;default):grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("center:max(10dlu;pref):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:max(10dlu;pref):grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("right:30px"),
				ColumnSpec.decode("2dlu"),
				ColumnSpec.decode("right:30px"),
				ColumnSpec.decode("2dlu"),
				ColumnSpec.decode("right:30px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				RowSpec.decode("1dlu"),
				RowSpec.decode("20px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("20px"),}));

		lblTemplate.setText(LANG.getString("EditPanel.Template")+":");
		add(lblTemplate, "2, 2, right, center");
		add(cmbTemplate, "4, 2, 5, 1, fill, fill");

		JButton btnDeleteTemplate = new JButton("");
		btnDeleteTemplate.setToolTipText(LANG.getString("EditPanel.DeleteTemplate"));
		btnDeleteTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteTemplate();
			}
		});
		
		JButton btnNewTemplate = new JButton("");
		btnNewTemplate.setToolTipText(LANG.getString("EditPanel.SaveNewTemplate"));
		btnNewTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTemplate();
			}
		});
				
		JButton btnSaveTemplate = new JButton("");
		btnSaveTemplate.setToolTipText(LANG.getString("EditPanel.SaveTemplate"));
		btnSaveTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveTemplate();
			}
		});
		btnSaveTemplate.setIcon(new ImageIcon(getClass().getResource("/disk.png")));
		add(btnSaveTemplate, "10, 2, fill, fill");
		btnNewTemplate.setIcon(new ImageIcon(getClass().getResource("/add.png")));
		add(btnNewTemplate, "12, 2, fill, fill");
		btnDeleteTemplate.setIcon(new ImageIcon(getClass().getResource("/cross.png")));
		add(btnDeleteTemplate, "14, 2, fill, fill");

		JLabel lblStartDirectory = new JLabel(LANG.getString("EditPanel.StartDir")+":");
		add(lblStartDirectory, "2, 4, right, center");

		txtStartDir = new JTextField();
		add(txtStartDir, "4, 4, 5, 1, fill, fill");
		txtStartDir.setColumns(10);

		JButton btnStartDir = new JButton("");
		btnStartDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NativeJFileChooser chooser = new NativeJFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtStartDir.setText(chooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		btnStartDir.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
		add(btnStartDir, "10, 4, fill, fill");

		JLabel lblEndDirectory = new JLabel(LANG.getString("EditPanel.EndDir")+":");
		add(lblEndDirectory, "2, 6, right, center");

		txtEndDir = new JTextField();
		add(txtEndDir, "4, 6, 5, 1, fill, fill");
		txtEndDir.setColumns(10);

		JButton btnEndDir = new JButton("");
		btnEndDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NativeJFileChooser chooser = new NativeJFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtEndDir.setText(chooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		btnEndDir.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
		add(btnEndDir, "10, 6, fill, fill");

		JLabel lblVisibility = new JLabel(LANG.getString("EditPanel.Visibility")+":");
		add(lblVisibility, "2, 8, right, default");

		dateTimePicker = new DateTimePicker();
		dateTimePicker.setEnabled(false);
		dateTimePicker.getEditor().setEnabled(false);
		Calendar calendar = dateTimePicker.getMonthView().getCalendar();
		// starting today if we are in a hurry
		calendar.setTime(new Date());
		dateTimePicker.getMonthView().setLowerBound(calendar.getTime());

		cmbVisibility = new JComboBox<VisibilityType>();		
		cmbVisibility.setModel(new DefaultComboBoxModel<VisibilityType>(VisibilityType.values()));
		add(cmbVisibility, "4, 8, 5, 1, fill, fill");

		JLabel lblReleaseAt = new JLabel(LANG.getString("EditPanel.Relase")+":");
		add(lblReleaseAt, "2, 10, right, default");

		add(dateTimePicker, "4, 10, 5, 1, fill, fill");

		JLabel lblLicense = new JLabel(LANG.getString("EditPanel.License")+":");
		add(lblLicense, "2, 12, right, default");

		cmbLicense = new JComboBox<LicenseType>();
		cmbLicense.setModel(new DefaultComboBoxModel<LicenseType>(LicenseType.values()));
		add(cmbLicense, "4, 12, 5, 1, fill, fill");
		cmbLicense.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cmbLicense.getSelectedItem() == LicenseType.CC){
					parent.monetisation(true);
				}else{
					parent.monetisation(chckbxAgeRestriction.isSelected());
				}
			}
		});
		
		JLabel lblThumbnail = new JLabel(LANG.getString("EditPanel.Thumbnail")+":");
		add(lblThumbnail, "2, 14, right, default");
		
		txtThumbnail = new JTextField();
		txtThumbnail.setDragEnabled(true);
		txtThumbnail.setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 8809983794742040683L;
			public synchronized void drop(DropTargetDropEvent evt) {
	            try {
	                evt.acceptDrop(DnDConstants.ACTION_COPY);
	                @SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
	                        .getTransferable().getTransferData(
	                                DataFlavor.javaFileListFlavor);
	                for (File file : droppedFiles) {
	                	if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")){
	                		txtThumbnail.setText(file.getAbsolutePath());
	                	}
	                }
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    });
		add(txtThumbnail, "4, 14, 5, 1, fill, fill");
		txtThumbnail.setColumns(10);
		
		JButton btnThumbnail = new JButton("");
		btnThumbnail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NativeJFileChooser chooser = new NativeJFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtThumbnail.setText(chooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		btnThumbnail.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
		add(btnThumbnail, "10, 14, fill, fill");
		
		JLabel lblStartUploadAt = new JLabel(LANG.getString("EditPanel.StartUpload")+":");
		lblStartUploadAt.setEnabled(false);
		add(lblStartUploadAt, "2, 16, right, default");
		
		dateTimePickerStart = new DateTimePicker();
		dateTimePickerStart.setEnabled(false);
		add(dateTimePickerStart, "4, 16, 5, 1, fill, fill");
		
		JLabel lblGameTitle = new JLabel(LANG.getString("EditPanel.Gametitle")+":");
		lblGameTitle.setEnabled(false);
		add(lblGameTitle, "2, 18, right, default");
		
		txtGameTitle = new JTextField();
		txtGameTitle.setEnabled(false);
		add(txtGameTitle, "4, 18, 5, 1, fill, fill");
		txtGameTitle.setColumns(10);
		
		JLabel lblAdditionalSettings = new JLabel(LANG.getString("EditPanel.OtherSettings")+":");
		add(lblAdditionalSettings, "2, 20, right, default");
		
		chckbxAllowEmbedding = new JCheckBox(LANG.getString("EditPanel.embedding"));
		add(chckbxAllowEmbedding, "4, 20, 3, 1");
		
		chckbxAgeRestriction = new JCheckBox(LANG.getString("EditPanel.AgeRestriction"));
		chckbxAgeRestriction.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(chckbxAgeRestriction.isSelected()){
					parent.monetisation(chckbxAgeRestriction.isSelected());
				}else{
					parent.monetisation(cmbLicense.getSelectedItem() == LicenseType.CC);
				}
			}
		});
		add(chckbxAgeRestriction, "8, 20, 3, 1, left, default");
		
		chckbxMakeStatisticsPublic = new JCheckBox(LANG.getString("EditPanel.Statistics"));
		chckbxMakeStatisticsPublic.setVerticalAlignment(SwingConstants.TOP);
		add(chckbxMakeStatisticsPublic, "4, 22, 3, 1, left, default");
		
		chckbxAllowComments = new JCheckBox(LANG.getString("EditPanel.AllowComments"));
		chckbxAllowComments.setSelected(true);
		add(chckbxAllowComments, "8, 22, 3, 1, left, default");
		
		JLabel lblMessage = new JLabel(LANG.getString("EditPanel.Message")+":");
		add(lblMessage, "2, 24, right, default");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "4, 24, 5, 7, fill, fill");
		
		txtMessage = new JTextPane();
		txtMessage.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtMessage.setEnabled(false);
		scrollPane.setViewportView(txtMessage);
		
		chckbxGoogle = new JCheckBox("Google+");
		chckbxGoogle.setSelected(true);
		chckbxGoogle.setEnabled(false);
		add(chckbxGoogle, "2, 26, left, default");
		
		chckbxTwitter = new JCheckBox("Twitter");
		chckbxTwitter.setSelected(true);
		chckbxTwitter.setEnabled(false);
		add(chckbxTwitter, "2, 28, left, default");
		
		chckbxFacebook = new JCheckBox("Facebook");
		chckbxFacebook.setSelected(true);
		chckbxFacebook.setEnabled(false);
		add(chckbxFacebook, "2, 30, left, default");
		
		cmbVisibility.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if (cmbVisibility.getSelectedItem() == VisibilityType.SCHEDULED) {
					setDateEnabled(true);
					setMessageEnabled(true);
				} else if(cmbVisibility.getSelectedItem() == VisibilityType.PUBLIC) {
					setDateEnabled(false);
					setMessageEnabled(true);
				} else {
					setDateEnabled(false);
					setMessageEnabled(false);
				}
			}
		});
	}

	protected void templateCmbChanged(ActionEvent e) {
		if(cmbTemplate.getSelectedItem() != null){
			String temp = cmbTemplate.getSelectedItem().toString();
			if ("comboBoxEdited".equals(e.getActionCommand())) {
				LOG.debug("AddTemplate " + temp);
				cmbTemplate.setEditable(false);
				parent.createTemplate(temp);
				this.adding = false;
			}else if("comboBoxChanged".equals(e.getActionCommand()) && !this.adding){
				LOG.debug("Load Template " + temp);
				Item t = (Item) cmbTemplate.getSelectedItem();
				parent.loadTemplate(t);
			}
		}
	}

	private void setDateEnabled(boolean enable){
		dateTimePicker.setEnabled(enable);
		dateTimePicker.getEditor().setEnabled(enable);
	}
	
	private void setMessageEnabled(boolean enable){
		txtMessage.setEnabled(enable);
		chckbxGoogle.setEnabled(enable);
		chckbxTwitter.setEnabled(enable);
		chckbxFacebook.setEnabled(enable);
	}
	
	protected void saveTemplate() {
		LOG.debug("Saving Template " + cmbTemplate.getSelectedItem());
		Item t = (Item) cmbTemplate.getSelectedItem();
		parent.saveTemplate(t.getId());
	}
	
	protected void deleteTemplate() {
		LOG.debug("Deleting Template" + cmbTemplate.getSelectedItem());
		Item t = (Item) cmbTemplate.getSelectedItem();
		parent.deleteTemplate(t.getId());
	}

	public void addTemplate() {
		this.adding = true;
		cmbTemplate.setEditable(true);
		cmbTemplate.grabFocus();
	}

	public JTextField getTxtStartDir() {
		return txtStartDir;
	}

	public JTextField getTxtEndDir() {
		return txtEndDir;
	}

	public void loadTemplates(){
		for (Entry<Integer,Template> e: TemplateMgr.templates.entrySet()) {
			cmbTemplate.addItem(new Item(e.getKey(),e.getValue()));
		}
	}
	
	public void refreshTemplates(){
		this.adding = true;
		cmbTemplate.removeAllItems();
		loadTemplates();
		this.adding = false;
	}
	
	public void refreshTemplates(String name){
		this.adding = true;
		cmbTemplate.removeAllItems();
		loadTemplates();
		for (int i=0; i<cmbTemplate.getItemCount();i++) {
			if(cmbTemplate.getItemAt(i).getTemplate().name.equals(name)){
				cmbTemplate.setSelectedIndex(i);
			}
		}
		this.adding = false;
	}
	
	public void setLicence(String license){
		for (int i = 0; i < cmbLicense.getItemCount(); i++) {
			if (cmbLicense.getItemAt(i).getData().equals(license)) {
				cmbLicense.setSelectedIndex(i);
			}
		}
	}
	
	public void setVisibility(String visibility, String releaseAt){
		for (int i = 0; i < cmbVisibility.getItemCount(); i++) {
			if (cmbVisibility.getItemAt(i).getData().equals(visibility)) {
				if((releaseAt==null || releaseAt.equals("")) && cmbVisibility.getItemAt(i) == VisibilityType.SCHEDULED){
					continue;
				}
				cmbVisibility.setSelectedIndex(i);
			}
		}
	}
		
	public JComboBox<VisibilityType> getCmbVisibility() {
		return cmbVisibility;
	}

	public DateTimePicker getDateTimePicker() {
		return dateTimePicker;
	}

	public JComboBox<Item> getCmbTemplate() {
		return cmbTemplate;
	}

	public JComboBox<LicenseType> getCmbLicense() {
		return cmbLicense;
	}
	public JTextField getTxtGameTitle() {
		return txtGameTitle;
	}
	public JTextField getTxtThumbnail() {
		return txtThumbnail;
	}
	public DateTimePicker getDateTimePickerStart() {
		return dateTimePickerStart;
	}
	public JCheckBox getChckbxAllowEmbedding() {
		return chckbxAllowEmbedding;
	}
	public JCheckBox getChckbxMakeStatisticsPublic() {
		return chckbxMakeStatisticsPublic;
	}
	public JTextPane getTxtMessage() {
		return txtMessage;
	}
	public JCheckBox getChckbxGoogle() {
		return chckbxGoogle;
	}
	public JCheckBox getChckbxTwitter() {
		return chckbxTwitter;
	}
	public JCheckBox getChckbxFacebook() {
		return chckbxFacebook;
	}
	public JCheckBox getChckbxAllowComments() {
		return chckbxAllowComments;
	}
	public JCheckBox getChckbxAgeRestriction() {
		return chckbxAgeRestriction;
	}
}
