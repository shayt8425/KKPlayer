package com.zhao.KKPlayer.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.zhao.KKPlayer.manager.PlayerDaoManager;
import com.zhao.KKPlayer.manager.PlayerManager;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.model.PlayList;

/**
 * KKPlayer的主客户端
 * 
 * @author Administrator
 * 
 */
public class KKPlayerClient {
	

	/**
	 * 播放器窗口的宽度
	 */
	public static int WIDTH = 470;
	/**
	 * 播放器窗口的高度
	 */
	public static int HEIGHT = 600;
	/**
	 * 播放器主窗口
	 */
	private JFrame mainFrame;
	private JPanel northJPanel;
	private JPanel topComponentsJPanel;
	private JPanel centerJPanel;
	private JPanel labelAndButtonJPanel;
	private JMenuBar mainMenuBar;
	/**
	 * 主菜单栏里的文件菜单
	 */
	private JMenu fileJMenu;
	/**
	 * 歌曲循环模式的菜单
	 */
	/**
	 * 编辑歌曲列表的菜单
	 */
	private EditMusicTableJMenu editMusicTableJMenu;

	/**
	 * 播放控制相关的菜单
	 */
	private PlayControlJMenu playControlJMenu;
	/**
	 * 打开文件的菜单项
	 */
	private JMenuItem openFileJMenuItem;

	/**
	 * 添加文件的菜单项
	 */
	private JMenuItem addFileJMenuItem;

	/**
	 * 退出系统的菜单选项
	 */
	private JMenuItem exitSystemJMenuItem;
	
	/**
	 * 添加文件夹菜单项
	 */
	private JMenuItem addDirectoryJMenuItem;

	/**
	 * 音乐播放的进度条
	 */
	private JSlider musicPlayingJSlider;

	/**
	 * 正在播放的歌曲的当前时间
	 */
	private JLabel playingTimeJLabel;

	/**
	 * 正在播放的歌曲的总时间
	 */
	private JLabel totalTimeJLabel;
	private JToolBar buttonJToolBar;
	private JButton playOrPauseJButton;
	private JButton stopJButton;
	private JButton nextJButton;
	private JButton prevJButton;
	private JButton openFileJButton;
	private JSplitPane musicJSplitPane;
	private JScrollPane leftJScrollPane;
	private JScrollPane rightJScrollPane;
	private JList playList;
	private JTable musicTable;
	private PlayListModel playListModel;
	private MusicTableModel musicTableModel;
	private PlayerManager playerManager;
	private MusicTableCellReader musicTableCellReader;
	private PlayerDaoManager playerDaoManager;
	private MusicLoopModeJMenu musicLoopModeJMenu;
	private PlayListJPopMenu playListJPopMenu;
	private MusicTableJPopMenu musicTableJPopMenu;
	private Properties props = new Properties();
	private TrayIcon trayIcon = null; // 托盘图标
	private MusicLyricShow musicLyricShow;
	
	public MusicLyricShow getMusicLyricShow(){
		if(musicLyricShow==null){
			musicLyricShow = new MusicLyricShow(this);
		}
		return musicLyricShow;		
	}

	public TrayIcon getTrayIcon() {
		return trayIcon;
	}

	private SystemTray tray = null; // 本操作系统托盘的实例

	public MusicTableJPopMenu getMusicTableJPopMenu() {
		if (musicTableJPopMenu == null) {
			musicTableJPopMenu = new MusicTableJPopMenu(this);
		}
		return musicTableJPopMenu;
	}

