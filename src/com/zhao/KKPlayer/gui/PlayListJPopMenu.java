package com.zhao.KKPlayer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.zhao.KKPlayer.model.PlayList;

public class PlayListJPopMenu extends JPopupMenu {

	private static final long serialVersionUID = 826131668862452832L;
	/**
	 * 添加播放列表
	 */
	private JMenuItem addPlayListJMenuItem;
	private JMenuItem deletePlayListJMenuItem;
	private JMenuItem renamePlayListJMenuItem;
	private KKPlayerClient playerClient;
	private AddPlayListView addPlayListView;
	private renamePlayListView renamePlayListView;
	
	public renamePlayListView getRenamePlayListView(){
		if(renamePlayListView == null){
			renamePlayListView = new renamePlayListView(playerClient, true);
		}
		return renamePlayListView;
	}

	public AddPlayListView getAddPlayListView() {
		if (addPlayListView == null) {
			addPlayListView = new AddPlayListView(playerClient, true);
		}
		return addPlayListView;
	}

	public PlayListJPopMenu(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		this.add(getAddPlayListJMenuItem());
		this.add(getDeletePlayListJmeJMenuItem());
		this.add(getRenamePlayListJmeJMenuItem());
	}

	public JMenuItem getAddPlayListJMenuItem(){
		if(addPlayListJMenuItem == null){
			addPlayListJMenuItem = new JMenuItem();
			addPlayListJMenuItem.setText("添加列表");
			addPlayListJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					getAddPlayListView().setLocation(playerClient.getMainFrame().getX()+50, playerClient.getMainFrame().getY()+200);
					getAddPlayListView().setVisible(true);
				}
			});
		}
		return addPlayListJMenuItem;
	}

	public JMenuItem getDeletePlayListJmeJMenuItem() {
		if (deletePlayListJMenuItem == null) {
			deletePlayListJMenuItem = new JMenuItem();
			deletePlayListJMenuItem.setText("删除列表");
			deletePlayListJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int index = playerClient.getPlayList().getSelectedIndex();
					PlayList playList = playerClient.getPlayListModel()
							.getPlayLists().get(index);
					playerClient.getPlayerDaoManager().deletePlayList(playList,
							index);
				}
			});
		}
		return deletePlayListJMenuItem;
	}

	public JMenuItem getRenamePlayListJmeJMenuItem() {
		if (renamePlayListJMenuItem == null) {
			renamePlayListJMenuItem = new JMenuItem();
			renamePlayListJMenuItem.setText("重命名");
			renamePlayListJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					int index = playerClient.getPlayList().getSelectedIndex();
					PlayList playList = playerClient.getPlayListModel().getPlayLists().get(index);
					getRenamePlayListView().setLocation(playerClient.getMainFrame().getX()+50, playerClient.getMainFrame().getY()+200);
					getRenamePlayListView().getJTextField().setText(playList.getName());
					getRenamePlayListView().setVisible(true);
				}
			});
		}
		return renamePlayListJMenuItem;
	}
	
}
