package com.zhao.KKPlayer.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.zhao.KKPlayer.gui.KKPlayerClient;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.player.MusicPlayer;
import com.zhao.KKPlayer.tag.MusicTag;
import com.zhao.KKPlayer.util.PlayerUtil;

/**
 * 处理音乐播放相关的类
 * 
 * @author Administrator
 * 
 */
public class PlayerManager {

	private KKPlayerClient playerClient;

	/**
	 * 相关的音乐解码器
	 */
	private MusicPlayer musicPlayer;
	/**
	 * 正在播放的音乐
	 */
	private Music playingMusic = new Music();
	/**
	 * 正播放的时间
	 */
	private PlayerTimeControlThread controlThread;
	private boolean singlePlay = false;
	private boolean randomPlay = false;
	/**
	 * 正在播放的歌曲所在播放列表的路径
	 */
	private String playingMusicListPath = "";
	/**
	 * 正在播放的歌曲在歌曲列表的索引
	 */
	private int playingMusicIndex;

	public PlayerTimeControlThread getControlThread() {
		return controlThread;
	}

	public int getPlayingMusicIndex() {
		return playingMusicIndex;
	}

	public boolean isSinglePlay() {
		return singlePlay;
	}

	public void setSinglePlay(boolean singlePlay) {
		this.singlePlay = singlePlay;
	}

	public boolean isRandomPlay() {
		return randomPlay;
	}

	public void setRandomPlay(boolean randomPlay) {
		this.randomPlay = randomPlay;
	}

	public PlayerManager(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
	}

	public Music getPlayingMusic() {
		return playingMusic;
	}

