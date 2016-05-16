package at.becast.youploader.util;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import at.becast.youploader.youtube.data.GameDataItem;
import at.becast.youploader.youtube.upload.GameData;

public class AutocompleteComboBox extends JComboBox<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4568236679352605417L;
	private boolean updating = false;
	public AutocompleteComboBox(){
		super();
		setEditable(true);
		Component c = getEditor().getEditorComponent();
		if ( c instanceof JTextComponent ){
			final JTextComponent tc = (JTextComponent)c;
			tc.getDocument().addDocumentListener(new DocumentListener(){
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					System.out.println(arg0);
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					if(tc.getText().length()>=2){
						update();
					}
				}
				
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					if(tc.getText().length()>=2){
						update();
					}
				}

				public void update(){
					if(!updating){
						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run() {
								GameData gd = new GameData(1);
								List<GameDataItem> founds;
								try {
									founds = new ArrayList<GameDataItem>(gd.getGames(tc.getText()));
									Set<String> foundSet = new HashSet<String>();
									for ( GameDataItem s : founds ){
										foundSet.add(s.toString().toLowerCase());
									}
									setEditable(false);
									removeAllItems();
									//if founds contains the search text, then only add once.
									if ( !foundSet.contains( tc.getText().toLowerCase()) ){
										addItem( tc.getText() );
									}							
	
									for (GameDataItem s : founds) {
										addItem(s);
									}
									setEditable(true);
									setPopupVisible(true);
									tc.requestFocus();
									tc.setCaretPosition(tc.getText().length());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}
			});
			
			//When the text component changes, focus is gained 
			//and the menu disappears. To account for this, whenever the focus
			//is gained by the JTextComponent and it has searchable values, we show the popup.
			tc.addFocusListener(new FocusListener(){
				@Override
				public void focusGained(FocusEvent arg0) {
					if ( tc.getText().length() > 0 ){
						setPopupVisible(true);
					}
				}

				@Override
				public void focusLost(FocusEvent arg0) {						
					//Unneeded
				}
			});
		}else{
			throw new IllegalStateException("Editing component is not a JTextComponent!");
		}
	}
	
	public void setUpdating(boolean updating){
		this.updating = updating;
	}
}