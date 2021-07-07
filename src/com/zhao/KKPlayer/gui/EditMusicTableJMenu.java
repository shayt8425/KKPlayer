package com.zhao.KKPlayer.gui;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.zhao.KKPlayer.model.PlayList;

public class EditMusicTableJMenu extends JMenu {
	
	private static final long serialVersionUID = -8894252386992618005L;
	
	private JMenuItem copyMusicJMenuItem;
	private JMenuItem cutMusicJmenuJMenuItem;
	private JMenuItem pasteMusicJMenuItem;
	private JMenuItem deleteMusicJMenuItem;
	private KKPlayerClient playerClient;
	
	public EditMusicTableJMenu(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		this.add(getCopyMusicJMenuItem());
		this.add(getCutMusicJMenuItem());
		this.add(getPasteMusicJMenuItem());
		this.add(getDeleteMusicJMenuItem());
		this.setText("编辑列表");
	}	
	
	private JMenuItem getCopyMusicJMenuItem() {
		if (copyMusicJMenuItem == null) {
			copyMusicJMenuItem = new JMenuItem();
			copyMusicJMenuItem.setText("复制歌曲");
			copyMusicJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
			copyMusicJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int[] musicIndexs = playerClient.getMusicJTable()
							.getSelectedRows();
					if(musicIndexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行复制操作！！", "警告",JOptionPane.WARNING_MESSAGE);
						return;
					}
					playerClient.getPlayerDaoManager().copyMusics(musicIndexs);
					playerClient.getEditMusicTableJMenu().getPasteMusicJMenuItem().setEnabled(true);
					playerClient.getMusicTableJPopMenu().getEditMusicTableJMenu().getPasteMusicJMenuItem().setEnabled(true);
				}
			});
		}
		return copyMusicJMenuItem;
	}

	private JMenuItem getCutMusicJMenuItem() {
		if (cutMusicJmenuJMenuItem == null) {
			cutMusicJmenuJMenuItem = new JMenuItem();
			cutMusicJmenuJMenuItem.setText("剪切歌曲");
			cutMusicJmenuJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
			cutMusicJmenuJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					PlayList playList = playerClient.getPlayListModel()
					.getPlayLists().get(
							playerClient.getPlayList()
									.getSelectedIndex());
					int[] musicIndexs = playerClient.getMusicJTable().getSelectedRows();
					if(musicIndexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行剪切制操作！！", "警告",JOptionPane.WARNING_MESSAGE);
						return;
					}
					playerClient.getPlayerDaoManager().cutMusics(musicIndexs, playList);
					playerClient.getEditMusicTableJMenu().getPasteMusicJMenuItem().setEnabled(true);
					playerClient.getMusicTableJPopMenu().getEditMusicTableJMenu().getPasteMusicJMenuItem().setEnabled(true);
				}
			});
		}
		return cutMusicJmenuJMenuItem;
	}

	public JMenuItem getPasteMusicJMenuItem() {
		if (pasteMusicJMenuItem == null) {
			pasteMusicJMenuItem = new JMenuItem();
			pasteMusicJMenuItem.setText("粘贴歌曲");
			pasteMusicJMenuItem.setEnabled(false);
			pasteMusicJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
			pasteMusicJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int index = playerClient.getMusicJTable().getSelectedRow();
					if(index==-1&&playerClient.getMusicTableModel().getMusics().size()!=0){
						index = playerClient.getMusicTableModel().getMusics().size()-1;
					}
					PlayList playList = playerClient.getPlayListModel()
							.getPlayLists().get(
									playerClient.getPlayList()
											.getSelectedIndex());
					playerClient.getPlayerDaoManager().pasteMusics(index,
							playList);
					playerClient.getEditMusicTableJMenu().getPasteMusicJMenuItem().setEnabled(false);
					playerClient.getMusicTableJPopMenu().getEditMusicTableJMenu().getPasteMusicJMenuItem().setEnabled(false);
				}
			});
		}
		return pasteMusicJMenuItem;
	}

	private JMenuItem getDeleteMusicJMenuItem() {
		if (deleteMusicJMenuItem == null) {
			deleteMusicJMenuItem = new JMenuItem();
			deleteMusicJMenuItem.setText("删除歌曲");
			deleteMusicJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
					Event.CTRL_MASK, true));
			deleteMusicJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					PlayList playList = playerClient.getPlayListModel()
							.getPlayLists().get(
									playerClient.getPlayList()
											.getSelectedIndex());
					int[] musicIndexs = playerClient.getMusicJTable().getSelectedRows();
					if(musicIndexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行删除操作！！", "警告",JOptionPane.WARNING_MESSAGE);
						return;
					}
					playerClient.getPlayerDaoManager().deleteMusics(musicIndexs, playList);
				}
			});
		}
		return deleteMusicJMenuItem;
	}
}
