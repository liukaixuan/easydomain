/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Administrator
 */
public class NetUtil {

    public static String getPageFromServer(String url) throws Exception{
        DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter("User-Agent", "Mozilla/4.0 (compatible; MSIE 4.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)") ;

        //success
		HttpGet get = new HttpGet(url) ;

		HttpResponse response = httpclient.execute(get);
		HttpEntity entity = response.getEntity();

		String content = HelpUtil.readStream(entity.getContent(), HelpUtil.ENCODE_UTF8) ;
		entity.consumeContent();

//        System.out.println(content) ;
        
        return content ;
    }

}
