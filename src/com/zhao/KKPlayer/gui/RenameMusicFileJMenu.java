package com.zhao.KKPlayer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class RenameMusicFileJMenu extends JMenu {

	private static final long serialVersionUID = -475641239671785073L;
	
	private KKPlayerClient playerClient;
	private JMenuItem renameitem1;
	private JMenuItem renameitem2;
	private JMenuItem renameitem3;
	private JMenuItem renameitem4;
	private RenameFileToCustomFormatView customFormatView;
	
	public RenameFileToCustomFormatView getCustomFormatView(){
		if(customFormatView==null){
			customFormatView = new RenameFileToCustomFormatView(playerClient, true);
		}
		return customFormatView;
	}
	
	public RenameMusicFileJMenu(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		this.add(getRenameitem1());
		this.add(getRenameitem2());
		this.add(getRenameitem3());
		this.add(getRenameitem4());
	}

	public JMenuItem getRenameitem1() {
		if(renameitem1==null){
			renameitem1 = new JMenuItem();
			renameitem1.setText("标题");
			renameitem1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					int[] musicIndexs = playerClient.getMusicJTable().getSelectedRows();
					if(musicIndexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行重命名操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					//重命名的格式
					String format = "%Title%";
					String[] parameters = playerClient.getPlayerManager().getFormatParameters(format);
					playerClient.getPlayerDaoManager().renameMusicFile(musicIndexs, parameters);
				}
			});
		}
		return renameitem1;
	}

	public JMenuItem getRenameitem2() {
		if(renameitem2==null){
			renameitem2 = new JMenuItem();
			renameitem2.setText("艺术家 - 标题");
			renameitem2.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					int[] musicIndexs = playerClient.getMusicJTable().getSelectedRows();
					if(musicIndexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行重命名操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					String format = "%Artist% - %Title%";
					String[] parameters = playerClient.getPlayerManager().getFormatParameters(format);
					playerClient.getPlayerDaoManager().renameMusicFile(musicIndexs, parameters);
				}
			});
		}
		return renameitem2;
	}

	public JMenuItem getRenameitem3() {
		if(renameitem3==null){
			renameitem3 = new JMenuItem();
			renameitem3.setText("标题 - 艺术家");
			renameitem3.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					int[] musicIndexs = playerClient.getMusicJTable().getSelectedRows();
					if(musicIndexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行重命名操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					String format = "%Title% - %Artist%";
					String[] parameters = playerClient.getPlayerManager().getFormatParameters(format);
					playerClient.getPlayerDaoManager().renameMusicFile(musicIndexs, parameters);
				}
			});
		}
		return renameitem3;
	}

	public JMenuItem getRenameitem4() {
		if(renameitem4==null){
			renameitem4 = new JMenuItem();
			renameitem4.setText("自定义格式");
			renameitem4.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					int[] musicindexs = playerClient.getMusicJTable().getSelectedRows();
					if(musicindexs.length==0){
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一首歌曲，再执行重命名操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					getCustomFormatView().setLocation(playerClient.getMainFrame().getX()+50, playerClient.getMainFrame().getY()+200);
					getCustomFormatView().setVisible(true);
				}
			});
		}
		return renameitem4;
	}
	
	

}
