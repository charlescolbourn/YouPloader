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
import net.miginfocom.swing.MigLayout;

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
import java.awt.Component;
import java.awt.event.ActionEvent;

/**
 *
 * @author Bernhard
 */
public class editPanel extends javax.swing.JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox jComboBox1;
    private JLabel jLabel1;
    private JTextField txtStartDir;
    private JTextField txtEndDir;
    /**
     * Creates new form editPanel
     */
    public editPanel() {
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        jComboBox1 = new JComboBox();
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
        		RowSpec.decode("25px"),}));

        jLabel1.setText("Template:");
        add(jLabel1, "2, 2, right, center");
        add(jComboBox1, "4, 2, fill, fill");
        
        JButton btnNewTemplate = new JButton("");
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
    }
	public JTextField getTxtStartDir() {
		return txtStartDir;
	}
	public JTextField getTxtEndDir() {
		return txtEndDir;
	}
}
