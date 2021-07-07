package com.zhao.KKPlayer.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavPlayer implements MusicPlayer {

	private AudioFormat format;
	private boolean stop;
	private boolean pause;
	private String fileName;

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public void close() {
		
	}

	public void pause() {
		setPause(true);
	}

	public void resume() {
		setPause(false);
	}

	public void seek() {
		
	}

	public void stop() {
		setStop(true);
	}

	public WavPlayer(String fileName) throws UnsupportedAudioFileException, IOException {
			this.fileName = fileName;
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(
					fileName));
			format = stream.getFormat();
			stream.close();
	}

	public void play() {
		InputStream source = null;
		try {
			source = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// use a short, 100ms (1/10th sec) buffer for real-time
		// change to the sound stream
		int bufferSize = format.getFrameSize()
				* Math.round(format.getSampleRate() / 10);
		byte[] buffer = new byte[bufferSize];

		// create a line to play to
		SourceDataLine line;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufferSize);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
			return;
		}

		new Thread(new PlayThread(line, source, buffer)).start();

	}


	private class PlayThread implements Runnable {

		private SourceDataLine line;
		private InputStream source;
		private byte[] buffer;

		public PlayThread(SourceDataLine line, InputStream source, byte[] buffer) {
			this.line = line;
			this.source = source;
			this.buffer = buffer;
		}

		public void run() {
			line.start();
			try {
				int numBytesRead = 0;
				while (numBytesRead != -1 && !isStop()) {
					numBytesRead = source.read(buffer, 0, buffer.length);
					if (numBytesRead != -1 && !isPause()) {
						line.write(buffer, 0, numBytesRead);
					} else {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				source.close();
				line.drain();
				line.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
