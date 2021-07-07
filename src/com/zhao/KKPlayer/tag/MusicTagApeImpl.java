package com.zhao.KKPlayer.tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.zhao.KKPlayer.gui.KKPlayerClient;
import com.zhao.KKPlayer.model.Music;
import davaguine.jmac.decoder.APEDecompress;
import davaguine.jmac.info.APEInfo;
import davaguine.jmac.info.APETag;
import davaguine.jmac.info.APETagField;
import entagged.audioformats.Tag;
import entagged.audioformats.ape.util.ApeTagBinaryField;
import entagged.audioformats.ape.util.ApeTagReader;
import entagged.audioformats.ape.util.ApeTagTextField;
import entagged.audioformats.ape.util.ApeTagWriter;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;

/**
 * Ape音乐标签的实现
 * 
 * @author Administrator
 * 
 */
public class MusicTagApeImpl implements MusicTag {

	/**
	 * 存放Ape对象的标签
	 */
	private APEDecompress apeDecompress;
	private APETag apeTag;
	private Music music;
	private KKPlayerClient client;

	/**
	 * 构造方法将ApeTags初始化
	 * 
	 * @param mi
	 */
	public MusicTagApeImpl(Music music,KKPlayerClient client) {
		try {
			this.music = music;
			this.client =  client;
			music.setTag(this);
			APEInfo info = new APEInfo(new File(music.getPath()));
			apeDecompress = new APEDecompress(info);
			apeTag = apeDecompress.getApeInfoTag();
			info.getApeInfoIoSource().close();
			info.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMusicAlbum() {
		try {
			if(apeTag.GetFieldString("album")!=null)
			return apeTag.GetFieldString("album");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getMusicArtist() {
		try {
			if(apeTag.GetFieldString("artist")!=null)
			return apeTag.GetFieldString("artist");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getMusicGenre() {
		try {
			if(apeTag.GetFieldString("genre")!=null)
			return apeTag.GetFieldString("genre");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getMusicLyric() {
		try {
			String lyric = apeTag.GetFieldString("lyrics");
			if(lyric!=null&&!lyric.equals("")){
				return lyric;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public long getMusicPlayTime() {
		long apeTime = apeDecompress.getApeInfoLengthMs();
		if (apeTime % 1000 > 500) {
			return (apeTime + 1000) - (apeTime % 1000);
		} else {
			return (apeTime / 1000) * 1000;
		}
	}

	public String getMusicTitle() {
		try {
			if(apeTag.GetFieldString("title")!=null)
			return apeTag.GetFieldString("title");
		} catch (IOException e) {
			return "";
		}
		return "";
	}

	public String getMusicTrackNumber() {
		try {
			if(apeTag.GetFieldString("track")!=null)
			return apeTag.GetFieldString("track");
		} catch (IOException e) {
			return "";
		}
		return "";
	}

	public String getMusicYear() {
		try {
			if(apeTag.GetFieldString("year")!=null)
			return apeTag.GetFieldString("year");
		} catch (IOException e) {
			return "";
		}
		return "";
	}

	public long getMusicSize() {
		return apeDecompress.getApeInfoApeTotalBytes() / 8;
	}

	public long getMusicBitRate() {
		return apeDecompress.getApeInfoAverageBitrate();
	}

	public String getMusicComment() {
		try {
			if (apeTag.GetFieldString("comment") != null) {
				return apeTag.GetFieldString("comment");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void save(Music music) {
		try {
			ApeTagReader reader = new ApeTagReader();
			RandomAccessFile raf = new RandomAccessFile(new File(music.getPath()), "rw");
			Tag tag = reader.read(raf);
			tag.setArtist(music.getArtist());
			tag.setAlbum(music.getAlbum());
			tag.setComment(music.getComment());
			tag.setGenre(music.getGenre());
			tag.setTitle(music.getTitle());
			tag.setTrack(music.getTrackNumber());
			tag.setYear(music.getYear());
			byte[] value = client.getMusicTableJPopMenu().getFilePropertiesView().getImgPanel().getData();
			if(value != null && value.length != 0){
				byte[] coverName = "cover.jpg".getBytes("GBK");
				byte[] newDataArr = new byte[coverName.length+1+value.length];
				for (int i = 0; i < coverName.length; i++) {
					newDataArr[i] = coverName[i];
				}
				newDataArr[coverName.length] = (byte)0;
				for (int i = coverName.length+1; i < newDataArr.length; i++) {
					newDataArr[i] = value[i-coverName.length-1];
				}
				tag.set(new ApeTagBinaryField("Cover Art (front)", newDataArr));
				apeTag.SetFieldBinary("Cover Art (front)", newDataArr,0);
			}
			ApeTagWriter writer = new ApeTagWriter();
			writer.write(tag, raf, raf);
			raf.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CannotReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}

	public void saveLyric(String lyric) {
		ApeTagReader reader = new ApeTagReader();
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(new File(music.getPath()), "rw");
			Tag tag = reader.read(raf);
			tag.set(new ApeTagTextField("lyrics", lyric));
			ApeTagWriter writer = new ApeTagWriter();
			writer.write(tag, raf, raf);
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CannotReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getPicData() {
		// TODO Auto-generated method stub
		try {
			APETagField tf = apeTag.GetTagField("Cover Art (front)");
			if(tf!=null){
				
				byte[] arr = tf.GetFieldValue();
				int offset = 0;
				for (int i = 0; i < arr.length; i++) {
					if(arr[i]==(byte)0){
						offset = i;
						break;
					}
				}
				byte[] descArr = new byte[arr.length-offset-1];
				String picName = new String(arr,"GBK").substring(0,offset);
				System.arraycopy(arr, offset+1, descArr, 0, descArr.length);
				
				return descArr;
			}else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
