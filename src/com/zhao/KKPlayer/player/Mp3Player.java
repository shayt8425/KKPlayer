package com.zhao.KKPlayer.player;

import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Mp3Player implements MusicPlayer {
	
	private Player player;
	private InputStream is;
	
	public Mp3Player(InputStream is){
		try {
			player = new Player(is);
			this.is=is;
		} catch (JavaLayerException e) {
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
				} catch (JavaLayerException e) {
					e.printStackTrace();
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
	
	public void close() {
		player.close();
		if(is!=null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void seek() {
		
	}

}
