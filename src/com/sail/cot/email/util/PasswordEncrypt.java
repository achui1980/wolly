package com.sail.cot.email.util;

import org.junit.Test;


/**
 * 密码加密解密
 * @author zhao
 *
 */
public class PasswordEncrypt {
	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String encrypt(String str){
		if(str==null||str.trim().equals(""))
			return null;
		char[] chars  = str.toCharArray();
		char[] enChars = new char[chars.length];
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			c += 3;
			enChars[i] = c;
		}
		return new String(enChars);
	}
	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public static String decrypt(String str){
		if(str==null||str.trim().equals(""))
			return null;
		char[] chars  = str.toCharArray();
		char[] enChars = new char[chars.length];
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			c -= 3;
			enChars[i] = c;
		}
		return new String(enChars);
	}
	@Test
	public void enTest(){
		String enStr = "faith61718191";
		System.out.println(PasswordEncrypt.encrypt(enStr));
	}
	//@Test
	public void deTest(){
		String deStr = "";
		System.out.println(PasswordEncrypt.encrypt(deStr));
	}
}
