package com.zhao.KKPlayer.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.zhao.KKPlayer.model.PlayList;


public class renamePlayListView extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private KKPlayerClient playerClient;
	private JPanel centerjPanel = null;
	private JPanel buttonjPanel = null;
	private JButton surejButton = null;
	private JButton canceljButton = null;
	private JLabel jLabel1 = null;
	private JTextField jTextField = null;

	/**
	 * @param owner
	 */
	public renamePlayListView(KKPlayerClient playerClient, boolean b) {
		super(playerClient.getMainFrame(), b);
		this.playerClient = playerClient;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("重命名播放列表");
		this.setContentPane(getJContentPane());
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
			jContentPane.add(getCenterjPanel(), BorderLayout.CENTER);
			jContentPane.add(getButtonjPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes centerjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterjPanel() {
		if (centerjPanel == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("请输入一个新的歌曲列表名字");
			jLabel1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			centerjPanel = new JPanel();
			centerjPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createBevelBorder(BevelBorder.LOWERED)), "播放列表", TitledBorder.CENTER, TitledBorder.BELOW_TOP, new Font("华文彩云", Font.PLAIN, 14), Color.magenta));
			centerjPanel.setLayout(gridLayout);
			centerjPanel.add(jLabel1, null);
			centerjPanel.add(getJTextField(), null);
		}
		return centerjPanel;
	}

	/**
	 * This method initializes buttonjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonjPanel() {
		if (buttonjPanel == null) {
			buttonjPanel = new JPanel();
			buttonjPanel.setLayout(new FlowLayout());
			buttonjPanel.add(getSurejButton(), null);
			buttonjPanel.add(getCanceljButton(), null);
		}
		return buttonjPanel;
	}

	/**
	 * This method initializes surejButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSurejButton() {
		if (surejButton == null) {
			surejButton = new JButton();
			surejButton.setText("确定");
			surejButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//获得存放播放列表数据模型的容器
					List<PlayList> playLists = playerClient.getPlayListModel().getPlayLists();
					String text = getJTextField().getText().trim();
					//获得当前用户选择的播放列表数据模型
					int index = playerClient.getPlayList().getSelectedIndex();
					PlayList playList = playLists.get(index);
					if(text!=null&&!text.equals("")){
						for (int i = 0; i < playLists.size(); i++) {
							if(playLists.get(i).getName().equals(text)&&!text.equals(playList.getName())){
								JOptionPane.showMessageDialog(playerClient.getMainFrame(), "已存在一个同名的播放列表，请取一个其他的名字！！","警告",JOptionPane.WARNING_MESSAGE);
								return;
							}
						}
						File file = new File(playList.getMusicListPath());
						playList.setName(text);
						playList.setMusicListPath("file/"+text+".xml");
						//重命名相关的文件
						playerClient.getPlayerDaoManager().renamePlayList(playList);
						playerClient.getPlayListModel().propertyChange(index, index);
						getJTextField().setText("");
						setVisible(false);
						file.renameTo(new File(playList.getMusicListPath()));
					}
				}
			});
		}
		return surejButton;
	}

	/**
	 * This method initializes canceljButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCanceljButton() {
		if (canceljButton == null) {
			canceljButton = new JButton();
			canceljButton.setText("取消");
			canceljButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					getJTextField().setText("");
					setVisible(false);
				}
			});
		}
		return canceljButton;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
