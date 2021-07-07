package com.zhao.KKPlayer.tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.farng.mp3.AbstractMP3FragmentBody;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.FrameBodyAPIC;
import org.farng.mp3.id3.ID3v2_2;
import org.farng.mp3.id3.ID3v2_2Frame;
import org.farng.mp3.id3.ID3v2_3;
import org.farng.mp3.id3.ID3v2_3Frame;
import org.farng.mp3.id3.ID3v2_4;
import org.farng.mp3.lyrics3.AbstractLyrics3;
import org.farng.mp3.lyrics3.Lyrics3v2;

import com.zhao.KKPlayer.gui.KKPlayerClient;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.util.PlayerUtil;

/**
 * Mp3音乐标签的实现
 * 
 * @author Administrator
 * 
 */
public class MusicTagMp3Impl implements MusicTag {

	private MP3File file;
	//当创建MP3File对象捕获到异常时则会产生此对象
	private ID3v2_2 id3v2;
	//音乐对象
	private Music music;
	
	private KKPlayerClient client;

	public MusicTagMp3Impl(Music music,KKPlayerClient client) {
		try {
			this.music = music;
			this.client = client;
			music.setTag(this);
			file = new MP3File(new File(music.getPath()));
			if(file.hasID3v2Tag()){
				id3v2 = (ID3v2_2) file.getID3v2Tag();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此方法返回歌曲的标题
	 * 
	 * @return 歌曲的标题标签
	 * @throws IOException
	 * @throws TagException
	 */
	public String getMusicTitle() {
		if (file != null) {
			if (id3v2!=null) {
				if(id3v2.getSongTitle()==null|| id3v2.getSongTitle().equals("")){
					if (file.hasID3v1Tag()) {
						return file.getID3v1Tag().getSongTitle();
					}
				}
				return id3v2.getSongTitle();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getSongTitle();
			}
		}
		return "";
	}
	
	public byte[] getPicData(){
		if (file != null && id3v2 != null) {
			if(id3v2.getFrame("APIC")!=null){
				AbstractMP3FragmentBody apic = id3v2.getFrame("APIC").getBody();
				byte[] data = (byte[]) apic.getObject("Picture Data");
				int offset = 0;
				for (int i = 0; i < data.length; i++) {
					if(data[i] == (byte)0){
						offset++;
					}else{
						break;
					}
				}
				byte[] arr = new byte[data.length-offset];
			   System.arraycopy(data,offset,arr,0,arr.length);
			   return arr;
			}
		}
		return null;
	}

	/**
	 * 此方法返回歌曲的艺术家标签
	 * 
	 * @throws IOException
	 * @throws TagException
	 */
	public String getMusicArtist() {
		if (file != null) {
			if (id3v2!=null) {
				if(id3v2.getLeadArtist()==null|| id3v2.getLeadArtist().equals("")){
					if (file.hasID3v1Tag()) {
						return file.getID3v1Tag().getLeadArtist();
					}
				}
				return id3v2.getLeadArtist();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getLeadArtist();
			}
		}
		return "";
	}

	/**
	 * 此方法返回歌曲的专辑信息
	 * 
	 * @throws TagException
	 */
	public String getMusicAlbum() {
		if (file != null) {
			if (id3v2!=null) {
				if(id3v2.getAlbumTitle()==null|| id3v2.getAlbumTitle().equals("")){
					if (file.hasID3v1Tag()) {
						return file.getID3v1Tag().getAlbumTitle();
					}
				}
				return id3v2.getAlbumTitle();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getAlbumTitle();
			}
		}
		return "";
	}

	/**
	 * 此方法返回歌曲内嵌的歌词信息
	 * 
	 * @param mi
	 * @return
	 */
	public String getMusicLyric() {
		if (file != null) {
			if (file.hasLyrics3Tag()) {
				return file.getLyrics3Tag().getSongLyric();
			}
		}
		return null;
	}

	public String getMusicTrackNumber() {
		if (file != null) {
			if (id3v2!=null) {
				return id3v2.getTrackNumberOnAlbum();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getTrackNumberOnAlbum();
			}
		}
		return "";
	}

	public String getMusicYear() {
		if (file != null) {
			if (id3v2!=null) {
				if(id3v2.getYearReleased()==null|| id3v2.getYearReleased().equals("")){
					if (file.hasID3v1Tag()) {
						return file.getID3v1Tag().getYearReleased();
					}
				}
				return id3v2.getYearReleased();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getYearReleased();
			}
		}
		return "";
	}

	public String getMusicGenre() {
		if (file != null) {
			if (id3v2!=null) {
				if(id3v2.getSongGenre()==null|| id3v2.getSongGenre().equals("")){
					if (file.hasID3v1Tag()) {
						return file.getID3v1Tag().getSongGenre();
					}
				}
				return id3v2.getSongGenre();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getSongGenre();
			}
		}
		return "";
	}

	public long getMusicPlayTime() {
		return PlayerUtil.readMp3Time(music.getPath());
	}

	public long getMusicSize() {
		return new File(music.getPath()).length();
	}

	public long getMusicBitRate() {
		return PlayerUtil.readMp3Bitrate(music.getPath());
	}

	public String getMusicComment() {
		if (file != null) {
			if (id3v2!=null) {
				if(id3v2.getSongComment()==null|| id3v2.getSongComment().equals("")){
					if (file.hasID3v1Tag()) {
						return file.getID3v1Tag().getSongComment();
					}
				}
				return id3v2.getSongComment();
			} else if (file.hasID3v1Tag()) {
				return file.getID3v1Tag().getSongComment();
			}

		}
		return "";
	}

	/**
	 * 在mp3文件中写入id3v2标签
	 * 
	 */
	public void save(Music music) {
		try {
			AbstractID3v2 id3v2 = new ID3v2_3();
			id3v2.setAlbumTitle(music.getAlbum());
			id3v2.setLeadArtist(music.getArtist());
			id3v2.setSongTitle(music.getTitle());
			id3v2.setTrackNumberOnAlbum(music.getTrackNumber());
			id3v2.setYearReleased(music.getYear());
			id3v2.setSongGenre(music.getGenre());
			id3v2.setSongComment(music.getComment());
			if(client.getMusicTableJPopMenu().getFilePropertiesView().getImgPanel().getData()!=null&&client.getMusicTableJPopMenu().getFilePropertiesView().getImgPanel().getData().length!=0){
				id3v2.setFrame(new ID3v2_3Frame(new FrameBodyAPIC((byte)0, "image/jpg", (byte)0, new String(new byte[]{3},"ISO-8859-1"), client.getMusicTableJPopMenu().getFilePropertiesView().getImgPanel().getData())));
				this.id3v2.setFrame(new ID3v2_3Frame(new FrameBodyAPIC((byte)0, "image/jpg", (byte)0, "", client.getMusicTableJPopMenu().getFilePropertiesView().getImgPanel().getData())));
			}
			file.setID3v2Tag(id3v2);
			file.save(TagConstant.MP3_FILE_SAVE_OVERWRITE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		} 
	}

	public void saveLyric(String lyric) {
		// 优先考虑文件是否包含id3v2标签,如果文件包含id3v2Tag则将歌词嵌入到id3v2标签中
		if (file.hasLyrics3Tag()) {// 如果歌曲只有lyrics标签
			AbstractLyrics3 lyrics3 = file.getLyrics3Tag();
			try {
				lyrics3.setSongLyric(new String(lyric));
				RandomAccessFile raf = new RandomAccessFile(new File(music
						.getPath()), "rw");
				lyrics3.overwrite(raf);
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TagException e) {
				e.printStackTrace();
			}
		} else {//如果文件不存在lyrics3标签,也不存在id3v2标签
			Lyrics3v2 lyrics3v2 = new Lyrics3v2();
			RandomAccessFile raf;
			try {
				lyrics3v2.setSongLyric(lyric);
				raf = new RandomAccessFile(new File(music.getPath()), "rw");
				lyrics3v2.write(raf);
				raf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
