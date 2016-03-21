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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

import at.becast.youploader.youtube.LicenseType;
import at.becast.youploader.youtube.VisibilityType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import at.becast.youploader.templates.Template;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Bernhard
 */
public class editPanel extends javax.swing.JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Template> cmbTemplate;
    private JLabel jLabel1;
    private JTextField txtStartDir;
    private JTextField txtEndDir;
    private JComboBox<VisibilityType> cmbVisibility;
    private DateTimePicker dateTimePicker;
    private JComboBox<LicenseType> cmbLicense;
    /**
     * Creates new form editPanel
     */
    public editPanel() {
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        cmbTemplate = new JComboBox<Template>();
        cmbTemplate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if("comboBoxEdited".equals(e.getActionCommand())) {
        			cmbTemplate.setEditable(false);
		        }
        	}
        });
        setLayout(new FormLayout(new ColumnSpec[] {
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("87px"),
        		FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
        		ColumnSpec.decode("center:max(140dlu;pref):grow"),
        		ColumnSpec.decode("2dlu"),
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
        		RowSpec.decode("25px"),}));

        jLabel1.setText("Template:");
        add(jLabel1, "2, 2, right, center");
        add(cmbTemplate, "4, 2, fill, fill");
        
        JButton btnNewTemplate = new JButton("");
        btnNewTemplate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		addTemplate();
        	}
        });
        btnNewTemplate.setIcon(new ImageIcon(getClass().getResource("/add.png")));
        add(btnNewTemplate, "6, 2, fill, fill");
        
        JButton btnSaveTemplate = new JButton("");
        btnSaveTemplate.setIcon(new ImageIcon(getClass().getResource("/disk.png")));
        add(btnSaveTemplate, "8, 2, fill, fill");
        
        JButton btnDeleteTemplate = new JButton("");
        btnDeleteTemplate.setIcon(new ImageIcon(getClass().getResource("/cross.png")));
        add(btnDeleteTemplate, "10, 2, fill, fill");
        
        JLabel lblStartDirectory = new JLabel("Start Directory:");
        add(lblStartDirectory, "2, 4, right, center");
        
        txtStartDir = new JTextField();
        add(txtStartDir, "4, 4, fill, fill");
        txtStartDir.setColumns(10);
        
        JButton btnStartDir = new JButton("");
        btnStartDir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser chooser = new JFileChooser();
        		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
     		    int returnVal = chooser.showOpenDialog(getParent());
     		    if(returnVal == JFileChooser.APPROVE_OPTION) {
     		    	txtStartDir.setText(chooser.getSelectedFile().getAbsolutePath().toString());
     		    }
        	}
        });
        btnStartDir.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
        add(btnStartDir, "6, 4, fill, fill");
        
        JLabel lblEndDirectory = new JLabel("End Directory:");
        add(lblEndDirectory, "2, 6, right, center");
        
        txtEndDir = new JTextField();
        add(txtEndDir, "4, 6, fill, fill");
        txtEndDir.setColumns(10);
        
        JButton btnEndDir = new JButton("");
        btnEndDir.setIcon(new ImageIcon(getClass().getResource("/folder.png")));
        add(btnEndDir, "6, 6, fill, fill");
        
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
        		if(cmbVisibility.getSelectedItem() == VisibilityType.SCHEDULED){
        			dateTimePicker.setEnabled(true);
        			dateTimePicker.getEditor().setEnabled(true);
        		}else{
        			dateTimePicker.setEnabled(false);
        			dateTimePicker.getEditor().setEnabled(false);
        		}
        	}
        });
        cmbVisibility.setModel(new DefaultComboBoxModel<VisibilityType>(VisibilityType.values()));
        add(cmbVisibility, "4, 8, fill, fill");
        
        JLabel lblReleaseAt = new JLabel("Release at:");
        add(lblReleaseAt, "2, 10, right, default");
        
        add(dateTimePicker, "4, 10, fill, fill");
        
        JLabel lblLicense = new JLabel("License:");
        add(lblLicense, "2, 12, right, default");
        
        cmbLicense = new JComboBox<LicenseType>();
        cmbLicense.setModel(new DefaultComboBoxModel<LicenseType>(LicenseType.values()));
        add(cmbLicense, "4, 12, fill, fill");
    }
    
    public void addTemplate(){
    	cmbTemplate.setEditable(true);
    	cmbTemplate.grabFocus();
    }
    
	public JTextField getTxtStartDir() {
		return txtStartDir;
	}
	public JTextField getTxtEndDir() {
		return txtEndDir;
	}
	public JComboBox<VisibilityType> getCmbVisibility() {
		return cmbVisibility;
	}
	public DateTimePicker getDateTimePicker() {
		return dateTimePicker;
	}
	public JComboBox<Template> getCmbTemplate() {
		return cmbTemplate;
	}
	public JComboBox<LicenseType> getCmbLicense() {
		return cmbLicense;
	}
}
