package com.zhao.KKPlayer.model;

/**
 * 播放列表的数据模型
 * 
 * @author Administrator
 * 
 */
public class PlayList {

	/**
	 * 标识属性
	 */
	private int id;
	private String name;
	/**
	 * 与之相关联的音乐列表的路径
	 */
	private String musicListPath;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMusicListPath() {
		return musicListPath;
	}

	public void setMusicListPath(String musicListPath) {
		this.musicListPath = musicListPath;
	}

}
