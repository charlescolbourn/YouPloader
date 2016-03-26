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

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.youtube.SyndicationType;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Bernhard
 */
public class MonetPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle LANG = ResourceBundle.getBundle("lang", Locale.getDefault());
	private JCheckBox chckbxMonetize;
	private JComboBox<SyndicationType> cmbContentSyndication;
	private JCheckBox chckbxOverlayads;
	private JCheckBox chckbxSponsoredCards;
	private JCheckBox chckbxSkippableVideoads;

	/**
     * Creates new form editPanel
     */
    public MonetPanel() {
        initComponents();
    }

    private void initComponents() {
    	setLayout(new FormLayout(new ColumnSpec[] {
    			FormSpecs.RELATED_GAP_COLSPEC,
    			ColumnSpec.decode("max(80dlu;default)"),
    			FormSpecs.RELATED_GAP_COLSPEC,
    			ColumnSpec.decode("80dlu"),
    			FormSpecs.RELATED_GAP_COLSPEC,
    			FormSpecs.DEFAULT_COLSPEC,
    			FormSpecs.RELATED_GAP_COLSPEC,
    			ColumnSpec.decode("default:grow"),},
    		new RowSpec[] {
    			FormSpecs.RELATED_GAP_ROWSPEC,
    			FormSpecs.DEFAULT_ROWSPEC,
    			FormSpecs.RELATED_GAP_ROWSPEC,
    			FormSpecs.DEFAULT_ROWSPEC,
    			FormSpecs.RELATED_GAP_ROWSPEC,
    			RowSpec.decode("25px"),
    			FormSpecs.RELATED_GAP_ROWSPEC,
    			RowSpec.decode("25px"),}));
    	

    	
    	chckbxOverlayads = new JCheckBox(LANG.getString("MonetPanel.Overlayads"));
    	chckbxOverlayads.setEnabled(false);
    	add(chckbxOverlayads, "2, 4");
    	
    	chckbxSponsoredCards = new JCheckBox(LANG.getString("MonetPanel.SponsoredCards"));
    	chckbxSponsoredCards.setEnabled(false);
    	add(chckbxSponsoredCards, "4, 4");
    	
    	chckbxSkippableVideoads = new JCheckBox(LANG.getString("MonetPanel.Videoads"));
    	chckbxSkippableVideoads.setEnabled(false);
    	add(chckbxSkippableVideoads, "6, 4");
    	
    	JLabel lblContentSyndication = new JLabel(LANG.getString("MonetPanel.Syndication"));
    	add(lblContentSyndication, "2, 6");
    	
    	cmbContentSyndication = new JComboBox<SyndicationType>();
    	add(cmbContentSyndication, "2, 8, 5, 1, fill, fill");
    	
    	chckbxMonetize = new JCheckBox(LANG.getString("MonetPanel.Monetize"));
    	chckbxMonetize.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
    			if(chckbxMonetize.isSelected()){
    				chckbxOverlayads.setEnabled(true);
    				chckbxSkippableVideoads.setEnabled(true);
    				chckbxSponsoredCards.setEnabled(true);
    			}else{
    				chckbxOverlayads.setEnabled(false);
    				chckbxSkippableVideoads.setEnabled(false);
    				chckbxSponsoredCards.setEnabled(false);
    			}
    		}
    	});
    	add(chckbxMonetize, "2, 2");
    }



	public JCheckBox getChckbxMonetize() {
		return chckbxMonetize;
	}
	public JComboBox<SyndicationType> getCmbContentSyndication() {
		return cmbContentSyndication;
	}
	public JCheckBox getChckbxOverlayads() {
		return chckbxOverlayads;
	}
	public JCheckBox getChckbxSponsoredCards() {
		return chckbxSponsoredCards;
	}
	public JCheckBox getChckbxSkippableVideoads() {
		return chckbxSkippableVideoads;
	}
}
