package com.zhao.KKPlayer.tag;

import java.io.File;
import java.io.IOException;

import com.zhao.KKPlayer.model.Music;

import davaguine.jmac.info.APETagField;
import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;
import entagged.audioformats.flac.FlacFileReader;
import entagged.audioformats.flac.FlacFileWriter;
import entagged.audioformats.ogg.util.OggTagField;

public class MusicTagFlacImpl implements MusicTag {

	private AudioFile audioFile;
	private Tag tag;

	public MusicTagFlacImpl(Music music) {
		try {
			audioFile = new FlacFileReader().read(new File(music.getPath()));
			tag = audioFile.getTag();
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
		if (tag.getFirstArtist() != null) {
			return tag.getFirstArtist();
		}
		return "";
	}

	public long getMusicBitRate() {
		return audioFile.getBitrate();
	}

	public String getMusicGenre() {
		if (tag.getFirstGenre() != null) {
			return tag.getFirstGenre();
		}
		return "";
	}

	public String getMusicLyric() {
		if (tag.toString().indexOf("LYRICS :") != -1) {
			return tag.get("LYRICS").get(0).toString();
		}
		return "";
	}

	public long getMusicPlayTime() {
		return audioFile.getLength() * 1000;
	}

	public long getMusicSize() {
		return audioFile.getParentFile().length();
	}

	public String getMusicTitle() {
		if (tag.getFirstTitle() != null) {
			return tag.getFirstTitle();
		}
		return "";
	}

	public String getMusicTrackNumber() {
		if (tag.getFirstTrack() != null) {
			return tag.getFirstTrack();
		}
		return "";
	}

	public String getMusicYear() {
		if (tag.getFirstYear() != null) {
			return tag.getFirstYear();
		}
		return "";
	}

	public String getMusicComment() {
		if (tag.getFirstComment() != null&&!tag.getFirstComment().trim().equals("")) {
			return tag.getFirstComment();
		}else{
			if(tag.toString().indexOf("\n	COMMENT")!=-1){
				String  temp = tag.toString().substring(tag.toString().indexOf("\n	COMMENT"));
				String temp1 = temp.substring(temp.indexOf("COMMENT"));
				return temp1.substring(0,temp1.indexOf("\n	")).split(" : ")[1];
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
		FlacFileWriter writer = new FlacFileWriter();
		try {
			writer.write(audioFile);
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}

	public void saveLyric(String lyric) {		
		FlacFileWriter writer = new FlacFileWriter();
		try {
			tag.set(new OggTagField("LYRICS", lyric));
			audioFile.setWritable(true);
			writer.write(audioFile);
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getPicData() {
		// TODO Auto-generated method stub
//		try {
//			APETagField tf = tag.GetTagField("Cover Art (front)");
//			if(tf!=null){
//				
//				byte[] arr = tf.GetFieldValue();
//				int offset = 0;
//				for (int i = 0; i < arr.length; i++) {
//					if(arr[i]==(byte)0){
//						offset = i;
//						break;
//					}
//				}
//				byte[] descArr = new byte[arr.length-offset-1];
//				String picName = new String(arr,"GBK").substring(0,offset);
//				System.arraycopy(arr, offset+1, descArr, 0, descArr.length);
//				
//				return descArr;
//			}else{
//				return null;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
	
}
