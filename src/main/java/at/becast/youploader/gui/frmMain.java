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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import at.becast.youploader.account.Account;
import at.becast.youploader.account.AccountManager;
import at.becast.youploader.account.AccountType;
import at.becast.youploader.database.SQLite;
import at.becast.youploader.gui.slider.SideBar;
import at.becast.youploader.gui.slider.SidebarSection;
import at.becast.youploader.settings.Settings;
import at.becast.youploader.templates.TemplateManager;
import at.becast.youploader.youtube.Categories;
import at.becast.youploader.youtube.LicenseType;
import at.becast.youploader.youtube.VisibilityType;
import at.becast.youploader.youtube.data.CategoryType;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.exceptions.UploadException;
import at.becast.youploader.youtube.io.UploadManager;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author genuineparts
 */

public class frmMain extends javax.swing.JFrame implements IMainMenu {

	private static final long serialVersionUID = 6965358827253585528L;
	public static final String DB_FILE = "data/data.db";
	public static final String VERSION = "0.3";
	private static final Logger LOG = LoggerFactory.getLogger(frmMain.class);
	public static UploadManager UploadManager;
	public static TemplateManager TemplateMgr;
	static Locale locale = Locale.getDefault();
	private static final ResourceBundle LANG = ResourceBundle.getBundle("lang", locale);
	public Settings s = Settings.getInstance();
	public AccountManager accMng = AccountManager.getInstance();
	private ModalDialog modal;
	private IMainMenu self;
	private JTextArea txtDescription;
	private JTabbedPane TabbedPane;
	private JComboBox<String> cmbFile;
	private JLabel lblSelectVideo;
	private JPanel mainTab;
	private JMenu menu, mnuAcc, mnuFile;
	private JMenuBar mnuBar;
	private JMenuItem mnuQuit, mntmAbout;
	private JTextField txtTitle;
	private JMenuItem mntmAddAccount;
	private JLabel lblTagslenght, lbltitlelenght, lblDesclenght;
	private JScrollPane TabQueues;
	private JPanel panel, panel_1;
	private JSpinner spinner;
	private JLabel lblUploadSpeed;
	private JPanel TabQueue;
	private JButton btnSelectMovie, btnStart, btnStop, btnAddToQueue;
	private JPanel QueuePanel;
	private JComboBox<CategoryType> cmbCategory;
	private JMenuItem mntmDonate;
	private JTextArea txtTags;
	private JComboBox<AccountType> cmbAccount;
	private SidebarSection ss1, ss2, ss3;
	public transient static HashMap<Integer, JMenuItem> _accounts = new HashMap<Integer, JMenuItem>();
	private JSlider slider;
	private JLabel lblUploads;
	private JScrollPane TagScrollPane;
	private int editItem = -1;

