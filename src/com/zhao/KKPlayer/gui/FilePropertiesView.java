package com.zhao.KKPlayer.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import com.zhao.KKPlayer.model.Music;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilePropertiesView extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel northjPanel = null;
	private JPanel filePathjPanel = null;
	private JLabel jLabel = null;
	private JTextField filePathjTextField = null;
	private JPanel secondjPanel = null;
	private JLabel jLabel1 = null;
	private JTextField musicTitlejTextField = null;
	private JPanel thirdjPanel = null;
	private JLabel jLabel2 = null;
	private JTextField artistjTextField = null;
	private JPanel forthjPanel = null;
	private JLabel jLabel3 = null;
	private JTextField albumjTextField = null;
	private JLabel jLabel4 = null;
	private JTextField trackjTextField = null;
	private JPanel fifthjPanel = null;
	private JLabel jLabel5 = null;
	private JTextField genrejTextField = null;
	private JLabel jLabel6 = null;
	private JTextField yearjTextField = null;
	private JPanel centerjPanel = null;
	private JTextArea commentjTextArea = null;
	private JPanel buttonjPanel = null;
	private JButton prevjButton = null;
	private JButton nextjButton = null;
	private JButton savejButton = null;
	private JButton canceljButton = null;
	private KKPlayerClient playerClient;
	private ImagePanel imgp = null;
	private JButton changeCoverButton = null;
	private JButton saveCoverButton = null;

	public FilePropertiesView(KKPlayerClient playerClient, boolean b) {
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
		this.setSize(570, 480);
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
			jContentPane.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createCompoundBorder(BorderFactory
							.createBevelBorder(BevelBorder.RAISED),
							BorderFactory
									.createBevelBorder(BevelBorder.LOWERED)),
					"音乐标签", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
					new Font("华文彩云", Font.PLAIN, 16), Color.magenta));
			jContentPane.add(getNorthjPanel(), BorderLayout.NORTH);
			jContentPane.add(getCenterjPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes northjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNorthjPanel() {
		if (northjPanel == null) {
			northjPanel = new JPanel();
			northjPanel.setLayout(new BorderLayout());
			//先添加左边相关歌曲信息面板
			JPanel northLeftJp = new JPanel();
			northLeftJp.setLayout(new GridLayout(5,1));
			northjPanel.add(northLeftJp,BorderLayout.CENTER);
			northLeftJp.add(getFilePathjPanel(), null);
			northLeftJp.add(getSecondjPanel(), null);
			northLeftJp.add(getThirdjPanel(), null);
			northLeftJp.add(getForthjPanel(), null);
			northLeftJp.add(getFifthjPanel(), null);
			//再添加右侧专辑封面面板
			northjPanel.add(getImgPanel(),BorderLayout.EAST);
		}
		return northjPanel;
	}
	
	public ImagePanel getImgPanel(){
		if(imgp == null){
			imgp = new ImagePanel(new byte[0]);
			imgp.setPreferredSize(new Dimension(210,200));
		}
		
		return imgp;
	}

	/**
	 * This method initializes filePathjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFilePathjPanel() {
		if (filePathjPanel == null) {
			jLabel = new JLabel();
			jLabel.setText("文件路径：");
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			filePathjPanel = new JPanel();
			filePathjPanel.setLayout(flowLayout);
			filePathjPanel.add(jLabel, null);
			filePathjPanel.add(getFilePathJTextField(), null);
		}
		return filePathjPanel;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getFilePathJTextField() {
		if (filePathjTextField == null) {
			filePathjTextField = new JTextField();
			filePathjTextField.setColumns(22);
			filePathjTextField.setEditable(false);
		}
		return filePathjTextField;
	}

	/**
	 * This method initializes secondjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecondjPanel() {
		if (secondjPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.LEFT);
			jLabel1 = new JLabel();
			jLabel1.setText("歌曲标题：");
			secondjPanel = new JPanel();
			secondjPanel.setLayout(flowLayout1);
			secondjPanel.add(jLabel1, null);
			secondjPanel.add(getMusicTitleJTextField(), null);
		}
		return secondjPanel;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getMusicTitleJTextField() {
		if (musicTitlejTextField == null) {
			musicTitlejTextField = new JTextField(22);
			musicTitlejTextField.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return musicTitlejTextField;
	}

	/**
	 * This method initializes thirdjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getThirdjPanel() {
		if (thirdjPanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("艺 术 家  ：");
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(FlowLayout.LEFT);
			thirdjPanel = new JPanel();
			thirdjPanel.setLayout(flowLayout2);
			thirdjPanel.add(jLabel2, null);
			thirdjPanel.add(getArtistJTextField(), null);
		}
		return thirdjPanel;
	}

	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getArtistJTextField() {
		if (artistjTextField == null) {
			artistjTextField = new JTextField(22);
			artistjTextField.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return artistjTextField;
	}

	/**
	 * This method initializes forthjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getForthjPanel() {
		if (forthjPanel == null) {
			jLabel4 = new JLabel();
			jLabel4.setText(" 音轨号：");
			jLabel3 = new JLabel();
			jLabel3.setText("专 辑 名  ：");
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(FlowLayout.LEFT);
			forthjPanel = new JPanel();
			forthjPanel.setLayout(flowLayout3);
			forthjPanel.add(jLabel3, null);
			forthjPanel.add(getAlbumJTextField(), null);
			forthjPanel.add(jLabel4, null);
			forthjPanel.add(getTrackJTextField(), null);
		}
		return forthjPanel;
	}

	/**
	 * This method initializes jTextField3
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getAlbumJTextField() {
		if (albumjTextField == null) {
			albumjTextField = new JTextField(12);
			albumjTextField.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return albumjTextField;
	}

	/**
	 * This method initializes jTextField4
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getTrackJTextField() {
		if (trackjTextField == null) {
			trackjTextField = new JTextField(4);
			trackjTextField.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return trackjTextField;
	}

	private JPanel getFifthjPanel() {
		if (fifthjPanel == null) {
			jLabel6 = new JLabel();
			jLabel6.setText("发行年份：");
			jLabel5 = new JLabel();
			jLabel5.setText("歌曲流派：");
			FlowLayout flowLayout4 = new FlowLayout();
			flowLayout4.setAlignment(FlowLayout.LEFT);
			fifthjPanel = new JPanel();
			fifthjPanel.setLayout(flowLayout4);
			fifthjPanel.add(jLabel5, null);
			fifthjPanel.add(getGenreJTextField(), null);
			fifthjPanel.add(jLabel6, null);
			fifthjPanel.add(getYearJTextField(), null);
		}
		return fifthjPanel;
	}

	public JTextField getGenreJTextField() {
		if (genrejTextField == null) {
			genrejTextField = new JTextField(10);
			genrejTextField.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return genrejTextField;
	}

	public JTextField getYearJTextField() {
		if (yearjTextField == null) {
			yearjTextField = new JTextField(5);
			yearjTextField.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return yearjTextField;
	}

	/**
	 * This method initializes centerjPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterjPanel() {
		if (centerjPanel == null) {
			centerjPanel = new JPanel();
			centerjPanel.setLayout(new BorderLayout());
			centerjPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createCompoundBorder(BorderFactory
							.createBevelBorder(BevelBorder.RAISED),
							BorderFactory
									.createBevelBorder(BevelBorder.LOWERED)),
					"备  注", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("微软雅黑", Font.PLAIN,
							14), Color.black));
			centerjPanel.add(getCommentJTextArea(), BorderLayout.CENTER);
			centerjPanel.add(getButtonjPanel(), BorderLayout.SOUTH);
		}
		return centerjPanel;
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	public JTextArea getCommentJTextArea() {
		if (commentjTextArea == null) {
			commentjTextArea = new JTextArea();
			commentjTextArea.setBackground(SystemColor.control);
			commentjTextArea.setForeground(new Color(255, 0, 51));
			commentjTextArea.setFont(new Font("华文中宋", Font.PLAIN, 12));
			commentjTextArea.addKeyListener(new KeyAdapter() {

				public void keyTyped(KeyEvent e) {
					getSavejButton().setEnabled(true);
				}
			});
		}
		return commentjTextArea;
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
			buttonjPanel.add(getPrevjButton(), null);
			buttonjPanel.add(getNextjButton(), null);
			buttonjPanel.add(getSavejButton(), null);
			buttonjPanel.add(getCanceljButton(), null);
			buttonjPanel.add(getChangeCoverButton(), null);
			buttonjPanel.add(getSaveCoverButton(), null);
		}
		return buttonjPanel;
	}
	
	private JButton getChangeCoverButton(){
		if(changeCoverButton == null){
			changeCoverButton = new JButton("更换封面");
			changeCoverButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser jfc = new JFileChooser();
					jfc.setFileFilter(new FileNameExtensionFilter("图片文件","gif","jpg","png","JPG","bmp"));
					jfc.setMultiSelectionEnabled(false);
					int returnVal = jfc.showOpenDialog(playerClient.getMusicTableJPopMenu().getFilePropertiesView());
					if(returnVal == JFileChooser.APPROVE_OPTION){
						File f = jfc.getSelectedFile();
						getImgPanel().setData(f.getAbsolutePath());
						getSavejButton().setEnabled(true);
					}
				}
			});
		}
		return changeCoverButton;
	}
	
	private JButton getSaveCoverButton(){
		if(saveCoverButton == null){
			saveCoverButton = new JButton("保存封面");
			saveCoverButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser jfc = new JFileChooser();
					jfc.setFileFilter(new FileNameExtensionFilter("图片文件","gif","jpg","png","JPG","bmp"));
					jfc.setMultiSelectionEnabled(false);
					int returnVal = jfc.showSaveDialog(playerClient.getMusicTableJPopMenu().getFilePropertiesView());
					if(returnVal == JFileChooser.APPROVE_OPTION){
						File f = jfc.getSelectedFile();
						try {
							FileOutputStream fos = new FileOutputStream(f);
							fos.write(getImgPanel().getData());
							fos.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}
		return saveCoverButton;
	}

	/**
	 * This method initializes prevjButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getPrevjButton() {
		if (prevjButton == null) {
			prevjButton = new JButton();
			prevjButton.setText("上一首");
			prevjButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int index = playerClient.getMusicTableModel().getMusics()
							.indexOf(
									playerClient.getMusicTableJPopMenu()
											.getLookingMusic());
					Music music = playerClient.getMusicTableModel().getMusics()
							.get(index - 1);
					playerClient.getMusicTableJPopMenu().lookingMusicPropertie(
							music);
					getSavejButton().setEnabled(false);
					getNextjButton().setEnabled(true);
				}
			});
		}
		return prevjButton;
	}

	/**
	 * This method initializes nextjButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getNextjButton() {
		if (nextjButton == null) {
			nextjButton = new JButton();
			nextjButton.setText("下一首");
			nextjButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int index = playerClient.getMusicTableModel().getMusics()
							.indexOf(
									playerClient.getMusicTableJPopMenu()
											.getLookingMusic());
					Music music = playerClient.getMusicTableModel().getMusics()
							.get(index + 1);
					getSavejButton().setEnabled(false);
					getPrevjButton().setEnabled(true);
					playerClient.getMusicTableJPopMenu().lookingMusicPropertie(
							music);
				}
			});
		}
		return nextjButton;
	}

	/**
	 * This method initializes savejButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getSavejButton() {
		if (savejButton == null) {
			savejButton = new JButton();
			savejButton.setText("保存");
			savejButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Music music = playerClient.getMusicTableJPopMenu()
							.getLookingMusic();
					music.setArtist(getArtistJTextField().getText());
					music.setAlbum(getAlbumJTextField().getText());
					music.setGenre(getGenreJTextField().getText());
					music.setYear(getYearJTextField().getText());
					music.setTrackNumber(getTrackJTextField().getText());
					music.setTitle(getMusicTitleJTextField().getText());
					music.setComment(getCommentJTextArea().getText());
					playerClient.getPlayerDaoManager().save(music);
					playerClient.getMusicTableModel().fireTableDataChanged();
					getSavejButton().setEnabled(false);
				}
			});
		}
		return savejButton;
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
	
}
