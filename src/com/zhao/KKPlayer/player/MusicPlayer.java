package com.zhao.KKPlayer.player;

/**
 * 各种音乐格式的播放器接口
 * @author Administrator
 *
 */
public interface MusicPlayer {

	/**
	 * 播放
	 */
	public void play();
	
	public void stop();
	
	public void pause();
	
	public void resume();
	
	/**
	 * 关闭播放器读出来的流
	 */
	public void close();
	
	/**
	 * 音乐快进或后退的方法
	 */
	public void seek();
	
}
