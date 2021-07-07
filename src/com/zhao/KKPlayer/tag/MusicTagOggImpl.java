package com.zhao.KKPlayer.tag;

import java.io.File;
import com.zhao.KKPlayer.model.Music;
import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.audioformats.ogg.OggFileReader;
import entagged.audioformats.ogg.OggFileWriter;
import entagged.audioformats.ogg.OggTag;
import entagged.audioformats.ogg.util.OggTagField;

/**
 * OGG音乐标签的实现
 * 
 * @author Administrator
 * 
 */
public class MusicTagOggImpl implements MusicTag {

	// 得到所有的ogg标签信息
	private Tag tag;
	private AudioFile audioFile;

	public MusicTagOggImpl(Music music) {
		OggFileReader oggFileReader = new OggFileReader();
		try {
			audioFile = oggFileReader.read(new File(music.getPath()));
			tag = (OggTag) audioFile.getTag();
			music.setTag(this);
		} catch (CannotReadException e) {
			e.printStackTrace();
		}
	}

	public String getMusicAlbum() {
		if (tag.getFirstAlbum() != null) {
			return tag.getFirstAlbum();
		}
		return "";
	}

	public String getMusicArtist() {
		if (tag.getFirstArtist() != null)
			return tag.getFirstArtist();
		return "";
	}

	public String getMusicGenre() {
		if (tag.getFirstGenre() != null)
			return tag.getFirstGenre();
		return "";
	}

	public String getMusicLyric() {
		if (tag.toString().indexOf("LYRICS") != -1) {
			return tag.get("LYRICS").get(0).toString();
		}
		return "";
	}

	public long getMusicPlayTime() {
		return audioFile.getLength() * 1000;
	}

	public String getMusicTitle() {
		if (tag.getFirstTitle() != null)
			return tag.getFirstTitle();
		return "";
	}

	public String getMusicTrackNumber() {
		if (tag.getFirstTrack() != null)
			return tag.getFirstTrack();
		return "";
	}

	public String getMusicYear() {
		if (tag.getFirstYear() != null)
			return tag.getFirstYear();
		return "";
	}

	public long getMusicSize() {
		return audioFile.getAbsoluteFile().length();
	}

	public long getMusicBitRate() {
		return audioFile.getBitrate();
	}

	public String getMusicComment() {
		if (tag.getFirstComment() != null&&!tag.getFirstComment().trim().equals("")) {
			return tag.getFirstComment();
		}else{
			if(tag.toString().indexOf("\n	COMMENT")!=-1){
				String  temp = tag.toString().substring(tag.toString().indexOf("\n	COMMENT"));
				String temp1 = temp.substring(temp.indexOf("COMMENT"));
				return temp1.substring(0,temp1.indexOf("\n")).split(" : ")[1];
			}else{
				return "";
			}
		}
	}

	public void save(Music music) {
		tag.setArtist(music.getArtist());
		tag.setAlbum(music.getAlbum());
		tag.setComment(music.getComment());
		tag.setGenre(music.getGenre());
		tag.setTitle(music.getTitle());
		tag.setTrack(music.getTrackNumber());
		tag.setYear(music.getYear());
		OggFileWriter writer = new OggFileWriter();
		try {
			writer.write(audioFile);
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}

	public void saveLyric(String lyric) {
		tag.set(new OggTagField("LYRICS", lyric));
		OggFileWriter writer = new OggFileWriter();
		try {
			writer.write(audioFile);
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getPicData() {
		// TODO Auto-generated method stub
		return null;
	}
}