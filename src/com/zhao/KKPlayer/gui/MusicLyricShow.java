package com.zhao.KKPlayer.gui;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class MusicLyricShow extends Frame {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 350;
	public static final int HEIGHT = 600;
	private Image backgroundImage;
	private Map<Long, String> datas = new HashMap<Long, String>();
	/**
	 * 歌曲时间标签，lyricMap的key
	 */
	private Long[] keys;
	private KKPlayerClient playerClient;
	private String[] lyrics;
	// 每一行歌词的绘制坐标
	private int[] xs;
	/**
	 * 歌词滚动的速度
	 */
	private double speed;
	/**
	 * 歌词滚动的步数
	 */
	private int step;
	
	boolean flag = false;
	
	private LyricShowJPopMenu lyricShowJPopMenu;
	
	public LyricShowJPopMenu getLyricShowJPopMenu(){
		if(lyricShowJPopMenu == null){
			lyricShowJPopMenu = new LyricShowJPopMenu(this,playerClient);
		}
		return lyricShowJPopMenu;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * This is the default constructor
	 */
	public MusicLyricShow(KKPlayerClient playerClient) {
		this.playerClient = playerClient;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLocation(playerClient.getMainFrame().getX()
						+ KKPlayerClient.WIDTH + 20, playerClient
						.getMainFrame().getY());
		this.setSize(WIDTH, HEIGHT);
		this.setBackground(Color.black);
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/images/BigIcon.png")));
		this.setTitle("歌词秀");
		this.setFont(new Font("sss", Font.BOLD, 12));
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				int b = e.getButton();
				if(b==MouseEvent.BUTTON3){
					getLyricShowJPopMenu().show(MusicLyricShow.this, e.getX(), e.getY());
				}
			}
			
		});
		new Thread(new paintThread()).start();
	}

	public void paint(Graphics g) {
		if(flag){
			draw(g);
		}
	}

	public void update(Graphics g) {
		if(flag){
		if (backgroundImage == null) {
			// 创建背景虚拟图片
			backgroundImage = this.createImage(this.getWidth(), this
					.getHeight());
		}
		Graphics imageGraphics = backgroundImage.getGraphics();
		imageGraphics.setColor(new Color(0,52,0));
		imageGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		paint(imageGraphics);
		g.drawImage(backgroundImage, 0, 0, null);}
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		// 如果没有歌词
		if (keys.length == 0) {
			g2d.setColor(Color.cyan);
			g2d.drawString("该歌曲暂无歌词！！！！", 120, 260);
			return;
		}
		for (int i = 0; i < keys.length; i++) {
			// FontRenderContext frc = g2d.getFontRenderContext();
			// //获得要绘制的字符串的边界
			// Rectangle2D stringBounds =
			// this.getFont().getStringBounds(lyrics[i], frc);
			// /*
			// //统计字母及数字的数量
			// int count = 0;
			// //统计冒号与点号的数量
			// int dotCount = 0;
			// //统计数字的数量
			// byte[] lyricBytes = lyrics[i].getBytes();
			// for (int j = 0; j < lyricBytes.length; j++) {
			// if(lyricBytes[j]>=0&&lyricBytes[j]<=127&&lyricBytes[j]!=46&&lyricBytes[j]!=58){
			// count++;
			// }else if(lyricBytes[j]==46||lyricBytes[j]==58){
			// dotCount++;
			// }
			// }
			// */
			// int x = (this.getWidth()-(int)stringBounds.getWidth())/2;
			int y = (int) (200 - (step * speed) + ((i + 1) * 15));
			if (i != keys.length - 1) {
				if ((playerClient.getPlayerManager().getControlThread().getPlayingTime() <= keys[i + 1])
						&& (playerClient.getPlayerManager().getControlThread().getPlayingTime()>= keys[i])) {
					g2d.setColor(new Color(150,254,2));
					g2d.drawString(lyrics[i], xs[i], y);
				} else {
					g2d.setColor(Color.white);
					g2d.drawString(lyrics[i], xs[i], y);
				}
			} else {
				if ((playerClient.getPlayerManager().getControlThread().getPlayingTime() >= keys[i])) {
					g2d.setColor(new Color(150,254,2));
					g2d.drawString(lyrics[i], xs[i], y);
				} else {
					g2d.setColor(Color.white);
					g2d.drawString(lyrics[i], xs[i], y);
				}
			}
			flag = false;
		}
		// 每重画一次歌词滚动一步
		if (!playerClient.getPlayerManager().getControlThread().isPause()
				& !playerClient.getPlayerManager().getControlThread().isStop()) {
			step++;
		}
	}
	
	//刷新界面数据
	public void refreshFrame(Set<Long> keySet) {
		Long[] keys = new Long[keySet.size()];
		int temp = 0;
		for (Iterator<Long> iterator = keySet.iterator(); iterator.hasNext();) {
			Long key = (Long) iterator.next();
			keys[temp] = key;
			temp++;
		}
		// 给时间标签排序
		Arrays.sort(keys);
		this.keys = keys;
		lyrics = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			lyrics[i] = getDatas().get(keys[i]).trim().toLowerCase();
		}
		autoResize();
	}

	public Map<Long, String> getDatas() {
		return datas;
	}

	public void setDatas(Map<Long, String> lyricMap) {
		this.datas = lyricMap;
	}

	/**
	 * 根据绘制的字符串最大像素，来自动调整窗口的大小
	 */
	public void autoResize() {
		xs = new int[keys.length];
		int width = 0;
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		if (g2d != null) {
			FontRenderContext frc = g2d.getFontRenderContext();
			for (int i = 0; i < lyrics.length; i++) {
				Rectangle2D stringBounds = this.getFont().getStringBounds(
						lyrics[i], frc);
				width = Math.max((int) stringBounds.getWidth() + 50, width);
			}
			if (width > WIDTH) {
				this.setSize(width, HEIGHT);
			} else {
				this.setSize(WIDTH, HEIGHT);
			}
			for (int i = 0; i < lyrics.length; i++) {
				Rectangle2D stringBounds = this.getFont().getStringBounds(
						lyrics[i], frc);
				xs[i] = (this.getWidth() - (int) stringBounds.getWidth()) / 2;
			}
			// 创建背景虚拟图片
			backgroundImage = this.createImage(this.getWidth(), this
					.getHeight());
		}
	}

	private class paintThread implements Runnable {

		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				flag = true;
				repaint();
			}
		}
	}

}
