package com.zhao.KKPlayer.tag;

import com.zhao.KKPlayer.model.Music;

/**
 * 定义得到音乐标签工具类的接口
 * 
 * @author Administrator
 * 
 */
public interface MusicTag {
	
	/**
	 * 返回图片封面数组
	 * @return
	 */
	public byte[] getPicData();
	/**
	 * 返回歌曲的标题标签信息
	 * @return
	 */
	public String getMusicTitle();
	
	/**
	 * 返回歌曲的艺术家标签信息
	 * @return
	 */
	public String getMusicArtist();
	
	/**
	 * 返回了歌曲的专辑标签信息
	 * @return
	 */
	public String getMusicAlbum();
	
	/**
	 * 读取歌曲的内嵌歌词
	 * @return
	 */
	public String getMusicLyric();
	
	/**
	 * 读取歌曲的音轨号
	 * @return
	 */
	public String getMusicTrackNumber();
	
	/**
	 * 读取歌曲的发行时间
	 * @return
	 */
	public String getMusicYear();

	public String getMusicGenre();

	public long getMusicPlayTime();

	public long getMusicSize();

	public long getMusicBitRate();
	
	/**
	 * 读取歌词的备注
	 * @return
	 */
	public String getMusicComment();
	
	public void save(Music music);
	
	public void saveLyric(String lyric);
}
