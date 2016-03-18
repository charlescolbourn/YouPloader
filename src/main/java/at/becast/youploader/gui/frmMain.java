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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.gui.slider.SideBar;
import at.becast.youploader.gui.slider.SidebarSection;
import at.becast.youploader.settings.Settings;
import at.becast.youploader.youtube.Categories;
import at.becast.youploader.youtube.data.CategoryType;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.exceptions.UploadException;
import at.becast.youploader.youtube.io.UploadManager;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSlider;

/**
 *
 * @author genuineparts
 */

public class frmMain extends javax.swing.JFrame implements IMainMenu{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6965358827253585528L;
	public static final String DB_FILE = "data/data.db";
	public static final String VERSION = "0.2";
	public static UploadManager UploadManager = new UploadManager();
	public Settings s = Settings.getInstance();
	public AccountManager accMng =  AccountManager.getInstance();
	private ModalDialog modal; 
	private IMainMenu self;
	private JTextArea txtDescription;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JButton jButton1;
    private JComboBox<String> cmbFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel mainTab;
    private javax.swing.JMenu mnuAcc;
    private javax.swing.JMenuBar mnuBar;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuQuit;
    private JTextField txtTitle;
    private JMenuItem mntmAddAccount;
    private JLabel lblTagslenght;
    private JLabel lblDesclenght;
    private JLabel lbltitlelenght;
    private JMenu menu;
    private JMenuItem mntmAbout;
    private JScrollPane TabQueues;
    private JPanel panel_1;
    private JSpinner spinner;
    private JLabel lblUploadSpeed;
    private JPanel TabQueue;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel QueuePanel;
    private JComboBox<CategoryType> cmbCategory;
    private JMenuItem mntmDonate;
    private JTextArea txtTags;
    private JScrollPane scrollPane_2;
    private JComboBox<String> cmbAccount;
	public transient static HashMap<Integer, JMenuItem> _accounts = new HashMap<Integer, JMenuItem>();
	private JSlider slider;
	private JLabel lblUploads;
	
	/**
     * Creates new form frmMain
     */
    public frmMain() {
    	self = this;
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        initComponents();
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
        this.setLocationRelativeTo( null );
    }


