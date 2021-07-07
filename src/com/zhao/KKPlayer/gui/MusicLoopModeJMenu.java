package com.zhao.KKPlayer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MusicLoopModeJMenu extends JMenu {


	private static final long serialVersionUID = 3854326470787561952L;
	/**
	 * 单曲循环菜单项
	 */
	private JMenuItem singleLoopModeJMenuItem = null;
	/**
	 * 列表循环菜单项
	 */
	private JMenuItem tableLoopModeJMenuItem = null;
	/**
	 * 随机播放菜单项
	 */
	private JMenuItem randomPlayJMenuItem = null;
	private KKPlayerClient playerClient;
	
	public MusicLoopModeJMenu(KKPlayerClient playerClient){
		this.playerClient = playerClient;
		this.setText("循环模式");
		this.add(getSingleLoopModeJMenuItem());
		this.add(getTableLoopModeJMenuItem());
		this.addSeparator();
		this.add(getRandomPlayJMenuItem());
	}
	
	public JMenuItem getSingleLoopModeJMenuItem() {
		if (singleLoopModeJMenuItem == null) {
			singleLoopModeJMenuItem = new JMenuItem();
			singleLoopModeJMenuItem.setText("     单曲循环");
			singleLoopModeJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().setSinglePlay(true);
					playerClient.getPlayerManager().setRandomPlay(false);
					getSingleLoopModeJMenuItem().setText("● 单曲循环");
					getTableLoopModeJMenuItem().setText("     列表循环");
					getRandomPlayJMenuItem().setText("     随机播放");
				}
			});
		}
		return singleLoopModeJMenuItem;
	}

	private JMenuItem getTableLoopModeJMenuItem() {
		if (tableLoopModeJMenuItem == null) {
			tableLoopModeJMenuItem = new JMenuItem();
			tableLoopModeJMenuItem.setText("● 列表循环");
			tableLoopModeJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().setSinglePlay(false);
					playerClient.getPlayerManager().setRandomPlay(false);
					getSingleLoopModeJMenuItem().setText("     单曲循环");
					getTableLoopModeJMenuItem().setText("● 列表循环");
					getRandomPlayJMenuItem().setText("     随机播放");
				}
			});
		}
		return tableLoopModeJMenuItem;
	}

	private JMenuItem getRandomPlayJMenuItem() {
		if (randomPlayJMenuItem == null) {
			randomPlayJMenuItem = new JMenuItem();
			randomPlayJMenuItem.setText("     随机播放");
			randomPlayJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().setRandomPlay(true);
					playerClient.getPlayerManager().setSinglePlay(false);
					getRandomPlayJMenuItem().setText("● 随机播放");
					getSingleLoopModeJMenuItem().setText("     单曲循环");
					getTableLoopModeJMenuItem().setText("     列表循环");
				}
			});
		}
		return randomPlayJMenuItem;
	}

}
