package com.zhao.KKPlayer.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.zhao.KKPlayer.dao.MusicDao;
import com.zhao.KKPlayer.dao.PlayListDao;
import com.zhao.KKPlayer.gui.KKPlayerClient;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.model.PlayList;
import com.zhao.KKPlayer.tag.MusicTag;
import com.zhao.KKPlayer.util.PlayerUtil;
import com.zhao.KKPlayer.util.XmlUtil;

/**
 * 处理音乐数据访问类的相关业务逻辑类
 * 
 * @author Administrator
 * 
 */
public class PlayerDaoManager {

	private PlayListDao playListDao;
	private MusicDao musicDao;
	private KKPlayerClient playerClient;
	/**
	 * 复制或剪切操作时记录的音乐信息
	 */
	List<Music> record;

	public PlayerDaoManager(KKPlayerClient playerClient) {
		playListDao = new PlayListDao();
		musicDao = new MusicDao();
		this.playerClient = playerClient;
	}

	public List<PlayList> getPlayLists() {
		return playListDao.getPlayLists();
	}

	public List<Music> getMusics() {
		return musicDao.getMusics();
	}

	public List<Music> addMusicFile(File[] files, PlayList playList) {
		List<Music> datas = new ArrayList<Music>();
		for (int i = 0; i < files.length; i++) {
			Music music = new Music();
			music.setName(files[i].getName().substring(0,
					files[i].getName().lastIndexOf(".")));
			music.setPath(files[i].getPath());
			MusicTag tag = PlayerUtil.recogniseMusicTag(music,playerClient);
			music.setArtist(tag.getMusicArtist());
			music.setTitle(tag.getMusicTitle());
			music.setAlbum(tag.getMusicAlbum());
			music.setGenre(tag.getMusicGenre());
			music.setTrackNumber(tag.getMusicTrackNumber());
			music.setTotalTime(tag.getMusicPlayTime());
			music.setComment(tag.getMusicComment());
			music.setYear(tag.getMusicYear());
			musicDao.addMusics(music);
			datas.add(music);
			System.out.println("添加文件："+music.getPath()+"成功！");
		}
		try {
			XmlUtil
					.writeXMLFile(musicDao.getDoc(), playList
							.getMusicListPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * 用户选择播放列表的时候改变音乐列表数据的方法
	 */
	public void musicTableValueChange(PlayList playList) {
		musicDao.updateDoc(playList.getMusicListPath());
		playerClient.getMusicTableModel().setMusics(getMusics());
	}

	public void deletePlayList(PlayList playList, int index) {
		playListDao.deletePlayList(playList);
		// 刷新界面中播放列表
		List<PlayList> playLists = playerClient.getPlayListModel()
				.getPlayLists();
		for (Iterator<PlayList> i = playLists.iterator(); i.hasNext();) {
			PlayList pl = (PlayList) i.next();
			if (pl.getId() == playList.getId()) {
				playLists.remove(pl);
				break;
			}
		}
		playerClient.getPlayList().setSelectedIndex(index - 1);
		playerClient.getPlayListModel().deleteRow(index, index);
	}

	public void addPlayList(String name, String musicListPath) {
		PlayList playList = playListDao.addPlayList(name, musicListPath);
		List<PlayList> playLists = playerClient.getPlayListModel()
				.getPlayLists();
		playLists.add(playList);
		playerClient.getPlayListModel().addRow(playLists.size() - 1,
				playLists.size() - 1);
	}

	public void initPlayList() {
		playListDao.init();
	}

	public void renamePlayList(PlayList playList) {
		playListDao.renamePlayList(playList);
	}

	public void copyMusics(int[] musicIndexs) {
		record = new ArrayList<Music>();
		for (int i = musicIndexs.length - 1; i >= 0; i--) {
			Music music = playerClient.getMusicTableModel().getMusics().get(
					musicIndexs[i]);
			record.add(music);
		}
	}

	public void pasteMusics(int index, PlayList playList) {
		for (Iterator<Music> i = record.iterator(); i.hasNext();) {
			Music music = (Music) i.next();
			musicDao.addMusics(music, ((index + 1) * 2) + 1);
		}
		try {
			XmlUtil
					.writeXMLFile(musicDao.getDoc(), playList
							.getMusicListPath());
			musicTableValueChange(playList);
			playerClient.getMusicJTable().setRowSelectionInterval(index + 1,
					index + record.size());
			playerClient.musicTableAutoScroll();
			// 移除记录的所有音乐信息
			for (Iterator<Music> i = record.iterator(); i.hasNext();) {
				i.next();
				i.remove();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteMusics(int[] musicIndexs, PlayList playList) {
		for (int i = 0; i < musicIndexs.length; i++) {
			Music music = playerClient.getMusicTableModel().getMusics().get(
					musicIndexs[i]);
			musicDao.deleteMusic(music);
		}
		try {
			XmlUtil
					.writeXMLFile(musicDao.getDoc(), playList
							.getMusicListPath());
			musicTableValueChange(playList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteWrongMusic(List<Music> musics) {
		List<Music> temps = new ArrayList<Music>();
		for (Iterator<Music> i = musics.iterator(); i.hasNext();) {
			Music music = (Music) i.next();
			if (!new File(music.getPath()).exists()) {
				musicDao.deleteMusic(music);
				temps.add(music);
			}
		}
		musics.removeAll(temps);
		try {
			XmlUtil
					.writeXMLFile(musicDao.getDoc(), musicDao
							.getMusicListPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cutMusics(int[] musicIndexs, PlayList playList) {
		copyMusics(musicIndexs);
		deleteMusics(musicIndexs, playList);
	}

	public String getMusicListPath() {
		return musicDao.getMusicListPath();
	}

	public void renameMusicFile(int[] musicIndexs, String[] parameters) {
		new Thread(new renameFileThread(musicIndexs, parameters)).start();
	}

	public void save(Music music) {
		MusicTag tag = PlayerUtil.recogniseMusicTag(music,playerClient);
		tag.save(music);
		musicDao.updateMusicInformation(music);
		try {
			XmlUtil
					.writeXMLFile(musicDao.getDoc(), musicDao
							.getMusicListPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class renameFileThread implements Runnable {

		private int[] musicIndexs;
		private String[] parameters;

		public renameFileThread(int[] musicIndexs, String[] parameters) {
			this.musicIndexs = musicIndexs;
			this.parameters = parameters;
		}

		public void run() {
			for (int i = 0; i < musicIndexs.length; i++) {
				Music music = playerClient.getMusicTableModel().getMusics()
						.get(musicIndexs[i]);
				String name = "";
				for (int j = 0; j < parameters.length; j++) {
					if (parameters[j].equalsIgnoreCase("Artist")) {
						name += music.getArtist();
					} else if (parameters[j].equalsIgnoreCase("Title")) {
						name += music.getTitle();
					} else if (parameters[j].equalsIgnoreCase("Album")) {
						name += music.getAlbum();
					} else if (parameters[j].equalsIgnoreCase("Year")) {
						name += music.getYear();
					} else if (parameters[j].equalsIgnoreCase("TrackNumber")) {
						name += music.getTrackNumber();
					} else {
						name += parameters[j];
					}
				}
				String oldFilePath = music.getPath();
				// 如果正在播放的歌曲和当前重命名的歌曲视同一歌曲
				if (oldFilePath.equals(playerClient.getPlayerManager()
						.getPlayingMusic().getPath())) {
					playerClient.getPlayerManager().stopMusicFile();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				File file = new File(oldFilePath);
				//找到相应的歌词文件
				File lyricFile = new File(oldFilePath.substring(0, oldFilePath
						.lastIndexOf("."))
						+ ".lrc");
				String suffix = oldFilePath.substring(oldFilePath
						.lastIndexOf("."));
				music.setName(name);
				String newFilePath = oldFilePath.substring(0, oldFilePath
						.lastIndexOf("\\"))
						+ File.separator + name + suffix;
				file.renameTo(new File(newFilePath));
				//如果歌词文件存在，重命名
				if (lyricFile.exists()) {
					lyricFile.renameTo(new File(newFilePath.substring(0,
							newFilePath.lastIndexOf("."))
							+ ".lrc"));
				}
				music.setPath(newFilePath);
				musicDao.updateMusicInformation(music);
			}
			try {
				XmlUtil
						.writeXMLFile(musicDao.getDoc(), musicDao
								.getMusicListPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void setMusicLyricPath(int id,String lyricPath){
		musicDao.setMusicLyricPath(id, lyricPath);
	}
	
	public String getMusicLyricPath(int id){
		return musicDao.getMusicLyricPath(id);
	}

	public void deleteMusicLyricPath(int id) {
		musicDao.deleteMusicLyricPath(id);
	}
}
