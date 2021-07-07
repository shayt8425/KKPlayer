package com.zhao.KKPlayer.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		InputStream is = new FileInputStream("F:\\i音乐\\我的下载\\Mr.Mr.-소녀시대.lrc");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while((len=is.read(buff))!=-1){
			baos.write(buff,0,len);
		}
		is.close();
		String str = new String(baos.toByteArray());
		System.out.println(str);
	}

}