	public Map<Long, String> getPlayingMusicLyric() {
		Map<Long, String> lyricMap = new HashMap<Long, String>();
		if(playingMusic.isLyricSetted()){
			String lyric = playingMusic.getLyric();
			lyricMap = parseMusicLyric(lyric);
			return lyricMap;
		}
		InputStream is = null;
		String lyricPath = playingMusic.getPath().substring(0,
				playingMusic.getPath().lastIndexOf("."))
				+ ".lrc";
		MusicTag musicTag = playingMusic.getTag();
		if(musicTag == null){
			musicTag = PlayerUtil.recogniseMusicTag(playingMusic,playerClient);
		}
		String inLinelyric = musicTag.getMusicLyric();
		String lyric = inLinelyric;
		try {
			if (inLinelyric != null && !inLinelyric.equals("")) {// 优先考虑是否有内嵌歌词
			} else if (new File(lyricPath).exists()) {// 再次考虑歌曲所在文件夹下是否有同名的歌词文件
				is = new FileInputStream(new File(lyricPath));
			} else if (playerClient.getPlayerDaoManager().getMusicLyricPath(// 在判断是否有关联到的歌词
					playingMusic.getId()) != null) {
				is = new FileInputStream(playerClient.getPlayerDaoManager()
						.getMusicLyricPath(playingMusic.getId()));
			}
			if (is != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len =  0 ;
				byte[] buff = new byte[1024];
				while((len = is.read(buff))!=-1){
					baos.write(buff,0,len);
				}
				lyric = new String(baos.toByteArray(),"UTF-8");
			}
			lyricMap = parseMusicLyric(lyric);
			playingMusic.setLyricSetted(true);
			playingMusic.setLyric(lyric);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(playerClient.getMusicLyricShow(),
					"关联的歌词文件不存在，将要删除关联！！！", "错误", JOptionPane.ERROR_MESSAGE);
			playerClient.getPlayerDaoManager().deleteMusicLyricPath(playingMusic.getId());
		} catch (IOException e) {
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
		return lyricMap;
	}
	

	public Map<Long, String> parseMusicLyric(String lyric) {
		Map<Long, String> lyricMap = new HashMap<Long, String>();
		try {
			if (lyric != null && !lyric.equals("")) {
				String[] lyrics = lyric.split("(\\r\\n)|(\\n)");
				Pattern p = Pattern.compile("\\[[\\d:.]+\\]");
				for (int i = 0; i < lyrics.length; i++) {
					Matcher m = p.matcher(lyrics[i]);
					while (m.find()) {
						Long key = PlayerUtil.parseTimeTag(m.group());
						String value = lyrics[i]
								.substring(lyrics[i].lastIndexOf("]") + 1);
						lyricMap.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(playerClient.getMusicLyricShow(),
					"歌词读取错误！！！", "错误", JOptionPane.ERROR_MESSAGE);
		}
		return lyricMap;
	}

	/**
	 * 开始播放音乐
	 * 
	 * @param music
	 *            播放的音乐信息
	 */
	public void playMusicFile(Music music) {
		if (musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer.close();
			musicPlayer = null;
			if (controlThread != null) {
				controlThread.stopThread();
			}
		}
		playerClient.getPlayOrPauseJButton().setIcon(
				new ImageIcon(getClass().getResource("/images/pause.png")));
		playerClient.getPlayOrPauseJButton().setText("暂停");
		try {
			musicPlayer = PlayerUtil.createMusicPlayer(music);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(playerClient.getMainFrame(),
					"播放的文件不存在，或路径错误！！！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		playingMusic = music;
		reloadLyricShow(0);
		playerClient.getMusicLyricShow().getLyricShowJPopMenu()
				.getSearchLyricJDialog().setVisible(false);
		musicPlayer.play();
		playingMusicListPath = playerClient.getPlayerDaoManager()
				.getMusicListPath();
		playingMusicIndex = findPlayingMusicIndex();
		playerClient.getMusicTableModel().fireTableDataChanged();
		if (playingMusic.getArtist() == null||playingMusic.getArtist().equals("")) {
			playerClient.getMainFrame().setTitle(
					"KKPlayer 正在播放 -- " + playingMusic.getName());
		} else {
			playerClient.getMainFrame().setTitle(
					"KKPlayer 正在播放 -- " + playingMusic.getArtist() + " - "
							+ playingMusic.getTitle());
		}
		playerClient.getTrayIcon().setToolTip(
				playerClient.getMainFrame().getTitle());
		// 设置进度条的最大值
		playerClient.getMusicPlayingJSlider().setMaximum(
				new Long(playingMusic.getTotalTime() / 1000).intValue());
		// 设置界面中歌曲播放的总时间
		playerClient.getTotalTimeJLabel().setText(
				PlayerUtil.formatMusicTime(playingMusic.getTotalTime()));
		controlThread = new PlayerTimeControlThread(playingMusic, musicPlayer,
				playerClient);
		controlThread.start();
	}

	public String getPlayingMusicListPath() {
		return playingMusicListPath;
	}

	/**
	 * 暂停音乐
	 */
	public void pauseMusicFile() {
		playerClient.getMainFrame().setTitle("KKPlayer 暂停中.....");
		playerClient.getTrayIcon().setToolTip(
				playerClient.getMainFrame().getTitle());
		playerClient.getPlayOrPauseJButton().setIcon(
				new ImageIcon(getClass().getResource("/images/play.png")));
		playerClient.getPlayOrPauseJButton().setText("继续");
		controlThread.pauseThread();
		musicPlayer.pause();
	}

	/**
	 * 恢复音乐播放
	 */
	public void resumeMusicFile() {
		if (playingMusic.getArtist().equals("")
				|| playingMusic.getArtist() == null) {
			playerClient.getMainFrame().setTitle(
					"KKPlayer 正在播放 -- " + playingMusic.getName());
		} else {
			playerClient.getMainFrame().setTitle(
					"KKPlayer 正在播放 -- " + playingMusic.getArtist() + " - "
							+ playingMusic.getTitle());
		}
		playerClient.getTrayIcon().setToolTip(
				playerClient.getMainFrame().getTitle());
		playerClient.getPlayOrPauseJButton().setIcon(
				new ImageIcon(getClass().getResource("/images/pause.png")));
		playerClient.getPlayOrPauseJButton().setText("暂停");
		controlThread.resumeThread();
		musicPlayer.resume();
	}

	/**
	 * 停止音乐
	 */
	public void stopMusicFile() {
		if (musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer.close();
			musicPlayer = null;
			if (controlThread != null) {
				controlThread.stopThread();
			}
		}
		playerClient.getMainFrame().setTitle("KKplayer 已停止");
		playerClient.getTrayIcon().setToolTip(
				playerClient.getMainFrame().getTitle());
		playerClient.getPlayOrPauseJButton().setIcon(
				new ImageIcon(getClass().getResource("/images/play.png")));
		playerClient.getPlayOrPauseJButton().setText("播放");
	}

	/**
	 * 找到当前播放歌曲在歌曲列表的索引位置的
	 * 
	 * @return
	 */
	public int findPlayingMusicIndex() {
		List<Music> musics = playerClient.getMusicTableModel().getMusics();
		for (int i = 0; i < musics.size(); i++) {
			String path = musics.get(i).getPath();
			int id = musics.get(i).getId();
			// 如果在列表中找到当前歌曲，返回该索引位置
			if (playingMusic != null) {
				if (path.equals(playingMusic.getPath())
						&& playingMusic.getId() == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void nextMusicFile() {
		List<Music> musics = playerClient.getMusicTableModel().getMusics();
		// 判断当前列表是否有歌曲
		if (musics.size() == 0) {
			JOptionPane
					.showMessageDialog(playerClient.getMainFrame(),
							"当前播放列表没有任何歌曲，请添加！！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
		} else {
			for (int i = 0; i < musics.size(); i++) {
				int id = musics.get(i).getId();
				String path = musics.get(i).getPath();
				// 如果找到和当前播放的曲目相同的歌曲而且找到的歌曲不是列表的最后一首歌
				if (path.equals(playingMusic.getPath())
						&& id == playingMusic.getId() && i != musics.size() - 1) {
					Music music = musics.get(i + 1);
					playMusicFile(music);
					break;
				} else if (path.equals(playingMusic.getPath())
						&& id == playingMusic.getId() && i == musics.size() - 1) {
					// 如果找到歌曲且是列表的最后一首歌则播放当前第一首
					Music music = musics.get(0);
					playMusicFile(music);
					break;
				} else if (i == musics.size() - 1
						&& !path.equals(playingMusic.getPath())
						&& id != playingMusic.getId()) {
					// 如果没有找到随机播放列表的某一首歌
					Random random = new Random();
					// 获得随机数
					int r = random.nextInt(musics.size());
					Music music = musics.get(r);
					playMusicFile(music);
				}
			}
			playerClient.musicTableAutoScroll();
		}
	}

	public void prevMusicFile() {
		List<Music> musics = playerClient.getMusicTableModel().getMusics();
		// 判断当前列表是否有歌曲
		if (musics.size() == 0) {
			JOptionPane
					.showMessageDialog(playerClient.getMainFrame(),
							"当前播放列表没有任何歌曲，请添加！！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
		} else {
			for (int i = 0; i < musics.size(); i++) {
				int id = musics.get(i).getId();
				String path = musics.get(i).getPath();
				// 如果找到和当前播放的曲目相同的歌曲而且找到的歌曲不是列表的第一首歌
				if (path.equals(playingMusic.getPath())
						&& id == playingMusic.getId() && i != 0) {
					Music music = musics.get(i - 1);
					playMusicFile(music);
					break;
				} else if (path.equals(playingMusic.getPath())
						&& id == playingMusic.getId() && i == 0) {
					// 如果找到歌曲且是列表的第一首歌则播放当前最后一首
					Music music = musics.get(musics.size() - 1);
					playMusicFile(music);
					break;
				} else if (i == musics.size() - 1
						&& !path.equals(playingMusic.getPath())
						&& id != playingMusic.getId()) {
					// 如果没有找到随机播放列表的某一首歌
					Random random = new Random();
					// 获得随机数
					int r = random.nextInt(musics.size());
					Music music = musics.get(r);
					playMusicFile(music);
				}
			}
			playerClient.musicTableAutoScroll();
		}
	}

	/**
	 * 歌曲的随机播放
	 */
	public void randomPlayMusicFile() {
		List<Music> musics = playerClient.getMusicTableModel().getMusics();
		if (musics.size() == 0) {
			JOptionPane
					.showMessageDialog(playerClient.getMainFrame(),
							"当前播放列表没有任何歌曲，请添加！！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
		} else {
			Random r = new Random();
			int index = r.nextInt(musics.size());
			Music music = musics.get(index);
			playMusicFile(music);
		}
	}

	public void singlePlayMusicFile() {
		List<Music> musics = playerClient.getMusicTableModel().getMusics();
		if (musics.size() == 0) {
			JOptionPane
					.showMessageDialog(playerClient.getMainFrame(),
							"当前播放列表没有任何歌曲，请添加！！", "提示",
							JOptionPane.INFORMATION_MESSAGE);
		} else {
			for (int i = 0; i < musics.size(); i++) {
				int id = musics.get(i).getId();
				String path = musics.get(i).getPath();
				// 如果找到和当前播放的曲目相同的歌曲则直接播放当前歌曲
				if (path.equals(playingMusic.getPath())
						&& id == playingMusic.getId()) {
					Music music = musics.get(i);
					playMusicFile(music);
					break;
				} else if (!path.equals(playingMusic.getPath())
						&& id != playingMusic.getId() && i == musics.size() - 1) {
					// 找到最后一首还是没有找到则随机选择一首歌曲循环播放
					Random r = new Random();
					int index = r.nextInt(musics.size());
					Music music = musics.get(index);
					playMusicFile(music);
				}
			}
		}
	}

	/**
	 * 获得用户重命名时的相关命名方法参数
	 * 
	 * @param text
	 * @return 参数的数组
	 */
	public String[] getFormatParameters(String format) {
		String[] parameters = null;
		if (format != null && !format.trim().equalsIgnoreCase("")) {
			StringTokenizer st = new StringTokenizer(format, "%");
			int count = st.countTokens();
			parameters = new String[count];
			for (int i = 0; i < count; i++) {
				parameters[i] = st.nextToken();
			}
		}
		return parameters;
	}

	/**
	 * 重新载入歌词相关信息
	 */
	public void reloadLyricShow(long playingTime) {
		playerClient.getMusicLyricShow().setDatas(getPlayingMusicLyric());
		playerClient.getMusicLyricShow().refreshFrame(
				playerClient.getMusicLyricShow().getDatas().keySet());
		double time = (double) playingMusic.getTotalTime() / 100;
		double temp = (double) (playerClient.getMusicLyricShow().getDatas()
				.size() * 12 + 200 - 250);
		double speed = temp / time;
		playerClient.getMusicLyricShow().setStep((int) (playingTime / 100));
		playerClient.getMusicLyricShow().setSpeed(speed);
	}
	/**
	 * 如果有歌词传进来，重新载入新的歌词
	 * @param playingTime
	 * @param lyric
	 */
	public void reloadLyricShow(long playingTime,String lyric) {
		playerClient.getMusicLyricShow().setDatas(parseMusicLyric(lyric));
		playerClient.getMusicLyricShow().refreshFrame(
				playerClient.getMusicLyricShow().getDatas().keySet());
		double time = (double) playingMusic.getTotalTime() / 100;
		double temp = (double) (playerClient.getMusicLyricShow().getDatas()
				.size() * 12 + 200 - 250);
		double speed = temp / time;
		playerClient.getMusicLyricShow().setStep((int) (playingTime / 100));
		playerClient.getMusicLyricShow().setSpeed(speed);
	}

}
