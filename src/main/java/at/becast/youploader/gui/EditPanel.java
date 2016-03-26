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
import java.util.Calendar;
import java.util.Date;
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
import at.becast.youploader.youtube.LicenseType;
import at.becast.youploader.youtube.VisibilityType;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

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
	private JComboBox<Item> cmbTemplate;
	private JLabel jLabel1;
	private JTextField txtStartDir;
	private JTextField txtEndDir;
	private frmMain parent;
	private Boolean adding = false;
	private JComboBox<VisibilityType> cmbVisibility;
	private DateTimePicker dateTimePicker;
	private JComboBox<LicenseType> cmbLicense;
	private TemplateManager TemplateMgr = TemplateManager.getInstance();
	private JTextField txtThumbnail;
	private JTextField txtGameTitle;

	/**
	 * Creates new form editPanel
	 */
	public EditPanel(frmMain parent) {
		this.parent = parent;
		initComponents();
	}

	private void initComponents() {
		jLabel1 = new JLabel();
		cmbTemplate = new JComboBox<Item>();
		load_templates();
		cmbTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TemplateCmbChanged(e);
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
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));

		jLabel1.setText("Template:");
		add(jLabel1, "2, 2, right, center");
		add(cmbTemplate, "4, 2, 5, 1, fill, fill");

		JButton btnNewTemplate = new JButton("");
		btnNewTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTemplate();
			}
		});
		btnNewTemplate.setIcon(new ImageIcon(getClass().getResource("/add.png")));
		add(btnNewTemplate, "10, 2, fill, fill");

		JButton btnSaveTemplate = new JButton("");
		btnSaveTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveTemplate();
			}
		});
		btnSaveTemplate.setIcon(new ImageIcon(getClass().getResource("/disk.png")));
		add(btnSaveTemplate, "12, 2, fill, fill");

		JButton btnDeleteTemplate = new JButton("");
		btnDeleteTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteTemplate();
			}
		});
		btnDeleteTemplate.setIcon(new ImageIcon(getClass().getResource("/cross.png")));
		add(btnDeleteTemplate, "14, 2, fill, fill");

		JLabel lblStartDirectory = new JLabel("Start Directory:");
		add(lblStartDirectory, "2, 4, right, center");

		txtStartDir = new JTextField();
		add(txtStartDir, "4, 4, 5, 1, fill, fill");
		txtStartDir.setColumns(10);

		JButton btnStartDir = new JButton("");
		btnStartDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtStartDir.setText(chooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		btnStartDir.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
		add(btnStartDir, "10, 4, fill, fill");

		JLabel lblEndDirectory = new JLabel("End Directory:");
		add(lblEndDirectory, "2, 6, right, center");

		txtEndDir = new JTextField();
		add(txtEndDir, "4, 6, 5, 1, fill, fill");
		txtEndDir.setColumns(10);

		JButton btnEndDir = new JButton("");
		btnEndDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					txtEndDir.setText(chooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		btnEndDir.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
		add(btnEndDir, "10, 6, fill, fill");

		JLabel lblVisibility = new JLabel("Visibility:");
		add(lblVisibility, "2, 8, right, default");

		dateTimePicker = new DateTimePicker();
		dateTimePicker.setEnabled(false);
		dateTimePicker.getEditor().setEnabled(false);
		Calendar calendar = dateTimePicker.getMonthView().getCalendar();
		// starting today if we are in a hurry
		calendar.setTime(new Date());
		dateTimePicker.getMonthView().setLowerBound(calendar.getTime());

		cmbVisibility = new JComboBox<VisibilityType>();
		cmbVisibility.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if (cmbVisibility.getSelectedItem() == VisibilityType.SCHEDULED) {
					dateTimePicker.setEnabled(true);
					dateTimePicker.getEditor().setEnabled(true);
				} else {
					dateTimePicker.setEnabled(false);
					dateTimePicker.getEditor().setEnabled(false);
				}
			}
		});
		cmbVisibility.setModel(new DefaultComboBoxModel<VisibilityType>(VisibilityType.values()));
		add(cmbVisibility, "4, 8, 5, 1, fill, fill");

		JLabel lblReleaseAt = new JLabel("Release at:");
		add(lblReleaseAt, "2, 10, right, default");

		add(dateTimePicker, "4, 10, 5, 1, fill, fill");

		JLabel lblLicense = new JLabel("License:");
		add(lblLicense, "2, 12, right, default");

		cmbLicense = new JComboBox<LicenseType>();
		cmbLicense.setModel(new DefaultComboBoxModel<LicenseType>(LicenseType.values()));
		add(cmbLicense, "4, 12, 5, 1, fill, fill");
		
		JLabel lblThumbnail = new JLabel("Thumbnail:");
		lblThumbnail.setEnabled(false);
		add(lblThumbnail, "2, 14, right, default");
		
		txtThumbnail = new JTextField();
		txtThumbnail.setEnabled(false);
		txtThumbnail.setEditable(false);
		add(txtThumbnail, "4, 14, 5, 1, fill, fill");
		txtThumbnail.setColumns(10);
		
		JButton btnThumbnail = new JButton("");
		btnThumbnail.setEnabled(false);
		btnThumbnail.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
		add(btnThumbnail, "10, 14, fill, fill");
		
		JLabel lblStartUploadAt = new JLabel("Start upload at:");
		lblStartUploadAt.setEnabled(false);
		add(lblStartUploadAt, "2, 16, right, default");
		
		DateTimePicker dateTimePickerStart = new DateTimePicker();
		dateTimePickerStart.setEnabled(false);
		dateTimePickerStart.setEditable(false);
		add(dateTimePickerStart, "4, 16, 5, 1, fill, fill");
		
		JLabel lblGameTitle = new JLabel("Game title:");
		lblGameTitle.setEnabled(false);
		add(lblGameTitle, "2, 18, right, default");
		
		txtGameTitle = new JTextField();
		txtGameTitle.setEditable(false);
		txtGameTitle.setEnabled(false);
		add(txtGameTitle, "4, 18, 5, 1, fill, fill");
		txtGameTitle.setColumns(10);
		
		JLabel lblAdditionalSettings = new JLabel("Other Settings:");
		add(lblAdditionalSettings, "2, 20, right, default");
		
		JCheckBox chckbxAllowEmbedding = new JCheckBox("Allow embedding");
		add(chckbxAllowEmbedding, "4, 20, 3, 1");
		
		JCheckBox chckbxAgeRestriction = new JCheckBox("Age restriction");
		add(chckbxAgeRestriction, "8, 20, 3, 1, left, default");
		
		JCheckBox chckbxMakeStatisticsPublic = new JCheckBox("Make statistics publicly visible");
		chckbxMakeStatisticsPublic.setVerticalAlignment(SwingConstants.TOP);
		add(chckbxMakeStatisticsPublic, "4, 22, 3, 1, left, default");
		
		JCheckBox chckbxAllowComments = new JCheckBox("Allow comments");
		add(chckbxAllowComments, "8, 22, 3, 1, left, default");
	}

	protected void TemplateCmbChanged(ActionEvent e) {
		if(cmbTemplate.getSelectedItem() != null){
			String temp = cmbTemplate.getSelectedItem().toString();
			if ("comboBoxEdited".equals(e.getActionCommand())) {
				LOG.debug("AddTemplate " + temp);
				cmbTemplate.setEditable(false);
				parent.createTemplate(temp);
				this.adding = false;
			}else if("comboBoxChanged".equals(e.getActionCommand())){
				if(!this.adding){
					LOG.debug("Load Template " + temp);
					Item t = (Item) cmbTemplate.getSelectedItem();
					parent.loadTemplate(t);
				}
			}
		}
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

	public void load_templates(){
		for (Entry<Integer,Template> e: TemplateMgr.templates.entrySet()) {
			cmbTemplate.addItem(new Item(e.getKey(),e.getValue()));
		}
	}
	
	public void refresh_templates(){
		this.adding = true;
		cmbTemplate.removeAllItems();
		load_templates();
		this.adding = false;
	}
	
	public void refresh_templates(String name){
		this.adding = true;
		cmbTemplate.removeAllItems();
		load_templates();
		for (int i=0; i<cmbTemplate.getItemCount();i++) {
			if(cmbTemplate.getItemAt(i).getTemplate().name.equals(name)){
				cmbTemplate.setSelectedIndex(i);
			}
		}
		this.adding = false;
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
}
