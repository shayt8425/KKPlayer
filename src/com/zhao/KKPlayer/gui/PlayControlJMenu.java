package com.zhao.KKPlayer.gui;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class PlayControlJMenu extends JMenu {

	private static final long serialVersionUID = 2237468911653704266L;

	private KKPlayerClient playerClient;
	private JMenuItem playJMenuItem = null;
	private JMenuItem nextJMenuItem = null;
	private JMenuItem prevJMenuItem = null;
	private JMenuItem stopJMenuItem = null;

	public PlayControlJMenu(KKPlayerClient PlayerClient) {
		this.playerClient = PlayerClient;
		this.add(getPlayJMenuItem());
		this.add(getStopJMenuItem());
		this.add(getNextJMenuItem());
		this.add(getPrevJMenuItem());
		this.addMenuListener(new MenuListener() {

			public void menuSelected(MenuEvent e) {
				int rowCount = PlayControlJMenu.this.playerClient
						.getMusicJTable().getSelectedRowCount();
				if (rowCount > 1) {
					getPlayJMenuItem().setEnabled(false);
				} else {
					getPlayJMenuItem().setEnabled(true);
				}
			}

			public void menuDeselected(MenuEvent e) {

			}

			public void menuCanceled(MenuEvent e) {

			}
		});
	}

	private JMenuItem getStopJMenuItem() {
		if (stopJMenuItem == null) {
			stopJMenuItem = new JMenuItem();
			stopJMenuItem.setText("停止");
			stopJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
					0, true));
			stopJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().stopMusicFile();
				}
			});
		}
		return stopJMenuItem;
	}

	public JMenuItem getPlayJMenuItem() {
		if (playJMenuItem == null) {
			playJMenuItem = new JMenuItem();
			playJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
					Event.CTRL_MASK, true));
			playJMenuItem.setText("播放");
			playJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int row = playerClient.getMusicJTable().getSelectedRow();
					if (row == -1) {
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请选择一首歌曲，在执行播放操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					playerClient.playUserChooseOperation();
				}
			});
		}
		return playJMenuItem;
	}

	private JMenuItem getNextJMenuItem() {
		if (nextJMenuItem == null) {
			nextJMenuItem = new JMenuItem();
			nextJMenuItem.setAccelerator(KeyStroke.getKeyStroke(
					KeyEvent.VK_DOWN, Event.ALT_MASK, true));
			nextJMenuItem.setText("下一首");
			nextJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().nextMusicFile();
				}
			});
		}
		return nextJMenuItem;
	}

	private JMenuItem getPrevJMenuItem() {
		if (prevJMenuItem == null) {
			prevJMenuItem = new JMenuItem();
			prevJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
					Event.ALT_MASK, true));
			prevJMenuItem.setText("上一首");
			prevJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().prevMusicFile();
				}
			});
		}
		return prevJMenuItem;
	}
}
