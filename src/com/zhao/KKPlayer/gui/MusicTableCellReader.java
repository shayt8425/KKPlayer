package com.zhao.KKPlayer.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.util.PlayerUtil;

public class MusicTableCellReader extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 6052030230464538300L;
	private KKPlayerClient playerClient;
	
	public MusicTableCellReader(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Music music = playerClient.getMusicTableModel().getMusics().get(row);
		String time = PlayerUtil.formatMusicTime(music.getTotalTime());
		String musicFormat = music.getPath().substring(music.getPath().lastIndexOf(".")+1);
		String text = "<html><body><span style=\"font-family: 楷体\">标 题："+music.getTitle()+"<br>艺术家："+music.getArtist()+"<br>专   辑："+music.getAlbum()+"<br>格  式："+musicFormat+"<br>长  度："+time+"<span><body><html>";
		setToolTipText(text);
		if(row == playerClient.getPlayerManager().findPlayingMusicIndex()){
			if(column==0){
				setIcon(new ImageIcon(getClass().getResource("/images/play02.png")));
			}else{
				setIcon(null);
			}
			setForeground(Color.red);
		}else{
			setIcon(null);
			setForeground(Color.black);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	}
	
}
