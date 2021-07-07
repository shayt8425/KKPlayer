package com.zhao.KKPlayer.gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.JButton;


public class RenameFileToCustomFormatView extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel firstjPanel = null;
	private JPanel secondjPanel = null;
	private JPanel thirdjPanel = null;
	private JPanel fourthjPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel2 = null;
	private JTextField jTextField = null;
	private JTextField jTextField1 = null;
	private JLabel jLabel1 = null;
	private JTextField jTextField2 = null;
	private JLabel jLabel3 = null;
	private JTextField jTextField3 = null;
	private JLabel jLabel41 = null;
	private JTextField jTextField4 = null;
	private JLabel jLabel42 = null;
	private JTextField jTextField5 = null;
	private JTextField jTextField6 = null;
	private JPanel buttonjPanel = null;
	private JButton surejButton = null;
	private JButton canceljButton = null;
	private KKPlayerClient playerClient = null;

	/**
	 * @param owner
	 */
	public RenameFileToCustomFormatView(KKPlayerClient playerClient,
			boolean b) {
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
		this.setSize(400, 300);
		this.setTitle("自定义重命名格式");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(6);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
			jContentPane.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createCompoundBorder(BorderFactory
							.createBevelBorder(BevelBorder.RAISED),
							BorderFactory
									.createBevelBorder(BevelBorder.LOWERED)),
					"格式", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
					new Font("华文彩云", Font.PLAIN, 14), Color.magenta));
			jContentPane.add(getJTextField6(), null);
			jContentPane.add(getFourthjPanel(), null);
			jContentPane.add(getThirdjPanel(), null);
			jContentPane.add(getSecondjPanel(), null);
			jContentPane.add(getFirstjPanel(), null);
			jContentPane.add(getButtonjPanel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes firstjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFirstjPanel() {
		if (firstjPanel == null) {
			jLabel42 = new JLabel();
			jLabel42.setText("示例: ");
			firstjPanel = new JPanel();
			firstjPanel.setLayout(new FlowLayout());
			firstjPanel.add(jLabel42, null);
			firstjPanel.add(getJTextField5(), null);
		}
		return firstjPanel;
	}

	/**
	 * This method initializes secondjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecondjPanel() {
		if (secondjPanel == null) {
			jLabel41 = new JLabel();
			jLabel41.setText("发行时间:");
			secondjPanel = new JPanel();
			secondjPanel.setLayout(new FlowLayout());
			secondjPanel.add(jLabel41, null);
			secondjPanel.add(getJTextField4(), null);
		}
		return secondjPanel;
	}

	/**
	 * This method initializes thirdjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getThirdjPanel() {
		if (thirdjPanel == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("音轨号:");
			jLabel1 = new JLabel();
			jLabel1.setText("专辑名:");
			thirdjPanel = new JPanel();
			thirdjPanel.setLayout(new FlowLayout());
			thirdjPanel.add(jLabel1, null);
			thirdjPanel.add(getJTextField2(), null);
			thirdjPanel.add(jLabel3, null);
			thirdjPanel.add(getJTextField3(), null);
		}
		return thirdjPanel;
	}

	/**
	 * This method initializes fourthjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFourthjPanel() {
		if (fourthjPanel == null) {

			jLabel2 = new JLabel();
			jLabel2.setText("歌曲标题:");
			jLabel = new JLabel();
			jLabel.setText("艺术家:");
			fourthjPanel = new JPanel();
			fourthjPanel.setLayout(new FlowLayout());
			fourthjPanel.add(jLabel, null);
			fourthjPanel.add(getJTextField(), null);
			fourthjPanel.add(jLabel2, null);
			fourthjPanel.add(getJTextField1(), null);
		}
		return fourthjPanel;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setText("%Artist%");
			jTextField.setEditable(false);
			jTextField.setColumns(11);
		}
		return jTextField;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
			jTextField1.setText("%Title%");
			jTextField1.setEditable(false);
			jTextField1.setColumns(11);
		}
		return jTextField1;
	}

	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField2() {
		if (jTextField2 == null) {
			jTextField2 = new JTextField();
			jTextField2.setColumns(11);
			jTextField2.setEditable(false);
			jTextField2.setText("%Album%");
		}
		return jTextField2;
	}

	/**
	 * This method initializes jTextField3
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField3() {
		if (jTextField3 == null) {
			jTextField3 = new JTextField();
			jTextField3.setColumns(12);
			jTextField3.setEditable(false);
			jTextField3.setText("%TrackNumber%");
		}
		return jTextField3;
	}

	/**
	 * This method initializes jTextField4
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField4() {
		if (jTextField4 == null) {
			jTextField4 = new JTextField();
			jTextField4.setColumns(27);
			jTextField4.setEditable(false);
			jTextField4.setText("%Year%");
		}
		return jTextField4;
	}

	/**
	 * This method initializes jTextField5
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField5() {
		if (jTextField5 == null) {
			jTextField5 = new JTextField();
			jTextField5.setColumns(29);
			jTextField5.setEditable(false);
			jTextField5.setText("%Artist% - %Album%表示命名的格式为”艺术家 - 专辑\"");
		}
		return jTextField5;
	}

	/**
	 * This method initializes jTextField6
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField6() {
		if (jTextField6 == null) {
			jTextField6 = new JTextField();
			jTextField6.addKeyListener(new KeyAdapter() {
				
				public void keyPressed(KeyEvent e) {
					int keyCode = e.getKeyCode();
					if(keyCode == KeyEvent.VK_ENTER){
						renameMusicFileName();
					}else if(keyCode == KeyEvent.VK_ESCAPE){
						setVisible(false);
					}
				}
				
			});
		}
		return jTextField6;
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
					renameMusicFileName();
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
					setVisible(false);
				}
			});
		}
		return canceljButton;
	}
	
	private void renameMusicFileName(){
		int[] musicIndexs = playerClient.getMusicJTable().getSelectedRows();
		if(musicIndexs.length==0){
			JOptionPane.showMessageDialog(playerClient
					.getMainFrame(), "请至少选择一首歌曲，再执行重命名操作！！", "警告",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		String format = getJTextField6().getText();
		String[] parameters = playerClient.getPlayerManager().getFormatParameters(format);
		playerClient.getPlayerDaoManager().renameMusicFile(musicIndexs, parameters);
		setVisible(false);
	}
}