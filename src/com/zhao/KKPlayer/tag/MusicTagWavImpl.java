package com.zhao.KKPlayer.tag;

import java.io.File;

import com.zhao.KKPlayer.model.Music;
import entagged.audioformats.AudioFile;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.wav.WavFileReader;

public  class MusicTagWavImpl implements MusicTag{
	
	private AudioFile af;
	
	public MusicTagWavImpl(Music music) {
		WavFileReader reader = new WavFileReader();
		try {
			af =reader.read(new File(music.getPath()));
			music.setTag(this);
		} catch (CannotReadException e) {
			e.printStackTrace();
		}
	}

	public String getMusicAlbum() {
		return "";
	}

	public String getMusicArtist() {
		return "";
	}
	
	public long getMusicBitRate() {
		return af.getBitrate();
	}

	public String getMusicComment() {
		return "";
	}

	public String getMusicGenre() {
		return "";
	}

	public String getMusicLyric() {
		return null;
	}

	public long getMusicPlayTime() {
		return af.getLength()*1000;
	}

	public long getMusicSize() {
		return af.getAbsoluteFile().length();
	}

	public String getMusicTitle() {
		return "";
	}

	public String getMusicTrackNumber() {
		return "";
	}

	public String getMusicYear() {
		return "";
	}

	public void save(Music music) {
		
	}

	public void saveLyric(String lyric) {
		
	}

	@Override
	public byte[] getPicData() {
		// TODO Auto-generated method stub
		return null;
	}

}
