package com.zhao.KKPlayer.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.zhao.KKPlayer.gui.KKPlayerClient;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.player.ApePlayer;
import com.zhao.KKPlayer.player.FlacPlayer;
import com.zhao.KKPlayer.player.Mp3Player;
import com.zhao.KKPlayer.player.MusicPlayer;
import com.zhao.KKPlayer.player.OggPlayer;
import com.zhao.KKPlayer.player.WavPlayer;
import com.zhao.KKPlayer.tag.MusicTag;
import com.zhao.KKPlayer.tag.MusicTagApeImpl;
import com.zhao.KKPlayer.tag.MusicTagFlacImpl;
import com.zhao.KKPlayer.tag.MusicTagMp3Impl;
import com.zhao.KKPlayer.tag.MusicTagOggImpl;
import com.zhao.KKPlayer.tag.MusicTagWavImpl;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

/**
 * 提供了播放器功能实现的一些工具类
 * 
 * @author Administrator
 * 
 */
public class PlayerUtil {

	/**
	 * 将读取的mp3时间格式化为mm:ss
	 * 
	 * @param mi
	 *            需要格式化时间的音乐
	 * @return 格式化后的时间
	 */
	public static String formatMusicTime(long musicTime) {
		long l = musicTime;
		int m = (int) (l / 1000 / 60);
		int s = (int) (l / 1000 - (m * 60));
		double time = (double) l / 1000;
		if ((time - (m * 60 + s)) >= 0.5) {
			s = s + 1;
		}
		String ss = Integer.toString(s);
		String mm = Integer.toString(m);
		if (mm.length() == 1) {
			mm = "0" + m;
		}
		if (ss.length() == 1) {
			ss = "0" + ss;
		}
		if (ss.equals("60")) {
			ss = "00";
			m += 1;
		}
		return mm + ":" + ss;
	}
	
	public static String getConfig(String key) throws Exception{
		Properties prop = new Properties();
		FileInputStream configFile = new FileInputStream("file/RecordOfPlayer.properties");
		prop.load(configFile);
		configFile.close();
		return prop.getProperty(key);
	}

