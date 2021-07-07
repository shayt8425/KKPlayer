package com.zhao.KKPlayer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.model.PlayList;
import com.zhao.KKPlayer.tag.MusicTag;
import com.zhao.KKPlayer.util.PlayerUtil;

/**
 * 音乐列表的右键菜单
 * 
 * @author Administrator
 * 
 */
public class MusicTableJPopMenu extends JPopupMenu {

	private static final long serialVersionUID = 8629860665826145257L;

	private KKPlayerClient playerClient;
	private JMenuItem playJMenuItem;
	private JMenuItem filePropertieJMenuItem;
	private JMenuItem addFileJMenuItem;
	private EditMusicTableJMenu editMusicTableJMenu;
	private JMenuItem browseMusicFileJMenuItem;
	private SendMusicToDiskJMenu sendMusicToDiskJMenu;
	private RenameMusicFileJMenu renameMusicFileJMenu;
	private FilePropertiesView filePropertiesView;
	private JMenuItem delMusicInDiskJMenuItem;
	private JMenuItem delWrongMusicJMenuItem;
	private JMenuItem openLyricShowJMenuItem;
	
	
	private JMenuItem getOpenLyricShowJMenuItem(){
		if(openLyricShowJMenuItem == null){
			openLyricShowJMenuItem = new JMenuItem();
			openLyricShowJMenuItem.setText("打开歌词");
			openLyricShowJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					if(getOpenLyricShowJMenuItem().getText().equals("打开歌词")){
						playerClient.getMusicLyricShow().setStep((int)(playerClient.getPlayerManager().getControlThread().getPlayingTime()/100));
						playerClient.getMusicLyricShow().setVisible(true);
						playerClient.getMusicLyricShow().autoResize();
						playerClient.getMusicLyricShow().toFront();
					}else{
						playerClient.getMusicLyricShow().setVisible(false);
					}
				}
			});
		}
		return openLyricShowJMenuItem;
	}

	private JMenuItem getDelWrongMusicJMenuItem() {
		if (delWrongMusicJMenuItem == null) {
			delWrongMusicJMenuItem = new JMenuItem();
			delWrongMusicJMenuItem.setText("错误的文件");
			delWrongMusicJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					List<Music> musics = playerClient.getMusicTableModel()
							.getMusics();
					playerClient.getPlayerDaoManager().deleteWrongMusic(musics);
					playerClient.getMusicTableModel().fireTableDataChanged();
				}
			});
		}
		return delWrongMusicJMenuItem;
	}

	private JMenuItem getDelMusicInDiskJMenuItem() {
		if (delMusicInDiskJMenuItem == null) {
			delMusicInDiskJMenuItem = new JMenuItem();
			delMusicInDiskJMenuItem.setText("物理删除");
			delMusicInDiskJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					final List<Music> musics = playerClient
							.getMusicTableModel().getMusics();
					final int[] indexs = playerClient.getMusicJTable()
							.getSelectedRows();
					PlayList playList = playerClient.getPlayListModel()
							.getPlayLists().get(
									playerClient.getPlayList()
											.getSelectedIndex());
					new Thread(new Runnable() {

						public void run() {
							for (int i = 0; i < indexs.length; i++) {
								Music music = musics.get(indexs[i]);
								if (music.getPath().equals(
										playerClient.getPlayerManager()
												.getPlayingMusic().getPath())) {
									playerClient.getPlayerManager()
											.stopMusicFile();
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}
								new File(music.getPath()).delete();
							}

						}
					}).start();
					playerClient.getPlayerDaoManager().deleteMusics(indexs,
							playList);
					playerClient.getMusicTableModel().fireTableDataChanged();
				}
			});
		}
		return delMusicInDiskJMenuItem;
	}

	/**
	 * 用户正在浏览的音乐
	 */
	private Music lookingMusic;

	public Music getLookingMusic() {
		return lookingMusic;
	}

	public void setLookingMusic(Music lookingMusic) {
		this.lookingMusic = lookingMusic;
	}

	public FilePropertiesView getFilePropertiesView() {
		if (filePropertiesView == null) {
			filePropertiesView = new FilePropertiesView(playerClient, true);
		}
		return filePropertiesView;
	}

	private RenameMusicFileJMenu getRenameMusicFileJMenu() {
		if (renameMusicFileJMenu == null) {
			renameMusicFileJMenu = new RenameMusicFileJMenu(playerClient);
			renameMusicFileJMenu.setText("重命名文件");
		}
		return renameMusicFileJMenu;
	}

	private SendMusicToDiskJMenu getSendMusicToDiskJMenu() {
		if (sendMusicToDiskJMenu == null) {
			sendMusicToDiskJMenu = new SendMusicToDiskJMenu(playerClient);
		}
		return sendMusicToDiskJMenu;
	}

	public JMenuItem getBrowseMusicFileJMenuItem() {
		if (browseMusicFileJMenuItem == null) {
			browseMusicFileJMenuItem = new JMenuItem();
			browseMusicFileJMenuItem.setText("浏览文件");
			browseMusicFileJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int index = playerClient.getMusicJTable().getSelectedRow();
					Music music = playerClient.getMusicTableModel().getMusics()
							.get(index);
					if (index == -1) {
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请选择一首歌曲，再执行浏览操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					try {
						Runtime.getRuntime().exec(
								"explorer /select," + music.getPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			});
		}
		return browseMusicFileJMenuItem;
	}

	public MusicTableJPopMenu(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		this.add(getPlayJMenuItem());
		this.add(getFilePropertieJMenuItem());
		this.addSeparator();
		this.add(getAddFileJMenuItem());
		this.add(getEditMusicTableJMenu());
		this.add(getBrowseMusicFileJMenuItem());
		this.add(getDelMusicInDiskJMenuItem());
		this.add(getDelWrongMusicJMenuItem());
		this.add(getSendMusicToDiskJMenu());
		this.add(getRenameMusicFileJMenu());
		this.add(getOpenLyricShowJMenuItem());
		this.addPopupMenuListener(new PopupMenuListener() {
			
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if(MusicTableJPopMenu.this.playerClient.getMusicLyricShow().isVisible()){
					getOpenLyricShowJMenuItem().setText("关闭歌词");
				}else{
					getOpenLyricShowJMenuItem().setText("打开歌词");
				}
			}
			
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}
			
			public void popupMenuCanceled(PopupMenuEvent e) {
				
			}
		});
	}

	public EditMusicTableJMenu getEditMusicTableJMenu() {
		if (editMusicTableJMenu == null) {
			editMusicTableJMenu = new EditMusicTableJMenu(playerClient);
		}
		return editMusicTableJMenu;
	}

	public JMenuItem getAddFileJMenuItem() {
		if (addFileJMenuItem == null) {
			addFileJMenuItem = new JMenuItem();
			addFileJMenuItem.setText("添加文件");
			addFileJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.addFileOperation(e);
				}
			});
		}
		return addFileJMenuItem;
	}

	public JMenuItem getPlayJMenuItem() {
		if (playJMenuItem == null) {
			playJMenuItem = new JMenuItem();
			playJMenuItem.setText("播放歌曲");
			playJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					playerClient.playUserChooseOperation();
				}
			});
		}
		return playJMenuItem;
	}

	public JMenuItem getFilePropertieJMenuItem() {
		if (filePropertieJMenuItem == null) {
			filePropertieJMenuItem = new JMenuItem();
			filePropertieJMenuItem.setText("文件属性");
			filePropertieJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					List<Music> musics = playerClient.getMusicTableModel()
							.getMusics();
					Music music = musics.get(playerClient.getMusicJTable()
							.getSelectedRow());
					getFilePropertiesView().getNextjButton().setEnabled(true);
					getFilePropertiesView().getPrevjButton().setEnabled(true);
					lookingMusicPropertie(music);
					getFilePropertiesView().getSavejButton().setEnabled(false);
					getFilePropertiesView().setLocation(
							playerClient.getMainFrame().getX() + 50,
							playerClient.getMainFrame().getY() + 150);
					getFilePropertiesView().setVisible(true);
				}
			});
		}
		return filePropertieJMenuItem;
	}

	public void lookingMusicPropertie(Music music) {
		List<Music> musics = playerClient.getMusicTableModel().getMusics();
		if (musics.indexOf(music) == musics.size() - 1) {
			getFilePropertiesView().getNextjButton().setEnabled(false);
		} else if (musics.indexOf(music) == 0) {
			getFilePropertiesView().getPrevjButton().setEnabled(false);
		}
		lookingMusic = music;
		getFilePropertiesView().getFilePathJTextField()
				.setText(music.getPath());
		getFilePropertiesView().getMusicTitleJTextField().setText(
				music.getTitle());
		getFilePropertiesView().getArtistJTextField()
				.setText(music.getArtist());
		getFilePropertiesView().getAlbumJTextField().setText(music.getAlbum());
		getFilePropertiesView().getTrackJTextField().setText(
				music.getTrackNumber());
		getFilePropertiesView().getGenreJTextField().setText(music.getGenre());
		getFilePropertiesView().getYearJTextField().setText(music.getYear());
		getFilePropertiesView().getCommentJTextArea().setText(
				music.getComment());
		getFilePropertiesView().setTitle("文件属性 -- " + music.getName());
		
		MusicTag tag = music.getTag()!=null?music.getTag():PlayerUtil.recogniseMusicTag(music,playerClient);
		
		getFilePropertiesView().getImgPanel().setData(tag.getPicData());
	}
}
