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
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

/**
 *
 * @author Bernhard
 */
public class MonetPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Creates new form editPanel
     */
    public MonetPanel() {
        initComponents();
    }

    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox1.setEnabled(false);

        jCheckBox1.setText("Overlay ads");
        
        JLabel lblNotYetImplemented = new JLabel("Not yet implemented :( Sorry");
        lblNotYetImplemented.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNotYetImplemented.setHorizontalAlignment(SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(jCheckBox1))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(92)
        					.addComponent(lblNotYetImplemented, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(85, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jCheckBox1)
        			.addGap(71)
        			.addComponent(lblNotYetImplemented, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(167, Short.MAX_VALUE))
        );
        this.setLayout(layout);
    }


    private javax.swing.JCheckBox jCheckBox1;
}
