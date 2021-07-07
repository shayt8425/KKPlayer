package com.zhao.KKPlayer.player;


import java.io.File;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class OggPlayer implements MusicPlayer{

	private BasicPlayer basicPlayer;
	
	public OggPlayer(File file) {
		basicPlayer = new BasicPlayer();
		try {
			basicPlayer.open(file);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		
	}

	public void pause() {
		try {
			basicPlayer.pause();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			basicPlayer.play();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void resume() {
		try {
			basicPlayer.resume();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			basicPlayer.stop();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void seek() {
		
	}	
}
