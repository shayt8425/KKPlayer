package com.zhao.KKPlayer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom.Element;

import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.tag.MusicTag;
import com.zhao.KKPlayer.util.PlayerUtil;

public class LyricShowJPopMenu extends JPopupMenu {

	private static final long serialVersionUID = 1596528689593257347L;

	private SearchLyricJDialog searchLyricJDialog;
	private MusicLyricShow musicLyricShow;
	private KKPlayerClient playerClient;
	private JMenuItem associateLyricJMenuItem;
	private JMenuItem reloadLyricJMenuItem;
	private JMenu inLinelyricJMenu;
	private JMenuItem lyricInMusicJMenuItem;
	private JMenuItem delLyricInMusicJMenuItem;
	private JMenuItem saveLyricJMenuItem;
	
	private JMenuItem getSaveLyricJMenuItem(){
		if(saveLyricJMenuItem == null){
			saveLyricJMenuItem = new JMenuItem();
			saveLyricJMenuItem.setText("保存歌词");
			saveLyricJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					Music music = playerClient.getPlayerManager().getPlayingMusic();
					JFileChooser chooser = new JFileChooser(music.getPath().substring(0,music.getPath().lastIndexOf(File.separator)));
					chooser.setMultiSelectionEnabled(false);
					chooser.setFileFilter(new FileNameExtensionFilter("lrc歌词文件", "lrc","LRC"));
					chooser.setDialogTitle("保存歌词");
					int returnVal = chooser.showSaveDialog(musicLyricShow);
					if(returnVal == JFileChooser.APPROVE_OPTION){
						File f = chooser.getSelectedFile();
						String path = f.getAbsolutePath();
						if(path.toLowerCase().indexOf(".lrc")==-1){
							path = path + ".lrc";
						}
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(path);
							String lyric = "[ti:"+music.getTitle()+"]\r\n[ar:"+music.getArtist()+"]\r\n[al:"+music.getAlbum()+"]\r\n\r\n";
							Map<Long, String> datas = musicLyricShow.getDatas();
							
							Object[] keys = datas.keySet().toArray();
							Arrays.sort(keys);
							for (int i = 0; i < keys.length; i++) {
								Long key = Long.parseLong(keys[i].toString());
								lyric = lyric+PlayerUtil.parseLongToTag(key)+datas.get(key)+"\r\n";
							}
							fos.write(lyric.getBytes());
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}finally{
							if(fos != null){
								try {
									fos.flush();
									fos.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
						
					}
				}
			});
		}
		return saveLyricJMenuItem;
	}

	
	private JMenu getinLineJMenu(){
		if(inLinelyricJMenu==null){
			inLinelyricJMenu = new JMenu();
			inLinelyricJMenu.setText("内嵌歌词");
			inLinelyricJMenu.add(getLyricInMusicJMenuItem());
			inLinelyricJMenu.add(getDelLyricJMenuItem());
		}
		return inLinelyricJMenu;
	}
	
	private JMenuItem getDelLyricJMenuItem(){
		if(delLyricInMusicJMenuItem==null){
			delLyricInMusicJMenuItem = new JMenuItem();
			delLyricInMusicJMenuItem.setText("删除内嵌歌词");
			delLyricInMusicJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					Music music = playerClient.getPlayerManager().getPlayingMusic();
					MusicTag tag = PlayerUtil.recogniseMusicTag(music,playerClient);
					tag.saveLyric("");
				}
			});
		}
		return delLyricInMusicJMenuItem;
	}
	
	private JMenuItem getLyricInMusicJMenuItem(){
		if(lyricInMusicJMenuItem==null){
			lyricInMusicJMenuItem = new JMenuItem();
			lyricInMusicJMenuItem.setText("嵌入到音频文件");
			lyricInMusicJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					Music music = playerClient.getPlayerManager().getPlayingMusic();
					music.getTag().saveLyric(music.getLyric());
				}				
			});
		}
		return lyricInMusicJMenuItem;
	}
	
	private JMenuItem getReloadLyricJMenuItem(){
		if(reloadLyricJMenuItem==null){
			reloadLyricJMenuItem = new JMenuItem();
			reloadLyricJMenuItem.setText("重新载入");
			reloadLyricJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					playerClient.getPlayerManager().reloadLyricShow(playerClient.getPlayerManager().getControlThread().getPlayingTime());
				}
			});
		}
		return reloadLyricJMenuItem;
	}
	
	private JMenuItem getAssociateLyricJMenuItem(){
		if(associateLyricJMenuItem==null){
			associateLyricJMenuItem = new JMenuItem();
			associateLyricJMenuItem.setText("关联歌词");
			associateLyricJMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					String path = playerClient.getPlayerManager().getPlayingMusic().getPath();
					JFileChooser jfc = new JFileChooser(path.substring(0,path.lastIndexOf(File.separator)));
					FileNameExtensionFilter filter = new FileNameExtensionFilter("歌词lrc文件", "lrc","LRC");
					jfc.setFileFilter(filter);
					jfc.setMultiSelectionEnabled(false);
					jfc.setDialogTitle("关联歌词");
					int val = jfc.showOpenDialog(musicLyricShow);
					if(val == JFileChooser.APPROVE_OPTION){
						File f = jfc.getSelectedFile();
						try {
							InputStream is = new FileInputStream(f);
							byte[] buff = new byte[1024];
							int len = 0;
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while((len=is.read(buff))!=-1){
								baos.write(buff, 0, len);
							}
							is.close();
							String lyric = new String(baos.toByteArray(),"UTF-8");
							//保存歌词与文件的相关性
							playerClient.getPlayerDaoManager().setMusicLyricPath(playerClient.getPlayerManager().getPlayingMusic().getId(), f.getAbsolutePath());
							//刷新歌词界面
							playerClient.getPlayerManager().reloadLyricShow(playerClient.getPlayerManager().getControlThread().getPlayingTime(), lyric);
							
							playerClient.getPlayerManager().getPlayingMusic().setLyricSetted(true);
							playerClient.getPlayerManager().getPlayingMusic().setLyric(lyric);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
					}
				}
			});
		}
		return associateLyricJMenuItem;
	}

	public SearchLyricJDialog getSearchLyricJDialog() {
		if (searchLyricJDialog == null) {
			searchLyricJDialog = new SearchLyricJDialog(musicLyricShow,playerClient, true);
		}
		return searchLyricJDialog;
	}

	public LyricShowJPopMenu(MusicLyricShow musicLyricShow,
			KKPlayerClient playerClient) {
		this.add(getSearchJMenuItem());
		this.add(getAssociateLyricJMenuItem());
		this.add(getReloadLyricJMenuItem());
		this.add(getinLineJMenu());
		this.add(getSaveLyricJMenuItem());
		this.addPopupMenuListener(new PopupMenuListener() {
			
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if(LyricShowJPopMenu.this.musicLyricShow.getDatas().size()==0){
					getSaveLyricJMenuItem().setEnabled(false);
				}else{
					getSaveLyricJMenuItem().setEnabled(true);
				}
			}
			
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				
			}
			
			public void popupMenuCanceled(PopupMenuEvent e) {
				
			}
		});
		this.musicLyricShow = musicLyricShow;
		this.playerClient = playerClient;
	}

	private JMenuItem searchJMenuItem;

	private JMenuItem getSearchJMenuItem() {
		if (searchJMenuItem == null) {
			searchJMenuItem = new JMenuItem();
			searchJMenuItem.setText("在线搜索");
			searchJMenuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//设置搜索的关键字
					getSearchLyricJDialog().getArtistJTextField().setText(
							playerClient.getPlayerManager().getPlayingMusic()
									.getArtist());
					getSearchLyricJDialog().getTitleJTextField().setText(
							playerClient.getPlayerManager().getPlayingMusic()
									.getTitle());
					//先将搜索界面保存为这一项置为null
					getSearchLyricJDialog().getSaveAsJTextField().setText("");
					// 打开搜索歌词界面前先清空搜索结果集合
					for (Iterator<Element> i = getSearchLyricJDialog()
							.getLyricSearchResult().getDatas().iterator(); i
							.hasNext();) {
						i.next();
						i.remove();
					}
					getSearchLyricJDialog().getLyricSearchResult()
					.fireTableDataChanged();
					getSearchLyricJDialog().getStateJLable().setText("请按搜索键对相关歌词进行搜索");
					getSearchLyricJDialog().setLocation(musicLyricShow.getX(),
							musicLyricShow.getY() + 100);
					getSearchLyricJDialog().setVisible(true);
				}
			});
		}
		return searchJMenuItem;
	}

}
