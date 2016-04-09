package at.becast.youploader.gui.spinner;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import at.becast.youploader.util.UTF8ResourceBundle;

public class SpeedValuesSpinnerEditor extends JPanel implements PropertyChangeListener, LayoutManager, ChangeListener{

	/**
	 * Spinner.Unlimited
	 */
	private static final ResourceBundle LANG = UTF8ResourceBundle.getBundle("lang", Locale.getDefault());
	private static final long serialVersionUID = -5739544907889014244L;
	private JSpinner spinner;
	public SpeedValuesSpinnerEditor(JSpinner spinner) {
		super();
		this.spinner = spinner;
		JTextField ftf = new JTextField();
        ftf.setName("Spinner.TextField");
		int numb = (int) this.spinner.getValue();
		if(numb==0){
			ftf.setText(LANG.getString("Spinner.Unlimited"));
		}else{
			ftf.setText(String.valueOf(numb));
		}
        ftf.addPropertyChangeListener(this);
        ftf.setEditable(false);
        ftf.setInheritsPopupMenu(true);

        String toolTipText = spinner.getToolTipText();
        if (toolTipText != null) {
            ftf.setToolTipText(toolTipText);
        }

        add(ftf);

        setLayout(this);
        spinner.addChangeListener(this);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner)(e.getSource());
        int numb = (int) spinner.getValue();
        if(numb==0){
        	getTextField().setText(LANG.getString("Spinner.Unlimited"));
		}else{
			getTextField().setText(String.valueOf(numb));
		}
    }

    public void dismiss(JSpinner spinner) {
        spinner.removeChangeListener(this);
    }
    
    public JSpinner getSpinner() {
        for (Component c = this; c != null; c = c.getParent()) {
            if (c instanceof JSpinner) {
                return (JSpinner)c;
            }
        }
        return null;
    }
    
    public JTextField getTextField() {
        return (JTextField)getComponent(0);
    }

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// Nothing to do
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// Nothing to do
		
	}
	
    private Dimension insetSize(Container parent) {
        Insets insets = parent.getInsets();
        int w = insets.left + insets.right;
        int h = insets.top + insets.bottom;
        return new Dimension(w, h);
    }

	@Override
	public Dimension preferredLayoutSize(Container parent) {
        Dimension preferredSize = insetSize(parent);
        if (parent.getComponentCount() > 0) {
            Dimension childSize = getComponent(0).getPreferredSize();
            preferredSize.width += childSize.width;
            preferredSize.height += childSize.height;
        }
        return preferredSize;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
        Dimension minimumSize = insetSize(parent);
        if (parent.getComponentCount() > 0) {
            Dimension childSize = getComponent(0).getMinimumSize();
            minimumSize.width += childSize.width;
            minimumSize.height += childSize.height;
        }
        return minimumSize;
	}

	@Override
	public void layoutContainer(Container parent) {
        if (parent.getComponentCount() > 0) {
            Insets insets = parent.getInsets();
            int w = parent.getWidth() - (insets.left + insets.right);
            int h = parent.getHeight() - (insets.top + insets.bottom);
            getComponent(0).setBounds(insets.left, insets.top, w, h);
        }		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		JSpinner spinner = getSpinner();

        if (spinner == null) {
            // Indicates we aren't installed anywhere.
            return;
        }

        Object source = evt.getSource();
        String name = evt.getPropertyName();
        if ((source instanceof JTextField) && "value".equals(name)) {
            int lastValue = (int) spinner.getValue();

            // Try to set the new value
            try {
            	String value = getTextField().getText();
            	if(value.equals(LANG.getString("Spinner.Unlimited"))){
            		spinner.setValue(0);
            	}else{
            		spinner.setValue(Integer.parseInt(value));
            	}
            } catch (IllegalArgumentException iae) {
                // SpinnerModel didn't like new value, reset
                try {
                	if(lastValue==0){
                		((JTextField)source).setText(LANG.getString("Spinner.Unlimited"));
                	}else{
                		((JTextField)source).setText(String.valueOf(lastValue));
                	}
                } catch (IllegalArgumentException iae2) {
                    // Still bogus, nothing else we can do, the
                    // SpinnerModel and JFormattedTextField are now out
                    // of sync.
                }
            }
        }
		
	}
}
