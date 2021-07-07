package com.zhao.KKPlayer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.zhao.KKPlayer.model.Music;

public class SendMusicToDiskJMenu extends JMenu {

	private static final long serialVersionUID = -8124641740376667108L;

	private KKPlayerClient playerClient;
	private List<File> files;

	public SendMusicToDiskJMenu(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		this.setText("发送到");
		this.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				update();
			}

		});
	}
	

	public void update() {
		updateFileSystem();
		addJMenuItem();
	}

	private void updateFileSystem() {
		files = new ArrayList<File>();
		File[] disks = File.listRoots();
		for (int i = 0; i < disks.length; i++) {
			if (disks[i].canWrite()) {
				files.add(disks[i]);
			}
		}
	}

	private void addJMenuItem() {
		this.removeAll();
		for (Iterator<File> i = files.iterator(); i.hasNext();) {
			final File f = (File) i.next();
			JMenuItem menuItem = new JMenuItem();
			menuItem.setText("磁盘" + f.getPath().substring(0, 1));
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int[] musicIndexs = playerClient.getMusicJTable()
							.getSelectedRows();
					if (musicIndexs.length == 0) {
						JOptionPane.showMessageDialog(playerClient
								.getMainFrame(), "请至少选择一个歌曲，再进行发送操作！！", "警告",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					new Thread(new SendMusicThread(f, musicIndexs)).start();
				}
			});
			this.add(menuItem);
		}
	}
	
	private class SendMusicThread implements Runnable{
		
		/**
		 * 发送的目标文件夹
		 */
		private File f;
		/**
		 * 发送的歌曲在列表的索引位置
		 */
		private int[] musicIndexs;
		
		public SendMusicThread(File f,int[] musicIndexs) {
			this.f = f;
			this.musicIndexs = musicIndexs;
		}

		public void run() {
			String title = playerClient.getMainFrame().getTitle();
			for (int j = 0; j < musicIndexs.length; j++) {
				if(playerClient.getMainFrame().getTitle().indexOf("正在发送文件")==-1){
					title= playerClient.getMainFrame().getTitle();
				}
				Music music = playerClient.getMusicTableModel()
						.getMusics().get(musicIndexs[j]);
				playerClient.getMainFrame().setTitle("正在发送文件："+new File(music.getPath()).getName());
				try {
					InputStream is = new FileInputStream(music
							.getPath());
					OutputStream os = new FileOutputStream(f
							.getAbsolutePath()
							+ music.getPath().substring(
									music.getPath()
											.lastIndexOf("\\")));
					byte[] buf = new byte[1024];
					while (is.read(buf) != -1) {
						os.write(buf);
					}
					is.close();
					os.flush();
					os.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			playerClient.getMainFrame().setTitle(title);
		}
	}

}
