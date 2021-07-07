package com.zhao.KKPlayer.gui;

import java.util.List;
import javax.swing.AbstractListModel;
import com.zhao.KKPlayer.model.PlayList;

public class PlayListModel extends AbstractListModel {

	private static final long serialVersionUID = 5688786046253441306L;
	private List<PlayList> playLists;

	public List<PlayList> getPlayLists() {
		return playLists;
	}

	public void setPlayLists(List<PlayList> playLists) {
		this.playLists = playLists;
	}


	public PlayListModel(KKPlayerClient playerClient) {
		setPlayLists(playerClient.getPlayerDaoManager().getPlayLists());
	}

	public Object getElementAt(int index) {
		if (playLists.size() == 0) {
			return null;
		}else{
			return playLists.get(index).getName();
		}
	}

	public int getSize() {
		return playLists.size();
	}
	
	public void deleteRow(int index0,int index1) {
		fireIntervalRemoved(this, index0, index1);
	}
	
	public void addRow(int index0,int index1){
		fireIntervalAdded(this, index0, index1);
	}
	
	public void propertyChange(int index0,int index1){
		fireContentsChanged(this, index0, index1);
	}

}
