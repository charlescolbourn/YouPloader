/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.becast.youploader.gui;

import at.becast.youploader.account.AccountManager;
import at.becast.youploader.gui.slider.SideBar;
import at.becast.youploader.gui.slider.SidebarSection;
import at.becast.youploader.settings.Settings;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextArea;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import at.becast.youploader.youtube.Categories;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSpinner;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Bernhard
 */
public class frmMain extends javax.swing.JFrame implements IMainMenu{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DB_FILE = "data/data.db";
	public static final String VERSION = "0.1";
	public Settings s = Settings.getInstance();
	public AccountManager accMng =  AccountManager.getInstance();
	private ModalDialog modal; 
	private IMainMenu self;
	public transient static HashMap<Integer, JMenuItem> _accounts = new HashMap<Integer, JMenuItem>();
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
    }


    public void initComponents() {

        TabbedPane = new javax.swing.JTabbedPane();
        mainTab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbFile = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        SideBar sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 300, true);
        SidebarSection ss1 = new SidebarSection(sideBar, "Template", new editPanel(),null);
        SidebarSection ss2 = new SidebarSection(sideBar, "Monetisation", new MonetPanel(),null);
        sideBar.addSection(ss1,false);
        sideBar.addSection(ss2);
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
        
        txtDescription = new JTextArea();
        txtDescription.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel lblTags = new JLabel("Tags");
        
        JTextPane txtTags = new JTextPane();
        txtTags.setBorder(BorderFactory.createEtchedBorder());
        
        label = new JLabel("(0/500)");
        
        JComboBox cmbAccount = new JComboBox();
        
        JLabel lblAccount = new JLabel("Account");
        
        JButton btnAddToQueue = new JButton("Add to Queue");
        
        JButton btnReset = new JButton("Reset");
        
        JLabel lblCategory = new JLabel("Category");
        
        JComboBox cmbCategory = new JComboBox();
        cmbCategory.setModel(new DefaultComboBoxModel(Categories.values()));
        
        label_1 = new JLabel("(0/1000)");
        
        label_2 = new JLabel("(0/100)");
        javax.swing.GroupLayout mainTabLayout = new javax.swing.GroupLayout(mainTab);
        mainTabLayout.setHorizontalGroup(
        	mainTabLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(mainTabLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblAccount)
        				.addGroup(Alignment.TRAILING, mainTabLayout.createSequentialGroup()
        					.addGroup(mainTabLayout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(Alignment.LEADING, mainTabLayout.createSequentialGroup()
        							.addComponent(lblTitle)
        							.addPreferredGap(ComponentPlacement.RELATED, 330, Short.MAX_VALUE)
        							.addComponent(label_2))
        						.addGroup(Alignment.LEADING, mainTabLayout.createSequentialGroup()
        							.addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 271, Short.MAX_VALUE)
        							.addComponent(label_1))
        						.addComponent(cmbAccount, Alignment.LEADING, 0, 396, Short.MAX_VALUE)
        						.addGroup(Alignment.LEADING, mainTabLayout.createSequentialGroup()
        							.addComponent(lblTags, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 302, Short.MAX_VALUE)
        							.addComponent(label))
        						.addComponent(txtDescription, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        						.addComponent(cmbCategory, Alignment.LEADING, 0, 396, Short.MAX_VALUE)
        						.addGroup(Alignment.LEADING, mainTabLayout.createSequentialGroup()
        							.addComponent(btnAddToQueue, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(btnReset, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
        						.addComponent(txtTitle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        						.addComponent(jLabel1, Alignment.LEADING)
        						.addComponent(cmbFile, Alignment.LEADING, 0, 396, Short.MAX_VALUE)
        						.addComponent(txtTags, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        					.addGap(2)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
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
        				.addComponent(label_2))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblCategory)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(cmbCategory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblDescription)
        				.addComponent(label_1))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(txtDescription, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(mainTabLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblTags)
        				.addComponent(label))
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
        		.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );
        mainTab.setLayout(mainTabLayout);

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
        
        mntmAbout = new JMenuItem("About");
        menu.add(mntmAbout);

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
        JPanel panel = new JPanel();
        TabQueues.setViewportView(panel);
        getContentPane().setLayout(layout);
        panel.setLayout(new MigLayout("", "[875px,grow,fill]", "[][][][]"));
        UploadItem i = new UploadItem();
        panel.add(i, new CC().wrap());
        panel.revalidate();
        UploadItem s = new UploadItem();
        panel.add(s, new CC().wrap());
        panel.revalidate();
        UploadItem r = new UploadItem();
        panel.add(r, new CC().wrap());
        panel.revalidate();
        UploadItem f = new UploadItem();
        panel.add(f, new CC().wrap());
        
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        
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
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("default:grow"),
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		FormSpecs.DEFAULT_COLSPEC,
        		FormSpecs.RELATED_GAP_COLSPEC,
        		ColumnSpec.decode("default:grow"),
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
        panel_2.add(btnStart, "2, 4");
        
        btnStop = new JButton("Stop");
        panel_2.add(btnStop, "6, 4");
        
        lblUploadSpeed = new JLabel("Upload Speed:");
        lblUploadSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
        panel_2.add(lblUploadSpeed, "48, 4");
        
        spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
        panel_2.add(spinner, "50, 4");
        
        JLabel lblKbps = new JLabel("kbps");
        panel_2.add(lblKbps, "52, 4");
        
        TabQueue = new JPanel();
        TabbedPane.addTab("Queue", null, TabQueue, null);
        TabQueue.setLayout(new BorderLayout(0, 0));
        
        TabQueue.add(panel_2, BorderLayout.SOUTH);
        
        TabQueue.add(TabQueues, BorderLayout.CENTER);
        
        panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel.revalidate();
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

	public void prep_modal(String Account,String code) {
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
    		JRadioButtonMenuItem rdoBtn = new JRadioButtonMenuItem(entry.getKey());
    		if(entry.getValue()==1){
    			rdoBtn.setSelected(true);
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
    
	public void refresh_accounts(){
		int s= _accounts.size();
		for(int i=0;i<s;i++){
			mnuAcc.remove(_accounts.get(i));
			_accounts.remove(i);
		}
		load_accounts();
	}

    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox cmbFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel mainTab;
    private javax.swing.JMenu mnuAcc;
    private javax.swing.JMenuBar mnuBar;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuQuit;
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JMenuItem mntmAddAccount;
    private JRadioButtonMenuItem rdbtnmntmNewRadioItem;
    private JLabel label;
    private JLabel label_1;
    private JLabel label_2;
    private JMenu menu;
    private JMenuItem mntmAbout;
    private JScrollPane TabQueues;
    private JPanel panel_1;
    private JScrollPane scrollPane;
    private JSpinner spinner;
    private JLabel lblUploadSpeed;
    private JPanel TabQueue;
    private JPanel panel_4;
    private JScrollPane scrollPane_1;
    private JButton btnStart;
    private JButton btnStop;
}
