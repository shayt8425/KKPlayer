package com.zhao.KKPlayer.model;

import com.zhao.KKPlayer.tag.MusicTag;

/**
 * 音乐的数据模型
 * 
 * @author Administrator
 * 
 */
public class Music {

	private int id;
	/**
	 * 歌曲的文件名
	 */
	private String name;
	/**
	 * 歌曲所在的路径
	 */
	private String path;
	/**
	 * 歌曲的总时长
	 */
	private Long totalTime;
	/**
	 * 歌曲的艺术家名称
	 */
	private String artist;
	/**
	 * 歌曲的标题
	 */
	private String title;
	
	/**
	 * 歌曲所属的专辑
	 */
	private String Album;
	/**
	 * 歌曲的流派
	 */
	private String genre;
	/**
	 * 歌曲的音轨号
	 */
	private String trackNumber;
	/**
	 * 歌曲的发行年份
	 */
	private String year;
	/**
	 * 歌曲的备注
	 */
	private String comment;
	
	/**
	 * 歌曲的歌词
	 */
	private String Lyric;
	
	/**
	 * 是否设置歌词
	 * @param lyric
	 */
	private boolean lyricSetted;
	
	private MusicTag tag;
	
	public void setTag(MusicTag tag) {
		this.tag = tag;
	}
	
	public MusicTag getTag() {
		return tag;
	}
	
	public boolean isLyricSetted() {
		return lyricSetted;
	}
	
	public void setLyricSetted(boolean lyricSetted) {
		this.lyricSetted = lyricSetted;
	}
	
	public void setLyric(String lyric) {
		Lyric = lyric;
	}
	
	public String getLyric() {
		return Lyric;
	}
	
	public String getAlbum() {
		return Album;
	}

	public void setAlbum(String album) {
		Album = album;
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