	/**
	 * 读一个mp3文件的时间长度
	 * 
	 * @param path
	 *            文件的路径
	 * @return 文件的播放时间的毫秒数
	 */
	public static long readMp3Time(String path) {
		InputStream is = null;
		Bitstream bitstream = null;
		try {
			is = new FileInputStream(path);
			bitstream = new Bitstream(is);
			Header header = bitstream.readFrame();
			long mp3Time = (long) header.total_ms(is.available());
			int mp3IntTIme = (int) (mp3Time / 1000);
			double mp3MsTime = (double) mp3Time % 1000;
			if (mp3MsTime > 500) {
				mp3IntTIme += 1;
			}
			return mp3IntTIme * 1000;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bitstream != null) {
					bitstream.close();
				}
			} catch (BitstreamException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	public static long readMp3Bitrate(String path) {
		InputStream is = null;
		Bitstream bitstream = null;
		try {
			is = new FileInputStream(path);
			bitstream = new Bitstream(is);
			Header header = bitstream.readFrame();
			return (long) header.bitrate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bitstream != null) {
					bitstream.close();
				}
			} catch (BitstreamException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	/**
	 * 识别音乐格式
	 * 
	 * @param mi
	 *            需要识别的音乐
	 * @return 音乐的标签信息类
	 */
	public static MusicTag recogniseMusicTag(Music music,KKPlayerClient client) {
		// 获得文件的后缀
		String suffix = music.getPath().substring(
				music.getPath().lastIndexOf("."));
		MusicTag musicTag = null;
		// /判断文件是属于哪一种类型
		if (suffix.equalsIgnoreCase(".mp3")) {
			musicTag = new MusicTagMp3Impl(music,client);
		} else if (suffix.equalsIgnoreCase(".ape")) {
			musicTag = new MusicTagApeImpl(music,client);
		} else if (suffix.equalsIgnoreCase(".ogg")) {
			musicTag = new MusicTagOggImpl(music);
		} else if (suffix.equalsIgnoreCase(".flac")) {
			musicTag = new MusicTagFlacImpl(music);
		} else if (suffix.equals(".wav")) {
			musicTag = new MusicTagWavImpl(music);
		}
		return musicTag;
	}

	public static MusicPlayer createMusicPlayer(Music music) throws Exception {
		// 得到文件的后缀
		String suffix = music.getPath().substring(
				music.getPath().lastIndexOf("."));
		MusicPlayer musicPlayer = null;
		if (suffix.equalsIgnoreCase(".flac")) {
			InputStream is = new FileInputStream(music.getPath());
			musicPlayer = new FlacPlayer(is);
		} else if (suffix.equalsIgnoreCase(".mp3")) {
			InputStream is = new FileInputStream(music.getPath());
			musicPlayer = new Mp3Player(is);
		} else if (suffix.equalsIgnoreCase(".ape")) {
			musicPlayer = new ApePlayer(music);
		} else if (suffix.equalsIgnoreCase(".ogg")) {
			musicPlayer = new OggPlayer(new File(music.getPath()));
		} else if (suffix.equals(".wav")) {
			musicPlayer = new WavPlayer(music.getPath());
		}
		return musicPlayer;
	}

	/**
	 * 根据歌词的时间标签，将其解析为一个Long型的数字
	 * 
	 * @param timeTag
	 * @return
	 */
	public static Long parseTimeTag(String timeTag) throws Exception{
		String temp = timeTag.substring(1, timeTag.indexOf("]"));
		String mm = temp.split(":")[0];
		String ss = temp.split(":")[1];
		int m = Integer.parseInt(mm);
		double s = Double.parseDouble(ss);
		Long time = new Double(m * 60 * 1000 + s * 1000).longValue();
		return time;
	}

	/**
	 * 根据时间解析为String的歌词中的时间标签
	 * 
	 * @param time
	 * @return
	 */
	public static String parseLongToTag(Long time) {
		int mm = (int) (time / 1000 / 60);
		double ss = (time - (mm * 1000 * 60)) / 1000D;
		String mmStr = mm + "";
		if (mmStr.length() == 1) {
			mmStr = "0" + mmStr;
		}
		String ssStr = ss + "";
		String s = ssStr.split("\\.")[0];
		String ms = ssStr.split("\\.")[1];
		if (s.length() == 1) {
			s = "0" + s;
		}
		if (ms.length() == 1) {
			ms = 0 + ms;
		}
		return "[" + mmStr + ":" + s + "." + ms + "]";
	}

	public static String toUnicodeHexString(String str)
			throws UnsupportedEncodingException {
		byte[] unicodeByte = str.getBytes("unicode");
		String unicodeStr = "";
		for (int i = 3; i < unicodeByte.length; i += 2) {
			String temp = Integer.toHexString(unicodeByte[i]).toUpperCase();
			String temp2 = Integer.toHexString(unicodeByte[i - 1])
					.toUpperCase();
			if (temp.length() > 2) {
				temp = temp.substring(6);
			} else if (temp.length() == 1) {
				temp = "0" + temp;
			}
			if (temp2.length() > 2) {
				temp2 = temp2.substring(6);
			} else if (temp2.length() == 1) {
				temp2 = temp2 + "0";
			}
			unicodeStr += temp + temp2;
		}
		return unicodeStr.toUpperCase();
	}

	/**
	 * 根据艺术家及歌名搜索千千静听歌词服务器上的可用歌词
	 * 
	 * @param artist
	 * @param title
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public static List<Element> searchLyrics(String artist, String title)
			throws Exception {
		List<Element> datas = new ArrayList<Element>();
		String unicodeArtist = PlayerUtil.toUnicodeHexString(artist);
		String unicodeTitle = PlayerUtil.toUnicodeHexString(title);
		String spec = "http://ttlrcct2.qianqian.com/dll/lyricsvr.dll?sh?Artist="
				+ unicodeArtist + "&Title=" + unicodeTitle + "&Flags=0";
		if(getConfig("useProxy").equals("true")){
			Properties prop = System.getProperties();
			prop.put("http.proxyHost", getConfig("proxyIp"));
			prop.put("http.proxyPort", getConfig("proxyPort"));
		}
		URL url = new URL(spec);
		InputStream is = url.openStream();
		byte[] buff = new byte[1024];//使用缓冲字节数组
		int len = 0;//读取的字节数
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while((len = is.read(buff))!=-1){
			baos.write(buff,0,len);
		}
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(new ByteArrayInputStream(baos.toByteArray()));
		is.close();
		datas = XPath.newInstance("//lrc").selectNodes(doc.getRootElement());
		return datas;
	}

	public static String toUtf8HexString(String str)
			throws UnsupportedEncodingException {
		byte[] utf8Byte = str.getBytes("utf-8");
		StringBuilder utf8StringBulder = new StringBuilder();
		for (int i = 0; i < utf8Byte.length; i++) {
			String temp = Integer.toHexString(utf8Byte[i]);
			if (temp.length() > 2) {
				temp = temp.substring(6);
			}
			utf8StringBulder.append(temp);
		}
		return utf8StringBulder.toString().toUpperCase();
	}

	public static String CreateQianQianCode(String artist, String title,
			int lrcId) {
		String qqHexString = null;
		try {
			qqHexString = toUtf8HexString(artist + title);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int length = qqHexString.length() / 2;
		int[] song = new int[length];
		for (int i = 0; i < song.length; i++) {
			song[i] = Integer.parseInt(qqHexString.substring(i * 2, i * 2 + 2),
					16);
		}
		int t1 = 0, t2 = 0, t3 = 0;
		t1 = (lrcId & 0x0000FF00) >> 8;
		if ((lrcId & 0x00FF0000) == 0) {
			t3 = 0x000000FF & ~t1;
		} else {
			t3 = 0x000000FF & ((lrcId & 0x00FF0000) >> 16);
		}

		t3 = t3 | ((0x000000FF & lrcId) << 8);
		t3 = t3 << 8;
		t3 = t3 | (0x000000FF & t1);
		t3 = t3 << 8;
		if ((lrcId & 0xFF000000) == 0) {
			t3 = t3 | (0x000000FF & (~lrcId));
		} else {
			t3 = t3 | (0x000000FF & (lrcId >> 24));
		}

		int j = length - 1;
		while (j >= 0) {
			int c = song[j];
			if (c >= 0x80)
				c = c - 0x100;

			t1 = (int) ((c + t2) & 0x00000000FFFFFFFF);
			t2 = (int) ((t2 << (j % 2 + 4)) & 0x00000000FFFFFFFF);
			t2 = (int) ((t1 + t2) & 0x00000000FFFFFFFF);
			j -= 1;
		}
		j = 0;
		t1 = 0;
		while (j <= length - 1) {
			int c = song[j];
			if (c > 128)
				c = c - 256;
			int t4 = (int) ((c + t1) & 0x00000000FFFFFFFF);
			t1 = (int) ((t1 << (j % 2 + 3)) & 0x00000000FFFFFFFF);
			t1 = (int) ((t1 + t4) & 0x00000000FFFFFFFF);
			j += 1;
		}

		int t5 = (int) Conv(t2 ^ t3);
		t5 = (int) Conv(t5 + (t1 | lrcId));
		t5 = (int) Conv(t5 * (t1 | t3));
		t5 = (int) Conv(t5 * (t2 ^ lrcId));

		long t6 = (long) t5;
		if (t6 > 2147483648L)
			t5 = (int) (t6 - 4294967296L);
		return t5 + "";
	}

	public static long Conv(int i) {
		long r = i % 4294967296L;
		if (i >= 0 && r > 2147483648L)
			r = r - 4294967296L;

		if (i < 0 && r < 2147483648L)
			r = r + 4294967296L;
		return r;
	}
	
	/**
	 * 复制文件或文件夹
	 * @param source 文件源对象
	 * @param dist	复制到的文件目录
	 */
	public static void copyFile(File source,File dist){
		//判断目标路径文件夹是否存在
		if(!dist.exists()){
			dist.mkdir();
		}
		//复制某一个文件后粘贴到目标路径之后生成的文件路径
		String distPath = dist.getAbsolutePath()+File.separator+source.getName();
		//如果源对象文件是一个文件夹
		if(source.isDirectory()){
			//首先在目标路径中创建该文件夹
			new File(distPath).mkdir();
			//列出文件夹下所有的文件
			File[] files = source.listFiles();
			//迭代文件夹下所有的文件，并使用递归调用再次调用本方法
			for (int i = 0; i < files.length; i++) {
				copyFile(files[i], new File(distPath));
			}
		}else{//如果文件不是文件夹则直接复制该文件
			try {
				InputStream is = new FileInputStream(source);
				byte[] buf = new byte[1024];
				OutputStream os = new FileOutputStream(new File(distPath));
				int len = 0;
				while((len=is.read(buf))!=-1){
					os.write(buf,0,len);
				}
				System.out.println("复制文件\""+source.getAbsolutePath()+"\""+"到"+"\""+distPath+"\"完毕");
				is.close();
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
