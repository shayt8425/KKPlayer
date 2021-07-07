package com.zhao.KKPlayer.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.jdom.Element;

public class LyricSearchResult extends AbstractTableModel {
	
	
	private static final long serialVersionUID = -6824601051032738319L;
	
	private List<Element> datas = new ArrayList<Element>();
	
	public List<Element> getDatas() {
		return datas;
	}

	public void setDatas(List<Element> datas) {
		this.datas = datas;
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return datas.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex==0){
			return datas.get(rowIndex).getAttributeValue("artist");
		}else{
			return datas.get(rowIndex).getAttributeValue("title");
		}
	}
	
	public String getColumnName(int column) {
		if(column==0){
			return "歌手";
		}else{
			return "歌曲名";
		}
	}

}