	/**
	 * Creates new form frmMain
	 */
	public frmMain() {

		self = this;
		UploadManager = new UploadManager(this);
		TemplateMgr = TemplateManager.getInstance();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			LOG.error(e1.getMessage(), frmMain.class);
		} catch (InstantiationException e1) {
			LOG.error(e1.getMessage(), frmMain.class);
		} catch (IllegalAccessException e1) {
			LOG.error(e1.getMessage(), frmMain.class);
		} catch (UnsupportedLookAndFeelException e1) {
			LOG.error(e1.getMessage(), frmMain.class);
		}
		initComponents();
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
		this.setLocationRelativeTo(null);
		try {
			load_queue();
		} catch (SQLException | IOException e) {
			LOG.error(e.getMessage(), frmMain.class);
		}
	}

	/**
	 * 
	 */
	public void initComponents() {
		LOG.debug("init Components", frmMain.class);
		TabbedPane = new JTabbedPane();
		mainTab = new JPanel();
		cmbCategory = new JComboBox<CategoryType>();
		cmbCategory.setModel(new DefaultComboBoxModel<CategoryType>());
		SideBar sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 300, true);
		ss1 = new SidebarSection(sideBar, "Settings", new EditPanel(this), null);
		ss2 = new SidebarSection(sideBar, "Playlists", new PlaylistPanel(this), null);
		ss3 = new SidebarSection(sideBar, "Monetisation", new MonetPanel(this), null);
		sideBar.addSection(ss1, false);
		sideBar.addSection(ss2);
		sideBar.addSection(ss3);
		mnuBar = new javax.swing.JMenuBar();
		mnuFile = new javax.swing.JMenu();
		mnuQuit = new javax.swing.JMenuItem();
		mnuAcc = new javax.swing.JMenu();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("YouPloader " + VERSION);
		setName("frmMain");
		for (Categories cat : Categories.values()) {
			cmbCategory.addItem(new CategoryType(cat.getID(), cat.toString()));
		}

		panel = new JPanel();
		javax.swing.GroupLayout mainTabLayout = new javax.swing.GroupLayout(mainTab);
		mainTabLayout
				.setHorizontalGroup(mainTabLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						mainTabLayout.createSequentialGroup()
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)));
		mainTabLayout.setVerticalGroup(mainTabLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(sideBar, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE));
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("2px"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("20px:grow"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
						ColumnSpec.decode("23px"), ColumnSpec.decode("33px"), FormSpecs.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("61px"), FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("24px"), ColumnSpec.decode("28px"), ColumnSpec.decode("40px"),
						ColumnSpec.decode("36px"), FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("28px"),
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("58px"), },
				new RowSpec[] { RowSpec.decode("2px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("14px"),
						RowSpec.decode("25px"), FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("14px"),
						RowSpec.decode("25px"), FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("14px"),
						RowSpec.decode("25px"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						RowSpec.decode("64dlu"), RowSpec.decode("14px"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(64dlu;default):grow"), FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, RowSpec.decode("25px"), FormSpecs.PARAGRAPH_GAP_ROWSPEC,
						RowSpec.decode("24px"), RowSpec.decode("23px"), }));

		lbltitlelenght = new JLabel("(0/100)");
		panel.add(lbltitlelenght, "14, 6, 3, 1, right, top");

		txtTitle = new JTextField();
		panel.add(txtTitle, "3, 7, 14, 1, fill, fill");
		txtTitle.setColumns(10);
		txtTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtTitle.getText().length() > 90) {
					lbltitlelenght.setForeground(Color.RED);
				} else {
					lbltitlelenght.setForeground(Color.BLACK);
				}
				if (txtTitle.getText().length() >= 101) {
					txtTitle.setText(txtTitle.getText().substring(0, 100));

				}
				lbltitlelenght.setText("(" + txtTitle.getText().length() + "/100)");
			}
		});

		JLabel lblCategory = new JLabel("Category");
		panel.add(lblCategory, "3, 9, 4, 1, left, top");
		panel.add(cmbCategory, "3, 10, 14, 1, fill, fill");

		JLabel lblDescription = new JLabel("Description");
		panel.add(lblDescription, "3, 12, 4, 1, left, bottom");

		lblDesclenght = new JLabel("(0/1000)");
		panel.add(lblDesclenght, "14, 12, 3, 1, right, bottom");

		JScrollPane DescriptionScrollPane = new JScrollPane();
		panel.add(DescriptionScrollPane, "3, 13, 14, 1, fill, fill");

		txtDescription = new JTextArea();
		DescriptionScrollPane.setViewportView(txtDescription);
		txtDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtDescription.setWrapStyleWord(true);
		txtDescription.setLineWrap(true);
		txtDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtDescription.getText().length() > 900) {
					lblDesclenght.setForeground(Color.RED);
				} else {
					lblDesclenght.setForeground(Color.BLACK);
				}
				if (txtDescription.getText().length() >= 1001) {
					txtDescription.setText(txtDescription.getText().substring(0, 1000));

				}
				lblDesclenght.setText("(" + txtDescription.getText().length() + "/1000)");
			}
		});

		JLabel lblTags = new JLabel("Tags");
		panel.add(lblTags, "3, 14, left, top");

		lblTagslenght = new JLabel("(0/500)");
		panel.add(lblTagslenght, "14, 14, 3, 1, right, top");

		TagScrollPane = new JScrollPane();
		panel.add(TagScrollPane, "3, 16, 14, 1, fill, fill");

		txtTags = new JTextArea();
		TagScrollPane.setViewportView(txtTags);
		txtTags.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtTags.setWrapStyleWord(true);
		txtTags.setLineWrap(true);
		txtTags.setBorder(BorderFactory.createEtchedBorder());
		txtTags.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtTags.getText().length() > 450) {
					lblTagslenght.setForeground(Color.RED);
				} else {
					lblTagslenght.setForeground(Color.BLACK);
				}
				if (txtTags.getText().length() >= 501) {
					txtTags.setText(txtTags.getText().substring(0, 500));

				}
				lblTagslenght.setText("(" + txtTags.getText().length() + "/500)");
			}
		});

		JLabel lblAccount = new JLabel("Account");
		panel.add(lblAccount, "3, 18, 4, 1, left, top");
		cmbAccount = new JComboBox<AccountType>();
		panel.add(cmbAccount, "3, 19, 14, 1, fill, fill");

		btnAddToQueue = new JButton(LANG.getString("frmMain.addtoQueue"));
		panel.add(btnAddToQueue, "3, 21, 6, 1, fill, fill");
		btnAddToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueueButton();
			}
		});
		lblSelectVideo = new JLabel();
		panel.add(lblSelectVideo, "3, 3, 4, 1, left, top");

		lblSelectVideo.setText("Select Video File");
		cmbFile = new JComboBox<String>();
		panel.add(cmbFile, "3, 4, 14, 1, fill, fill");
		btnSelectMovie = new JButton();
		panel.add(btnSelectMovie, "18, 4, center, top");
		btnSelectMovie.setIcon(new ImageIcon(getClass().getResource("/film_add.png")));

		JLabel lblTitle = new JLabel("Title");
		panel.add(lblTitle, "3, 6, left, top");

		JButton btnReset = new JButton("Reset");
		panel.add(btnReset, "11, 21, 6, 1, fill, fill");
		btnSelectMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				EditPanel edit = (EditPanel) ss1.contentPane;
				if (edit.getTxtStartDir() != null && !edit.getTxtStartDir().equals("")) {
					chooser.setCurrentDirectory(new File(edit.getTxtStartDir().getText()));
				}
				int returnVal = chooser.showOpenDialog((Component) self);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					cmbFile.removeAllItems();
					cmbFile.addItem(chooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		mainTab.setLayout(mainTabLayout);
		TabbedPane.addTab("Video Settings", mainTab);

		mnuFile.setText("File");

		mnuQuit.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
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
						Desktop.getDesktop().browse(new URI(
								"https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ42BHSUTGPT6"));
					} catch (IOException | URISyntaxException e1) {
						/* TODO: error handling */ }
				} else {
					/* TODO: error handling */ }
			}
		});

		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				frmAbout about = new frmAbout();
				about.setVisible(true);
			}
		});
		menu.add(mntmAbout);
		// https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ42BHSUTGPT6

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(TabbedPane).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(TabbedPane,
				GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE));

		TabQueues = new JScrollPane();
		QueuePanel = new JPanel();
		TabQueues.setViewportView(QueuePanel);
		getContentPane().setLayout(layout);
		QueuePanel.setLayout(new MigLayout("", "[875px,grow,fill]", "[][][][]"));
		/*
		 * UploadItem i = new UploadItem(); panel.add(i, new CC().wrap());
		 * panel.revalidate(); UploadItem s = new UploadItem(); panel.add(s, new
		 * CC().wrap()); panel.revalidate(); UploadItem r = new UploadItem();
		 * panel.add(r, new CC().wrap()); panel.revalidate(); UploadItem f = new
		 * UploadItem(); panel.add(f, new CC().wrap());
		 */

		/*
		 * GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		 * gbc_scrollPane.insets = new Insets(0, 0, 5, 0); gbc_scrollPane.fill =
		 * GridBagConstraints.BOTH; gbc_scrollPane.gridx = 0;
		 * gbc_scrollPane.gridy = 0;
		 */

		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		buttonPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(39dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(92dlu;default)"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(46dlu;min)"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:max(22dlu;default)"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(0dlu;default)"), }));

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UploadManager.start();
			}
		});
		buttonPanel.add(btnStart, "2, 4");

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UploadManager.stop();
			}
		});
		buttonPanel.add(btnStop, "6, 4");

		lblUploads = new JLabel("Uploads:");
		buttonPanel.add(lblUploads, "18, 4, right, fill");

		slider = new JSlider();
		slider.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				JSlider s = (JSlider) evt.getSource();
				UploadManager.set_uploadlimit(s.getValue());
			}
		});
		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);
		slider.setMinimum(1);
		slider.setMaximum(5);
		slider.setValue(1);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		buttonPanel.add(slider, "20, 4, fill, fill");

		lblUploadSpeed = new JLabel("Upload Speed:");
		lblUploadSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		buttonPanel.add(lblUploadSpeed, "24, 4");

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				UploadManager.set_limit(Integer.parseInt(s.getValue().toString()));
			}
		});
		buttonPanel.add(spinner, "26, 4");

		JLabel lblKbps = new JLabel("kbps");
		buttonPanel.add(lblKbps, "28, 4");

		TabQueue = new JPanel();
		TabbedPane.addTab("Queue", null, TabQueue, null);
		TabQueue.setLayout(new BorderLayout(0, 0));

		TabQueue.add(buttonPanel, BorderLayout.SOUTH);

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
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		if (args.length > 0 && args[0].equalsIgnoreCase("debug")) {

		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new frmMain().setVisible(true);
			}
		});
	}

	public void prep_modal(Account Account, String code) {
		modal = new ModalDialog((Frame) this, Account, code);
	}

	public void show_modal() {
		modal.setVisible(true);
	}

	public void close_modal() {
		modal.success();
	}

	public void load_accounts() {
		int i = 0;
		HashMap<AccountType, Integer> accounts = accMng.load();
		for (Entry<AccountType, Integer> entry : accounts.entrySet()) {
			cmbAccount.addItem(entry.getKey());
			JMenuItem rdoBtn = new JMenuItem(entry.getKey().toString());
			if (entry.getValue() == 1) {
				rdoBtn.setSelected(true);
				cmbAccount.setSelectedItem(entry.getKey());
			}
			rdoBtn.setActionCommand(entry.getKey().toString());
			rdoBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// accMng.change_user(e.getActionCommand());
				}
			});
			_accounts.put(i, rdoBtn);
			mnuAcc.add(rdoBtn);
			i++;
		}

	}

	private void load_queue() throws JsonParseException, JsonMappingException, SQLException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement prest = null;
		String sql = "SELECT * FROM `uploads` ORDER BY `id`";
		prest = SQLite.c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				UploadItem f = new UploadItem();
				Video v = mapper.readValue(rs.getString("data"), new TypeReference<Video>() {
				});
				f.upload_id = rs.getInt("id");
				String url = rs.getString("url");
				String yt_id = rs.getString("yt_id");
				f.getlblUrl().setText("https://www.youtube.com/watch?v=" + yt_id);
				f.getlblName().setText(v.snippet.title);
				File data = new File(rs.getString("file"));
				int acc_id = rs.getInt("account");
				String status = rs.getString("status");
				long position = rs.getLong("uploaded");
				long size = rs.getLong("lenght");
				if (url != null && !url.equals("") && !status.equals("FINISHED")) {
					UploadManager.add_resumeable_upload(f, data, v, acc_id, url, yt_id);
					f.getProgressBar().setString(String.format("%6.2f%%", (float) position / size * 100));
					f.getProgressBar().setValue((int) ((float) position / size * 100));
					f.getProgressBar().revalidate();
					f.revalidate();
					f.repaint();
				} else if (status.equals("NOT_STARTED")) {
					UploadManager.add_upload(f, data, v, acc_id);
				} else {
					f.getBtnEdit().setEnabled(false);
					f.getProgressBar().setValue(100);
					f.getProgressBar().setString("100 %");
				}
				this.getQueuePanel().add(f, new CC().wrap());
				this.getQueuePanel().revalidate();

			}
			rs.close();
			prest.close();
		}
	}

	/**
	 * @param String
	 *            File
	 * @param String
	 *            Name
	 * @param String
	 *            Account
	 * @return UploadItem
	 * @throws IOException
	 * @throws UploadException
	 */
	public UploadItem create_upload(String File, String Name, int acc_id) throws IOException {
		UploadItem f = new UploadItem();
		EditPanel edit = (EditPanel) ss1.contentPane;
		f.getlblName().setText(Name);
		this.getQueuePanel().add(f, new CC().wrap());
		this.getQueuePanel().revalidate();
		Video v = new Video();
		v.snippet.title = Name;
		CategoryType cat = (CategoryType) cmbCategory.getSelectedItem();
		v.snippet.categoryId = cat.getValue();
		v.snippet.description = txtDescription.getText();
		if (txtTags != null && !txtTags.getText().equals("")) {
			String[] tags = txtTags.getText().split(",");
			String[] trimmedtags = new String[tags.length];
			for (int i = 0; i < tags.length; i++) {
				trimmedtags[i] = tags[i].trim();
			}
			v.snippet.tags = trimmedtags;
		}
		VisibilityType visibility = (VisibilityType) edit.getCmbVisibility().getSelectedItem();
		if (visibility == VisibilityType.SCHEDULED) {
			if (edit.getDateTimePicker().getEditor().getValue() != null
					&& !edit.getDateTimePicker().getEditor().getValue().equals("")) {
				v.status.privacyStatus = VisibilityType.SCHEDULED.getData();
				Date date = edit.getDateTimePicker().getDate();
				String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
				SimpleDateFormat formatter = new SimpleDateFormat(pattern);
				f.getlblRelease().setText(edit.getDateTimePicker().getEditor().getValue().toString());
				v.status.publishAt = formatter.format(date);
			} else {
				v.status.privacyStatus = VisibilityType.PRIVATE.getData();
			}
		} else {
			v.status.privacyStatus = visibility.getData();
			if (visibility == VisibilityType.PUBLIC) {
				f.getlblRelease().setText("public");
			}
		}
		LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
		v.status.license = license.getData();
		File data = new File(File);
		UploadManager.add_upload(f, data, v, acc_id);
		return f;
	}

	public void update(String File, int acc_id, int upload_id) throws IOException, SQLException {
		EditPanel edit = (EditPanel) ss1.contentPane;
		String sql = "SELECT `yt_id` FROM `uploads` WHERE `id`=" + upload_id;
		PreparedStatement prest = null;
		prest = SQLite.c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				String video_id = rs.getString("yt_id");
				Video v = new Video();
				v.snippet.title = txtTitle.getText();
				CategoryType cat = (CategoryType) cmbCategory.getSelectedItem();
				v.snippet.categoryId = cat.getValue();
				v.snippet.description = txtDescription.getText();
				if (txtTags != null && !txtTags.getText().equals("")) {
					String[] tags = txtTags.getText().split(",");
					String[] trimmedtags = new String[tags.length];
					for (int i = 0; i < tags.length; i++) {
						trimmedtags[i] = tags[i].trim();
					}
					v.snippet.tags = trimmedtags;
				}
				VisibilityType visibility = (VisibilityType) edit.getCmbVisibility().getSelectedItem();
				if (visibility == VisibilityType.SCHEDULED) {
					if (edit.getDateTimePicker().getEditor().getValue() != null
							&& !edit.getDateTimePicker().getEditor().getValue().equals("")) {
						v.status.privacyStatus = VisibilityType.SCHEDULED.getData();
						Date date = edit.getDateTimePicker().getDate();
						String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
						SimpleDateFormat formatter = new SimpleDateFormat(pattern);
						v.status.publishAt = formatter.format(date);
					} else {
						v.status.privacyStatus = VisibilityType.PRIVATE.getData();
					}
				} else {
					v.status.privacyStatus = visibility.getData();
				}
				LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
				v.status.license = license.getData();
				if (video_id != null && !video_id.equals("")) {
					SQLite.updateUploadData(v, upload_id);
				} else {
					File data = new File(File);
					SQLite.updateUpload(acc_id, data, v, upload_id);
				}
				File data = new File(File);
				UploadManager.update_upload(upload_id, data, v, acc_id);
			}
		}
	}

	private JPanel getQueuePanel() {
		return QueuePanel;
	}

	public void refresh_accounts() {
		int s = _accounts.size();
		for (int i = 0; i < s; i++) {
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

	private void QueueButton() {
		AccountType acc = (AccountType) cmbAccount.getSelectedItem();
		if (this.editItem != -1) {
			try {
				update(cmbFile.getSelectedItem().toString(), acc.getValue(), this.editItem);
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cmbFile.removeAllItems();
		} else {
			if (cmbFile.getSelectedItem() != null && !cmbFile.getSelectedItem().toString().equals("")) {
				try {
					create_upload(cmbFile.getSelectedItem().toString(), txtTitle.getText(), acc.getValue());
					cmbFile.removeAllItems();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "You have to select a file.", "Give me something to work with!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void removeItem(int upload_id) {
		for (int i = 0; i < this.getQueuePanel().getComponentCount(); i++) {
			UploadItem item = (UploadItem) this.getQueuePanel().getComponent(i);
			if (item.upload_id == upload_id) {
				this.getQueuePanel().remove(i);
				this.getQueuePanel().revalidate();
				this.getQueuePanel().repaint();
			}
		}
	}

	public void editUpload(int upload_id) throws SQLException, JsonParseException, JsonMappingException, IOException {
		this.editItem = upload_id;
		btnAddToQueue.setText(LANG.getString("frmMain.updateUpload"));
		TabbedPane.setSelectedIndex(0);
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement prest = null;
		EditPanel edit = (EditPanel) ss1.contentPane;
		String sql = "SELECT * FROM `uploads` WHERE `id`=" + upload_id;
		prest = SQLite.c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				Video v = mapper.readValue(rs.getString("data"), new TypeReference<Video>() {
				});
				cmbFile.removeAllItems();
				cmbFile.addItem(rs.getString("file"));
				for (int i = 0; i < cmbCategory.getItemCount(); i++) {
					if (cmbCategory.getItemAt(i).getValue() == v.snippet.categoryId) {
						cmbCategory.setSelectedIndex(i);
					}
				}
				txtTitle.setText(v.snippet.title);
				cmbAccount.setSelectedItem(rs.getString("account"));
				txtDescription.setText(v.snippet.description);
				String tags = "";
				for (int i = 0; i < v.snippet.tags.length; i++) {
					if (i == 0) {
						tags = v.snippet.tags[i];
					} else {
						tags += "," + v.snippet.tags[i];
					}
				}
				txtTags.setText(tags);
				for (int i = 0; i < edit.getCmbLicense().getItemCount(); i++) {
					if (edit.getCmbLicense().getItemAt(i).getData().equals(v.status.license)) {
						edit.getCmbLicense().setSelectedIndex(i);
					}
				}
				for (int i = 0; i < edit.getCmbVisibility().getItemCount(); i++) {
					if (edit.getCmbVisibility().getItemAt(i).getData().equals(v.status.privacyStatus)) {
						edit.getCmbVisibility().setSelectedIndex(i);
					}
				}
				if (v.status.publishAt != null && !v.status.publishAt.equals("")) {
					edit.getDateTimePicker().setEnabled(true);
					edit.getDateTimePicker().getEditor().setEnabled(true);
					String pattern = "yyyy-MM-dd'T'HH:mm:ss.sssZ";
					SimpleDateFormat formatter = new SimpleDateFormat(pattern);
					Date date;
					try {
						date = formatter.parse(v.status.publishAt);
						edit.getDateTimePicker().setDate(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				edit.revalidate();
				edit.repaint();

			}
			rs.close();
			prest.close();
		} else {
			rs.close();
			prest.close();
		}

	}
}