	public KKPlayerClient() {
		// 初始化至少有默认列表
		if (getPlayerDaoManager().getPlayLists().size() == 0) {
			getPlayerDaoManager().initPlayList();
		}
		// 读取配置文件
		try {
			InputStream is = new FileInputStream(
					"file/RecordOfPlayer.properties");
			props.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PlayListJPopMenu getPlayListJPopMenu() {
		if (playListJPopMenu == null) {
			playListJPopMenu = new PlayListJPopMenu(this);
		}
		return playListJPopMenu;
	}

	private MusicLoopModeJMenu getMusicLoopModeJMenu() {
		if (musicLoopModeJMenu == null) {
			musicLoopModeJMenu = new MusicLoopModeJMenu(this);
		}
		return musicLoopModeJMenu;
	}

	public PlayerDaoManager getPlayerDaoManager() {
		if (playerDaoManager == null) {
			playerDaoManager = new PlayerDaoManager(this);
		}
		return playerDaoManager;
	}

	private MusicTableCellReader getMusicTableCellReader() {
		if (musicTableCellReader == null) {
			musicTableCellReader = new MusicTableCellReader(this);
		}
		return musicTableCellReader;
	}

	public PlayerManager getPlayerManager() {
		if (playerManager == null) {
			playerManager = new PlayerManager(this);
		}
		return playerManager;
	}

	public MusicTableModel getMusicTableModel() {
		if (musicTableModel == null) {
			musicTableModel = new MusicTableModel(this);
		}
		return musicTableModel;
	}

	public PlayListModel getPlayListModel() {
		if (playListModel == null) {
			playListModel = new PlayListModel(this);
		}
		return playListModel;
	}

	/**
	 * 歌曲列表的自动滚动
	 */
	public void musicTableAutoScroll() {
		// 更新jscrollpane的视图
		getRightJScrollPane().setViewportView(getMusicJTable());
		// 得到正在播放的歌曲的行数
		int row;
		// 如果当前在列表有选择歌曲，则将滚动条的位置放到选中的歌曲位置
		if (getMusicJTable().getSelectedRow() != -1) {
			row = getMusicJTable().getSelectedRow();
		} else {
			row = getPlayerManager().findPlayingMusicIndex();
		}
		// 自动设置滚动条
		getRightJScrollPane().getVerticalScrollBar().setValue(20 * (row - 8));
	}

	public JList getPlayList() {
		if (playList == null) {
			playList = new JList();
			playList.setModel(getPlayListModel());
			playList.setSelectedIndex(0);
			playList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			playList.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					int index = getPlayList().getSelectedIndex();
					PlayList playList = getPlayerDaoManager().getPlayLists()
							.get(index);
					getPlayerDaoManager().musicTableValueChange(playList);
					musicTableAutoScroll();
				}
			});
			playList.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					int b = e.getButton();
					int index = getPlayList().locationToIndex(e.getPoint());
					if (b == MouseEvent.BUTTON3) {
						if (index == 0) {
							getPlayListJPopMenu()
									.getDeletePlayListJmeJMenuItem()
									.setEnabled(false);
							getPlayListJPopMenu()
									.getRenamePlayListJmeJMenuItem()
									.setEnabled(false);
						} else {
							getPlayListJPopMenu()
									.getRenamePlayListJmeJMenuItem()
									.setEnabled(true);
							getPlayListJPopMenu()
									.getDeletePlayListJmeJMenuItem()
									.setEnabled(true);
						}
						getPlayListJPopMenu().show(getPlayList(), e.getX(),
								e.getY());
						getPlayList().setSelectedIndex(index);
					}
				}
			});
		}
		return playList;
	}

	public JTable getMusicJTable() {
		if (musicTable == null) {
			musicTable = new JTable();
			musicTable.setModel(getMusicTableModel());
			musicTable.setSelectionForeground(Color.yellow);
			musicTable.setSelectionBackground(Color.red);
			musicTable.setRowHeight(20);
			musicTable.setFont(new Font("ssss", Font.PLAIN, 12));
			musicTable.setGridColor(Color.red);
			musicTable.getColumn("歌曲编号").setPreferredWidth(20);
			musicTable.getColumn(getMusicTableModel().getColumnName(1))
					.setPreferredWidth(180);
			musicTable.getColumn(getMusicTableModel().getColumnName(2))
					.setPreferredWidth(20);
			for (int i = 0; i < musicTable.getColumnCount(); i++) {
				musicTable.getColumn(getMusicTableModel().getColumnName(i))
						.setCellRenderer(getMusicTableCellReader());
			}
			musicTable.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					// 实现双击歌曲列表的某一首歌曲播放
					int clickCount = e.getClickCount();
					// 确定用户所按得是那个键
					int b = e.getButton();
					// 用户选择的歌曲在列表的索引位置
					int index = getMusicJTable().getSelectedRow();
					// 判断用户是否用鼠标左键双击
					if (clickCount == 2 && b == MouseEvent.BUTTON1) {
						Music music = getMusicTableModel().getMusics().get(
								index);
						getPlayerManager().playMusicFile(music);
					} else if (b == MouseEvent.BUTTON3) {
						// 如果用户按了右键，则弹出右键菜单
						int[] rows = getMusicJTable().getSelectedRows();
						if (rows.length == 1 || rows.length == 0) {
							int row = getMusicJTable().rowAtPoint(e.getPoint());
							getMusicJTable().setRowSelectionInterval(row, row);
							getMusicTableJPopMenu().getPlayJMenuItem()
									.setEnabled(true);
							getMusicTableJPopMenu()
									.getBrowseMusicFileJMenuItem().setEnabled(
											true);
							getMusicTableJPopMenu().getFilePropertieJMenuItem()
									.setEnabled(true);
							getMusicTableJPopMenu().show(getMusicJTable(),
									e.getX(), e.getY());
						} else {
							getMusicTableJPopMenu().getPlayJMenuItem()
									.setEnabled(false);
							getMusicTableJPopMenu()
									.getBrowseMusicFileJMenuItem().setEnabled(
											false);
							getMusicTableJPopMenu().getFilePropertieJMenuItem()
									.setEnabled(false);
							getMusicTableJPopMenu().show(getMusicJTable(),
									e.getX(), e.getY());
						}
					}
				}
			});

			musicTable.addKeyListener(new KeyAdapter() {

				public void keyPressed(KeyEvent e) {
					int rowCount = getMusicJTable().getSelectedRowCount();
					if (rowCount >= 2 && e.getKeyCode() == KeyEvent.VK_CONTROL) {
						getPlayControlJMenu().getPlayJMenuItem().setEnabled(
								false);
					} else if (rowCount == 1
							&& e.getKeyCode() == KeyEvent.VK_CONTROL) {
						getPlayControlJMenu().getPlayJMenuItem().setEnabled(
								true);
					}
				}
			});
		}
		return musicTable;
	}

	private JScrollPane getLeftJScrollPane() {
		if (leftJScrollPane == null) {
			leftJScrollPane = new JScrollPane();
			leftJScrollPane.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.cyan, 1), "播放列表",
					TitledBorder.CENTER, TitledBorder.DEFAULT_JUSTIFICATION,
					new Font("华文彩云", Font.PLAIN, 16), Color.magenta));
			leftJScrollPane.setViewportView(getPlayList());
		}
		return leftJScrollPane;
	}

	private JScrollPane getRightJScrollPane() {
		if (rightJScrollPane == null) {
			rightJScrollPane = new JScrollPane();
			rightJScrollPane.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.cyan, 1), "歌曲列表",
					TitledBorder.CENTER, TitledBorder.DEFAULT_JUSTIFICATION,
					new Font("华文彩云", Font.PLAIN, 16), Color.magenta));
			rightJScrollPane.setViewportView(getMusicJTable());
			rightJScrollPane.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					int b = e.getButton();
					if (b == MouseEvent.BUTTON3) {
						getMusicTableJPopMenu().getPlayJMenuItem().setEnabled(
								false);
						getMusicTableJPopMenu().getBrowseMusicFileJMenuItem()
								.setEnabled(false);
						getMusicTableJPopMenu().getFilePropertieJMenuItem()
								.setEnabled(false);
						getMusicTableJPopMenu().show(getRightJScrollPane(),
								e.getX(), e.getY());
					}
				}
			});
		}
		return rightJScrollPane;
	}

	private JSplitPane getMusicJSplitPane() {
		if (musicJSplitPane == null) {
			musicJSplitPane = new JSplitPane();
			musicJSplitPane.setDividerSize(5);
			musicJSplitPane.setDividerLocation(100);
			musicJSplitPane.add(getLeftJScrollPane(), JSplitPane.LEFT);
			musicJSplitPane.add(getRightJScrollPane(), JSplitPane.RIGHT);
		}
		return musicJSplitPane;
	}

	private JPanel getCenterJPanel() {
		if (centerJPanel == null) {
			centerJPanel = new JPanel(new BorderLayout());
			centerJPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLoweredBevelBorder(), BorderFactory
							.createLineBorder(Color.green, 2)));
			centerJPanel.add(getMusicJSplitPane());
		}
		return centerJPanel;
	}

	private JPanel getTopComponentsJPanel() {
		if (topComponentsJPanel == null) {
			topComponentsJPanel = new JPanel(new BorderLayout());
			topComponentsJPanel.add(getMusicPlayingJSlider(),
					BorderLayout.NORTH);
			topComponentsJPanel.add(getLabelAndButtonJPanel(),
					BorderLayout.CENTER);
		}
		return topComponentsJPanel;
	}

	public JButton getPlayOrPauseJButton() {
		if (playOrPauseJButton == null) {
			playOrPauseJButton = new JButton();
			playOrPauseJButton.setBorder(null);
			playOrPauseJButton.setIcon(new ImageIcon(getClass().getResource(
					"/images/play.png")));
			playOrPauseJButton.setText("播放");
			playOrPauseJButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			playOrPauseJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getPlayOrPauseJButton().getText().equals("播放")) {
						int index = getMusicJTable().getSelectedRow();
						if (index == -1) {
							JOptionPane.showMessageDialog(getMainFrame(),
									"请选择一首歌曲，再按播放键！！！", "警告",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						playUserChooseOperation();
					} else if (getPlayOrPauseJButton().getText().equals("暂停")) {
						getPlayerManager().pauseMusicFile();
					} else {
						getPlayerManager().resumeMusicFile();
					}
				}
			});
		}
		return playOrPauseJButton;
	}

	private JButton getNextJButton() {
		if (nextJButton == null) {
			nextJButton = new JButton();
			nextJButton.setBorder(BorderFactory
					.createLineBorder(Color.white, 1));
			nextJButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			nextJButton.setIcon(new ImageIcon(getClass().getResource(
					"/images/next.png")));
			nextJButton.setText("下一首");
			nextJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getPlayerManager().nextMusicFile();
				}
			});
		}
		return nextJButton;
	}

	private JButton getPrevJButton() {
		if (prevJButton == null) {
			prevJButton = new JButton();
			prevJButton.setBorder(BorderFactory
					.createLineBorder(Color.white, 1));
			prevJButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			prevJButton.setIcon(new ImageIcon(getClass().getResource(
					"/images/prev.png")));
			prevJButton.setText("上一首");
			prevJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getPlayerManager().prevMusicFile();
				}
			});
		}
		return prevJButton;
	}

	private JButton getOpenFileJButton() {
		if (openFileJButton == null) {
			openFileJButton = new JButton();
			openFileJButton.setBorder(BorderFactory.createLineBorder(
					Color.white, 1));
			openFileJButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			openFileJButton.setIcon(new ImageIcon(getClass().getResource(
					"/images/openFile.png")));
			openFileJButton.setText("打开文件");
			openFileJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int oldSize = getMusicTableModel().getMusics().size();
					addFileOperation(e);
					if (getMusicJTable().getSelectedRow() != oldSize) {
						return;
					}
					playUserChooseOperation();
				}
			});
		}
		return openFileJButton;
	}

	private JButton getStopJButton() {
		if (stopJButton == null) {
			stopJButton = new JButton();
			stopJButton.setBorder(BorderFactory
					.createLineBorder(Color.white, 1));
			stopJButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			stopJButton.setIcon(new ImageIcon(getClass().getResource(
					"/images/stop.png")));
			stopJButton.setText("停止");
			stopJButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getPlayerManager().stopMusicFile();
				}
			});
		}
		return stopJButton;
	}

	private JToolBar getButtonJToolBar() {
		if (buttonJToolBar == null) {
			buttonJToolBar = new JToolBar(JToolBar.HORIZONTAL);
			buttonJToolBar.setBorderPainted(true);
			buttonJToolBar.add(new JToolBar.Separator());
			buttonJToolBar.add(getPlayOrPauseJButton());
			buttonJToolBar.add(getStopJButton());
			buttonJToolBar.add(getNextJButton());
			buttonJToolBar.add(getPrevJButton());
			buttonJToolBar.add(getOpenFileJButton());
			buttonJToolBar.setFloatable(false);
		}
		return buttonJToolBar;
	}

	public JLabel getPlayingTimeJLabel() {
		if (playingTimeJLabel == null) {
			playingTimeJLabel = new JLabel();
			playingTimeJLabel.setText("00:00");
		}
		return playingTimeJLabel;
	}

	public JLabel getTotalTimeJLabel() {
		if (totalTimeJLabel == null) {
			totalTimeJLabel = new JLabel();
			totalTimeJLabel.setText("00:00");
		}
		return totalTimeJLabel;
	}

	private JPanel getLabelAndButtonJPanel() {
		if (labelAndButtonJPanel == null) {
			labelAndButtonJPanel = new JPanel();
			labelAndButtonJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			labelAndButtonJPanel.add(getPlayingTimeJLabel());
			labelAndButtonJPanel.add(new JLabel("/"));
			labelAndButtonJPanel.add(getTotalTimeJLabel());
			labelAndButtonJPanel.add(getButtonJToolBar());
		}
		return labelAndButtonJPanel;
	}

	public JSlider getMusicPlayingJSlider() {
		if (musicPlayingJSlider == null) {
			musicPlayingJSlider = new JSlider();
			musicPlayingJSlider.setValue(0);
			musicPlayingJSlider.setPaintLabels(true);
			musicPlayingJSlider.setPaintTicks(true);
			musicPlayingJSlider.setPaintTrack(true);
			musicPlayingJSlider.setMinorTickSpacing(10);
		}
		return musicPlayingJSlider;
	}

	private PlayControlJMenu getPlayControlJMenu() {
		if (playControlJMenu == null) {
			playControlJMenu = new PlayControlJMenu(this);
			playControlJMenu.setText("播放控制");
		}
		return playControlJMenu;
	}

	public EditMusicTableJMenu getEditMusicTableJMenu() {
		if (editMusicTableJMenu == null) {
			editMusicTableJMenu = new EditMusicTableJMenu(this);
			editMusicTableJMenu.setText("编辑列表");
		}
		return editMusicTableJMenu;
	}


	/**
	 * 添加文件操作
	 * 
	 * @param e
	 *            触发添加文件操作的事件
	 */
	public void addFileOperation(ActionEvent e) {
		String Path = props.getProperty("lastFileAddingPath");
		JFileChooser chooser = new JFileChooser(Path);
		String[] musicTypes = props.getProperty("supportMusicType").split(",");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("音乐文件",musicTypes);
		chooser.setFileFilter(filter);
		Object o = e.getSource();
		if (o instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) o;
			if (item.getText().equals("添加文件")) {
				chooser.setMultiSelectionEnabled(true);
			} else {
				chooser.setMultiSelectionEnabled(false);
			}
		} else {
			chooser.setMultiSelectionEnabled(false);
		}
		int returnVal = chooser.showOpenDialog(getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// 先得到插入的第一首歌的索引位置
			int index0 = getMusicTableModel().getMusics().size();
			File[] files = null;
			if (chooser.isMultiSelectionEnabled()) {
				files = chooser.getSelectedFiles();
			} else {
				File file = chooser.getSelectedFile();
				files = new File[1];
				files[0] = file;
			}
			PlayList playList = getPlayListModel().getPlayLists().get(
					getPlayList().getSelectedIndex());
			getPlayerDaoManager().addMusicFile(files, playList);
			getMusicTableModel().setMusics(playerDaoManager.getMusics());
			int index1 = index0 + files.length - 1;
			getMusicJTable().setRowSelectionInterval(index0, index1);
			musicTableAutoScroll();
			// 记录添加的文件的文件路径
			String recordPath = files[0].getAbsolutePath().substring(0,
					files[0].getAbsolutePath().lastIndexOf("\\"));
			props.setProperty("lastFileAddingPath", recordPath);
		}
	}

	/**
	 * 播放用户选中的歌曲
	 */
	public void playUserChooseOperation() {
		// 获得用户选中的歌曲在列表的索引位置
		int index = getMusicJTable().getSelectedRow();
		if (index != -1) {
			Music music = getMusicTableModel().getMusics().get(index);
			getPlayerManager().playMusicFile(music);
		}
	}
	

	private JMenuItem getExitSystemJMenuItem() {
		if (exitSystemJMenuItem == null) {
			exitSystemJMenuItem = new JMenuItem();
			exitSystemJMenuItem.setText("退出系统");
			exitSystemJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					beforeExitSystemOperation();
					getMainFrame().setVisible(false);
					System.exit(0);
				}
			});
		}
		return exitSystemJMenuItem;
	}

	private JMenuItem getOpenFileJMenuItem() {
		if (openFileJMenuItem == null) {
			openFileJMenuItem = new JMenuItem();
			openFileJMenuItem.setText("打开文件");
			openFileJMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_O, Event.CTRL_MASK, true));
			openFileJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int oldSize = getMusicTableModel().getMusics().size();
					addFileOperation(e);
					if (getMusicJTable().getSelectedRow() != oldSize) {
						return;
					}
					playUserChooseOperation();
				}
			});
		}
		return openFileJMenuItem;
	}

	private JMenuItem getAddFileJMenuItem() {
		if (addFileJMenuItem == null) {
			addFileJMenuItem = new JMenuItem();
			addFileJMenuItem.setText("添加文件");
			addFileJMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_F, Event.CTRL_MASK, true));
			addFileJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					addFileOperation(e);
				}
			});
		}
		return addFileJMenuItem;
	}

	private JMenu getFileJMenu() {
		if (fileJMenu == null) {
			fileJMenu = new JMenu();
			fileJMenu.setText("文件");
			fileJMenu.add(getOpenFileJMenuItem());
			fileJMenu.add(getAddFileJMenuItem());
			fileJMenu.add(getAddDirectoryJMenuItem());
			fileJMenu.addSeparator();
			fileJMenu.add(getExitSystemJMenuItem());
		}
		return fileJMenu;
	}

	private JMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new JMenuBar();
			mainMenuBar.add(getFileJMenu());
			mainMenuBar.add(getMusicLoopModeJMenu());
			mainMenuBar.add(getPlayControlJMenu());
			mainMenuBar.add(getEditMusicTableJMenu());
		}
		return mainMenuBar;
	}

	private JPanel getNorthJPanel() {
		if (northJPanel == null) {
			northJPanel = new JPanel();
			northJPanel.setLayout(new BorderLayout());
			northJPanel.add(getMainMenuBar(), BorderLayout.NORTH);
			northJPanel.add(getTopComponentsJPanel());
			northJPanel.setVisible(true);
		}
		return northJPanel;
	}

	/**
	 * 在系统退出前的相关操作
	 */
	private void beforeExitSystemOperation() {
		String musicListPath = getPlayerManager().getPlayingMusicListPath();
		int musicIndex = getPlayerManager().getPlayingMusicIndex();
		props.setProperty("lastPlayingMusicIndex", musicIndex + "");
		props.setProperty("lastMusicListPath", musicListPath);
		try {
			OutputStream os = new FileOutputStream(
					"file/RecordOfPlayer.properties");
			props.store(os, null);
			os.flush();
			os.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public JFrame getMainFrame() {
		if (mainFrame == null) {
			mainFrame = new JFrame();
			mainFrame.setSize(WIDTH, HEIGHT);
			mainFrame.setLocation(100,100);
			mainFrame.setLayout(new BorderLayout());
			mainFrame.add(getNorthJPanel(), BorderLayout.NORTH);
			mainFrame.add(getCenterJPanel(), BorderLayout.CENTER);
			mainFrame.setTitle("KKplayer");
			mainFrame.setUndecorated(false);
			mainFrame.setResizable(false);
			mainFrame.setIconImage(new ImageIcon(getClass().getResource(
					"/images/BigIcon.png")).getImage());
			mainFrame.addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					beforeExitSystemOperation();
					mainFrame.setVisible(false);
					System.exit(0);
				}

				public void windowIconified(WindowEvent e) {
					try {
						tray.add(trayIcon);
						getMainFrame().dispose();
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
			});
			tray();
			// 初始化mainFrame时自动播放记录的歌曲
			List<PlayList> playLists = getPlayerDaoManager().getPlayLists();
			for (int i = 0; i < playLists.size(); i++) {
				String musicListPath = playLists.get(i).getMusicListPath();
				if (musicListPath.equals(props.get("lastMusicListPath"))) {
					getPlayList().setSelectedIndex(i);
					break;
				}
			}
			int musicIndex = 0;
			if (!props.get("lastPlayingMusicIndex").toString().trim()
					.equals("")) {
				musicIndex = Integer.parseInt(props
						.get("lastPlayingMusicIndex").toString());
			}
			if (musicIndex < getMusicTableModel().getMusics().size()
					&& musicIndex != -1) {
				Music music = getMusicTableModel().getMusics().get(musicIndex);
				getPlayerManager().playMusicFile(music);
				musicTableAutoScroll();
			}
		}
		return mainFrame;
	}

	public static void main(String[] args) {
		KKPlayerClient playerClient = new KKPlayerClient();
		playerClient.getMainFrame().setVisible(true);
	}

	private void tray() {
		tray = SystemTray.getSystemTray();// 获得本操作的系统托盘
		ImageIcon icon = new ImageIcon(getClass().getResource(
				"/images/icon.png"));
		PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
		MenuItem show = new MenuItem("打开播放器");
		MenuItem exit = new MenuItem("退出播放器");
		MenuItem prev = new MenuItem("上一首");
		MenuItem next = new MenuItem("下一首");
		pop.add(show);
		pop.add(prev);
		pop.add(next);
		pop.add(exit);
		trayIcon = new TrayIcon(icon.getImage(), "", pop);

		/**
		 * 添加鼠标监听器，当鼠标在托盘图标上双击时，默认显示窗口
		 */
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 鼠标双击
					tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
					getMainFrame().setExtendedState(JFrame.NORMAL);
					getMainFrame().setVisible(true); // 显示窗口
					getMainFrame().toFront();
					musicTableAutoScroll();
				}
			}

		});
		show.addActionListener(new ActionListener() { // 点击“显示窗口”菜单后将窗口显示出来
					public void actionPerformed(ActionEvent e) {
						tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
						getMainFrame().setExtendedState(JFrame.NORMAL);
						getMainFrame().setVisible(true); // 显示窗口
						getMainFrame().toFront();
						musicTableAutoScroll();
					}
				});
		exit.addActionListener(new ActionListener() { // 点击“退出演示”菜单后退出程序
					public void actionPerformed(ActionEvent e) {
						beforeExitSystemOperation();
						getMainFrame().setVisible(false);
						System.exit(0); // 退出程序
					}
				});

		prev.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getPlayerManager().prevMusicFile();
			}
		});

		next.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getPlayerManager().nextMusicFile();
			}
		});
	}
	
	public JMenuItem getAddDirectoryJMenuItem()
	{
		if(addDirectoryJMenuItem == null)
		{
			addDirectoryJMenuItem = new JMenuItem();
			addDirectoryJMenuItem.setText("添加文件夹");
			addDirectoryJMenuItem.addActionListener(new ActionListener() 
			{
				
				public void actionPerformed(ActionEvent e) 
				{	
					//获取最后添加的路径
					String Path = props.getProperty("lastFileAddingPath");
					String[] musicTypes = props.getProperty("supportMusicType").split(",");
					JFileChooser jfc = new JFileChooser(Path);
					jfc.setDialogType(JFileChooser.OPEN_DIALOG);
					jfc.setDialogTitle("添加文件夹");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = jfc.showDialog(getMainFrame(), "选择");
					if(returnVal == JFileChooser.APPROVE_OPTION)
					{	
						//起始位置
						int index0 = getMusicTableModel().getMusics().size();
						//首先定义添加文件的容器，未添加时的起始位置
						List<File> files = new ArrayList<File>();
						File dir = jfc.getSelectedFile();
						props.setProperty("lastFileAddingPath", dir.getAbsolutePath());
						addDirectoryMusic(dir,files,musicTypes);
						PlayList playList = getPlayListModel().getPlayLists().get(
								getPlayList().getSelectedIndex());
						getPlayerDaoManager().addMusicFile(files.toArray(new File[files.size()]), playList);
						getMusicTableModel().setMusics(getPlayerDaoManager().getMusics());
						//添加后的位置
						int index1 = index0 + files.size() -1;
						//选中添加的文件
						if(index1 > index0)
						{
							getMusicJTable().setRowSelectionInterval(index0, index1);
							musicTableAutoScroll();
						}
						
					}
				}
			});
		}
		return addDirectoryJMenuItem;
	}
	
	private void addDirectoryMusic(File dir,List<File> files, String[] musicTypes)
	{
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) 
		{
			if(fs[i].isFile())
			{	
				String suffix = fs[i].getAbsolutePath().substring(fs[i].getAbsolutePath().lastIndexOf(".") + 1);
				for (int j = 0; j < musicTypes.length; j++) 
				{
					if(musicTypes[j].equals(suffix))
					{
						files.add(fs[i]);
						break;
					}
				}
			}
			else
			{
				addDirectoryMusic(fs[i], files,musicTypes);
			}
		}
	}
}
