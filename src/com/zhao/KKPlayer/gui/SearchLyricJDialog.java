package com.zhao.KKPlayer.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;

import javax.swing.JDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdom.Element;

import com.zhao.KKPlayer.tag.MusicTag;
import com.zhao.KKPlayer.util.PlayerUtil;
import javax.swing.JCheckBox;

public class SearchLyricJDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JTextField artistJTextField = null;
	private JLabel jLabel1 = null;
	private JTextField titleJTextField = null;
	private JButton jButton = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JLabel stateJLabel = null;
	private JScrollPane jScrollPane = null;
	private JTable resultJTable = null;
	private JPanel jPanel3 = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JPanel jPanel4 = null;
	private JLabel jLabel3 = null;
	private JTextField saveAsJTextField = null;
	private LyricSearchResult lyricSearchResult;
	private KKPlayerClient playerClient;
	private JCheckBox jCheckBox = null;
	private JLabel jLabel4 = null;

	public LyricSearchResult getLyricSearchResult() {
		if (lyricSearchResult == null) {
			lyricSearchResult = new LyricSearchResult();
		}
		return lyricSearchResult;
	}

	/**
	 * @param owner
	 */
	public SearchLyricJDialog(MusicLyricShow musicLyricShow,
			KKPlayerClient playerClient, boolean b) {
		super(musicLyricShow, b);
		this.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		this.playerClient = playerClient;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(420, 300);
		this.setContentPane(getJContentPane());
		this.setTitle("歌词搜索");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
			jContentPane.add(getJPanel1(), BorderLayout.CENTER);
			jContentPane.add(getJPanel3(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("标题：");
			jLabel = new JLabel();
			jLabel.setText("歌手：");
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			jPanel = new JPanel();
			jPanel.setLayout(flowLayout);
			jPanel.add(jLabel, null);
			jPanel.add(getArtistJTextField(), null);
			jPanel.add(jLabel1, null);
			jPanel.add(getTitleJTextField(), null);
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getArtistJTextField() {
		if (artistJTextField == null) {
			artistJTextField = new JTextField();
			artistJTextField.setColumns(10);
		}
		return artistJTextField;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getTitleJTextField() {
		if (titleJTextField == null) {
			titleJTextField = new JTextField();
			titleJTextField.setColumns(10);
		}
		return titleJTextField;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("搜索");
			jButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {

						public void run() {
							stateJLabel.setText("正在搜索相关歌词");
							// 去掉空格\<>等字符，去掉()或者（）中的内容
							String artist = getArtistJTextField().getText()
									.toLowerCase().replaceAll(
											"(?<=\\().+(?=\\))|(?<=（).+(?=）)",
											"").replaceAll(
											"[!-/:-@{-~\\[-`\\s]", "");
							String title = getTitleJTextField().getText()
									.toLowerCase().replaceAll(
											"(?<=\\().+(?=\\))|(?<=（).+(?=）)",
											"").replaceAll(
											"[!-/:-@{-~\\[-`\\s]", "");
							try {
								List<Element> datas = PlayerUtil.searchLyrics(
										artist, title);
								getLyricSearchResult().setDatas(datas);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(
										SearchLyricJDialog.this,
										"无法完成歌词搜索，可能是网络无连接！！！", "错误",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							getLyricSearchResult().fireTableDataChanged();
							if (getLyricSearchResult().getDatas().size() != 0) {
								getSaveAsJTextField().setText(
										playerClient.getPlayerManager()
												.getPlayingMusic().getName()
												+ ".lrc");
							}
							if (getLyricSearchResult().getDatas().size() == 0) {
								stateJLabel.setText("没有搜索到相关的歌词文件！！");
							} else {
								stateJLabel.setText("选择搜索到得歌词文件进行下载：");
							}
						}
					}).start();
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 * @throws FileNotFoundException
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJPanel2(), BorderLayout.NORTH);
			jPanel1.add(getJScrollPane(), BorderLayout.CENTER);
			jPanel1.add(getJPanel4(), BorderLayout.SOUTH);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.LEFT);
			jPanel2 = new JPanel();
			jPanel2.setLayout(flowLayout1);
			jPanel2.add(getStateJLable());
		}
		return jPanel2;
	}

	public JLabel getStateJLable() {
		if (stateJLabel == null) {
			stateJLabel = new JLabel();
		}
		return stateJLabel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getResultJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getResultJTable() {
		if (resultJTable == null) {
			resultJTable = new JTable(getLyricSearchResult());
			resultJTable.setRowHeight(20);
			resultJTable.setFont(new Font("sss", Font.BOLD, 12));
			resultJTable
					.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			resultJTable.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					int b = e.getButton();
					int clickCount = e.getClickCount();
					if (b == 1 && clickCount == 2) {
						new Thread(new Runnable() {

							public void run() {
								lyricdownload();
							}
						}).start();
					}
				}
			});
		}
		return resultJTable;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jLabel4 = new JLabel();
			jLabel4
					.setText("是否嵌入到音频文件                                          ");
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(FlowLayout.RIGHT);
			jPanel3 = new JPanel();
			jPanel3.setLayout(flowLayout2);
			jPanel3.add(getJCheckBox(), null);
			jPanel3.add(jLabel4, null);
			jPanel3.add(getJButton1(), null);
			jPanel3.add(getJButton2(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("下载");
			jButton1.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					new Thread(new Runnable() {

						public void run() {
							lyricdownload();
						}
					}).start();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("关闭");
			jButton2.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButton2;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("保存为：");
			jPanel4 = new JPanel();
			jPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
			jPanel4.add(jLabel3, null);
			jPanel4.add(getSaveAsJTextField(), null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getSaveAsJTextField() {
		if (saveAsJTextField == null) {
			saveAsJTextField = new JTextField();
			saveAsJTextField.setColumns(31);
		}
		return saveAsJTextField;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
		}
		return jCheckBox;
	}

	private void lyricdownload() {
		int index = getResultJTable().getSelectedRow();
		if (index == -1) {
			JOptionPane.showMessageDialog(SearchLyricJDialog.this,
					"请至少选择一个歌曲，在进行下载操作！！！！", "警告", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Element lrc = getLyricSearchResult().getDatas().get(index);
		getStateJLable().setText("正在下载相关歌词文件");
		// 生成千千静听的加密的code
		String code = PlayerUtil.CreateQianQianCode(lrc
				.getAttributeValue("artist"), lrc.getAttributeValue("title"),
				Integer.parseInt(lrc.getAttributeValue("id")));
		String spec = "http://ttlrcct2.qianqian.com/dll/lyricsvr.dll?dl?Id="
				+ lrc.getAttributeValue("id") + "&Code=" + code;
		try {
			URL url = new URL(spec);
			InputStream is = url.openStream();
			String path = playerClient.getPlayerManager().getPlayingMusic()
					.getPath();
			byte[] buff = new byte[1024]; // 定义一个与字节流可用数相同的字节数组
			int len = 0;
			String lyric = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = is.read(buff)) != -1) {
				baos.write(buff,0,len);
			}
			lyric = new String(baos.toByteArray(), "utf-8");
			lyric = lyric.replaceAll("=", "");
			is.close();
			if (lyric.indexOf("Search ID or Code error!") != -1) {
				stateJLabel.setText("不能下载相关的歌词！！");
			} else {
				stateJLabel.setText("下载歌词成功！！");
				String lyricPath = path.substring(0, path
						.lastIndexOf(File.separator))
						+ File.separator + getSaveAsJTextField().getText();
				OutputStream os = new FileOutputStream(lyricPath);
				os.write(lyric.getBytes("UTF-8"));
				os.flush();
				os.close();
				String musicName = playerClient.getPlayerManager()
						.getPlayingMusic().getName();
				// 如果用户保存的文件名与歌曲的名字不一样，建立关联
				if (!getSaveAsJTextField().getText().substring(0,
						getSaveAsJTextField().getText().lastIndexOf("."))
						.equalsIgnoreCase(musicName)) {
					playerClient.getPlayerDaoManager().setMusicLyricPath(
							playerClient.getPlayerManager().getPlayingMusic()
									.getId(), lyricPath);
				}
				// 判断用户是否将下载的歌词嵌入到音乐文件中
				if (getJCheckBox().isSelected()) {
					// 保存歌词
					MusicTag tag = PlayerUtil.recogniseMusicTag(playerClient
							.getPlayerManager().getPlayingMusic(),playerClient);
					tag.saveLyric(lyric);
				}
				playerClient.getPlayerManager().reloadLyricShow(
						playerClient.getPlayerManager().getControlThread()
								.getPlayingTime(), lyric);
				
				playerClient.getPlayerManager().getPlayingMusic().setLyricSetted(true);
				playerClient.getPlayerManager().getPlayingMusic().setLyric(lyric);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
