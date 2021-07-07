package com.zhao.KKPlayer.util;

import java.io.UnsupportedEncodingException;

public class TestUTF16 {

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "с╒нд";
          
          for (int i = 0; i < str.getBytes("GBK").length; i++) {
			System.out.println(str.getBytes("GBK")[i]);
		}
          
        String aStr = "abc";
        String bStr = "abc";
        System.out.println(aStr ==bStr);

	}
}
