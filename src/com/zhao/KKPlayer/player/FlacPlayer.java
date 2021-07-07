package com.zhao.KKPlayer.player;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;


public class FlacPlayer implements PCMProcessor, MusicPlayer {

	private FLACDecoder decoder;
	private InputStream is;
	private AudioFormat fmt;
	private DataLine.Info info;
	private SourceDataLine line;
	private Vector<LineListener> listeners = new Vector<LineListener>();

	public FlacPlayer(InputStream is) {
		decoder = new FLACDecoder(is);
		this.is = is;
	}

	public void play() {
		new Thread(new Runnable() {

			public void run() {
				try {
					decoder.addPCMProcessor(FlacPlayer.this);
					decoder.decode();
				} catch (IOException e) {
					// 不管异常
				}
			}
		}).start();
	}

	public void stop() {
		decoder.setStop(true);
		decoder.removePCMProcessor(this);
		this.close();
	}

	public void pause() {
		decoder.setPause(true);
	}

	public void resume() {
		decoder.setPause(false);
	}

	public void addListener(LineListener listener) {
		listeners.add(listener);
	}

	/**
	 * Process the StreamInfo block.
	 * 
	 * @param streamInfo
	 *            the StreamInfo block
	 * @see org.kc7bfi.jflac.PCMProcessor#processStreamInfo(org.kc7bfi.jflac.metadata.StreamInfo)
	 */
	public void processStreamInfo(StreamInfo streamInfo) {
		try {
			fmt = streamInfo.getAudioFormat();
			info = new DataLine.Info(SourceDataLine.class, fmt,
					AudioSystem.NOT_SPECIFIED);
			line = (SourceDataLine) AudioSystem.getLine(info);

			// Add the listeners to the line at this point, it's the only
			// way to get the events triggered.
			int size = listeners.size();
			for (int index = 0; index < size; index++)
				line.addLineListener((LineListener) listeners.get(index));

			line.open(fmt, AudioSystem.NOT_SPECIFIED);
			line.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process the decoded PCM bytes.
	 * 
	 * @param pcm
	 *            The decoded PCM data
	 * @see org.kc7bfi.jflac.PCMProcessor#processPCM(org.kc7bfi.jflac.util.ByteSpace)
	 */
	public void processPCM(ByteData pcm) {
		line.write(pcm.getData(), 0, pcm.getLen());
	}

	public void removeListener(LineListener listener) {
		listeners.removeElement(listener);
	}

	public void close() {
		if (is != null) {
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
