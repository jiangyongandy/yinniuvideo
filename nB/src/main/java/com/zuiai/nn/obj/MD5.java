package com.zuiai.nn.obj;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private MD5() {}
	public static String getPwd(String pwd)
	{
		 try {
				 MessageDigest digest = MessageDigest.getInstance("MD5");
				 byte[] bs = digest.digest(pwd.getBytes());
				 String hexString = "";
				 for (byte b : bs)
				 {
					 int temp = b & 255;
					 if (temp < 16 && temp >= 0)
					 {
						 hexString = hexString + "0" + Integer.toHexString(temp);
					 }
					 else
					 {
						 hexString = hexString + Integer.toHexString(temp);
					 }
				 }
				 return hexString;
			 } catch (NoSuchAlgorithmException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 return "";
	 }


	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
