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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
import javax.swing.KeyStroke;
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
import at.becast.youploader.templates.Item;
import at.becast.youploader.templates.Template;
import at.becast.youploader.templates.TemplateManager;
import at.becast.youploader.youtube.Categories;
import at.becast.youploader.youtube.LicenseType;
import at.becast.youploader.youtube.VisibilityType;
import at.becast.youploader.youtube.data.CategoryType;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.exceptions.UploadException;
import at.becast.youploader.youtube.io.UploadManager;
import ch.qos.logback.classic.LoggerContext;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author genuineparts
 */

public class FrmMain extends JFrame implements IMainMenu {

	private static final long serialVersionUID = 6965358827253585528L;
	public static final String DB_FILE = System.getProperty("user.home") + "/YouPloader/data/data.db";
	public static final String APP_NAME = "YouPloader";
	public static final String VERSION = "0.3";
	private static final Logger LOG = LoggerFactory.getLogger(FrmMain.class);
	public static UploadManager UploadMgr;
	public static TemplateManager TemplateMgr;
	private static final ResourceBundle LANG = ResourceBundle.getBundle("lang", Locale.getDefault());
	private static Settings s;
	private static Boolean firstlaunch = false;
	private AccountManager accMng = AccountManager.getInstance();
	private Boolean tos;
	private JButton btnAddToQueue;
	private ModalDialog modal;
	private IMainMenu self;
	private JTextArea txtDescription;
	private JTabbedPane TabbedPane;
	private JComboBox<String> cmbFile;
	private JMenu mnuAcc;
	private JTextField txtTitle;
	private JLabel lblTagslenght, lbltitlelenght, lblDesclenght;
	private JSpinner spinner;
	private JPanel QueuePanel;
	private JComboBox<CategoryType> cmbCategory;
	private JTextArea txtTags;
	private JComboBox<AccountType> cmbAccount;
	private SidebarSection ss1, ss2, ss3;
	public transient static HashMap<Integer, JMenuItem> _accounts = new HashMap<Integer, JMenuItem>();
	private int editItem = -1;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/*
		 * if (args.length > 0 && args[0].equalsIgnoreCase("debug")) {
		 * 
		 * 
		 * }
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			LOG.error("Look and Feel exception", e);
		}
		File dataDir = new File(System.getProperty("user.home") + "/YouPloader/data/");
		if (!dataDir.exists()) {
			LOG.info(APP_NAME + " " + VERSION + " first launch. Database folder not found.", FrmMain.class);
			dataDir.mkdirs();
			if (!SQLite.setup()) {
				JOptionPane.showMessageDialog(null,
						String.format(LANG.getString("frmMain.errordatabase.Message"),
								System.getProperty("user.home") + "/YouPloader/data/"),
						LANG.getString("frmMain.errordatabase.title"), JOptionPane.ERROR_MESSAGE);
			}
			firstlaunch = true;
		}
		UploadMgr = UploadManager.getInstance();
		TemplateMgr = TemplateManager.getInstance();
		s = Settings.getInstance();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FrmMain().setVisible(true);
			}
		});
	}

	/**
	 * Creates new form frmMain
	 */
	public FrmMain() {
		LOG.info(APP_NAME + " " + VERSION + " starting.", FrmMain.class);
		self = this;
		this.tos = false;
		this.setMinimumSize(new Dimension(900, 580));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LOG.info(APP_NAME + " " + VERSION + " closing.", FrmMain.class);
				LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
				loggerContext.stop();
				e.getWindow().dispose();
			}
		});
		UploadMgr.setParent(this);
		initComponents();
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/yp.png")));
		this.setLocationRelativeTo(null);
		try {
			loadQueue();
		} catch (SQLException | IOException e) {
			LOG.error("Error: ", e);
		}
		EditPanel edit = (EditPanel) ss1.contentPane;
		if (edit.getCmbTemplate().getModel().getSize() > 0) {
			edit.getCmbTemplate().setSelectedIndex(0);
		}
		this.setVisible(true);
		if (firstlaunch) {
			int n = JOptionPane.showConfirmDialog(null, LANG.getString("frmMain.initialAccount.Message"),
					LANG.getString("frmMain.initialAccount.title"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (n == JOptionPane.YES_OPTION) {
				mntmAddAccountActionPerformed();
			}
		}
	}

	/**
	 * 
	 */
	public void initComponents() {
		LOG.debug("init Components", FrmMain.class);
		TabbedPane = new JTabbedPane();
		JPanel mainTab = new JPanel();
		JMenuBar mnuBar = new JMenuBar();
		JMenu mnuFile = new JMenu();
		JMenuItem mnuQuit = new JMenuItem();
		mnuAcc = new JMenu();
		cmbCategory = new JComboBox<CategoryType>();
		cmbCategory.setModel(new DefaultComboBoxModel<CategoryType>());
		SideBar sideBar = new SideBar(SideBar.SideBarMode.TOP_LEVEL, true, 300, true);
		ss1 = new SidebarSection(sideBar, LANG.getString("frmMain.Sidebar.Settings"), new EditPanel(this), null);
		ss2 = new SidebarSection(sideBar, LANG.getString("frmMain.Sidebar.Playlists"), new PlaylistPanel(this), null);
		ss3 = new SidebarSection(sideBar, LANG.getString("frmMain.Sidebar.Monetisation"), new MonetPanel(this), null);
		sideBar.addSection(ss1, false);
		sideBar.addSection(ss2);
		sideBar.addSection(ss3);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(APP_NAME + " " + VERSION);
		setName("frmMain");
		for (Categories cat : Categories.values()) {
			cmbCategory.addItem(new CategoryType(cat.getID(), cat.toString()));
		}

		JPanel panel = new JPanel();
		GroupLayout mainTabLayout = new GroupLayout(mainTab);
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
				calcNotifies();
			}
		});

		JLabel lblCategory = new JLabel(LANG.getString("frmMain.Category"));
		panel.add(lblCategory, "3, 9, 4, 1, left, top");
		panel.add(cmbCategory, "3, 10, 14, 1, fill, fill");

		JLabel lblDescription = new JLabel(LANG.getString("frmMain.Description"));
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
				calcNotifies();
			}
		});

		JLabel lblTags = new JLabel(LANG.getString("frmMain.Tags"));
		panel.add(lblTags, "3, 14, left, top");

		lblTagslenght = new JLabel("(0/500)");
		panel.add(lblTagslenght, "14, 14, 3, 1, right, top");

		JScrollPane TagScrollPane = new JScrollPane();
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
				calcNotifies();
			}
		});

		JLabel lblAccount = new JLabel(LANG.getString("frmMain.Account"));
		panel.add(lblAccount, "3, 18, 4, 1, left, top");
		cmbAccount = new JComboBox<AccountType>();
		panel.add(cmbAccount, "3, 19, 14, 1, fill, fill");

		btnAddToQueue = new JButton(LANG.getString("frmMain.addtoQueue"));
		btnAddToQueue.setEnabled(false);
		panel.add(btnAddToQueue, "3, 21, 6, 1, fill, fill");
		btnAddToQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queueButton();
			}
		});
		JLabel lblSelectVideo = new JLabel();
		panel.add(lblSelectVideo, "3, 3, 4, 1, left, top");

		lblSelectVideo.setText(LANG.getString("frmMain.selectVideoFile"));
		cmbFile = new JComboBox<String>();
		panel.add(cmbFile, "3, 4, 14, 1, fill, fill");
		JButton btnSelectMovie = new JButton();
		panel.add(btnSelectMovie, "18, 4, center, top");
		btnSelectMovie.setIcon(new ImageIcon(getClass().getResource("/film_add.png")));

		JLabel lblTitle = new JLabel(LANG.getString("frmMain.Title"));
		panel.add(lblTitle, "3, 6, left, top");

		JButton btnReset = new JButton(LANG.getString("frmMain.Reset"));
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
		TabbedPane.addTab(LANG.getString("frmMain.Tabs.VideoSettings"), mainTab);

		mnuFile.setText(LANG.getString("frmMain.menu.File"));

		mnuQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnuQuit.setText(LANG.getString("frmMain.menu.Quit"));
		mnuQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuQuitActionPerformed();
			}
		});
		mnuFile.add(mnuQuit);

		mnuBar.add(mnuFile);

		mnuAcc.setText(LANG.getString("frmMain.menu.Account"));
		mnuBar.add(mnuAcc);

		JSeparator separator = new JSeparator();
		JMenuItem mntmAddAccount = new JMenuItem(LANG.getString("frmMain.menu.AddAccount"));
		mntmAddAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mntmAddAccountActionPerformed();
			}
		});

		mnuAcc.add(mntmAddAccount);
		mnuAcc.add(separator);

		setJMenuBar(mnuBar);

		JMenu menu = new JMenu("?");
		mnuBar.add(menu);

		JMenuItem mntmDonate = new JMenuItem(LANG.getString("frmMain.menu.Donate"));
		menu.add(mntmDonate);
		mntmDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI(
								"https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ42BHSUTGPT6"));
					} catch (IOException | URISyntaxException e1) {
						LOG.error("Can't open browser");
					}
				} else {
					LOG.error("Desktop not supported.");
				}
			}
		});

		JMenuItem mntmAbout = new JMenuItem(LANG.getString("frmMain.menu.About"));
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				FrmAbout about = new FrmAbout();
				about.setVisible(true);
			}
		});
		menu.add(mntmAbout);

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(TabbedPane).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(TabbedPane,
				GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE));

		JScrollPane TabQueues = new JScrollPane();
		QueuePanel = new JPanel();
		TabQueues.setViewportView(QueuePanel);
		getContentPane().setLayout(layout);
		QueuePanel.setLayout(new MigLayout("", "[875px,grow,fill]", "[][][][]"));
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

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startUploads();
			}
		});
		buttonPanel.add(btnStart, "2, 4");

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UploadMgr.stop();
			}
		});
		buttonPanel.add(btnStop, "6, 4");

		JLabel lblUploads = new JLabel("Uploads:");
		buttonPanel.add(lblUploads, "18, 4, right, fill");

		JSlider slider = new JSlider();
		slider.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				JSlider s = (JSlider) evt.getSource();
				UploadMgr.setUploadlimit(s.getValue());
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

		JLabel lblUploadSpeed = new JLabel("Upload Speed:");
		lblUploadSpeed.setHorizontalAlignment(SwingConstants.TRAILING);
		buttonPanel.add(lblUploadSpeed, "24, 4");

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(10)));
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				UploadMgr.setLimit(Integer.parseInt(s.getValue().toString()));
			}
		});
		buttonPanel.add(spinner, "26, 4");

		JLabel lblKbps = new JLabel("kbps");
		buttonPanel.add(lblKbps, "28, 4");

		JPanel TabQueue = new JPanel();
		TabbedPane.addTab(LANG.getString("frmMain.Tabs.Queue"), null, TabQueue, null);
		TabQueue.setLayout(new BorderLayout(0, 0));

		TabQueue.add(buttonPanel, BorderLayout.SOUTH);

		TabQueue.add(TabQueues, BorderLayout.CENTER);

		QueuePanel.revalidate();
		loadAccounts();
		pack();
		ss1.expand();
	}

	protected void startUploads() {
		if ("0".equals(s.setting.get("tos_agreed")) && !this.tos) {
			JCheckBox checkbox = new JCheckBox(LANG.getString("frmMain.tos.Remember"));
			String message = LANG.getString("frmMain.tos.Message");
			Object[] params = { message, checkbox };
			int n;
			do {
				n = JOptionPane.showConfirmDialog(null, params, LANG.getString("frmMain.tos.Title"),
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			} while (n == JOptionPane.CLOSED_OPTION);
			if (n == JOptionPane.OK_OPTION) {
				if (checkbox.isSelected()) {
					s.setting.put("tos_agreed", "1");
					s.save("tos_agreed");
				}
				this.tos = true;
				UploadMgr.start();
			}
		} else {
			UploadMgr.start();
		}
	}

	private void mnuQuitActionPerformed() {
		System.exit(0);
	}

	private void mntmAddAccountActionPerformed() {
		AddAccount acc = new AddAccount(this);
		acc.setVisible(true);

	}

	public void prepModal(Account Account, String code) {
		modal = new ModalDialog((Frame) this, Account, code);
	}

	public void showModal() {
		modal.setVisible(true);
	}

	public void closeModal() {
		modal.success();
	}

	public void refreshAccounts() {
		int s = _accounts.size();
		for (int i = 0; i < s; i++) {
			mnuAcc.remove(_accounts.get(i));
			_accounts.remove(i);
		}
		cmbAccount.removeAllItems();
		btnAddToQueue.setEnabled(false);
		loadAccounts();
	}

	public void loadAccounts() {
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
					editAccount(e.getActionCommand());
				}
			});
			_accounts.put(i, rdoBtn);
			mnuAcc.add(rdoBtn);
			i++;
		}
		if (!accounts.isEmpty()) {
			btnAddToQueue.setEnabled(true);
		}

	}

	protected void editAccount(String name) {
		int id = accMng.getId(name);
		EditAccount accedit = new EditAccount(this, name, id);
		accedit.setVisible(true);
	}

	private void loadQueue() throws JsonParseException, JsonMappingException, SQLException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement prest = null;
		Connection c = SQLite.getInstance();
		String sql = "SELECT * FROM `uploads` ORDER BY `id`";
		prest = c.prepareStatement(sql);
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
				String enddir = rs.getString("enddir");
				long position = rs.getLong("uploaded");
				long size = rs.getLong("lenght");
				if (url != null && !url.equals("") && !"FINISHED".equals(status)) {
					UploadMgr.addResumeableUpload(f, data, v, acc_id, enddir, url, yt_id);
					f.getProgressBar().setString(String.format("%6.2f%%", (float) position / size * 100));
					f.getProgressBar().setValue((int) ((float) position / size * 100));
					f.getProgressBar().revalidate();
					f.revalidate();
					f.repaint();
				} else if ("NOT_STARTED".equals(status)) {
					UploadMgr.addUpload(f, data, v, acc_id, enddir);
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
	public UploadItem createUpload(String File, String Name, int acc_id) throws IOException {
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
		UploadMgr.addUpload(f, data, v, acc_id, edit.getTxtEndDir().getText());
		return f;
	}

	public void update(String File, int acc_id, int upload_id) throws IOException, SQLException {
		EditPanel edit = (EditPanel) ss1.contentPane;
		String sql = "SELECT `yt_id` FROM `uploads` WHERE `id`=" + upload_id;
		PreparedStatement prest = null;
		Connection c = SQLite.getInstance();
		prest = c.prepareStatement(sql);
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
				String enddir = edit.getTxtEndDir().getText();
				v.status.license = license.getData();
				if (video_id != null && !video_id.equals("")) {
					SQLite.updateUploadData(v, upload_id);
				} else {
					File data = new File(File);
					SQLite.updateUpload(acc_id, data, v, enddir, upload_id);
				}
				File data = new File(File);
				UploadMgr.updateUpload(upload_id, data, v, acc_id);
			}
		}
	}

	private void queueButton() {
		AccountType acc = (AccountType) cmbAccount.getSelectedItem();
		if (this.editItem != -1) {
			try {
				update(cmbFile.getSelectedItem().toString(), acc.getValue(), this.editItem);
			} catch (IOException | SQLException e) {
				LOG.error("Error: ", e);
			}
			btnAddToQueue.setText(LANG.getString("frmMain.addtoQueue"));
			cmbFile.removeAllItems();
		} else {
			if (cmbFile.getSelectedItem() != null && !cmbFile.getSelectedItem().toString().equals("")) {
				try {
					createUpload(cmbFile.getSelectedItem().toString(), txtTitle.getText(), acc.getValue());
					cmbFile.removeAllItems();
				} catch (IOException e1) {
					LOG.error("Error: ", e1);
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
		Connection c = SQLite.getInstance();
		btnAddToQueue.setText(LANG.getString("frmMain.updateUpload"));
		TabbedPane.setSelectedIndex(0);
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement prest = null;
		EditPanel edit = (EditPanel) ss1.contentPane;
		String sql = "SELECT * FROM `uploads` WHERE `id`=" + upload_id;
		prest = c.prepareStatement(sql);
		ResultSet rs = prest.executeQuery();
		if (rs.isBeforeFirst()) {
			while (rs.next()) {
				Video v = mapper.readValue(rs.getString("data"), new TypeReference<Video>() {
				});
				cmbFile.removeAllItems();
				cmbFile.addItem(rs.getString("file"));
				this.setCategory(v.snippet.categoryId);
				txtTitle.setText(v.snippet.title);
				cmbAccount.setSelectedItem(rs.getString("account"));
				txtDescription.setText(v.snippet.description);
				txtTags.setText(prepareTagsfromArray(v.snippet.tags));
				edit.setLicence(v.status.license);
				edit.setVisibility(v.status.privacyStatus);
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
						LOG.error("Error: ", e);
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

	public void createTemplate(String name) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		Template t = new Template(name);
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
		v.status.privacyStatus = visibility.getData();
		LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
		v.status.license = license.getData();
		if (edit.getTxtStartDir() != null) {
			t.setStartdir(edit.getTxtStartDir().getText());
		}
		if (edit.getTxtEndDir() != null) {
			t.setEnddir(edit.getTxtEndDir().getText());
		}
		t.setVideodata(v);
		TemplateMgr.save(t);
		edit.refreshTemplates(t.name);
	}

	private String prepareTagsfromArray(String[] in) {
		String tags = "";
		for (int i = 0; i < in.length; i++) {
			if (i == 0) {
				tags = in[i];
			} else {
				tags += "," + in[i];
			}
		}
		return tags;
	}

	public void loadTemplate(Item item) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		Template t = item.getTemplate();
		this.setCategory(t.videodata.snippet.categoryId);
		txtTitle.setText(t.videodata.snippet.title);
		txtDescription.setText(t.videodata.snippet.description);
		txtTags.setText(prepareTagsfromArray(t.videodata.snippet.tags));
		edit.setLicence(t.videodata.status.license);
		if (t.videodata.status.publishAt != null && t.videodata.status.publishAt.equals("1")
				&& t.videodata.status.privacyStatus.equals("private")) {
			edit.getCmbVisibility().setSelectedItem(VisibilityType.SCHEDULED);
		} else if (t.videodata.status.publishAt != null && t.videodata.status.publishAt.equals("0")
				&& t.videodata.status.privacyStatus.equals("private")) {
			edit.getCmbVisibility().setSelectedItem(VisibilityType.PRIVATE);
		} else {
			edit.setVisibility(t.videodata.status.privacyStatus);
		}
		if (t.enddir != null) {
			edit.getTxtEndDir().setText(t.enddir);
		}

		if (t.startdir != null) {
			edit.getTxtStartDir().setText(t.startdir);
		}
		calcNotifies();
	}

	private void calcNotifies() {
		if (txtTags.getText().length() > 450) {
			lblTagslenght.setForeground(Color.RED);
		} else {
			lblTagslenght.setForeground(Color.BLACK);
		}
		if (txtTags.getText().length() >= 501) {
			txtTags.setText(txtTags.getText().substring(0, 500));

		}
		lblTagslenght.setText("(" + txtTags.getText().length() + "/500)");

		if (txtDescription.getText().length() > 900) {
			lblDesclenght.setForeground(Color.RED);
		} else {
			lblDesclenght.setForeground(Color.BLACK);
		}
		if (txtDescription.getText().length() >= 1001) {
			txtDescription.setText(txtDescription.getText().substring(0, 1000));

		}
		lblDesclenght.setText("(" + txtDescription.getText().length() + "/1000)");

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

	public void saveTemplate(int id) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		Video v = new Video();
		Template t = TemplateMgr.get(id);
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
			v.status.publishAt = "1";
		} else {
			v.status.publishAt = "0";
		}
		v.status.privacyStatus = visibility.getData();
		LicenseType license = (LicenseType) edit.getCmbLicense().getSelectedItem();
		v.status.license = license.getData();
		if (edit.getTxtStartDir() != null) {
			t.setStartdir(edit.getTxtStartDir().getText());
		}
		if (edit.getTxtEndDir() != null) {
			t.setEnddir(edit.getTxtEndDir().getText());
		}
		t.setVideodata(v);
		TemplateMgr.update(id, t);
		edit.refreshTemplates(t.name);
	}

	public void setCategory(int catId) {
		for (int i = 0; i < cmbCategory.getItemCount(); i++) {
			if (cmbCategory.getItemAt(i).getValue() == catId) {
				cmbCategory.setSelectedIndex(i);
			}
		}
	}

	public void deleteTemplate(int id) {
		EditPanel edit = (EditPanel) ss1.contentPane;
		TemplateMgr.delete(id);
		edit.refreshTemplates();
	}

	private JPanel getQueuePanel() {
		return QueuePanel;
	}

	public JTextArea getTxtTags() {
		return txtTags;
	}

	public JTextArea getTxtDescription() {
		return txtDescription;
	}

	public JSpinner getSpinner() {
		return spinner;
	}
}