    /**
     * 
     */
    public void initComponents() {

        TabbedPane = new javax.swing.JTabbedPane();
        mainTab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbFile = new JComboBox<String>();
        jButton1 = new javax.swing.JButton();

        SideBar sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 300, true);
        SidebarSection ss1 = new SidebarSection(sideBar, "Template", new editPanel(),null);
        SidebarSection ss2 = new SidebarSection(sideBar, "Monetisation", new MonetPanel(),null);
        sideBar.addSection(ss1,false);
        sideBar.addSection(ss2);
        jButton1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 JFileChooser chooser = new JFileChooser();
        		 editPanel edit = (editPanel)ss1.contentPane;
        		 if(edit.getTxtStartDir() != null && !edit.getTxtStartDir().equals("")){
        			 chooser.setCurrentDirectory(new File(edit.getTxtStartDir().getText()));
        		 }
        		    int returnVal = chooser.showOpenDialog((Component) self);
        		    if(returnVal == JFileChooser.APPROVE_OPTION) {
        		    	cmbFile.removeAllItems();
        		    	cmbFile.addItem(chooser.getSelectedFile().getAbsolutePath().toString());
        		    }
        	}
        });
        mnuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuQuit = new javax.swing.JMenuItem();
        mnuAcc = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("YouPloader "+VERSION);
        setIconImages(null);
        setName("frmMain");

        jLabel1.setText("Select Video File");

        jButton1.setText("jButton1");
        
        JLabel lblTitle = new JLabel("Title");
        
        txtTitle = new JTextField();
        txtTitle.setColumns(10);

        
        JLabel lblDescription = new JLabel("Description");
        
        JLabel lblTags = new JLabel("Tags");
        
        txtTags = new JTextArea();
        txtTags.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txtTags.setWrapStyleWord(true);
        txtTags.setLineWrap(true);
        txtTags.setBorder(BorderFactory.createEtchedBorder());
        
        lblTagslenght = new JLabel("(0/500)");
        txtTags.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		if(txtTags.getText().length()>450){
        			lblTagslenght.setForeground(Color.RED);
        		}else{
        			lblTagslenght.setForeground(Color.BLACK);
                }
                if(txtTags.getText().length()>=501){
                	txtTags.setText(txtTags.getText().substring(0, 500));

                }
                lblTagslenght.setText("("+txtTags.getText().length()+"/500)");
        	}
        });
        cmbAccount = new JComboBox<String>();
        
        JLabel lblAccount = new JLabel("Account");
        
        JButton btnAddToQueue = new JButton("Add to Queue");
        btnAddToQueue.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(cmbFile.getSelectedItem()!=null && !cmbFile.getSelectedItem().toString().equals("")){
        			try {
						create_upload(cmbFile.getSelectedItem().toString(), txtTitle.getText(), cmbAccount.getSelectedItem().toString());
					} catch (IOException | UploadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}else{
        			JOptionPane.showMessageDialog(null,"You have to select a file.","Give me something to work with!", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });
        
        JButton btnReset = new JButton("Reset");
        
        JLabel lblCategory = new JLabel("Category");
        
        cmbCategory = new JComboBox<CategoryType>();
        cmbCategory.setModel(new DefaultComboBoxModel<CategoryType>());
        for(Categories cat: Categories.values()){
        	 cmbCategory.addItem(new CategoryType(cat.getID(),cat.toString()));
        }
        
        lblDesclenght = new JLabel("(0/1000)");
        
        lbltitlelenght = new JLabel("(0/100)");
        txtTitle.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		if(txtTitle.getText().length()>90){
                	lbltitlelenght.setForeground(Color.RED);
        		}else{
                	lbltitlelenght.setForeground(Color.BLACK);
                }
                if(txtTitle.getText().length()>=101){
                	txtTitle.setText(txtTitle.getText().substring(0, 100));

                }
                lbltitlelenght.setText("("+txtTitle.getText().length()+"/100)");
        	}
        });
        
        scrollPane_2 = new JScrollPane();
        javax.swing.GroupLayout mainTabLayout = new javax.swing.GroupLayout(mainTab);
        mainTabLayout.setHorizontalGroup(
        	mainTabLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(mainTabLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, mainTabLayout.createSequentialGroup()
        					.addGroup(mainTabLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
        						.addGroup(mainTabLayout.createSequentialGroup()
        							.addComponent(lblTitle)
        							.addPreferredGap(ComponentPlacement.RELATED, 353, Short.MAX_VALUE)
        							.addComponent(lbltitlelenght))
        						.addGroup(mainTabLayout.createSequentialGroup()
        							.addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
        							.addComponent(lblDesclenght))
        						.addComponent(cmbAccount, 0, 409, Short.MAX_VALUE)
        						.addGroup(mainTabLayout.createSequentialGroup()
        							.addComponent(lblTags, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 315, Short.MAX_VALUE)
        							.addComponent(lblTagslenght))
        						.addComponent(cmbCategory, 0, 409, Short.MAX_VALUE)
        						.addGroup(mainTabLayout.createSequentialGroup()
        							.addComponent(btnAddToQueue, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(btnReset, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
        						.addComponent(txtTitle, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
        						.addComponent(jLabel1)
        						.addComponent(cmbFile, 0, 409, Short.MAX_VALUE)
        						.addComponent(txtTags, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        					.addGap(2))
        				.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblAccount))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
        			.addContainerGap())
        );
        mainTabLayout.setVerticalGroup(
        	mainTabLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(mainTabLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jLabel1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(cmbFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jButton1))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblTitle)
        				.addComponent(lbltitlelenght))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCategory)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(cmbCategory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblDescription)
        				.addComponent(lblDesclenght))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblTags)
        				.addComponent(lblTagslenght))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(txtTags, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblAccount)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(cmbAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(10)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnAddToQueue)
        				.addComponent(btnReset))
        			.addContainerGap())
        		.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );
        
        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txtDescription.setWrapStyleWord(true);
        txtDescription.setLineWrap(true);
        scrollPane_2.setViewportView(txtDescription);
        mainTab.setLayout(mainTabLayout);
        txtDescription.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		if(txtDescription.getText().length()>900){
        			lblDesclenght.setForeground(Color.RED);
        		}else{
        			lblDesclenght.setForeground(Color.BLACK);
                }
                if(txtDescription.getText().length()>=1001){
                	txtDescription.setText(txtDescription.getText().substring(0, 1000));

                }
                lblDesclenght.setText("("+txtDescription.getText().length()+"/1000)");
        	}
        });
        TabbedPane.addTab("Video Settings", mainTab);

        mnuFile.setText("File");

        mnuQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mnuQuit.setText("Quit");
        mnuQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuQuitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuQuit);

        mnuBar.add(mnuFile);

        mnuAcc.setText("Account");
        mnuBar.add(mnuAcc);
        
        JSeparator separator = new JSeparator();
        mntmAddAccount = new JMenuItem("Add Account");
        mntmAddAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	mntmAddAccountActionPerformed(evt);
            }
        });
        
        mnuAcc.add(mntmAddAccount);
        mnuAcc.add(separator);
        

       

        setJMenuBar(mnuBar);
        
        menu = new JMenu("?");
        mnuBar.add(menu);
        
        mntmDonate = new JMenuItem("Donate");
        menu.add(mntmDonate);
        mntmDonate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (Desktop.isDesktopSupported()) {
      		      try {
      		        Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ42BHSUTGPT6"));
      		      } catch (IOException | URISyntaxException e1) { /* TODO: error handling */ }
      		    } else { /* TODO: error handling */ }
            }
        });
        
        mntmAbout = new JMenuItem("About");
        menu.add(mntmAbout);
        //https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ42BHSUTGPT6

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(TabbedPane)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(TabbedPane, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
        );
        
        TabQueues = new JScrollPane();
        QueuePanel = new JPanel();
        TabQueues.setViewportView(QueuePanel);
        getContentPane().setLayout(layout);
        QueuePanel.setLayout(new MigLayout("", "[875px,grow,fill]", "[][][][]"));
        /*UploadItem i = new UploadItem();
        panel.add(i, new CC().wrap());
        panel.revalidate();
        UploadItem s = new UploadItem();
        panel.add(s, new CC().wrap());
        panel.revalidate();
        UploadItem r = new UploadItem();
        panel.add(r, new CC().wrap());
        panel.revalidate();
        UploadItem f = new UploadItem();
        panel.add(f, new CC().wrap());*/
        
        /*GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;*/
        
        JPanel panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 1;
        panel_2.setLayout(new FormLayout(new ColumnSpec[] {
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("default:grow"),
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("max(39dlu;default)"),
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("max(92dlu;default)"),
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("max(46dlu;min)"),
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("left:max(22dlu;default)"),
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,},
        	new RowSpec[] {
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		FormSpecs.DEFAULT_ROWSPEC,
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		FormSpecs.DEFAULT_ROWSPEC,
        		FormSpecs.RELATED_GAP_ROWSPEC,
        		RowSpec.decode("max(0dlu;default)"),}));
        
        btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		UploadManager.start();
        	}
        });
        panel_2.add(btnStart, "2, 4");
        
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		UploadManager.stop();
        	}
        });
        panel_2.add(btnStop, "6, 4");
        
        lblUploads = new JLabel("Uploads:");
        panel_2.add(lblUploads, "18, 4, right, fill");
        
        slider = new JSlider();
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setMinimum(1);
        slider.setMaximum(5);
        slider.setValue(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel_2.add(slider, "20, 4, fill, fill");
        
        lblUploadSpeed = new JLabel("Upload Speed:");
        lblUploadSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
        panel_2.add(lblUploadSpeed, "24, 4");
        
        spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
        spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				UploadManager.set_limit(Integer.parseInt(s.getValue().toString()));			
			}
        });
        panel_2.add(spinner, "26, 4");
        
        JLabel lblKbps = new JLabel("kbps");
        panel_2.add(lblKbps, "28, 4");
        
        TabQueue = new JPanel();
        TabbedPane.addTab("Queue", null, TabQueue, null);
        TabQueue.setLayout(new BorderLayout(0, 0));
        
        TabQueue.add(panel_2, BorderLayout.SOUTH);
        
        TabQueue.add(TabQueues, BorderLayout.CENTER);
        
        panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        QueuePanel.revalidate();
        load_accounts();
        pack();
        ss1.expand();
    }

    private void mnuQuitActionPerformed(ActionEvent evt) {
        System.exit(0);
    }

	private void mntmAddAccountActionPerformed(ActionEvent evt) {
		AddAccount acc = new AddAccount(this);
		acc.setVisible(true);
		
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMain().setVisible(true);
            }
        });
    }

	public void prep_modal(Account Account,String code) {
		modal=new ModalDialog((Frame)this, Account, code);
	}
    public void show_modal(){
    	modal.setVisible(true);
    }
    public void close_modal(){
    	modal.success();
    }
    
    public void load_accounts(){
    	int i = 0;
    	HashMap<String, Integer> accounts =  accMng.load();
    	for(Entry<String, Integer> entry : accounts.entrySet()){
    		cmbAccount.addItem(entry.getKey());
    		JRadioButtonMenuItem rdoBtn = new JRadioButtonMenuItem(entry.getKey());
    		if(entry.getValue()==1){
    			rdoBtn.setSelected(true);
    			cmbAccount.setSelectedItem(entry.getKey());
    		}
    		rdoBtn.setActionCommand(entry.getKey());
    		rdoBtn.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					accMng.change_user(e.getActionCommand());
				}					
			});
			_accounts.put(i, rdoBtn);
            mnuAcc.add(rdoBtn);
            i++;
    	}
        
    }
    
    /**
     * @param String File
     * @param String Name
     * @param String Account
     * @return UploadItem
     * @throws IOException
     * @throws UploadException
     */
    public UploadItem create_upload(String File, String Name, String Account) throws IOException, UploadException{
        UploadItem f = new UploadItem();
        f.getlblName().setText(Name);
        this.getQueuePanel().add(f, new CC().wrap());
        this.getQueuePanel().revalidate();
        Video v = new Video();
        v.snippet.title=Name;
        CategoryType cat = (CategoryType) cmbCategory.getSelectedItem();
        v.snippet.categoryId = cat.getValue();
        v.snippet.description = txtDescription.getText();
        if(txtTags != null && !txtTags.getText().equals("")){
        	String[] tags = txtTags.getText().trim().split(",");
        	v.snippet.tags = tags;
        }
		File data = new File(File);
		UploadManager.add_upload(f, data, v, Account); 
        return f;
    }
    
	private JPanel getQueuePanel() {
		return QueuePanel;
	}
	
	public void refresh_accounts(){
		int s= _accounts.size();
		for(int i=0;i<s;i++){
			mnuAcc.remove(_accounts.get(i));
			_accounts.remove(i);
		}
		load_accounts();
	}

	public JTextArea getTxtTags() {
		return txtTags;
	}
	public JTextArea getTxtDescription() {
		return txtDescription;
	}
}
