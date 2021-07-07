package com.zhao.KKPlayer.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.util.PlayerUtil;

/**
 * 歌曲列表的数据模板
 * @author Administrator
 *
 */
public class MusicTableModel extends AbstractTableModel {

	
	private List<Music> musics;
	private static final long serialVersionUID = -7919285829236462253L;

	public MusicTableModel(KKPlayerClient playerClient) {
		musics = playerClient.getPlayerDaoManager().getMusics();
	}
	
	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return musics.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(musics.size()==0){
			return null;
		}else{
			if(columnIndex==0){
				return rowIndex+1;
			}else if(columnIndex==1){
				Music music = musics.get(rowIndex);
				if(music.getArtist()==null||music.getArtist().trim().equals("")){
					return music.getName();
				}
				return music.getArtist()+" - "+music.getTitle();
			}else{
				return PlayerUtil.formatMusicTime(musics.get(rowIndex).getTotalTime());
			}
		}
	}
	
	public String getColumnName(int column) {
		if(column==0){
			return "歌曲编号";
		}else if(column==1){
			return "歌曲标题(艺术家 - 标题)";
		}else{
			return "歌曲时间";
		}
	}
	
	public List<Music> getMusics() {
		return musics;
	}

	public void setMusics(List<Music> musics) {
		this.musics = musics;
		fireTableDataChanged();
	}


}
