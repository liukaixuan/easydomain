/**
 * ByteUtils.java created by liu kaixuan(liukaixuan@gmail.com) at 1:44:04 PM on Nov 10, 2008 
 */
package com.guzzservices.easydomain.util;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * @author liu kaixuan(liukaixuan@gmail.com)
 * @date Nov 10, 2008 1:44:04 PM
 */
public class ByteUtils {
	
	public static String keyByte2String(byte[] b){
		return new String(Base64.encodeBase64(b)) ;
	}
	
	public static byte[] string2keyByte(String s){
		return Base64.decodeBase64(s.getBytes()) ;
	}

}
