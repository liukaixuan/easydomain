/*
 * Title:   TRS Blog
 * URL:     http://www.trs.com.cn
 * Copyright (c) 2006, TRS. All rights reserved.
 * 
 * Created on 2006-3-28 18:01:26, by Chen Liang
 */
package com.guzzservices.easydomain.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;


/**
 * 关闭流/Socket等操作的工具类.<BR>
 * 该类的方法均是在最终清理资源时使用, 那时已经不关心异常, 为调用者方便起见, 因此该类的方法不再抛出异常.
 * 
 * @author liushen
 */
public class CloseUtil {
    
    /**
     * 关闭给定的输入流. <BR>
     * @param inStream
     */
    public static void closeInputStream(InputStream inStream) {
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e) {
               
            }
        }
    }

    /**
     * 关闭给定的输出流. <BR>
     * @param outStream
     */
    public static void closeOutputStream(OutputStream outStream) {
        if (outStream != null) {
            try {
                outStream.close();
            } catch (IOException e) {
                
            }
        }
    }
    
    /**
     * 关闭给定的输出流. <BR>
     * @param outStream
     */
    public static void closeWriter(Writer writer) {
        if (writer != null) {
            try {
            	writer.close();
            } catch (IOException e) {
               
            }
        }
    }

    /**
     * 关闭给定的Socket.
     * @param socket 给定的Socket
     */
    public static void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
               
            }
        }
    }

	public static void closeReader(Reader reader) {
		if (reader != null) {
            try {
            	reader.close();
            } catch (IOException e) {
               
            }
        }
	}

}
