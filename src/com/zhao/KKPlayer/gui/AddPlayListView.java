package com.zhao.KKPlayer.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.zhao.KKPlayer.model.PlayList;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class AddPlayListView extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel buttonjPanel = null;
	private JButton addMusicTypejButton = null;
	private JButton canceljButton = null;
	private KKPlayerClient playerClient;
	private JPanel centerjPanel = null;
	private JLabel jLabel = null;
	private JTextField jTextField = null;

	/**
	 * @param owner
	 */
	public AddPlayListView(KKPlayerClient playerClient, boolean b) {
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
		this.setTitle("添加播放列表");
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
			jContentPane.add(getButtonjPanel(), BorderLayout.SOUTH);
			jContentPane.add(getCenterjPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
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
			buttonjPanel.add(getAddMusicTypejButton(), null);
			buttonjPanel.add(getCanceljButton(), null);
		}
		return buttonjPanel;
	}

	/**
	 * This method initializes addMusicTypejButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddMusicTypejButton() {
		if (addMusicTypejButton == null) {
			addMusicTypejButton = new JButton();
			addMusicTypejButton.setText("添加");
			addMusicTypejButton
					.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addPlayList();
						}
					});
		}
		return addMusicTypejButton;
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
			canceljButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getJTextField().setText("");
							setVisible(false);
						}
					});
		}
		return canceljButton;
	}

	/**
	 * This method initializes centerjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterjPanel() {
		if (centerjPanel == null) {
			jLabel = new JLabel();
			jLabel.setText("请输入需要添加的列表名字");
			jLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			centerjPanel = new JPanel();
			centerjPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createCompoundBorder(BorderFactory
							.createBevelBorder(BevelBorder.RAISED),
							BorderFactory
									.createBevelBorder(BevelBorder.LOWERED)),
					"播放列表", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
					new Font("华文彩云", Font.PLAIN, 14), Color.magenta));
			centerjPanel.setLayout(gridLayout);
			centerjPanel.add(jLabel, null);
			centerjPanel.add(getJTextField(), null);
		}
		return centerjPanel;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.addKeyListener(new KeyAdapter() {
				
				public void keyPressed(KeyEvent e) {
					int keyCode = e.getKeyCode();
					if(keyCode == KeyEvent.VK_ENTER){
						addPlayList();
					}else if(keyCode == KeyEvent.VK_ESCAPE){
						getJTextField().setText("");
						setVisible(false);
					}
				}
				
			});
		}
		return jTextField;
	}
	
	private void addPlayList(){
		List<PlayList> playLists = playerClient.getPlayListModel().getPlayLists();
		String text = getJTextField().getText().trim();
		if (text != null && text.equals("") == false) {
			for (int i = 0; i < playLists.size(); i++) {
				if(playLists.get(i).getName().equals(text)){
					JOptionPane.showMessageDialog(playerClient.getMainFrame(), "已存在一个同名的播放列表，请取一个其他的名字！！","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			playerClient.getPlayerDaoManager().addPlayList(
					text, "file/" + text + ".xml");
			playerClient.getPlayList().setSelectedIndex(
					playerClient.getPlayListModel()
							.getPlayLists().size()-1);
			getJTextField().setText("");
			setVisible(false);
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
