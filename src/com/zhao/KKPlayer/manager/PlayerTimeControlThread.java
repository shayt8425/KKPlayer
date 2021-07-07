package com.zhao.KKPlayer.manager;

import com.zhao.KKPlayer.gui.KKPlayerClient;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.player.MusicPlayer;
import com.zhao.KKPlayer.util.PlayerUtil;

/**
 * 播放时间控制的线程
 * 
 * @author Administrator
 * 
 */
public class PlayerTimeControlThread extends Thread {

	private boolean stop = false;
	private boolean pause = false;
	private long playingTime;
	private Music playingMusic;
	private MusicPlayer player;
	private KKPlayerClient playerClient;

	public PlayerTimeControlThread(Music playingMusic, MusicPlayer player,
			KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		this.player = player;
		this.playingMusic = playingMusic;
		// 构造方法初始化时间为0
		this.playingTime = 0;
	}

	public void run() {
		while (playingTime <= playingMusic.getTotalTime()
				&& playingMusic != null && !stop && player != null) {
			if (!pause) {
				if (playingTime % 1000 == 0) {
					playerClient.getMusicPlayingJSlider().setValue(
							new Long(playingTime / 1000).intValue());
					playerClient.getPlayingTimeJLabel().setText(
							PlayerUtil.formatMusicTime(playingTime));
				}
				if (playingTime == playingMusic.getTotalTime()) {
					try {
						if (playerClient.getPlayerManager().isRandomPlay()) {
							// 播放下一首的时间间隔2秒的时间
							Thread.sleep(2000);
							playerClient.getPlayerManager()
									.randomPlayMusicFile();
						} else {
							if (playerClient.getPlayerManager().isSinglePlay()) {
								Thread.sleep(2000);
								playerClient.getPlayerManager()
										.singlePlayMusicFile();
							} else {
								Thread.sleep(2000);
								playerClient.getPlayerManager().nextMusicFile();

							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					for (int i = 0; i < 10; i++) {
						if (stop) {
							break;
						} else {
							Thread.sleep(99);
							playingTime += 100;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stopThread() {
		playerClient.getPlayingTimeJLabel().setText("00:00");
		playerClient.getMusicPlayingJSlider().setValue(0);
		stop = true;
	}

	public void pauseThread() {
		pause = true;
	}

	public void resumeThread() {
		pause = false;
	}

	public boolean isPause() {
		return pause;
	}

	public boolean isStop() {
		return stop;
	}

	public long getPlayingTime() {
		return playingTime;
	}

}
