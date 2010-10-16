/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.util;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class HelpUtil {
	public static final String ENCODE_UTF8 = "UTF-8" ;
	public static final String ENCODE_GBK = "GBK" ;
	public static final String ENCODE_ISO8859_1 = "ISO-8859-1" ;

	public static String readStream(InputStream in, String encoding) {

		InputStreamReader r = null ;

		StringWriter sw = new StringWriter();
		int chByte = 0;

		try {
			r = new InputStreamReader(in, encoding) ;

			chByte = r.read() ;

			while (chByte != -1) {
				sw.write(chByte);
				chByte = r.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
            try {
                r.close();
            } catch (IOException ex) {
                Logger.getLogger(HelpUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(HelpUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

		return sw.getBuffer().toString() ;
	}

    public static String readSecureFile(File file, String encoding){
        if(file == null) return null ;
        if(!file.exists()) return null ;

        FileInputStream fis ;
        try {
            fis = new FileInputStream(file);
             return readStream(fis, encoding) ;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(HelpUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null ;
    }

    public static void saveSecureFile(File file, String content,String encoding) throws IOException{
        if(!file.exists()){
            file.createNewFile();
        }

        if(content == null){
			content = "" ;
		}

		FileOutputStream fos = null ;
		OutputStreamWriter osw = null ;
		BufferedWriter bw = null ;

		try{
			file.createNewFile() ;

			fos = new FileOutputStream(file) ;
			osw = new OutputStreamWriter(fos, encoding);
	        bw = new BufferedWriter(osw);

	        bw.write(content) ;

		}finally{
			CloseUtil.closeWriter(bw) ;
			CloseUtil.closeWriter(osw) ;
			CloseUtil.closeOutputStream(fos) ;
		}
    }

    public static int string2Int(String str, int defaultValue){
        try{
            return Integer.parseInt(str) ;
        }catch(Exception e){
            return defaultValue ;
        }
    }

	public static boolean isSame(String a, String b){
		if(a == null && b == null) return true ;
		if(a == null) return false ;

		return a.equals(b) ;
	}

    public static boolean notEmpty(String str){
		if(str == null) return false ;
		if(str.trim().length() == 0) return false ;

		return true ;
	}

    /**@return 是否成功打开了给定的URL*/
    public static boolean openURLInSystemBroswer(String url){
        try {
            Desktop top = Desktop.getDesktop() ;
            top.browse(new URI(url));
        } catch (Exception ex) {
            return false ;
        }

        return true ;
    }

    /**@return 是否成功打开了给定的URL*/
    public static boolean openURLInSystemBroswer(URI url){
        try {
            Desktop top = Desktop.getDesktop() ;
            top.browse(url);
        } catch (Exception ex) {
            return false ;
        }

        return true ;
    }

}
