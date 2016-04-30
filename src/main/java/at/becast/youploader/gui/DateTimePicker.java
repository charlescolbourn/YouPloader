package at.becast.youploader.gui;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.apache.commons.lang3.time.DateUtils;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.awt.*;

public class DateTimePicker extends JXDatePicker {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2873827187894531697L;
	private JSlider minuteSlider;
	private JSlider hourSlider;
    private JPanel timePanel;
    private JFormattedTextField timeField;
    private DateFormat timeFormat;

    public DateTimePicker() {
        super();
        getMonthView().setSelectionModel(new SingleDaySelectionModel());
        setFormats( DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ) );
        setTimeFormat( DateFormat.getTimeInstance( DateFormat.SHORT ) );
        setTimeSpinners();
    }

    public DateTimePicker( Date d ) {
        this();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 0 );
        calendar.set(Calendar.MINUTE, 0 );
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        setDate(calendar.getTime());
        setTimeSpinners();
        commitTime();
    }

    public void commitEdit() throws ParseException {
        commitTime();
        super.commitEdit();
    }

    public void cancelEdit() {
        super.cancelEdit();
        setTimeSpinners();
    }

    @Override
    public JPanel getLinkPanel() {
        super.getLinkPanel();
        if( timePanel == null ) {
            timePanel = createTimePanel();
        }
        setTimeSpinners();
        return timePanel;
    }

    private JPanel createTimePanel() {
        JPanel newPanel = new JPanel();
        DateFormatter dateModel = new DateFormatter();
    	timeField = new JFormattedTextField(dateModel);
		GridBagConstraints gbc_timeField = new GridBagConstraints();
		gbc_timeField.insets = new Insets(0, 0, 5, 5);
		gbc_timeField.gridx = 1;
		gbc_timeField.gridy = 0;
		gbc_timeField.gridwidth = GridBagConstraints.REMAINDER;
        newPanel.setLayout(new GridBagLayout());
        //newPanel.add(panelOriginal);
		GridBagConstraints gbc_hourSlider = new GridBagConstraints();
		gbc_hourSlider.insets = new Insets(0, 0, 5, 5);
		gbc_hourSlider.gridx = 0;
		gbc_hourSlider.gridy = 2;
		gbc_hourSlider.gridwidth = GridBagConstraints.REMAINDER;
        if( timeFormat == null ) timeFormat = DateFormat.getTimeInstance( DateFormat.SHORT );
        updateTextFieldFormat();
        hourSlider = new JSlider(SwingConstants.HORIZONTAL,0,23,0);
        hourSlider.setSnapToTicks(true);
        hourSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				commitTime();
				Date d = (Date) timeField.getValue();
				if (d != null) {
					timeField.setValue(DateUtils.setHours(d, hourSlider.getValue()));
					firePropertyChange("date", d, timeField.getValue());
					setDate((Date) timeField.getValue());
				}
				
			}
		});
        GridBagConstraints gbc_minuteSlider = new GridBagConstraints();
        gbc_minuteSlider.insets = new Insets(0, 0, 5, 5);
        gbc_minuteSlider.gridx = 0;
		gbc_minuteSlider.gridy = 3;
		gbc_minuteSlider.gridwidth = GridBagConstraints.REMAINDER;
        minuteSlider = new JSlider(SwingConstants.HORIZONTAL,0,59,0);
        minuteSlider.setSnapToTicks(true);
        minuteSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				commitTime();
				Date d = (Date) timeField.getValue();
				if (d != null) {
					timeField.setValue(DateUtils.setMinutes(d, minuteSlider.getValue()));
					firePropertyChange("date", d, timeField.getValue());
					setDate((Date) timeField.getValue());
				}
				
			}
		});
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
        newPanel.add(new JLabel( "Time:" ),gbc_label );
        newPanel.add(timeField,gbc_timeField);
        newPanel.add(hourSlider,gbc_hourSlider);
        newPanel.add(minuteSlider,gbc_minuteSlider);
        newPanel.setBackground(Color.WHITE);
        return newPanel;
    }

    private void updateTextFieldFormat() {
        if( timeField == null ) return;
        JFormattedTextField tf = timeField;
        DefaultFormatterFactory factory = (DefaultFormatterFactory) tf.getFormatterFactory();
        DateFormatter formatter = (DateFormatter) factory.getDefaultFormatter();
        // Change the date format to only show the hours
        formatter.setFormat( timeFormat );
    }

    private void commitTime() {
        Date date = getDate();
        if (date != null) {
            Date time = (Date) timeField.getValue();
            GregorianCalendar timeCalendar = new GregorianCalendar();
            timeCalendar.setTime( time );

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get( Calendar.HOUR_OF_DAY ) );
            calendar.set(Calendar.MINUTE, timeCalendar.get( Calendar.MINUTE ) );
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Date newDate = calendar.getTime();
            setDate(newDate);
        }

    }

    private void setTimeSpinners() {
        Date date = getDate();
        if (date != null) {
        	timeField.setValue( date );
        }
    }

    public DateFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(DateFormat timeFormat) {
        this.timeFormat = timeFormat;
        updateTextFieldFormat();
    }

}