package com.zhao.KKPlayer.player;

import java.io.IOException;
import com.zhao.KKPlayer.model.Music;
import davaguine.jmac.player.Player;

public class ApePlayer implements MusicPlayer {
	
	public Player player;
	
	public ApePlayer(Music music) throws Exception {
			player = new Player(music.getPath());
	}

	public void close() {
		try {
			player.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		player.setPause(true);
	}

	public void play() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					player.play();
				} catch (IOException e) {
					//跳过IO异常
				}	
			}
		}).start();
	}

	public void resume() {
		player.setPause(false);
	}

	public void stop() {
		player.setStop(true);
	}

	public void seek() {
		
	}
}
