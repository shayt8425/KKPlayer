package com.zhao.KKPlayer.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	
	private byte[] value;
	private Image img;
	
	public ImagePanel(byte[] data) {
		// TODO Auto-generated constructor stub
		this.value = data;
		
	}
	
	public byte[] getData(){
		return this.value;
	}
	
	public ImagePanel(String fileName) {
		// TODO Auto-generated constructor stub
		this.value = getByteDataByFile(fileName);
	}
	
	public byte[] getByteDataByFile(String fileName){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buff = new byte[1024];
			while((len = fis.read(buff))!=-1){
				baos.write(buff,0,len);
			}
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public void setData(String fileName){
		this.value = getByteDataByFile(fileName);
		repaint();
	}
	
	public void setData(byte[] data){
		this.value = data;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if(this.value != null && this.value.length != 0){
			try {
				BufferedImage bi = ImageIO.read(new ByteArrayInputStream(this.value));
				if(bi.getWidth() > 200 || bi.getHeight() > 200){
					double scale = (double)200/bi.getHeight() > (double)200/bi.getWidth() ? (double)200/bi.getWidth() : (double)200/bi.getHeight();
					int targetW = (int) (scale * bi.getWidth());
					int targetH = (int) (scale * bi.getHeight());
					// ???????????????????????????  
			        BufferedImage bTarget = new BufferedImage(targetW, targetH,  
			                BufferedImage.TYPE_3BYTE_BGR);  
			        /// ????????????????????????  
			        AffineTransform transform = new AffineTransform();  
			        // ???????????????????????????  
			        transform.setToScale(scale, scale);  

			        // ??????????????????????????????  
			        AffineTransformOp ato = new AffineTransformOp(transform, null);  
			        // ??????????????????bSrc?????????bTarget  
			        ato.filter(bi, bTarget);  
			        g.drawImage(bTarget, 0, 0, null);
				}else{
					g.drawImage(bi, 0, 0, null);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(img == null){
				img = this.createImage(200, 200);
			}
			Graphics g2d = img.getGraphics();
			g2d.setColor(new Color(0xee,0xee,0xee));
			g2d.fillRect(0, 0, 200, 200);
			g2d.setColor(Color.RED);
			g2d.drawString("????????????", 75, 100);
			g.drawImage(img, 0, 0, null);
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		for (int i = 0; i < "??????????????????".getBytes("GBK").length; i++) {
			System.out.println("??????????????????".getBytes("GBK")[i]);
		}
	}
}
